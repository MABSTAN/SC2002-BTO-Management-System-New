package controller;
import container.*;
public class DataInitializer {
    // Declare at the class level (accessible in all methods)
    private static ApplicantList applicantList;
    private static OfficerList officerList;
    private static ManagerList managerList;
    private static ProjectList projectList;
    private static ApplicationList applicationList;
    private static RegistrationList registrationList;
    private static WithdrawalList withdrawals;
    private static EnquiryList enquiries;

    public static void loadData() {
        // Initialize lists by loading from CSV
        applicantList = new ApplicantList("../data/ApplicantList.csv");
        officerList = new OfficerList("../data/OfficerList.csv");
        managerList = new ManagerList("../data/ManagerList.csv");
        projectList = new ProjectList("../data/ProjectList.csv", managerList, officerList);
        applicationList = new ApplicationList("../data/ApplicationList.csv", applicantList, projectList);
        registrationList = new RegistrationList("../data/RegistrationList.csv", officerList, projectList);
        withdrawals = new WithdrawalList("../data/WithdrawalList.csv", applicationList, applicantList, projectList);
        enquiries = new EnquiryList("../data/EnquiryList.csv", applicantList, projectList);
    }

    // Getter methods to retrieve data
    public static ApplicantList getApplicantList() {
        return applicantList;
    }

    public static OfficerList getOfficerList() {
        return officerList;
    }

    public static ManagerList getManagerList() {
        return managerList;
    }

    public static ProjectList getProjectList() {
        return projectList;
    }

    public static ApplicationList getApplicationList() {
        return applicationList;
    }

    public static RegistrationList getRegistrationList() {
        return registrationList;
    }

    public static WithdrawalList getWithdrawalList() {
        return withdrawals;
    }

    public static EnquiryList getEnquiryList() {
        return enquiries;
    }
}