package InsuranceClaimManagementSystem.Controller;


import InsuranceClaimManagementSystem.Model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController implements Serializable {
    private static AdminController instance;
    private final PolicyHoldersController policyHoldersController = PolicyHoldersController.getInstance();
    private final DependentsController dependentsController = DependentsController.getInstance();
    private static final Logger logger = Logger.getLogger(AdminController.class.getName());
    private List<Admin> admins;

    public AdminController() {
        this.admins = new ArrayList<>();
    }

    public static AdminController getInstance() {
        if (instance == null) {
            instance = new AdminController();
        }
        return instance;
    }

    // Find an admin by his/her username and password
    public Admin findAdmin(String username, String password) {
        for (Admin admin : admins) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null; // Return null if no policyholder is found
    }

    // Add an admin into the system
    public void addAdmin(Admin admin) {
        admins.add(admin);
    }

    // Method to authenticate admin logins
    public Admin authenticateAdmin(String username, String password) {
        return findAdmin(username, password);
    }

    // Method to remove a user
    public void removeUser(String customerID) {
        for (Customer customer : this.getAllCustomers()) {
            if (customer.getCustomerID().equals(customerID)) {
                if (customer instanceof PolicyHolder) {
                    policyHoldersController.removePolicyHolder(customerID);
                    List<Dependent> dependentsToRemove = ((PolicyHolder) customer).getDependents();
                    for (Dependent dependent : dependentsToRemove) {
                        dependentsController.removeDependent(dependent.getCustomerID());
                    }
                } else if (customer instanceof Dependent) {
                    dependentsController.removeDependent(customerID);
                }
                break;
            }
        }
    }

    // Method to find a customer by ID
    public Customer findCustomer(String customerID) {
        for (Customer customer : this.getAllCustomers()) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }

    // Method to get all admins
    public List<Admin> getAdminList() {
        return admins;
    }

    // Method to get all customers
    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        allCustomers.addAll(policyHoldersController.getAllPolicyHolders());
        allCustomers.addAll(dependentsController.getAllDependents());
        return allCustomers;
    }

    // Method to get all policy holders
    public List<PolicyHolder> getAllPolicyHolders() {
        return policyHoldersController.getAllPolicyHolders();
    }

    // Method to get all dependents
    public List<Dependent> getAllDependents() {
        return dependentsController.getAllDependents();
    }

    // Create a new file for admins' data
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

    // Method to serialize admins to file
    public void serializeAdminToFile(String filePath) {
        createFileIfNotExists(filePath);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)
        ){
            File file = new File(filePath);
            file.getParentFile().mkdirs(); // Create parent directory
            objectOutputStream.writeObject(admins);
            System.out.println("Admin have been saved to " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception while serializing admins file.");
        }
    }

    // Method to read the admins' data
    public void deserializeAdminsFromFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Object importedObject = objectInputStream.readObject();

            if (importedObject instanceof ArrayList<?> importedData && !((ArrayList<?>) importedObject).isEmpty()) {

                if (importedData.get(0) instanceof Admin) {
                    admins = (ArrayList<Admin>) importedData;
                    System.out.println("Admins have been deserialized and imported from data/admins.dat");
                    return;
                }
            }
            System.err.println("Error: Unexpected data format in the file.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception while reading admins file.");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Class not found during deserialization.");
        }
    }
}
