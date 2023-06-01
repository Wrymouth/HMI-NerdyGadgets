import com.aspose.pdf.operators.BX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class OrderlineListPanel extends JPanel{
    private ArrayList<OrderlinePanel> orderlinePanels;

    private ArrayList<Orderline> orderlines;
    private boolean hasEditButtons;
    private JScrollPane jsProductsScroll;
    private JPanel selectedProductsView = new JPanel();
    private Dialog aod;

    public OrderlineListPanel(ArrayList<Orderline> orderlines, boolean hasEditButtons, Dialog parent) {
        // setup data
        this.hasEditButtons = hasEditButtons;
        aod = parent;
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
        createOrderlinePanels();
        revalidate();
        repaint();
    }

    public void createOrderlinePanels() {
        orderlinePanels = new ArrayList<OrderlinePanel>();
        removeAll();

        selectedProductsView.setLayout(new BoxLayout(selectedProductsView, BoxLayout.PAGE_AXIS));

        jsProductsScroll = new JScrollPane(selectedProductsView);
        jsProductsScroll.setPreferredSize(new Dimension(300, 300));
        add(jsProductsScroll);
        for (Orderline orderline : orderlines) {
                OrderlinePanel orderlinePanel = new OrderlinePanel(orderline, hasEditButtons, orderlines, this);
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

    public JPanel getSelectedProductsView() {
        return selectedProductsView;
    }

    public Dialog getAod() {
        return aod;
    }
}
