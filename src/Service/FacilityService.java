package Service;

import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;
import Repository.FacilityRepository;
import View.AppTools;

import java.util.*;

public class FacilityService implements IFacilityService {
    private final FacilityRepository faciRepository;
    private final AppTools tools;
    LinkedHashMap<Facility, Integer> currentFacilities;
    private final String errMsg;

    public FacilityService() {
        faciRepository = new FacilityRepository();
        tools = new AppTools();
        currentFacilities = faciRepository.readFile();
        errMsg = "-> Invalid Input, Try Again";
    }

    public LinkedHashMap<Facility, Integer> getCurrentFacilities() {
        return currentFacilities;
    }

    public void setCurrentFacilities(LinkedHashMap<Facility, Integer> currentFacilities) {
        this.currentFacilities = currentFacilities;
    }

    @Override
    public void display() {
        try {
            displayVillas();
            displayHouses();
            displayRooms();
        } catch (Exception e) {
            System.out.println("-> Error displaying facilities: " + e.getMessage());
        }
    }

    public void displayMaintenance() {
        System.out.println("+-----------------+----------------------+-----------------+----------------------+");
        System.out.printf("| %-15s | %-20s | %-15s | %-20s |%n", "Facility ID", "Facility Name", "Usage Count", "Status");
        System.out.println("+-----------------+----------------------+-----------------+----------------------+");

        for (Map.Entry<Facility, Integer> entry : getCurrentFacilities().entrySet()) {
            Facility facility = entry.getKey();
            Integer usageCount = entry.getValue();
            String usageStatus = (usageCount > 5) ? "Need Maintenance" : "Good Condition";

            System.out.printf("| %-15s | %-20s | %-15d | %-20s |%n",
                    facility.getFacilityID(),
                    facility.getFacilityName(),
                    usageCount,
                    usageStatus);
        }
        System.out.println("+-----------------+----------------------+-----------------+----------------------+");
    }

    private void displayVillas() {
        try {
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+--------------------+-------------------+-----------------+");
            System.out.printf("| %-15s | %-20s | %-15s | %-15s | %-12s | %-15s | %-15s | %-15s | %-15s |%n",
                    "Villa ID", "Name", "Area", "Price", "Max People", "Rental Type", "Standard", "Number of Floors", "Pool Area");
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+--------------------+-------------------+-----------------+");

            for (Map.Entry<Facility, Integer> entry : currentFacilities.entrySet()) {
                Facility facility = entry.getKey();
                if (facility instanceof Villa) {
                    Villa villa = (Villa) facility;
                    System.out.printf("| %-15s | %-20s | %-15.2f | %-15.2f | %-12d | %-15s | %-15s | %-15d | %-15.2f |%n",
                            villa.getFacilityID(), villa.getFacilityName(), villa.getArea(), villa.getRentalCost(),
                            villa.getMaxPeople(), villa.getRentalType(), villa.getRoomStandard(),
                            villa.getNumberOfFloor(), villa.getPoolArea());
                }
            }

            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+--------------------+-------------------+-----------------+");
        } catch (Exception e) {
            System.out.println("Error displaying Villas: " + e.getMessage());
        }
    }

    private void displayHouses() {
        try {
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+--------------------+");
            System.out.printf("| %-15s | %-20s | %-15s | %-15s | %-12s | %-15s | %-15s | %-15s |%n",
                    "House ID", "Name", "Area", "Price", "Max People", "Rental Type", "Standard", "Number of Floors");
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+--------------------+");

            for (Map.Entry<Facility, Integer> entry : currentFacilities.entrySet()) {
                Facility facility = entry.getKey();
                if (facility instanceof House) {
                    House house = (House) facility;
                    System.out.printf("| %-15s | %-20s | %-15.2f | %-15.2f | %-12d | %-15s | %-15s | %-15d |%n",
                            house.getFacilityID(), house.getFacilityName(), house.getArea(), house.getRentalCost(),
                            house.getMaxPeople(), house.getRentalType(), house.getRoomStandard(), house.getNumberOfFloor());
                }
            }

            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+--------------------+");
        } catch (Exception e) {
            System.out.println("-> Error displaying Houses: " + e.getMessage());
        }
    }

    private void displayRooms() {
        try {
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+");
            System.out.printf("| %-15s | %-20s | %-15s | %-15s | %-12s | %-15s | %-20s |%n",
                    "Room ID", "Name", "Area", "Price", "Max People", "Rental Type", "Free Service");
            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+");

            for (Map.Entry<Facility, Integer> entry : currentFacilities.entrySet()) {
                Facility facility = entry.getKey();
                if (facility instanceof Room) {
                    Room room = (Room) facility;
                    System.out.printf("| %-15s | %-20s | %-15.2f | %-15.2f | %-12d | %-15s | %-20s |%n",
                            room.getFacilityID(), room.getFacilityName(), room.getArea(), room.getRentalCost(),
                            room.getMaxPeople(), room.getRentalType(), room.getFreeService());
                }
            }

            System.out.println("+-----------------+----------------------+-----------------+-----------------+--------------+------------------+-------------------+");
        } catch (Exception e) {
            System.out.println("-> Error displaying Rooms: " + e.getMessage());
        }
    }

    @Override
    public void add(Facility entity) {
        try {
            currentFacilities.put(entity, 0);
            System.out.println("-> Add Facility Successfully!!");
        } catch (Exception e) {
            System.out.println("-> `Error adding facility: " + e.getMessage());
        }
    }

    public void addVilla() {
        displayVillas();
        try {
            addFacility("Villa", Villa.class);
        } catch (Exception e) {
            System.out.println("Error adding villa: " + e.getMessage());
        }
    }

    public void addHouse() {
        displayHouses();
        try {
            addFacility("House", House.class);
            display();
        } catch (Exception e) {
            System.out.println("Error adding house: " + e.getMessage());
        }
    }

    public void addRoom() {
        displayRooms();
        try {
            addFacility("Room", Room.class);
            display();
        } catch (Exception e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }

    // Class<? extends Facility> -> chi lop Facility hoac cac lop con cua facility
    public void addFacility(String facilityType, Class<? extends Facility> facilityClass) {
        try {
            String ID;

            do {
                do {
                    ID = tools.validateID(facilityType + " ID", "ID Must Follow SVxx-xxxx", "SV(VL|HO|RO)-\\d{4}");
                    if (findByID(ID) != null) {
                        System.out.println("-> ID Already Exist, Try New One");
                    }
                } while (findByID(ID) != null);

                String facilityName = tools.validateStringInput(facilityType + " Name", errMsg);

                double area = tools.validateDouble("Area", errMsg, 30);

                double rentalCost = tools.validateDouble("Rental Cost", errMsg, 0);
                int maxPeople;
                do {
                    maxPeople = tools.validateInteger("Max People", errMsg, 0);
                } while (maxPeople > 20);

                String rentalType;

                do {
                    rentalType = rentalType = tools.validateStringInput("Rental Type", errMsg);
                } while (!rentalType.equalsIgnoreCase("day")
                        && (!rentalType.equalsIgnoreCase("week")
                        && !rentalType.equalsIgnoreCase("month")));

                Facility newFacility = null;

                if (facilityClass == Villa.class) {
                    String roomStandard = tools.validateStringInput("Room Standard", errMsg);
                    double poolArea = tools.validateDouble("Pool Area", errMsg, 30);
                    int numberOfFloor = tools.validateInteger("Number Of Floor", errMsg, 0);
                    newFacility = new Villa(ID, facilityName, area, rentalCost, maxPeople, rentalType, roomStandard, poolArea, numberOfFloor);
                } else if (facilityClass == House.class) {
                    String roomStandard = tools.validateStringInput("Room Standard", errMsg);
                    int numberOfFloor = tools.validateInteger("Number Of Floor", errMsg, 0);
                    newFacility = new House(ID, facilityName, area, rentalCost, maxPeople, rentalType, roomStandard, numberOfFloor);
                } else if (facilityClass == Room.class) {
                    String freeService = tools.validateStringInput("Free Service", errMsg);
                    newFacility = new Room(ID, facilityName, area, rentalCost, maxPeople, rentalType, freeService);
                }

                if (newFacility != null) {
                    add(newFacility);
                }

                int usageCount = currentFacilities.get(newFacility);

                if (usageCount < 5 && usageCount >= 0){
                    incrementUsage(newFacility);
                } else {
                    resetUsage(newFacility);
                }

            } while (tools.validateStringInput("-> Do You Want To Continue (Y/N)", "Invalid Input, Try Again").equalsIgnoreCase("Y"));

            if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
                save();
            }
        } catch (Exception e) {
            System.out.println("Error adding facility: " + e.getMessage());
        }
    }

    @Override
    public void save() {
        try {
            faciRepository.writeFile(currentFacilities);
            System.out.println("-> Facilities saved to file successfully !!!");
        } catch (Exception e) {
            System.out.println("Error saving facilities to file: " + e.getMessage());
        }
    }

    @Override
    public void update(Facility f) {
    }

    @Override
    public Facility findByID(String ID) {
        try {
            for (Facility facility : currentFacilities.keySet()) {
                if (facility.getFacilityID().equalsIgnoreCase(ID)) {
                    return facility;
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding facility by ID: " + e.getMessage());
        }
        System.out.println("-> ID Not Found");
        return null;
    }

    public String getFacilityID() {
        String faciID;
        try {
            do {
                faciID = tools.validateID("Facility ID", "ID Must Follow SVxx-xxxx", "SV(VL|HO|RO)-\\d{4}");
                Facility facility = findByID(faciID);
                if (facility != null) {
                    return faciID;
                } else {
                    System.out.println("-> ID Not Found, Try Again!");
                }
            } while (true);
        } catch (Exception e) {
            System.out.println("-> Error While Getting Facility ID: " + e.getMessage());
            return null;
        }
    }

    public void incrementUsage(Facility facility){
        try {
            if (currentFacilities.containsKey(facility)) {
                currentFacilities.put(facility, currentFacilities.get(facility) + 1);
            }
        }catch (Exception e){
            throw new RuntimeException("-> Error While Increase Usage Count - " + e.getMessage());
        }
    }

    public void resetUsage(Facility facility){
        try {
            if (currentFacilities.containsKey(facility)){
                currentFacilities.put(facility, 0);
            }
        } catch (Exception e){
            throw new RuntimeException("-> Error While Reset Usage Count - " + e.getMessage());
        }
    }

}
