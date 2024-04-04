package InsuranceClaimManagementSystem.Controller;


import InsuranceClaimManagementSystem.Model.*;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ClaimsController implements Serializable, ClaimProcessManager {
    private static ClaimsController instance;
    private static int lastClaimID = 0;
    public ArrayList<Claim> claims;
    public ClaimsController() {
        claims = new ArrayList<>();
    }
    private static final Logger logger = Logger.getLogger(ClaimsController.class.getName());

    public static ClaimsController getInstance() {
        if (instance == null) {
            instance = new ClaimsController();
        }
        return instance;
    }

    public List<Claim> getAllClaims() {
        return claims;
    }


    @Override
    public void addClaim(Claim claim) {
        claims.add(claim);
    }

    @Override
    public void updateClaim(Claim claim) {
        // Find the claim by ID and update the details
        for (int i = 0; i < claims.size(); i++) {
            if (claims.get(i).getClaimID().equals(claim.getClaimID())) {
                claims.set(i, claim);
                break;
            }
        }
    }

    @Override
    public void deleteClaim(String claimID) {
        // Find and remove the claim by ID
        claims.removeIf(claim -> claim.getClaimID().equals(claimID));
    }

    @Override
    public Claim getAClaim(String claimID) {
        // Find and return the claim by ID
        for (Claim claim : claims) {
            if (claim.getClaimID().equals(claimID)) {
                return claim;
            }
        }
        return null;
    }

    @Override
    public List<Claim> getClaims() {
        return new ArrayList<>(claims); // Return a copy of the list to prevent direct modification
    }

    public boolean claimExits(String claimID) {
        for (Claim claim : claims) {
            if (claim.getClaimID().equals(claimID)) {
                return true;
            }
        }
        return false;
    }


    public String generateClaimID() {
        lastClaimID++;
        return "f-" + String.format("%010d", lastClaimID);
    }


    public Claim getClaimByID(String claimID) {
        Claim claim = null;
        for (Claim c : claims) {
            if (c.getClaimID().equals(claimID)) {
                claim = c;
            }
        }
        return claim;
    }



    public void serializeClaimsToFile(String filePath) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)){

            objectOutputStream.writeObject(claims);
            System.out.println("Claims have been serialized to file: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error occurred while serializing claims to file " + filePath, e);
        }
    }


    public void deserializeAllClaimsFromFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            Object importedObject = objectInputStream.readObject();

            if (importedObject instanceof ArrayList<?> importedData && !((ArrayList<?>) importedObject).isEmpty()) {
                if (importedData.get(0) instanceof Claim) {
                    claims = (ArrayList<Claim>) importedData;
                    System.out.println("Claims have been deserialized and imported from " + filePath);
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

}