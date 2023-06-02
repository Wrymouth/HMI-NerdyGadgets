import javax.swing.*;
import java.awt.*;

public class WarehousePanel extends JPanel {
    private Warehouse warehouse;
    private Product[][] positions;
    private int width;
    private int height;
    private int boxWidth;
    private int boxHeight;
    private int productWidth;
    private int productHeight;
    private int xStart;
    private int yStart;
    private int robotX; // grid
    private int robotY; // grid

    public WarehousePanel() {

        warehouse = new Warehouse(positions);

        this.width = 200;
        this.height = 200;

        setPreferredSize(new Dimension(200, 200));

        this.boxWidth = 40;
        this.boxHeight = 40;
        this.productWidth = 30;
        this.productHeight = 30;
        this.xStart = 30;
        this.yStart = 30;
        this.robotX = 7;
        this.robotY = 0;

        setBackground(Color.WHITE);
    }

    public void setRobotPositionToProduct(Product product) {
        for (int i = 0; i < positions.length; i++) {
            for (int j = 0; j < positions[i].length; j++) {
                if (positions[i][j].getId() == product.getId()) {
                    robotX = i;
                    robotY = j;
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 0;
        int y = 0;
        positions = new Product[5][5];
        for (Product product : DBMethods.fetchAllProducts()) {
            if (product.getQuantity() > 0) {
                positions[x][y] = product;
            }
            y++;
            if (y == 5) {
                y = 0;
                x++;
            }
        }
        // draw 5x5 grid in the middle of the panel
        g.setColor(Color.BLACK);
        for (int i = 0; i < 6; i++) {
            // horizontal
            g.drawLine(xStart, yStart + i * boxHeight, xStart + 5 * boxWidth, yStart + i * boxHeight);
            // vertical
            g.drawLine(xStart + i * boxWidth, yStart, xStart + i * boxWidth, yStart * boxHeight);
        }

        // draw products
        for (int i = 0; i < positions.length; i++) {
            Product[] column = positions[i];
            for (int j = 0; j < column.length; j++) {
                if (column[j] == null) {
                    continue;
                }
                int productX = j * boxWidth + xStart;
                int productY = i * boxHeight + yStart;
                g.setColor(getProductColor(column[j].getVolume()));
                g.fillOval(productX, productY, productWidth, productHeight);
                g.setColor(Color.BLACK);
                g.setFont(new Font("default", Font.BOLD, 16));
                g.drawString(String.valueOf(column[j].getId()), productX, productY + 30);
            }
        }
        // draw robot
        g.setColor(Color.GRAY);
        int robotPosX = robotX * boxWidth + xStart;
        int robotPosY = robotY * boxHeight + yStart;
        ((Graphics2D) g).setStroke(new BasicStroke(3));
        g.drawRect(robotPosX, height-robotPosY, productWidth, productHeight);
    }

    public static Color getProductColor(int productVolume) {
        if (productVolume == 10) {
            return Color.RED;
        } else if (productVolume == 8) {
            return Color.YELLOW;
        } else if (productVolume == 5) {
            return Color.GREEN;
        } else if (productVolume == 2) {
            return Color.BLUE;
        } else {
            return Color.BLACK;
        }
    }
}
