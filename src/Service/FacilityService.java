package Service;

import Model.Facility;
import Repository.FacilityRepository;
import View.AppTools;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FacilityService implements IFacilityService {
    private final FacilityRepository faciRepository;
    private final AppTools tools;
    private final LinkedHashMap<Facility, Integer> facilitiList;

    public FacilityService() {
        faciRepository = new FacilityRepository();
        tools = new AppTools();
        facilitiList = faciRepository.readFile();
    }

    @Override
    public void display() {

    }

    @Override
    public void add(Object entity) {
        if (entity instanceof Facility){
            Facility newFacility = (Facility) entity;

        }
    }

    @Override
    public void save() {

    }

    @Override
    public void edit() {

    }
}
