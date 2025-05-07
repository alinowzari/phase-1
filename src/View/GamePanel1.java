package View;

import Controller.ClickButtonController;
import Controller.LineController;
import Controller.PacketMovementController;
import Controller.PortController;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel1 extends JPanel {
    private final ArrayList<Line> lines = new ArrayList<>();
    private final Systems systems;
    private final PortController portController;
    private final LineController lineController;
    private final PacketMovementController packetController;
    private final ClickButtonController buttonController;

    public GamePanel1(Systems systems) {
        this.systems = systems;
        setBackground(new Color(67, 61, 61));

        // System S1
        NetworkSystem S1 = new NetworkSystem(100, 100, 100, 200);
        Port s1_inputSquare1 = new Port(100, 50, PortType.INPUT, PortShape.SQUARE); s1_inputSquare1.setParentSystem(S1);
        Port s1_inputTriangle1 = new Port(100, 100, PortType.INPUT, PortShape.TRIANGLE); s1_inputTriangle1.setParentSystem(S1);
        Port s1_inputTriangle2 = new Port(100, 150, PortType.INPUT, PortShape.TRIANGLE); s1_inputTriangle2.setParentSystem(S1);
        s1_inputSquare1.addPacket(new SquarePacket(s1_inputSquare1));
        s1_inputTriangle1.addPacket(new TrianglePacket(s1_inputTriangle1));
        s1_inputTriangle2.addPacket(new TrianglePacket(s1_inputTriangle2));
        S1.addPort(s1_inputSquare1);
        S1.addPort(s1_inputTriangle1);
        S1.addPort(s1_inputTriangle2);

        // System S2
        NetworkSystem S2 = new NetworkSystem(400, 700, 100, 200);
        Port s2_outputSquare1 = new Port(0, 50, PortType.OUTPUT, PortShape.SQUARE); s2_outputSquare1.setParentSystem(S2);
        Port s2_outputTriangle1 = new Port(0, 100, PortType.OUTPUT, PortShape.TRIANGLE); s2_outputTriangle1.setParentSystem(S2);
        Port s2_inputSquare1 = new Port(100, 50, PortType.INPUT, PortShape.SQUARE, s2_outputSquare1); s2_inputSquare1.setParentSystem(S2);
        Port s2_inputTriangle1 = new Port(100, 100, PortType.INPUT, PortShape.TRIANGLE, s1_inputTriangle1); s2_inputTriangle1.setParentSystem(S2);
        s2_outputSquare1.setEvenPort(s2_inputSquare1);
        s2_outputTriangle1.setEvenPort(s2_inputTriangle1);
        S2.addPort(s2_outputSquare1);
        S2.addPort(s2_outputTriangle1);
        S2.addPort(s2_inputSquare1);
        S2.addPort(s2_inputTriangle1);// formerly inTri22


        // ✅ Add test SquarePacket to output square port;

        // System S3
        NetworkSystem S3 = new NetworkSystem(700, 700, 100, 200);
        Port s3_outputTriangle1 = new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE); s3_outputTriangle1.setParentSystem(S3);
        Port s3_inputTriangle1 = new Port(100, 50, PortType.INPUT, PortShape.TRIANGLE, s3_outputTriangle1); s3_inputTriangle1.setParentSystem(S3);
        s3_outputTriangle1.setEvenPort(s3_inputTriangle1);
        S3.addPort(s3_outputTriangle1);
        S3.addPort(s3_inputTriangle1);


        // System S4
        NetworkSystem S4 = new NetworkSystem(1200, 250, 100, 200);
        Port s4_outputTriangle1 = new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE); s4_outputTriangle1.setParentSystem(S4);
        Port s4_outputSquare1 = new Port(0, 100, PortType.OUTPUT, PortShape.SQUARE); s4_outputSquare1.setParentSystem(S4);
        Port s4_outputTriangle2=new Port(0, 150, PortType.OUTPUT, PortShape.TRIANGLE); s4_outputTriangle2.setParentSystem(S4);
        S4.addPort(s4_outputTriangle1);
        S4.addPort(s4_outputTriangle2);
        S4.addPort(s4_outputSquare1);


        // Add systems to the global model
        systems.addSystem(S1);
        systems.addSystem(S2);
        systems.addSystem(S3);
        systems.addSystem(S4);

        systems.setLines(lines);
        // Controllers
        packetController = new PacketMovementController(this, systems, lines);
        portController = new PortController(this, systems, lines, packetController);
        lineController = new LineController(this, lines, systems);
        buttonController=new ClickButtonController(this, systems, packetController);
        // You no longer need to call launchPackets here — movement happens via `onLineAdded`
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw all systems
        for (NetworkSystem system : systems.getSystems()) {
            system.draw(g2);
        }

        // Draw lines
        g2.setColor(Color.YELLOW);
        g2.setStroke(new BasicStroke(2));
        for (Line line : lines) {
            line.draw(g2);
        }

        // Draw packets
        for (Packet packet : packetController.getMovingPackets()) {
            packet.draw(g2);
        }

        // Draw temp line while dragging
        if (portController.getTempLine() != null) {
            portController.getTempLine().draw(g2);
        }
    }
}
