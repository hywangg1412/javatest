package Service;

import Model.Customer;
import Repository.CustomerRepository;
import View.AppTools;
import View.Menu;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;

public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final AppTools tools;
    private final String errMsg;
    private final String updatedMsg;
    private final ArrayList<Customer> currentCustomer;

    public CustomerService() {
        customerRepository = new CustomerRepository();
        currentCustomer = customerRepository.readFile();
        tools = new AppTools();
        errMsg = "-> Invalid Input, Try Again";
        updatedMsg = "Updated Successfully !!!";
    }

    @Override
    public void display() {
        if (!currentCustomer.isEmpty()) {
            System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s\n",
                    "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Address", "Customer Type");
            System.out.println("---------------------------------------------------------------------------------------------------");

            for (Customer customer : currentCustomer) {
                System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s\n",
                        customer.getID(),
                        customer.getFullname(),
                        customer.getDOB(),
                        customer.isGender() ? "Male" : "Female",
                        customer.getCMND(),
                        customer.getPhoneNumber(),
                        customer.getEmail(),
                        customer.getAddress(),
                        customer.getCustomerType());
            }
            System.out.println("---------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("-> The list is empty !!");
        }
    }

    @Override
    public void add(Customer entity) {
        currentCustomer.add(entity);
        System.out.println("-> Customer Added Successfully (not saved to file yet) !!!");
    }

    @Override
    public void save() {
        customerRepository.writeFile(currentCustomer);
        System.out.println("-> Customers saved to file successfully !!!");
    }

    @Override
    public void edit() {
        String editID = tools.validateID("Enter Customer ID to Edit", "ID Must Follow CUS-0000", "CUS-\\d{4}");
        ArrayList<Customer> found = searchCustomer(cus -> cus.getID().equalsIgnoreCase(editID));

        if (found.isEmpty()) {
            System.out.println("-> Not Found Customer With ID " + editID);
        } else {
            Customer foundCus = found.get(0);
            String[] editOptions = {
                    "Full Name", "Date of Birth", "CMND", "Gender",
                    "Phone Number", "Email", "Address", "Customer Type", "Finish Editing"
            };

            Menu<String> editMenu = new Menu<>(editOptions, "---- CUSTOMIZE CUSTOMER INFORMATION ----") {
                @Override
                public void execute(int n) {
                    switch (n) {
                        case 1 -> updateFullName(foundCus);
                        case 2 -> updateDateOfBirth(foundCus);
                        case 3 -> updateCMND(foundCus);
                        case 4 -> updateGender(foundCus);
                        case 5 -> updatePhoneNumber(foundCus);
                        case 6 -> updateEmail(foundCus);
                        case 7 -> updateAddress(foundCus);
                        case 8 -> updateCustomerType(foundCus);
                        case 9 -> System.out.println("Finished editing.");
                        default -> System.out.println(errMsg);
                    }
                }
            };

            do {
                editMenu.run();
            } while (tools.validateStringInput("-> Do you want to continue editing customers (Y/N)", errMsg).equalsIgnoreCase("Y"));

            if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
                updateCustomer(foundCus);
                save();
            }
        }
    }

    // Update an existing customer
    public void updateCustomer(Customer updatedCustomer) {
        for (int i = 0; i < currentCustomer.size(); i++) {
            Customer cus = currentCustomer.get(i);
            if (cus.getID().equals(updatedCustomer.getID())) {
                currentCustomer.set(i, updatedCustomer);
                System.out.println("-> Customer With ID " + updatedCustomer.getID() + " Updated successfully !!!");
                return;
            }
        }
        System.out.println("-> Customer With ID " + updatedCustomer.getID() + " Is Not Found !!!");
    }

    // Check for duplicate customer ID
    public boolean isDuplicateID(String ID) {
        return currentCustomer.stream().anyMatch(cus -> cus.getID().equalsIgnoreCase(ID));
    }

    // Search for customers based on a predicate
    public ArrayList<Customer> searchCustomer(Predicate<Customer> criteria) {
        ArrayList<Customer> searchResults = new ArrayList<>();
        for (Customer customer : currentCustomer) {
            if (criteria.test(customer)) {
                searchResults.add(customer);
            }
        }
        return searchResults;
    }

    // Add a new customer with input validation
    public void addCustomer() {
        do {
            String ID;
            do {
                ID = tools.validateID("Customer ID", "ID Must Follow CUS-0000", "CUS-\\d{4}");
            } while (isDuplicateID(ID));

            String name = tools.normalizeName(tools.validateStringInput("Customer Full Name", errMsg));
            LocalDate DOB = tools.validateDateOfBirth("Customer Date Of Birth", errMsg);
            String CMND = tools.validateIDCard("Customer CMND", errMsg);
            boolean isMale = tools.validateGender("Gender (Male (M) / Female (F))", errMsg).equalsIgnoreCase("Male");
            String phoneNum = tools.validatePhoneNumber("Customer Phone Number", errMsg);
            String email = tools.validateEmail("Customer Email", errMsg);
            String address = tools.validateStringInput("Customer Address", errMsg);
            String type = tools.validateStringInput("Customer Type", errMsg);

            add(new Customer(ID, name, AppTools.localDateToString(DOB), isMale, CMND, phoneNum, email, address, type));

            if (tools.validateStringInput("-> Do you want to save the customer data to file (Y/N)", errMsg).equalsIgnoreCase("Y")) {
                save();
            }
        } while (tools.validateStringInput("-> Do you want to continue adding customers (Y/N)", errMsg).equalsIgnoreCase("Y"));
    }

    // Individual update methods
    private void updateFullName(Customer customer) {
        String newName = tools.validateStringInput("Enter New Full Name: ", errMsg);
        customer.setFullname(tools.normalizeName(newName));
        System.out.println("Full Name " + updatedMsg);
    }

    private void updateDateOfBirth(Customer customer) {
        LocalDate newDOB = tools.validateDateOfBirth("Enter New Date of Birth (yyyy-mm-dd): ", errMsg);
        customer.setDOB(newDOB);
        System.out.println("Date of Birth " + updatedMsg);
    }

    private void updateCMND(Customer customer) {
        String newCMND = tools.validateStringInput("Enter New CMND: ", errMsg);
        customer.setCMND(newCMND);
        System.out.println("CMND " + updatedMsg);
    }

    private void updateGender(Customer customer) {
        String newGender = tools.validateStringInput("Enter New Gender (Male/Female): ", errMsg);
        customer.setGender(newGender.equalsIgnoreCase("Male"));
        System.out.println("Gender " + updatedMsg);
    }

    private void updatePhoneNumber(Customer customer) {
        String newPhoneNumber = tools.validateStringInput("Enter New Phone Number: ", errMsg);
        customer.setPhoneNumber(newPhoneNumber);
        System.out.println("Phone Number " + updatedMsg);
    }

    private void updateEmail(Customer customer) {
        String newEmail = tools.validateStringInput("Enter New Email: ", errMsg);
        customer.setEmail(newEmail);
        System.out.println("Email " + updatedMsg);
    }

    private void updateAddress(Customer customer) {
        String newAddress = tools.validateStringInput("Enter New Address: ", errMsg);
        customer.setAddress(newAddress);
        System.out.println("Address " + updatedMsg);
    }

    private void updateCustomerType(Customer customer) {
        String newType = tools.validateStringInput("Enter New Customer Type: ", errMsg);
        customer.setCustomerType(newType);
        System.out.println("Customer Type " + updatedMsg);
    }
}
