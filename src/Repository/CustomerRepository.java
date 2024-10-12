package Repository;

import Model.Customer;
import View.AppTools;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerRepository implements ICustomerRepository {

    public CustomerRepository(){};

    @Override
    public ArrayList<Customer> readFile() {
        ArrayList<Customer> customerList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path + customerPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 9) {
                    continue;
                }
                try {
                    LocalDate DOB = AppTools.parseDate(data[2]);
                    boolean gender = data[3].equalsIgnoreCase("Male");
                    Customer customer = new Customer(
                            data[0],
                            data[1],
                            DOB,
                            gender,
                            data[4],
                            data[5],
                            data[6],
                            data[7],
                            data[8]
                    );
                    customerList.add(customer);
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error reading file: " + e.getMessage(), e);
        }
        return customerList;
    }

    @Override
    public void writeFile(ArrayList<Customer> entities) {
        StringBuilder data = new StringBuilder();
        for (Customer customer : entities) {
            data.append(customer.getID()).append(",")
                    .append(customer.getFullName()).append(",")
                    .append(customer.getDOB()).append(",")
                    .append(customer.isGender() ? "Male" : "Female").append(",")
                    .append(customer.getCMND()).append(",")
                    .append(customer.getPhoneNumber()).append(",")
                    .append(customer.getEmail()).append(",")
                    .append(customer.getAddress()).append(",")
                    .append(customer.getCustomerType()).append("\n");
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + customerPath))) {
            bw.write(data.toString());
        } catch (IOException e) {
            throw new RuntimeException("-> Error writing to file: " + e.getMessage(), e);
        }
    }
}