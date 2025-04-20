package controller.officer;

import controller.officer.helper.*;
import controller.PasswordService;
import controller.applicant.helper.*;
import container.*;
import entity.*;
import utils.BackButton;
import utils.ClearScreen;
import java.util.InputMismatchException;
import java.util.Scanner;
import controller.officer.template.*;
import controller.applicant.template.*;

/**
 * Handles all officer-related operations in the system, including registration,
 * project management, and applicant-like interactions (e.g. submitting enquiries and applications).
 */
public class OfficerController {

    private Officer officer;
    private ProjectList projectList;
    private ApplicationList applicationList;
    private EnquiryList enquiryList;
    private WithdrawalList withdrawalList;
    private RegistrationList registrationList;

    private Scanner scanner;

    private IOfficerViewProjects projectHandler;
    private IOfficerMakeEnquiry enquiryHandler;
    private IApplicantViewApplication applicationHandler;
    private IApplicantMakeWithdrawal withdrawalHandler;

    private IOfficerRegistration registrationHandler;
    private IOfficerManageEnquiries manageEnquiriesHandler;
    private IOfficerManageProject manageProjectHandler;
    private IOfficerManageApplication manageApplicationHandler;

    /**
     * Constructs the officer controller and initializes all handlers for both
     * officer-specific and applicant-style functionality.
     *
     * @param officer           the logged-in officer
     * @param projectList       list of all available projects
     * @param applicationList   list of all applications in the system
     * @param enquiryList       list of all submitted enquiries
     * @param withdrawalList    list of all withdrawal requests
     * @param registrationList  list of all officer registrations
     */
    public OfficerController(Officer officer, ProjectList projectList,
                             ApplicationList applicationList, EnquiryList enquiryList,
                             WithdrawalList withdrawalList, RegistrationList registrationList) {
        this.officer = officer;
        this.projectList = projectList;
        this.applicationList = applicationList;
        this.enquiryList = enquiryList;
        this.withdrawalList = withdrawalList;
        this.registrationList = registrationList;
        this.scanner = new Scanner(System.in);

        // Shared applicant-like functionality
        this.projectHandler = new OfficerViewProjects(officer, projectList, applicationList, registrationList);
        this.enquiryHandler = new OfficerMakeEnquiry(officer, projectList, enquiryList);
        this.applicationHandler = new ApplicantViewApplication(officer, applicationList);
        this.withdrawalHandler = new ApplicantMakeWithdrawal(officer, withdrawalList, applicationList);

        // Officer-specific functionality
        this.registrationHandler = new OfficerRegistration(officer, projectList, registrationList, applicationList);
        this.manageEnquiriesHandler = new OfficerManageEnquiries(officer, enquiryList);
        this.manageProjectHandler = new OfficerManageProject(officer, projectList);
        this.manageApplicationHandler = new OfficerManageApplication(officer, applicationList);
    }

    /**
     * Displays the main dashboard for officer users and handles routing to different menus.
     */
    public void showMenu() {
        int choice = 0;
        do {
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |              Officer Dashboard                   |");
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |  1) Apply for BTO Project (as Applicant)         |");
            System.out.println("            |  2) Register for Project (as Officer)            |");
            System.out.println("            |  3) Manage Officer's Job                         |");
            System.out.println("            |  4) Change Password                              |");
            System.out.println("            |  5) Logout                                       |");
            System.out.println("            +--------------------------------------------------+\n\n");
            System.out.print("Enter choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                ClearScreen.clear();
                System.out.println("Please input an integer!");
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            ClearScreen.clear();

            switch (choice) {
                case 1: showApplicantMenu(); break;
                case 2: showOfficerRegistrationMenu(); break;
                case 3: showOfficerManagementMenu(); break;
                case 4:
                    PasswordService.changePassWord(officer);
                    BackButton.goBack();
                    return;
                case 5:
                    System.out.println("Logging out...");
                    BackButton.goBack();
                    break;
                default:
                    System.out.println("Invalid choice! Please choose a valid option");
                    BackButton.goBack();
            }
        } while (choice != 5);
    }

    /**
     * Displays the officer's applicant-mode menu, allowing access to enquiry, application,
     * and project-browsing functionality.
     */
    private void showApplicantMenu() {
        int choice = 0;
        do {
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |           Applicant Mode (Officer View)         |");
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |  1) View BTO Project List                       |");
            System.out.println("            |  2) Apply for a BTO Project                     |");
            System.out.println("            |  3) View My Application                         |");
            System.out.println("            |  4) Withdraw Application                        |");
            System.out.println("            |  5) Submit an Enquiry                           |");
            System.out.println("            |  6) View My Enquiries                           |");
            System.out.println("            |  7) Edit an Enquiry                             |");
            System.out.println("            |  8) Delete an Enquiry                           |");
            System.out.println("            |  9) Back                                        |");
            System.out.println("            +--------------------------------------------------+\n\n");
            System.out.print("Enter choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                ClearScreen.clear();
                System.out.println("Please input an integer!");
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            ClearScreen.clear();

            switch (choice) {
                case 1: projectHandler.viewProjectList(); break;
                case 2: projectHandler.applyForProject(); break;
                case 3: applicationHandler.viewApplicationStatus(); break;
                case 4: withdrawalHandler.withdrawApplication(); break;
                case 5: enquiryHandler.makeEnquiry(); break;
                case 6: enquiryHandler.viewEnquiry(); break;
                case 7: enquiryHandler.editEnquiry(); break;
                case 8: enquiryHandler.deleteEnquiry(); break;
                case 9:
                    ClearScreen.clear();
                    return;
                default:
                    ClearScreen.clear();
                    System.out.println("Invalid choice! Please choose a valid option");
            }
            BackButton.goBack();
        } while (true);
    }

    /**
     * Allows the officer to register for a new project or check the status of current registrations.
     */
    private void showOfficerRegistrationMenu() {
        int choice = 0;
        do {
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |              Officer Registration                |");
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |  1) Register as Officer                          |");
            System.out.println("            |  2) View Registration Status                     |");
            System.out.println("            |  3) Back                                         |");
            System.out.println("            +--------------------------------------------------+\n\n");
            System.out.print("Enter choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                ClearScreen.clear();
                System.out.println("Please input an integer!");
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            ClearScreen.clear();

            switch (choice) {
                case 1: registrationHandler.registerForProject(); break;
                case 2: registrationHandler.viewRegistrationStatus(); break;
                case 3:
                    ClearScreen.clear();
                    return;
                default:
                    ClearScreen.clear();
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
            BackButton.goBack();
        } while (true);
    }

    /**
     * Displays the officer’s management dashboard for viewing enquiries,
     * project details, and updating successful applications.
     * Only accessible if the officer is currently assigned to a project.
     */
    private void showOfficerManagementMenu() {
        if (officer.getAssignedProject() == null) {
            System.out.println("You do not have an active project. Cannot manage officer responsibilities.");
            BackButton.goBack();
            return;
        }

        int choice = 0;
        do {
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |           Officer Project Management            |");
            System.out.println("            +--------------------------------------------------+");
            System.out.println("            |  1) View Enquiries                              |");
            System.out.println("            |  2) Reply Enquiry                               |");
            System.out.println("            |  3) View Project Details                        |");
            System.out.println("            |  4) View Applications                           |");
            System.out.println("            |  5) Update Applicant Profile                    |");
            System.out.println("            |  6) Back                                        |");
            System.out.println("            +--------------------------------------------------+\n\n");
            System.out.print("Enter choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                ClearScreen.clear();
                System.out.println("Please input an integer!");
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            ClearScreen.clear();

            switch (choice) {
                case 1: manageEnquiriesHandler.viewEnquiries(); break;
                case 2: manageEnquiriesHandler.replyToEnquiry(); break;
                case 3: manageProjectHandler.viewProjectDetails(); break;
                case 4: manageApplicationHandler.viewApplications(); break;
                case 5: manageApplicationHandler.updateApplicationStatus(); break;
                case 6:
                    ClearScreen.clear();
                    return;
                default:
                    ClearScreen.clear();
                    System.out.println("Invalid choice! Please choose a valid option.");
            }
            BackButton.goBack();
        } while (true);
    }
}

