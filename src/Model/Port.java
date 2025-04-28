package Model;

import java.awt.*;

public class Port {
    private int x, y; // Position relative to NetworkSystem
    private PortType type;
    private PortShape shape;

    public static final int SIZE = 20;    // Bigger size
    private static final int CLICK_RADIUS = 12;

    public Port(int x, int y, PortType type, PortShape shape) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.shape = shape;
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

    public Point getPortCenter() {
        return new Point(x, y);
    }

}
