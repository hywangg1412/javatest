package Repository;

import Model.Employee;

import java.util.ArrayList;

public interface IEmployeeRepository extends Repository<Employee, ArrayList<Employee>> {

    final String employeePath = "\\Data\\employee.csv";

    public ArrayList<Employee> readFile();

    public void writeFile(ArrayList<Employee> employees);

}
