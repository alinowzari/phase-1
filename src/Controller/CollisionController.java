package Controller;

import Model.MovingPackets;
import Model.Packet;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;

public class CollisionController {
    private final List<Packet> movingPackets;
    private final Map<String, Long> lastCollisionTime = new HashMap<>();
    private static final long COLLISION_COOLDOWN_MS = 300;
    private Timer collisionTimer;

    public CollisionController(MovingPackets movingPackets) {
        this.movingPackets = movingPackets.getMovingPackets();
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

        for (int i = 0; i < movingPackets.size(); i++) {
            Packet a = movingPackets.get(i);
            ArrayList<Point> aPoints = a.getAllPoints(a.getPosition());

            for (int j = i + 1; j < movingPackets.size(); j++) {
                Packet b = movingPackets.get(j);
                String key = getKey(a, b);
                if (now - lastCollisionTime.getOrDefault(key, 0L) < COLLISION_COOLDOWN_MS)
                    continue;

                ArrayList<Point> bPoints = b.getAllPoints(b.getPosition());
                for (Point p : bPoints) {
                    if (aPoints.contains(p)) {
                        // Collision detected
                        a.size--;
                        b.size--;
                        lastCollisionTime.put(key, now);

                        System.out.println("Collision: " + a.name + " <-> " + b.name);

                        // Remove if dead
                        if (a.size <= 0) {
                            movingPackets.remove(a);
                            a.getStartPort().removePacket(a);
                        }
                        if (b.size <= 0) {
                            movingPackets.remove(b);
                            b.getStartPort().removePacket(b);
                        }

                        break;
                    }
                }
            }
        }
    }

    private String getKey(Packet a, Packet b) {
        String name1 = a.name;
        String name2 = b.name;
        return name1.compareTo(name2) < 0 ? name1 + "_" + name2 : name2 + "_" + name1;
    }
}
