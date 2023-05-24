import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class BinPacking {
    private int boxSize = 15;

    public ArrayList<Box> binPacking(ArrayList<Product> products) {

        // First sort all products on volume in decreasing order
        Collections.sort(products, Comparator.comparing(Product::getVolume));
        Collections.reverse(products);

        // Now call first fit for sorted items
        ArrayList<Box> boxes = firstFit(products);
        return boxes;

    }

    private ArrayList<Box> firstFit(ArrayList<Product> products) {
        ArrayList<Box> boxes = new ArrayList<>();
        int maxNrBoxes = products.size();

        // Initialize result (Count of boxes)
        int cntOfBoxes = 0;

        // Create an array to store remaining space in boxes
        // there can be at most 'maxNrBoxes' boxes
        int[] remainingSpace = new int[maxNrBoxes];

        // Place items one by one
        for (Product product : products) {
            // Find the first in use box that can accommodate the product
            int i = 0;
            for (Box box : boxes) {
                if (remainingSpace[i] >= product.getVolume())
                {
                    // Product fits in this box, add product to it
                    ArrayList<Product> boxProducts = box.getProducts();
                    boxProducts.add(product);

                    // Calculate remaining space in the current box and store that in the arraylist
                    remainingSpace[i] = remainingSpace[i] - product.getVolume();

                    break;
                }
                i++;
            }

            // If none of the used boxes can accommodate the product, add a new box
            if (i == cntOfBoxes)
            {
                // Add product to the new box
                ArrayList<Product> boxProducts = new ArrayList<Product>();
                boxProducts.add(product);
                boxes.add(new Box(boxProducts));

                // Calculate remaining space in the box and store that in the arraylist
                remainingSpace[cntOfBoxes] = boxSize - product.getVolume();

                cntOfBoxes++;
            }
        }

        return boxes;
    }
}
