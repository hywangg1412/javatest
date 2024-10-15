package Service;

import Model.Booking;
import Model.Contract;
import Repository.BookingRepository;
import Repository.ContractRepository;
import View.AppTools;

import java.lang.reflect.Field;
import java.util.Set;

public class ContractService implements IContactService {
    private final AppTools tools;
    private final Set<Contract> contractList;
    private final ContractRepository contractRepository;
    private final Set<Booking> bookingList;

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;
    private final String errMsg;

    public ContractService() {
        bookingService = new BookingService();

        tools = new AppTools();

        bookingRepository = new BookingRepository();
        contractRepository = new ContractRepository();

        contractList = contractRepository.readFile();
        bookingList = bookingRepository.readFile();

        errMsg = "-> Invalid Input, Please Try Again";
    }

    @Override
    public void add(Contract entity) {
        try {
            contractList.add(entity);
            System.out.println("-> Contract added successfully!");
        } catch (Exception e) {
            System.out.println("-> Error While Adding Contract: " + e.getMessage());
        }
    }

    public void addContract() {
        try {
            if (bookingList.isEmpty()) {
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
                if (findByContractNum(contractNum) == null) {
                    System.out.println("-> Duplicated Contract Num, Try Another One");
                }
            } while (findByContractNum(contractNum) == null);

            double depositAmount = tools.validateDouble("Deposit Amount", "Invalid amount, try again", 0);
            double totalAmount = calculateTotalAmount(selectedBooking);
            Contract newContract = new Contract(contractNum, bookingID, depositAmount, totalAmount);
            add(newContract);
            System.out.println("-> Contract created successfully with Contract Number: " + contractNum);
            save();

        } catch (Exception e) {
            System.out.println("-> Error while adding contract: " + e.getMessage());
        }
    }

    @Override
    public void display() {
        try {
            if (contractList.isEmpty()) {
                System.out.println("-> No contracts available.");
                return;
            }
            System.out.println("+---------------+---------------+-------------------+---------------+");
            System.out.printf("| %-13s | %-13s | %-17s | %-13s |\n",
                    "NO", "Booking ID", "Deposit Amount", "Total Payment");
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
    public void update(Contract updatedContract) {
        try {
            Field[] fields = updatedContract.getClass().getDeclaredFields();
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
                    Object currentValue = selectedField.get(updatedContract);
                    System.out.println("Current Value Of - " + selectedField.getName() + " - " + currentValue);
                    String newValue = tools.validateString("Enter New Value For " + selectedField.getName(), errMsg);

                    if (selectedField.getType() == String.class) {
                        selectedField.set(updatedContract, newValue);
                    } else if (selectedField.getType() == int.class) {
                        selectedField.setInt(updatedContract, Integer.parseInt(newValue));
                    } else if (selectedField.getType() == double.class) {
                        selectedField.setDouble(updatedContract, Double.parseDouble(newValue));
                    } else {
                        System.out.println("-> Unsupported Field Type - " + selectedField.getName());
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field: " + e.getMessage());
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
            return null;
        } catch (Exception e) {
            System.out.println("-> Error while finding contract by ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void sort() {

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

    private double calculateTotalAmount(Booking booking) {
        try {
            return 100.0;
        } catch (Exception e) {
            System.out.println("-> Error while calculating total amount: " + e.getMessage());
            return 0.0;
        }
    }
}
