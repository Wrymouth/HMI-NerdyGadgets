import java.util.ArrayList;

public class Order {
    private ArrayList<Orderline> products;

    public void getProducts() {
        for(Orderline p : products) {
            System.out.println(p);
        }
    }

    public void placeProductsInBoxes() {

    }
}
