import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

public class OrderPanel extends JPanel {
    private JLabel jlOrderName;
    private Order order;

    public OrderPanel() { //Constructor for when an order has been selected
        setPreferredSize(new Dimension(200, 200));
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new FlowLayout());

        jlOrderName = new JLabel("Geen order geselecteerd!");
        add(jlOrderName);
    }

    public void setOrder(Order order) {
        this.order = order;
        displayOrder();

    }

    public Order getOrder() {
        return order;
    }

    private void displayOrder() {
        removeAll();
        ArrayList<Orderline> dbOrderlines = DBMethods.fetchOrderlines(this.order);
        jlOrderName.setText(order.getName());

        for(Orderline ol : dbOrderlines) {
            JLabel jlProductName = new JLabel();
            add(jlProductName);

            JLabel jlProductQuantity = new JLabel();
            Font font = new Font("Courier",Font.ITALIC,12);
            jlProductQuantity.setFont(font);
            add(jlProductQuantity);

            jlProductName.setText(ol.getProduct().getName());
            jlProductQuantity.setText(ol.getAmount() + " stuks " + "(voorraad " + ol.getProduct().getQuantity() + ")");
        }
        revalidate();
        repaint();
    }
}
