package controller.applicant.helper;
import container.*;
import entity.*;
import utils.Colour;

import java.util.Scanner;
import controller.applicant.template.IApplicantMakeWithdrawal;
public class ApplicantMakeWithdrawal implements IApplicantMakeWithdrawal{
    private Applicant applicant;
    private WithdrawalList withdrawalList;
    private ApplicationList applicationList;
    private Scanner scanner;

    public ApplicantMakeWithdrawal(Applicant applicant, WithdrawalList withdrawalList, ApplicationList applicationList) {
        this.applicant = applicant;
        this.withdrawalList = withdrawalList;
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    public void withdrawApplication() {
        Application application = applicationList.getApplicationByApplicant(applicant);
        if (application == null) {
            System.out.println(Colour.RED + "You do not have an active application to withdraw." + Colour.RESET);
            // BackButton.goBack();
            return;
        }
        System.out.print("Are you sure you want to withdraw? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            Withdrawal withdrawal = new Withdrawal(application);
            withdrawalList.addWithdrawal(withdrawal);
            applicationList.removeApplication(application);
            applicant.setCurrentApplication(null);
        }
        else {
            System.out.println(Colour.YELLOW + "Withdrawal cancelled." + Colour.RESET);
        }
        // BackButton.goBack();
    }
}