/**
 * @author <Bui Cong Duy - s3978546>
 */
package InsuranceClaimManagementSystem.Controller;

import InsuranceClaimManagementSystem.Model.*;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PolicyHoldersController implements Serializable {
    private static PolicyHoldersController instance;
    private PolicyHolder currentPolicyHolder;
    private ArrayList<PolicyHolder> policyHolders;
    private final DependentsController dependentsController = DependentsController.getInstance();
    private static final Logger logger = Logger.getLogger(PolicyHoldersController.class.getName());
    public PolicyHoldersController() {
        policyHolders = new ArrayList<>();
    }

    public static PolicyHoldersController getInstance() {
        if (instance == null) {
            instance = new PolicyHoldersController();
        }
        return instance;
    }

    public List<PolicyHolder> getAllPolicyHolders() {
        return policyHolders;
    }


    public PolicyHolder authenticatePolicyHolder(String userID, String fullName) {
        PolicyHolder policyHolder = findPolicyHolder(userID, fullName);
        if (policyHolder != null) {
            currentPolicyHolder = policyHolder;
        }
        return policyHolder;
    }


    public PolicyHolder findPolicyHolder(String policyHolderID, String fullName) {
        for (PolicyHolder policyHolder : policyHolders) {
            if (policyHolder.getCustomerID().equals(policyHolderID) && policyHolder.getFullName().equals(fullName)) {
                return policyHolder;
            }
        }
        return null; // Return null if no policyholder is found
    }

    public PolicyHolder findPolicyHolderByID(String policyHolderID) {
        for (PolicyHolder policyHolder : policyHolders) {
            if (policyHolder.getCustomerID().equals(policyHolderID)) {
                return policyHolder;
            }
        }
        return null;
    }



    public void serializePolicyHoldersToFile(String filePath) {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ){
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Create parent directory
            objectOutputStream.writeObject(policyHolders);
            System.out.println("Policy holders have been saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error: Unable to save policy holders to " + filePath);
        }
    }

    // Method to read the policyholders' data from the system
    public void deserializePolicyHoldersFromFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Object importedObject = objectInputStream.readObject();

            if (importedObject instanceof ArrayList<?> importedData && !((ArrayList<?>) importedObject).isEmpty()) {
                if (importedData.get(0) instanceof PolicyHolder) {
                    policyHolders = (ArrayList<PolicyHolder>) importedData;
                    System.out.println("Policy holders have been deserialized and imported from " + filePath);
                    return;
                }
            }
            logger.log(Level.SEVERE, "Unexpected data format in the policy holders file.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception while reading policy holders file.", e);
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Class not found during deserialization.", e);
        }
    }


    public PolicyHolder getCurrentPolicyHolder() {
        return currentPolicyHolder;
    }


    public void setCurrentPolicyHolder(PolicyHolder currentPolicyHolder) {
        this.currentPolicyHolder = currentPolicyHolder;
    }




    public void addPolicyHolder(PolicyHolder policyHolder) {
        policyHolders.add(policyHolder);
    }

    public void removePolicyHolder(String policyHolderID) {
        PolicyHolder policyHolder = findPolicyHolderByID(policyHolderID);
        policyHolders.remove(policyHolder);
    }


    public void addDependent(PolicyHolder currentPolicyHolder, Dependent dependent) {
        if (currentPolicyHolder != null) {
            currentPolicyHolder.addDependent(dependent);

            // Update the dependents in the system
            dependentsController.serializeDependentsToFile("data/dependents.dat");
        } else {
            System.err.println("Error: No current policy holder set.");
        }
    }

    // Allows policyholder to remove a dependent from the dependents list
    public boolean removeDependent(PolicyHolder currentPolicyHolder, String dependentID) {
        if (currentPolicyHolder != null) {
            Optional<Dependent> dependentToRemove = currentPolicyHolder.getDependents().stream()
                    .filter(dependent -> dependent.getCustomerID().equals(dependentID))
                    .findFirst();
            if (dependentToRemove.isPresent()) {
                currentPolicyHolder.removeDependent(dependentToRemove.get());
                return true;
            } else {
                System.err.println("Error: Dependent with ID " + dependentID + " not found.");
                return false;
            }
        } else {
            System.err.println("Error: No current policy holder set.");
            return false;
        }
    }


    public boolean dependentExists(String dependentID) {
        for (Dependent dependent : getAllDependents(currentPolicyHolder)) {
            if (dependent.getCustomerID().equals(dependentID)) {
                return true;
            }
        }
        return false;
    }


    public Dependent getDependentByID(String dependentID) {
        Dependent dependent = null;
        for (Dependent d : getCurrentPolicyHolder().getDependents()) {
            if (d.getCustomerID().equals(dependentID)) {
                dependent = d;
            }
        }
        return dependent;
    }


    public Optional<Dependent> getDependentByName(String dependentName) {
        for (Dependent dependent : getCurrentPolicyHolder().getDependents()) {
            if (dependent.getFullName().equalsIgnoreCase(dependentName)) {
                return Optional.of(dependent);
            }
        }
        return Optional.empty();
    }


    public List<Dependent> getAllDependents(PolicyHolder currentPolicyHolder) {
        return currentPolicyHolder.getDependents();
    }

    public void setPolicyHolders(ArrayList<PolicyHolder> policyHolders) {
        this.policyHolders = policyHolders;
    }

    // Method to quickly set the dependents list
    public void setDependents(PolicyHolder currentPolicyHolder, List<Dependent> dependents) {
        if (currentPolicyHolder != null) {
            currentPolicyHolder.setDependents(dependents);
        } else {
            System.err.println("Error: No current policy holder set ");
        }
    }

    // Get the insurance card of a policyholder by their ID
    public InsuranceCard getInsuranceCard(String policyHolderID, String fullName) {
        PolicyHolder policyHolder = findPolicyHolder(policyHolderID, fullName);
        return policyHolder != null ? policyHolder.getInsuranceCard() : null;
    }
}
