package Service;

import Model.Booking;
import Model.Facility;
import Repository.BookingRepository;
import View.AppTools;

import java.time.LocalDate;
import java.util.TreeSet;

public class BookingService implements IBookingService {
    private TreeSet<Booking> bookingList;
    private final BookingRepository bkRepository;
    private final String errMsg;
    private final CustomerService customerService;
    private final FacilityService facilityService;

    public BookingService() {
        bkRepository = new BookingRepository();
        bookingList = bkRepository.readFile();
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
        if (bookingList.isEmpty()) {
            System.out.println("-> The List Is Empty.");
            return;
        }

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
            Booking existingBooking = findByID(updatedBooking.getBookingID());
            if (existingBooking != null) {
                bookingList.remove(existingBooking);
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
        for (Booking b : bookingList) {
            if (b.getBookingID().equalsIgnoreCase(ID)) {
                return b;
            }
        }
        return null;
    }

    public void addBooking() {
        try {
            do {
                String cusID = customerService.getCustomerID();
                String faciID = facilityService.getFacilityID();

                String bookingID;
                do {
                    bookingID = AppTools.validateID("Booking ID", errMsg, "^BK\\d{3}$");
                    if (findByID(bookingID) != null) {
                        System.out.println("-> Duplicated ID, Try Again.");
                    }
                } while (findByID(bookingID) != null);

                LocalDate bookingDate = AppTools.validateBookingDate("Booking Date", errMsg);
                LocalDate startDate = AppTools.validateStartDate(bookingDate, "Start Date", errMsg);
                LocalDate endDate = AppTools.validateEndDate(startDate, "End Date", errMsg);

                add(new Booking(bookingID, AppTools.localDateToString(bookingDate),
                        AppTools.localDateToString(startDate),
                        AppTools.localDateToString(endDate), cusID, faciID));

                Facility foundFacility = facilityService.findByID(faciID);
                if (foundFacility != null) {
                    Integer usageCount = facilityService.getCurrentFacilities().getOrDefault(foundFacility, 0) + 1;
                }

                if (AppTools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
                    save();
                } else {
                    System.out.println("-> Booking not saved.");
                }
            } while (AppTools.validateStringInput("-> Do You Want To Continue Adding Booking (Y/N)", errMsg).equalsIgnoreCase("Y"));
        } catch (Exception e) {
            System.out.println("-> Error While Adding Booking: " + e.getMessage());
        }
    }

}
