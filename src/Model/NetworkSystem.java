package Model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkSystem {
    private int x, y, width, height;
    private List<Port> ports;

    public NetworkSystem(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.ports = new ArrayList<>();
    }

    public void addPort(Port port) {
        ports.add(port);
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void draw(Graphics2D g) {
        // Draw the box
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
        g.setColor(new Color(36, 120, 158));
        g.fillRect(x, y, width, height/10);
        g.setColor(Color.white);
        g.drawRect(x, y, width, height/10);


        // Draw ports
        for (Port port : ports) {
            port.draw(g, x, y); // Offset by system position
        }
    }
//
//    public Rectangle getBounds() {
//        return new Rectangle(x, y, width, height);
//    }
//
    public Point getPosition() {
        return new Point(x, y);
    }
}
