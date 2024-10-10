package Model;

public class Room extends Facility{
    private String freeService;

    public Room(String facilityID, String facilityName, double area, double rentalCost, int maxPeople, String rentalType, String freeService) {
        super(facilityID, facilityName, area, rentalCost, maxPeople, rentalType);
        this.freeService = freeService;
    }

    public String getFreeService() {
        return freeService;
    }

    public void setFreeService(String freeService) {
        this.freeService = freeService;
    }
}
