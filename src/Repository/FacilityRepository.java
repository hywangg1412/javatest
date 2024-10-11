package Repository;


import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;

public class FacilityRepository implements IFacilityRepository {

    @Override
    public LinkedHashMap<Facility, Integer> readFile() {
        LinkedHashMap<Facility, Integer> facilityList = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader (path + facilityPath))){
            String line;
            while ((line = br.readLine()) != null){
                String[] data = line.split(",");
                if (data.length < 9){
                    continue;
                }
                try {
                    Facility facility;
                    String ID = data[0];
                    String facilityType = data[1];
                    double Area = Double.parseDouble(data[2]);
                    double rentalCost = Double.parseDouble(data[3]);
                    int maxPeole = Integer.parseInt(data[4]);
                    String rentalType = data[5];

                    if (facilityType.equalsIgnoreCase("Villa")){
                        String roomStandard = data[6];
                        double poolArea = Double.parseDouble(data[7]);
                        int numberOfFloor = Integer.parseInt(data[8]);
                        facility = new Villa(ID,facilityType,Area,rentalCost,maxPeole,rentalType,roomStandard,poolArea,numberOfFloor);
                        facilityList.put(facility,0);
                    } else if (facilityType.equalsIgnoreCase("House")){
                        String roomStandard = data[6];
                        int numberOfFloor = Integer.parseInt(data[7]);
                        facility = new House(ID,facilityType,Area,rentalCost,maxPeole,rentalType,roomStandard,numberOfFloor);
                        facilityList.put(facility,0);
                    } else if (facilityType.equalsIgnoreCase("Room")){
                        String freeService = data[6];
                        facility = new Room(ID, facilityType, Area, rentalCost, maxPeole, rentalType, freeService);
                        facilityList.put(facility,0);
                    } else {
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("-> Error While Parsing Line " + line + " - " + e.getMessage());
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return facilityList;
    }

    @Override
    public void writeFile(LinkedHashMap<Facility, Integer> Facilities) {

    }
}
