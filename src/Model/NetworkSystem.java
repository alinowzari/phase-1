package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Model.PortType.INPUT;
import static Model.PortType.OUTPUT;

public class NetworkSystem {
    private int x, y, width, height;
    private ArrayList<Port> ports;
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Packet> packetOfSystem=new ArrayList<>();
    private boolean isHomeSystem;
    public String name;

    public NetworkSystem(int x, int y, int width, int height, String name) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ports = new ArrayList<>();
        this.name=name;
    }
    public ArrayList<Port> getInputPorts() {
        ArrayList<Port> inputPorts = new ArrayList<>();
        for (Port port : ports) {
            if(port.portType()==INPUT){
                inputPorts.add(port);
            }
        }
        return inputPorts;
    }
    public boolean containsPacket(Packet packet) {
        return packetOfSystem.contains(packet);
    }

    public void setHomeSystem() {
        isHomeSystem = true;
    }
    public boolean isHomeSystem() {
        return isHomeSystem;
    }
    public ArrayList<Port> getOutputPorts() {
        ArrayList<Port> outputPorts = new ArrayList<>();
        for (Port port : ports) {
            if(port.portType()==OUTPUT){
                outputPorts.add(port);
            }
        }
        return outputPorts;
    }
    public void addPort(Port port) {
        port.setParentSystem(this);
        ports.add(port);
    }
    public void addPacket(Packet packet) {
        packetOfSystem.add(packet);
    }
    public void setLines(ArrayList<Line> lines) {
        this.lines = lines;
    }

    public ArrayList<Port> getPorts() {
        return ports;
    }
    public void draw(Graphics2D g) {
        // Draw the box
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Draw header
        g.setColor(new Color(36, 120, 158));
        g.fillRect(x, y, width, height / 10);
        g.setColor(Color.white);
        g.drawRect(x, y, width, height / 10);

        // Draw ports
        for (Port port : ports) {
            port.draw(g, x, y); // Offset by system position
        }

        // ✅ Draw packet count in center
        String countText = String.valueOf(countPackets());
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(countText);
        int textHeight = metrics.getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2;

        g.setColor(Color.BLACK);
        g.drawString(countText, textX, textY);
    }
    public void printPacketContents() {
        System.out.println("📦 System \"" + name + "\" contains the following packets:");
        if (packetOfSystem.isEmpty()) {
            System.out.println("  (no packets)");
        } else {
            for (Packet packet : packetOfSystem) {
                System.out.println("  - " + packet.name);
            }
        }
    }
    public Point getPosition() {
        return new Point(x, y);
    }
    public int countPackets() {
        int count = 0;
        for(Port port : ports) {
            count=count+port.getPackets().size();
        }
        return count;
    }
    public void removePacket(Packet packet) {
        packetOfSystem.remove(packet);
    }
}
