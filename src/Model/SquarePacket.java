package Model;

import java.awt.*;
import java.util.ArrayList;

public class SquarePacket extends Packet {

    public SquarePacket(Port port, String name) {
        super(port, name);
        this.size = 2;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(position.x - 10, position.y - 10, 10, 10);
    }

    @Override
    public ArrayList<Point> getAllPoints(Point center) {
        ArrayList<Point> points = new ArrayList<>();
        int half = size / 2;

        for (int dx = -half; dx <= half; dx++) {
            for (int dy = -half; dy <= half; dy++) {
                points.add(new Point(center.x + dx, center.y + dy));
            }
        }

        return points;
    }
}
