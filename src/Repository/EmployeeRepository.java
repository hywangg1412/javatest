package Repository;

import Model.Employee;
import View.AppTools;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class EmployeeRepository implements IEmployeeRepository {
    public EmployeeRepository() {}

    @Override
    public ArrayList<Employee> readFile() {
        ArrayList<Employee> empList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path + employeePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] toString = line.split(",");
                if (toString.length < 10) {
                    continue;
                }
                LocalDate DOB = AppTools.parseDate(toString[2]);
                boolean gender = toString[3].equals("Male");
                double salary = Double.parseDouble(toString[9]);
                Employee employee = new Employee(
                        toString[0],
                        toString[1],
                        DOB, gender, toString[4],
                        toString[5], toString[6], toString[7], toString[8],
                        salary);

                empList.add(employee);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return empList;
    }

    @Override
    public void writeFile(ArrayList<Employee> entities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + employeePath))) {
            for (Employee employee : entities) {
                String line = employee.getID() + ","
                        + employee.getFullName() + ","
                        + employee.getDOB() + ","
                        + (employee.isGender() ? "Male" : "Female") + ","
                        + employee.getCMND() + ","
                        + employee.getPhoneNumber() + ","
                        + employee.getEmail() + ","
                        + employee.getDegree() + ","
                        + employee.getPosition() + ","
                        + employee.getSalary();

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error Writing to file", e);
        }
    }
}
