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
        try (BufferedReader br = new BufferedReader(new FileReader(path + facilityPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }
                try {
                    Facility facility;
                    String ID = data[0];
                    String facilityType = data[1];
                    double area = Double.parseDouble(data[2]);
                    double rentalCost = Double.parseDouble(data[3]);
                    int maxPeople = Integer.parseInt(data[4]);
                    String rentalType = data[5];

                    int usageCount = Integer.parseInt(data[data.length - 1]);

                    if (facilityType.equalsIgnoreCase("Villa")) {
                        String roomStandard = data[6];
                        double poolArea = Double.parseDouble(data[7]);
                        int numberOfFloors = Integer.parseInt(data[8]);
                        facility = new Villa(ID, facilityType, area, rentalCost, maxPeople, rentalType, roomStandard, poolArea, numberOfFloors);
                    } else if (facilityType.equalsIgnoreCase("House")) {
                        String roomStandard = data[6];
                        int numberOfFloors = Integer.parseInt(data[7]);
                        facility = new House(ID, facilityType, area, rentalCost, maxPeople, rentalType, roomStandard, numberOfFloors);
                    } else if (facilityType.equalsIgnoreCase("Room")) {
                        String freeService = data[6];
                        facility = new Room(ID, facilityType, area, rentalCost, maxPeople, rentalType, freeService);
                    } else {
                        continue;
                    }
                    facilityList.put(facility, usageCount);

                } catch (Exception e) {
                    System.out.println("-> Error while parsing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("-> Error while reading file: " + e.getMessage());
        }
        return facilityList;
    }

    public void writeFile(LinkedHashMap<Facility, Integer> facilityList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + facilityPath))) {
            for (Map.Entry<Facility, Integer> entry : facilityList.entrySet()) {
                Facility facility = entry.getKey();
                int usageCount = entry.getValue();

                String line = String.join(",",
                        facility.getFacilityID(),
                        facility.getFacilityName(),
                        String.valueOf(facility.getArea()),
                        String.valueOf(facility.getRentalCost()),
                        String.valueOf(facility.getMaxPeople()),
                        facility.getRentalType());

                if (facility instanceof Villa) {
                    Villa villa = (Villa) facility;
                    line += String.join(",",
                            villa.getRoomStandard(),
                            String.valueOf(villa.getPoolArea()),
                            String.valueOf(villa.getNumberOfFloor()),
                            String.valueOf(usageCount));
                } else if (facility instanceof House) {
                    House house = (House) facility;
                    line += String.join(",",
                            house.getRoomStandard(),
                            String.valueOf(house.getNumberOfFloor()),
                            String.valueOf(usageCount));
                } else if (facility instanceof Room) {
                    Room room = (Room) facility;
                    line += String.join(",",
                            room.getFreeService(),
                            String.valueOf(usageCount));
                }
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error while writing file: " + e.getMessage());
        }
    }

}
