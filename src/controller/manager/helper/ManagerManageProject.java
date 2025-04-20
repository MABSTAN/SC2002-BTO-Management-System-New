package controller.manager.helper;
import entity.*;
import utils.BackButton;
import utils.ClearScreen;
import utils.Colour;
import container.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import controller.manager.template.IManagerManageProject;
public class ManagerManageProject implements IManagerManageProject{
    private Manager manager;
    private ProjectList projectList;
    private ApplicationList applicationList;
    private RegistrationList registrationList;

    private Scanner scanner;

    public ManagerManageProject(Manager manager, ProjectList projectList, ApplicationList applicationList, RegistrationList registrationList) {
        this.manager = manager;
        this.projectList = projectList;
        this.applicationList = applicationList;
        this.registrationList = registrationList;
        this.scanner = new Scanner(System.in);
    }

    public void createProject() {
        if (manager.getActiveProject() != null) {
            System.out.println(Colour.RED + "You are already handling an active project: " + Colour.RESET + manager.getActiveProject().getProjectName());
            System.out.println("You must finish managing the current project before creating a new one!");
            return;
        }

        String name;
         while (true) {
             System.out.print("Enter Project Name: ");
             name = scanner.nextLine();
             if (projectList.getProjectByName(name) != null) {
                 System.out.println(Colour.RED + "A project with this name already exists. Please enter a different name." + Colour.RESET);
             } else {break;}
         }

        System.out.print("Enter Neighborhood: ");
        String neighborhood = scanner.nextLine();

        int twoRoom = 0, sellingPriceTwoRoom = 0, threeRoom = 0, sellingPriceThreeRoom = 0;
        boolean validInput = false;

        // Handle integer inputs with retry
        while (!validInput) {
            System.out.print("Enter number of 2-Room Flats: ");
            String input = scanner.nextLine();
            try {
                twoRoom = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please input a valid integer!" + Colour.RESET);
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.print("Enter the selling price of 2-Room Flats: ");
            String input = scanner.nextLine();
            try {
                sellingPriceTwoRoom = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please input a valid integer!" + Colour.RESET);
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.print("Enter number of 3-Room Flats: ");
            String input = scanner.nextLine();
            try {
                threeRoom = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please input a valid integer!" + Colour.RESET);
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.print("Enter the selling price of 3-Room Flats: ");
            String input = scanner.nextLine();
            try {
                sellingPriceThreeRoom = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please input a valid integer!" + Colour.RESET);
            }
        }

        Date openDate = null, closeDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        validInput = false;
        while (!validInput) {
            System.out.print("Enter Opening Date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            try {
                openDate = sdf.parse(dateInput);
                validInput = true;
            } catch (ParseException e) {
                System.out.println(Colour.RED + "Invalid date format. Please use yyyy-MM-dd." + Colour.RESET);
            }
        }

        validInput = false;
        while (!validInput) {
            System.out.print("Enter Closing Date (yyyy-MM-dd): ");
            String dateInput = scanner.nextLine();
            try {
                closeDate = sdf.parse(dateInput);
                validInput = true;
            } catch (ParseException e) {
                System.out.println(Colour.RED + "Invalid date format. Please use yyyy-MM-dd." + Colour.RESET);
            }
        }

        validInput = false;
        int maxOfficer = 0;
        while (!validInput) {
            System.out.print("Enter Max Officer Slot: ");
            String input = scanner.nextLine();
            try {
                maxOfficer = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println(Colour.RED + "Please input a valid integer!" + Colour.RESET);
            }
        }

        Project project = new Project(name, neighborhood, twoRoom, sellingPriceTwoRoom, threeRoom, sellingPriceThreeRoom, openDate, closeDate, maxOfficer);
        project.setManager(manager);

        // Assign manager's responsibility
        manager.addManagedProject(project);
        if (project.getVisibility()) {
            manager.setActiveProject(project);
        }

        projectList.addProject(project);
        System.out.println(Colour.GREEN + "Project created successfully." + Colour.RESET);
    }

    public void editProject() {
        System.out.print("Enter project name to edit: ");
        String name = scanner.nextLine();

        Project project = projectList.getProjectByName(name);
        if (project == null || !manager.getManagedProjects().contains(project)) {
            System.out.println(Colour.RED + "Project not found or not owned by you." + Colour.RESET);
            return;
        }

        while (true) {
            System.out.println("            +------------------------------------------------+");
            System.out.println("            |            Select What to Edit                 |");
            System.out.println("            +------------------------------------------------+");
            System.out.println("            |  1) Neighborhood                               |");
            System.out.println("            |  2) 2-Room Units                               |");
            System.out.println("            |  3) Selling Price of 2-Room Units              |");
            System.out.println("            |  4) 3-Room Units                               |");
            System.out.println("            |  5) Selling Price of 3-Room Units              |");
            System.out.println("            |  6) Open Date                                  |");
            System.out.println("            |  7) Close Date                                 |");
            System.out.println("            |  8) Max Officer Slots                          |");
            System.out.println("            |  9) Exit                                       |");
            System.out.println("            +------------------------------------------------+\n\n");
            System.out.print("Enter choice: ");
            int choice;
            try{
                choice = scanner.nextInt();
            }
            catch(InputMismatchException e){
                ClearScreen.clear();
                System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }


            scanner.nextLine();
            ClearScreen.clear();
            switch (choice) {
                case 1:
                    System.out.print("New Neighborhood: ");
                    project.setNeighborhood(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("New 2-Room Units: ");
                    project.setAvailableTwoRoom(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 3:
                    System.out.print("New 2-Room Units Selling Price: ");
                    project.setSellingPriceTwoRoom(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 4:
                    System.out.print("New 3-Room Units: ");
                    project.setAvailableThreeRoom(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 5:
                    System.out.print("New 3-Room Units Selling Price: ");
                    project.setSellingPriceThreeRoom(scanner.nextInt());
                    scanner.nextLine();
                    break;

                case 6:
                    try {
                        System.out.print("New Opening Date (yyyy-MM-dd): ");
                        String openDateStr = scanner.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newOpenDate = sdf.parse(openDateStr);
                        project.setOpeningDate(newOpenDate);
                        System.out.println(Colour.GREEN + "Opening date updated." + Colour.RESET);
                    } catch (ParseException e) {
                        System.out.println(Colour.RED + "Invalid date format. Please use yyyy-MM-dd." + Colour.RESET);
                    }
                    break;

                case 7:
                    try {
                        System.out.print("New Closing Date (yyyy-MM-dd): ");
                        String closeDateStr = scanner.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date newCloseDate = sdf.parse(closeDateStr);
                        project.setClosingDate(newCloseDate);
                        System.out.println(Colour.GREEN + "Closing date updated." + Colour.RESET);
                    } catch (ParseException e) {
                        System.out.println(Colour.RED + "Invalid date format. Please use yyyy-MM-dd." + Colour.RESET);
                    }
                    break;

                case 8:
                    System.out.print("New Max Officer Slots: ");
                    project.setMaxOfficer(scanner.nextInt());
                    scanner.nextLine();
                    break;
                case 9:
                    System.out.println(Colour.GREEN + "Edit complete." + Colour.RESET);
                    return;
                default:
                    System.out.println(Colour.RED + "Invalid choice! Please enter a valid option." + Colour.RESET);
            }
        }
    }

    public void deleteProject() {
        System.out.print("Enter project name to delete: ");
        String name = scanner.nextLine();

        Project project = projectList.getProjectByName(name);
        if (project == null || !manager.getManagedProjects().contains(project)) {
            System.out.println(Colour.RED + "Project not found or not managed by you." + Colour.RESET);
            return;
        }

        // 1. Remove related applications
        applicationList.removeApplicationsByProject(project);

        // 2. Remove related registrations
        registrationList.removeRegistrationByProject(project);

        // 3. Clear officer project references
        for (Officer officer : project.getOfficers()) {
            if (officer.getAssignedProject() != null && officer.getAssignedProject().equals(project)) {
                officer.setAssignedProject(null);
            }
            officer.getManagedProjects().remove(project);
        }

        // 4. Clear manager references
        Manager manager = project.getManager();
        if (manager != null) {
            manager.getManagedProjects().remove(project);
            if (manager.getActiveProject() != null && manager.getActiveProject().equals(project)) {
                manager.setActiveProject(null);
            }
        }

        // 5. Remove project from ProjectList
        projectList.removeProject(project);
    }

    public void changeProjectVisibility() {
        System.out.print("Enter project name to change visibility: ");
        String name = scanner.nextLine();

        Project project = projectList.getProjectByName(name);
        if (project == null || !manager.getManagedProjects().contains(project)) {
            System.out.println(Colour.RED + "Project not found or not managed by you." + Colour.RESET);
            return;
        }

        project.setVisibility(!project.getVisibility());
        System.out.println(Colour.GREEN + "Visibility updated: " + (project.getVisibility() ? "Visible" : "Hidden" ) + Colour.RESET);
    }

    public void viewOwnProject() {
        ArrayList<Project> myProjects = manager.getManagedProjects();
        if (myProjects.isEmpty()) {
            System.out.println(Colour.RED + "You have not created any projects." + Colour.RESET);
            return;
        }

        System.out.println("\nYour Projects:");
        for (Project project : myProjects) {
            System.out.println(project.toString());
        }
    }

    public void viewAllCreatedProject() {
        System.out.println("\nAll Projects in the System:");
        for (Project project : projectList.getProjectList()) {
            System.out.println(project.toString());
        }
    }
}
