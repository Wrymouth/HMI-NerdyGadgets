import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel {
    private JTextField tAmount;
    private JButton bRemove;
    private boolean hasEditButtons;


    public ProductPanel(Product product, boolean hasEditButtons) {
        this.hasEditButtons = hasEditButtons;
        setPreferredSize(new Dimension(100, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // list view
        JLabel lName = new JLabel(product.getName());
        add(lName);
        if (hasEditButtons) {
            tAmount = new JTextField(2);
            add(tAmount);
            bRemove = new JButton("X");
            add(bRemove);
        }
    }
}
