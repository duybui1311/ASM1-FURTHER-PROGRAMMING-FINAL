package InsuranceClaimManagementSystem.View;

import InsuranceClaimManagementSystem.util.DataGenerator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Initialize the Scanner object

        // Generate sample data
        DataGenerator.main(new String[]{});

        // Welcome message
        System.out.println("=============================================================== WELCOME TO DUY BUI'S INSURANCE CLAIMS MANAGEMENT SYSTEM! ===============================================================");

        // Main menu
        while (true) {
            System.out.println("\nPlease login ");
            System.out.println("1. Login as Admin");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                // This will lead to AdminView, which is the main part of this project
                case 1 -> {
                    AdminView adminView = new AdminView();
                    adminView.adminLogin();
                }
                default -> System.err.println("Invalid input. Please try again.");
            }
        }
    }
}
