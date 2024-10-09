package Model;

public class Customer extends Person{

    private String address;
    private String customerType;


    public Customer(String ID, String fullname, String DOB, boolean gender, String CMND, String phoneNumber, String email, String address, String customerType) {
        super(ID, fullname, DOB, gender, CMND, phoneNumber, email);
        this.address = address;
        this.customerType = customerType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }
}
