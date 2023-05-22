import com.aspose.pdf.*;

public class PackingSlip {
    private Customer customer;
    private Order order;
//    private static String _dataDir = "C:\\School\\Java\\HMI-NerdyGadgets\\PackingSlips";
    public PackingSlip(Order order) throws Exception {
        this.order = order;
        customer = DBMethods.fetchCustomer(order.getCustomer_ID());
    }

    public void printPackingSlips(){
        int i = 1;
        for (Box box: order.getBoxes()) {
            Document document = new Document();

//Add page
            Page page = document.getPages().add();


            // Initializes a new instance of the Table
            Table customerTable = new Table();
            // Set the table border color as LightGray
            customerTable.setBorder(new BorderInfo(BorderSide.All, .5f, Color.getLightGray()));
            // set the border for table cells
            customerTable.setDefaultCellBorder(new BorderInfo(BorderSide.All, .5f, Color.getLightGray()));

            page.getParagraphs().add(new TextFragment("Klantgegevens:"));

            // add row to table
            Row row = customerTable.getRows().add();
            // add table cells
            row.getCells().add("naam");
            row.getCells().add("bezorgadres");
            row.getCells().add("postcode");
            row.getCells().add("stad");

            Row row2 = customerTable.getRows().add();
            row2.getCells().add(customer.getName());
            row2.getCells().add(customer.getAdress());
            row2.getCells().add(customer.getZIP_code());
            row2.getCells().add(customer.getCity());
            // Add table object to first page of input document
            document.getPages().get_Item(1).getParagraphs().add(customerTable);

            // Add spacing between tables
            page.getParagraphs().add(new TextFragment("\n")); // Add an empty paragraph for spacing

            // Initializes a new instance of the Table
            Table productTable = new Table();
            // Set the table border color as LightGray
            productTable.setBorder(new BorderInfo(BorderSide.All, .5f, Color.getLightGray()));
            // set the border for table cells
            productTable.setDefaultCellBorder(new BorderInfo(BorderSide.All, .5f, Color.getLightGray()));

            page.getParagraphs().add(new TextFragment("Productgegevens:"));

            Row row3 = productTable.getRows().add();
// add table cells
            row3.getCells().add("productnummer");
            row3.getCells().add("naam");
            row3.getCells().add("aantal");

            for (Product product : box.getProducts()) {
                Row row4 = productTable.getRows().add();
                row4.getCells().add(String.valueOf(product.getId()));
                row4.getCells().add(product.getName());
                row4.getCells().add(String.valueOf(product.getQuantity()));
            }

            document.getPages().get_Item(1).getParagraphs().add(productTable);


// Save updated PDF
            document.save("packingSlip" + "OrderID" + order.getId() + "Box" + i + ".pdf");
            i++;
        }
    }
}
