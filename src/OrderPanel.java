import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderPanel extends JPanel {
    private JLabel jlOrderName;
    private Order order;
    private Customer customer;

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

    public void setOrder(Order order, Customer customer) {
        this.order = order;
        this.customer = customer;
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
            add(jlProductQuantity);

            jlProductName.setText(ol.getProduct().getName());
            jlProductQuantity.setText(ol.getAmount() + " stuks " + "(voorraad " + ol.getProduct().getQuantity() + ")");

        }

        //Customer data
        CustomerPanel ct = new CustomerPanel();
        ct.setCustomer(customer);
        add(ct);

        revalidate();
        repaint();
    }
}
