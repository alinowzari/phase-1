package View;
import Model.*;
import View.GamePanel1;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameFrame1 extends JFrame {
    public GameFrame1() {
        setTitle("Level 1 - Blueprint Hell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        Systems systems = new Systems();
        GamePanel1 panel = new GamePanel1(systems);
        this.add(panel);
        setVisible(true);
    }
}
