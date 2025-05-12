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
                if (a.getPosition() == null || b.getPosition() == null) {
                    System.out.println("⚠️ Skipped invalid collision pair: " + a.name + " & " + b.name);
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
                    applyAreaImpact(a, b);
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
    private void applyAreaImpact(Packet a, Packet b) {
        // 1. Get center points
        Point centerA = a.getPosition();
        Point centerB = b.getPosition();

        // 2. Compute the collision center C
        int cx = (centerA.x + centerB.x) / 2;
        int cy = (centerA.y + centerB.y) / 2;
        Point c = new Point(cx, cy);

        // 3. Define radius and push strength
        final int IMPACT_RADIUS = 100;     // Increase for more noticeable range
        final int MAX_PUSH = 25;           // Increase for stronger visible displacement

        System.out.println("💥 Collision at center: " + c);

        // 4. Move a and b directly using their vector from C
        displaceByVector(c, a, MAX_PUSH);
        displaceByVector(c, b, MAX_PUSH);

        // 5. Affect other nearby packets
        for (Packet p : movingPackets) {
            if (p == a || p == b) continue;  // Skip the original two

            Point pos = p.getPosition();
            double dx = pos.x - c.x;
            double dy = pos.y - c.y;
            double dist = Math.hypot(dx, dy);

            if (dist > 0 && dist <= IMPACT_RADIUS) {
                double nx = dx / dist;
                double ny = dy / dist;
                double strength = 1.0 - (dist / IMPACT_RADIUS);

                int moveX = (int) (nx * MAX_PUSH * strength);
                int moveY = (int) (ny * MAX_PUSH * strength);
                Point newPos = new Point(pos.x + moveX, pos.y + moveY);

                System.out.printf("   ↪ %s moved from %s to %s (Δx=%d, Δy=%d)\n",
                        p.name, pos, newPos, moveX, moveY);

                p.setPosition(newPos);
            }
        }
    }

    // 🔧 Utility method to move a packet based on vector from origin point
    private void displaceByVector(Point from, Packet target, int magnitude) {
        Point pos = target.getPosition();
        double dx = pos.x - from.x;
        double dy = pos.y - from.y;
        double dist = Math.hypot(dx, dy);

        if (dist > 0) {
            double nx = dx / dist;
            double ny = dy / dist;

            int moveX = (int) (nx * magnitude);
            int moveY = (int) (ny * magnitude);
            Point newPos = new Point(pos.x + moveX, pos.y + moveY);

            System.out.printf("   👉 Direct move: %s from %s to %s\n", target.name, pos, newPos);

            target.setPosition(newPos);
        }
    }

}
