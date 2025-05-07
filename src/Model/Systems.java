package Model;

import java.util.ArrayList;

public class Systems {
    ArrayList<NetworkSystem> systems;
    ArrayList<Line> Lines;
    public Systems() {
        systems = new ArrayList<>();
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
}
