package Repository;

import Model.Contract;

import java.util.Set;

public interface IContactRepository extends Repository<Contract, Set<Contract>>{

    final String contractPath = "\\Data\\contract.csv";

    public Set<Contract> readFile();

    public void writeFile(Set<Contract> contractSet);
}
