package Service;

import Model.Employee;
import Repository.EmployeeRepository;
import View.AppTools;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository empRepository;
    private final AppTools tools;
    private final String errMsg;
    private final ArrayList<Employee> currentEmp;

    public EmployeeService() {
        empRepository = new EmployeeRepository();
        currentEmp = empRepository.readFile();
        tools = new AppTools();
        errMsg = "-> Invalid Input, Try Again";
    }

    @Override
    public void display() {
        try {
            if (!currentEmp.isEmpty()) {
                System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s %-10s\n",
                        "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Degree", "Position", "Salary");
                System.out.println("---------------------------------------------------------------------------------------------------");

                currentEmp.forEach(employee ->
                        System.out.printf("%-10s %-20s %-10s %-10s %-15s %-10s %-27s %-15s %-12s %-10.1f\n",
                                employee.getID(),
                                employee.getFullName(),
                                employee.getDOB(),
                                employee.isGender() ? "Male" : "Female",
                                employee.getCMND(),
                                employee.getPhoneNumber(),
                                employee.getEmail(),
                                employee.getDegree(),
                                employee.getPosition(),
                                employee.getSalary())
                );
                System.out.println("---------------------------------------------------------------------------------------------------");
            } else {
                System.out.println("-> The list is empty !!");
            }
        } catch (Exception e) {
            System.out.println("-> Error displaying employees: " + e.getMessage());
        }
    }

    @Override
    public void add(Employee entity) {
        try {
            currentEmp.add(entity);
            System.out.println("-> Employee Added Successfully (not saved to file yet) !!!");
        } catch (Exception e) {
            System.out.println("-> Error adding employee: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            empRepository.writeFile(currentEmp);
            System.out.println("-> Employees saved to file successfully !!!");
        } catch (Exception e) {
            System.out.println("-> Error saving employees: " + e.getMessage());
        }
    }

    @Override
    public void update(Employee e) {
        Field[] fields = e.getClass().getSuperclass().getDeclaredFields();
        Field[] subFields = e.getClass().getDeclaredFields();
        int totalField = fields.length + subFields.length;
        boolean isEditing = true;

        while (isEditing) {
            System.out.println("---- CUSTOMIZE EMPLOYEE -----");
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
                updateField(selectedField, e);
            } catch (IllegalAccessException ex) {
                System.out.println("-> Error While Updating " + selectedField.getName() + ": " + ex.getMessage());
            } catch (Exception ex) {
                System.out.println("-> Unexpected error: " + ex.getMessage());
            }
        }
    }

    private void updateField(Field field, Employee e) throws IllegalAccessException {
        String fieldName = field.getName();

        try {
            if (fieldName.equalsIgnoreCase("DOB")) {
                LocalDate DOB = tools.validateDateOfBirth("Enter New Value For Date Of Birth", errMsg);
                field.set(e, DOB);
                System.out.println(fieldName + " Updated Successfully !!!");
            } else if (fieldName.equalsIgnoreCase("Salary")) {
                double salary = tools.validateSalary("Employee Salary", errMsg);
                field.set(e, salary);
                System.out.println(fieldName + " Updated Successfully !!!");
            } else if (fieldName.equalsIgnoreCase("Gender")) {
                boolean isMale = tools.validateGender("Gender (Male (M) / Female (F))", errMsg).equals("Male");
                field.set(e, isMale);
                System.out.println(fieldName + " Updated Successfully !!!");
            } else {
                String newValue = tools.validateString("Enter New Value For " + fieldName + ": ", errMsg);
                field.set(e, newValue);
                System.out.println(fieldName + " Updated Successfully !!!");
            }
        } catch (Exception ex) {
            System.out.println("-> Error while updating field " + fieldName + ": " + ex.getMessage());
        }
    }

    public void addEmployee() {
        do {
            try {
                String ID;
                do {
                    ID = tools.validateID("EmployeeID", "ID Must Follow EMP-0000", "EMP-\\d{4}");
                } while (findByID(ID) != null);

                String name = tools.normalizeName(tools.validateStringInput("Employee Full Name", errMsg));
                LocalDate DOB = tools.validateDateOfBirth("Employee Date Of Birth", errMsg);
                String CMND = tools.validateIDCard("Employee CMND", errMsg);
                boolean isMale = tools.validateGender("Gender (Male (M) / Female (F))", errMsg).equals("Male");
                String phoneNum = tools.validatePhoneNumber("Employee Phone Number", errMsg);
                String email = tools.validateEmail("Employee Email", errMsg);
                String degree = tools.validateString("Employee Degree", errMsg);
                String position = tools.validateStringInput("Employee Position", errMsg);
                double salary = tools.validateSalary("Employee Salary", errMsg);

                add(new Employee(ID, name, DOB, isMale, CMND, phoneNum, email, degree, position, salary));

                if (tools.validateStringInput("-> Do you want to save the employee data to file (Y/N)", errMsg).equalsIgnoreCase("Y")) {
                    save();
                }
            } catch (Exception e) {
                System.out.println("-> Error adding employee: " + e.getMessage());
            }
        } while (tools.validateStringInput("-> Do you want to continue adding employees (Y/N)", errMsg).equalsIgnoreCase("Y"));
    }


    @Override
    public Employee findByID(String ID) {
        for (Employee employee : currentEmp) {
            if (employee.getID().equalsIgnoreCase(ID)) {
                return employee;
            }
        }
        return null;
    }
}
