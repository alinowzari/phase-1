package Model;

import java.awt.*;
import java.util.ArrayList;

public class Port {
    private int x, y; // Position relative to NetworkSystem
    private PortType type;
    private PortShape shape;
    private NetworkSystem parentSystem;
    public static final int SIZE = 20;    // Bigger size
    private static final int CLICK_RADIUS = 12;
    private ArrayList<Packet> packets;
    private Port EvenPort;

    public Port(int x, int y, PortType type, PortShape shape) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.shape = shape;
        packets = new ArrayList<>();
    }
    public Port(int x, int y, PortType type, PortShape shape, Port EvenPort) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.shape = shape;
        this.EvenPort = EvenPort;
        packets = new ArrayList<>();

    }
    public void setParentSystem(NetworkSystem parent) {
        this.parentSystem = parent;
    }
    public PortType portType() {
        return type;
    }
    public NetworkSystem getParentSystem() {
        return parentSystem;
    }
    public void setEvenPort(Port EvenPort) {
        this.EvenPort = EvenPort;
    }
    public void addPacket(Packet packet) {
        packets.add(packet);
    }
    public ArrayList<Packet> getPackets() {
        return packets;
    }
    public void draw(Graphics2D g, int offsetX, int offsetY) {
        int drawX = offsetX + x;
        int drawY = offsetY + y;

        g.setColor(shape == PortShape.SQUARE ? Color.YELLOW : Color.GREEN);

        if (shape == PortShape.SQUARE) {
            if (type == PortType.INPUT) {
                g.fillRect(drawX, drawY, SIZE, SIZE);
            }
            if (type == PortType.OUTPUT) {
                g.fillRect(drawX-20, drawY, SIZE, SIZE);
            }
        }
        else if (shape == PortShape.TRIANGLE) {
            if(type==PortType.INPUT) {
                drawTriangle(g, drawX, drawY);
            }
            else if(type==PortType.OUTPUT) {
                drawTriangle2(g, drawX, drawY);
            }
        }
    }

    private void drawTriangle(Graphics2D g, int x, int y) {
        Polygon triangle = new Polygon();

        // ⚡ Manual triangle design
        // This is a triangle pointing RIGHT by default
        triangle.addPoint(x, y);               // top-left
        triangle.addPoint(x, y + SIZE);         // bottom-left
        triangle.addPoint(x + SIZE, y + SIZE / 2); // middle-right (tip)

        g.fillPolygon(triangle);
    }
    private void drawTriangle2(Graphics2D g, int x, int y) {
        Polygon triangle = new Polygon();
        triangle.addPoint(x, y);
        triangle.addPoint(x, y + SIZE);
        triangle.addPoint((x - SIZE), (y+SIZE/2));


        g.fillPolygon(triangle);
    }

    public boolean isClicked(int mouseX, int mouseY, int offsetX, int offsetY) {
        int centerX = offsetX + x + SIZE / 2;
        int centerY = offsetY + y + SIZE / 2;

        double dist = Math.hypot(mouseX - centerX, mouseY - centerY);
        return dist <= CLICK_RADIUS;
    }

    public Point getPosition() {
        return new Point(x, y);
    }

    public PortType getType() {
        return type;
    }

    public PortShape getShape() {
        return shape;
    }
    public Port getEvenPort() {
        return EvenPort;
    }
    public Point getPortCenter() {
        if (parentSystem == null) return new Point(0, 0); // fallback

        Point sysPos = parentSystem.getPosition();
        int centerX = sysPos.x + x;
        int centerY = sysPos.y + y;

        if (shape == PortShape.SQUARE) {
            if (type == PortType.OUTPUT) {
                centerX -= 20;
            }
        } else if (shape == PortShape.TRIANGLE) {
            if (type == PortType.OUTPUT) {
                centerX -= 20;
            }
        }

        centerX += SIZE / 2;
        centerY += SIZE / 2;

        return new Point(centerX, centerY);
    }
    public void ChangePort() {
        for (Packet packet : packets) {
            EvenPort.addPacket(packet);
        }
        packets.clear();
    }
    public void removePacket(Packet packet) {
        packets.remove(packet);
    }
}
