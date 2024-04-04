package InsuranceClaimManagementSystem.Controller;

import InsuranceClaimManagementSystem.Model.*;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InsuranceCardController implements Serializable {
    private static InsuranceCardController instance;
    private static final Logger logger = Logger.getLogger(InsuranceCardController.class.getName());
    public ArrayList<InsuranceCard> insuranceCards;

    public InsuranceCardController() {
        insuranceCards = new ArrayList<>();
    }

    public static InsuranceCardController getInstance() {
        if (instance == null) {
            instance = new InsuranceCardController();
        }
        return instance;
    }

    public List<InsuranceCard> getInsuranceCards() {
        return insuranceCards;
    }

    // Method to create a new insurance card
    public InsuranceCard generateInsuranceCard(Customer cardHolder, PolicyHolder policyHolder, String policyOwner) {
        int cardNumber = generateCardNumber();
        Date expirationDate = getDefaultExpirationDate();

        // Create insurance card with provided customer and policy owner information
        return new InsuranceCard(cardNumber, policyHolder, cardHolder, policyOwner, expirationDate);
    }


    // Method to generate a random card number
    private int generateCardNumber() {
        long randomNumber = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
        return (int) (randomNumber % Integer.MAX_VALUE);
    }

    // Method to generate default expiration date
    private Date getDefaultExpirationDate() {
        // Considering the expiration date for an insurance card is one year from the date it is issued
        long oneYearInMillis = 365L * 24 * 60 * 60 * 1000; // Converting a year into milliseconds
        return new Date(System.currentTimeMillis() + oneYearInMillis);
    }

    public void serializeInsuranceCardsToFile(String filePath) {
        createFileIfNotExists("data/insuranceCards.dat");
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ){
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Create parent directory
            objectOutputStream.writeObject(insuranceCards);
            System.out.println("Insurance cards have been saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error: Unable to save insurance cards to " + filePath);
        }
    }

    public void deserializeInsuranceCardsFromFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Object importedObject = objectInputStream.readObject();

            if (importedObject instanceof ArrayList<?> importedData && !((ArrayList<?>) importedObject).isEmpty()) {
                if (importedData.get(0) instanceof InsuranceCard) {
                    insuranceCards = (ArrayList<InsuranceCard>) importedData;
                    System.out.println("Insurance cards have been deserialized and imported from " + filePath);
                    return;
                }
            }
            logger.log(Level.SEVERE, "Unexpected data format in the insurance cards file.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception while reading insurance cards file", e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Class not found during deserialization.");
        }
    }

    public void createFileIfNotExists(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + filePath);
                } else {
                    System.err.println("Error: Unable to create file " + filePath);
                }
            } catch (IOException e) {
                System.err.println("Error: Unable to create file " + filePath);
            }
        }
    }

    public void addInsuranceCard(InsuranceCard insuranceCard) {
        if (insuranceCard != null) {
            insuranceCards.add(insuranceCard);
        } else {
            System.err.println("Error: Cannot add a null insurance card");
        }
    }
}
