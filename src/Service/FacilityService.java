package Service;

import Model.Facility;
import Model.House;
import Model.Room;
import Model.Villa;
import Repository.FacilityRepository;
import View.AppTools;

import java.util.LinkedHashMap;

public class FacilityService implements IFacilityService {
    private final FacilityRepository faciRepository;
    private final AppTools tools;
    private final LinkedHashMap<Facility, Integer> facilityList;
    private final String errMsg;

    public FacilityService() {
        faciRepository = new FacilityRepository();
        tools = new AppTools();
        facilityList = faciRepository.readFile();
        errMsg = "-> Invalid Input, Try Again";
    }

    @Override
    public void display() {
        System.out.printf("%-15s %-20s %-15s%n", "Facility ID", "Facility Name", "Usage Count");
        System.out.println("--------------------------------------------------------------");

        for (Facility facility : facilityList.keySet()) {
            Integer usageCount = facilityList.get(facility);
            System.out.printf("%-15s %-20s %-15d%n",
                    facility.getFacilityID(),
                    facility.getFacilityName(),
                    usageCount);
        }
    }


    @Override
    public void add(Facility entity) {
        facilityList.put(entity, 0);
        System.out.println("-> Add Facility Successfully!!");
    }

    public void addVilla(){
        addFacility("Villa", Villa.class);
        display();
    }

    public void addHouse(){
        addFacility("House", House.class);
        display();
    }

    public void addRoom(){
        addFacility("Room", Room.class);
        display();
    }

    public void addFacility(String facilityType, Class<? extends Facility> facilityClass) {
        String ID;
        do {
            do {
                ID = tools.validateID(facilityType + " ID",
                        "ID Must Follow SVxx-xxxx",
                        "SV(VL|HO|RO)-\\d{4}");

                if (isDuplicateID(ID)) {
                    System.out.println("-> ID Already Exist , Try New One");
                }
            } while (isDuplicateID(ID));
            String facilityName = tools.validateStringInput(facilityType + " Name", errMsg);
            double Area = tools.validateDouble("Area", errMsg, 0);
            double rentalCost = tools.validateDouble("Rental Cost", errMsg, 0);
            int maxPeople = tools.validateInteger("Max People", errMsg, 0);
            String rentalType = tools.validateStringInput("Rental Type", errMsg);

            Facility newFacility = null;

            if (facilityClass == Villa.class) {
                String roomStandard = tools.validateStringInput("Room Standard", errMsg);
                double poolArea = tools.validateDouble("Pool Area", errMsg, 0);
                int numberOfFloor = tools.validateInteger("Number Of Floor", errMsg, 0);
                newFacility = new Villa(ID, facilityName, Area, rentalCost, maxPeople, rentalType, roomStandard, poolArea, numberOfFloor);

            } else if (facilityClass == House.class) {
                String roomStandard = tools.validateStringInput("Room Standard", errMsg);
                int numberOfFloor = tools.validateInteger("Number Of Floor", errMsg, 0);
                newFacility = new House(ID, facilityName, Area, rentalCost, maxPeople, rentalType, roomStandard, numberOfFloor);
            } else if (facilityClass == Room.class) {
                String freeService = tools.validateStringInput("Free Service", errMsg);
                newFacility = new Room(ID, facilityName, Area, rentalCost, maxPeople, rentalType, freeService);
            }
            if (newFacility != null){
                add(newFacility);
            }
        } while (tools.validateStringInput("-> Do You Want To Continue (Y/N)", "Invalid Input, Try Again").equalsIgnoreCase("Y"));
        if (tools.validateStringInput("-> Do you want to save changes to file (Y/N): ", errMsg).equalsIgnoreCase("Y")) {
            save();
        }
    }

    private boolean isDuplicateID(String ID) {
        return facilityList.keySet().stream().anyMatch(facility -> facility.getFacilityID().equalsIgnoreCase(ID));
    }


    @Override
    public void save() {
        faciRepository.writeFile(facilityList);
        System.out.println("Facilities saved successfully.");
    }

    @Override
    public void update() {
    }
}
