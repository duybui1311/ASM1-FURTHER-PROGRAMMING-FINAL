package InsuranceClaimManagementSystem.Controller;

import InsuranceClaimManagementSystem.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DependentsController implements Serializable {
    private static DependentsController instance;
    private Dependent currentDependent;
    private static final Logger logger = Logger.getLogger(DependentsController.class.getName());
    public ArrayList<Dependent> dependents;
    public DependentsController() {
        dependents = new ArrayList<>();
    }

    public static DependentsController getInstance() {
        if (instance == null) {
            instance = new DependentsController();
        }
        return instance;
    }

    // Method to authenticate a dependent's login
    public Dependent authenticateDependent(String userID, String fullName) {
        Dependent dependent = findDependent(userID, fullName);
        if (dependent != null) {
            currentDependent = dependent;
        }
        return dependent;
    }

    // Method to get the current dependent
    public Dependent getCurrentDependent() {
        return currentDependent;
    }

    // Method to set the current dependent
    public void setCurrentDependent(Dependent currentDependent) {
        this.currentDependent = currentDependent;
    }

    // Find a dependent by name and ID for login purpose
    public Dependent findDependent(String ID, String name) {
        for (Dependent dependent : dependents) {
            if (dependent.getCustomerID().equals(ID) && dependent.getFullName().equals(name)) {
                return dependent;
            }
        }
        return null;
    }

    // Method to get all dependents in the system
    public List<Dependent> getAllDependents() {
        return dependents;
    }

    // Method to get a dependent's insurance card
    public InsuranceCard getInsuranceCard(String dependentID, String fullName) {
        Dependent dependent = findDependent(dependentID, fullName);
        return dependent != null ? dependent.getInsuranceCard() : null;
    }

    public PolicyHolder getPolicyOwner(Dependent dependent) {
        return dependent.getPolicyHolder();
    }

    // Method to add a dependent into dependents list
    public void addDependent(Dependent dependent) {
        dependents.add(dependent);
    }

    public void removeDependent(String dependentID) {
        Dependent dependent = findDependentByID(dependentID);
        dependents.remove(dependent);
    }

    public Dependent findDependentByID(String dependentID) {
        for (Dependent dependent : dependents) {
            if (dependent.getCustomerID().equals(dependentID)) {
                return dependent;
            }
        }
        return null;
    }

    private void createFileIfNotExists(String filePath) {
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

    // Method to serialize the dependents to the system
    public void serializeDependentsToFile(String filePath) {
        createFileIfNotExists(filePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(dependents);
            System.out.println("Dependents have been serialized and saved to file: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while serializing dependents to file: " + filePath, e);
        }
    }

    // Method to deserialize dependents of a policy holder
    public void deserializeDependentsFromFile(String filePath, PolicyHolder currentPolicyHolder) {
        if (currentPolicyHolder != null) {
            try (FileInputStream fileInputStream = new FileInputStream(filePath);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                Object importedObject = objectInputStream.readObject();

                if (importedObject instanceof ArrayList<?>) {
                    @SuppressWarnings("unchecked")
                    ArrayList<Dependent> allDependents = (ArrayList<Dependent>) importedObject;
                    ArrayList<Dependent> dependentArrayList = new ArrayList<>(allDependents);

                    dependents = dependentArrayList.stream()
                            .filter(dependent -> dependent.getPolicyHolder().equals(currentPolicyHolder))
                            .collect(Collectors.toCollection(ArrayList::new));

                    System.out.println("Dependents have been deserialized and imported from " + filePath + " for policy holder " + currentPolicyHolder.getCustomerID());
                    return;
                }
                logger.log(Level.SEVERE, "Unexpected data format in the dependents file.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IO exception while reading dependents file", e);
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Class not found during deserialization.", e);
            }
        } else {
            logger.log(Level.SEVERE, "Error: No current policy holder set.");
        }
    }

    // Method to deserialize ALL dependents in the system (used for DependentView)
    public void deserializeAllDependents(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Object importedObject = objectInputStream.readObject();

            if (importedObject instanceof ArrayList<?>) {
                dependents = (ArrayList<Dependent>) importedObject;
                System.out.println("Dependents have been deserialized and imported from " + filePath);
                return;
            }
            logger.log(Level.SEVERE, "Unexpected data format in the dependents file.");
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error while deserializing dependents from file: " + filePath, e);
        }
    }

}
