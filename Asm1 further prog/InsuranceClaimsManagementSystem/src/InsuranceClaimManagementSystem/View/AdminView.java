package InsuranceClaimManagementSystem.View;

import InsuranceClaimManagementSystem.Model.*;
import InsuranceClaimManagementSystem.Controller.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AdminView {
    private final AdminController adminController = AdminController.getInstance();
    private final CustomersController customersController = CustomersController.getInstance();
    private final PolicyHoldersController policyHoldersController = PolicyHoldersController.getInstance();
    private final DependentsController dependentsController = DependentsController.getInstance();
    private final ClaimsController claimsController = ClaimsController.getInstance();
    private final InsuranceCardController insuranceCardController = InsuranceCardController.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    // Authenticate admins' login menu
    public void authenticateAdmins() {
        int maxAttempts = 5;
        int attempts = 0;
        while (true) {
            adminController.deserializeAdminsFromFile("data/admins.dat");
            System.out.println("________________________________________________________________________________ADMIN LOGIN____________________________________________________________________________________");
            System.out.println("Enter your username:");
            String username = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            Admin admin = adminController.authenticateAdmin(username, password);

            if (admin != null) {
                System.out.println("Login successful. Welcome, " + admin.getUsername() + "!");

                // Deserialize all data in the system
                policyHoldersController.deserializePolicyHoldersFromFile("data/policyholders.dat");
                dependentsController.deserializeAllDependents("data/dependents.dat");
                insuranceCardController.deserializeInsuranceCardsFromFile("data/insuranceCards.dat");
                claimsController.deserializeAllClaimsFromFile("data/claims.dat");

                menu(); // Proceed to main menu
                return; // Exit the method
            } else {
                attempts++;

                if (attempts < maxAttempts) {
                    System.out.println("Login failed. Please check your username and password.");
                    System.out.println("Attempts remaining: " + (maxAttempts - attempts));
                    System.out.println("1. Try again");
                    System.out.println("2. Cancel");
                    System.out.println("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    if (choice != 1) {
                        System.out.println("Exiting admin login...");
                        break;
                    }
                } else {
                    System.out.println("Maximum login attempts reached. Exiting admin login...");
                    break;
                }
            }
        }
    }

    // Display the menu for admins
    public void menu() {
        while (true) {
            System.out.println("========================================================================= WELCOME ADMIN =========================================================================");
            System.out.println("You can choose one of the following procedure:");
            System.out.println("1. Manage Customers");
            System.out.println("2. Manage Claims");
            System.out.println("3. Manage Insurance Cards");
            System.out.println("4. Exit");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.manageCustomers();
                case 2 -> this.manageClaims();
                case 3 -> this.manageInsuranceCards();
                case 4 -> {
                    System.exit(0);
                    System.out.println("Exiting the system...");
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // Menu for admins to manage the customers
    public void manageCustomers() {
        while (true) {
            System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS____________________________________________________________________________________");
            System.out.println("You can choose one of the following options:");
            System.out.println("1. View customers");
            System.out.println("2. Add a new policy holder");
            System.out.println("3. Add a new dependent");
            System.out.println("4. Remove a customer");
            System.out.println("5. Modify a customer");
            System.out.println("6. Exit");
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewAllCustomersMenu();
                case 2 -> this.addPolicyHolder();
                case 3 -> this.addDependent();
                case 4 -> this.removeCustomer();
                case 5 -> this.modifyCustomer();
                case 6 -> {
                    this.menu();
                    return;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // Method to view all customers in the system (admin can choose to view all customers, all policyholders, or all dependents)
    public void viewAllCustomersMenu() {
        while (true) {
            System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - VIEW ALL CUSTOMER____________________________________________________________________________________");
            System.out.println("You can choose one of the following procedure:");
            System.out.println("1. View all customers");
            System.out.println("2. View all policy holders");
            System.out.println("3. View all dependents");
            System.out.println("4. View a customer's details");
            System.out.println("5. Cancel");
            System.out.println("Enter your choice:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewAllCustomers();
                case 2 -> this.viewAllPolicyHolders();
                case 3 -> this.viewAllDependents();
                case 4 -> this.viewACustomerDetails();
                case 5 -> {
                    this.manageCustomers();
                    return;
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    public void viewAllCustomers() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - VIEW ALL CUSTOMERS____________________________________________________________________________________");
        List<Customer> customers = adminController.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("No customers found.");
            this.viewAllCustomersMenu();
        } else {
            System.out.println("There are currently " + customers.size() + " users in the system.");

            // Display header
            System.out.printf("%-20s | %-40s | %-40s | %-40s\n" , "ID", "Full name", "Insurance Card Number", "Role");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            // Display content
            for (Customer customer : customers) {
                String id = customer.getCustomerID();
                String fullName = customer.getFullName();
                int insuranceCardNumber = customer.getInsuranceCard().getCardNumber();
                String role = (customer instanceof PolicyHolder) ? "Policy Holder" : "Dependent";

                System.out.printf("%-20s | %-40s | %-40s | %-40s\n", id, fullName, insuranceCardNumber, role);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public void viewAllPolicyHolders() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - VIEW ALL CUSTOMERS - VIEW ALL POLICY HOLDERS____________________________________________________________________________________");
        List<PolicyHolder> policyHolders = adminController.getAllPolicyHolders();
        if (policyHolders.isEmpty()) {
            System.out.println("No policy holder found.");
            this.viewAllCustomersMenu();
        } else {
            System.out.println("There are currently " + policyHolders.size() + " policy holders in the system.");

            // Display header
            System.out.printf("%-20s | %-70s | %-70s\n" , "ID", "Full name", "Insurance Card Number");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            // Display content
            for (PolicyHolder policyHolder : policyHolders) {
                String id = policyHolder.getCustomerID();
                String fullName = policyHolder.getFullName();
                int insuranceCardNumber = policyHolder.getInsuranceCard().getCardNumber();
                System.out.printf("%-20s | %-70s | %-70s\n", id, fullName, insuranceCardNumber);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        }
    }

    public void viewACustomerDetails() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - VIEW ALL CUSTOMERS - VIEW A CUSTOMER DETAILS____________________________________________________________________________________");
        System.out.println("Enter customer ID (enter 'cancel' to cancel): ");
        String customerID = scanner.nextLine();

        if (customerID.equalsIgnoreCase("cancel")) {
            System.out.println("Procedure has been canceled.");
            return;
        }

        this.displayCustomerDetails(customerID);
    }

    private void displayCustomerDetails(String customerID) {
        Customer customer = adminController.findCustomer(customerID);
        if (customer == null) {
            System.err.println("No customer found. Please try again.");
        } else {
            System.out.println("Customer found: ");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Customer ID: " + customer.getCustomerID());
            System.out.println("Customer full name: " + customer.getFullName());
            String role;
            if (customer instanceof PolicyHolder) {
                role = "Policy Holder";
                System.out.println("Role: " + role);
                System.out.println("Dependents: ");
                for (Dependent dependent : ((PolicyHolder) customer).getDependents()) {
                    System.out.println(dependent.getFullName() + " (" + dependent.getCustomerID() + ")");
                }
            } else if (customer instanceof Dependent) {
                role = "Dependent";
                System.out.println("Role: " + role);
            }
            System.out.println("Insurance card: " + customer.getInsuranceCard().getCardNumber());
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------");
        }

    }

    public void viewAllDependents() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - VIEW ALL CUSTOMERS - VIEW ALL DEPENDENTS____________________________________________________________________________________");
        List<Dependent> dependents = adminController.getAllDependents();
        if (dependents.isEmpty()) {
            System.out.println("No dependents found.");
            this.viewAllCustomersMenu();
        } else {
            System.out.println("There are currently " + dependents.size() + " dependents in the system.");

            // Display header
            System.out.printf("%-20s | %-70s | %-70s\n" , "ID", "Full name", "Insurance Card Number");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            // Display content
            for (Dependent dependent : dependents) {
                String id = dependent.getCustomerID();
                String fullName = dependent.getFullName();
                int insuranceCardNumber = dependent.getInsuranceCard().getCardNumber();
                System.out.printf("%-20s | %-70s | %-70s\n", id, fullName, insuranceCardNumber);
            }
        }
    }

    public void addPolicyHolder() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - CREATE NEW POLICY HOLDER____________________________________________________________________________________");
        System.out.println("Enter the policy holder's name (enter 'cancel' to cancel): ");
        String newPolicyHolderName = scanner.nextLine();

        if (newPolicyHolderName.equals("cancel")) {
            System.out.println("Procedure has been canceled.");
            return;
        }

        System.out.println("Enter the policy owner (enter 'cancel' to cancel): ");
        String newPolicyOwner = scanner.nextLine();
        if (newPolicyOwner.equals("cancel")) {
            System.out.println("Procedure has been canceled.");
            return;
        }

        // Asking for user's confirmation
        System.out.println("Do you want to create this policy holder with new insurance card (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equals("yes")) {
            String newPolicyHolderID = customersController.generateUserID();
            PolicyHolder newPolicyHolder = new PolicyHolder(newPolicyHolderID, newPolicyHolderName, null);

            // Create new insurance card
            InsuranceCard newPolicyHolderInsuranceCard = insuranceCardController.generateInsuranceCard(newPolicyHolder, newPolicyHolder, newPolicyOwner);
            newPolicyHolder.setInsuranceCard(newPolicyHolderInsuranceCard); // Assign new insurance card to new policy holder

            // Serialize new policy holder to the system
            policyHoldersController.serializePolicyHoldersToFile("data/policyholders.dat");
            System.out.println("New policy holder with new insurance card has been created: ");

            this.manageCustomers(); // Return to manage customers menu

        } else {
            System.out.println("Procedure has been canceled.");
            this.manageCustomers(); // Return to manage customers menu
        }
    }

    public void addDependent() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - CREATE NEW DEPENDENT____________________________________________________________________________________");
        System.out.println("Enter a policy holder ID to add a dependent:");
        String policyHolderID = scanner.nextLine();
        PolicyHolder policyHolder = policyHoldersController.findPolicyHolderByID(policyHolderID);

        System.out.println("Policy Holder found: ");
        System.out.println("Policy Holder ID: " + policyHolder.getCustomerID());
        System.out.println("Policy Holder Name: " + policyHolder.getFullName());
        System.out.println("Insurance Card Number: " + policyHolder.getInsuranceCard().getCardNumber());
        System.out.println("Current Dependents: ");
        for (Dependent dependent : policyHolder.getDependents()) {
            System.out.println(dependent.getFullName() + " (" + dependent.getCustomerID() + ")");
        }

        // Asking for user's confirmation
        System.out.println("Do you want to add a new dependent to this policy holder? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equals("yes")) {
            // Asking for new dependent's name
            System.out.println("Enter new dependent's full name (enter 'cancel' to cancel): ");
            String newDependentName = scanner.nextLine();

            if (newDependentName.equalsIgnoreCase("cancel")) {
                System.out.println("Procedure has been canceled.");
            } else {
                // Create a new dependent instance
                Dependent newDependent = new Dependent(customersController.generateUserID(), newDependentName, null, policyHolder);

                // Assign insurance card
                newDependent.setInsuranceCard(policyHolder.getInsuranceCard());

                // Add new dependent to the policy holder's class
                policyHolder.addDependent(newDependent);

                // Add new dependent into the controller
                dependentsController.addDependent(newDependent);

                System.out.println("New dependent has been added!");

                // Serialize the dependent into the system
                dependentsController.serializeDependentsToFile("data/dependents.dat");
            }
        } else {
            System.out.println("Procedure has been canceled.");
        }
    }

    public void removeCustomer() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE USERS - REMOVE A CUSTOMER____________________________________________________________________________________");
        System.out.println("Enter the ID of the customer you want to remove (enter 'cancel' to cancel): ");
        String customerID = scanner.nextLine();

        if (customerID.equalsIgnoreCase("cancel")) {
            return;
        }

        // Display the customer details
        System.out.println("Customer found: ");
        this.displayCustomerDetails(customerID);

        // Prompt user confirmation
        System.out.println("Do you want to remove this user? (yes/no):");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            adminController.removeUser(customerID);
            System.out.println("Customer has been removed successfully.");
        } else if (confirmation.equalsIgnoreCase("no")) {
            System.out.println("Procedure has been canceled.");
        } else {
            System.out.println("Invalid input. Procedure has been canceled");
        }

    }

    public void modifyCustomer() {

    }

    public void manageClaims() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE CLAIMS____________________________________________________________________________________");
        while (true) {
            System.out.println("You can choose one of the following procedures:");
            System.out.println("1. View All Claims");
            System.out.println("2. Modify A Claim");
            System.out.println("3. Cancel");
            System.out.println("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewAllClaims();
                case 2 -> this.modifyAClaim();
                case 3 -> {
                    this.menu();
                    return;
                }
                default -> System.err.println("Invalid input. Please try again.");
            }
        }
    }

    // Display all claims
    public void viewAllClaims() {
        System.out.println("________________________________________________________________________________ADMIN - MANAGE CLAIMS - VIEW ALL CLAIMS____________________________________________________________________________________");
        List<Claim> claims = claimsController.getAllClaims();

        if (claims.isEmpty()) {
            System.out.println("No claims found in the system.");
        } else {
            System.out.println("All claims in the system:");
            // Display header
            System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                    "ID", "Date", "Insured Person", "Banking Info", "Card Number", "Exam Date", "Documents", "Claim Amount", "Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Display content
            for (Claim claim : claims) {
                System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                        claim.getClaimID(), claim.getClaimDate(), claim.getInsuredPerson(), claim.getReceiverBankingInfo(),
                        claim.getCardNumber(), claim.getExamDate(), claim.getDocuments(),
                        claim.getClaimAmount() + "$", claim.getStatus());
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Provide sorting options
            System.out.println("\nSort claims by: ");
            System.out.printf("%-25s | %-25s | %-25s\n", "1. Date (Latest to Oldest)", "2. Date (Oldest to Latest)", "3. Cancel");
            System.out.println("Enter your choice: ");
            int sortChoice = scanner.nextInt();
            scanner.nextLine();

            if (sortChoice != 3) {
                // Sort claims
                List<Claim> sortedClaims = this.sortClaims(sortChoice);

                // Display sorted claims
                System.out.println("________________________________________________________________________________ADMIN - MANAGE CLAIMS - VIEW ALL SORTED CLAIMS____________________________________________________________________________________");
                this.displaySortedClaims(sortedClaims);
            }

            // Admin can view a claim by entering claim ID
            System.out.println("Enter a claim ID to view the details (enter 'cancel' to cancel): ");
            String selectedID = scanner.nextLine();

            if (!selectedID.equalsIgnoreCase("cancel")) {
                this.displayClaimDetails(selectedID);
            } else {
                System.out.println("Exiting...");
                this.manageClaims();
            }
        }
    }

    // Method to sort the claims
    private List<Claim> sortClaims(int sortChoice) {
        List<Claim> sortedClaims = new ArrayList<>(claimsController.getAllClaims());
        switch (sortChoice) {
            case 1 -> sortedClaims.sort(Comparator.comparing(Claim::getClaimDate).reversed());
            case 2 -> sortedClaims.sort(Comparator.comparing(Claim::getClaimDate));
            default -> System.err.println("Invalid choice. Sorting by default order.");
        }
        return sortedClaims;
    }

    private void displaySortedClaims(List<Claim> sortedClaims) {
        // Display header
        System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                "ID", "Date", "Insured Person", "Banking Info", "Card Number", "Exam Date", "Documents", "Claim Amount", "Status");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        // Display content
        for (Claim claim : sortedClaims) {
            System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                    claim.getClaimID(), claim.getClaimDate(), claim.getInsuredPerson(), claim.getReceiverBankingInfo(),
                    claim.getCardNumber(), claim.getExamDate(), claim.getDocuments(),
                    claim.getClaimAmount() + "$", claim.getStatus());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    // Display a claim's details
    public void displayClaimDetails(String claimID) {
        if (claimsController.claimExits(claimID)) {
            Claim claim = claimsController.getClaimByID(claimID);
            System.out.println("________________________________________________________________________________");
            System.out.println("Claim ID: " + claim.getClaimID());
            System.out.println("Date: " + claim.getClaimDate());
            System.out.println("Insured person: " + claim.getInsuredPerson());
            System.out.println("Card number: " + claim.getCardNumber());
            System.out.println("Exam date: " + claim.getClaimDate());
            System.out.println("Documents: " + claim.getDocuments());
            System.out.println("Claim amount: " + claim.getClaimAmount() + "$");
            System.out.println("Status: " + claim.getStatus());
            System.out.println("Receiver Banking Information: " + claim.getReceiverBankingInfo());
            System.out.println("________________________________________________________________________________");
        } else {
            System.err.println("The claim with the ID" + claimID + " does not exist.");
        }
    }

    public void modifyAClaim() {
        while (true) {
            System.out.println("________________________________________________________________________________ADMIN - MANAGE CLAIMS - MODIFY A CLAIM____________________________________________________________________________________");
            System.out.println("Enter the claim ID you want to modify (enter 'cancel' to cancel): ");
            String claimID = scanner.nextLine();

            if (claimID.equalsIgnoreCase("cancel")) {
                break;
            }

            Claim claimToEdit = claimsController.getClaimByID(claimID);
            if (claimToEdit == null) {
                System.out.println("Claim not found. Please try again.");
                return;
            }

            // Display current details of the claim
            System.out.println("Claim found: ");
            this.displayClaimDetails(claimID);

            // Allow admin to approve or process a claim
            System.out.println("Enter the new status of this claim (enter '0' to cancel): ");
            System.out.println("1. PROCESSING");
            System.out.println("2. DONE");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                claimToEdit.setStatus(Claim.Status.PROCESSING);
            } else if (choice == 2) {
                claimToEdit.setStatus(Claim.Status.DONE);
            } else if (choice == 0) {
                return;
            } else {
                System.err.println("Invalid input. Please try again.");
                return;
            }

            System.out.println("Do you want to save this change? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                claimsController.serializeClaimsToFile("data/claims.dat");
                claimsController.saveClaimsToTextFile("data/claims.txt");
                System.out.println("Claim has been updated successfully.");
            } else {
                System.out.println("Procedure has been canceled.");
                return;
            }
        }

    }

    public void manageInsuranceCards() {

    }

}
