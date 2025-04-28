package Controller;

import Model.*;
import View.GamePanel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PortController implements MouseListener, MouseMotionListener, ActionListener {
    private GamePanel1 panel;
    private Port startPort = null;  // Selected starting port
    private int mouseX, mouseY;
    private ArrayList<Line> lines;
    private Line tempLine;          // 🔥 TEMPORARY LINE for dragging!

    private Timer repaintTimer;

    public PortController(GamePanel1 panel, ArrayList<Line> lines) {
        this.panel = panel;
        this.lines = lines;

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        repaintTimer = new Timer(16, this); // 60 FPS repaint
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

    @Override
    public void mousePressed(MouseEvent e) {
        for (NetworkSystem sys : panel.getSystems()) {
            for (Port port : sys.getPorts()) {
                if (port.isClicked(e.getX(), e.getY(), sys.getPosition().x, sys.getPosition().y)) {
                    startPort = port;
                    tempLine = new Line(startPort); // 🔥 Start a temp line
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

            // Find the closest port to mouse release
            for (NetworkSystem sys : panel.getSystems()) {
                for (Port port : sys.getPorts()) {
                    if (port != startPort) { // Don't connect to self
                        Point portCenter = panel.getPortCenter(port);
                        double dist = Math.hypot(e.getX() - portCenter.x, e.getY() - portCenter.y);

                        if (dist < closestDistance) {
                            closestDistance = dist;
                            closestPort = port;
                        }
                    }
                }
            }

            // Threshold distance (release close enough)
            final double CONNECTION_RADIUS = 20;

            if (closestPort != null && closestDistance <= CONNECTION_RADIUS) {
                if (closestPort.getShape() == startPort.getShape()) {
                    System.out.println("✅ New line created (nearby)");
                    lines.add(new Line(startPort, closestPort));
                } else {
                    System.out.println("❌ Shapes do not match. Cannot connect.");
                }
            } else {
                System.out.println("❌ No port close enough to connect.");
            }
        }

        // Always clear dragging state
        startPort = null;
        tempLine = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        // 🔥 Update the temp line if dragging
        if (tempLine != null) {
            tempLine.setTempEnd(mouseX, mouseY);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
