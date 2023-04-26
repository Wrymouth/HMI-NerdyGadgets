import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel {
    private JTextField tAmount;
    private JButton bRemove;
    private boolean hasEditButtons;


    public ProductPanel(Product product, int amount, boolean hasEditButtons) {
        this.hasEditButtons = hasEditButtons;
        setPreferredSize(new Dimension(100, 40));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // list view
        JLabel lName = new JLabel(product.getName());
        add(lName);
        if (hasEditButtons) {
            tAmount = new JTextField(2);
            tAmount.setText(String.valueOf(amount));
            add(tAmount);
            bRemove = new JButton("X");
            add(bRemove);
        }
    }
}
