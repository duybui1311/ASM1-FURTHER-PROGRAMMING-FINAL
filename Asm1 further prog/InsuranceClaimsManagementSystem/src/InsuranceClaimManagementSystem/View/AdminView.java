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
        while (true) {
            adminController.deserializeAdminsFromFile("data/admins.dat");
            System.out.println("This is the login page for admins");
            System.out.println("Enter your username:");
            String username = scanner.nextLine();

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            Admin admin = adminController.authenticateAdmin(username, password);

            if (admin != null) {
                System.out.println("Login successful!");

                // Deserialize all data in the system
                policyHoldersController.deserializePolicyHoldersFromFile("data/policyholders.dat");
                dependentsController.deserializeAllDependents("data/dependents.dat");
                insuranceCardController.deserializeInsuranceCardsFromFile("data/insuranceCards.dat");
                claimsController.deserializeAllClaimsFromFile("data/claims.dat");

                menu(); // Proceed to main menu
                return; // Exit the method
            } else {
                System.out.println("Login failed. Please check your username and password.");
                System.out.println("Exiting admin login...");
                break;
            }
        }
    }


    // Menu for admin view
    public void menu() {
        while (true) {
            System.out.println("This is the admin page!");
            System.out.println("1. Manage Claims");
            System.out.println("2. Manage Customters");
            System.out.println("3. Manage Insurance Cards");
            System.out.println("4. Exit");
            System.out.println("Please enter your choice of functions: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.manageClaims();
                case 2 -> this.manageCustomers();
                case 3 -> this.manageInsuranceCards();
                case 4 -> {
                    System.exit(0);
                    System.out.println("Shutting down the system...");
                }
                default -> System.out.println("Invalid input. Please try again.");
            }
        }
    }

    // customer manager for admins
    public void manageCustomers() {
        while (true) {
            System.out.println("This is the customer managment page ");
            System.out.println("1. View customers");
            System.out.println("2. Add a new dependent");
            System.out.println("3. Add a new policy holder");
            System.out.println("4. Remove a customer");
            System.out.println("5. Modify a customer");
            System.out.println("6. Exit");
            System.out.println("Please enter your choice of functions:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewCustomer();
                case 2 -> this.addDependent();
                case 3 -> this.addPolicyHolder();
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

    // to view customer, dependent and policy holders
    public void viewCustomer() {
        while (true) {
            System.out.println("This is where the admin can view all types of customers");
            System.out.println("1. View all customers");
            System.out.println("2. View all dependents");
            System.out.println("3. View all policyholder");
            System.out.println("4. View a customer's details");
            System.out.println("5. Cancel");
            System.out.println("Please enter your choice of functions:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewAllCustomers();
                case 2 -> this.viewAllDependents();
                case 3 -> this.viewAllPolicyHolders();
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
        System.out.println("View All Customers");
        List<Customer> customers = adminController.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("There are no customers!");
            return;
        }

        System.out.printf("At the moment there are %d users in the system.\n", customers.size());
        System.out.printf("%-20s | %-40s | %-40s | %-40s\n", "ID", "Full name", "Insurance Card Number", "Role");

        customers.forEach(customer -> {
            String id = customer.getCustomerID();
            String fullName = customer.getFullName();
            int insuranceCardNumber = customer.getInsuranceCard().getCardNumber();
            String role = (customer instanceof PolicyHolder) ? "Policy Holder" : "Dependent";
            System.out.printf("%-20s | %-40s | %-40s | %-40s\n", id, fullName, insuranceCardNumber, role);
        });
    }
    public void viewAllDependents() {
        System.out.println("Manage Dependents");
        List<Dependent> dependents = adminController.getAllDependents();
        if (dependents.isEmpty()) {
            System.out.println("There are no dependents!");
            this.viewCustomer();
            return;
        }

        System.out.println("At the moment there are" + dependents.size() + " dependents in the system.");
        // Display header
        System.out.printf("%-20s | %-70s | %-70s\n", "ID", "Full name", "Insurance Card Number");

        // Display content using forEach loop
        dependents.forEach(dependent -> {
            String id = dependent.getCustomerID();
            String fullName = dependent.getFullName();
            int insuranceCardNumber = dependent.getInsuranceCard().getCardNumber();
            System.out.printf("%-20s | %-70s | %-70s\n", id, fullName, insuranceCardNumber);
        });
    }
    public void viewAllPolicyHolders() {
        System.out.println("Manage Policy Holders");
        List<PolicyHolder> policyHolders = adminController.getAllPolicyHolders();
        if (policyHolders.isEmpty()) {
            System.out.println("There are no policy holders!");
            return;
        }

        System.out.printf("At the moment there are %d policy holders in the system.\n", policyHolders.size());
        System.out.printf("%-20s | %-70s | %-70s\n", "ID", "Full name", "Insurance Card Number");

        policyHolders.forEach(policyHolder -> {
            String id = policyHolder.getCustomerID();
            String fullName = policyHolder.getFullName();
            int insuranceCardNumber = policyHolder.getInsuranceCard().getCardNumber();
            System.out.printf("%-20s | %-70s | %-70s\n", id, fullName, insuranceCardNumber);
        });
    }


    public void viewACustomerDetails() {
        System.out.println("View Customer Detail");
        System.out.println("Enter customer ID:");
        String customerID = scanner.nextLine();



        this.displayCustomerDetails(customerID);
    }

    private void displayCustomerDetails(String customerID) {
        Customer customer = adminController.findCustomer(customerID);
        if (customer == null) {
            System.err.println("No customer found. Please try again.");
            return;
        }

        String role = (customer instanceof PolicyHolder) ? "Policy Holder" : "Dependent";
        StringBuilder sb = new StringBuilder();
        sb.append("Customer found:\n");
        sb.append("-----------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("Customer ID: ").append(customer.getCustomerID()).append("\n");
        sb.append("Customer full name: ").append(customer.getFullName()).append("\n");
        sb.append("Role: ").append(role).append("\n");

        if (customer instanceof PolicyHolder) {
            sb.append("Dependents:\n");
            for (Dependent dependent : ((PolicyHolder) customer).getDependents()) {
                sb.append(dependent.getFullName()).append(" (").append(dependent.getCustomerID()).append(")\n");
            }
        }

        sb.append("Insurance card: ").append(customer.getInsuranceCard().getCardNumber()).append("\n");
        sb.append("-----------------------------------------------------------------------------------------------------------------------------------------------\n");

        System.out.println(sb.toString());
    }





    public void addPolicyHolder() {
        System.out.println("Add a Policy Holder");
        System.out.println("Enter the policy holder's name:");
        String newPolicyHolderName = scanner.nextLine();

        // Generate a new ID for the policy holder
        String newPolicyHolderID = customersController.generateUserID();
        PolicyHolder newPolicyHolder = new PolicyHolder(newPolicyHolderID, newPolicyHolderName, null);

        // Generate a new insurance card for the policy holder
        String newPolicyOwner = "RMIT"; // Assuming a default policy owner
        InsuranceCard newPolicyHolderInsuranceCard = insuranceCardController.generateInsuranceCard(newPolicyHolder, newPolicyHolder, newPolicyOwner);
        newPolicyHolder.setInsuranceCard(newPolicyHolderInsuranceCard);

        // Add the new policy holder to the system
        policyHoldersController.addPolicyHolder(newPolicyHolder);

        // Serialize the updated list of policy holders to file
        policyHoldersController.serializePolicyHoldersToFile("data/policyholders.dat");

        System.out.println("New policy holder with ID " + newPolicyHolderID + " and insurance card number " + newPolicyHolderInsuranceCard.getCardNumber() + " has been created.");
        this.manageCustomers(); // Return to manage customers menu
    }

    public void addDependent() {
        System.out.println("Add a dependent");
        System.out.println("Enter the policy holder ID to add a dependent:");
        String policyHolderID = scanner.nextLine();
        PolicyHolder policyHolder = policyHoldersController.findPolicyHolderByID(policyHolderID);

        if (policyHolder == null) {
            System.out.println("Policy holder with ID " + policyHolderID + " not found.");
            return;
        }

        System.out.println("Policy Holder found: ");
        System.out.println("Policy Holder ID: " + policyHolder.getCustomerID());
        System.out.println("Policy Holder Name: " + policyHolder.getFullName());
        System.out.println("Insurance Card Number: " + policyHolder.getInsuranceCard().getCardNumber());
        System.out.println("Current Dependents: ");
        for (Dependent dependent : policyHolder.getDependents()) {
            System.out.println(dependent.getFullName() + " (" + dependent.getCustomerID() + ")");
        }

        // Proceed with adding a dependent
        System.out.println("Enter the dependent's name:");
        String dependentName = scanner.nextLine();

        // Generate a new ID for the dependent
        String newDependentID = customersController.generateUserID();
        Dependent newDependent = new Dependent(newDependentID, dependentName, policyHolder.getInsuranceCard(), policyHolder);

        // Add the new dependent to the system
        dependentsController.addDependent(newDependent);

        // Update the policy holder's list of dependents
        List<Dependent> updatedDependents = policyHolder.getDependents();
        updatedDependents.add(newDependent);
        policyHolder.setDependents(updatedDependents);

        // Serialize the updated list of dependents and policy holder to file
        dependentsController.serializeDependentsToFile("data/dependents.dat");
        policyHoldersController.serializePolicyHoldersToFile("data/policyholders.dat");

        System.out.println("New dependent with ID " + newDependentID + " has been added to the policy holder.");
    }

    public void removeCustomer() {
        System.out.println("Remove a customer");
        System.out.println("Enter the ID of the customer you want to remove:");

        String customerID = scanner.nextLine();
        Customer customer = adminController.findCustomer(customerID);

        if (customer == null) {
            System.out.println("Customer with ID " + customerID + " not found.");
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
        } else {
            System.out.println("Procedure canceled.");
        }
    }


    public void modifyCustomer() {
        System.out.println("Modify a customer");
        System.out.println("Enter the ID of the customer you want to modify:");

        String customerID = scanner.nextLine();
        Customer customer = adminController.findCustomer(customerID);

        if (customer == null) {
            System.out.println("Customer with ID " + customerID + " not found.");
            return;
        }

        // Display the customer details
        System.out.println("Customer found: ");
        this.displayCustomerDetails(customerID);

        // Prompt user for modification
        System.out.println("Enter the new full name for the customer (press Enter to keep current):");
        String newName = scanner.nextLine();

        // Update the customer's full name if the user provided a new name
        if (!newName.isEmpty()) {
            customer.setFullName(newName);
        }

        // Prompt user confirmation for the modification
        System.out.println("Are you sure you want to modify this customer? (yes/no):");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            // Save the modified customer details
            // For example, you might call a method in your controller to update the customer
            // adminController.updateCustomer(customer);
            System.out.println("Customer details have been modified successfully.");
        } else {
            System.out.println("Modification canceled.");
        }
    }


    public void manageClaims() {
        System.out.println("Manage Claims");
        while (true) {
            System.out.println("1. View Claims");
            System.out.println("2. Modify A Claim");
            System.out.println("3. Cancel");
            System.out.println("Please enter your choice of functions:");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> this.viewClaims();
                case 2 -> this.modifyClaim();
                case 3 -> {
                    this.menu();
                    return;
                }
                default -> System.err.println("Invalid input. Please try again.");
            }
        }
    }

    public void viewClaims() {
        System.out.println("View All Claims");
        List<Claim> claims = claimsController.getAllClaims();

        if (claims.isEmpty()) {
            System.out.println("There are no claims!");
            return;
        }

        System.out.println("All claims in the system:");
        displayClaims(claims);

        // sort claims
        System.out.println("\nSort claims by: ");
        System.out.println("1. Date (Latest to Oldest)");
        System.out.println("2. Date (Oldest to Latest)");
        System.out.println("3. Cancel");
        System.out.println("Enter your choice: ");
        int sortChoice = scanner.nextInt();
        scanner.nextLine();

        if (sortChoice == 1 || sortChoice == 2) {
            // Sort and display claims
            List<Claim> sortedClaims = sortClaims(sortChoice);
            System.out.println("Sorted claims:");
            displayClaims(sortedClaims);
        } else if (sortChoice == 3) {
            System.out.println("Exiting...");
        } else {
            System.out.println("Invalid choice. Exiting...");
        }
    }

    private List<Claim> sortClaims(int sortChoice) {
        List<Claim> sortedClaims = new ArrayList<>(claimsController.getAllClaims());
        Comparator<Claim> comparator = (sortChoice == 1) ?
                Comparator.comparing(Claim::getClaimDate).reversed() :
                Comparator.comparing(Claim::getClaimDate);
        sortedClaims.sort(comparator);
        return sortedClaims;
    }

    public void displayClaims(List<Claim> claims) {
        if (claims.isEmpty()) {
            System.out.println("There are no claims!");
            return;
        }

        System.out.println("All claims in the system:");
        displayClaimsHeader();
        for (Claim claim : claims) {
            displayClaimInfo(claim);
        }
    }

    private void displayClaimsHeader() {
        System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                "ID", "Date", "Insured Person", "Banking Info", "Card Number", "Exam Date", "Documents", "Claim Amount", "Status");
    }

    private void displayClaimInfo(Claim claim) {
        System.out.printf("%-13s | %-30s | %-30s | %-40s | %-15s | %-35s | %-50s | %-15s | %-15s\n",
                claim.getClaimID(), claim.getClaimDate(), claim.getInsuredPerson(), claim.getReceiverBankingInfo(),
                claim.getCardNumber(), claim.getExamDate(), claim.getDocuments(),
                claim.getClaimAmount() + "$", claim.getStatus());
    }

    public void displayClaimDetails(String claimID) {
        Claim claim = claimsController.getClaimByID(claimID);
        if (claim != null) {
            System.out.println("Claim details:");
            displayClaimInfo(claim);
        } else {
            System.err.println("The claim with ID " + claimID + " does not exist.");
        }
    }

    public void modifyClaim() {
        System.out.println("Enter the claim ID you want to modify : ");
        String claimID = scanner.nextLine();
        Claim claimToEdit = claimsController.getClaimByID(claimID);
        if (claimToEdit == null) {
            System.out.println("Claim not found. Please try again.");
            return;
        }

        System.out.println("Current claim details:");
        displayClaimDetails(claimID);

        System.out.println("Enter the new status of this claim:");
        System.out.println("1. PROCESSING");
        System.out.println("2. DONE");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                claimToEdit.setStatus(Claim.Status.PROCESSING);
                break;
            case 2:
                claimToEdit.setStatus(Claim.Status.DONE);
                break;
            default:
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
        }
    }

    public void manageInsuranceCards() {

    }

}
