package controller.officer.helper;
import container.*;
import entity.*;
import utils.ClearScreen;
import utils.Colour;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import controller.officer.template.IOfficerManageApplication;
public class OfficerManageApplication implements IOfficerManageApplication{
    private Officer officer;
    private ApplicationList applicationList;
    private Scanner scanner;
    public OfficerManageApplication(Officer officer, ApplicationList applicationList) {
        this.officer = officer;
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    // Method 1: View all successful applications for the officer's project
    public void viewApplications() {
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        System.out.println("\n=== Successful Applications for Project: " + assignedProject.getProjectName() + " ===");
        List<Application> applications = applicationList.getApplicationList();

        boolean found = false;
        for (Application application : applications) {
            if (application.getProject().equals(assignedProject) && application.getApplicationStatus() == Application.ApplicationStatus.SUCCESSFUL) {
                System.out.println("Applicant NRIC: " + application.getApplicant().getNric());
                found = true;
            }
        }

        if (!found) {
            System.out.println(Colour.RED + "No successful applications found for your project." + Colour.RESET);
        }
    }

    // Method 2: Update application status from SUCCESSFUL to BOOKED
    public void updateApplicationStatus() {
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        List<Application> applications = applicationList.getApplicationList();

        // Filter successful applications
        List<Application> successfulApplication = applications.stream()
                .filter(app -> app.getProject().equals(assignedProject) && app.getApplicationStatus() == Application.ApplicationStatus.SUCCESSFUL)
                .toList();

        if (successfulApplication.isEmpty()) {
            System.out.println(Colour.RED + "No successful applications to update." + Colour.RESET);
            return;
        }

        // Display NRICs
        System.out.println("\n=== Successful Applications (Eligible for Booking) ===");
        for (int i = 0; i < successfulApplication.size(); i++) {
            System.out.println((i + 1) + ") " + successfulApplication.get(i).getApplicant().getNric());
        }

        System.out.print("Select an application to book (enter number or 0 to cancel): ");
        int choice;
        try{
            choice = scanner.nextInt();
        }
        catch(InputMismatchException e){
            ClearScreen.clear();
            System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (choice <= 0 || choice > successfulApplication.size()) {
            System.out.println(Colour.RED + "Cancelled or invalid choice." + Colour.RESET);
            return;
        }

        Application selectedApplication = successfulApplication.get(choice - 1);

        // Update number of flats
        Application.FlatType type = selectedApplication.getFlatType();
        if (type == Application.FlatType.TWOROOM) {
            int current = assignedProject.getAvailableTwoRoom();
            // if (current > 0) {
            //     assignedProject.setAvailableTwoRoom(current - 1);
            //     selectedApplication.setBookedFlat(true);
            //     System.out.println("Success");
            // }
            // else {
            //     System.out.println("No more 2-room flats available.");
            //     return;
            // }
            assignedProject.setAvailableTwoRoom(current - 1);
            selectedApplication.setBookedFlat(true);
            System.out.println(Colour.GREEN + "Success!" + Colour.RESET);
            
        }
        else if (type == Application.FlatType.THREEROOM) {
            int current = assignedProject.getAvailableThreeRoom();
            // if (current > 0) {
            //     assignedProject.setAvailableThreeRoom(current - 1);
            //     selectedApplication.setBookedFlat(true);
            // }
            // else {
            //     System.out.println("No more 3-room flats available.");
            //     return;
            // }
            assignedProject.setAvailableThreeRoom(current - 1);
            selectedApplication.setBookedFlat(true);
            System.out.println(Colour.GREEN + "Success!" + Colour.RESET);
        }
        // Update application status:
        selectedApplication.setApplicationStatus(Application.ApplicationStatus.BOOKED);
        // Use-a relationship:
        OfficerGenerateReceipt receiptHandler = new OfficerGenerateReceipt(officer);
        receiptHandler.generateReceipt();

        // selectedApplication.setApplicationStatus(Application.ApplicationStatus.BOOKED);
        // System.out.println("Application status updated to BOOKED and flat availability adjusted.");
    }
}
