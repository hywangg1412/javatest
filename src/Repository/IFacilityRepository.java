package Repository;

import Model.Facility;

import java.util.LinkedHashMap;

public interface IFacilityRepository extends Repository<Facility, LinkedHashMap<Facility, Integer>>{

    final String facilityPath = "\\Data\\facility.csv";

    public LinkedHashMap<Facility, Integer> readFile();

    public void writeFile(LinkedHashMap<Facility, Integer> Facilities);
}
