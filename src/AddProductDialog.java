import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AddProductDialog extends JDialog {
    private ArrayList<Product> products;
    
    private JList listProducts;
    private Product selectedProduct;
    private JTextField tAmount;
    private JButton bAdd;

    public AddProductDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        // setup data
        products = DBMethods.fetchAllProducts();
        // setup ui
        setTitle("Product toevoegen");
        setSize(750, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);
        listProducts = new JList<String>();
        add(listProducts);
        JLabel lAmount = new JLabel("Aantal");
        add(lAmount);
        tAmount = new JTextField();
        tAmount.setPreferredSize(new Dimension(50,10));
        add(tAmount);
        bAdd = new JButton("Toevoegen");
        add(bAdd);
    }
}
