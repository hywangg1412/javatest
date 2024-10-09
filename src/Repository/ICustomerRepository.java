package Repository;

import Model.Customer;
import Model.Employee;
import Service.Service;

import java.util.ArrayList;

public interface ICustomerRepository extends Repository<Customer , ArrayList<Customer>> {

    final String customerPath = "\\Data\\customer.csv";

    public ArrayList<Customer> readFile();

    public void writeFile(ArrayList<Customer> customers);

}
