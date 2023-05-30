import javax.swing.*;
import java.awt.*;
import com.fazecast.jSerialComm.SerialPort; // library voor arduino communicatie
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class WarehousePanel extends JPanel {
    private Product Product;
    private Warehouse warehouse;
    private int width;
    private int height;
    private int boxWidth;
    private int boxHeight;
    private int productWidth;
    private int productHeight;
    private int xStart;
    private int yStart;
    private int ordercounter;
    private Robot robot;
    private Order order;
    private Orderline orderline;
    private SelectOrderDialog selectOrderDialog;
    private int robotX;
    private int robotY;

    public WarehousePanel(Robot robot) {
        int i = 0;
        int j = 0;
        Product[][] positions = new Product[5][5];
        for (Product product : DBMethods.fetchAllProducts()) {
            positions[i][j] = product;
            j++;
            if (j == 5) {
                j = 0;
                i++;
            }
        }

        warehouse = new Warehouse(positions);

        this.width = 200;
        this.height = 200;

        setPreferredSize(new Dimension(200, 200));

        this.boxWidth = 30;
        this.boxHeight = 30;
        this.productWidth = 15;
        this.productHeight = 15;
        this.xStart = 30;
        this.yStart = 30;
        this.robot = robot;
        this.order = new Order();
        this.orderline = new Orderline();
        this.selectOrderDialog = new SelectOrderDialog();


        setBackground(Color.WHITE);
//        SerialPort Port = SerialPort.getCommPorts()[0];
//         Port.addDataListener(new SerialPortDataListener() {
//             @Override
//             public int getListeningEvents() {
//                 return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
//             }
//
//             @Override
//             public void serialEvent(SerialPortEvent serialPortEvent) {
//                 repaint();
//             }
//         });

    }

    public void setRobotPosition(int x, int y) {
        this.robotX = x;
        this.robotY = y;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw 5x5 grid in the middle of the panel
        g.setColor(Color.BLACK);
        for (int i = 0; i < 6; i++) {
            // horizontal
            g.drawLine(xStart, yStart + i * boxHeight, xStart + 5 * boxWidth, yStart + i * boxHeight);
            // vertical
            g.drawLine(xStart + i * boxWidth, yStart, xStart + i * boxWidth, yStart * boxHeight);
        }

        if (selectOrderDialog.getSelectedOrder() != null) {
            while (selectOrderDialog.getSelectedOrder() != null){
            repaint();
            }
            // draw products
            Product[][] positions = warehouse.getPositions();
            for (int i = 0; i < positions.length; i++) {
                Product[] column = positions[i];
                for (int j = 0; j < column.length; j++) {
                    if (column[j] == null) {
                        continue;
                    }
                    int productX = j * boxWidth + xStart;
                    int productY = i * boxHeight + yStart;
                    g.setColor(getProductColor(column[j].getVolume()));
                    g.fillOval(productX + 10, productY + 10, productWidth, productHeight);
                }
            }

            // draw robot
            g.setColor(Color.GRAY);
            g.fillOval(200 + robot.getPositionX(), height - robot.getPositionY(), productWidth, productHeight);

            //draw complete orders

            if (robot.getPositionX() == order.getPositionX() && robot.getPositionY() == order.getPositionY()) {
                ordercounter++;
                g.setColor(Color.WHITE);
                g.drawOval(1, 1, productWidth, productHeight);
            }

            //draw route
            g.drawLine(order.getPositionX(), order.getPositionY(), order.getPositionX() + 10, order.getPositionY() + 10);
        }
        // draw robot
        g.setColor(Color.GRAY);
        g.fillOval(robotX, height-robotY, productWidth, productHeight);
        
    }





    private Color getProductColor(int productVolume) {
        if(productVolume == 10) {
            return Color.RED;
        } else if (productVolume == 8) {
            return Color.YELLOW;
        } else if(productVolume == 5) {
            return Color.GREEN;
        } else if(productVolume == 2) {
            return Color.BLUE;
        } else {
            return Color.BLACK;
        }
    }
        public void CompleteOrder(){
        if(ordercounter == 3){
            System.out.println("doos vol");
        }
        }

}
