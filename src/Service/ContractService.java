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
    Set<Contract> contractList;
    private final ContractRepository contractRepository;
    private final BookingRepository bookingRepository;
    private final FacilityService facilityService;
    private final BookingService bookingService;
    private final String errMsg;

    public ContractService() {
        bookingService = new BookingService();
        bookingRepository = new BookingRepository();
        contractRepository = new ContractRepository();
        facilityService = new FacilityService();
        contractList = contractRepository.readFile();
        errMsg = "-> Invalid Input, Please Try Again";
    }

    public Set<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(Set<Contract> contractList) {
        this.contractList = contractList;
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
        display();
        try {
            if (bookingService.getBookingList().isEmpty()) {
                System.out.println("-> No bookings available to create a contract.");
                return;
            }

            System.out.println("Available Bookings:");
            bookingService.display();

            String bookingID;
            do {
                bookingID = AppTools.validateID("Booking ID", "Invalid ID, try again", "^BK\\d{3}$");
                if (bookingService.findByID(bookingID) == null) {
                    System.out.println("-> Booking ID Not Found ");
                }
            } while (bookingService.findByID(bookingID) == null);
            Booking selectedBooking = bookingService.findByID(bookingID);

            if (selectedBooking == null) {
                System.out.println("-> Booking not found.");
                return;
            }

            int contractNum;
            do {
                contractNum = AppTools.validateInteger("Contract Number", "Invalid number, try again", 0);
                if (findByContractNum(contractNum) != null) {
                    System.out.println("-> Duplicated Contract Num, Try Another One");
                }
            } while (findByContractNum(contractNum) != null);

            double depositAmount;
            double totalAmount;
            display();
            do {
                depositAmount = AppTools.validateDouble("Deposit Amount", "Invalid amount, try again", 0);
                totalAmount = getTotalPayment(bookingID) - depositAmount;
                display();

                if (totalAmount <= 0) {
                    System.out.println("-> Invalid Total Amount, Lower Your Deposit Amount ");
                }
            } while (totalAmount <= 0);
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

        double baseCost = 0, areaCost = 0;

        switch (foundFacility.getRentalType().toLowerCase()) {
            case "day" -> baseCost = totalDays * foundFacility.getRentalCost();
            case "week" -> baseCost = (totalDays / 7) * foundFacility.getRentalCost();
            case "month" -> baseCost = (totalDays / 30) * foundFacility.getRentalCost();
            default -> System.out.println("-> Invalid rental type.");
        }

        if (foundFacility.getArea() < 100) {
            areaCost = 0;
        } else if (foundFacility.getArea() >= 100 && foundFacility.getArea() < 300) {
            areaCost = (foundFacility.getArea() - 100) * 15;
        } else {
            areaCost = (foundFacility.getArea() - 300) * 25 + (200 * 15);
        }
        return (baseCost + areaCost) * foundFacility.getRentalCost();
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
            for (Contract contract : getContractList()) {
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

                int choice = AppTools.validateInteger("Enter Your Choice", errMsg, 1);

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
                            int contractNum;
                            do {
                                contractNum = AppTools.validateInteger("Contract Number", "Invalid number, try again", 0);
                                if (findByContractNum(contractNum) != null) {
                                    System.out.println("-> Duplicated Contract Num, Try Another One");
                                }
                            } while (findByContractNum(contractNum) != null);
                            selectedField.set(c, contractNum);
                        }
                        case 2 -> {
                            String bookingID = AppTools.validateID("Booking ID", "Invalid ID, try again", "^BK\\d{3}$");
                            selectedField.set(c, bookingID);
                        }
                        case 3 -> {
                            double depositAmount = AppTools.validateDouble("Deposit Amount", "Invalid amount, try again", 1);
                            selectedField.set(c, depositAmount);
                        }
                        case 4 -> {
                            double totalAmount = AppTools.validateDouble("Total Amount", errMsg, 1);
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
