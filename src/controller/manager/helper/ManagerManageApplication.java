package controller.manager.helper;
import java.util.ArrayList;
import java.util.Scanner;
import entity.*;
import utils.Colour;
import container.*;
import controller.manager.template.IManagerManageApplication;
public class ManagerManageApplication implements IManagerManageApplication{
    private ApplicationList applicationList;
    private Scanner scanner;

    public ManagerManageApplication(ApplicationList applicationList) {
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    // View all applications for a given project
    public void viewApplication(Project project) {
        ArrayList<Application> applications = applicationList.getApplicationsByProject(project);
        if (applications.isEmpty()) {
            System.out.println(Colour.RED + "No applications for this project." + Colour.RESET);
            return;
        }

        System.out.println("All Applications for Project: " + project.getProjectName());
        for (Application app : applications) {
            System.out.println(app); // Ensure Application.toString() is informative
        }
    }

    // Manage only pending applications
    public void manageApplication(Project project) {
        ArrayList<Application> pendingApplications = applicationList.getPendingApplicationsByProject(project);

        if (pendingApplications.isEmpty()) {
            System.out.println(Colour.RED + "No pending applications to manage." + Colour.RESET);
            return;
        }

        System.out.println("Pending Applications:");
        for (int i = 0; i < pendingApplications.size(); i++) {
            System.out.println((i + 1) + ". " + pendingApplications.get(i));
        }

        System.out.print("Select application to manage (enter number): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input." + Colour.RESET);
            return;
        }

        if (choice < 0 || choice >= pendingApplications.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            return;
        }

        Application selectedApp = pendingApplications.get(choice);
        Application.FlatType flatType = selectedApp.getFlatType();

        int availability;

        if (flatType == Application.FlatType.TWOROOM){
            availability = project.getAvailableTwoRoom();
        }
        else{
            availability = project.getAvailableThreeRoom();
        }

        if (availability > 0) {
            System.out.println("1. Accept");
            System.out.println("2. Reject");
            System.out.print("Enter your choice: ");
            String action = scanner.nextLine();

            if (action.equals("1")) {
                selectedApp.setApplicationStatus(Application.ApplicationStatus.SUCCESSFUL);
                if (flatType == Application.FlatType.TWOROOM){
                    project.setAvailableTwoRoom(availability-1);
                } else{
                    project.setAvailableThreeRoom(availability-1);
                }
                System.out.println(Colour.GREEN + "Application accepted." + Colour.RESET);
            } else if (action.equals("2")) {
                selectedApp.setApplicationStatus(Application.ApplicationStatus.UNSUCCESSFUL);
                selectedApp.getApplicant().setCurrentApplication(null);
                System.out.println(Colour.YELLOW + "Application rejected." + Colour.RESET);
            } else {
                System.out.println(Colour.RED + "Invalid action." + Colour.RESET);
            }
        } else {
            selectedApp.setApplicationStatus(Application.ApplicationStatus.UNSUCCESSFUL);
            selectedApp.getApplicant().setCurrentApplication(null);
            System.out.println("No more flats available for " + flatType + "." + Colour.RED + "Application rejected." + Colour.RESET);
        }
    }
}