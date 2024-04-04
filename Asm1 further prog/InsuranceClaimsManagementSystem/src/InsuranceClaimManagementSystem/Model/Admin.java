package InsuranceClaimManagementSystem.Model;

import java.io.Serializable;
import java.util.List;

public class Admin implements Serializable {
    private String userID;
    private String username;
    private String password;
    private List<Customer> customerList;
    private List<PolicyHolder> policyHolderList;
    private List<Dependent> dependentList;
    private List<InsuranceCard> insuranceCardList;
    private List<Claim> claimList;

    public Admin(String userID, String username, String password) {
        this.userID = userID;
        this.username = username;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public List<PolicyHolder> getPolicyHolderList() {
        return policyHolderList;
    }

    public void setPolicyHolderList(List<PolicyHolder> policyHolderList) {
        this.policyHolderList = policyHolderList;
    }

    public List<Dependent> getDependentList() {
        return dependentList;
    }

    public void setDependentList(List<Dependent> dependentList) {
        this.dependentList = dependentList;
    }

    public List<InsuranceCard> getInsuranceCardList() {
        return insuranceCardList;
    }

    public void setInsuranceCardList(List<InsuranceCard> insuranceCardList) {
        this.insuranceCardList = insuranceCardList;
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public void setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
    }
}
