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
        setPreferredSize(new Dimension(400, 200));
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setLayout(new GridLayout(orderlines.size(),1));

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
        for (Orderline orderline : orderlines) {
            OrderlinePanel orderlinePanel = new OrderlinePanel(orderline, hasEditButtons);
            orderlinePanels.add(orderlinePanel);
            add(orderlinePanel);
        }
    }

    public ArrayList<Orderline> getOrderlines() {
        return orderlines;
    }

    public ArrayList<OrderlinePanel> getOrderlinePanels() {
        return orderlinePanels;
    }
}
