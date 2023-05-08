//Class allows for div-like elements to make UI design easier

import javax.swing.*;
import java.awt.*;

public class HMIContainer extends JPanel {
    private String title;
    private JPanel panel;
    public HMIContainer(String title, JPanel panel) {
        this.title = title;
        this.panel = panel;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel(title));
        JPanel HMIpanel = new JPanel();
        add(HMIpanel);

        setVisible(true);
    }

    public HMIContainer(String title, JPanel panel, int width, int height) { //Allows for custom width and height
        this.title = title;
        this.panel = panel;

        setPreferredSize(new Dimension(width, height)); //Set custom width and height
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel(title));
        JPanel HMIpanel = new JPanel();
        add(HMIpanel);

        setVisible(true);
    }

    public String getTitle() {
        return title;
    }

    public JPanel getPanel() {
        return panel;
    }

    public OrderPanel getOrderPanel() {
        return (OrderPanel) panel;
    }

    public BoxesPanel getBoxesPanel() {
        return (BoxesPanel) panel;
    }

    public WarehousePanel getWarehousePanel() {
        return (WarehousePanel) panel;
    }
}
