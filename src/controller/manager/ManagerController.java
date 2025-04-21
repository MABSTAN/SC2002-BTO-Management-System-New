package controller.manager;

import controller.PasswordService;
import controller.manager.helper.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import entity.*;
import utils.BackButton;
import utils.ClearScreen;
import utils.Colour;
import container.*;
import controller.manager.template.*;
import controller.FilterMenu;

/**
 * Controls the main menu interactions for a manager user in the BTO system.
 * <p>
 * Handles creation and modification of projects, as well as reviewing and responding to
 * applications, registrations, enquiries, withdrawals, and generating booking reports.
 */
public class ManagerController {

    private Manager manager;
    private ProjectList projectList;
    private ApplicationList applicationList;
    private RegistrationList registrationList;
    private WithdrawalList withdrawalList;
    private EnquiryList enquiryList;

    private IManagerManageProject projectManager;
    private IManagerManageApplication applicationManager;
    private IManagerManageRegistration registrationManager;
    private IManagerManageWithdrawal withdrawalManager;
    private IManagerGenerateReport reportGenerator;
    private IManagerManageEnquiries enquiryManager;

    /**
     * Constructs a manager controller for handling administrative operations.
     *
     * @param manager           the manager currently logged in
     * @param projectList       the list of all BTO projects
     * @param applicationList   the list of applications submitted by applicants
     * @param registrationList  the list of officer registrations
     * @param withdrawalList    the list of withdrawal requests
     * @param enquiryList       the list of enquiries submitted by users
     */
    public ManagerController(Manager manager, ProjectList projectList, ApplicationList applicationList,
                             RegistrationList registrationList, WithdrawalList withdrawalList, EnquiryList enquiryList) {
        this.manager = manager;
        this.projectList = projectList;
        this.applicationList = applicationList;
        this.registrationList = registrationList;
        this.withdrawalList = withdrawalList;
        this.enquiryList = enquiryList;

        this.projectManager = new ManagerManageProject(manager, projectList, applicationList, registrationList);
        this.applicationManager = new ManagerManageApplication(applicationList);
        this.registrationManager = new ManagerManageRegistration(registrationList);
        this.withdrawalManager = new ManagerManageWithdrawal(withdrawalList, applicationList);
        this.reportGenerator = new ManagerGenerateReport(applicationList);
        this.enquiryManager = new ManagerManageEnquiries(enquiryList);
    }

    /**
     * Displays and manages the main dashboard menu for the manager.
     * <p>
     * Options include creating/editing/deleting projects, managing applications,
     * registrations, withdrawals, replying to enquiries, generating reports, and updating filters.
     */
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("            +----------------------------------------------------+");
            System.out.println("            |                Manager Dashboard                   |");
            System.out.println("            +----------------------------------------------------+");
            System.out.println("            |  1) Create Project                                 |");
            System.out.println("            |  2) Edit Project                                   |");
            System.out.println("            |  3) Delete Project                                 |");
            System.out.println("            |  4) Change Project Visibility                      |");
            System.out.println("            |  5) View Own Projects                              |");
            System.out.println("            |  6) View All Created Projects                      |");
            System.out.println("            |  7) View Applicant Applications                    |");
            System.out.println("            |  8) Manage Applicant Application                   |");
            System.out.println("            |  9) View Officer Registrations                     |");
            System.out.println("            | 10) Manage Officer Registrations                   |");
            System.out.println("            | 11) Manage Applicant Withdrawal                    |");
            System.out.println("            | 12) Generate Report                                |");
            System.out.println("            | 13) View All Enquiries                             |");
            System.out.println("            | 14) View Enquiries for Managed Projects            |");
            System.out.println("            | 15) Reply to Enquiry                               |");
            System.out.println("            | 16) Change Password                                |");
            System.out.println("            | 17) Manage Filters                                 |");
            System.out.println("            | 18) Logout                                         |");
            System.out.println("            +----------------------------------------------------+\n\n");
            System.out.print("  Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                ClearScreen.clear();
                System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            scanner.nextLine(); // consume newline
            ClearScreen.clear();

            switch (choice) {
                case 1: projectManager.createProject(); break;
                case 2: projectManager.editProject(); break;
                case 3: projectManager.deleteProject(); break;
                case 4: projectManager.changeProjectVisibility(); break;
                case 5: projectManager.viewOwnProject(); break;
                case 6: projectManager.viewAllCreatedProject(); break;
                case 7: applicationManager.viewApplication(manager.getActiveProject()); break;
                case 8: applicationManager.manageApplication(manager.getActiveProject()); break;
                case 9: registrationManager.viewRegistration(); break;
                case 10:
                    Project activeProject = manager.getActiveProject();
                    if (activeProject == null) {
                        System.out.println(Colour.RED + "You are not currently handling any active project." + Colour.RESET);
                    } else {
                        registrationManager.manageRegistration(activeProject);
                    }
                    break;
                case 11: withdrawalManager.manageWithdrawal(manager.getActiveProject()); break;
                case 12: reportGenerator.generateReport(); break;
                case 13: enquiryManager.viewEnquiry(); break;
                case 14: enquiryManager.viewHandledProjectEnquiry(manager.getActiveProject()); break;
                case 15: enquiryManager.replyHandledProjectEnquiry(manager.getActiveProject()); break;
                case 16:
                    PasswordService.changePassWord(manager);
                    return;
                case 17:
                    new FilterMenu().manageFilters();
                    ClearScreen.clear();
                    break;
                case 18:
                    System.out.println(Colour.BLUE + "Logging out..." + Colour.RESET);
                    BackButton.goBack();
                    return;
                default:
                    System.out.println(Colour.RED + "Invalid choice! Please enter a valid option." + Colour.RESET);
            }

            BackButton.goBack();
        }
    }
}
