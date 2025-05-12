package Model;

import java.util.ArrayList;

public class MovingPackets {
    private final ArrayList<Packet> movingPackets;

    public MovingPackets() {
        movingPackets = new ArrayList<>();
    }

    public void addPacket(Packet packet) {
        movingPackets.add(packet);
    }

    public ArrayList<Packet> getMovingPackets() {
        return movingPackets;
    }

    // 💥 DO NOT replace the reference, just update contents
    public void setMovingPackets(ArrayList<Packet> newList) {
        movingPackets.clear();
        movingPackets.addAll(newList);
    }

    public void removePacket(Packet packet) {
        movingPackets.remove(packet);
    }
    public boolean containsPacket(Packet packet) {
        return movingPackets.contains(packet);
    }
}
