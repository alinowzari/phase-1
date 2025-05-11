package View;

import Model.Systems;

import javax.swing.*;
import java.awt.*;

public class LengthBoxPanel extends JPanel {
    private double remainingLength;
    private Systems system;

    public LengthBoxPanel( Systems systems) {
        this.remainingLength = systems.maxLineLength;
        setPreferredSize(new Dimension(60, 60));
        setBackground(new Color(30, 30, 30));
        this.system = systems;
    }

    public void updateLength() {
        this.remainingLength = system.maxLineLength-system.getCurrentLineLength();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw box
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // Draw remaining length inside
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        updateLength();
        String text = (int)remainingLength + "";
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g.drawString(text, (getWidth() - textWidth) / 2, (getHeight() + textHeight / 2) / 2);
    }
}
