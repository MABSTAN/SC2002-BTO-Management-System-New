package controller.officer.helper;
import container.*;
import entity.*;
import utils.Colour;
import controller.officer.template.IOfficerManageProject;
public class OfficerManageProject implements IOfficerManageProject{
    private Officer officer;
    private ProjectList projectList;

    public OfficerManageProject(Officer officer, ProjectList projectList) {
        this.officer = officer;
        this.projectList = projectList;
    }

    public void viewProjectDetails() {
        Project assignedProject = officer.getAssignedProject();

        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        System.out.println("\n===== Project Details =====");
        System.out.println(assignedProject.toString());
    }
}
