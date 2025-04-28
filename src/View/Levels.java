package View;

import javax.swing.*;
import java.awt.*;

public class Levels extends JPanel {
    private JButton level1Button;
    private JButton level2Button;
    private JButton backButton;

    private JFrame parentFrame;   // <-- still need frame to swap panels

    public Levels(JFrame frame) {
        this.parentFrame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        add(createTitleLabel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        add(createBackButton(), BorderLayout.SOUTH);
    }

    private JLabel createTitleLabel() {
        JLabel label = new JLabel("Select a Level", SwingConstants.CENTER);
        label.setFont(new Font("Courier New", Font.BOLD, 36));
        label.setForeground(Color.RED);
        label.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        level1Button = createStyledButton("Level 1");
        level2Button = createStyledButton("Level 2");

        level1Button.setAlignmentX(Component.CENTER_ALIGNMENT);
        level2Button.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(level1Button);
        panel.add(Box.createVerticalStrut(40));
        panel.add(level2Button);
        panel.add(Box.createVerticalGlue());
        level1Button.addActionListener(e -> {
            GameFrame1 level1=new GameFrame1();
            level1.setVisible(true);
            parentFrame.dispose();
        });
        return panel;
    }

    private JPanel createBackButton() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        backButton = createStyledButton("Back");

        backButton.addActionListener(e -> {
            parentFrame.setContentPane(new GameMenu(parentFrame)); // <-- Open a new GameMenu
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        panel.add(backButton);
        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 80));
        button.setMaximumSize(new Dimension(300, 80));
        button.setFont(new Font("Consolas", Font.BOLD, 22));
        button.setBackground(new Color(30, 30, 30));
        button.setForeground(Color.RED);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.RED));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 0, 0));
                button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30));
                button.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
        });

        return button;
    }
}
