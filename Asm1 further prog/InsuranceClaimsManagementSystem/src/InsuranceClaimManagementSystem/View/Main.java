package InsuranceClaimManagementSystem.View;

import InsuranceClaimManagementSystem.util.DataGenerator;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);


        DataGenerator.main(new String[]{});


        System.out.println("=============================================================== WELCOME TO DUY BUI'S INSURANCE CLAIMS MANAGEMENT SYSTEM! ===============================================================");


        AdminView adminView = new AdminView();
        adminView.adminLogin();
    }
}
