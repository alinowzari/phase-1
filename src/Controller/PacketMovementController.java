package Controller;

import Model.*;
import View.GamePanel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PacketMovementController {
    private final GamePanel1 panel;
    private final Systems systems;
    private final ArrayList<Line> lines;
    private final ArrayList<Packet> movingPackets = new ArrayList<>();

    public PacketMovementController(GamePanel1 panel, Systems systems, ArrayList<Line> lines) {
        this.panel = panel;
        this.systems = systems;
        this.lines = lines;
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
        Timer timer = new Timer(16, new ActionListener() {
            final Point start = line.getStartPort().getPortCenter();
            final Point end = line.getEndPort().getPortCenter();

            final float speed = 3f;
            final float totalDistance = (float) start.distance(end);
            float traveled = 0f;

            @Override
            public void actionPerformed(ActionEvent e) {
                traveled += speed;

                if (traveled >= totalDistance) {
                    packet.setPosition(end);

                    // ✅ STOP the movement
                    ((Timer) e.getSource()).stop();

                    // ✅ Remove from moving list
                    movingPackets.remove(packet);

                    // ✅ Update startPort reference to the new port
                    Port destinationPort = line.getEndPort();
                    packet.setCurrentLine(null); // movement done
                    packet.setStartPort(destinationPort); // new owner

                    // ✅ Add packet to destination port
                    destinationPort.addPacket(packet);
                    if (destinationPort.getType() == PortType.OUTPUT && destinationPort.getEvenPort() != null) {
                        System.out.println("port change");
                        destinationPort.ChangePort();
                        Port evenPort = destinationPort.getEvenPort();
                        Line nextLine = findLineFromPort(evenPort);

                        if (nextLine != null) {
                            packet.setStartPort(evenPort);     // update to evenPort as new start
                            packet.setCurrentLine(nextLine);
                            startMovement(packet, nextLine);   // recursive movement
                            movingPackets.add(packet);
                            return; // stop here since we're continuing the chain
                        }
                    }
                    // ✅ Repaint UI
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
    public void onLineAdded(Line line) {
        Port startPort = line.getStartPort();

        if (startPort.getType() == PortType.INPUT && !startPort.getPackets().isEmpty()) {
            for (Packet packet : startPort.getPackets()) {
                if (packet.getCurrentLine() == null) {
                    packet.setCurrentLine(line);
                    startMovement(packet, line);
                    movingPackets.add(packet);
                }
            }
            startPort.getPackets().clear(); // optional: clear the port's queue
        }
    }
    public void startAllMovablePackets() {
        for (NetworkSystem system : systems.getSystems()) {
            for (Port port : system.getPorts()) {
                if (port.getType() == PortType.INPUT && !port.getPackets().isEmpty()) {
                    Line line = findLineFromPort(port);
                    if (line != null) {
                        for (Packet packet : port.getPackets()) {
                            if (packet.getCurrentLine() == null) {
                                packet.setCurrentLine(line);
                                startMovement(packet, line);
                                movingPackets.add(packet);
                            }
                        }
                        port.getPackets().clear();
                    }
                }
            }
        }
    }

}
