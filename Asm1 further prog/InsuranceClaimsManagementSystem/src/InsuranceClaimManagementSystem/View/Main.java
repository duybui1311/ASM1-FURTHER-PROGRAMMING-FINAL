package InsuranceClaimManagementSystem.View;

import InsuranceClaimManagementSystem.util.DataGenerator;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        // Initialize the Scanner object
        Scanner scanner = new Scanner(System.in);

        // Generate sample data
        DataGenerator.main(new String[]{});

        // Welcome message
        System.out.println("=============================================================== WELCOME TO DUY BUI'S INSURANCE CLAIMS MANAGEMENT SYSTEM! ===============================================================");

        // Directly login as admin
        AdminView adminView = new AdminView();
        adminView.adminLogin();
    }
}
