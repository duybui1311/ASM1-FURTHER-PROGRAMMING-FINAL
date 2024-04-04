package InsuranceClaimManagementSystem.util;

import InsuranceClaimManagementSystem.Controller.*;
import InsuranceClaimManagementSystem.Model.*;
import java.io.File;
import java.util.*;


public class DataGenerator {

    public static void main(String[] args) {
        PolicyHoldersController policyHoldersController = PolicyHoldersController.getInstance();
        InsuranceCardController insuranceCardController = InsuranceCardController.getInstance();
        DependentsController dependentsController = DependentsController.getInstance();
        AdminController adminController = AdminController.getInstance();
        ClaimsController claimsController = ClaimsController.getInstance();

        // Generate sample data
        generatedata(policyHoldersController, insuranceCardController, dependentsController, adminController);

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

    private static void generatedata(PolicyHoldersController policyHoldersController, InsuranceCardController insuranceCardController, DependentsController dependentsController, AdminController adminController) {
        // Generate admins
        Admin[] admins = {
                new Admin("13112004", "Duy Bui", "123456"),
                new Admin("111111", "admin", "123456"),
                new Admin("111111", "son hoang", "123456")
        };
        for (Admin admin : admins) {
            adminController.addAdmin(admin);
        }

        // Generate policy holders and insurance cards
        List<PolicyHolder> policyHolders = new ArrayList<>();
        for (int i = 0; i < 17; i++) { // Generate 17 policy holders
            PolicyHolder policyHolder = new PolicyHolder("c-" + (1234567 + i), NameGenerator.generateFullName(), null);
            policyHolders.add(policyHolder);
            InsuranceCard insuranceCard = insuranceCardController.generateInsuranceCard(policyHolder, policyHolder, "RMIT");
            insuranceCardController.addInsuranceCard(insuranceCard);
            policyHolder.setInsuranceCard(insuranceCard);
            policyHoldersController.addPolicyHolder(policyHolder);
        }

        // Generate dependents
        for (PolicyHolder policyHolder : policyHolders) {
            for (int i = 0; i < 3; i++) { // Generate 3 dependents for each policy holder
                Dependent dependent = new Dependent("c-" + (10000003 + i), "Dependent " + (i + 1), policyHolder.getInsuranceCard(), policyHolder);
                dependentsController.addDependent(dependent);
                policyHolder.getDependents().add(dependent);
            }
            policyHoldersController.setDependents(policyHolder, policyHolder.getDependents());
        }

        // Generate claims
        Random random = new Random();
        ClaimsController claimsController = ClaimsController.getInstance();
        for (PolicyHolder policyHolder : policyHolders) {
            for (Dependent dependent : policyHolder.getDependents()) {
                for (int i = 0; i < 2; i++) { // Generate 2 claims for each policy holder and dependent
                    Customer customer = dependent != null ? dependent : policyHolder;
                    Claim claim = generateClaim(customer, random.nextInt(2) + 1);
                    claimsController.addClaim(claim);
                }
            }
            for (int i = 0; i < 2; i++) { // Generate 2 claims for each policy holder
                Claim claim = generateClaim(policyHolder, random.nextInt(2) + 1);
                claimsController.addClaim(claim);
            }
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

        String claimID = claimsController.generateClaimID();
        Date claimDate = generateDate(index);
        int cardNumber = customer.getInsuranceCard().getCardNumber();
        Date examDate = new Date();
        List<String> documents = generateDocuments();
        int claimAmount = (int) (Math.random() * 1000);
        Claim.Status status = Claim.Status.NEW;
        String receiverBankingInfo;

        // Generate banking information using the generateBank class
        generateBank bankGenerator = new generateBank();
        String bankName = bankGenerator.generateBankName();
        String claimantName = NameGenerator.generateFullName();
        String accountNumber = bankGenerator.generateBankNumber(generateBank.generateBankName(),claimantName);

        Claim claim = new Claim(claimID, claimDate, customer, cardNumber, examDate, documents, claimAmount, status, bankName, claimantName, accountNumber);

        // Set the banking information
        claim.setBankName(bankName);
        claim.setAccountHolderName(claimantName);
        claim.setAccountNumber(accountNumber);

        return claim;
    }






    private static Date generateDate(int index) {
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


    private record generateBank() {
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

        public static String generateBankName() {
            Random random = new Random();
            return BANK_NAME[random.nextInt(BANK_NAME.length)];
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


    public class NameGenerator {
        private static final Random random = new Random();

        private static final String[] firstNames = {"John", "Emma", "Michael", "David", "William", "Mai", "Nghia", "Son", "Hai", "Duy", "Tri", "Ha", "Phanh", "Hoe","Hai Minh","Thao"};
        private static final String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Sins", "Tran", "Miller", "Wilson", "Moore", "Taylor","Nguyen", "Tran","Bui"};

        public static String generateFullName() {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            return firstName + " " + lastName;
        }
    }

    public class DocumentGenerator {

        public static List<String> generateDocuments() {
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
    }

    public class BankGenerator {
        public static String generateAccountNumber() {

            StringBuilder accountNumber = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                accountNumber.append(random.nextInt(10));
            }
            return accountNumber.toString();
        }
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

        public static String generateBankName() {
            Random random = new Random();
            int index = random.nextInt(BANK_NAME.length);
            return BANK_NAME[index];
        }
    }


    public class  DateGenerator {

        public static Date generateDate(int index) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2023, Calendar.JANUARY, 1);

            Random random = new Random();
            int daysToAdd = random.nextInt(365) + index;
            calendar.add(Calendar.DAY_OF_YEAR, daysToAdd);
            return calendar.getTime();
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

    }

}
