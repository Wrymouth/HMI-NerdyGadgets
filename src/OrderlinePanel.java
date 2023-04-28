import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderlinePanel extends JPanel implements ActionListener {
    private Orderline orderline;

    private JTextField tAmount;
    private JButton bRemove;
    private boolean hasEditButtons;


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
            add(tAmount);
            bRemove = new JButton("X");
            bRemove.addActionListener(this);
            add(bRemove);
        }
    }

    public String getAmount() {
        return tAmount.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bRemove) {

        }
    }

    public Orderline getOrderline() {
        orderline.setAmount(Integer.parseInt(this.getAmount()));
        return orderline;
    }
}
