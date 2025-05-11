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
    public PacketMovementController(GamePanel1 panel, Systems systems, ArrayList<Line> lines, MovingPackets movingPacket) {
        this.panel = panel;
        this.systems = systems;
        this.lines = lines;
        this.moving = movingPacket;
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
            private Port parentalPort=packet.getStartPort();
            @Override
            public void actionPerformed(ActionEvent e) {
                line.setMovingPacket(packet);
                traveled += speed;
                parentalPort.removePacket(packet);
//                if (traveled >= totalDistance) {
//                    packet.setPosition(end);
//
//                    // ✅ STOP the movement
//                    ((Timer) e.getSource()).stop();
//
//                    // ✅ Remove from moving list
//                    movingPackets.remove(packet);
//                    line.removeMovingPacket();
//                    // ✅ Update startPort reference to the new port
//                    Port destinationPort = line.getEndPort();
//                    packet.setCurrentLine(null); // movement done
//                    packet.setStartPort(destinationPort); // new owner
//
//                    // ✅ Add packet to destination port
//                    destinationPort.addPacket(packet);
//                    if (destinationPort.getType() == PortType.OUTPUT && destinationPort.getEvenPort() != null) {
//                        System.out.println("port change");
//                        destinationPort.ChangePort();
//                        Port evenPort = destinationPort.getEvenPort();
//                        Line nextLine = findLineFromPort(evenPort);
//
//                        if (nextLine != null) {
//                            nextLine.setMovingPacket(packet);
//                            packet.setStartPort(evenPort);
//                            packet.setCurrentLine(nextLine);
//                            startMovement(packet, nextLine);
//                            movingPackets.add(packet);
//                            evenPort.removePacket(packet);
//                            return;
//                        }
//                    }
//                    // ✅ Repaint UI
//                    panel.repaint();
//
//                    return;
//                }
                if (traveled >= totalDistance) {
                    ((Timer) e.getSource()).stop();
                    packet.setPosition(end);
                    movingPackets.remove(packet);
                    moving.setMovingPackets(movingPackets);
                    line.removeMovingPacket();

                    Port destinationPort = line.getEndPort();

// ✅ Remove from previous port
                    Port currentPort = packet.getStartPort();
                    currentPort.removePacket(packet);

// ✅ Update ownership
                    packet.setCurrentLine(null);
                    packet.setStartPort(destinationPort);
                    destinationPort.addPacket(packet);

// 🚀 Try queueing next packet
                    checkAndStartQueuedPackets();

// 🔁 Reroute through evenPort if needed
                    if (destinationPort.getType() == PortType.OUTPUT && destinationPort.getEvenPort() != null) {
                        destinationPort.ChangePort();
                        destinationPort.removePacket(packet); // Important clean-up
                        Port evenPort = destinationPort.getEvenPort();
                        Line nextLine = findLineFromPort(evenPort);

                        if (nextLine != null) {
                            packet.setStartPort(evenPort);
                            packet.setCurrentLine(nextLine);
                            Queue<Packet> nextQueue = packetQueue.computeIfAbsent(nextLine, k -> new LinkedList<>());
                            nextQueue.offer(packet);
                            checkAndStartQueuedPackets();
                        }
                    }
                    parentalPort.removePacket(packet);
                    panel.repaint();
                    return;
                }
                // Move smoothly
                float ratio = traveled / totalDistance;
                int x = (int) (start.x + (end.x - start.x) * ratio);
                int y = (int) (start.y + (end.y - start.y) * ratio);
//                ArrayList<Packet> collisions = getCollidingPackets(packet);
//                if (!collisions.isEmpty()) {
//                    System.out.println(packet.name);
//                    System.out.println("collisions: " + collisions.size());
//                    for (Packet collision : collisions) {
//                        System.out.println("name"+collision.name);
//                    }
//                    packet.size -= collisions.size();
//                    if (packet.size <= 0) {
//                        movingPackets.remove(packet);
//                        packet.getStartPort().removePacket(packet);
//                        ((Timer) e.getSource()).stop();
//                        return;
//                    }
//
//                    for (Packet p : collisions) {
//                        p.size--;
//                        if (p.size <= 0) {
//                            movingPackets.remove(p);
//                            p.getStartPort().removePacket(p);
//                        }
//                    }
//                }
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
//    public void startAllMovablePackets() {
//        for (NetworkSystem system : systems.getSystems()) {
//            for (Port port : system.getPorts()) {
//                if (port.getType() == PortType.INPUT && !port.getPackets().isEmpty()) {
//                    Line line = findLineFromPort(port);
//                    if (line != null && line.getMovingPacket() == null) {
//                        for (Packet packet : port.getPackets()) {
//                            if (packet.getCurrentLine() == null && line.getMovingPacket()==null) {
//                                line.setMovingPacket(packet);
//                                packet.setCurrentLine(line);
//                                startMovement(packet, line);
//                                movingPackets.add(packet);
//                            }
//                        }
//                        port.getPackets().clear();
//                    }
//                }
//            }
//        }
//    }
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
                movingPackets.add(packet);
                moving.setMovingPackets(movingPackets);
                startMovement(packet, line);
            }
        }
    }

//
//    public ArrayList<Packet> getCollidingPackets(Packet target) {
//        ArrayList<Packet> collisions = new ArrayList<>();
//        ArrayList<Point> pointOfTarget = target.getAllPoints(target.getPosition());
//        for (Packet packet : allPackets) {
//            ArrayList<Point> pointOfOthers=packet.getAllPoints(packet.getPosition());
//            for (Point point : pointOfOthers) {
//                if(pointOfTarget.contains(point)) {
//                    collisions.add(packet);
//                    break;
//                }
//            }
//        }
//        return collisions;
//    }
//public ArrayList<Packet> getCollidingPackets(Packet target) {
//    ArrayList<Packet> collisions = new ArrayList<>();
//    ArrayList<Point> targetPoints = target.getAllPoints(target.getPosition());
//
//    long now = System.currentTimeMillis();
//
//    for (Packet other : movingPackets) {
//        if (other == target) continue;
//        if (other.getCurrentLine() == null || target.getCurrentLine() == null) continue;
//
//        // Cooldown check
//        if (lastCollisionTime.containsKey(other) && now - lastCollisionTime.get(other) < COLLISION_COOLDOWN_MS) {
//            continue;
//        }
//
//        ArrayList<Point> otherPoints = other.getAllPoints(other.getPosition());
//
//        for (Point point : otherPoints) {
//            if (targetPoints.contains(point)) {
//                collisions.add(other);
//                lastCollisionTime.put(other, now);
//                break;
//            }
//        }
//    }
//
//    return collisions;
//}
//    private String pairKey(Packet a, Packet b) {
//        String name1 = a.name;
//        String name2 = b.name;
//        return name1.compareTo(name2) < 0 ? name1 + "_" + name2 : name2 + "_" + name1;
//    }
}