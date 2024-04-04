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
        return null;
    }


    public void addAdmin(Admin admin) {
        admins.add(admin);
    }


    public Admin authenticateAdmin(String username, String password) {
        return findAdmin(username, password);
    }


    public List<Admin> getAllAdmins() {
        return new ArrayList<>(admins);
    }

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


    public Customer findCustomer(String customerID) {
        for (Customer customer : this.getAllCustomers()) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }


    public List<Customer> getAllCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        allCustomers.addAll(policyHoldersController.getAllPolicyHolders());
        allCustomers.addAll(dependentsController.getAllDependents());
        return allCustomers;
    }


    public List<PolicyHolder> getAllPolicyHolders() {
        return policyHoldersController.getAllPolicyHolders();
    }


    public List<Dependent> getAllDependents() {
        return dependentsController.getAllDependents();
    }





    public void serializeAdminToFile(String filePath) {
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
