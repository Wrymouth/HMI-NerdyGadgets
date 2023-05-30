import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

public class SelectOrderDialog extends JDialog implements ActionListener {
    private ArrayList<Order> orders;
    private Order selectedOrder;

    private JList listOrders;
    private JButton bAddOrder;
    private JButton bSelect;
    private Frame frame;

    public SelectOrderDialog(Frame frame, boolean modal) {
        super(frame, modal);
        this.frame = frame;
        orders = DBMethods.fetchAllOrders();

        // setup ui
        setTitle("Orders");
        setSize(200, 300);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        // ui components
        JLabel lOrders = new JLabel("Orders");
        add(lOrders);

        Vector<String> orderNames = new Vector<String>();
        for (Order order : orders) {
            orderNames.add(order.getName());
        }
        listOrders = new JList<String>(orderNames);
        listOrders.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                int index = listOrders.getSelectedIndex();
                if (index != -1) {
                    selectedOrder = orders.get(listOrders.getSelectedIndex());
                }
            }
        });
        listOrders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(listOrders);
        bAddOrder = new JButton("Order toevoegen");
        bAddOrder.addActionListener(this);
        add(bAddOrder);
        bSelect = new JButton("Selecteren");
        bSelect.addActionListener(this);
        add(bSelect);
        setVisible(true);
    }

    public Order getSelectedOrder() {
        return selectedOrder;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bAddOrder) {
            AddOrderDialog dialog = new AddOrderDialog(this, true);
            dialog.setVisible(true);
            dispose();
            SelectOrderDialog sod = new SelectOrderDialog(frame, true);
        } else if (e.getSource() == bSelect) {
            if(selectedOrder != null) {
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Selecteer eerst een order!",
                        "Geen order geselecteerd", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
