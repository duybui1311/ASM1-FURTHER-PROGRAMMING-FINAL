/**
 * @author <Bui Cong Duy - s3978546>
 */

package InsuranceClaimManagementSystem.Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public abstract class Customer implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static int lastAssignedID = 0;
    private String customerID;
    private String fullName;
    private InsuranceCard insuranceCard;
    private List<Claim> claims;

    public Customer(String customerID, String fullName, InsuranceCard insuranceCard) {
        this.customerID = customerID;
        this.fullName = fullName;
        this.insuranceCard = insuranceCard;
    }

    private String generateID() {
        // Increment the last assigned ID and format it
        lastAssignedID++;
        return "c-" + String.format("%07d", lastAssignedID);
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
