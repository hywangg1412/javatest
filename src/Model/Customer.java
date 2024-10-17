package Model;

import java.time.LocalDate;

public class Customer extends Person implements Comparable<Customer>{

    private String address;
    private String customerType;

    private double voucher;

    public Customer(String ID, String fullname, LocalDate DOB, boolean gender, String CMND, String phoneNumber, String email, String address, String customerType) {
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

    @Override
    public int compareTo(Customer o) {
        return this.getID().compareTo(o.getID());
    }

    public double getVoucher() {
        return voucher;
    }

    public void setVoucher(double voucher) {
        this.voucher = voucher;
    }

    @Override
    public String toString() {
        return super.toString() + "Customer{" +
                "address='" + address + '\'' +
                ", customerType='" + customerType + '\'' +
                ", voucher=" + voucher +
                '}';
    }
}
