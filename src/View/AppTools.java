package View;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.StringJoiner;

public class AppTools {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final Scanner scanner = new Scanner(System.in);

    // Get user input
    public String getString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    // Validate string input
    public String validateString(String prompt, String errorMsg) {
        String input;
        while (true) {
            input = getString(prompt);
            if (!input.isEmpty() && input.matches("^[\\S]*$")) {
                return input;
            } else {
                printErrorMessage(errorMsg);
            }
        }
    }

    // Validate double input with minimum value
    public double validateDouble(String prompt, String errorMsg, double minValue) {
        double value;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                value = Double.parseDouble(input);
                if (value > minValue) {
                    return value;
                } else {
                    System.out.println(errorMsg + " - Value must be greater than " + minValue + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMsg + " - Please enter a valid number.");
            }
        }
    }

    // Validate integer input with optional minimum value
    public int validateInteger(String prompt, String errorMsg, Integer minValue) {
        int value;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                value = Integer.parseInt(input);
                if (minValue == null || value >= minValue) {
                    return value;
                } else {
                    System.out.println(errorMsg + " - Value must be at least " + minValue + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMsg + " - Please enter a valid integer.");
            }
        }
    }


    // Normalize names
    public String normalizeName(String name) {
        String cleanedName = name.toLowerCase().replaceAll("[^\\p{L}\\d\\s]", "").trim();
        cleanedName = cleanedName.replaceAll("[,\\s]+", " ");
        StringJoiner normalized = new StringJoiner(" ");
        for (String word : cleanedName.split(" ")) {
            if (!word.isEmpty()) {
                normalized.add(Character.toUpperCase(word.charAt(0)) + word.substring(1));
            }
        }
        return normalized.toString();
    }

    // Print error messages
    private void printErrorMessage(String message) {
        System.out.println("\u001B[31m" + message + "\u001B[0m");
    }

    // Validate string input with specific conditions
    public String validateStringInput(String prompt, String errorMsg) {
        String input;
        while (true) {
            input = getString(prompt);
            if (!input.isEmpty() && input.matches("^[\\p{L} .'-]+$")) {
                return input;
            } else {
                printErrorMessage(errorMsg + " - Please enter a valid string without numbers or special characters.");
            }
        }
    }

    // Validate ID with regex
    public String validateID(String prompt, String errorMsg, String regex) {
        String input;
        while (true) {
            input = getString(prompt);
            if (input.isEmpty() || !input.matches(regex)) {
                printErrorMessage(errorMsg + " - ID must match the format and cannot be empty.");
            } else {
                return input;
            }
        }
    }

    // Validate phone number
    public String validatePhoneNumber(String prompt, String errorMsg) {
        String input;
        while (true) {
            input = getString(prompt);
            if (input.isEmpty() || !input.matches("^\\d{10}$")) {
                printErrorMessage(errorMsg + " - Phone number must be exactly 10 digits and cannot be empty.");
            } else {
                return input;
            }
        }
    }

    // Parse date from string
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid Date Format, Please Try (yyyy-MM-dd)");
            return null;
        }
    }

    // Convert LocalDate to String
    public static String localDateToString(LocalDate date) {
        return (date == null) ? null : date.format(DATE_FORMATTER);
    }

    // Validate email address
    public String validateEmail(String prompt, String errorMsg) {
        String input;
        while (true) {
            input = getString(prompt);
            if (input.isEmpty() || !input.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,3}$")) {
                printErrorMessage(errorMsg + " - Please enter a valid email address.");
            } else {
                return input;
            }
        }
    }

    // Validate date of birth
    public LocalDate validateDateOfBirth(String prompt, String errorMsg) {
        LocalDate parsedDate;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                parsedDate = LocalDate.parse(input, DATE_FORMATTER);
                if (isAgeValid(parsedDate)) {
                    return parsedDate;
                } else {
                    System.out.println("You must be at least 18 years old.");
                }
            } catch (DateTimeParseException e) {
                System.out.println(errorMsg + " - Date must be in the format (yyyy-MM-dd)");
            }
        }
    }

    // Validate booking date
    public LocalDate validateBookingDate(String prompt, String errorMsg) {
        LocalDate parsedDate;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                parsedDate = LocalDate.parse(input, DATE_FORMATTER);
                if (isFutureDate(parsedDate)) {
                    return parsedDate;
                } else {
                    System.out.println("The booking date must be in the future.");
                }
            } catch (DateTimeParseException e) {
                System.out.println(errorMsg + " - Date must be in the format (yyyy-MM-dd)");
            }
        }
    }

    // Validate start date
    public LocalDate validateStartDate(LocalDate bookingDate, String prompt, String errorMsg) {
        LocalDate parsedDate;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                parsedDate = LocalDate.parse(input, DATE_FORMATTER);
                if (parsedDate.isAfter(bookingDate)) {
                    return parsedDate;
                } else {
                    System.out.println("The start date must be after the booking date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println(errorMsg + " - Date must be in the format (yyyy-MM-dd)");
            }
        }
    }

    // Validate end date
    public LocalDate validateEndDate(LocalDate startDate, String prompt, String errorMsg) {
        LocalDate parsedDate;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                parsedDate = LocalDate.parse(input, DATE_FORMATTER);
                if (parsedDate.isAfter(startDate)) {
                    return parsedDate;
                } else {
                    System.out.println("The end date must be after the start date.");
                }
            } catch (DateTimeParseException e) {
                System.out.println(errorMsg + " - Date must be in the format (yyyy-MM-dd)");
            }
        }
    }


    // Check if the date is in the future
    private boolean isFutureDate(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

    // Check if age is valid (18 years or older)
    private boolean isAgeValid(LocalDate BOD) {
        LocalDate today = LocalDate.now();
        int age = today.getYear() - BOD.getYear();
        if (today.getMonthValue() < BOD.getMonthValue() ||
                (today.getMonthValue() == BOD.getMonthValue() && today.getDayOfMonth() < BOD.getDayOfMonth())) {
            age--;
        }
        return age >= 18;
    }

    // Validate gender input
    public String validateGender(String prompt, String errorMsg) {
        String input;
        while (true) {
            System.out.print(prompt + ": ");
            input = scanner.nextLine().trim().toLowerCase();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            if (input.equals("male") || input.equals("m")) {
                return "Male";
            } else if (input.equals("female") || input.equals("f")) {
                return "Female";
            } else {
                System.out.println(errorMsg + " - Please enter 'Male (M)' or 'Female (F)'.");
            }
        }
    }

    // Validate salary input
    public double validateSalary(String prompt, String errorMsg) {
        double salary;
        while (true) {
            System.out.print(prompt + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(errorMsg + " - Input cannot be empty.");
                continue;
            }

            try {
                salary = Double.parseDouble(input);
                if (salary > 0) {
                    return salary;
                }
                System.out.println("Salary must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println(errorMsg + " - Salary must be a valid number.");
            }
        }
    }

    // Validate ID card number
    public String validateIDCard(String prompt, String errorMsg) {
        String input;
        while (true) {
            input = getString(prompt);
            if (input.isEmpty() || (!input.matches("^\\d{9}$") && !input.matches("^\\d{12}$"))) {
                printErrorMessage(errorMsg + " - ID Card must be 9 or 12 digits");
            } else {
                return input;
            }
        }
    }
}
