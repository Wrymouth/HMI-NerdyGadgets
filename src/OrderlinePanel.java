import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class OrderlinePanel extends JPanel implements ActionListener {
    private Orderline orderline;

    private ArrayList<Orderline> orderlines;

    private JTextField tAmount;
    private JButton bRemove;
    private boolean hasEditButtons;
    private OrderlineListPanel olp;

    private boolean error = false;

    public OrderlinePanel(Orderline orderline, boolean hasEditButtons, ArrayList<Orderline> orderlines, OrderlineListPanel olp) {
        this.orderline = orderline;
        this.hasEditButtons = hasEditButtons;
        this.orderlines = orderlines;
        this.olp = olp;
        setPreferredSize(new Dimension(100, 40));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // list view

        JLabel lName = new JLabel(orderline.getProduct().getName());
        lName.setPreferredSize(new Dimension(150, 50));
        add(lName);

        if (hasEditButtons) {
            tAmount = new JTextField(1);
            tAmount.setText(String.valueOf(orderline.getAmount()));
            tAmount.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    setAmount();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    setAmount();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    setAmount();
                }

                public void setAmount() {
                    try {
                        //sends error if selected amount is bigger than current storage
                        if ((orderline.getProduct().getQuantity() - Integer.parseInt(tAmount.getText())) < 0){
                            JOptionPane.showMessageDialog(olp.getAod(), "Van dit product is er te weinig in voorraad \n " +
                                            "er is nog " + orderline.getProduct().getQuantity() + " in voorraad",
                                    "Te weinig voorraad", JOptionPane.INFORMATION_MESSAGE);
                            //resets textfield
                            SwingUtilities.invokeLater(() -> {
                                tAmount.setText(String.valueOf(orderline.getProduct().getQuantity()));
                            });
                        } else {
                            orderline.setAmount(Integer.parseInt(tAmount.getText()));
                            error = false;
                        }
                    } catch (NumberFormatException e) {
                        orderline.setAmount(0);
                        error = true;
                    }
                }
            });

            add(tAmount);
            bRemove = new JButton("X");
            bRemove.setPreferredSize(new Dimension(50, 50));
            bRemove.addActionListener(this);
            add(bRemove);
        }
    }

    public int getAmount() {
        return Integer.parseInt(tAmount.getText());
    }

    public boolean isError() {
        return error;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bRemove) {
            removeAll();
            orderlines.remove(orderline);
            for (int i = 0; i < orderlines.size(); i++) {
                Orderline element = orderlines.get(i);
                if (element == null) {
                    orderlines.remove(i);
                    i--; // Decrement index to compensate for removed element
                }
            }
            olp.getSelectedProductsView().removeAll();
            olp.getSelectedProductsView().validate();
            olp.setOrderlines(orderlines);
        }
    }
}