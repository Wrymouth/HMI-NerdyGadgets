import com.aspose.pdf.operators.BX;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

public class OrderlineListPanel extends JPanel implements ComponentListener{
    private ArrayList<OrderlinePanel> orderlinePanels;

    private ArrayList<Orderline> orderlines;
    private boolean hasEditButtons;
    private JScrollPane jsProductsScroll;
    private JPanel selectedProductsView = new JPanel();

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


        selectedProductsView.setLayout(new BoxLayout(selectedProductsView, BoxLayout.PAGE_AXIS));

        jsProductsScroll = new JScrollPane(selectedProductsView);
        jsProductsScroll.setPreferredSize(new Dimension(300, 300));
        add(jsProductsScroll);

        for (Orderline orderline : orderlines) {
            if(orderline != null){
                OrderlinePanel orderlinePanel = new OrderlinePanel(orderline, hasEditButtons, orderlines);
                orderlinePanels.add(orderlinePanel);
                orderlinePanel.addComponentListener(this);
                selectedProductsView.add((orderlinePanel));
            }
        }
    }

    public ArrayList<Orderline> getOrderlines() {
        return orderlines;
    }

    public ArrayList<OrderlinePanel> getOrderlinePanels() {
        return orderlinePanels;
    }

    @Override
    public void componentResized(ComponentEvent e) {
//        this.removeAll();
//        this.revalidate();
//        this.repaint();
//        System.out.println("removed");
    }

    @Override
    public void componentMoved(ComponentEvent e) {
//        this.removeAll();
//        this.revalidate();
//        this.repaint();
//        System.out.println("removed");
    }

    @Override
    public void componentShown(ComponentEvent e) {
//        this.removeAll();
//        this.revalidate();
//        this.repaint();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        this.revalidate();
        this.repaint();
        System.out.println("ll");
    }
}
