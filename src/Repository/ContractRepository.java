package Repository;

import Model.Contract;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ContractRepository implements IContactRepository {

    @Override
    public Set<Contract> readFile() {
        Set<Contract> contractList = new HashSet<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path + contractPath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] data = line.split(",");
                if (data.length < 4){
                    System.out.println("-> Skipping line " + line);
                    continue;
                }
                Contract contract = new Contract(
                        Integer.parseInt(data[0]),
                        data[1],
                        Double.parseDouble(data[2]),
                        Double.parseDouble(data[3]));

                contractList.add(contract);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contractList;
    }

    @Override
    public void writeFile(Set<Contract> contractList) {
        try (BufferedWriter bufferedWriter  = new BufferedWriter(new FileWriter(path + contractPath))){
            for (Contract contract : contractList){
                String line = contract.getContractNum() + "," +
                        contract.getBookingID() + "," +
                        contract.getDepositAmmount() + "," +
                        contract.getTotalPayment();
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
            System.out.println("-> Contract Save Successfully!");
        } catch (IOException e) {
            System.out.println("-> ERROR While Saving Contract " + e.getMessage());;
        }
    }
}
