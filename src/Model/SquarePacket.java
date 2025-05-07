package Model;

import java.awt.*;

public class SquarePacket extends Packet {

    public SquarePacket(Port port) {
        super(port);
        this.size = 2;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(position.x - 10, position.y - 10, 10, 10);
    }
}
