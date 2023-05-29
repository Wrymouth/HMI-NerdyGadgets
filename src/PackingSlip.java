import com.aspose.pdf.*;
import com.aspose.pdf.Color;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PackingSlip {
    private Customer customer;
    private Order order;
    public PackingSlip(Order order) throws Exception {
        this.order = order;
        try {
            customer = DBMethods.fetchCustomer(order.getCustomerID());
        } catch (NullPointerException e) {

        }

    }

    public void printPackingSlips() {
        int i = 1;
        for (Box box: order.getBoxes()) {
            int[] amount = new int[26];
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
                row2.getCells().add(customer.getAddress());
                row2.getCells().add(customer.getZIPcode());
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
                amount[product.getId()] = Collections.frequency(box.getProducts(), product);
            }

            List<Product> distinctProducts = box.getProducts().stream().distinct().toList();

            for (Product product: distinctProducts) {
                Row row4 = productTable.getRows().add();
                row4.getCells().add(String.valueOf(product.getId()));
                row4.getCells().add(product.getName());
                row4.getCells().add(String.valueOf(amount[product.getId()]));
            }

                document.getPages().get_Item(1).getParagraphs().add(productTable);


// Save updated PDF
                document.save("packingSlip" + "OrderID" + order.getId() + "Box" + i + ".pdf");
                i++;
            }
        }
    }
