import javax.swing.*;
import java.awt.*;

public class OrderPanel extends JPanel {
    private Order order;
    
    public OrderPanel() {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 
    }

    public OrderPanel(Order order) {
        this.order = order;
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JLabel lOrder = new JLabel(order.getName());
        add(lOrder);
    }
}
