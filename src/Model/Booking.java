package Model;

import View.AppTools;

import java.time.LocalDate;
import java.util.Comparator;

public class Booking implements Comparator<Booking> {
    private String bookingID;
    private LocalDate bookingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String customerID;
    private String serviceID;

    public Booking(String bookingID, String bookingDate, String startDate, String endDate, String customerID, String serviceID) {
        this.bookingID = bookingID;
        this.bookingDate = AppTools.parseDate(bookingDate);
        this.startDate = AppTools.parseDate(startDate);
        this.endDate = AppTools.parseDate(endDate);
        this.customerID = customerID;
        this.serviceID = serviceID;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    @Override
    public int compare(Booking o1, Booking o2) {
        return o1.getStartDate().compareTo(o2.startDate);
    }

}
