package Repository;

import Model.Customer;
import View.AppTools;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

public class PromotionRepository implements IPromotionRepository {

    @Override
    public TreeSet<Customer> readFile() {
        TreeSet<Customer> customerList = new TreeSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path + promotionPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 10) {
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
    public void writeFile(TreeSet<Customer> entities) {
//        StringBuilder data = new StringBuilder();
//        for (Customer customer : entities) {
//            data.append(customer.getID()).append(",")
//                    .append(customer.getFullName()).append(",")
//                    .append(customer.getDOB()).append(",")
//                    .append(customer.isGender() ? "Male" : "Female").append(",")
//                    .append(customer.geCMND()).append(",")
//                    .append(customer.getPhoneNumber()).append(",")
//                    .append(customer.getEmail()).append(",")
//                    .append(customer.getAddress()).append(",")
//                    .append(customer.getCustomerType()).append(",").append("\n");
//        }
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + promotionPath))) {
//            bw.write(data.toString());
//        } catch (IOException e) {
//            throw new RuntimeException("-> Error writing to file: " + e.getMessage(), e);
//        }
    }

    public void readFile(Stack<Customer> customers, List<Double> voucherList) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path + promotionPath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 4) {
                    System.out.println("-> Error In Line - " + line);
                    continue;
                }
                String customerID = data[0];
                String fullName = data[1];
                String phoneNumber = data[2];
                double voucher = Double.parseDouble(data[3]);
                Customer customer = new Customer(customerID, fullName, null, true, "", phoneNumber, null, null, null);
                customers.push(customer);

                voucherList.add(voucher);
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error While Reading Data - " + e.getMessage());
        }
    }

    @Override
    public void writeFile(Stack<Customer> customersList, List<Double> voucherList) {
        if (customersList.size() != voucherList.size()) {
            throw new RuntimeException("-> Customer List And Voucher List Must Match");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + promotionPath))) {
            for (int i = 0; i < customersList.size(); i++) {
                Customer customer = customersList.get(i);
                bufferedWriter.write(customer.getID() + "," +
                        customer.getFullName() + "," +
                        customer.getPhoneNumber() + "," +
                        voucherList.get(i) + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error While Writing To File - " + e.getMessage());
        }
    }
}
