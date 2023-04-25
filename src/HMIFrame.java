import javax.swing.*;
import java.awt.*;

public class HMIFrame extends JFrame {
    private WarehousePanel pWarehouse;
    private BoxesPanel pBoxes;
    private OrderPanel pOrder;
    private JButton bSelectOrder;
    private JButton bEditOrder;
    private JButton bPickUpOrder;
    private JButton bPrintPdf;
    private Order order;

    public HMIFrame() {
        setTitle("HMI NerdyGadgets");
        setSize(800, 600);
        setLayout(new FlowLayout());
        JLabel lWarehouse = new JLabel("Weergave robot");
        add(lWarehouse);
        pWarehouse = new WarehousePanel();
        add(pWarehouse);
        JLabel lBoxes = new JLabel("Dozen");
        add(lBoxes);
        pBoxes = new BoxesPanel();
        add(pBoxes);
        JLabel lOrder = new JLabel("Order");
        add(lOrder);
        pOrder = new OrderPanel();
        add(pOrder);
        bSelectOrder = new JButton("Selecteer order");
        add(bSelectOrder);
        bEditOrder = new JButton("Wijzig order");
        add(bEditOrder);
        bPickUpOrder = new JButton("Haal order op");
        add(bPickUpOrder);
        bPrintPdf = new JButton("Print pakbon");
        add(bPrintPdf);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
