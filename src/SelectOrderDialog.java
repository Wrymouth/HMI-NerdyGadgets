import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SelectOrderDialog extends JDialog implements ActionListener {
    private ArrayList<Order> orders;

    private JList listOrders;
    private JButton bAddOrder;
    private JButton bSelect;

    public SelectOrderDialog(Frame frame, boolean modal) {
        super(frame, modal);
        orders = new ArrayList<Order>();
        dbFetchAllOrders();
        // setup ui
        setTitle("Orders");
        setSize(200, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lOrders = new JLabel("Orders");
        add(lOrders);
        listOrders = new JList<String>();
        add(listOrders);
        bAddOrder = new JButton("Order toevoegen");
        bAddOrder.addActionListener(this);
        add(bAddOrder);
        bSelect = new JButton("Selecteren");
        bSelect.addActionListener(this);
        add(bSelect);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bAddOrder) {
            AddOrderDialog dialog = new AddOrderDialog(this, true);
            dialog.setVisible(true);
        } else if (e.getSource() == bSelect) {
            // TODO select order and close dialog
            dispose();
        }
    }

    public void dbFetchAllOrders() {
        // TODO get all orders from database
    }

}
