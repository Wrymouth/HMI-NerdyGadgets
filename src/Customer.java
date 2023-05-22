public class Customer {
    private int customerID;
    private String name;
    private String adress;
    private String ZIP_code;
    private String city;

    public Customer(int customerID, String name, String adress, String ZIP_code, String city) {
        this.customerID = customerID;
        this.name = name;
        this.adress = adress;
        this.ZIP_code = ZIP_code;
        this.city = city;
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

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getZIP_code() {
        return ZIP_code;
    }

    public void setZIP_code(String ZIP_code) {
        this.ZIP_code = ZIP_code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
