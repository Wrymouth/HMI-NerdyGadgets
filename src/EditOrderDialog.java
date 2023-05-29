import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditOrderDialog extends JDialog implements ActionListener {

    private Order order;
    private Customer selectedCustomer;
    private ArrayList<Product> allProducts;

    private OrderlineListPanel pOrderlineList;
    private JButton jbSave;
    private JComboBox<String> productChoiceList; //Dropdown that contains all products that can be added to order
    private JButton jbAddProductToOrder;
    private ArrayList<String> addedProducts; //ArrayList that contains all product names selected from dropdown

    private JButton jbCancel;

    private CustomerPanel customerInfo;

    public EditOrderDialog(Frame frame, boolean modal, Order order) {
        super(frame, modal);
        allProducts = DBMethods.fetchAllProducts();
        allProducts = DBMethods.fetchAllProducts();
        addedProducts = new ArrayList<>();
        this.order = order;
        selectedCustomer = DBMethods.fetchCustomer(order.getCustomerID());
        // setup ui

        setTitle("Order wijzigen");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4,3));

        // ui components
        pOrderlineList = new OrderlineListPanel(order.getOrderlines(), true);
        add(pOrderlineList);

        //Panel with all data from selected user
        customerInfo = new CustomerPanel();
        add(customerInfo);
        customerInfo.setCustomer(selectedCustomer);

        //Gets all products from list and stores them in a dropdown
        productChoiceList = new JComboBox<String>();
        for(Product p : allProducts) {
            productChoiceList.addItem(p.getName());
        }
        productChoiceList.setVisible(true);
        add(productChoiceList);

        jbAddProductToOrder = new JButton("Voeg product toe");
        jbAddProductToOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbAddProductToOrder.addActionListener(this);
        add(jbAddProductToOrder);

        jbSave = new JButton("Order opslaan");
        jbSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbSave.addActionListener(this);
        add(jbSave);

        jbCancel = new JButton("Annuleren");
        jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbCancel.addActionListener(this);
        add(jbCancel);
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Voeg product toe")) {
            pOrderlineList.getSelectedProductsView().removeAll();
            String selectedValue = String.valueOf(productChoiceList.getSelectedItem()); //Gets value from dropdown
            addedProducts.add(selectedValue); //Adds value to list
            // get selected product from name
            Product selectedProduct = null;
            for (Product product : allProducts) {
                if (product.getName().equals(selectedValue)) {
                    selectedProduct = product;
                }
            }
            for (Orderline ol : order.getOrderlines()) {
                if (ol.getProduct().getId() == selectedProduct.getId()) {
                    ol.setAmount(ol.getAmount() + 1);
                    pOrderlineList.setOrderlines(order.getOrderlines());
                    return;
                }
            }
            Orderline orderline = new Orderline(selectedProduct);
            order.addOrderline(orderline);
            pOrderlineList.setOrderlines(order.getOrderlines());
        } else if(e.getActionCommand().equals("Order opslaan")) {
            if (!pOrderlineList.getOrderlines().isEmpty()) {
                DBMethods.updateOrder(order, pOrderlineList.getOrderlines());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Order mag niet leeg zijn!",
                        "Order leeg", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if(e.getActionCommand().equals("Annuleren")) {
            dispose();
        }
    }
}