package InsuranceClaimManagementSystem.View;

import InsuranceClaimManagementSystem.Controller.*;
import InsuranceClaimManagementSystem.Model.*;

import java.util.List;
import java.util.Scanner;

public class PolicyHolderView {
    private final ClaimsController claimsController = ClaimsController.getInstance();
    private final CustomersController customersController = CustomersController.getInstance();
    private final PolicyHoldersController policyHoldersController = PolicyHoldersController.getInstance();
    private final DependentsController dependentsController = DependentsController.getInstance();
    private final InsuranceCardController insuranceCardController = InsuranceCardController.getInstance();
    private PolicyHolder currentPolicyHolder;
    private final ClaimView claimView;
    private final Scanner scanner = new Scanner(System.in);

    public PolicyHolderView() {
        this.currentPolicyHolder = policyHoldersController.getCurrentPolicyHolder();
        this.claimView = new ClaimView(currentPolicyHolder);
    }

    // Manage user login
    public void authenticateUser() {
        int maxAttempts = 5; // Limits fail attempts
        int attempts = 0;

        while (true) {
            policyHoldersController.deserializePolicyHoldersFromFile("data/policyholders.dat");
            System.out.println("________________________________________________________________________________POLICY HOLDER LOGIN____________________________________________________________________________________");
            System.out.println("Enter your user ID:");
            String inputID = scanner.nextLine();

            System.out.println("Enter your full name: ");
            String inputName = scanner.nextLine();

            // Authenticate the login
            PolicyHolder policyHolderCustomer = policyHoldersController.authenticatePolicyHolder(inputID, inputName);

            if (policyHolderCustomer != null) {
                currentPolicyHolder = policyHolderCustomer; // Set the current policy holder
                System.out.println("Login successful. Welcome, " + inputName + "!"); // Return welcome message
                policyHoldersController.setCurrentPolicyHolder(currentPolicyHolder); // Set the current policy holder in the controller
                insuranceCardController.deserializeInsuranceCardsFromFile("data/insuranceCards.dat");
                dependentsController.deserializeDependentsFromFile("data/dependents.dat", currentPolicyHolder);
                menu(); // Proceed to main menu
                return; // Exit the method
            } else {
                attempts++;
                if (attempts < maxAttempts) {
                    System.out.println("Login failed. Please check your user ID and full name.");
                    System.out.println("Attempts remaining: " + (maxAttempts - attempts));
                    System.out.println("1. Try again");
                    System.out.println("2. Cancel");
                    System.out.println("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice != 1) {
                        System.out.println("Exiting policy holder login...");
                        break;
                    }
                } else {
                    System.out.println("Maximum login attempts reached. Exiting policy holder login...");
                    break;
                }
            }
        }
    }

    // Method to display the menu for policy holders
    public void menu() {
        while (true) {
            System.out.println("========================================================================= WELCOME POLICY HOLDER =========================================================================");
            System.out.println("You can choose one of the following options: ");
            System.out.println("1. Manage Dependents");
            System.out.println("2. View Insurance Card");
            System.out.println("3. Manage Claims");
            System.out.println("4. Manage Personal Information");
            System.out.println("5. Renew or Modify Policy");
            System.out.println("6. Exit");
            System.out.println("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.manageDependents();
                case 2 -> this.viewInsuranceCard();
                case 3 -> {
                    // Set the current policyholder to be the current user in ClaimView
                    claimView.setCurrentCustomer(currentPolicyHolder);
                    claimView.viewClaimsMenu();
                }
                case 4 -> this.managePersonalInfo();
                case 5 -> this.managePolicy();
                case 6 -> {
                    // Exit the program
                    System.out.println("Exiting the program...");
                    policyHoldersController.clearCurrentPolicyHolder();
                    System.exit(0);
                }
                default -> {
                    System.err.println("Error: Invalid input.");
                }
            }
        }
    }

    // Display a menu for managing dependents
    public void manageDependents() {
        dependentsController.deserializeDependentsFromFile("data/dependents.dat", currentPolicyHolder);
        while (true) {
            System.out.println("___________________________________________________________________________POLICY HOLDER - MANAGE DEPENDENTS____________________________________________________________________________________");
            System.out.println("You can choose one of the following options: ");
            System.out.println("1. View All Dependents");
            System.out.println("2. Add A Dependent");
            System.out.println("3. Modify A Dependent");
            System.out.println("4. Remove A Dependent");
            System.out.println("5. Back to Main Menu");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewAllDependents();
                case 2 -> addDependent();
                case 3 -> modifyDependent();
                case 4 -> removeDependent();
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid input");
            }
        }

    }

    // View all dependents
    public void viewAllDependents() {
        System.out.println("______________________________________________________________________POLICY HOLDER - MANAGE DEPENDENTS - VIEW ALL DEPENDENTS____________________________________________________________________________________");

        List<Dependent> dependents = policyHoldersController.getCurrentPolicyHolder().getDependents();

        if (dependents.isEmpty()) {
            System.out.println("No dependents found.");
            manageDependents();
        } else {
            System.out.println("You currently have " + dependents.size() + " dependent(s).");
            System.out.println("List of dependents:");

            // Display header
            System.out.printf("%-20s | %-70s\n", "ID", "Full name");
            System.out.println("-----------------------------------------------------");

            // Display content
            for (Dependent dependent : dependents) {
                System.out.printf("%-20s | %-70s\n", dependent.getCustomerID(), dependent.getFullName());
            }
            System.out.println("-----------------------------------------------------");

            // User can view a dependent's details
            System.out.println("Enter the ID of a dependent that you want to view (enter 'cancel' to cancel): ");
            String selectedID = scanner.nextLine();
            if (!selectedID.equalsIgnoreCase("cancel")) {
                displayADependentDetails(selectedID);
            } else {
                // Return to the manage dependents menu
                manageDependents();
            }
        }
    }

    // Display a dependent details
    public void displayADependentDetails(String dependentID) {
        Dependent dependent = policyHoldersController.getDependentByID(dependentID);
        if (dependent != null) {
            System.out.println("ID: " + dependent.getCustomerID());
            System.out.println("Full name: " + dependent.getFullName());
            System.out.println(dependent.getInsuranceCard().toString());

        } else {
            System.out.println("Error: No dependent found. Please try again.");
        }
    }

    // Method to display the user's insurance card
    public void viewInsuranceCard() {
        while (true) {
            System.out.println("________________________________________________________________________________POLICY HOLDER - VIEW INSURANCE CARD____________________________________________________________________________________");

            // Get the insurance card
            InsuranceCard insuranceCard = policyHoldersController.getInsuranceCard(currentPolicyHolder.getCustomerID(), currentPolicyHolder.getFullName());
            if (insuranceCard != null) {
                System.out.println("Insurance Card Details: \n");
                System.out.println("Card Number: " + insuranceCard.getCardNumber());
                System.out.println("Policy Owner: " + insuranceCard.getPolicyOwner());
                System.out.println("Card Holder: " + insuranceCard.getCardHolder());
                System.out.println("Expiration Date: " + insuranceCard.getExpirationDate());
                System.out.println("\nEnter any key to exit: ");
                String input = scanner.nextLine();
                if (input != null) {
                    return;
                }
            } else {
                System.out.println("No insurance card found for the provided ID and full name.");
                return;
            }
        }

    }

    // Method to display adding dependent procedure
    public void addDependent() {
        System.out.println("______________________________________________________________________POLICY HOLDER - MANAGE DEPENDENTS - ADD A DEPENDENT____________________________________________________________________________________");
        while (true){
            System.out.println("Adding a new dependent (Enter 'cancel' at any time to cancel the procedure.");
            System.out.println("Enter the dependent's full name: ");
            String fullName = scanner.nextLine();

            if (fullName.equals("cancel")) {
                System.out.println("Procedure has been canceled.");
                break;
            }

            // Create a new dependent
            String newDependentID = customersController.generateUserID();
            Dependent newDependent = new Dependent(newDependentID, fullName, null, currentPolicyHolder);
            policyHoldersController.addDependent(currentPolicyHolder, newDependent);

            // Serialize the new dependent into the system
            dependentsController.serializeDependentsToFile("data/dependents.dat");

            // Set the insurance card
            newDependent.setInsuranceCard(currentPolicyHolder.getInsuranceCard());

            // Print out the result and view all dependents
            System.out.println("Dependent" + newDependent.getFullName() + "has been added successfully!");
            viewAllDependents();
        }
    }

    // Method to display modifying a dependent procedure
    public void modifyDependent() {
        System.out.println("_____________________________________________________________POLICY HOLDER - MANAGE DEPENDENTS - MODIFY A DEPENDENT'S INFORMATION____________________________________________________________________________________");
        System.out.println("Enter the dependent's ID: ");
        String selectedID = scanner.nextLine();

        // Check if the targeted dependent exists
        Dependent dependentToEdit = policyHoldersController.getDependentByID(selectedID);
        if (dependentToEdit == null) {
            System.err.println("Error: Dependent with this ID does not exist.");
            return;
        }

        // Display the current details of the targeted dependent
        System.out.println("Dependent found: ");
        displayADependentDetails(selectedID);

        // Edit dependent's name
        System.out.println("Enter the new dependent name (enter 'cancel' to cancel): ");
        String newName = scanner.nextLine();
        if (!newName.equals("cancel")) {
            dependentToEdit.setFullName(newName);
        }

        // Asking for user's confirmation
        System.out.println("Do you want to save your changes (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            dependentsController.serializeDependentsToFile("data/dependents.dat"); // Updating the dependent
            System.out.println("Dependent has been edited successfully.");
        } else if (confirmation.equals("no")) {
            System.out.println("Procedure has been canceled.");
            this.manageDependents();
        } else {
            System.out.println("Invalid input. Procedure has been canceled.");
            this.manageDependents();
        }
    }

    // Method to display removing a dependent procedure
    public void removeDependent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("______________________________________________________________________POLICY HOLDER - MANAGE DEPENDENTS - REMOVING A DEPENDENT____________________________________________________________________________________");

        String selectedID;
        do {
            System.out.println("Enter the ID of the dependent (enter 'cancel' to cancel the procedure): ");
            selectedID = scanner.nextLine();
            if (policyHoldersController.dependentExists(selectedID)) {
                // Display dependent information first
                System.out.println("Dependent found: ");
                displayADependentDetails(selectedID);

                System.out.println("Do you want to remove this dependent? (yes/no): ");
                String confirmation = scanner.nextLine();

                if (confirmation.equalsIgnoreCase("yes")) {
                    boolean removed = policyHoldersController.removeDependent(currentPolicyHolder, selectedID);
                    if (removed) {
                        System.out.println("Dependent has been removed successfully.");
                    }
                }
            }
        } while (!selectedID.equals("cancel"));
    }

    // Menu to manage personal information
    public void managePersonalInfo() {
        while (true) {
            System.out.println("_________________________________________________________________________POLICY HOLDER - MANAGE PERSONAL INFORMATION____________________________________________________________________________________");
            System.out.println("You can choose one of the following options:");
            System.out.println("1. View Personal Information");
            System.out.println("2. Edit Personal Information");
            System.out.println("3. Cancel");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewPersonalInfo();
                case 2 -> editPersonalInfo();
                case 3 -> {
                    return;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // Method to view a customer's details
    public void viewPersonalInfo() {
        if (currentPolicyHolder != null) {
            System.out.println("__________________________________________________________________________POLICY HOLDER - VIEW PERSONAL INFORMATION____________________________________________________________________________________");
            System.out.println("ID: " + currentPolicyHolder.getCustomerID());
            System.out.println("Full Name: " + currentPolicyHolder.getFullName());
            System.out.println(currentPolicyHolder.getInsuranceCard().toString());
        }
    }

    // Method to edit policyholder's personal information
    public void editPersonalInfo() {

    }

    // Method to allow policyholder to manage a policy
    public void managePolicy() {

    }
}
