package Model;

import View.AppTools;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Objects;

public class Booking implements Comparable<Booking> {
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
    public int compareTo(Booking o) {
        int dateComparision = this.startDate.compareTo(o.startDate);
        if (dateComparision == 0){
            return this.bookingID.compareTo(o.bookingID);
        }
        return dateComparision;
    }

    // Override equals and hashCode to avoid duplicate entries
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingID.equals(booking.bookingID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingID);
    }

}
