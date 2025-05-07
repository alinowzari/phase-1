package Controller;

import Model.*;
import View.GamePanel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PortController implements MouseListener, MouseMotionListener, ActionListener {
    private final GamePanel1 panel;
    private final Systems systems;
    private final ArrayList<Line> lines;
    private final PacketMovementController packetController;

    private Port startPort = null;
    private Line tempLine;
    private int mouseX, mouseY;

    private final Timer repaintTimer;

    public PortController(GamePanel1 panel, Systems systems, ArrayList<Line> lines, PacketMovementController packetController) {
        this.panel = panel;
        this.systems = systems;
        this.lines = lines;
        this.packetController = packetController;

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        repaintTimer = new Timer(16, this);
        repaintTimer.start();
    }

    public Line getTempLine() {
        return tempLine;
    }

    public Port getStartPort() {
        return startPort;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
    private boolean isPortConnected(Port port) {
        for (Line line : lines) {
            if (line.getStartPort().equals(port) || line.getEndPort().equals(port)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        for (NetworkSystem sys : systems.getSystems()) {
            for (Port port : sys.getPorts()) {
                if (port.isClicked(e.getX(), e.getY(), sys.getPosition().x, sys.getPosition().y)) {
                    if (isPortConnected(port)) {
                        System.out.println("❌ This port is already connected.");
                        return;
                    }
                    startPort = port;
                    tempLine = new Line(startPort);
                    tempLine.setTempEnd(e.getX(), e.getY());
                    return;
                }
            }
        }
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (startPort != null) {
            Port closestPort = null;
            double closestDistance = Double.MAX_VALUE;

            for (NetworkSystem sys : systems.getSystems()) {
                for (Port port : sys.getPorts()) {
                    if (port != startPort) {
                        Point portCenter = port.getPortCenter();
                        double dist = portCenter.distance(e.getPoint());
                        if (dist < closestDistance) {
                            closestDistance = dist;
                            closestPort = port;
                        }
                    }
                }
            }

            final double CONNECTION_RADIUS = 20;

            if (closestPort != null && closestDistance <= CONNECTION_RADIUS) {
                if (closestPort.getShape() == startPort.getShape()) {
                    Line newLine = new Line(startPort, closestPort);
                    lines.add(newLine);
                    System.out.println("✅ New line created.");

                    // 🔥 Notify packet controller
//                    packetController.onLineAdded(newLine);
                } else {
                    System.out.println("❌ Shapes do not match.");
                }
            } else {
                System.out.println("❌ No port close enough.");
            }
        }

        startPort = null;
        tempLine = null;
    }

    @Override public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        if (tempLine != null) {
            tempLine.setTempEnd(mouseX, mouseY);
        }
    }

    @Override public void actionPerformed(ActionEvent e) { panel.repaint(); }
    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
