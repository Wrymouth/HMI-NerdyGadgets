import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProductListPanel extends JPanel {
    private ArrayList<ProductPanel> productPanels;

    private ArrayList<Orderline> orderlines;
    private boolean hasEditButtons;

    public ProductListPanel(ArrayList<Orderline> orderlines, boolean hasEditButtons) {
        // setup data
        if (orderlines == null) {
            System.out.println("hoi");
            setOrderlines(new ArrayList<Orderline>());
        } else {
            setOrderlines(orderlines);
        }
        this.hasEditButtons = hasEditButtons;
        // setup gui
        setPreferredSize(new Dimension(100, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void setOrderlines(ArrayList<Orderline> orderlines) {
        this.orderlines = orderlines;
        createProductPanels();
        revalidate();
        repaint();
    }



    public void createProductPanels() {
        productPanels = new ArrayList<ProductPanel>();
        removeAll();
        for (Orderline orderline : orderlines) {
            ProductPanel productPanel = new ProductPanel(orderline.getProduct(), orderline.getAmount(), hasEditButtons);
            productPanels.add(productPanel);
            add(productPanel);
        }
    }
}
