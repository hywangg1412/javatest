package Service;

import Model.Booking;
import Model.Facility;
import Repository.BookingRepository;
import View.AppTools;

import java.time.LocalDate;

import java.time.temporal.ChronoUnit;
import java.util.TreeSet;

public class BookingService implements IBookingService {
    public TreeSet<Booking> bookingList;
    private final BookingRepository bkRepository;

    private final AppTools tools;
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

    public TreeSet<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(TreeSet<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public void display() {
        try {
            if (!bookingList.isEmpty()) {
                System.out.println("+------------+-----------------+--------------+--------------+-----------------+--------------+");
                System.out.printf("| %-10s | %-15s | %-12s | %-12s | %-15s | %-12s |\n",
                        "Booking ID", "Booking Date", "Start Date", "End Date", "Customer ID", "Service ID");
                System.out.println("+------------+-----------------+--------------+--------------+-----------------+--------------+");
                for (Booking booking : bookingList) {
                    System.out.printf("| %-10s | %-15s | %-12s | %-12s | %-15s | %-12s |\n",
                            booking.getBookingID(),
                            AppTools.localDateToString(booking.getBookingDate()),
                            AppTools.localDateToString(booking.getStartDate()),
                            AppTools.localDateToString(booking.getEndDate()),
                            booking.getCustomerID(),
                            booking.getServiceID());

                }
                System.out.println("+------------+-----------------+--------------+--------------+-----------------+--------------+");

            } else {
                System.out.println("-> The List Is Empty.");
            }
        } catch (Exception e) {
            System.out.println("-> Error While Displaying Booking List: " + e.getMessage());
        }
    }

    @Override
    public void add(Booking booking) {
        try {
            bookingList.add(booking);
            System.out.println("-> Booking Added Successfully!");
        } catch (Exception e) {
            System.out.println("-> Error While Adding Booking: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            bkRepository.writeFile(bookingList);
            System.out.println("-> Booking Saved Successfully!");
        } catch (Exception e) {
            System.out.println("-> Error While Saving Data: " + e.getMessage());
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
                System.out.println("-> Booking Not Found.");
            }
        } catch (Exception e) {
            System.out.println("-> Error While Updating Booking: " + e.getMessage());
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
            System.out.println("-> Error While Finding Booking by ID " + ID + ": " + e.getMessage());
        }
        return null;
    }

    public void addBooking() {
        try {
            do {
                customerService.display();
                String cusID = customerService.getCustomerID();

                facilityService.display();
                String faciID = facilityService.getFacilityID();

                String bookingID;
                do {
                    bookingID = tools.validateID("Booking ID", errMsg, "^BK\\d{3}$");
                    if (findByID(bookingID) != null){
                        System.out.println("-> Duplicated ID , Try Again.");
                    }
                }while (findByID(bookingID) != null);

                LocalDate bookingDate = tools.validateBookingDate("Booking Date", errMsg);
                LocalDate startDate = tools.validateStartDate(bookingDate, "Start Date", errMsg);
                LocalDate endDate = tools.validateEndDate(startDate, "End Date", errMsg);

                add(new Booking(bookingID, AppTools.localDateToString(bookingDate), AppTools.localDateToString(startDate), AppTools.localDateToString(endDate), cusID, faciID));

                Facility foundFacility = facilityService.findByID(faciID);

                // Increase Usage Count When Creating Booking
                if (foundFacility != null){
                    Integer usageCount = facilityService.getCurrentFacilities().get(foundFacility);
                    if (usageCount == null){
                        usageCount = 0;
                    }
                     usageCount++;
                }

                // Save
                if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
                    save();
                } else {
                    System.out.println("-> Booking not saved.");
                }
            } while (tools.validateStringInput("Do You Want To Continue Adding Booking (Y/N)", errMsg).equalsIgnoreCase("Y"));
        } catch (Exception e) {
            System.out.println("-> Error While Adding Booking: " + e.getMessage());
        }
    }


}
