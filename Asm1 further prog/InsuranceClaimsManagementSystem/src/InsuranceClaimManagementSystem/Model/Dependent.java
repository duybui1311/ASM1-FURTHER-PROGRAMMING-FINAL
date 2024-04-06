/**
 * @author <Bui Cong Duy - s3978546>
 */

package InsuranceClaimManagementSystem.Model;

import java.io.Serializable;
import java.util.List;

public class Dependent extends Customer implements Serializable {
    private PolicyHolder policyHolder;
    private List<Claim> claims;
    public Dependent(String customerID, String fullName, InsuranceCard insuranceCard, PolicyHolder policyHolder) {
        super(customerID, fullName, insuranceCard);
        this.policyHolder = policyHolder;
    }

    // View claim history
    public List<Claim> viewClaimHistory() {
        return getClaims();
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    @Override
    public List<Claim> getClaims() {
        return claims;
    }

    @Override
    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    @Override
    public String toString() {
        return getFullName() + "(" + getCustomerID() + ")";
    }
}
