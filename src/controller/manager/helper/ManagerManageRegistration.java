package controller.manager.helper;
import entity.*;
import utils.Colour;
import container.*;
import java.util.ArrayList;
import java.util.Scanner;
import controller.manager.template.IManagerManageRegistration;
public class ManagerManageRegistration implements IManagerManageRegistration{
    private RegistrationList registrationList;
    private Scanner scanner;

    public ManagerManageRegistration(RegistrationList registrationList) {
        this.registrationList = registrationList;
        this.scanner = new Scanner(System.in);
    }

    // View all registrations created by this manager (not filtered by project)
    public void viewRegistration() {
        ArrayList<Registration> allRegistrations = registrationList.getRegistrations();
        if (allRegistrations.isEmpty()) {
            System.out.println(Colour.RED + "No registrations found." + Colour.RESET);
            return;
        }

        System.out.println("All Registrations:");
        for (Registration registration : allRegistrations) {
            System.out.println(registration); // Ensure Registration.toString() is informative
        }
    }

    // Manage PENDING officer registrations for a project the manager is currently handling
    public void manageRegistration(Project project) {
        ArrayList<Registration> pendingRegistrations = registrationList.getPendingRegistrationsByProject(project);

        if (pendingRegistrations.isEmpty()) {
            System.out.println(Colour.RED + "No pending registrations for this project." + Colour.RESET);
            return;
        }

        System.out.println("Pending Registrations for Project: " + project.getProjectName());
        for (int i = 0; i < pendingRegistrations.size(); i++) {
            System.out.println((i + 1) + ". " + pendingRegistrations.get(i));
        }

        System.out.print("Select registration to manage (enter number): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input." + Colour.RESET);
            return;
        }

        if (choice < 0 || choice >= pendingRegistrations.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            return;
        }

        Registration selectedReg = pendingRegistrations.get(choice);

        if (project.getOfficers().size() < project.getMaxOfficer()) {
            System.out.println("1. Approve");
            System.out.println("2. Reject");
            System.out.print("Enter your choice: ");
            String action = scanner.nextLine();

            if (action.equals("1")) {
                selectedReg.setStatus(Registration.RegistrationStatus.APPROVED);
                project.addOfficers(selectedReg.getOfficer());
                System.out.println("Updated officers for project " + project.getProjectName() + ": " + project.getOfficers());
                selectedReg.getOfficer().setAssignedProject(project);
                System.out.println(Colour.GREEN + "Officer registration approved." + Colour.RESET);
            } else if (action.equals("2")) {
                selectedReg.setStatus(Registration.RegistrationStatus.REJECTED);
                System.out.println(Colour.RED + "Officer registration rejected." + Colour.RESET);
            } else {
                System.out.println(Colour.RED + "Invalid action." + Colour.RESET);
            }
        } else {
            selectedReg.setStatus(Registration.RegistrationStatus.REJECTED);
            System.out.println("Officer slots full." + Colour.RED + "Registration rejected." + Colour.RESET);
        }
    }
}
