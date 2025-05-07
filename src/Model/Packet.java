package Model;

import java.awt.*;

public abstract class Packet {
    protected Point position;
    protected Port startPort;
    protected Line currentLine;  // null until matched
    protected int size;

    public Packet(Port startPort) {
        this.startPort = startPort;
        this.position = new Point(startPort.getPortCenter());
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
}
