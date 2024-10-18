package Service;

import Model.Booking;
import Model.Customer;
import Repository.PromotionRepository;
import View.AppTools;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

public class PromotionService implements IPromotionService {
    BookingService bookingService;
    CustomerService customerService;
    PromotionRepository promotionRepository;

    AppTools tools;

    Stack<Customer> customersWithVouchers;
    List<Double> voucherList;

    String errMsg;

    public PromotionService() {
        bookingService = new BookingService();
        customerService = new CustomerService();
        promotionRepository = new PromotionRepository();

        customersWithVouchers = new Stack<>();
        voucherList = new ArrayList<>();

        promotionRepository.readFile(customersWithVouchers, voucherList);

        tools = new AppTools();

        errMsg = "-> Invalid Input, Try Again";


    }

    public static void main(String[] args) {
        PromotionService promotionService = new PromotionService();
        System.out.println("Customers with vouchers: " + promotionService.customersWithVouchers.size());
        System.out.println("Voucher list: " + promotionService.voucherList.size());
    }

    public void displayCustomerWithVoucher() {
        if (customersWithVouchers.size() != voucherList.size()) {
            System.out.println("-> Customer List And Voucher List Not Match.");
            return;
        }
        if (!customersWithVouchers.isEmpty()) {
            String rowFormat = "| %-4s | %-23s | %-14s | %-11.2f |\n";
            String lineSeparator = "+------+-------------------------+----------------+-------------+";

            System.out.println(lineSeparator);
            System.out.printf("| %-4s | %-23s | %-14s | %-11s |\n", "ID", "Full Name", "Phone Number", "Voucher (%)");
            System.out.println(lineSeparator);

            for (int i = 0; i < customersWithVouchers.size(); i++) {
                Customer customer = customersWithVouchers.get(i);
                Double voucher = voucherList.get(i);
                System.out.printf(rowFormat, customer.getID(), customer.getFullName(), customer.getPhoneNumber(), voucher);
            }
            System.out.println(lineSeparator);
        } else {
            System.out.println("-> The List Is Empty");
        }
    }

    public void inputYear() {
        int year = tools.validateInteger("Enter Year You Want To View", errMsg, 0);
        showCustomerByYear(year);
    }

    public void inputVoucher() {
        int v10 = tools.validateInteger("Enter Number Of 10% Voucher", errMsg, 0);
        int v20 = tools.validateInteger("Enter Number Of 20% Voucher", errMsg, 0);
        int v50 = tools.validateInteger("Enter Number Of 50% Voucher", errMsg, 0);

        if (v10 + v20 + v50 == 0) {
            System.out.println("-> There No Voucher To Distribute");
            return;
        }
        Voucher(v10, v20, v50);
    }

    private void Voucher(int v10, int v20, int v50) {
        customersWithVouchers = new Stack<>();
        voucherList = new ArrayList<>();

        LocalDate now = LocalDate.now();
        for (Booking booking : bookingService.getBookingList()) {
            if (booking.getBookingDate().getMonthValue() == now.getMonthValue()) {
                Customer customer = customerService.findByID(booking.getCustomerID());
                if (customer != null) {
                    if (customersWithVouchers.contains(customer)) {
                        customersWithVouchers.push(customer);
                    } else {
                        System.out.println("-> Customer " + customer.getFullName() + " already has a voucher.");
                    }
                } else {
                    System.out.println("-> Customer With ID " + booking.getCustomerID() + " Not Found.");
                }
            }
        }

        if (customersWithVouchers.isEmpty()) {
            System.out.println("-> No customers eligible for vouchers this month.");
            return;
        }

        List<Customer> tempList = new ArrayList<>(customersWithVouchers);
        try {
            for (Customer customer : tempList) {
                if (v10 > 0) {
                    v10--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 10%.");
                    voucherList.add(0.1);
                } else if (v20 > 0) {
                    v20--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 20%.");
                    voucherList.add(0.2);
                } else if (v50 > 0) {
                    v50--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 50%.");
                    voucherList.add(0.5);
                }
            }
            promotionRepository.writeFile(customersWithVouchers, voucherList);
        } catch (Exception e) {
            throw new RuntimeException("-> Error While Allocating Voucher For Customer - " + e.getMessage());
        }
        display();
    }

    private void showCustomerByYear(int year) {
        TreeSet<Customer> customersYearList = new TreeSet<>();
        try {
            for (Booking booking : bookingService.getBookingList()) {
                if (booking.getBookingDate().getYear() == year) {
                    Customer customer = customerService.findByID(booking.getCustomerID());
                    customersYearList.add(customer);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("-> Error Orcur While Adding Customer To Year List: " + e.getMessage());
        }

        if (!customersYearList.isEmpty()) {
            try {
                System.out.println("+------------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+");
                System.out.printf("| %-10s | %-20s | %-10s | %-10s | %-15s | %-10s | %-27s | %-15s | %-12s |\n",
                        "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Address", "Customer Type");
                System.out.println("+------------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+");
                for (Customer customer : customersYearList) {
                    System.out.printf("| %-10s | %-20s | %-10s | %-10s | %-15s | %-10s | %-27s | %-15s | %-12s |\n",
                            customer.getID(),
                            customer.getFullName(),
                            customer.getDOB(),
                            customer.isGender() ? "Male" : "Female",
                            customer.getCMND(),
                            customer.getPhoneNumber(),
                            customer.getEmail(),
                            customer.getAddress(),
                            customer.getCustomerType());
                }
                System.out.println("+------------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+");
            } catch (Exception e) {
                throw new RuntimeException("-> Error While Displaying Customer " + e.getMessage());
            }
        } else {
            System.out.println("-> There No Customer In Year " + year);
        }
    }

    @Override
    public void display() {
        try {
            if (customersWithVouchers.isEmpty()) {
                System.out.println("-> No Customers have received vouchers.");
            } else {
                System.out.println("+----------+-------------------------+-----------------+-------------+");
                System.out.printf("| %-20s | %-23s | %-15s | %-11s |\n", "ID", "Full Name", "Phone Number", "Voucher (%)");
                System.out.println("+----------+-------------------------+-----------------+-------------+");

                for (int i = 0; i < customersWithVouchers.size(); i++) {
                    Customer customer = customersWithVouchers.get(i);
                    double voucherValue = voucherList.get(i);
                    System.out.printf("| %-20s | %-23s | %-15s | %-11.2f |\n",
                            customer.getID(),
                            customer.getFullName(),
                            customer.getPhoneNumber(),
                            voucherValue);
                }
            }
            System.out.println("+----------+-------------------------+-----------------+-------------+");
        } catch (Exception e) {
            throw new RuntimeException("-> Error While Displaying Customer " + e.getMessage());
        }
    }

    @Override
    public void add(Object entity) {
    }

    @Override
    public void save() {
    }

    @Override
    public void update(Object enity) throws IllegalAccessException {
    }

    @Override
    public Object findByID(String ID) {
        return null;
    }

    @Override
    public void sort() {

    }
}
