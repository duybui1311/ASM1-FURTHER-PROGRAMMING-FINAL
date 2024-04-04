package InsuranceClaimManagementSystem.View;


import InsuranceClaimManagementSystem.Controller.*;
import InsuranceClaimManagementSystem.Model.*;

import java.util.Optional;
import java.util.Scanner;

public class DependentView {
    private final DependentsController dependentsController = DependentsController.getInstance();
    private final InsuranceCardController insuranceCardController = InsuranceCardController.getInstance();
    private Dependent currentDependent;
    private final ClaimView claimView;
    private final Scanner scanner = new Scanner(System.in);

    public DependentView() {
        this.currentDependent = dependentsController.getCurrentDependent();
        this.claimView = new ClaimView(currentDependent);
    }

    // Authenticate dependent logins
    public void authenticateUser() {
        int maxAttempts = 5; // Limits fail attempts
        int attempts = 0;

        while (true) {
            System.out.println("________________________________________________________________________________DEPENDENT LOGIN____________________________________________________________________________________");
            System.out.println("Enter your user ID:");
            String inputID = scanner.nextLine();

            System.out.println("Enter your full name: ");
            String inputName = scanner.nextLine();

            // Authenticate login
            Dependent dependentCustomer = dependentsController.authenticateDependent(inputID, inputName);

            if (dependentCustomer != null) {
                currentDependent = dependentCustomer;
                System.out.println("Login successful. Welcome, " + inputName + "!");
                dependentsController.setCurrentDependent(currentDependent);
                insuranceCardController.deserializeInsuranceCardsFromFile("data/insuranceCards.dat");
                menu(); // Proceed to main menu
                return; // Exit the method
            } else {
                System.out.println("Login failed. Please check your user ID and full name.");
                attempts++;

                // Limit login attempts
                if (attempts < maxAttempts) {
                    System.out.println("Login failed. Please check your user ID and full name.");
                    System.out.println("Attempts remaining: " + (maxAttempts - attempts));
                    System.out.println("1. Try again");
                    System.out.println("2. Cancel");
                    System.out.println("Enter your choice: "); // Asking for user confirmation
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice != 1) {
                        System.out.println("Exiting dependent login...");
                        return;
                    }
                } else {
                    System.out.println("Maximum login attempts reached. Exiting dependent login...");
                    break;
                }
            }
        }
    }

    // Display the home menu for the dependents
    public void menu() {
        while (true) {
            System.out.println("========================================================================= WELCOME DEPENDENT CUSTOMER =========================================================================");
            System.out.println("You can choose one of the following options: ");
            System.out.println("1. View Insurance Card");
            System.out.println("2. View Personal Information");
            System.out.println("3. Manage Claims");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewInsuranceCard();
                case 2 -> this.viewPersonalInfo();
                case 3 -> {
                    claimView.setCurrentCustomer(currentDependent);
                    claimView.viewClaimsMenu();
                }
                default -> {
                    System.out.println("Invalid input.");
                    return;
                }
            }
        }
    }

    // Allows dependent to view his/her insurance card
    public void viewInsuranceCard() {
        System.out.println("________________________________________________________________________________DEPENDENT - VIEW INSURANCE CARD____________________________________________________________________________________");

        // Get the insurance card
        InsuranceCard insuranceCard = dependentsController.getInsuranceCard(currentDependent.getCustomerID(), currentDependent.getFullName());
        if (insuranceCard != null) {
            System.out.println("Insurance Card Details: ");
            System.out.println("Card Number: " + insuranceCard.getCardNumber());
            System.out.println("Policy Owner: " + insuranceCard.getPolicyOwner());
            System.out.println("Expiration Date: " + insuranceCard.getExpirationDate());
        } else {
            System.out.println("No insurance card found for the provided ID and full name.");
        }
    }

    public void viewPersonalInfo() {
        if (currentDependent != null) {
            System.out.println("__________________________________________________________________________DEPENDENT - VIEW PERSONAL INFORMATION____________________________________________________________________________________");
            System.out.println("ID: " + currentDependent.getCustomerID());
            System.out.println("Name: " + currentDependent.getFullName());
            System.out.println("Insurance Card: " + currentDependent.getInsuranceCard().toString());
            System.out.println(currentDependent.getPolicyHolder().toString());
        }
    }
}
