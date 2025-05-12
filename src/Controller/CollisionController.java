package Controller;

import Model.Line;
import Model.MovingPackets;
import Model.Packet;
import Model.Port;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class CollisionController {
    private final List<Packet> movingPackets;
    private final Map<String, Long> lastCollisionTime = new HashMap<>();
    private static final long COLLISION_COOLDOWN_MS = 300;
    private Timer collisionTimer;
    private MovingPackets moving;
    private ArrayList<Line> lines;

    public CollisionController(MovingPackets movingPackets, ArrayList<Line> lines) {
        this.movingPackets = movingPackets.getMovingPackets();
        this.moving = movingPackets;
        this.lines=lines;
    }

    public void startCollisionLoop() {
        collisionTimer = new Timer(50, e -> detectAndHandleCollisions());
        collisionTimer.start();
    }

    public void stopCollisionLoop() {
        if (collisionTimer != null) {
            collisionTimer.stop();
        }
    }

    private void detectAndHandleCollisions() {
        long now = System.currentTimeMillis();
        Set<Packet> toRemove = new HashSet<>();

        for (int i = 0; i < movingPackets.size(); i++) {
            Packet a = movingPackets.get(i);
            ArrayList<Point> aPoints = a.getAllPoints(a.getPosition());

            for (int j = i + 1; j < movingPackets.size(); j++) {
                Packet b = movingPackets.get(j);
                String key = getKey(a, b);

                if (now - lastCollisionTime.getOrDefault(key, 0L) < COLLISION_COOLDOWN_MS) {
                    continue;
                }

                ArrayList<Point> bPoints = b.getAllPoints(b.getPosition());
                boolean collisionDetected = false;

                for (Point p : bPoints) {
                    if (aPoints.contains(p)) {
                        collisionDetected = true;
                        break;
                    }
                }

                if (collisionDetected) {
                    a.size--;
                    b.size--;
                    lastCollisionTime.put(key, now);
                    System.out.println("this is the size of "+a.name+" "+ a.size);
                    System.out.println("this is the size of "+b.name+" "+ b.size);
                    System.out.println("Collision: " + a.name + " <-> " + b.name);

                    if (a.size <= 0) toRemove.add(a);
                    if (b.size <= 0) toRemove.add(b);
                }
            }
        }

        // Remove after iteration
        for (Packet p : toRemove) {
            fullyRemovePacket(p);
        }
        toRemove.clear();
    }
    private String getKey(Packet a, Packet b) {
        String name1 = a.name;
        String name2 = b.name;
        return name1.compareTo(name2) < 0 ? name1 + "_" + name2 : name2 + "_" + name1;
    }
    public void fullyRemovePacket(Packet p) {
        moving.removePacket(p);
        movingPackets.remove(p);

        // Clear from the port it's currently in
        Port start = p.getStartPort();
        if (start != null) {
            start.removePacket(p);
        }

        // Also remove from current line if needed
        Line currentLine = p.getCurrentLine();
        if (currentLine != null) {
            currentLine.removeMovingPacket();

            // Just to be safe, also remove from both ports
            Port end = currentLine.getEndPort();
            if (end != null) end.removePacket(p);
            if (start != null) start.removePacket(p);
        }

        // Clear references to help with GC and prevent dangling pointers
        p.setCurrentLine(null);
        p.setStartPort(null);
    }
}
