package Controller;

import Model.Line;
import Model.NetworkSystem;
import Model.Port;
import Model.Systems;
import View.GamePanel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class LineController implements MouseListener {
    private final GamePanel1 panel;
    private final ArrayList<Line> lines;
    private final Systems systems;

    public LineController(GamePanel1 panel, ArrayList<Line> lines, Systems systems) {
        this.panel = panel;
        this.lines = lines;
        this.systems = systems;

        panel.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!SwingUtilities.isLeftMouseButton(e)) return;

        Point click = e.getPoint();

        // Loop through lines and check if clicked near their end port
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            Port endPort = line.getEndPort();
            Point endCenter = endPort.getPortCenter();

            double distance = click.distance(endCenter);
            if (distance <= 12) {  // Use radius to detect click near port
                lines.remove(i);
                System.out.println("🗑 Line removed (clicked end port)");
                panel.repaint();
                return;
            }
        }
    }

    // Unused
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
