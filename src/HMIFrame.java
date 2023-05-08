import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HMIFrame extends JFrame implements ActionListener {
    private WarehousePanel pWarehouse;
    private BoxesPanel pBoxes;
    private OrderPanel pOrder;

    private SelectOrderDialog dSelectOrder;
    private EditOrderDialog	dEditOrder;

    private JButton bSelectOrder;
    private JButton bEditOrder;
    private JButton bPickUpOrder;
    private JButton bPrintPdf;
    
    private Order order;
    
    public HMIFrame() {
        // basic setup
        setTitle("NerdyGadgets Magazijnmanagement");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        // warehouse panel
        pWarehouse = new WarehousePanel();
        add(pWarehouse);
        JLabel lWarehouse = new JLabel("Weergave robot");
        pWarehouse.add(lWarehouse);

        // order panel
        HMIContainer orderPanel = new HMIContainer("Order", new OrderPanel());
        add(orderPanel);
        bEditOrder = new JButton("Wijzig order"); //Edit order button
        bEditOrder.addActionListener(this);
        orderPanel.add(bEditOrder);
        bPickUpOrder = new JButton("Haal order op");
        bPickUpOrder.addActionListener(this);
        orderPanel.add(bPickUpOrder);

        bSelectOrder = new JButton("Selecteer order"); //Select order button
        bSelectOrder.addActionListener(this);
        orderPanel.add(bSelectOrder);

        // boxes panel
        HMIContainer boxesPanel = new HMIContainer("Dozen", new BoxesPanel());
        add(boxesPanel);

        // buttons

        //Print receipt button
        bPrintPdf = new JButton("Print pakbon");
        bPrintPdf.addActionListener(this);
        add(bPrintPdf);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEditOrder) {
            dEditOrder = new EditOrderDialog(this, true, order);
            dEditOrder.setVisible(true);
        } else if (e.getSource() == bSelectOrder) {
            dSelectOrder = new SelectOrderDialog(this, true);
            pOrder.setOrder(dSelectOrder.getSelectedOrder());
            dSelectOrder.dispose();
        } else if (e.getSource() == bPickUpOrder) {
            // TODO pick up order
        } else if (e.getSource() == bPrintPdf) {
            // TODO print pdf
        }
    }
}
