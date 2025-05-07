package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkSystem {
    private int x, y, width, height;
    private ArrayList<Port> ports;
    private ArrayList<Line> lines = new ArrayList<>();

    public NetworkSystem(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ports = new ArrayList<>();
    }

    public void addPort(Port port) {
        port.setParentSystem(this);
        ports.add(port);
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
        String countText = String.valueOf(packetCount());
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(countText);
        int textHeight = metrics.getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height + textHeight) / 2;

        g.setColor(Color.BLACK);
        g.drawString(countText, textX, textY);
    }

//
//    public Rectangle getBounds() {
//        return new Rectangle(x, y, width, height);
//    }
//
    public Point getPosition() {
        return new Point(x, y);
    }
    public int packetCount() {
        int count = 0;
        for (Port port : ports) {
            count +=port.getPackets().size();
        }
        return count;
    }
}
