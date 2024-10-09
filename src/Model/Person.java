package Model;

import View.AppTools;

import java.time.LocalDate;

public abstract class Person {

    private String ID;
    private String fullname;
    private LocalDate DOB;
    private boolean gender;
    private String CMND;
    private String phoneNumber;
    private String email;

    public Person() {
    }

    public Person(String ID, String fullname, String DOB, boolean gender, String CMND, String phoneNumber, String email) {
        this.ID = ID;
        this.fullname = fullname;
        this.DOB = AppTools.parseDate(DOB);
        this.gender = gender;
        this.CMND = CMND;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getCMND() {
        return CMND;
    }

    public void setCMND(String CMND) {
        this.CMND = CMND;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
