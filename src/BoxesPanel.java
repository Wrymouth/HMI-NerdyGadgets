import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BoxesPanel extends JPanel {
    private ArrayList<Box> boxes;

    private int pickedProductsCount; // how many products have been picked up
    private int displayedProductsCount; // how many of the picked up products have been displayed

    public BoxesPanel() {
        setPreferredSize(new Dimension(200, 200));
        setBackground(Color.WHITE);

    }

    public void setBoxes(ArrayList<Box> boxes) {
        this.boxes = boxes;
        repaint();
    }

    public void setCount(int count) {
        displayedProductsCount = count;
        System.out.println("displayedProductsCount: " + displayedProductsCount);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw "table" for the boxes to be placed on
        // get width and height of the panel
        int tableWidth = getWidth();
        int tableHeight = getHeight() - 30;
        int boxWidth = 100;
        int boxHeight = 100;
        int productWidth = 30;
        int productHeight = 30;

        displayedProductsCount = 0;

        g.setColor(Color.BLACK);
        g.drawLine(0, tableHeight, tableWidth, tableHeight);

        if (boxes == null)
            return;
        for (int i = 0; i < boxes.size(); i++) {
            int boxStartX = 30 + (i * (boxWidth + 10));
            int boxStartY = tableHeight - boxHeight;
            g.setColor(Color.BLACK);
            g.drawRect(boxStartX, boxStartY, boxWidth, boxHeight);
            ArrayList<Product> products = boxes.get(i).getProducts();
            for (int j = 0; j < products.size(); j++) {
                if (displayedProductsCount == pickedProductsCount)
                    continue;
                Product product = products.get(j);
                // products in rows of 3 from the bottom of the box to the top
                int productX = boxStartX + (j % 3) * productWidth;
                int productY = boxStartY + boxHeight - (j / 3 + 1) * productHeight;

                g.setColor(WarehousePanel.getProductColor(product.getVolume()));
                g.fillOval(productX, productY, productWidth, productHeight);
                g.setColor(Color.BLACK);
                g.setFont(new Font("default", Font.BOLD, 16));
                g.drawString(String.valueOf(product.getId()), productX + 12, productY + 20);
                displayedProductsCount++;
            }
            boxStartX += boxWidth;
        }
    }
}
