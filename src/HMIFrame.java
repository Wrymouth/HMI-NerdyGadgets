//IMPORTANT: All elements on main UI are added to the HMIContainer object,
//please add buttons, labels etc to an HMIContainer object and not to the main UI frame itself.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HMIFrame extends JFrame implements ActionListener {
    // Create all HMIContainers so we can later access their data
    private HMIContainer warehousePanel;
    private HMIContainer boxesPanel;
    private HMIContainer orderPanel;
    private HMIContainer ButtonPanel;

    private SelectOrderDialog dSelectOrder;
    private EditOrderDialog dEditOrder;

    private JButton bSelectOrder;
    private JButton bEditOrder;
    private JButton bPickUpOrder;
    private JButton bPrintPdf;
    private JButton jbEmergency;

    private Order order;
    private Robot robot = new Robot();
    private ArduinoComm arduino;

    private String comPort = "COM4";

    public HMIFrame() {
        // GUI Setup
        setTitle("NerdyGadgets Magazijnmanagement");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        // Warehouse panel
        warehousePanel = new HMIContainer("", new WarehousePanel(robot));
        add(warehousePanel);
        JLabel lWarehouse = new JLabel("Weergave robot");
        warehousePanel.add(lWarehouse);
        warehousePanel.add(warehousePanel.getWarehousePanel());

        // Order panel
        orderPanel = new HMIContainer("Order", new OrderPanel());
        add(orderPanel);
        orderPanel.add(orderPanel.getOrderPanel());

        bEditOrder = new JButton("Wijzig order"); // Edit order button
        bEditOrder.addActionListener(this);
        orderPanel.add(bEditOrder);

        bPickUpOrder = new JButton("Haal order op"); // Get order button
        bPickUpOrder.addActionListener(this);
        orderPanel.add(bPickUpOrder);

        bSelectOrder = new JButton("Selecteer order"); // Select order button
        bSelectOrder.addActionListener(this);
        orderPanel.add(bSelectOrder);

        // Boxes panel
        boxesPanel = new HMIContainer("Dozen", new BoxesPanel());
        add(boxesPanel);
        boxesPanel.add(boxesPanel.getBoxesPanel());

        // Panel with PDF button
        ButtonPanel = new HMIContainer("", new JPanel());
        add(ButtonPanel);
        bPrintPdf = new JButton("Print pakbon"); // Print receipt button
        bPrintPdf.addActionListener(this);
        ButtonPanel.add(bPrintPdf);

        jbEmergency = new JButton("Noodstop"); // Robot emergency button
        jbEmergency.setBackground(Color.RED);
        jbEmergency.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbEmergency.addActionListener(this);
        ButtonPanel.add(jbEmergency);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                ArduinoComm.closeComPort(comPort);
                System.exit(0);
            }
        });
        arduino = new ArduinoComm(robot, comPort);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEditOrder) {
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Selecteer eerst een order");
            } else {
                dEditOrder = new EditOrderDialog(this, true, order);
                dEditOrder.setVisible(true);
                order = dEditOrder.getOrder();
                orderPanel.getOrderPanel().setOrder(order);
                dEditOrder.dispose();
            }
        } else if (e.getSource() == bSelectOrder) {
            dSelectOrder = new SelectOrderDialog(this, true);
            Order selectedOrder = dSelectOrder.getSelectedOrder();
            if (selectedOrder == null) {
                return;
            }
            ArrayList<Orderline> orderlines = DBMethods.fetchOrderlines(selectedOrder);
            this.order = selectedOrder;
            this.order.setOrderlines(orderlines);
            orderPanel.getOrderPanel().setOrder(selectedOrder);
            dSelectOrder.dispose();
            order.placeProductsInBoxes();

        } else if (e.getSource() == bPickUpOrder) {
            try {
                arduino.TSP(order);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getSource() == bPrintPdf) {
            try {
                PackingSlip pdf = new PackingSlip(order);
                pdf.printPackingSlips();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Noodstop")) {
            try {
                arduino.sendEmergencySignal(true);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
