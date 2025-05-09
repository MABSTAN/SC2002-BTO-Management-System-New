package controller.officer.helper;

import container.*;
import entity.*;
import utils.ClearScreen;
import utils.Colour;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import controller.FilterSettings;
import controller.UserSession;
import controller.officer.template.IOfficerManageApplication;

/**
 * Handles application-related actions for officers assigned to BTO projects.
 * Officers can view successful applications for their assigned project,
 * and update them to BOOKED status, triggering receipt generation.
 */
public class OfficerManageApplication implements IOfficerManageApplication {

    private Officer officer;
    private ApplicationList applicationList;
    private Scanner scanner;

    /**
     * Constructs the application manager for a specific officer.
     *
     * @param officer          the officer using the system
     * @param applicationList  the list of all applications
     */
    public OfficerManageApplication(Officer officer, ApplicationList applicationList) {
        this.officer = officer;
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the NRICs of applicants with SUCCESSFUL applications
     * for the project the officer is currently managing.
     */
    public void viewApplications() {
        FilterSettings filters = UserSession.getFilterSettings();
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        System.out.println(Colour.BLUE + "\n==== Successful Applications for Project: " + assignedProject.getProjectName() + " ====" + Colour.RESET);
        List<Application> applications = applicationList.getApplicationList();

        boolean found = false;
        for (Application application : applications) {
            if (application.getProject().equals(assignedProject)
                && application.getApplicationStatus() == Application.ApplicationStatus.SUCCESSFUL
                && (filters.getFlatType() == null || application.getFlatType() == filters.getFlatType())) {
                System.out.println(Colour.BLUE + "Applicant NRIC: " + application.getApplicant().getNric() + Colour.RESET);
                found = true;
            }
        }

        if (!found) {
            System.out.println(Colour.RED + "No successful applications found for your project with current filters." + Colour.RESET);
        }
    }

    /**
     * Updates a SUCCESSFUL application to BOOKED status and generates a receipt.
     * Reduces flat availability based on the chosen flat type.
     */
    public void updateApplicationStatus() {
        FilterSettings filters = UserSession.getFilterSettings();
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        List<Application> applications = applicationList.getApplicationList();

        List<Application> successfulApplication = applications.stream()
            .filter(app -> app.getProject().equals(assignedProject)
                && app.getApplicationStatus() == Application.ApplicationStatus.SUCCESSFUL
                && (filters.getFlatType() == null || app.getFlatType() == filters.getFlatType()))
            .toList();

        if (successfulApplication.isEmpty()) {
            System.out.println(Colour.RED + "No successful applications to update with current filters." + Colour.RESET);
            return;
        }

        System.out.println(Colour.BLUE + "\n==== Successful Applications (Eligible for Booking) ====" + Colour.RESET);
        for (int i = 0; i < successfulApplication.size(); i++) {
            System.out.println((i + 1) + ") " + successfulApplication.get(i).getApplicant().getName()
                + ": " + successfulApplication.get(i).getApplicant().getNric());
        }

        System.out.print(Colour.BLUE + "Select an application to book (enter number or 0 to cancel): " + Colour.RESET);
        int choice;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
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
        Application.FlatType type = selectedApplication.getFlatType();

        if (type == Application.FlatType.TWOROOM) {
            int current = assignedProject.getAvailableTwoRoom();
            assignedProject.setAvailableTwoRoom(current - 1);
        } else if (type == Application.FlatType.THREEROOM) {
            int current = assignedProject.getAvailableThreeRoom();
            assignedProject.setAvailableThreeRoom(current - 1);
        }

        selectedApplication.setBookedFlat(true);
        selectedApplication.setApplicationStatus(Application.ApplicationStatus.BOOKED);

        OfficerGenerateReceipt receiptHandler = new OfficerGenerateReceipt(officer, selectedApplication);
        receiptHandler.generateReceipt();
    }
}
