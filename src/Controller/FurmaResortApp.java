package Controller;

import Model.Contract;
import Model.Customer;
import Model.Employee;
import Service.*;
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

    public void execute(int n) {
        try {
            switch (n) {
                case 1 -> employeeManagementMenu();
                case 2 -> customerManagement();
                case 3 -> facilityManagement();
                case 4 -> bookingManagement();
                case 5 -> promotionManagement();
                case 6 -> System.out.println("-> Exiting.....");
                default -> System.out.println(errMsg);
            }
        } catch (Exception e) {
            System.out.println("-> Error occurred: " + e.getMessage());
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
                try {
                    switch (n) {
                        case 1 -> empService.display();
                        case 2 -> empService.addEmployee();
                        case 3 -> {
                            try {
                                String editID = tools.validateID("Enter Employee ID Want To Edit", "ID Must Follow EMP-0000", "EMP-\\d{4}");
                                Employee foundEmp = empService.findByID(editID);
                                if (foundEmp == null) {
                                    System.out.println("-> Not Found Employee With ID: " + editID);
                                    boolean createNew = tools.validateStringInput("-> Do You Want To Create New Employee (Y/N)", errMsg).equalsIgnoreCase("Y");
                                    if (createNew) {
                                        empService.addEmployee();
                                    } else {
                                        System.out.println("-> No Employee Created");
                                    }
                                } else {
                                    empService.update(foundEmp);
                                }
                            } catch (Exception e) {
                                System.out.println("-> Error while editing or creating employee: " + e.getMessage());
                            }
                        }
                        case 4 -> System.out.println("-> Redirecting....");
                        default -> System.out.println(errMsg);
                    }
                } catch (Exception e) {
                    System.out.println("-> Error occurred in Employee Management: " + e.getMessage());
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
                try {
                    switch (n) {
                        case 1 -> customerService.display();
                        case 2 -> customerService.addCustomer();
                        case 3 -> {
                            try {
                                String ID = tools.validateID("Customer ID", "ID Must Follow CUS-0000", "CUS-\\d{4}");
                                Customer foundCustomer = customerService.findByID(ID);
                                if (foundCustomer == null) {
                                    System.out.println("-> Not Found Customer With ID - " + ID);
                                    boolean createNew = tools.validateStringInput("-> Do you Want To Create New Customer (Y/N)", errMsg).equalsIgnoreCase("Y");
                                    if (createNew) {
                                        customerService.addCustomer();
                                    } else {
                                        System.out.println("-> No Customer Created.");
                                    }
                                } else {
                                    customerService.update(foundCustomer);
                                }
                            } catch (Exception e) {
                                System.out.println("-> Error while editing or creating customer: " + e.getMessage());
                            }
                        }
                        case 4 -> System.out.println("-> Redirecting....");
                        default -> System.out.println(errMsg);
                    }
                } catch (Exception e) {
                    System.out.println("-> Error occurred in Customer Management: " + e.getMessage());
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
                "Display Facility List",
                "Back main menu"};
        Menu faciMenu = new Menu(facilityOpt, "---- FACILITY MANAGEMENT ----") {
            @Override
            public void execute(int n) {
                try {
                    switch (n) {
                        case 1 -> facilityService.addVilla();
                        case 2 -> facilityService.addHouse();
                        case 3 -> facilityService.addRoom();
                        case 4 -> facilityService.display();
                        case 5 -> System.out.println("-> Redirecting....");
                        default -> System.out.println(errMsg);
                    }
                } catch (Exception e) {
                    System.out.println("-> Error occurred in Facility Management: " + e.getMessage());
                }
            }
        };
        faciMenu.run();
    }

    public void bookingManagement() {
        BookingService bookingService = new BookingService();
        ContractService contractService = new ContractService();
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
                try {
                    switch (n) {
                        case 1 -> bookingService.addBooking();
                        case 2 -> bookingService.display();
                        case 3 -> contractService.addContract();
                        case 4 -> contractService.display();
                        case 5 -> {
                            try {
                                int editID = tools.validateInteger("Enter Contract ID to edit", errMsg, 0);
                                Contract foundContract = contractService.findByContractNum(editID);
                                if (foundContract == null) {
                                    System.out.println("-> Contract ID not found.");
                                    boolean createNew = tools.validateStringInput("Do you want to create a new contract? (y/n)", errMsg).equalsIgnoreCase("y");
                                    if (createNew) {
                                        contractService.addContract();
                                    } else {
                                        System.out.println("-> No contract created.");
                                    }
                                } else {
                                    contractService.update(foundContract);
                                }
                            } catch (Exception e) {
                                System.out.println("-> Error while editing or creating contract: " + e.getMessage());
                            }
                        }
                        case 6 -> System.out.println("-> Redirecting....");
                        default -> System.out.println(errMsg);
                    }
                } catch (Exception e) {
                    System.out.println("-> Error occurred in Booking Management: " + e.getMessage());
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
