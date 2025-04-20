package controller.officer.helper;
import container.*;
import entity.*;
import utils.ClearScreen;
import utils.Colour;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import controller.officer.template.IOfficerViewProjects;
import controller.applicant.helper.ApplicantViewProjects;
public class OfficerViewProjects extends ApplicantViewProjects implements IOfficerViewProjects{
    private Officer officer;
    private ProjectList projectList;
    private RegistrationList registrationList;

    public OfficerViewProjects(Officer officer, ProjectList projectList, ApplicationList applicationList, RegistrationList registrationList) {
        super(officer, projectList, applicationList);
        this.officer = officer;
        this.projectList = projectList;
        this.registrationList = registrationList;
    }

    @Override
    public void viewProjectList() {

        System.out.println("Available BTO Projects:");
        for (Project project : projectList.getProjectList()) {

            // Skip projects this officer is registered for (even if not yet approved)
            Registration reg = registrationList.getRegistrationByOfficerAndProject(officer, project);
            if (reg != null) continue;

            // Skip if officer is assigned to this project
            if (officer.getAssignedProject() != null && officer.getAssignedProject().equals(project)) {
                continue;
            }
            if(officer.getMaritalStatus() == User.MaritalStatus.SINGLE && officer.getAge() >= 35 && project.getAvailableTwoRoom() > 0){
                System.out.println(project);
            }
            else{
                if(project.getVisibility() == true && (project.getAvailableThreeRoom() > 0 || project.getAvailableTwoRoom() > 0)){
                    System.out.println(project);
                }
            }
        }

        /*********************** */
        // Show all projects even if the visibility is turned off
    }

    @Override
    public void applyForProject() {
        Scanner sc = new Scanner(System.in);
        // Briefly show the name of available projects:
        System.out.println("Projects you can apply:");
        for (Project project : projectList.getProjectList()) {
            Registration reg = registrationList.getRegistrationByOfficerAndProject(officer, project);
            if (reg != null) continue;
            if (officer.getAssignedProject() != null && officer.getAssignedProject().equals(project)) continue;
            if(officer.getMaritalStatus() == User.MaritalStatus.SINGLE && officer.getAge() >= 35 && project.getAvailableTwoRoom() > 0) System.out.println(project.getProjectName());
            else{
                if(project.getVisibility() == true && (project.getAvailableThreeRoom() > 0 || project.getAvailableTwoRoom() > 0)){
                    System.out.println(project.getProjectName());
                }
            }
        }
        /****************************************/
        System.out.print("Enter Project Name to apply: ");
        String projectName = sc.nextLine();
        Project project = projectList.getProjectByName(projectName);

        if (project == null) {
            System.out.println(Colour.RED + "Project not found." + Colour.RESET);
            return;
        }

        // Officer is already assigned to manage this project
        if (officer.getAssignedProject() != null && officer.getAssignedProject().equals(project)) {
            System.out.println(Colour.RED + "You are already managing this project as an Officer. Cannot apply as an applicant." + Colour.RESET);
            return;
        }

        // Officer has registration record for this project
        Registration reg = registrationList.getRegistrationByOfficerAndProject(officer, project);
        if (reg != null) {
            System.out.println(Colour.RED + "You have already registered for this project as an Officer. Cannot apply." + Colour.RESET);
            return;
        }

        // Already has an application
        Application existingApp = super.applicationList.getApplicationByApplicant(officer);
        if (existingApp != null) {
            System.out.println(Colour.RED + "You already have an application." + Colour.RESET);
            return;
        }

        // Can not apply for project which is not opened yet
        if (project.getOpeningDate().after(new Date())){
            System.out.println(Colour.RED + "The project is currently not open for application." + Colour.RESET);
            return;
        }
        // Manage Age group:
        Application.FlatType selectedFlatType;
        //
        if(officer.getMaritalStatus() == User.MaritalStatus.SINGLE && officer.getAge() >= 35){
            System.out.println(Colour.YELLOW + "You are only eligible to apply for 2-ROOM flat" + Colour.RESET);
            selectedFlatType = Application.FlatType.TWOROOM;
        }
        else{
            System.out.println("Select flat type to apply for:");
            System.out.println("Enter 1 -> 2-room flat");
            System.out.println("Enter 2 -> 3-room flat");
            System.out.print("Enter choice (1 or 2): ");
            int choice;
            try{
                choice = sc.nextInt();
            }
            catch(InputMismatchException e){
                ClearScreen.clear();
                System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
                sc.nextLine();
                return;
            }
            sc.nextLine();
            if (choice  == 1) {
                if(project.getAvailableTwoRoom() == 0){
                    System.out.println(Colour.RED + "Sorry, there is not any 2-room flats" + Colour.RESET);
                    return;
                }
                selectedFlatType = Application.FlatType.TWOROOM;
            }
            else if (choice == 2) {
                if(project.getAvailableThreeRoom() == 0){
                    System.out.println(Colour.RED + "Sorry, there is not any 3-room flats" + Colour.RESET);
                    return;
                }
                selectedFlatType = Application.FlatType.THREEROOM;
            }
            else {
                System.out.println(Colour.RED + "Invalid choice. Application cancelled." + Colour.RESET);
                return;
            }
        }


        // Proceed with application
        Application newApplication = new Application(project, officer);
        newApplication.setFlatType(selectedFlatType);
        super.applicationList.addApplication(newApplication);
        officer.addApplication(newApplication);
        officer.setCurrentApplication(newApplication);
        System.out.println(Colour.GREEN + "Application submitted successfully!" + Colour.RESET);
    }
}
