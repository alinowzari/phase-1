package Model;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class TrianglePacket extends Packet {

    public TrianglePacket(Port port, String name) {
        super(port, name);
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
    @Override
    public ArrayList<Point> getAllPoints(Point center) {
        ArrayList<Point> points = new ArrayList<>();
        int h = size;

        for (int dy = -h / 2; dy <= h / 2; dy++) {
            int widthAtY = (h / 2) - Math.abs(dy);
            for (int dx = 0; dx <= widthAtY; dx++) {
                points.add(new Point(center.x + dx, center.y + dy));
            }
        }

        return points;
    }

}
