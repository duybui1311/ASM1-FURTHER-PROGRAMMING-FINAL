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
            System.out.println("2. Login as a Policy Holder");
            System.out.println("3. Login as a Dependent");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                // This will lead to AdminView, which is the main part of this project
                case 1 -> {
                    AdminView adminView = new AdminView();
                    adminView.authenticateAdmins();
                }
                case 2 -> {
                    System.out.println("This is an addition feature. Do you really want to explore it? (yes/no): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        PolicyHolderView policyHolderView = new PolicyHolderView();
                        policyHolderView.authenticateUser();
                    } else {
                        return;
                    }

                }
                case 3 -> {
                    System.out.println("This is an addition feature. Do you really want to explore it? (yes/no): ");
                    String confirmation = scanner.nextLine();
                    if (confirmation.equalsIgnoreCase("yes")) {
                        DependentView dependentView = new DependentView();
                        dependentView.authenticateUser();
                    } else {
                        return;
                    }
                }
            }
        }
    }
}