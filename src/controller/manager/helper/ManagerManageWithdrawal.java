package controller.manager.helper;
import container.*;
import entity.*;
import utils.Colour;

import java.util.ArrayList;
import java.util.Scanner;
import controller.manager.template.IManagerManageWithdrawal;
public class ManagerManageWithdrawal implements IManagerManageWithdrawal{
    private WithdrawalList withdrawalList;
    private ApplicationList applicationList;
    private Scanner scanner;

    public ManagerManageWithdrawal(WithdrawalList withdrawalList, ApplicationList applicationList) {
        this.withdrawalList = withdrawalList;
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    public void manageWithdrawal(Project project) {
        ArrayList<Withdrawal> pendingWithdrawals = withdrawalList.getPendingWithdrawalsByProject(project);

        if (pendingWithdrawals.isEmpty()) {
            System.out.println(Colour.RED + "No pending withdrawals for this project." + Colour.RESET);
            return;
        }

        System.out.println("Pending Withdrawals:");
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            System.out.println((i + 1) + ". " + pendingWithdrawals.get(i));
        }

        System.out.print("Select a withdrawal to manage (enter number): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input." + Colour.RESET);
            return;
        }

        if (choice < 0 || choice >= pendingWithdrawals.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            return;
        }

        Withdrawal withdrawal = pendingWithdrawals.get(choice);
        Application application = withdrawal.getApplication();
        Applicant applicant = application.getApplicant();

        System.out.println("1. Approve");
        System.out.println("2. Reject");
        System.out.print("Enter your choice: ");
        String action = scanner.nextLine();

        if (action.equals("1")) {
            withdrawal.setWithdrawalStatus(Withdrawal.WithdrawalStatus.APPROVED);

            if (application.getBookedFlat()) {
                Application.FlatType flatType = application.getFlatType();

                // Restore flat availability
                if (flatType == Application.FlatType.TWOROOM) {
                    project.setAvailableTwoRoom(project.getAvailableTwoRoom() + 1);
                } else if (flatType == Application.FlatType.THREEROOM) {
                    project.setAvailableThreeRoom(project.getAvailableThreeRoom() + 1);
                }
            }

            // Change status to unsuccessful and clear currentApplication
            application.setApplicationStatus(Application.ApplicationStatus.UNSUCCESSFUL);

            System.out.println(Colour.GREEN + "Withdrawal approved." + Colour.RESET + "Application marked as UNSUCCESSFUL.");
        } else if (action.equals("2")) {
            withdrawal.setWithdrawalStatus(Withdrawal.WithdrawalStatus.REJECTED);
            System.out.println(Colour.RED + "Withdrawal rejected." + Colour.RESET);
        } else {
            System.out.println(Colour.RED + "Invalid action." + Colour.RESET);
        }
    }

}