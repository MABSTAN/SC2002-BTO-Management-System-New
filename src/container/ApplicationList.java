package container;
import entity.*;
import utils.CSVReader;
import utils.CSVWriter;
import utils.Colour;

import java.util.*;

public class ApplicationList {
    private ArrayList<Application> applicationList;
    
    private ApplicantList applicantList;
    private ProjectList projectList;

    public ApplicationList(String filepath, ApplicantList applicantList, ProjectList projectList) {
        this.applicationList = new ArrayList<>();
        this.applicantList = applicantList;
        this.projectList = projectList;
        loadApplications(filepath);
    }

    private void loadApplications(String filePath) {
        List<String[]> data = CSVReader.readCSV(filePath);
        for (String[] row : data) {
            if (row[0].equalsIgnoreCase("applicantNric")) continue; // skip header
    
            String nric = row[0];
            String projectName = row[1];
            String statusStr = row[2];
    
            Applicant applicant = applicantList.getApplicantByNric(nric);
            Project project = projectList.getProjectByName(projectName);
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(statusStr);
    
            if (applicant != null && project != null) {
                Application application = new Application(project, applicant);
                application.setApplicationStatus(status);
                this.applicationList.add(application);
                applicant.setCurrentApplication(application); // optional
                } 
            else {
                System.out.println("Could not find Applicant or Project for: " + nric + " / " + projectName);
                }
            }
        }


    public void addApplication(Application application){
        this.applicationList.add(application);
    }

    public void removeApplication(Application application) {
        if (applicationList.remove(application)) {
            System.out.println(Colour.GREEN + "Application removed successfully." + Colour.RESET);
        } else {
            System.out.println(Colour.RED + "Application not found." + Colour.RESET);
        }
    }

    public ArrayList<Application> getApplicationList(){
        return applicationList;
    }

    public Application getApplicationByApplicant(Applicant applicant) {
        for (Application application : applicationList) {
            if (application.getApplicant().equals(applicant)) {
                return application;
            }
        }
        return null; // No existing application
    }

    public ArrayList<Application> getApplicationsByProject(Project project) {
        ArrayList<Application> projectApplication = new ArrayList<>();
        for (Application application : applicationList) {
            if (application.getProject().equals(project)) {
                projectApplication.add(application);
            }
        }
        return projectApplication; // No existing application
    }

    public ArrayList<Application> getPendingApplicationsByProject(Project project) {
        ArrayList<Application> pendingApplications = new ArrayList<>();
        for (Application application : applicationList) {
            if (application.getProject().equals(project) &&
                application.getApplicationStatus() == Application.ApplicationStatus.PENDING) {
                pendingApplications.add(application);
            }
        }
        return pendingApplications;
    }


    public void removeApplicationsByProject(Project project) {
        for (Application application : new ArrayList<>(applicationList)) {
            if (application.getProject().equals(project)) {
                // Clear applicant reference
                Applicant applicant = application.getApplicant();
                if (applicant != null) {
                    applicant.setCurrentApplication(null); // Only if such a setter exists
                }
                this.removeApplication(application);
            }
        }
    }

    public ArrayList<Application> getSuccessfulApplications() {
        ArrayList<Application> successful = new ArrayList<>();
        for (Application application : applicationList) {
            if (application.getApplicationStatus() == Application.ApplicationStatus.SUCCESSFUL) {
                successful.add(application);
            }
        }
        return successful;
    }

    public void saveToCSV() {
    List<String[]> data = new ArrayList<>();
    data.add(new String[]{"applicantNric", "projectName", "status"});

    for (Application a : this.applicationList) {
        data.add(new String[]{
            a.getApplicant().getNric(),
            a.getProject().getProjectName(),
            a.getApplicationStatus().name()
        });
    }

    CSVWriter.writeCSV("../data/ApplicationList.csv", data);
}
}