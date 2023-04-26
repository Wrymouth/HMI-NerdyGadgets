import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class AddOrderDialog extends JDialog implements ActionListener {
    private Order order;
    private ArrayList<Product> products;

    private JList productsList;
    private JButton bPlaceOrder;
    private JComboBox<String> productChoiceList; //Dropdown that contains all products that can be added to order
    private JButton jbAddProductToOrder;
    private ArrayList<String> addedProducts; //ArrayList that contains all product names selected from dropdown

    public AddOrderDialog(JDialog dialog, boolean modal) {
        super(dialog, modal);
        // setup data
        products = DBMethods.dbFetchAllProducts();
        addedProducts = new ArrayList<>();
        // setup ui
        setTitle("Nieuw order");
        setSize(300, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        // ui components
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);

        productsList = new JList<String>();
        add(productsList);

        bPlaceOrder = new JButton("Order plaatsen");
        add(bPlaceOrder);

        productChoiceList = new JComboBox<String>();
        for(Product p : getProducts()) {
            productChoiceList.addItem(p.getName());
        }
        productChoiceList.setVisible(true);
        add(productChoiceList);

        jbAddProductToOrder = new JButton("Voeg product toe");
        jbAddProductToOrder.addActionListener(this);
        add(jbAddProductToOrder);

    }

    //Gets all products from the DB and stores them in an ArrayList
    public ArrayList<Product> getProducts() {
        ArrayList<Product> productList = DBMethods.dbFetchAllProducts();
        return productList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Voeg product toe")) {
            String selectedValue = String.valueOf(productChoiceList.getSelectedItem()); //Gets value from dropdown
            addedProducts.add(selectedValue); //Adds value to list
            System.out.println(addedProducts);
            repaint();
        }
    }
}
