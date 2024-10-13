package Service;


import Model.Booking;
import Repository.BookingRepository;
import View.AppTools;

import java.time.LocalDate;
import java.util.Set;


public class BookingService implements IBookingService {
    private final Set<Booking> bookingList;
    private final AppTools tools;
    private final BookingRepository bkRepository;
    private final String errMsg;
    private final CustomerService customerService;
    private final FacilityService facilityService;

    public BookingService() {
        bkRepository = new BookingRepository();
        bookingList = bkRepository.readFile();
        tools = new AppTools();
        customerService = new CustomerService();
        facilityService = new FacilityService();
        errMsg = "-> Invalid Input, Try Again.";
    }

    @Override
    public void display() {
        try {
            if (!bookingList.isEmpty()) {
                System.out.printf("%-10s %-15s %-10s %-10s %-15s %-10s\n",
                        "Booking ID", "Booking Date", "Start Date", "End Date", "Customer ID", "Service ID");
                System.out.println("--------------------------------------------------------------------------------");
                for (Booking booking : bookingList) {
                    System.out.printf("%-10s %-15s %-10s %-10s %-15s %-10s\n",
                            booking.getBookingID(),
                            AppTools.localDateToString(booking.getBookingDate()),
                            AppTools.localDateToString(booking.getStartDate()),
                            AppTools.localDateToString(booking.getEndDate()),
                            booking.getCustomerID(),
                            booking.getServiceID());
                }
                System.out.println("--------------------------------------------------------------------------------");
            } else {
                System.out.println("-> The List Is Empty.");
            }
        } catch (Exception e) {
            System.out.println("-> Error While Display Booking List - " + e.getMessage());
        }
    }

    @Override
    public void add(Booking booking) {
        try {
            bookingList.add(booking);
            System.out.println("-> Booking Add Successfully!");
        } catch (Exception e) {
            System.out.println("-> Error While Adding Booking - " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            bkRepository.writeFile(bookingList);
            System.out.println("-> Booking saved successfully!");
        } catch (Exception e) {
            System.out.println("-> Error While Saving Data - " + e.getMessage());
        }
    }

    @Override
    public void update(Booking updatedBooking) {
        try {
            Booking existBooking = findByID(updatedBooking.getBookingID());
            if (existBooking != null) {
                bookingList.remove(existBooking);
                bookingList.add(updatedBooking);
                System.out.println("-> Booking ID " + updatedBooking.getBookingID() + " Updated Successfully!");
            } else {
                System.out.println("-> Booking Not Found");
            }
        } catch (Exception e) {
            System.out.println("-> Error While Updating Booking - " + e.getMessage());
        }
    }


    @Override
    public Booking findByID(String ID) {
        try {
            for (Booking b : bookingList) {
                if (b.getBookingID().equalsIgnoreCase(ID)) {
                    return b;
                }
            }
        } catch (Exception e) {
            System.out.println("-> Error While Finding ID " + ID + e.getMessage());
        }
        return null;
    }

    public void addBooking() {
        do {
            customerService.display();
            String cusID = getCustomerID();

            facilityService.display();
            String faciID = getFacilityID();

            String bookingID = tools.validateID("Booking ID", errMsg, "^BK\\d{3}$");
            LocalDate bookingDate = tools.validateBookingDate("Booking Date", errMsg);
            LocalDate startDate = tools.validateStartDate(bookingDate, "Start Date", errMsg);
            LocalDate endDate = tools.validateEndDate(startDate, "End Date", errMsg);

            add(new Booking(bookingID, AppTools.localDateToString(bookingDate), AppTools.localDateToString(startDate), AppTools.localDateToString(endDate), cusID, faciID));

            if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
                save();
            } else {
                System.out.println("-> Booking not saved.");
            }
        } while (tools.validateStringInput("Do You Want To Continue Adding Booking (Y/N)", errMsg).equalsIgnoreCase("Y"));
    }

    private String getCustomerID() {
        String cusID;
        do {
            cusID = tools.validateID("Customer ID", errMsg, "CUS-\\d{4}");
            if (customerService.findByID(cusID) == null) {
                System.out.println("-> ID Not Found, Try Again!");
            }
        } while (customerService.findByID(cusID) == null);
        return cusID;
    }

    private String getFacilityID() {
        String faciID;
        do {
            faciID = tools.validateID("Facility ID", "ID Must Follow SVxx-xxxx", "SV(VL|HO|RO)-\\d{4}");
            if (facilityService.findByID(faciID) == null) {
                System.out.println("-> ID Not Found, Try Again!");
            }
        } while (facilityService.findByID(faciID) == null);
        return faciID;
    }

    public void createContracts() {
    }
}
