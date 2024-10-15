package Service;

import Model.Customer;
import Repository.CustomerRepository;
import View.AppTools;

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
                System.out.println("+------------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+");
                System.out.printf("| %-10s | %-20s | %-10s | %-10s | %-15s | %-10s | %-27s | %-15s | %-12s |\n",
                        "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Address", "Customer Type");
                System.out.println("+------------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+");
                for (Customer customer : currentCustomer) {
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
        Field[] fields = c.getClass().getSuperclass().getDeclaredFields();
        Field[] subFields = c.getClass().getDeclaredFields();
        int totalField = fields.length + subFields.length;
        boolean isEditing = true;

        while (isEditing) {
            System.out.println("---- CUSTOMIZE CUSTOMER -----");
            for (int i = 0; i < fields.length; i++) {
                System.out.println((i + 1) + ". " + fields[i].getName());
            }
            for (int j = 0; j < subFields.length; j++) {
                System.out.println((fields.length + j + 1) + ". " + subFields[j].getName());
            }
            System.out.println((totalField + 1) + ". Finish Customize");

            int choice = tools.validateInteger("Choose Your Option", errMsg, 0);

            if (choice == totalField + 1) {
                isEditing = false;
                System.out.println("-> Finish Customize");
                continue;
            }

            Field selectedField;
            if (choice <= fields.length) {
                selectedField = fields[choice - 1];
            } else {
                selectedField = subFields[choice - fields.length - 1];
            }
            selectedField.setAccessible(true);
            try {
                if (selectedField.getType() == LocalDate.class) {
                    LocalDate DOB = tools.validateDateOfBirth("Enter New Value For Date Of Birth", errMsg);
                    selectedField.set(c, DOB);
                    System.out.println(selectedField.getName() + " Updated Successfully !!!");
                } else if (selectedField.getType() == double.class) {
                    double salary = tools.validateSalary("Employee Salary", errMsg);
                    selectedField.set(c, salary);
                    System.out.println(selectedField.getName() + " Updated Successfully !!!");
                } else if (selectedField.getType() == boolean.class) {
                    boolean isMale = tools.validateGender("Gender (Male (M) / Female (F))", errMsg).equalsIgnoreCase("Male");
                    selectedField.set(c, isMale);
                    System.out.println(selectedField.getName() + " Updated Successfully !!!");
                } else if (selectedField.getType() == String.class) {
                    String newValue = tools.validateString("Enter New Value For " + selectedField.getName() + ": ", errMsg);
                    selectedField.set(c, newValue);
                    System.out.println(selectedField.getName() + " Updated Successfully !!!");
                } else {
                    System.out.println("-> Field Type Not Supported : " + selectedField.getName());
                }
            } catch (IllegalAccessException ex) {
                System.out.println("-> Error While Updating " + selectedField.getName() + ": " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("-> Unexpected error: " + ex.getMessage());
            }
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
            System.out.println("-> Error finding customer by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void sort() {

    }

    public String getCustomerID() {
        String cusID;
        try {
            do {
                cusID = tools.validateID("Customer ID", errMsg, "CUS-\\d{4}");
                if (findByID(cusID) == null) {
                    System.out.println("-> ID Not Found, Try Again!");
                }
            } while (findByID(cusID) == null);
            return cusID;
        } catch (Exception e) {
            System.out.println("-> Error While Getting Customer ID: " + e.getMessage());
            return null;
        }
    }

    public void addCustomer() {
        do {
            try {
                String ID;
                do {
                    ID = tools.validateID("Customer ID", "ID Must Follow CUS-0000", "CUS-\\d{4}");
                } while (findByID(ID) != null);

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


}
