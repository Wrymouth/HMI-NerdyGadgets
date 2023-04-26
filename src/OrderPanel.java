import javax.swing.*;
import java.awt.*;

public class OrderPanel extends JPanel {
    private Order order;
    
    public OrderPanel() {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}
