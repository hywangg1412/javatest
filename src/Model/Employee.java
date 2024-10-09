package Model;

public class Employee extends Person{
    private String degree;
    private String position;
    private double salary;

    public Employee(String ID, String fullname, String DOB, boolean gender, String CMND, String phoneNumber, String email, String degree, String position, double salary) {
        super(ID, fullname, DOB, gender, CMND, phoneNumber, email);
        this.degree = degree;
        this.position = position;
        this.salary = salary;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
            this.salary = salary;
    }


}
