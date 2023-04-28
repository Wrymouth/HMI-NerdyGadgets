import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderlineListPanel extends JPanel {
    private ArrayList<OrderlinePanel> orderlinePanels;

    private ArrayList<Orderline> orderlines;
    private boolean hasEditButtons;

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
    }

    public void setOrderlines(ArrayList<Orderline> orderlines) {
        this.orderlines = orderlines;
        createProductPanels();
        revalidate();
        repaint();
    }



    public void createProductPanels() {
        orderlinePanels = new ArrayList<OrderlinePanel>();
        removeAll();
        for (Orderline orderline : orderlines) {
            OrderlinePanel orderlinePanel = new OrderlinePanel(orderline, hasEditButtons);
            orderlinePanels.add(orderlinePanel);
            add(orderlinePanel);
        }
    }

    public ArrayList<Orderline> getOrderlines() {
        ArrayList<Orderline> orderlines = new ArrayList<Orderline>();
        for (OrderlinePanel orderlinePanel : orderlinePanels) {
            orderlines.add(orderlinePanel.getOrderline());
        }
        return orderlines;
    }

    public ArrayList<OrderlinePanel> getOrderlinePanels() {
        return orderlinePanels;
    }
}
