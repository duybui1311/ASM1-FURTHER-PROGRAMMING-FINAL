package InsuranceClaimManagementSystem.util;

import InsuranceClaimManagementSystem.Controller.*;
import InsuranceClaimManagementSystem.Model.*;
import InsuranceClaimManagementSystem.View.AdminView;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class DataGenerator {

    public static void main(String[] args) {
        PolicyHoldersController policyHoldersController = PolicyHoldersController.getInstance();
        InsuranceCardController insuranceCardController = InsuranceCardController.getInstance();
        DependentsController dependentsController = DependentsController.getInstance();
        AdminController adminController = AdminController.getInstance();
        ClaimsController claimsController = ClaimsController.getInstance();

        // Generate sample data
        generateSampleData(policyHoldersController, insuranceCardController, dependentsController, adminController);

        // Generate claims for existing customers
        List<PolicyHolder> policyHolders = new ArrayList<>(policyHoldersController.getAllPolicyHolders());
        for (PolicyHolder policyHolder : policyHolders) {
            for (int i = 0; i < 2; i++) {
                Claim claim = generateClaim(policyHolder, 2);
                claimsController.addClaim(claim);
            }
        }

        List<Dependent> dependents = new ArrayList<>(dependentsController.getAllDependents());
        for (Dependent dependent : dependents) {
            for (int i = 0; i < 2; i++) {
                Claim claim = generateClaim(dependent, 2);
                claimsController.addClaim(claim);
            }
        }

        // Serialize the generated data
        serializeData(adminController, policyHoldersController, insuranceCardController, dependentsController, claimsController);

        // Deserialize and print the data
        deserializeAndPrintData(adminController, policyHoldersController, dependentsController, insuranceCardController, claimsController);
    }

    private static void generateSampleData(PolicyHoldersController policyHoldersController, InsuranceCardController insuranceCardController, DependentsController dependentsController, AdminController adminController) {
        // Generate sample admins
        Admin admin1 = new Admin("13112004", "Duy Bui", "123456");
        Admin admin2 = new Admin("111111", "admin", "123456");

        // Add admins to the controller
        adminController.addAdmin(admin1);
        adminController.addAdmin(admin2);

        // Generate sample PolicyHolders
        PolicyHolder policyHolder1 = new PolicyHolder("c-1234567", "Tran Hoang Son", null); // Constant
        PolicyHolder policyHolder2 = new PolicyHolder("c-1234568", "Lebron James", null);

        // Generate sample Insurance Cards for PolicyHolders
        InsuranceCard insuranceCard1 = insuranceCardController.generateInsuranceCard(policyHolder1, policyHolder1, "RMIT");
        InsuranceCard insuranceCard2 = insuranceCardController.generateInsuranceCard(policyHolder2, policyHolder2, "RMIT");
        insuranceCardController.addInsuranceCard(insuranceCard1);
        insuranceCardController.addInsuranceCard(insuranceCard2);

        // Set insurance cards for PolicyHolders
        policyHolder1.setInsuranceCard(insuranceCard1);
        policyHolder2.setInsuranceCard(insuranceCard2);

        // Add policyholders to the controller
        policyHoldersController.addPolicyHolder(policyHolder1);
        policyHoldersController.addPolicyHolder(policyHolder2);

        // Generate sample Dependents
        Dependent dependent1 = new Dependent("c-0000003", "Dependent 1", insuranceCard1, policyHolder1);
        Dependent dependent2 = new Dependent("c-0000004", "Dependent 2", insuranceCard1, policyHolder1);
        Dependent dependent3 = new Dependent("c-0000005", "Dependent 3", insuranceCard2, policyHolder2);

        // Add dependents to the controller
        dependentsController.addDependent(dependent1);
        dependentsController.addDependent(dependent2);
        dependentsController.addDependent(dependent3);

        // Assign the dependents for the policy holders
        List<Dependent> dependents1 = new ArrayList<>();
        dependents1.add(dependent1);
        policyHolder1.setDependents(dependents1);

        List<Dependent> dependents2 = new ArrayList<>();
        dependents2.add(dependent2);
        dependents2.add(dependent3);
        policyHolder2.setDependents(dependents2);
        policyHoldersController.setDependents(policyHolder2, dependents2);

        // Generate and add policy holders to the controller
        List<PolicyHolder> policyHolders = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PolicyHolder policyHolder = generatePolicyHolder();
            policyHolders.add(policyHolder);
            policyHoldersController.addPolicyHolder(policyHolder);
        }

        // Generate insurance cards for policy holders
        for (PolicyHolder policyHolder : policyHolders) {
            InsuranceCard insuranceCard = insuranceCardController.generateInsuranceCard(policyHolder, policyHolder, "RMIT");
            insuranceCardController.addInsuranceCard(insuranceCard);
            policyHolder.setInsuranceCard(insuranceCard);
        }

        // Generate dependents
        List<Dependent> dependents = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            PolicyHolder policyHolder = policyHolders.get(i / 5);
            Dependent dependent = generateDependent(policyHolder);
            dependents.add(dependent);
            dependentsController.addDependent(dependent);
            dependent.setInsuranceCard(policyHolder.getInsuranceCard());
        }

        // Assign dependents to policy holders
        for (int i = 0; i < 15; i += 5) {
            PolicyHolder policyHolder = policyHolders.get(i / 5);
            List<Dependent> policyHolderDependents = new ArrayList<>(dependents.subList(i, i + 5));
            policyHolder.setDependents(policyHolderDependents);
            policyHoldersController.setDependents(policyHolder, policyHolderDependents);
        }

        // Generate and add claims
        int totalClaims = 0;
        Random random = new Random();
        ClaimsController claimsController = ClaimsController.getInstance();
        while (totalClaims < 20) {
            int index = random.nextInt(policyHolders.size());
            PolicyHolder policyHolder = policyHolders.get(index);
            Claim claim = generateClaim(policyHolder, 1);
            claimsController.addClaim(claim);
            totalClaims++;
        }
    }

    private static PolicyHolder generatePolicyHolder() {
        CustomersController customersController = CustomersController.getInstance();
        String fullName = NameGenerator.generateFullName();
        return new PolicyHolder(customersController.generateUserID(), fullName, null);
    }

    private static Dependent generateDependent(PolicyHolder policyHolder) {
        CustomersController customersController = CustomersController.getInstance();
        String fullName = NameGenerator.generateFullName();
        return new Dependent(customersController.generateUserID(), fullName, null, policyHolder);
    }

    private static Claim generateClaim(Customer customer, int index) {
        ClaimsController claimsController = ClaimsController.getInstance();
        // Generate claim details
        String claimID = claimsController.generateClaimID();
        Date claimDate = generateClaimDate(index);
        int cardNumber = customer.getInsuranceCard().getCardNumber();
        Date examDate = new Date();
        List<String> documents = generateDocuments();
        int claimAmount = (int) (Math.random() * 1000);
        Claim.Status status = Claim.Status.NEW;
        String receiverBankingInfo;

        Claim claim = new Claim(claimID, claimDate, customer, cardNumber, examDate, documents, claimAmount, status, null);

        // Generate banking info
        BankingInfoGenerator bankingInfoGenerator = new BankingInfoGenerator(claim);
        String bankName = BankingInfoGenerator.BANK_NAME[new Random().nextInt(BankingInfoGenerator.BANK_NAME.length)];
        String claimantName = bankingInfoGenerator.getClaimantName();
        receiverBankingInfo = bankingInfoGenerator.generateBankNumber(bankName, claimantName);

        // Update the claim with banking info
        claim.setReceiverBankingInfo(receiverBankingInfo);
        return claim;
    }

    private static Date generateClaimDate(int index) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JANUARY, 1);

        Random random = new Random();
        int daysToAdd = random.nextInt(365) + index;
        calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
        return calendar.getTime();
    }

    private static List<String> generateDocuments() {
        List<String> documents = new ArrayList<>();
        String[] documentTypes = {
                "Insurance Card",
                "Doctor's Prescription",
                "Police Report",
                "Proof of Loss",
                "Medical Records",
                "Witness Statements",
                "Claim Form",
                "Hospital Bills",
                "Photographs",
                "Treatment Plan",
                "Diagnostic Reports",
                "Receipts",
                "Witness Testimonies",
                "Expert Opinions",
        };
        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            int index = random.nextInt(documentTypes.length);
            documents.add(documentTypes[index]);
        }
        return documents;
    }

    private record BankingInfoGenerator(Claim claim) {
        private static final String[] BANK_NAME = {
                "VP Bank",
                "Vietcombank",
                "Techcombank",
                "MB Bank",
                "BIDV",
                "SHB",
                "ACB",
                "VietinBank",
                "Agribank",
                "Sacombank",
                "TPBank",
                "Viet Capital Bank",
                "SeABank",
                "VietBank",
        };

        public String getClaimantName() {
            return claim.getInsuredPerson().getFullName();
        }

        public String generateBankNumber(String bankName, String claimantName) {
            Random random = new Random();

            // Remove spaces from claimant's name
            claimantName = claimantName.replaceAll("\\s", "");

            // Generate a random 12-digit bank number
            StringBuilder bankNumber = new StringBuilder(bankName);
            bankNumber.append("-").append(claimantName).append("-");

            int randomNumber;
            for (int i = 0; i < 12; i++) {
                randomNumber = random.nextInt(10);
                bankNumber.append(randomNumber); // Append a random digit (0-9)
            }

            return bankNumber.toString();
        }
    }

    public static class NameGenerator {
        private static final String[] FIRST_NAMES = {"Hai", "Nghia", "Mai", "Dat", "Josh", "David", "Son", "Duy", "Thao", "Ha"};
        private static final String[] LAST_NAMES = {"Tran", "Pham", "Nguyen", "Bui", "Luu", "Do", "Sins", "James", "Bryant"};
        private static final Random random = new Random();

        private static String generateFullName() {
            String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
            String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
            return lastName + " " + firstName;
        }
    }

    private static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                System.err.println("Failed to create directory: " + directoryPath);
            }
        }
    }

    private static void serializeData(AdminController adminController, PolicyHoldersController policyHoldersController, InsuranceCardController insuranceCardController, DependentsController dependentsController, ClaimsController claimsController) {
        createDirectoryIfNotExists("Data");
        adminController.serializeAdminToFile("Data/admins.dat");
        policyHoldersController.serializePolicyHoldersToFile("Data/policyholders.dat");
        dependentsController.serializeDependentsToFile("Data/dependents.dat");
        insuranceCardController.serializeInsuranceCardsToFile("Data/insuranceCards.dat");
        claimsController.serializeClaimsToFile("Data/claims.dat");
    }

    private static void deserializeAndPrintData(AdminController adminController, PolicyHoldersController policyHoldersController, DependentsController dependentsController, InsuranceCardController insuranceCardController, ClaimsController claimsController) {
        policyHoldersController.deserializePolicyHoldersFromFile("Data/policyholders.dat");
        dependentsController.deserializeAllDependents("Data/dependents.dat");
        insuranceCardController.deserializeInsuranceCardsFromFile("Data/insuranceCards.dat");
        adminController.deserializeAdminsFromFile("Data/admins.dat");
        claimsController.deserializeAllClaimsFromFile("Data/claims.dat");
        claimsController.saveClaimsToTextFile("Data/claims.txt");

        System.out.println("All admins:");
        for (Admin admin : adminController.getAdminList()) {
            System.out.println("Username: " + admin.getUsername());
            System.out.println("Password: " + admin.getPassword());
        }

        System.out.println("All policy holders:");
        for (PolicyHolder policyHolder : policyHoldersController.getAllPolicyHolders()) {
            System.out.println(policyHolder);
            System.out.println("Dependents:");
            List<Dependent> dependents = policyHoldersController.getAllDependents(policyHolder);
            for (Dependent dependent : dependents) {
                System.out.println(dependent);
            }
            System.out.println();
        }

        System.out.println("All dependents: ");
        for (Dependent dependent : dependentsController.getAllDependents()) {
            System.out.println(dependent);
        }

        System.out.println("All insurance cards:");
        for (InsuranceCard insuranceCard : insuranceCardController.getInsuranceCards()) {
            System.out.println(insuranceCard.toString());
            System.out.println("\n");
        }

        AdminView adminView = new AdminView();
        adminView.viewClaims();
    }

}