package Service;

import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;
import Repository.FacilityRepository;
import View.AppTools;

import java.sql.SQLOutput;
import java.util.*;

public class FacilityService implements IFacilityService {
    private final FacilityRepository faciRepository;
    private final AppTools tools;
    private LinkedHashMap<Facility, Integer> currentFacilities;
    private final String errMsg;

    public FacilityService() {
        faciRepository = new FacilityRepository();
        tools = new AppTools();
        currentFacilities = faciRepository.readFile();
        errMsg = "-> Invalid Input, Try Again";
    }

    @Override
    public void display() {
        try {
            System.out.println("+-----------------+----------------------+-----------------+");
            System.out.printf("| %-15s | %-20s | %-15s |%n", "Facility ID", "Facility Name", "Usage Count");
            System.out.println("+-----------------+----------------------+-----------------+");

            for (Map.Entry<Facility, Integer> entry : currentFacilities.entrySet()) {
                Facility facility = entry.getKey();
                Integer usageCount = entry.getValue();
                System.out.printf("| %-15s | %-20s | %-15d |%n",
                        facility.getFacilityID(),
                        facility.getFacilityName(),
                        facility.getUsageCount());
            }
            System.out.println("+-----------------+----------------------+-----------------+");

        } catch (Exception e) {
            System.out.println("Error displaying facilities: " + e.getMessage());
        }
    }

    public void sort(){

    }

    @Override
    public void add(Facility entity) {
        try {
            currentFacilities.put(entity, 0);
            System.out.println("-> Add Facility Successfully!!");
        } catch (Exception e) {
            System.out.println("Error adding facility: " + e.getMessage());
        }
    }

    public void addVilla() {
        try {
            addFacility("Villa", Villa.class);
            display();
        } catch (Exception e) {
            System.out.println("Error adding villa: " + e.getMessage());
        }
    }

    public void addHouse() {
        try {
            addFacility("House", House.class);
            display();
        } catch (Exception e) {
            System.out.println("Error adding house: " + e.getMessage());
        }
    }

    public void addRoom() {
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
                    if (findByID(ID)!= null) {
                        System.out.println("-> ID Already Exist, Try New One");
                    }
                } while (findByID(ID)!= null);

                String facilityName = tools.validateStringInput(facilityType + " Name", errMsg);
                double area = tools.validateDouble("Area", errMsg, 30);
                double rentalCost = tools.validateDouble("Rental Cost", errMsg, 0);
                int maxPeople = tools.validateInteger("Max People", errMsg, 0);
                String rentalType = tools.validateStringInput("Rental Type", errMsg);

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

    // FIX THIS`
    public String getFacilityID() {
        String faciID;
        try {
            do {
                faciID = tools.validateID("Facility ID", "ID Must Follow SVxx-xxxx", "SV(VL|HO|RO)-\\d{4}");
                Facility facility = findByID(faciID);
                if (facility != null) {
                    incrementUsageCount(facility);

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

    public void incrementUsageCount(Facility facility){
        try {
            if (currentFacilities.containsKey(facility)){
                int currentCount = currentFacilities.get(facility);
                facility.incrementUsageCount();
                currentFacilities.put(facility, currentCount + 1);
                System.out.println("-> Usage Count Of " + facility.getFacilityID() + " Increased To: " + currentCount + 1);
            } else {
                System.out.println("-> Not Found Facility With ID - " + facility.getFacilityID());
            }
        } catch (Exception e){
            System.out.println("-> Error While Increment Usage Count Of " + facility.getFacilityID());
        }
    }


}
