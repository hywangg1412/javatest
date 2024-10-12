package Service;

import Model.Employee;
import Repository.EmployeeRepository;
import View.AppTools;
import View.Menu;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeService implements IEmployeeService {
    private final EmployeeRepository empRepository;
    private final AppTools tools;
    private final String errMsg;
    private final String updatedMsg;
    private final ArrayList<Employee> currentEmp;

    public EmployeeService() {
        empRepository = new EmployeeRepository();
        currentEmp = empRepository.readFile();
        tools = new AppTools();
        errMsg = "-> Invalid Input, Try Again";
        updatedMsg = "Updated Successfully !!!";
    }

    @Override
    public void display() {
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
    }

    @Override
    public void add(Employee entity) {
        currentEmp.add(entity);
        System.out.println("-> Employee Added Successfully (not saved to file yet) !!!");
    }

    @Override
    public void save() {
        empRepository.writeFile(currentEmp);
        System.out.println("-> Employees saved to file successfully !!!");
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
                System.out.println("-> Error While Updating " + selectedField.getName());
            }
        }
    }

    private void updateField(Field field, Employee e) throws IllegalAccessException {
        String fieldName = field.getName();

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
    }

    public void addEmployee() {
        do {
            String ID;
            do {
                ID = tools.validateID("EmployeeID", "ID Must Follow EMP-0000", "EMP-\\d{4}");
            } while (isDuplicateID(ID));

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
        } while (tools.validateStringInput("-> Do you want to continue adding employees (Y/N)", errMsg).equalsIgnoreCase("Y"));
    }


    private boolean isDuplicateID(String ID) {
        return currentEmp.stream().anyMatch(emp -> emp.getID().equalsIgnoreCase(ID));
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

//    private void updateFullName(Employee employee) {
//        String newName = tools.validateStringInput("Enter New Full Name: ", errMsg);
//        employee.setFullName(tools.normalizeName(newName));
//        System.out.println("Full Name " + updatedMsg);
//    }
//
//    private void updateDateOfBirth(Employee employee) {
//        LocalDate newDOB = tools.validateDateOfBirth("Enter New Date of Birth (yyyy-mm-dd): ", errMsg);
//        employee.setDOB(newDOB);
//        System.out.println("Date of Birth " + updatedMsg);
//    }
//
//    private void updateCMND(Employee employee) {
//        String newCMND = tools.validateStringInput("Enter New CMND: ", errMsg);
//        employee.setCMND(newCMND);
//        System.out.println("CMND " + updatedMsg);
//    }
//
//    private void updateGender(Employee employee) {
//        String newGender = tools.validateStringInput("Enter New Gender (Male/Female): ", errMsg);
//        employee.setGender(newGender.equalsIgnoreCase("Male"));
//        System.out.println("Gender " + updatedMsg);
//    }
//
//    private void updatePhoneNumber(Employee employee) {
//        String newPhoneNumber = tools.validateStringInput("Enter New Phone Number: ", errMsg);
//        employee.setPhoneNumber(newPhoneNumber);
//        System.out.println("Phone Number " + updatedMsg);
//    }
//
//    private void updateEmail(Employee employee) {
//        String newEmail = tools.validateStringInput("Enter New Email: ", errMsg);
//        employee.setEmail(newEmail);
//        System.out.println("Email " + updatedMsg);
//    }
//
//    private void updateDegree(Employee employee) {
//        String newDegree = tools.validateStringInput("Enter New Degree: ", errMsg);
//        employee.setDegree(newDegree);
//        System.out.println("Degree " + updatedMsg);
//    }
//
//    private void updatePosition(Employee employee) {
//        String newPosition = tools.validateStringInput("Enter New Position: ", errMsg);
//        employee.setPosition(newPosition);
//        System.out.println("Position " + updatedMsg);
//    }
//
//    private void updateSalary(Employee employee) {
//        double newSalary = tools.validateSalary("Enter New Salary: ", errMsg);
//        employee.setSalary(newSalary);
//        System.out.println("Salary " + updatedMsg);
//    }
//
//    public void updateEmployee(Employee updatedEmployee) {
////        for (int i = 0; i < currentEmp.size(); i++) {
////            Employee emp = currentEmp.get(i);
////            if (emp.getID().equals(updatedEmployee.getID())) {
////                currentEmp.set(i, updatedEmployee);
////                System.out.println("-> Employee With ID " + updatedEmployee.getID() + " Updated successfully !!!");
////                return;
////            }
////        }
////        System.out.println("-> Employee With ID " + updatedEmployee.getID() + " Is Not Found !!!");
////    }
//
//    }

//    @Override
//    public void update() {
//        String editID = tools.validateID("Enter Employee ID Want To Edit", "ID Must Follow EMP-0000", "EMP-\\d{4}");
//        Employee foundEmp = findByID(editID);
//
//        if (foundEmp == null) {
//            System.out.println("-> Not Found Employee With ID " + editID);
//        } else {
//            String[] editOptions = {
//                    "Full Name", "Date of Birth", "CMND", "Gender",
//                    "Phone Number", "Email", "Degree", "Position", "Salary", "Finish Editing"
//            };
//
//            Menu<String> editMenu = new Menu<>(editOptions, "---- CUSTOMIZE EMPLOYEE INFORMATION ----") {
//                @Override
//                public void execute(int n) {
//                    switch (n) {
//                        case 1 -> updateFullName(foundEmp);
//                        case 2 -> updateDateOfBirth(foundEmp);
//                        case 3 -> updateCMND(foundEmp);
//                        case 4 -> updateGender(foundEmp);
//                        case 5 -> updatePhoneNumber(foundEmp);
//                        case 6 -> updateEmail(foundEmp);
//                        case 7 -> updateDegree(foundEmp);
//                        case 8 -> updatePosition(foundEmp);
//                        case 9 -> updateSalary(foundEmp);
//                        case 10 -> System.out.println("Finished editing.");
//                        default -> System.out.println(errMsg);
//                    }
//                }
//            };
//
//            do {
//                editMenu.run();
//            } while (tools.validateStringInput("-> Do you want to continue editing employees (Y/N)", errMsg).equalsIgnoreCase("Y"));
//
//            if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
//                updateEmployee(foundEmp);
//                save();
//            }
//        }
//    }
