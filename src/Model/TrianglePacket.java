package Model;

import java.awt.*;

public class TrianglePacket extends Packet {

    public TrianglePacket(Port port) {
        super(port);
        this.size = 3;
    }

    @Override
    public void draw(Graphics2D g) {
        int half = 15;

        Polygon triangle = new Polygon();
        triangle.addPoint(position.x, position.y - half); // top
        triangle.addPoint(position.x - half, position.y + half); // bottom-left
        triangle.addPoint(position.x + half, position.y + half); // bottom-right

        g.setColor(Color.BLUE);
        g.fillPolygon(triangle);
    }
}
