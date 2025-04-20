package interact;
import container.*;
import entity.*;
import utils.BackButton;
import utils.ClearScreen;
import utils.DataSyncUtil;
import utils.Colour;
import controller.*;
import controller.applicant.ApplicantController;
import controller.officer.OfficerController;
import controller.manager.ManagerController;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenu {
    public static void main(String[] args) {
        // Catch uncaught exceptions globally and save data before crashing
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.println(Colour.RED + "An unexpected error occurred: " + throwable.getMessage() + Colour.RESET);

            try {
                DataSyncUtil syncUtil = new DataSyncUtil(
                    DataInitializer.getApplicantList(),
                    DataInitializer.getProjectList(),
                    DataInitializer.getManagerList(),
                    DataInitializer.getOfficerList(),
                    DataInitializer.getApplicationList(),
                    DataInitializer.getRegistrationList(),
                    DataInitializer.getWithdrawalList(),
                    DataInitializer.getEnquiryList()
                );
                syncUtil.saveAll();
                System.out.println(Colour.GREEN + " All data saved before crash." + Colour.RESET);
            } catch (Exception e) {
                System.out.println(Colour.RED + "Failed to save data during crash: " + e.getMessage() + Colour.RESET);
            }

            System.exit(1);
        });

        DataInitializer.loadData(); // Load users from CSV
        ApplicantList applicantList = DataInitializer.getApplicantList();
        ManagerList managerList = DataInitializer.getManagerList();
        OfficerList officerList = DataInitializer.getOfficerList();
        ProjectList projectList = DataInitializer.getProjectList();
        ApplicationList applicationList = DataInitializer.getApplicationList();
        RegistrationList registrationList = DataInitializer.getRegistrationList();
        WithdrawalList withdrawalList = DataInitializer.getWithdrawalList();
        EnquiryList enquiryList = DataInitializer.getEnquiryList();
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DataSyncUtil syncUtil = new DataSyncUtil(
                DataInitializer.getApplicantList(),
                DataInitializer.getProjectList(),
                DataInitializer.getManagerList(),
                DataInitializer.getOfficerList(),
                DataInitializer.getApplicationList(),
                DataInitializer.getRegistrationList(),
                DataInitializer.getWithdrawalList(),
                DataInitializer.getEnquiryList()
            );
            syncUtil.saveAll();
            System.out.println(Colour.GREEN + "All data saved before shutdown." + Colour.RESET);
        }));

        Scanner scanner = new Scanner(System.in);
        ClearScreen.clear();
        
        while (true) {
            System.out.println(Colour.BLUE + "  ____ _______ ____    __  __                                                   _   ");
            System.out.println(" |  _ \\__   __/ __ \\  |  \\/  |                                                 | |  ");
            System.out.println(" | |_) | | | | |  | | | \\  / | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ ");
            System.out.println(" |  _ <  | | | |  | | | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|");
            System.out.println(" | |_) | | | | |__| | | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ ");
            System.out.println(" |____/  |_|  \\____/  |_|  |_|\\__,_|_| |_|\\__,_|\\__,_|\\___|_| |_| |_|\\___|_| |_|\\__|");
            System.out.println("                                                __/ |                               ");
            System.out.println("                                               |___/                                " + Colour.RESET);
            System.out.println("                    +----------------------------------------+");
            System.out.println("                    |  1) Applicant Login                    |");
            System.out.println("                    |  2) Officer Login                      |");
            System.out.println("                    |  3) Manager Login                      |");
            System.out.println("                    |  4) Exit                               |");
            System.out.println("                    +----------------------------------------+");
            int choice;
            String nric, password;
            System.out.print("Enter choice: ");
            try{
                choice = scanner.nextInt();
                if (choice == 4) {

                    DataSyncUtil syncUtil = new DataSyncUtil(
                        applicantList,
                        projectList,
                        managerList,
                        officerList,
                        applicationList,
                        registrationList,
                        withdrawalList,
                        enquiryList
                    );
                    syncUtil.saveAll(); 
                    ClearScreen.clear();
                    System.out.println("Bye Bye!");
                    break;
                    }
                scanner.nextLine();
            }
            catch(InputMismatchException e){
                ClearScreen.clear();
                System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
                BackButton.goBack();
                scanner.nextLine();
                continue;
            }
            // Input and validate NRIC:
            System.out.print("Enter NRIC: ");
            nric = scanner.nextLine().trim().toUpperCase();
            if(!AuthenticationService.validNRIC(nric)){ 
                ClearScreen.clear();
                System.out.println(Colour.RED + "Invalid NRIC" + Colour.RESET);
                BackButton.goBack();
                continue;
            }

            // Input password:
            System.out.print("Enter Password: ");
            password = scanner.nextLine().trim();

            // Check account's availability:
            User user = AuthenticationService.authenticate(nric, password,choice);
            if (user != null) {
                UserSession.setCurrentUser(user);
                ClearScreen.clear();
                if (user instanceof Officer) {
                    new OfficerController((Officer) user, projectList, applicationList, enquiryList, withdrawalList, registrationList).showMenu();
                }
                else if (user instanceof Applicant) {
                    new ApplicantController((Applicant) user, projectList, applicationList, enquiryList, withdrawalList).showMenu();
                }
                else if (user instanceof Manager) {
                    new ManagerController((Manager) user, projectList, applicationList, registrationList, withdrawalList, enquiryList).showMenu();
                }
            }
            else {
                ClearScreen.clear();
                System.out.println(Colour.RED + "Invalid credentials. Please try again." + Colour.RESET);
                BackButton.goBack();
            }
        }
        scanner.close();
    }
}
