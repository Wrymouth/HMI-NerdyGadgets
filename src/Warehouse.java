public class Warehouse {
    private Product[][] positions;

    public Warehouse(Product[][] positions) {
        this.positions = positions;
    }
    
    public Product[][] getPositions() {
        return positions;
    }
}
