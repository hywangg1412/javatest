package Controller;

import Model.Employee;
import Service.BookingService;
import Service.CustomerService;
import Service.EmployeeService;
import Service.FacilityService;
import View.AppTools;
import View.Menu;


public class FurmaResortApp extends Menu {
    private static final String MENU_TILTE = "---- FURUMA RESORT ----";
    private static final String[] MENU_OPTION =
            {
                    "Employee Management",
                    "Customer Management",
                    "Facility Management",
                    "Booking Management",
                    "Promotion Management",
                    "Exit"};

    private String errMsg;
    private AppTools tools;

    public FurmaResortApp() {
        super(MENU_OPTION, MENU_TILTE);
        errMsg = "-> Invalid Input, Try Again";
        tools = new AppTools();
    }

    @Override
    public void execute(int n) {
        switch (n) {
            case 1 -> employeeManagementMenu();
            case 2 -> customerManagement();
            case 3 -> facilityManagement();
            case 4 -> bookingManagement();
            case 5 -> promotionManagement();
            case 6 -> System.out.println("-> Exiting.....");
            default -> System.out.println(errMsg);
        }
    }

    public void employeeManagementMenu() {
        EmployeeService empService = new EmployeeService();
        String[] empOpt = {
                "Display list employees",
                "Add new employee",
                "Edit employee",
                "Back main menu"};
        Menu empMenu = new Menu(empOpt, "---- EMPLOYEE MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1 -> empService.display();
                    case 2 -> empService.addEmployee();
                    case 3 -> {
                        String editID = tools.validateID("Enter Employee ID Want To Edit", "ID Must Follow EMP-0000", "EMP-\\d{4}");
                        Employee foundEmp = empService.findByID(editID);
                        empService.update(foundEmp);
                    }
                    case 4 -> System.out.println("-> Redirecting....");
                    default -> System.out.println(errMsg);
                }
            }
        };
        empMenu.run();
    }

    public void customerManagement() {
        CustomerService customerService = new CustomerService();
        String[] customerOpt = {
                "Display list customer",
                "Add new customer",
                "Edit customer",
                "Back main menu"};
        Menu customerMenu = new Menu(customerOpt, "---- CUSTOMER MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1 -> customerService.display();
                    case 2 -> customerService.addCustomer();
                    case 3 -> customerService.updateEMp();
                    case 4 -> System.out.println("-> Redirecting....");
                    default -> System.out.println(errMsg);
                }
            }
        };
        customerMenu.run();
    }

    public void facilityManagement() {
        FacilityService facilityService = new FacilityService();
        String[] facilityOpt = {
                "Add New Villa",
                "Add New House",
                "Add New Room",
                "Back main menu"};
        Menu faciMenu = new Menu(facilityOpt, "---- FACILITY MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1 -> facilityService.addVilla();
                    case 2 -> facilityService.addHouse();
                    case 3 -> facilityService.addRoom();
                    case 4 -> System.out.println("-> Redirecting....");
                    default -> System.out.println(errMsg);
                }
            }
        };
        faciMenu.run();
    }

    public void bookingManagement() {
        BookingService bookingService = new BookingService();
        String[] bookingOpt = {
                "Add new booking",
                "Display booking list",
                "Create new contracts",
                "Display contracts list",
                "Edit contracts",
                "Back main menu"};
        Menu bookingMenu = new Menu(bookingOpt, "---- BOOKING MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 1 -> bookingService.addBooking();
                    case 2 -> bookingService.display();
                    case 6 -> System.out.println("-> Redirecting....");
                    default -> System.out.println(errMsg);
                }
            }
        };
        bookingMenu.run();
    }

    public void promotionManagement() {
        String[] promoOpt = {
                "Display list customers use service",
                "Display list customers get voucher",
                "Back main menu"};
        Menu promoMenu = new Menu(promoOpt, "---- PROMOTION MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                switch (n) {
                    case 3 -> System.out.println("-> Redirecting....");
                    default -> System.out.println(errMsg);
                }
            }
        };
        promoMenu.run();
    }

    public static void main(String[] args) {
        new FurmaResortApp().run();
    }

}
