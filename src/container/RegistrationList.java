package container;
import entity.*;
import utils.CSVReader;
import utils.CSVWriter;
import utils.Colour;

import java.util.*;

public class RegistrationList {
    private ArrayList<Registration> registrationList;

    private OfficerList officerList;
    private ProjectList projectList;

    public RegistrationList() {
        this.registrationList = new ArrayList<>();
    }

    public RegistrationList(String filepath, OfficerList officerList, ProjectList projectList){
        this.registrationList = new ArrayList<>();
        this.officerList = officerList;
        this.projectList = projectList;
        loadRegistrations(filepath);
    }

    private void loadRegistrations(String filePath) {
    List<String[]> data = CSVReader.readCSV(filePath);
    for(String[] row : data) {
        String officerNric = row[0];
        String projectName = row[1];
        String status = row[2]; 
        
        Officer officer = officerList.getOfficerByNric(officerNric);
        Project project = projectList.getProjectByName(projectName);

        if (officer != null && project != null) {
            Registration registration = new Registration(officer, project);
            registration.setStatus(Registration.RegistrationStatus.valueOf(status));
            registrationList.add(registration);
            }
        }
    }

    public void addRegistration(Registration registration) {
        registrationList.add(registration);
    }

    public ArrayList<Registration> getRegistrations() {
        return registrationList;
    }

    public ArrayList<Registration> getRegistrationsByOfficer(Officer officer) {
        ArrayList<Registration> result = new ArrayList<>();
        for (Registration reg : registrationList) {
            if (reg.getOfficer().equals(officer)) {
                result.add(reg);
            }
        }
        return result;
    }

    public Registration getRegistrationByOfficerAndProject(Officer officer, Project project) {
        for (Registration reg : registrationList) {
            if (reg.getOfficer().equals(officer) && reg.getProject().equals(project)) {
                return reg;
            }
        }
        return null;
    }

    public void removeRegistrationByProject(Project project) {
        registrationList.removeIf(reg -> reg.getProject().equals(project));
        System.out.println(Colour.GREEN + "All registrations for project '" + project.getProjectName() + "' have been removed." + Colour.RESET);
    }

    public ArrayList<Registration> getPendingRegistrationsByProject(Project project) {
        ArrayList<Registration> pendingList = new ArrayList<>();
        for (Registration registration : registrationList) {
            if (registration.getProject().equals(project) &&
                registration.getStatus() == Registration.RegistrationStatus.PENDING) {
                pendingList.add(registration);
            }
        }
        return pendingList;
    }

    public void saveToCSV() {
    List<String[]> data = new ArrayList<>();
    data.add(new String[]{"officerNric", "projectName", "status"});

    for (Registration r : this.registrationList) {
        data.add(new String[]{
            r.getOfficer().getNric(),
            r.getProject().getProjectName(),
            r.getStatus().name()
        });
    }

    CSVWriter.writeCSV("../data/RegistrationList.csv", data);
}
}