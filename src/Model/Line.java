package Model;
import java.awt.geom.QuadCurve2D;
import View.GamePanel1;
import java.awt.*;

public class Line {
    private Port start;
    private Port end; // can be null if still dragging
    private int tempX, tempY;
    public Packet MovingPacket;// mouse position if dragging
    public Line(Port start) {
        this.start = start;
        this.end = null;
    }

    public Line(Port start, Port end) {
        this.start = start;
        this.end = end;
    }
    public void setMovingPacket(Packet packet) {
        MovingPacket = packet;
    }
    public void removeMovingPacket() {
        MovingPacket = null;
    }
    public Packet getMovingPacket() {
        return MovingPacket;
    }
    public void setTempEnd(int x, int y) {
        this.tempX = x;
        this.tempY = y;
    }
    public Point getTempEnd() {
        return new Point(tempX, tempY);
    }
    public boolean isTemporary() {
        return end == null;
    }

    public Port getStartPort() {
        return start;
    }

    public Port getEndPort() {
        return end;
    }
    public void draw(Graphics2D g) {
        Point startCenter = start.getPortCenter();
        g.setStroke(new BasicStroke(2));

        if (end != null) {
            Point endCenter = end.getPortCenter();
            if(end.getShape()==PortShape.SQUARE){
                g.setColor(Color.YELLOW);
                g.drawLine(startCenter.x, startCenter.y, endCenter.x, endCenter.y);
            }
            else{
                g.setColor(Color.GREEN);
                g.drawLine(startCenter.x, startCenter.y, endCenter.x, endCenter.y);
            }
        } else {
            g.setColor(Color.RED);
            // Temp line while dragging
            g.drawLine(startCenter.x, startCenter.y, tempX, tempY);
        }
    }
}
