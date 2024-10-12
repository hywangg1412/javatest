package Model;

import View.AppTools;

import java.time.LocalDate;

public abstract class Person {

    private String ID;
    private String fullName;
    private LocalDate DOB;
    private boolean Gender;
    private String CMND;
    private String phoneNumber;
    private String Email;

    public Person() {
    }

    public Person(String ID, String fullname, LocalDate DOB, boolean gender, String CMND, String phoneNumber, String email) {
        this.ID = ID;
        this.fullName = fullname;
        this.DOB = DOB;
        this.Gender = gender;
        this.CMND = CMND;
        this.phoneNumber = phoneNumber;
        this.Email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDOB() {
        return DOB;
    }

    public void setDOB(LocalDate DOB) {
        this.DOB = DOB;
    }

    public boolean isGender() {
        return Gender;
    }

    public void setGender(boolean gender) {
        this.Gender = gender;
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
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }
}
