import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditOrderDialog extends JDialog implements ActionListener {

    private Order order;
    private ArrayList<Product> allProducts;

    private OrderPanel pOrder;
    private ProductListPanel pProductList;
    private JComboBox<String> productChoiceList;
    private JButton bAddProduct;
    private JButton bSave;

    public EditOrderDialog(Frame frame, boolean modal, Order order) {
        super(frame, modal);
        this.order = order;

        allProducts = DBMethods.dbFetchAllProducts();
        // setup ui
        setTitle("Order bewerken");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);
        productChoiceList = new JComboBox<String>();
        for(Product p : DBMethods.dbFetchAllProducts()) {
            productChoiceList.addItem(p.getName());
        }
        add(productChoiceList);
        JLabel lOrder = new JLabel("Order");
        add(lOrder);
        System.out.println(order);
        pProductList = new ProductListPanel(order.getOrderlines(), true);
        add(pProductList);
        bAddProduct = new JButton("Voeg product toe");
        bAddProduct.addActionListener(this);
        add(bAddProduct);
        bSave = new JButton("Opslaan");
        bSave.addActionListener(this);
        add(bSave);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bAddProduct) {
            String selectedValue = String.valueOf(productChoiceList.getSelectedItem()); //Gets value from dropdown
            // get selected product from name
            Product selectedProduct = null;
            for (Product product : allProducts) {
                if (product.getName().equals(selectedValue)) {
                    selectedProduct = product;
                }
            }
            Orderline orderline = new Orderline(selectedProduct);
            order.addOrderline(orderline);
            pProductList.setOrderlines(order.getOrderlines());
        } else if (e.getSource() == bSave) {
            // TODO modify order object and save to database
            dispose();
        }
    }

}