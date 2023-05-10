import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditOrderDialog extends JDialog implements ActionListener {
    
    private Order order;
    private OrderPanel pOrder;
    private JButton bAddProduct;
    private JButton bSave;

    public EditOrderDialog(Frame frame, boolean modal, Order order) {
        super(frame, modal);
        this.order = order;
        // setup ui
        setTitle("Order bewerken");
        setSize(400, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lProducts = new JLabel("Producten");
        add(lProducts);
        pOrder = new OrderPanel();
        add(pOrder);
        pOrder.setOrder(order);
        bAddProduct = new JButton("Product toevoegen");
        bAddProduct.addActionListener(this);
        add(bAddProduct);
        bSave = new JButton("Opslaan");
        bSave.addActionListener(this);
        add(bSave);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bAddProduct) {
            AddProductDialog dialog = new AddProductDialog(this, true);
            dialog.setVisible(true);
        } else if (e.getSource() == bSave) {
            // TODO modify order object and save to database
            dispose();
        }
    }
}