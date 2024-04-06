/**
 * @author <Bui Cong Duy - s3978546>
 */
package InsuranceClaimManagementSystem.Model;

import java.util.List;

public interface ClaimProcessManager {
    void addClaim(Claim claim);
    void updateClaim(Claim claim);
    void deleteClaim(String claimID);
    Claim getAClaim(String claimID);

    List<Claim> getAClaims();

    List<Claim> getClaims();
}
