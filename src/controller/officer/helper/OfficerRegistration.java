package controller.officer.helper;
import container.*;
import entity.*;
import utils.ClearScreen;
import utils.Colour;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import controller.officer.template.IOfficerRegistration;
public class OfficerRegistration implements IOfficerRegistration{
    private Officer officer;
    private ProjectList projectList;
    private RegistrationList registrationList;
    private ApplicationList applicationList;
    private Scanner scanner;

    public OfficerRegistration(Officer officer, ProjectList projectList, RegistrationList registrationList, ApplicationList applicationList) {
        this.officer = officer;
        this.projectList = projectList;
        this.registrationList = registrationList;
        this.applicationList = applicationList;
        this.scanner = new Scanner(System.in);
    }

    public void registerForProject() {
        // If officer is already assigned or has a pending/approved registration
        if (officer.getAssignedProject() != null || !officer.getPendingRegistrations().isEmpty() || !officer.getApprovedRegistrations().isEmpty()) {
            System.out.println(Colour.RED + "You are already managing or registering for a project. Cannot register again." + Colour.RESET);
            return;
        }

        // Filter available projects
        ArrayList<Project> availableProjects = new ArrayList<>();
        for (Project project : projectList.getProjectList()) {
            boolean hasSlot = project.getMaxOfficer() > project.getOfficers().size();

            // Check if officer already applied as applicant (via application list)
            boolean hasNotApplied = applicationList.getApplicationByApplicant(officer) == null;

            if (hasSlot && hasNotApplied) {
                availableProjects.add(project);
            }
        }

        if (availableProjects.isEmpty()) {
            System.out.println(Colour.RED + " No projects are currently available for registration." + Colour.RESET);
            return;
        }

        // Display and let officer choose
        System.out.println("\nAvailable Projects for Officer Registration:");
        for (int i = 0; i < availableProjects.size(); i++) {
            System.out.println((i + 1) + ") " + availableProjects.get(i).getProjectName());
        }

        System.out.print("Select a project to register (0 to cancel): ");
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

        if (choice < 1 || choice > availableProjects.size()) {
            System.out.println(Colour.GREEN + "Registration cancelled." + Colour.RESET);
            return;
        }

        Project selectedProject = availableProjects.get(choice - 1);
        Registration registration = new Registration(officer, selectedProject);
        registrationList.addRegistration(registration);
        officer.addRegistration(registration);

        System.out.println(Colour.GREEN + "Registration submitted successfully for project: " + selectedProject.getProjectName() + Colour.RESET);
    }

    public void viewRegistrationStatus() {
        ArrayList<Registration> registrations = officer.getRegistrations();
        if (registrations.isEmpty()) {
            System.out.println(Colour.RED + "No registration history found." + Colour.RESET);
            return;
        }

        System.out.println("\nRegistration History:");
        for (Registration r : registrations) {
            System.out.println("- Project: " + r.getProject().getProjectName() +
                               " | Status: " + r.getStatus());
        }
    }
}
