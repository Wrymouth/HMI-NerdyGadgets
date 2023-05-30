import javax.swing.*;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private Customer customer;

    private JLabel jlCustomerID = new JLabel();
    private JLabel jlCustomerName = new JLabel();
    private JLabel jlCustomerAddress = new JLabel();
    private JLabel jlCustomerZipCode = new JLabel();
    private JLabel jlCustomerCity = new JLabel();

    public CustomerPanel(Customer customer) {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.customer = customer;
        add(new JLabel("Geen klant geselecteerd!"));
    }

    public CustomerPanel() {
        setPreferredSize(new Dimension(200, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Geen klant geselecteerd!"));
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        displayCustomer();
    }
    private void displayCustomer() {
        removeAll();

        //Set data
        add(new JLabel("Klantgegevens:"));
        jlCustomerID.setText("KlantID: " + customer.getCustomerID());
        add(jlCustomerID);
        jlCustomerName.setText(customer.getName());
        add(jlCustomerName);
        jlCustomerAddress.setText(customer.getAddress());
        add(jlCustomerAddress);
        jlCustomerZipCode.setText(customer.getZIPcode());
        add(jlCustomerZipCode);
        jlCustomerCity.setText(customer.getCity());
        add(jlCustomerCity);

        revalidate();
        repaint();
    }
}
