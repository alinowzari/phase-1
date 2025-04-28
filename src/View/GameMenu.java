package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMenu extends JPanel {
    private JButton startButton;
    private JButton levelButton;
    private JButton settingsButton;
    private JButton exitButton;
    private static final int TILE_SIZE = 40;

    private JFrame parentFrame;

    public GameMenu(JFrame frame) {
        this.parentFrame = frame;

        setLayout(new BorderLayout());
        initializeButtons();
        add(createLogoPanel(), BorderLayout.NORTH);
        add(createButtonPanel(), BorderLayout.CENTER);
        setBackground(Color.BLACK);

        addListeners(); // <--- all button actions here
    }

    private void addListeners() {
        startButton.addActionListener(e -> {
            System.out.println("Start Game clicked");
        });

        levelButton.addActionListener(e -> {
            System.out.println("Level Select clicked");
            Levels levelPanel = new Levels(parentFrame); // create the level screen
            parentFrame.setContentPane(levelPanel);   // swap the panel
            parentFrame.revalidate();
            parentFrame.repaint();
        });

        settingsButton.addActionListener(e -> {
            System.out.println("Settings clicked");
        });

        exitButton.addActionListener(e -> System.exit(0));
    }
    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel titleLabel = new JLabel("Blueprint Hell");
        titleLabel.setFont(new Font("Courier New", Font.BOLD, 48));
        titleLabel.setForeground(Color.RED);

        logoPanel.add(titleLabel);
        return logoPanel;
    }

    private void initializeButtons() {
        startButton = createStyledButton("Start");
        levelButton = createStyledButton("Level");
        settingsButton = createStyledButton("Settings");
        exitButton = createStyledButton("Exit");
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 100, 0));

        panel.add(Box.createVerticalGlue());

        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(startButton);
        panel.add(Box.createVerticalStrut(25));
        panel.add(levelButton);
        panel.add(Box.createVerticalStrut(25));
        panel.add(settingsButton);
        panel.add(Box.createVerticalStrut(25));
        panel.add(exitButton);

        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Consolas", Font.BOLD, 20));
        button.setPreferredSize(new Dimension(250, 60));
        button.setMaximumSize(new Dimension(250, 60));
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessboardBackground(g);
    }

    private void drawChessboardBackground(Graphics g) {
        int rows = getHeight() / TILE_SIZE + 1;
        int cols = getWidth() / TILE_SIZE + 1;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                boolean isWhite = (x + y) % 2 == 0;
                g.setColor(isWhite ? new Color(20, 20, 20) : new Color(10, 10, 10));
                g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }
}
