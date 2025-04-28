import javax.swing.*;
import java.awt.*;
import View.GameMenu;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Blueprint Hell");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true); // Remove window borders
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
            frame.setVisible(true);

            GameMenu menu = new GameMenu(frame);
            frame.setContentPane(menu);
        });
    }
}
