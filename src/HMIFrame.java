//IMPORTANT: All elements on main UI are added to the HMIContainer object,
//please add buttons, labels etc to an HMIContainer object and not to the main UI frame itself.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.border.Border;

public class HMIFrame extends JFrame implements ActionListener {
    // Create all HMIContainers so we can later access their data
    private HMIContainer warehousePanel;
    private HMIContainer boxesPanel;
    private HMIContainer orderPanel;
    private HMIContainer buttonPanel;

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
        setLayout(new GridLayout(2, 1, 5, 5));

        // Warehouse panel
        warehousePanel = new HMIContainer("", new WarehousePanel());
        add(warehousePanel);
        warehousePanel.add(warehousePanel.getWarehousePanel());
        Border blackline = BorderFactory.createLineBorder(Color.black);
        warehousePanel.setBorder(blackline);

        // Order panel
        orderPanel = new HMIContainer("", new OrderPanel());
        add(orderPanel);
        orderPanel.add(orderPanel.getOrderPanel());
        orderPanel.setBorder(blackline);

        bEditOrder = new JButton("Bewerk order"); // Edit order button
        bEditOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bEditOrder.addActionListener(this);
        orderPanel.add(bEditOrder);

        bPickUpOrder = new JButton("Haal order op"); // Get order button
        bPickUpOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bPickUpOrder.addActionListener(this);
        orderPanel.add(bPickUpOrder);

        bSelectOrder = new JButton("Selecteer order"); // Select order button
        bSelectOrder.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bSelectOrder.addActionListener(this);
        orderPanel.add(bSelectOrder);

        // Boxes panel
        boxesPanel = new HMIContainer("", new BoxesPanel());
        add(boxesPanel);
        boxesPanel.add(boxesPanel.getBoxesPanel());
        boxesPanel.setBorder(blackline);

        // Panel with PDF and emergency button
        buttonPanel = new HMIContainer("", new JPanel());
        add(buttonPanel);
        bPrintPdf = new JButton("Print pakbon"); // Print receipt button
        bPrintPdf.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bPrintPdf.addActionListener(this);
        buttonPanel.add(bPrintPdf);

        jbEmergency = new JButton("Noodstop"); // Robot emergency button
        jbEmergency.setBackground(Color.RED);
        jbEmergency.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jbEmergency.addActionListener(this);
        buttonPanel.add(jbEmergency);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                ArduinoComm.closeComPort(comPort);
                System.exit(0);
            }
        });
        arduino = new ArduinoComm(comPort, warehousePanel.getWarehousePanel(), boxesPanel.getBoxesPanel());
        setVisible(true);
    }

    public void createSOD(){
        dSelectOrder = new SelectOrderDialog(this, true);
        Order selectedOrder = dSelectOrder.getSelectedOrder();
        if (selectedOrder == null) {
            return;
        }
        ArrayList<Orderline> orderlines = DBMethods.fetchOrderlines(selectedOrder);
        this.order = selectedOrder;
        this.order.setOrderlines(orderlines);
        orderPanel.getOrderPanel().setOrder(selectedOrder, DBMethods.fetchCustomer(selectedOrder.getCustomerID()));
        dSelectOrder.dispose();
        order.placeProductsInBoxes();
        boxesPanel.getBoxesPanel().setBoxes(order.getBoxes());
        arduino.setOrder(order);
        arduino.setAllProducts();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bEditOrder) { // Opens dialog where user will be able to edit an order
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Selecteer eerst een order!",
                        "Geen order geselecteerd!", JOptionPane.INFORMATION_MESSAGE);
            } else {
                dEditOrder = new EditOrderDialog(this, true, order);
                dEditOrder.setVisible(true);
                order = dEditOrder.getOrder();
                orderPanel.getOrderPanel().setOrder(order);
                dEditOrder.dispose();
            }
        } else if (e.getSource() == bSelectOrder) { // Opens dialog where user will be able to select an order
            createSOD();
        } else if (e.getSource() == bPickUpOrder) {
            if (order.isProcessed()) {
                JOptionPane.showMessageDialog(this, "Deze order is al opgehaald!");
                return;
            }
            arduino.TSP();
        } else if (e.getSource() == bPrintPdf) {
            try {
                if (order == null) {
                    JOptionPane.showMessageDialog(this, "Selecteer eerst een order!",
                            "Geen order geselecteerd!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    PackingSlip pdf = new PackingSlip(order);
                    pdf.printPackingSlips(); //Creates new PDF packing slip
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else if (e.getActionCommand().equals("Noodstop")) {
            try {
                arduino.sendEmergencySignal(true); //Sends emergency signal to Arduino
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
