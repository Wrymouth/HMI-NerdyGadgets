import javax.swing.*;
import java.awt.*;

public class OrderPanel extends JPanel {
    private JLabel jlOrderName;
    private Order order;

    public OrderPanel() { //Constructor for when an order has been selected
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        jlOrderName = new JLabel("Geen order geselecteerd!");
        add(jlOrderName);
    }

    public void setOrder(Order order) {
        this.order = order;
        displayOrder();
    }

    private void displayOrder() {
        jlOrderName.setText(order.getName());

        for(Orderline ol : order.getOrderlines()) {
            JLabel jlProductName = new JLabel();
            JLabel jlProductAmount = new JLabel();

            jlProductName.setText(ol.getProduct().getName());
            jlProductAmount.setText(String.valueOf(ol.getAmount()));
        }
    }
}
