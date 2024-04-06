/**
 * @author <Bui Cong Duy - s3978546>
 */
package InsuranceClaimManagementSystem.Controller;

import InsuranceClaimManagementSystem.Model.Customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomersController implements Serializable {
    private static CustomersController instance;
    private ArrayList<Customer> customers;
    private static int lastUserID = 0; // Ensuring each generated ID is unique

    public static CustomersController getInstance() {
        if (instance == null) {
            instance = new CustomersController();
        }
        return instance;
    }


    public String generateUserID() {
        lastUserID++;
        return "c-" + String.format("%07d", lastUserID);
    }

    public CustomersController() {
        this.customers = new ArrayList<>();
    }

    public Customer findCustomerByID(String customerID) {
        for (Customer customer : customers) {
            if (customer.getCustomerID().equals(customerID)) {
                return customer;
            }
        }
        return null;
    }
}
