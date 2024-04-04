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

        public void adminLogin() {
            while (true) {
                adminController.deserializeAdminsFromFile("Data/admins.dat");
                System.out.println("------------------------------");
                System.out.println("      Admin Login Page");
                System.out.println("------------------------------");
                System.out.println("Enter your credentials to log in:");
                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                Admin admin = adminController.authenticateAdmin(username, password);

                if (admin != null) {
                    System.out.println("\nLogin successful!");
                    System.out.println("------------------------------");

                    // Deserialize all data in the system
                    policyHoldersController.deserializePolicyHoldersFromFile("Data/policyholders.dat");
                    dependentsController.deserializeAllDependents("Data/dependents.dat");
                    insuranceCardController.deserializeInsuranceCardsFromFile("Data/insuranceCards.dat");
                    claimsController.deserializeAllClaimsFromFile("Data/claims.dat");

                    menu(); // Proceed to main menu
                    return; // Exit the method
                } else {
                    System.out.println("\nLogin failed. Please check your username and password.");
                    System.out.println("------------------------------");
                    System.out.println("Exiting admin login...\n");
                    System.out.println("-------------------------------------------------------------------------------");
                }
            }
        }

        public void menu() {
            // Display the main menu options
            System.out.println("------------------------------");
            System.out.println("          Main Menu           ");
            System.out.println("------------------------------");
            System.out.println("1. Manage Claims");
            System.out.println("2. Manage Customers");
            System.out.println("3. Manage Insurance Cards");
            System.out.println("4. Exit");
            System.out.print("Please enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    this.manageClaimsMenu();
                    break;
                case 2:
                    this.manageCustomersMenu(); // Assuming you meant to call `manageCustomersMenu()` here
                    break;
                case 3:
                    this.manageInsuranceCardsMenu();
                    break;
                case 4:
                    System.out.println("Shutting down the system...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
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
                    case 1 -> this.viewCustomers();
                    case 2 -> this.addDependentMenu();
                    case 3 -> this.addPolicyHolderMenu();
                    case 4 -> this.removeCustomerMenu();
                    case 5 -> this.modifyCustomerMenu();
                    case 6 -> {
                        this.menu();
                        return;
                    }
                    default -> System.out.println("Invalid input. Please try again.");
                }
            }
        }

        // Manage customers in the admin interface
        public void manageCustomersMenu() {
            while (true) {
                System.out.println("---------------------------------");
                System.out.println("       Customer Management       ");
                System.out.println("---------------------------------");
                System.out.println("1. View Customers");
                System.out.println("2. Add a New Dependent");
                System.out.println("3. Add a New Policy Holder");
                System.out.println("4. Remove a Customer");
                System.out.println("5. Modify a Customer");
                System.out.println("6. Back to Main Menu");
                System.out.print("Please enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        this.viewCustomers();
                        break;
                    case 2:
                        this.addDependentMenu();
                        break;
                    case 3:
                        this.addPolicyHolderMenu();
                        break;
                    case 4:
                        this.removeCustomerMenu();
                        break;
                    case 5:
                        this.modifyCustomerMenu();
                        break;
                    case 6:
                        return; // Return to the main menu
                    default:
                        System.out.println("Invalid input. Please try again.");
                }
            }
        }

        public void viewCustomers() {
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|                                      All Customers in the System                                                         |");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("| %-20s | %-40s | %-40s | %-10s |\n", "ID", "Full Name", "Insurance Card Number", "Role");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            List<Customer> customers = adminController.getAllCustomers();
            if (customers.isEmpty()) {
                System.out.println("|                                    No customers found in the system                                                    |");
                System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                return;
            }

            for (Customer customer : customers) {
                String id = customer.getCustomerID();
                String fullName = customer.getFullName();
                int insuranceCardNumber = customer.getInsuranceCard().getCardNumber();
                String role = (customer instanceof PolicyHolder) ? "Policy Holder" : "Dependent";
                System.out.printf("| %-20s | %-40s | %-40s | %-10s |\n", id, fullName, insuranceCardNumber, role);
            }
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        }

        public void viewDependents() {
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
            System.out.println("|                                         Manage Dependents                                                                  |");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            List<Dependent> dependents = adminController.getAllDependents();
            if (dependents.isEmpty()) {
                System.out.println("|                                    There are no dependents in the system                                                 |");
                System.out.println("----------------------------------------------------------------------------------------------------------------------------");
                this.viewCustomers(); // Redirect to view customers
                return;
            }

            System.out.printf("| %-20s | %-70s | %-70s |\n", "ID", "Full Name", "Insurance Card Number");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");

            dependents.forEach(dependent -> {
                String id = dependent.getCustomerID();
                String fullName = dependent.getFullName();
                int insuranceCardNumber = dependent.getInsuranceCard().getCardNumber();
                System.out.printf("| %-20s | %-70s | %-70s |\n", id, fullName, insuranceCardNumber);
            });
            System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        }

        public void viewPolicyHolders() {
            System.out.println("----------------------------------------------------------");
            System.out.println("|               Manage Policy Holders                    |");
            System.out.println("----------------------------------------------------------");

            List<PolicyHolder> policyHolders = adminController.getAllPolicyHolders();
            if (policyHolders.isEmpty()) {
                System.out.println("|              There are no policy holders!              |");
                System.out.println("----------------------------------------------------------");
                return;
            }

            System.out.printf("| At the moment there are %d policy holders in the system. |\n", policyHolders.size());
            System.out.printf("| %-20s | %-70s | %-70s |\n", "ID", "Full Name", "Insurance Card Number");
            System.out.println("----------------------------------------------------------");

            policyHolders.forEach(policyHolder -> {
                String id = policyHolder.getCustomerID();
                String fullName = policyHolder.getFullName();
                int insuranceCardNumber = policyHolder.getInsuranceCard().getCardNumber();
                System.out.printf("| %-20s | %-70s | %-70s |\n", id, fullName, insuranceCardNumber);
            });
            System.out.println("----------------------------------------------------------");
        }


        public void viewCustomerDetailsMenu() {
            System.out.println("--------------------------------------------------");
            System.out.println("|           View Customer Details                |");
            System.out.println("--------------------------------------------------");

            System.out.println("Please enter the ID of the customer:");
            String customerID = scanner.nextLine();

            this.displayCustomerDetailsMenu(customerID);
        }

        private void displayCustomerDetailsMenu(String customerID) {
            Customer customer = adminController.findCustomer(customerID);
            if (customer == null) {
                System.err.println("No customer found. Please try again.");
                return;
            }

            String role = (customer instanceof PolicyHolder) ? "Policy Holder" : "Dependent";

            // Display customer details with improved formatting
            System.out.println("--------------------------------------------------");
            System.out.println("|              Customer Details                  |");
            System.out.println("--------------------------------------------------");
            System.out.println("Customer ID: " + customer.getCustomerID());
            System.out.println("Full Name: " + customer.getFullName());
            System.out.println("Role: " + role);

            if (customer instanceof PolicyHolder) {
                System.out.println("Dependents:");
                for (Dependent dependent : ((PolicyHolder) customer).getDependents()) {
                    System.out.println(dependent.getFullName() + " (" + dependent.getCustomerID() + ")");
                }
            }

            System.out.println("Insurance Card Number: " + customer.getInsuranceCard().getCardNumber());
            System.out.println("--------------------------------------------------");
        }


        public void addPolicyHolderMenu() {
            System.out.println("---------------------------------");
            System.out.println("        Add a Policy Holder       ");
            System.out.println("---------------------------------");
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
            policyHoldersController.serializePolicyHoldersToFile("Data/policyholders.dat");

            System.out.println("New policy holder with ID " + newPolicyHolderID + " and insurance card number " + newPolicyHolderInsuranceCard.getCardNumber() + " has been created.");
            this.manageCustomersMenu(); // Return to manage customers menu
        }


        public void addDependentMenu() {
            System.out.println("---------------------------------");
            System.out.println("        Add a Dependent          ");
            System.out.println("---------------------------------");
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

            if (policyHolder.getDependents().isEmpty()) {
                System.out.println("No dependents added yet.");
            } else {
                for (Dependent dependent : policyHolder.getDependents()) {
                    System.out.println(dependent.getFullName() + " (" + dependent.getCustomerID() + ")");
                }
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
            dependentsController.serializeDependentsToFile("Data/dependents.dat");
            policyHoldersController.serializePolicyHoldersToFile("Data/policyholders.dat");

            System.out.println("New dependent with ID " + newDependentID + " has been added to the policy holder.");
        }

        public void removeCustomerMenu() {
            System.out.println("---------------------------------");
            System.out.println("       Remove a Customer         ");
            System.out.println("---------------------------------");
            System.out.println("Enter the ID of the customer you want to remove:");

            String customerID = scanner.nextLine();
            Customer customer = adminController.findCustomer(customerID);

            if (customer == null) {
                System.out.println("Customer with ID " + customerID + " not found.");
                return;
            }

            // Display the customer details
            System.out.println("Customer found: ");
            this.displayCustomerDetailsMenu(customerID);

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

        public void modifyCustomerMenu() {
            System.out.println("---------------------------------");
            System.out.println("        Modify a Customer        ");
            System.out.println("---------------------------------");
            System.out.println("Enter the ID of the customer you want to modify:");

            String customerID = scanner.nextLine();
            Customer customer = adminController.findCustomer(customerID);

            if (customer == null) {
                System.out.println("Customer with ID " + customerID + " not found.");
                return;
            }

            // Display the customer details
            System.out.println("Customer found: ");
            this.displayCustomerDetailsMenu(customerID);

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

        public void manageClaimsMenu() {
            System.out.println("---------------------------------");
            System.out.println("         Manage Claims           ");
            System.out.println("---------------------------------");

            while (true) {
                System.out.println("1. View All Claims");
                System.out.println("2. Add a Claim");
                System.out.println("3. Update a Claim");
                System.out.println("4. Remove a Claim");
                System.out.println("5. View a Claim");
                System.out.println("6. Cancel");
                System.out.print("Please enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        this.viewAllClaimsMenu();
                        break;
                    case 2:
                        this.addClaimMenu();
                        break;
                    case 3:
                        this.updateClaimMenu();
                        break;
                    case 4:
                        this.removeClaimMenu();
                        break;
                    case 5:
                        this.viewClaimMenu();
                        break;
                    case 6:
                        this.menu();
                        return;
                    default:
                        System.err.println("Invalid input. Please try again.");
                }
            }
        }

        public void addClaimMenu() {
            // Implement the functionality to add a claim
        }

        public void updateClaimMenu() {
            // Implement the functionality to update a claim
        }

        public void removeClaimMenu() {
            // Implement the functionality to remove a claim
        }

        public void viewClaimMenu() {
            // Implement the functionality to view a specific claim
        }


        public void viewAllClaimsMenu() {
            System.out.println("----------------------------------");
            System.out.println("         View All Claims           ");
            System.out.println("----------------------------------");
            List<Claim> claims = claimsController.getAllClaims();

            if (claims.isEmpty()) {
                System.out.println("There are no claims!");
                return;
            }

            System.out.println("All claims in the system:");
            displayClaims(claims);

            // Sort claims
            System.out.println("\nSort claims by:");
            System.out.println("1. Date (Latest to Oldest)");
            System.out.println("2. Date (Oldest to Latest)");
            System.out.println("3. Cancel");
            System.out.print("Enter your choice: ");
            int sortChoice = scanner.nextInt();
            scanner.nextLine();

            if (sortChoice == 1 || sortChoice == 2) {
                // Sort and display claims
                List<Claim> sortedClaims = sortClaims(sortChoice);
                System.out.println("\nSorted claims:");
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

            displayClaimsHeader();
            for (Claim claim : claims) {
                displayClaimInfo(claim);
            }
        }

        private void displayClaimsHeader() {
            System.out.printf("%-13s | %-12s | %-20s | %-30s | %-15s | %-12s | %-30s | %-15s | %-15s\n",
                    "ID", "Date", "Insured Person", "Banking Info", "Card Number", "Exam Date", "Documents", "Claim Amount", "Status");
        }

        private void displayClaimInfo(Claim claim) {
            System.out.printf("%-13s | %-12s | %-20s | %-30s | %-15s | %-12s | %-30s | %-15s | %-15s\n",
                    claim.getClaimID(), claim.getClaimDate(), claim.getInsuredPerson(), claim.getReceiverBankingInfo(),
                    claim.getCardNumber(), claim.getExamDate(), claim.getDocuments(),
                    claim.getClaimAmount() + "$", claim.getStatus());
        }

        public void displayClaimDetails(String claimID) {
            Claim claim = claimsController.getClaimByID(claimID);
            if (claim != null) {
                System.out.println("----------------------------------");
                System.out.println("         Claim Details            ");
                System.out.println("----------------------------------");
                displayClaimInfo(claim);
            } else {
                System.err.println("The claim with ID " + claimID + " does not exist.");
            }
        }

        public void modifyClaimStatusMenu() {
            System.out.println("----------------------------------");
            System.out.println("        Modify Claim Status        ");
            System.out.println("----------------------------------");

            System.out.print("Enter the ID of the claim you want to modify: ");
            String claimID = scanner.nextLine();
            Claim claimToEdit = claimsController.getClaimByID(claimID);

            if (claimToEdit == null) {
                System.out.println("Claim not found. Please try again.");
                return;
            }

            System.out.println("Current claim details:");
            displayClaimDetails(claimID);

            System.out.println("Select the new status of this claim:");
            System.out.println("1. PROCESSING");
            System.out.println("2. DONE");
            System.out.print("Enter your choice: ");
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

            System.out.print("Do you want to save this change? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                claimsController.serializeClaimsToFile("Data/claims.dat");
                claimsController.saveClaimsToTextFile("Data/claims.txt");
                System.out.println("Claim status has been updated successfully.");
            } else {
                System.out.println("Procedure has been canceled.");
            }
        }

        public void manageInsuranceCardsMenu() {
            System.out.println("----------------------------------");
            System.out.println("     Manage Insurance Cards        ");
            System.out.println("----------------------------------");

            // Display menu options
            System.out.println("1. View All Insurance Cards");
            System.out.println("2. Add New Insurance Card");
            System.out.println("3. Remove Insurance Card");
            System.out.println("4. Modify Insurance Card Details");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    viewAllInsuranceCards();
                    break;
                case 2:
                    addInsuranceCard();
                    break;
                case 3:
                    removeInsuranceCard();
                    break;
                case 4:
                    modifyInsuranceCardDetails();
                    break;
                case 5:
                    // Return to main menu or previous menu
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }

        // Function to view all insurance cards
        private void viewAllInsuranceCards() {
            // Retrieve all insurance cards from your system
            List<InsuranceCard> insuranceCards = insuranceCardController.getAllInsuranceCards();

            // Check if there are no insurance cards
            if (insuranceCards.isEmpty()) {
                System.out.println("There are no insurance cards in the system.");
                return;
            }

            // Display header
            System.out.println("----------------------------------");
            System.out.println("       All Insurance Cards        ");
            System.out.println("----------------------------------");
            System.out.printf("%-20s | %-30s | %-15s\n", "Card Number", "Customer Name", "Expiration Date");

            // Display each insurance card
            for (InsuranceCard insuranceCard : insuranceCards) {
                String cardNumber = String.valueOf(insuranceCard.getCardNumber());
                String customerName = insuranceCard.getCardHolder().getFullName();
                String expirationDate = insuranceCard.getExpirationDate().toString(); // Replace with actual method to get expiration date

                System.out.printf("%-20s | %-30s | %-15s\n", cardNumber, customerName, expirationDate);
            }
        }


        // Function to add a new insurance card
        private void addInsuranceCard() {
            System.out.println("Adding a New Insurance Card");

            // Prompt the user to enter insurance card details
            System.out.println("Enter the card number:");
            int cardNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // You may prompt for additional details such as policy holder, card holder, expiration date, etc.

            // Create a new InsuranceCard object with the provided details
            InsuranceCard newInsuranceCard = new InsuranceCard(cardNumber, null, null, null, null);

            // Call the corresponding function in the controller to add the new insurance card
            insuranceCardController.addInsuranceCard(newInsuranceCard);

            System.out.println("New insurance card added successfully.");
        }

        // Function to remove an insurance card
        private void removeInsuranceCard() {
            System.out.println("Removing an Insurance Card");

            // Prompt the user to enter the card number of the insurance card to be removed
            System.out.println("Enter the card number of the insurance card to remove:");
            int cardNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Call the corresponding function in the controller to remove the insurance card
            insuranceCardController.removeInsuranceCard(cardNumber);

            System.out.println("Insurance card with card number " + cardNumber + " has been removed.");
        }

        // Function to modify insurance card details
        private void modifyInsuranceCardDetails() {
            System.out.println("Modifying Insurance Card Details");

            // Prompt the user to enter the card number of the insurance card to modify
            System.out.println("Enter the card number of the insurance card to modify:");
            int cardNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            // Call the corresponding function in the controller to retrieve the insurance card
            InsuranceCard insuranceCard = insuranceCardController.getInsuranceCardByNumber(cardNumber);

            if (insuranceCard != null) {
                // Prompt the user to enter the new details for the insurance card
                System.out.println("Enter the new details for the insurance card:");

                // You may prompt for additional details such as policy holder, card holder, expiration date, etc.

                // Update the insurance card with the new details
                // insuranceCard.setXXX(newXXX);

                // Call the corresponding function in the controller to update the insurance card
                insuranceCardController.updateInsuranceCard(insuranceCard);

                System.out.println("Insurance card details have been modified successfully.");
            } else {
                System.out.println("Insurance card with card number " + cardNumber + " not found.");
            }
        }

    }
