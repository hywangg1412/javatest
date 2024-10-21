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
    ArrayList<Employee> currentEmp;

    public EmployeeService() {
        empRepository = new EmployeeRepository();
        currentEmp = empRepository.readFile();
        tools = new AppTools();
        errMsg = "-> Invalid Input, Try Again";
    }

    public ArrayList<Employee> getCurrentEmp() {
        return currentEmp;
    }

    public void setCurrentEmp(ArrayList<Employee> currentEmp) {
        this.currentEmp = currentEmp;
    }

    @Override
    public void display() {
        try {
            if (!getCurrentEmp().isEmpty()) {
                System.out.println("+----------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+----------+");
                System.out.printf("| %-8s | %-20s | %-10s | %-10s | %-15s | %-12s | %-27s | %-15s | %-12s | %-8s |\n",
                        "ID", "Full Name", "DOB", "Gender", "CMND", "Phone", "Email", "Degree", "Position", "Salary");
                System.out.println("+----------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+----------+");
                for (Employee employee : getCurrentEmp()) {
                    System.out.printf("| %-8s | %-20s | %-10s | %-10s | %-15s | %-12s | %-27s | %-15s | %-12s | %-8.1f |\n",
                            employee.getID(),
                            employee.getFullName(),
                            employee.getDOB(),
                            employee.isGender() ? "Male" : "Female",
                            employee.getCMND(),
                            employee.getPhoneNumber(),
                            employee.getEmail(),
                            employee.getDegree(),
                            employee.getPosition(),
                            employee.getSalary());
                }
                System.out.println("+----------+----------------------+------------+------------+-----------------+------------+-----------------------------+-----------------+--------------+----------+");

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
            getCurrentEmp().add(entity);
            System.out.println("-> Employee Added Successfully (not saved to file yet) !!!");
        } catch (Exception e) {
            System.out.println("-> Error adding employee: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            empRepository.writeFile(getCurrentEmp());
            System.out.println("-> Employees saved to file successfully !!!");
        } catch (Exception e) {
            System.out.println("-> Error saving employees: " + e.getMessage());
        }
    }

    @Override
    public void update(Employee e) throws IllegalAccessException {

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

            int choice = AppTools.validateInteger("Choose Your Option", errMsg, 0);

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
                switch (choice) {
                    case 1 -> {
                        String ID;
                        do {
                            ID = AppTools.validateID("EmployeeID", "ID Must Follow EMP-0000", "EMP-\\d{4}");
                            if (findByID(ID) != null){
                                System.out.println("-> Duplicated ID, Try Again.");
                            }
                        } while (findByID(ID) != null);
                        selectedField.set(e, ID);
                    }
                    case 2 -> {
                        String name = AppTools.normalizeName(AppTools.validateStringInput("Employee Full Name", errMsg));
                        selectedField.set(e, name);
                    }
                    case 3 -> {
                        LocalDate DOB = AppTools.validateDateOfBirth("Employee Date Of Birth", errMsg);
                        selectedField.set(e, DOB);
                    }
                    case 4 -> {

                        String CMND = AppTools.validateIDCard("Employee CMND", errMsg);
                        selectedField.set(e, CMND);
                    }
                    case 5 -> {
                        boolean isMale = AppTools.validateGender("Gender (Male (M) / Female (F))", errMsg).equals("Male");
                        selectedField.set(e, isMale);
                    }
                    case 6 -> {
                        String phoneNum = AppTools.validatePhoneNumber("Employee Phone Number", errMsg);
                        selectedField.set(e, phoneNum);
                    }
                    case 7 -> {
                        String email = AppTools.validateEmail("Employee Email", errMsg);
                        selectedField.set(e, email);
                    }
                    case 8 -> {
                        String degree = AppTools.validateString("Employee Degree", errMsg);
                        selectedField.set(e, degree);
                    }
                    case 9 -> {
                        String position = AppTools.validateStringInput("Employee Position", errMsg);
                        selectedField.set(e, position);
                    }
                    case 10 -> {
                        double salary = AppTools.validateDouble("Employee Salary", errMsg,0);
                        selectedField.set(e, salary);
                    }
                    default -> System.out.println(errMsg);
                }
                save();
            } catch (Exception Ex) {
                System.out.println("-> Error While Updating Employee.");
            }
        }
    }


    public void addEmployee() {
        do {
            try {
                String ID;
                do {
                    ID = AppTools.validateID("EmployeeID", "ID Must Follow EMP-0000", "EMP-\\d{4}");
                    if (findByID(ID) != null) {
                        System.out.println("-> Duplicate ID , Try Again");
                    }
                } while (findByID(ID) != null);

                String name = AppTools.normalizeName(AppTools.validateStringInput("Employee Full Name", errMsg));
                LocalDate DOB = AppTools.validateDateOfBirth("Employee Date Of Birth", errMsg);
                String CMND = AppTools.validateIDCard("Employee CMND", errMsg);
                boolean isMale = AppTools.validateGender("Gender (Male (M) / Female (F))", errMsg).equals("Male");
                String phoneNum = AppTools.validatePhoneNumber("Employee Phone Number", errMsg);
                String email = AppTools.validateEmail("Employee Email", errMsg);
                String degree = AppTools.validateString("Employee Degree", errMsg);
                String position = AppTools.validateStringInput("Employee Position", errMsg);
                double salary = AppTools.validateDouble("Employee Salary", errMsg,0);

                add(new Employee(ID, name, DOB, isMale, CMND, phoneNum, email, degree, position, salary));

                if (AppTools.validateStringInput("-> Do you want to save the employee data to file (Y/N)", errMsg).equalsIgnoreCase("Y")) {
                    save();
                }
            } catch (Exception e) {
                System.out.println("-> Error adding employee: " + e.getMessage());
            }
        } while (AppTools.validateStringInput("-> Do you want to continue adding employees (Y/N)", errMsg).equalsIgnoreCase("Y"));
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
