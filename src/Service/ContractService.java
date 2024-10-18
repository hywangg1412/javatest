package Service;

import Model.Booking;
import Model.Contract;
import Model.Facility;
import Repository.BookingRepository;
import Repository.ContractRepository;
import View.AppTools;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

public class ContractService implements IContactService {
    private final AppTools tools;
    private final Set<Contract> contractList;
    private final ContractRepository contractRepository;
    private final BookingRepository bookingRepository;
    private final FacilityService facilityService;
    private final BookingService bookingService;
    private final String errMsg;

    public ContractService() {
        bookingService = new BookingService();
        tools = new AppTools();
        bookingRepository = new BookingRepository();
        contractRepository = new ContractRepository();
        facilityService = new FacilityService();
        contractList = contractRepository.readFile();
        errMsg = "-> Invalid Input, Please Try Again";
    }

    @Override
    public void add(Contract entity) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Contract cannot be null");
            }
            contractList.add(entity);
            System.out.println("-> Contract added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println("-> Error While Adding Contract: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("-> Unexpected error: " + e.getMessage());
        }
    }

    public void addContract() {
        try {
            if (bookingService.getBookingList().isEmpty()) {
                System.out.println("-> No bookings available to create a contract.");
                return;
            }

            System.out.println("Available Bookings:");
            bookingService.display();

            String bookingID = tools.validateID("Booking ID", "Invalid ID, try again", "^BK\\d{3}$");
            Booking selectedBooking = bookingService.findByID(bookingID);

            if (selectedBooking == null) {
                System.out.println("-> Booking not found.");
                return;
            }

            int contractNum;
            do {
                contractNum = tools.validateInteger("Contract Number", "Invalid number, try again", 0);
                if (findByContractNum(contractNum) != null) {
                    System.out.println("-> Duplicated Contract Num, Try Another One");
                }
            } while (findByContractNum(contractNum) != null);

            double depositAmount = tools.validateDouble("Deposit Amount", "Invalid amount, try again", 0);
            double totalAmount = getTotalPayment(bookingID) - depositAmount;
            Contract newContract = new Contract(contractNum, bookingID, depositAmount, totalAmount);
            add(newContract);
            System.out.println("-> Contract created successfully with Contract Number: " + contractNum);
            save();

        } catch (IllegalArgumentException e) {
            System.out.println("-> Error while adding contract: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("-> Unexpected error while adding contract: " + e.getMessage());
        }
    }

    public double getTotalPayment(String bookingID) {
        Booking foundBooking = bookingService.findByID(bookingID);
        Facility foundFacility = facilityService.findByID(foundBooking.getServiceID());

        LocalDate startDate = foundBooking.getStartDate();
        LocalDate endDate = foundBooking.getEndDate();

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);

        try {
            switch (foundFacility.getRentalType().toLowerCase()) {
                case "day":
                    return (totalDays * foundFacility.getRentalCost());
                case "week":
                    return ((totalDays / 7) * foundFacility.getRentalCost());
                case "month":
                    int yearDifference = endDate.getYear() - startDate.getYear();
                    int monthDifference = endDate.getMonthValue() - startDate.getMonthValue();

                    int duration = yearDifference * 12 + monthDifference;

                    // Adjust if start day is greater than end day
                    if (startDate.getDayOfMonth() > endDate.getDayOfMonth()) {
                        duration--;
                    }
                    return duration * foundFacility.getRentalCost();
                default:
                    System.out.println("-> Invalid rental type.");
                    return 0;
            }
        } catch (Exception e) {
            System.out.println("-> Error While Getting Duration: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void display() {
        if (contractList.isEmpty()) {
            System.out.println("-> No contracts available.");
            return;
        }
        try {
            System.out.println("+---------------+---------------+-------------------+---------------+");
            System.out.printf("| %-13s | %-13s | %-17s | %-13s |\n", "NO", "Booking ID", "Deposit Amount", "Total Payment");
            System.out.println("+---------------+---------------+-------------------+---------------+");
            for (Contract contract : contractList) {
                System.out.printf("| %-13d | %-13s | %-17.2f | %-13.2f |\n",
                        contract.getContractNum(),
                        contract.getBookingID(),
                        contract.getDepositAmmount(),
                        contract.getTotalPayment());
            }
            System.out.println("+---------------+---------------+-------------------+---------------+");

        } catch (Exception e) {
            System.out.println("-> Error while displaying contracts: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            contractRepository.writeFile(contractList);
        } catch (Exception e) {
            System.out.println("-> Error while saving contracts: " + e.getMessage());
        }
    }

    @Override
    public void update(Contract c) {
        try {
            Field[] fields = c.getClass().getDeclaredFields();
            int totalFields = fields.length;
            boolean isUpdate = true;

            while (isUpdate) {
                System.out.println("---- UPDATE CONTRACT ----");
                for (int i = 0; i < totalFields; i++) {
                    System.out.println((i + 1) + ". " + fields[i].getName());
                }
                System.out.println((totalFields + 1) + ". Finish Customize");

                int choice = tools.validateInteger("Enter Your Choice", errMsg, 1);

                if (choice == totalFields + 1) {
                    isUpdate = false;
                    System.out.println("-> Finish Update.");
                    continue;
                }

                if (choice < 1 || choice > totalFields + 1) {
                    System.out.println(errMsg);
                    continue;
                }

                Field selectedField = fields[choice - 1];
                selectedField.setAccessible(true);

                try {
                    switch (choice) {
                        case 1 -> {
                            String bookingID = tools.validateID("Booking ID", "Invalid ID, try again", "^BK\\d{3}$");
                            selectedField.set(c, bookingID);
                        }
                        case 2 -> {
                            int contractNum;
                            do {
                                contractNum = tools.validateInteger("Contract Number", "Invalid number, try again", 0);
                                if (findByContractNum(contractNum) != null) {
                                    System.out.println("-> Duplicated Contract Num, Try Another One");
                                }
                            } while (findByContractNum(contractNum) != null);
                            selectedField.set(c, contractNum);
                        }
                        case 3 -> {
                            double depositAmount = tools.validateDouble("Deposit Amount", "Invalid amount, try again", 1);
                            selectedField.set(c, depositAmount);
                        }
                        case 4 -> {
                            double totalAmount = tools.validateDouble("Total Amount", errMsg, 1);
                            selectedField.set(c, totalAmount);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("-> Error accessing field: " + e.getMessage());
                }
                save();
            }
        } catch (Exception e) {
            System.out.println("-> Error while updating contract: " + e.getMessage());
        }
    }

    @Override
    public Contract findByID(String ID) {
        try {
            for (Contract contract : contractList) {
                if (contract.getBookingID().equals(ID)) {
                    return contract;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("-> Error while finding contract by ID: " + e.getMessage());
            return null;
        }
    }


    public Contract findByContractNum(int contractNum) {
        try {
            for (Contract contract : contractList) {
                if (contract.getContractNum() == contractNum) {
                    return contract;
                }
            }
            return null;
        } catch (Exception e) {
            System.out.println("-> Error while finding contract by number: " + e.getMessage());
            return null;
        }
    }
}
