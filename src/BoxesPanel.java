import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoxesPanel extends JPanel {
    private ArrayList<Box> boxes;

    public BoxesPanel() {
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.WHITE);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
