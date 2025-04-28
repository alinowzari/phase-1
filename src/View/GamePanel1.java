package View;

import Controller.PortController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel1 extends JPanel {
    private List<NetworkSystem> systems = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private PortController portController;
    private Line tempLine;

    public GamePanel1() {
        setBackground(new Color(67, 61, 61));

        // Example systems
        NetworkSystem S1 = new NetworkSystem(100, 100, 100, 200);
        S1.addPort(new Port(100, 50, PortType.INPUT, PortShape.SQUARE));
        S1.addPort(new Port(100, 100, PortType.INPUT, PortShape.TRIANGLE));
        S1.addPort(new Port(100, 150, PortType.INPUT, PortShape.TRIANGLE));

        NetworkSystem S2 = new NetworkSystem(400, 700, 100, 200);
        S2.addPort(new Port(0, 50, PortType.OUTPUT, PortShape.SQUARE));
        S2.addPort(new Port(0, 100, PortType.OUTPUT, PortShape.TRIANGLE));
        S2.addPort(new Port(100, 50, PortType.INPUT, PortShape.TRIANGLE));

        NetworkSystem S3 = new NetworkSystem(700, 700, 100, 200);
        S3.addPort(new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE));
        S3.addPort(new Port(100, 100, PortType.INPUT, PortShape.SQUARE));

        NetworkSystem S4 = new NetworkSystem(1200, 250, 100, 200);
        S4.addPort(new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE));
        S4.addPort(new Port(0, 100, PortType.OUTPUT, PortShape.SQUARE));
        systems.add(S1);
        systems.add(S2);
        systems.add(S3);
        systems.add(S4);

        // ✅ You must create PortController
        portController = new PortController(this, lines);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (NetworkSystem system : systems) {
            system.draw((Graphics2D) g);
        }

        g.setColor(Color.YELLOW);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Draw permanent lines
        for (Line line : lines) {
            line.draw((Graphics2D) g, this); // Pass the panel to help Line find positions
        }
        // Draw temporary line while dragging
        if (portController.getTempLine() != null) {
            portController.getTempLine().draw(g2, this);
        }
    }

    public List<NetworkSystem> getSystems() {
        return systems;
    }

    public Point getPortCenter(Port port) {
        for (NetworkSystem sys : systems) {
            if (sys.getPorts().contains(port)) {
                Point sysPos = sys.getPosition();
                Point portPos = port.getPosition();

                int centerX = sysPos.x + portPos.x;
                int centerY = sysPos.y + portPos.y;

                if (port.getShape() == PortShape.SQUARE) {
                    if (port.getType() == PortType.OUTPUT) {
                        centerX -= 20; // Move centerX left because OUTPUT square is shifted left
                    }
                    centerX += Port.SIZE / 2;
                    centerY += Port.SIZE / 2;
                }
                else if (port.getShape() == PortShape.TRIANGLE) {
                    if (port.getType() == PortType.OUTPUT) {
                        centerX -= 20; // Triangle is also shifted left
                    }
                    centerX += Port.SIZE / 2;
                    centerY += Port.SIZE / 2;
                }

                return new Point(centerX, centerY);
            }
        }
        return new Point(0, 0);
    }


}
