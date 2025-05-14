package Model;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import static Model.PortShape.TRIANGLE;
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
    public PortShape getShape(){
        return TRIANGLE;
    }
    @Override
    public ArrayList<Point> getAllPoints(Point center) {
        ArrayList<Point> points = new ArrayList<>();
        int half = 15;

        Polygon triangle = new Polygon();
        triangle.addPoint(center.x, center.y - half); // top
        triangle.addPoint(center.x - half, center.y + half); // bottom-left
        triangle.addPoint(center.x + half, center.y + half); // bottom-right

        Rectangle bounds = triangle.getBounds();
        for (int x = bounds.x; x < bounds.x + bounds.width; x++) {
            for (int y = bounds.y; y < bounds.y + bounds.height; y++) {
                if (triangle.contains(x, y)) {
                    points.add(new Point(x, y));
                }
            }
        }

        return points;
    }


}
