package InsuranceClaimManagementSystem.View;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Welcome message
        System.out.println("=============================================================== WELCOME TO INSURANCE CLAIMS MANAGEMENT SYSTEM! ===============================================================");
        while (true) {
            System.out.println("Please login (enter '0' to cancel): ");
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
                }
            }
        }
    }