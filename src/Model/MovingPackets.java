package Model;

import java.util.ArrayList;

public class MovingPackets{
    ArrayList<Packet> movingPackets;
    public MovingPackets(){
        movingPackets = new ArrayList<>();
    }
    public void addPacket(Packet packet){
        movingPackets.add(packet);
    }
    public ArrayList<Packet> getMovingPackets(){
        return movingPackets;
    }
    public void setMovingPackets(ArrayList<Packet> movingPackets){
        this.movingPackets = movingPackets;
    }
    public void removePacket(Packet packet){
        movingPackets.remove(packet);
    }
}
