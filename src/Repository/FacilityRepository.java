package Repository;


import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class FacilityRepository implements IFacilityRepository {


    public LinkedHashMap<Facility, Integer> readFile() {
        LinkedHashMap<Facility, Integer> facilityMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path + facilityPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }
                try {
                    Facility facility;
                    if (data.length < 6) {
                        System.out.println("-> Error: Not enough data in line: " + line);
                        continue;
                    }

                    String ID = data[0];
                    String facilityType = data[1];
                    double area = Double.parseDouble(data[2]);
                    double rentalCost = Double.parseDouble(data[3]);
                    int maxPeople = Integer.parseInt(data[4]);
                    String rentalType = data[5];
                    int usageCount = data.length > 6 ? Integer.parseInt(data[data.length - 1]) : 0; // Giá trị mặc định 0

                    if (facilityType.equalsIgnoreCase("Villa")) {
                        if (data.length < 9) {
                            System.out.println("-> Error: Not enough data for Villa in line: " + line);
                            continue;
                        }
                        String roomStandard = data[6];
                        double poolArea = Double.parseDouble(data[7]);
                        int numberOfFloors = Integer.parseInt(data[8]);
                        facility = new Villa(ID, facilityType, area, rentalCost, maxPeople, rentalType, roomStandard, poolArea, numberOfFloors);
                    } else if (facilityType.equalsIgnoreCase("House")) {
                        if (data.length < 8) {
                            System.out.println("-> Error: Not enough data for House in line: " + line);
                            continue;
                        }
                        String roomStandard = data[6];
                        int numberOfFloors = Integer.parseInt(data[7]);
                        facility = new House(ID, facilityType, area, rentalCost, maxPeople, rentalType, roomStandard, numberOfFloors);
                    } else if (facilityType.equalsIgnoreCase("Room")) {
                        if (data.length < 7) {
                            System.out.println("-> Error: Not enough data for Room in line: " + line);
                            continue;
                        }
                        String freeService = data[6];
                        facility = new Room(ID, facilityType, area, rentalCost, maxPeople, rentalType, freeService);
                    } else {
                        continue;
                    }
                    facilityMap.put(facility, usageCount);

                } catch (Exception e) {
                    System.out.println("-> Error while parsing line: " + line + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("-> Error while reading file: " + e.getMessage());
        }
        return facilityMap;
    }

    // Ghi dữ liệu vào file
    public void writeFile(LinkedHashMap<Facility, Integer> facilityList) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path + facilityPath))) {
            for (Map.Entry<Facility, Integer> entry : facilityList.entrySet()) {
                Facility facility = entry.getKey();
                int usageCount = entry.getValue();

                String line = facility.getFacilityID() + ","
                        + facility.getFacilityName() + ","
                        + facility.getArea() + ","
                        + facility.getRentalCost() + ","
                        + facility.getMaxPeople() + ","
                        + facility.getRentalType();

                if (facility instanceof Villa) {
                    Villa villa = (Villa) facility;
                    line += "," + villa.getRoomStandard()
                            + "," + villa.getPoolArea()
                            + "," + villa.getNumberOfFloor();
                } else if (facility instanceof House) {
                    House house = (House) facility;
                    line += "," + house.getRoomStandard()
                            + "," + house.getNumberOfFloor();
                } else if (facility instanceof Room) {
                    Room room = (Room) facility;
                    line += "," + room.getFreeService();
                }
                line += "," + usageCount;

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("-> Error while writing file: " + e.getMessage());
        }
    }


}
