package Repository;


import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path+facilityPath))){
            for (Map.Entry<Facility,Integer> entry : Facilities.entrySet()){
                Facility facility = entry.getKey();
                Integer usageCount = entry.getValue();
                String line = facility.getFacilityID() + " " + facility.getFacilityName()
                        + facility.getArea() + " " + facility.getRentalCost() + " " + facility.getMaxPeople()
                        + " " + facility.getRentalType() + " ";

                if (facility instanceof Villa){
                    Villa villa = (Villa) facility;
                    line += villa.getRoomStandard() + " " + villa.getPoolArea() + " " + villa.getNumberOfFloor()
                            + " " + usageCount ;
                } else if (facility instanceof House) {
                    House house = (House) facility;
                    line += house.getRoomStandard() + " " + house.getNumberOfFloor() + " " + usageCount;
                } else if (facility instanceof  Room) {
                    Room room = (Room) facility;
                    line += room.getFreeService() + " " + usageCount;
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error While Writing To File "+ e.getMessage());
        }
    }
}
