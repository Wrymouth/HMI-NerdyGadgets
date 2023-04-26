import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddOrderDialog extends JDialog {
    private Order order;
    private ArrayList<Product> products;

    private JList listProducts;
    private OrderPanel pOrder;
    private JButton bAddProduct;
    private JButton bPlaceOrder;

    public AddOrderDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        // setup data
        products = DBMethods.dbFetchAllProducts();
        // setup ui
        setTitle("Nieuw order");
        setSize(300, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);
        listProducts = new JList<String>();
        add(listProducts);
        JLabel lOrder = new JLabel("Order");
        // pOrder = new OrderPanel();
        // add(pOrder);
        bAddProduct = new JButton("Product toevoegen");
        add(bAddProduct);
        bPlaceOrder = new JButton("Order plaatsen");
        add(bPlaceOrder);
    }
}
