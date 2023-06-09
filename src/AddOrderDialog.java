import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AddOrderDialog extends JDialog implements ActionListener {
    private Order order;
    private Customer selectedCustomer = new Customer();
    private ArrayList<Product> allProducts;

    private OrderlineListPanel pOrderlineList;
    private JButton bPlaceOrder;
    private JComboBox<String> productChoiceList; //Dropdown that contains all products that can be added to order
    private JButton jbAddProductToOrder;
    private ArrayList<String> addedProducts; //ArrayList that contains all product names selected from dropdown

    private ArrayList<Customer> allCustomers; //Arraylist of all existing customers
    private JComboBox<String> customerChoiceList; //Dropdown

    private JButton jbSelectCustomer;
    private JButton jbCancel;

    private CustomerPanel customerInfo;

    public AddOrderDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        // setup data
        allProducts = DBMethods.fetchProductsInStorage();
        allCustomers = DBMethods.fetchAllCustomers();
        addedProducts = new ArrayList<>();
        order = new Order();

        // setup ui
        setTitle("Nieuw order");
        setSize(500, 500);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4,3));

        // ui components
        pOrderlineList = new OrderlineListPanel(order.getOrderlines(), true, this);
        add(pOrderlineList);

        //Panel with all data from selected user
        customerInfo = new CustomerPanel();
        add(customerInfo);

        //Gets all products from list and stores them in a dropdown
        productChoiceList = new JComboBox<String>();
        for(Product p : allProducts) {
            productChoiceList.addItem(p.getName());
        }
        productChoiceList.setVisible(true);
        add(productChoiceList);

        //Gets all customers from list and stores them in a dropdown
        customerChoiceList = new JComboBox<String>();
        for(Customer c : allCustomers) {
            customerChoiceList.addItem(c.getName());
        }
        customerChoiceList.setVisible(true);
        add(customerChoiceList);

        jbAddProductToOrder = new JButton("Voeg product toe");
        jbAddProductToOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbAddProductToOrder.addActionListener(this);
        add(jbAddProductToOrder);

        jbSelectCustomer = new JButton("Selecteer klant");
        jbSelectCustomer.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbSelectCustomer.addActionListener(this);
        add(jbSelectCustomer);

        bPlaceOrder = new JButton("Order plaatsen");
        bPlaceOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bPlaceOrder.addActionListener(this);
        add(bPlaceOrder);

        jbCancel = new JButton("Annuleren");
        jbCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbCancel.addActionListener(this);
        add(jbCancel);
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
                //searches for the orderline containing selectedproduct
                for (Orderline ol : order.getOrderlines()) {
                    if (ol.getProduct().getId() == selectedProduct.getId()) {
                        if ((selectedProduct.getQuantity() - (ol.getAmount() + 1)) < 0){ //checks if selected amount is bigger than amount in storage
                            JOptionPane.showMessageDialog(this, "Van dit product is er te weinig in voorraad \n" +
                                            "er is nog " + selectedProduct.getQuantity() + " in voorraad",
                                    "Te weinig voorraad", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else {
                            ol.setAmount(ol.getAmount() + 1);
                            pOrderlineList.setOrderlines(order.getOrderlines());
                        }
                        return;
                    }
                }
                Orderline orderline = new Orderline(selectedProduct); //new orderline with the selected product
                order.addOrderline(orderline); //add that orderline to the current order
                pOrderlineList.setOrderlines(order.getOrderlines()); //updates the orderlines in this order for OrderLineListPanel to use
        } else if(e.getActionCommand().equals("Order plaatsen")) {
            if(!pOrderlineList.getOrderlines().isEmpty()) {
                if(selectedCustomer.getCustomerID() != 0) { //Check if customer is set
                    DBMethods.addOrder(new Order(selectedCustomer.getCustomerID()));
                    for(Orderline ol : pOrderlineList.getOrderlines()) {
                        DBMethods.addOrderline(ol, 0); // new order so orderId is not known yet
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Er moet een klant geselecteerd zijn!",
                            "Geen klant geselecteerd", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Order mag niet leeg zijn!",
                        "Order leeg", JOptionPane.INFORMATION_MESSAGE);
            }
        } else if(e.getActionCommand().equals("Selecteer klant")) {
            String selectedName = String.valueOf(customerChoiceList.getSelectedItem());
            selectedCustomer = DBMethods.fetchCustomerByName(selectedName); //Get selected from db based on name
            customerInfo.setCustomer(selectedCustomer); //Set customer in CustomerPanel which will also display it
        } else if(e.getActionCommand().equals("Annuleren")) {
            dispose();
        }
    }
}
