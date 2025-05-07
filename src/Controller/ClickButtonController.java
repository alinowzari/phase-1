package Controller;

import Model.Systems;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClickButtonController implements ActionListener {
    private final JButton startButton;
    private boolean startPressed = false;
    private final Systems systems;
    private final PacketMovementController packetMovementController;

    public ClickButtonController(JPanel panel, Systems systems, PacketMovementController packetMovementController) {
        this.systems = systems;
        this.packetMovementController = packetMovementController;

        startButton = new JButton("Start");
        startButton.setBounds(10, 10, 100, 30);
        startButton.addActionListener(this);
        panel.setLayout(null);
        panel.add(startButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (systems.AllPortsConnected()) {
            startPressed = true;
            System.out.println("▶️ Start pressed — ports connected. Starting packet movement.");
            packetMovementController.startAllMovablePackets();  // 🔁 trigger movement
        } else {
            JOptionPane.showMessageDialog(null, "❌ Not all ports are connected.");
        }
    }

    public boolean isStartPressed() {
        return startPressed;
    }
}

