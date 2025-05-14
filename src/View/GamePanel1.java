package View;

import Controller.*;
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
    private final MovingPackets movingPackets;
    private final CollisionController collisionController;
    private final LengthBoxPanel lengthBox;
    private final CoinCounter coinCounter = new CoinCounter(0);
    private JLabel coinLabel;

    public GamePanel1(Systems systems) {
        this.systems = systems;
        movingPackets = new MovingPackets();
        setBackground(new Color(67, 61, 61));
        setLayout(null); // absolute layout for placing components

        // Length UI box
        setupNetworkSystems();  // 💡 Extracted setup into a helper method

        systems.setLines(lines);
        lengthBox = new LengthBoxPanel(systems);
        lengthBox.setBounds(900, 10, 60, 60);
        coinLabel = new JLabel("Coins: " + coinCounter.getCoin());
        coinLabel.setForeground(Color.WHITE);
        coinLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coinLabel.setBounds(980, 10, 150, 30); // adjust position/size as needed
        add(coinLabel);
        add(lengthBox);

        // Controllers
        packetController = new PacketMovementController(this, systems, lines, movingPackets, coinCounter);
        portController = new PortController(this, systems, lines, packetController);
        lineController = new LineController(this, lines, systems);
        collisionController=new CollisionController(movingPackets, lines);
        buttonController = new ClickButtonController(this, systems, packetController, collisionController);
    }

    private void setupNetworkSystems() {
        // === System S1 ===
        NetworkSystem S1 = new NetworkSystem(100, 100, 100, 200, "first s");
        Port s1_outputSquare= new Port(0, 50, PortType.OUTPUT, PortShape.SQUARE);
        Port s1_outputTriangle = new Port(0, 150, PortType.OUTPUT, PortShape.TRIANGLE);
        Port s1_inputSquare = new Port(100, 50, PortType.INPUT, PortShape.SQUARE);
        Port s1_inputTri1 = new Port(100, 100, PortType.INPUT, PortShape.TRIANGLE);
        Port s1_inputTri2 = new Port(100, 150, PortType.INPUT, PortShape.TRIANGLE);
        Packet firstSquare=new SquarePacket(s1_inputSquare, "first square");
        Packet secondSquare=new SquarePacket(s1_inputTri1, "second square");
        Packet firstTriangle=new TrianglePacket(s1_inputTri1, "first triangle");
        Packet secondTriangle=new TrianglePacket(s1_inputTri2, "second triangle");
        Packet thirdTriangle=new TrianglePacket(s1_inputTri2, "third triangle");
        Packet forthTriangle=new TrianglePacket(s1_inputTri1, "forth triangle");
        s1_inputTri1.addPacket(forthTriangle);
        s1_inputTri2.addPacket(thirdTriangle);
        s1_inputSquare.addPacket(firstSquare);
        s1_inputSquare.addPacket(secondSquare);
        s1_inputTri1.addPacket(firstTriangle);
        s1_inputTri2.addPacket(secondTriangle);
        S1.addPacket(firstSquare);
        S1.addPacket(secondSquare);
        S1.addPacket(thirdTriangle);
        S1.addPacket(forthTriangle);
        S1.addPacket(firstTriangle);
        S1.addPacket(secondTriangle);
        S1.addPort(s1_inputSquare);
        S1.addPort(s1_inputTri1);
        S1.addPort(s1_inputTri2);
        S1.addPort(s1_outputSquare);
        S1.addPort(s1_outputTriangle);
        S1.setHomeSystem();

        // === System S2 ===
        NetworkSystem S2 = new NetworkSystem(400, 700, 100, 200, "second s");
        Port s2_outSquare = new Port(0, 50, PortType.OUTPUT, PortShape.SQUARE);
        Port s2_outTri = new Port(0, 100, PortType.OUTPUT, PortShape.TRIANGLE);
        Port s2_inSquare = new Port(100, 50, PortType.INPUT, PortShape.SQUARE, s2_outSquare);
        Port s2_inTri = new Port(100, 100, PortType.INPUT, PortShape.TRIANGLE, s1_inputTri1);
        s2_outSquare.setEvenPort(s2_inSquare);
        s2_outTri.setEvenPort(s2_inTri);
        S2.addPort(s2_outSquare);
        S2.addPort(s2_outTri);
        S2.addPort(s2_inSquare);
        S2.addPort(s2_inTri);

        // === System S3 ===
        NetworkSystem S3 = new NetworkSystem(700, 700, 100, 200, "third s");
        Port s3_outTri = new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE);
        Port s3_inTri = new Port(100, 50, PortType.INPUT, PortShape.TRIANGLE, s3_outTri);
        s3_outTri.setEvenPort(s3_inTri);
        S3.addPort(s3_outTri);
        S3.addPort(s3_inTri);

        // === System S4 ===
        NetworkSystem S4 = new NetworkSystem(1200, 250, 100, 200, "forth s");
        Port s4_InSquare = new Port(100, 50, PortType.INPUT, PortShape.SQUARE);
        Port s4_InTri = new Port(100, 150, PortType.INPUT, PortShape.TRIANGLE);
        Port s4_outTri1 = new Port(0, 50, PortType.OUTPUT, PortShape.TRIANGLE,s4_InTri);
        Port s4_outSquare = new Port(0, 100, PortType.OUTPUT, PortShape.SQUARE, s4_InSquare);
        Port s4_outTri2 = new Port(0, 150, PortType.OUTPUT, PortShape.TRIANGLE, s4_InTri);
        s4_InSquare.setEvenPort(s4_outSquare);
        S4.addPort(s4_InSquare);
        S4.addPort(s4_InTri);
        S4.addPort(s4_outTri1);
        S4.addPort(s4_outSquare);
        S4.addPort(s4_outTri2);

        systems.addSystem(S1);
        systems.addSystem(S2);
        systems.addSystem(S3);
        systems.addSystem(S4);
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
            if(packet.size>0 && movingPackets.containsPacket(packet)) {
                packet.draw(g2);
            }
        }

        // Draw temp line while dragging
        if (portController.getTempLine() != null) {
            portController.getTempLine().draw(g2);
        }
    }
    public void updateCoinDisplay() {
        coinLabel.setText("Coins: " + coinCounter.getCoin());
    }
}
