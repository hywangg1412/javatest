package Service;

import Model.Customer;
import Repository.CustomerRepository;
import View.AppTools;
import View.Menu;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;

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
        try {
            if (!currentCustomer.isEmpty()) {
                System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s\n",
                        "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Address", "Customer Type");
                System.out.println("---------------------------------------------------------------------------------------------------");

                for (Customer customer : currentCustomer) {
                    System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s\n",
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
                System.out.println("---------------------------------------------------------------------------------------------------");
            } else {
                System.out.println("-> The list is empty !!");
            }
        } catch (Exception e) {
            System.out.println("Error displaying customers: " + e.getMessage());
        }
    }

    @Override
    public void add(Customer entity) {
        try {
            currentCustomer.add(entity);
            System.out.println("-> Customer Added Successfully (not saved to file yet) !!!");
        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            customerRepository.writeFile(currentCustomer);
            System.out.println("-> Customers saved to file successfully !!!");
        } catch (Exception e) {
            System.out.println("Error saving customers to file: " + e.getMessage());
        }
    }

    @Override
    public void update(Customer c) {
        // Implementation if needed
    }

    public void updateEmp() {
        try {
            String editID = tools.validateID("Enter Customer ID to Edit", "ID Must Follow CUS-0000", "CUS-\\d{4}");
            Customer foundCus = findByID(editID);

            if (foundCus == null) {
                System.out.println("-> Not Found Customer With ID " + editID);
            } else {
                String[] editOptions = {
                        "Full Name", "Date of Birth", "CMND", "Gender",
                        "Phone Number", "Email", "Address", "Customer Type", "Finish Editing"
                };

                Menu<String> editMenu = new Menu<>(editOptions, "---- CUSTOMIZE CUSTOMER INFORMATION ----") {
                    @Override
                    public void execute(int n) {
                        try {
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
                        } catch (Exception e) {
                            System.out.println("Error updating customer: " + e.getMessage());
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
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    @Override
    public Customer findByID(String ID) {
        try {
            for (Customer customer : currentCustomer) {
                if (customer.getID().equalsIgnoreCase(ID)) {
                    return customer;
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding customer by ID: " + e.getMessage());
        }
        return null;
    }

    public void updateCustomer(Customer updatedCustomer) {
        try {
            for (int i = 0; i < currentCustomer.size(); i++) {
                Customer cus = currentCustomer.get(i);
                if (cus.getID().equals(updatedCustomer.getID())) {
                    currentCustomer.set(i, updatedCustomer);
                    System.out.println("-> Customer With ID " + updatedCustomer.getID() + " Updated successfully !!!");
                    return;
                }
            }
            System.out.println("-> Customer With ID " + updatedCustomer.getID() + " Is Not Found !!!");
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    public boolean isDuplicateID(String ID) {
        try {
            return currentCustomer.stream().anyMatch(cus -> cus.getID().equalsIgnoreCase(ID));
        } catch (Exception e) {
            System.out.println("Error checking for duplicate ID: " + e.getMessage());
            return false;
        }
    }

    public void addCustomer() {
        do {
            try {
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

                add(new Customer(ID, name, DOB, isMale, CMND, phoneNum, email, address, type));

                if (tools.validateStringInput("-> Do you want to save the customer data to file (Y/N)", errMsg).equalsIgnoreCase("Y")) {
                    save();
                }
            } catch (Exception e) {
                System.out.println("Error adding customer: " + e.getMessage());
            }
        } while (tools.validateStringInput("-> Do you want to continue adding customers (Y/N)", errMsg).equalsIgnoreCase("Y"));
    }

    private void updateFullName(Customer customer) {
        try {
            String newName = tools.validateStringInput("Enter New Full Name: ", errMsg);
            customer.setFullName(tools.normalizeName(newName));
            System.out.println("Full Name " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating full name: " + e.getMessage());
        }
    }

    private void updateDateOfBirth(Customer customer) {
        try {
            LocalDate newDOB = tools.validateDateOfBirth("Enter New Date of Birth (yyyy-mm-dd): ", errMsg);
            customer.setDOB(newDOB);
            System.out.println("Date of Birth " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating date of birth: " + e.getMessage());
        }
    }

    private void updateCMND(Customer customer) {
        try {
            String newCMND = tools.validateStringInput("Enter New CMND: ", errMsg);
            customer.setCMND(newCMND);
            System.out.println("CMND " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating CMND: " + e.getMessage());
        }
    }

    private void updateGender(Customer customer) {
        try {
            String newGender = tools.validateStringInput("Enter New Gender (Male/Female): ", errMsg);
            customer.setGender(newGender.equalsIgnoreCase("Male"));
            System.out.println("Gender " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating gender: " + e.getMessage());
        }
    }

    private void updatePhoneNumber(Customer customer) {
        try {
            String newPhoneNumber = tools.validateStringInput("Enter New Phone Number: ", errMsg);
            customer.setPhoneNumber(newPhoneNumber);
            System.out.println("Phone Number " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating phone number: " + e.getMessage());
        }
    }

    private void updateEmail(Customer customer) {
        try {
            String newEmail = tools.validateStringInput("Enter New Email: ", errMsg);
            customer.setEmail(newEmail);
            System.out.println("Email " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating email: " + e.getMessage());
        }
    }

    private void updateAddress(Customer customer) {
        try {
            String newAddress = tools.validateStringInput("Enter New Address: ", errMsg);
            customer.setAddress(newAddress);
            System.out.println("Address " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating address: " + e.getMessage());
        }
    }

    private void updateCustomerType(Customer customer) {
        try {
            String newType = tools.validateStringInput("Enter New Customer Type: ", errMsg);
            customer.setCustomerType(newType);
            System.out.println("Customer Type " + updatedMsg);
        } catch (Exception e) {
            System.out.println("Error updating customer type: " + e.getMessage());
        }
    }
}
