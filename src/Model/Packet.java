package Model;

import java.awt.*;
import java.util.ArrayList;

public abstract class Packet {
    protected Point position;
    protected Port startPort;
    protected Line currentLine;  // null until matched
    public int size;
    private Port parentPort;
    public String name;

    public Packet(Port startPort, String name) {
        this.startPort = startPort;
        this.position = new Point(startPort.getPortCenter());
        this.name = name;
    }

    public Port getStartPort() {
        return startPort;
    }
    public void setStartPort(Port startPort) {
        this.startPort = startPort;
    }

    public Line getCurrentLine() {
        return currentLine;
    }

    public void setCurrentLine(Line line) {
        this.currentLine = line;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        this.position = p;
    }
    public abstract void draw(Graphics2D g);
    public abstract ArrayList<Point> getAllPoints(Point center);
}
