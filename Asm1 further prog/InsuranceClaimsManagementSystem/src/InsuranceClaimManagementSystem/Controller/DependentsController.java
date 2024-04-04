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


    public Dependent authenticateDependent(String userID, String fullName) {
        Dependent dependent = findDependent(userID, fullName);
        if (dependent != null) {
            currentDependent = dependent;
        }
        return dependent;
    }



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


    public PolicyHolder getPolicyOwner(Dependent dependent) {
        return dependent.getPolicyHolder();
    }


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




    public void serializeDependentsToFile(String filePath) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(dependents);
            System.out.println("Dependents have been serialized and saved to file: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while serializing dependents to file: " + filePath, e);
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
