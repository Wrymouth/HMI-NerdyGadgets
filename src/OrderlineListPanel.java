import com.aspose.pdf.operators.BX;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderlineListPanel extends JPanel {
    private ArrayList<OrderlinePanel> orderlinePanels;

    private ArrayList<Orderline> orderlines;
    private boolean hasEditButtons;
    private JScrollPane jsProductsScroll;

    public OrderlineListPanel(ArrayList<Orderline> orderlines, boolean hasEditButtons) {
        // setup data
        this.hasEditButtons = hasEditButtons;
        if (orderlines == null) {
            setOrderlines(new ArrayList<Orderline>());
        } else {
            setOrderlines(orderlines);
        }
        // setup gui
        setPreferredSize(new Dimension(100, 200));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(new JLabel("Geen product(en) geselecteerd!"));
    }

    public void setOrderlines(ArrayList<Orderline> orderlines) {
        this.orderlines = orderlines;
        createOrderlinePanels();
        revalidate();
        repaint();
    }

    public void createOrderlinePanels() {
        orderlinePanels = new ArrayList<OrderlinePanel>();
        removeAll();

        JPanel selectedProductsView = new JPanel();
        selectedProductsView.setLayout(new BoxLayout(selectedProductsView, BoxLayout.PAGE_AXIS));

        jsProductsScroll = new JScrollPane(selectedProductsView);
        jsProductsScroll.setPreferredSize(new Dimension(300, 300));
        add(jsProductsScroll);

        for (Orderline orderline : orderlines) {
            OrderlinePanel orderlinePanel = new OrderlinePanel(orderline, hasEditButtons);
            orderlinePanels.add(orderlinePanel);
            selectedProductsView.add((orderlinePanel));
        }
    }

    public ArrayList<Orderline> getOrderlines() {
        return orderlines;
    }

    public ArrayList<OrderlinePanel> getOrderlinePanels() {
        return orderlinePanels;
    }
}
