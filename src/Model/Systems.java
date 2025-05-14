package Model;

import java.awt.*;
import java.util.ArrayList;

public class Systems {
    ArrayList<NetworkSystem> systems;
    ArrayList<Line> Lines;
    public double maxLineLength;
    public Systems(double maxLineLength) {
        systems = new ArrayList<>();
        this.maxLineLength = maxLineLength;
    }
    public ArrayList<NetworkSystem> getSystems() {
        return systems;
    }
    public void setLines(ArrayList<Line> Lines) {
        this.Lines = Lines;
    }
    public void setSystems(ArrayList<NetworkSystem> systems) {
        this.systems = systems;
    }
    public void addSystem(NetworkSystem system) {
        systems.add(system);
    }
    public void removeSystem(NetworkSystem system) {
        systems.remove(system);
    }
    public boolean AllPortsConnected() {
        for (NetworkSystem system : systems) {
            ArrayList<Port> ports=system.getPorts();
            for (Port port : ports) {
                boolean connected=false;
                for(Line line:Lines) {
                    if(line.getEndPort().equals(port) || line.getStartPort().equals(port)) {
                        connected=true;
                    }
                }
                if(!connected) {
                    return false;
                }
            }
        }
        return true;
    }
    public double getCurrentLineLength() {
        double total = 0;
        for (Line line : Lines) {
            Point p1 = line.getStartPort().getPortCenter();
            Point p2 = line.getEndPort() != null ? line.getEndPort().getPortCenter() : line.getTempEnd();
            total += p1.distance(p2);
        }
        return total;
    }
    public ArrayList<Packet> getAllPackets() {
        ArrayList<Packet> packets = new ArrayList<>();
        for (NetworkSystem system : systems) {
            for (Port port : system.getPorts()) {
                packets.addAll(port.getPackets());
            }
        }
        return packets;
    }
}
