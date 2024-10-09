package Model;

public class Contract {
    private int contractNum;
    private String bookingID;
    private double depositAmmount;
    private double totalPayment;

    public Contract(int contractNum, String bookingID, double depositAmmount, double totalPayment) {
        this.contractNum = contractNum;
        this.bookingID = bookingID;
        this.depositAmmount = depositAmmount;
        this.totalPayment = totalPayment;
    }

    public int getContractNum() {
        return contractNum;
    }

    public void setContractNum(int contractNum) {
        this.contractNum = contractNum;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public double getDepositAmmount() {
        return depositAmmount;
    }

    public void setDepositAmmount(double depositAmmount) {
        this.depositAmmount = depositAmmount;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
}
