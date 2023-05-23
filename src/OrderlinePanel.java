import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderlinePanel extends JPanel implements ActionListener {
    private Orderline orderline;

    private JTextField tAmount;
    private JButton bRemove;
    private boolean hasEditButtons;

    private boolean error = false;

    public OrderlinePanel(Orderline orderline, boolean hasEditButtons) {
        this.orderline = orderline;
        this.hasEditButtons = hasEditButtons;
        setPreferredSize(new Dimension(100, 40));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // list view
        JLabel lName = new JLabel(orderline.getProduct().getName());
        add(lName);
        if (hasEditButtons) {
            tAmount = new JTextField(2);
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
                        orderline.setAmount(Integer.parseInt(tAmount.getText()));
                        error = false;
                    } catch (NumberFormatException e) {
                        orderline.setAmount(0);
                        error = true;
                    }
                }
            });
            add(tAmount);
            bRemove = new JButton("X");
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

        }
    }

    public Orderline getOrderline(){
        orderline.setAmount(this.getAmount());
        return orderline;
    }
}