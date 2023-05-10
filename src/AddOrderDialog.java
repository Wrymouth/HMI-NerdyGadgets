import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddOrderDialog extends JDialog implements ActionListener {
    private Order order;
    private ArrayList<Product> allProducts;

    private OrderlineListPanel pOrderlineList;
    private JButton bPlaceOrder;
    private JComboBox<String> productChoiceList; //Dropdown that contains all products that can be added to order
    private JButton jbAddProductToOrder;
    private ArrayList<String> addedProducts; //ArrayList that contains all product names selected from dropdown

    public AddOrderDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        // setup data
        allProducts = DBMethods.fetchAllProducts();
        addedProducts = new ArrayList<>();
        order = new Order();
        // setup ui
        setTitle("Nieuw order");
        setSize(300, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // ui components
        JLabel lOrder = new JLabel("Order");
        add(lOrder);
        pOrderlineList = new OrderlineListPanel(order.getOrderlines(), true);
        add(pOrderlineList);
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);

        productChoiceList = new JComboBox<String>();
        for(Product p : allProducts) {
            productChoiceList.addItem(p.getName());
        }
        productChoiceList.setVisible(true);
        add(productChoiceList);

        jbAddProductToOrder = new JButton("Voeg product toe");
        jbAddProductToOrder.addActionListener(this);
        add(jbAddProductToOrder);

        bPlaceOrder = new JButton("Order plaatsen");
        bPlaceOrder.addActionListener(this);
        add(bPlaceOrder);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Voeg product toe")) {
            String selectedValue = String.valueOf(productChoiceList.getSelectedItem()); //Gets value from dropdown
            addedProducts.add(selectedValue); //Adds value to list
            // get selected product from name
            Product selectedProduct = null;
            for (Product product : allProducts) {
                if (product.getName().equals(selectedValue)) {
                    selectedProduct = product;
                }
            }
            Orderline orderline = new Orderline(selectedProduct);
            order.addOrderline(orderline);
            pOrderlineList.setOrderlines(order.getOrderlines());
        } else if (e.getActionCommand().equals("Order plaatsen")) {
            if(!pOrderlineList.getOrderlines().isEmpty()) {
                DBMethods.addOrder(new Order());
                for(Orderline ol : pOrderlineList.getOrderlines()) {
                    DBMethods.addOrderline(ol);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Order mag niet leeg zijn!",
                        "Order leeg", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
