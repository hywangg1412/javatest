package Service;

import Model.Booking;
import Model.Customer;
import Repository.PromotionRepository;
import View.AppTools;

import java.time.LocalDate;
import java.util.Stack;
import java.util.TreeSet;

public class PromotionService implements IPromotionService {
    BookingService bookingService;
    CustomerService customerService;
    PromotionRepository promotionRepository;

    AppTools tools;

    TreeSet<Customer> customerWithVoucher;

    String errMsg;

    public PromotionService() {
        bookingService = new BookingService();
        customerService = new CustomerService();
        promotionRepository = new PromotionRepository();


        customerWithVoucher = promotionRepository.readFile();

        tools = new AppTools();

        errMsg = "-> Invalid Input, Try Again";
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


    public void inputYear() {
        int year = tools.validateInteger("Enter Year You Want To View", errMsg, 0);
        showCustomerByYear(year);
    }

    public void inputVoucher() {
        int v10 = tools.validateInteger("Enter Number Of 10% Voucher", errMsg, 0);
        int v20 = tools.validateInteger("Enter Number Of 20% Voucher", errMsg, 0);
        int v50 = tools.validateInteger("Enter Number Of 50% Voucher", errMsg, 0);
        Voucher(v10, v20, v50);
    }

    private void Voucher(int v10, int v20, int v50) {
        Stack<Customer> voucherList = new Stack<>();
        try {
            LocalDate now = LocalDate.now();
            for (Booking booking : bookingService.getBookingList()) {
                if (booking.getBookingDate().getMonthValue() == now.getMonthValue()) {
                    Customer customer = customerService.findByID(booking.getCustomerID());
                    if (customer != null) {
                        if (!voucherList.contains(customer)){
                            voucherList.push(customer);
                        } else {
                            System.out.println("-> Customer Already In The List.");
                        }
                    } else {
                        System.out.println("-> Customer With ID " + booking.getCustomerID() + " Not Found.");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("-> Error While Push Customer Into Voucher List - " + e.getMessage());
        }

        try {
            while (!voucherList.isEmpty() && (v10 + v20 + v50) > 0) {
                Customer customer = voucherList.pop();
                if (v10 > 0) {
                    customer.setVoucher(0.1);
                    v10--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 10%.");
                } else if (v20 > 0) {
                    customer.setVoucher(0.2);
                    v20--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 20%.");
                } else if (v50 > 0) {
                    customer.setVoucher(0.5);
                    v50--;
                    System.out.println("Customer " + customer.getFullName() + " Have Voucher 50%.");
                }
                customerWithVoucher.add(customer);
            }
            try {
                promotionRepository.writeFile(customerWithVoucher);
            } catch (Exception e) {
                throw new RuntimeException("-> Error While Writing Voucher Data - " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("-> Error While Allocating Voucher For Customer - " + e.getMessage());
        }
        display();
    }

    @Override
    public void display() {
        try {
            System.out.println("+------------+----------------------+------------+");
            System.out.printf("| %-10s | %-20s | %-10s |\n", "ID", "Full Name", "Voucher");
            System.out.println("+------------+----------------------+------------+");

            if (customerWithVoucher.isEmpty()) {
                System.out.println("-> No Customers have received vouchers.");
            } else {
                for (Customer customer : customerWithVoucher) {
                    System.out.printf("| %-10s | %-20s | %-10.2f |\n",
                            customer.getID(),
                            customer.getFullName(),
                            customer.getVoucher());
                }
            }
            System.out.println("+------------+----------------------+------------+");
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
