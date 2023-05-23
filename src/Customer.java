import java.util.ArrayList;

public class Customer {
    private int customerID;
    private String name;
    private String address;
    private String ZIPcode;
    private String city;

    public Customer(int customerID, String name, String address, String ZIPcode, String city) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.ZIPcode = ZIPcode;
        this.city = city;
    }

    public Customer() {

    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getZIPcode() {
        return ZIPcode;
    }

    public void setZIPcode(String ZIP_code) {
        this.ZIPcode = ZIP_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ZIPcode='" + ZIPcode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

