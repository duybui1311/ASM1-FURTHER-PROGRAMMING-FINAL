package InsuranceClaimManagementSystem.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PolicyHolder extends Customer implements Serializable {
    private List<Dependent> dependents;
    private List<Claim> claims;
    public PolicyHolder(String customerID, String fullName, InsuranceCard insuranceCard) {
        super(customerID, fullName, insuranceCard);
        this.dependents = new ArrayList<>();
    }

    // Policy holder can add dependent(s)
    public void addDependent(Dependent dependent) {
        dependents.add(dependent);
    }

    public void removeDependent(Dependent dependent) {
        dependents.remove(dependent);
    }

    public List<Dependent> getDependents() {
        return dependents;
    }

    public void setDependents(List<Dependent> dependents) {
        this.dependents = dependents;
    }

    @Override
    public List<Claim> getClaims() {
        return claims;
    }

    @Override
    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public void updateDependentInfo(Dependent dependent, String fullName, InsuranceCard insuranceCard) {
        dependent.setFullName(fullName);
        dependent.setInsuranceCard(insuranceCard);
    }

    // Policyholder can renew a policy
    public void renewPolicy(int years) {
        // Get current expiration date
        Date currentExpirationDate = getInsuranceCard().getExpirationDate();

        // Calculate the new expiration date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentExpirationDate);
        calendar.add(Calendar.YEAR, years);
        Date newExpirationDate = calendar.getTime();

        // Update the expiration date of the insurance card
        getInsuranceCard().setExpirationDate(newExpirationDate);
    }

    // Policyholder can also modify a policy
    public void modifyPolicy(InsuranceCard newInsuranceCard) {
        // Update the insurance card with the new details
        setInsuranceCard(newInsuranceCard);
    }

    // View claim history
    public List<Claim> viewClaimHistory() {
        return getClaims();
    }

    @Override
    public String toString() {
        return getFullName() + "(" + getCustomerID() + ")";
    }
}
