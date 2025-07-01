package personalfinancemanager.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

 public class ConsoleInput {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (yyyy-MM-dd): ");
            String input = scanner.nextLine().trim();
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
        }
    }
}

