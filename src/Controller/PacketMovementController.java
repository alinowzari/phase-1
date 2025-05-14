package Controller;

import Model.*;
import View.GamePanel1;

import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class PacketMovementController {
    private final GamePanel1 panel;
    private final Systems systems;
    private final ArrayList<Line> lines;
    private final ArrayList<Packet> movingPackets = new ArrayList<>();
    private MovingPackets moving;
    private final Map<Line, Queue<Packet>> packetQueue = new HashMap<>();
    private final CoinCounter coinCounter;
    public PacketMovementController(GamePanel1 panel, Systems systems, ArrayList<Line> lines, MovingPackets movingPacket, CoinCounter coinCounter) {
        this.panel = panel;
        this.systems = systems;
        this.lines = lines;
        this.moving = movingPacket;
        this.coinCounter = coinCounter;
    }


    private Line findLineFromPort(Port port) {
        for (Line line : lines) {
            if (line.getStartPort() == port) {
                return line;
            }
        }
        return null;
    }

    private void startMovement(Packet packet, Line line) {
        if(packet.size<=0){
            movingPackets.remove(packet);
            moving.removePacket(packet);
            return;
        }
        if (!movingPackets.contains(packet)) {
            movingPackets.add(packet);
            moving.addPacket(packet);  // <-- ensures the shared object has it too
        }
        Timer timer = new Timer(16, new ActionListener() {
            final Point start = line.getStartPort().getPortCenter();
            final Point end = line.getEndPort().getPortCenter();
            final float speed = 3f;
            final float totalDistance = (float) start.distance(end);
            float traveled = 0f;
            ;
            @Override
            public void actionPerformed(ActionEvent e) {
                line.setMovingPacket(packet);
                traveled += speed;


                if (traveled >= totalDistance) {
                    coinCounter.addCoinAmount(10);
                    panel.updateCoinDisplay();
                    ((Timer) e.getSource()).stop();
                    packet.setPosition(end);
                    movingPackets.remove(packet);
                    moving.setMovingPackets(movingPackets);
                    line.removeMovingPacket();

                    Port destinationPort = line.getEndPort();

// ✅ Remove from previous port
                    Port currentPort = packet.getStartPort();
                    if (currentPort != null) {
                        packet.getStartPort().removePacket(packet);
                    }

// ✅ Update ownership
                    packet.setCurrentLine(null);
                    if (packet.size > 0) {
                        packet.setStartPort(destinationPort);
                        line.getEndPort().addPacket(packet);
                    }

// 🚀 Try queueing next packet
                    checkAndStartQueuedPackets();

// 🔁 Reroute through evenPort if needed
                    if (destinationPort.getType() == PortType.OUTPUT && destinationPort.getEvenPort() != null) {
                        line.getEndPort().ChangePort();
                        line.getEndPort().removePacket(packet); // Important clean-up
                        Port evenPort = line.getEndPort().getEvenPort();
                        Line nextLine = findLineFromPort(evenPort);

                        if (nextLine != null) {
                            packet.setStartPort(evenPort);
                            packet.setCurrentLine(nextLine);
                            Queue<Packet> nextQueue = packetQueue.computeIfAbsent(nextLine, k -> new LinkedList<>());
                            nextQueue.offer(packet);
                            checkAndStartQueuedPackets();
                        }
                    }
                    packet.getStartPort().removePacket(packet);
                    panel.repaint();
                    return;
                }
                // Move smoothly
                float ratio = traveled / totalDistance;
                int x = (int) (start.x + (end.x - start.x) * ratio);
                int y = (int) (start.y + (end.y - start.y) * ratio);
                packet.setPosition(new Point(x, y));
                panel.repaint();
            }
        });
        timer.start();
    }


    public ArrayList<Packet> getMovingPackets() {
        return movingPackets;
    }
    public void startAllMovablePackets() {
        for (NetworkSystem system : systems.getSystems()) {
            for (Port port : system.getPorts()) {
                if (port.getType() == PortType.INPUT && !port.getPackets().isEmpty()) {
                    Line line = findLineFromPort(port);
                    if (line != null) {
                        Queue<Packet> queue = packetQueue.computeIfAbsent(line, k -> new LinkedList<>());
                        for (Packet packet : port.getPackets()) {
                            if (packet.getCurrentLine() == null) {
                                packet.setCurrentLine(line);
                                queue.offer(packet);
                            }
                        }
                        port.getPackets().clear();
                    }
                }
            }
        }

        // Start packets on any available lines
        checkAndStartQueuedPackets();
    }
    private void checkAndStartQueuedPackets() {
        for (Map.Entry<Line, Queue<Packet>> entry : packetQueue.entrySet()) {
            Line line = entry.getKey();
            Queue<Packet> queue = entry.getValue();

            if (line.getMovingPacket() == null && !queue.isEmpty()) {
                Packet packet = queue.poll();
                line.setMovingPacket(packet);
                if (!movingPackets.contains(packet)) {
                    movingPackets.add(packet);
                    moving.addPacket(packet);
                }
                startMovement(packet, line);
            }
        }
    }
}