package controller.applicant.helper;
import container.*;
import entity.*;
import utils.Colour;
import controller.applicant.template.IApplicantViewApplication;
public class ApplicantViewApplication implements IApplicantViewApplication{
    private Applicant applicant;
    private ApplicationList applicationList;
    public ApplicantViewApplication(Applicant applicant, ApplicationList applicationList){
        this.applicant = applicant;
        this.applicationList = applicationList;
        }

    public void viewApplicationStatus(){
        Application application = applicationList.getApplicationByApplicant(applicant);
        if (application != null){
            System.out.println("Project Name: " + application.getProject().getProjectName());
            System.out.println("Flat Type: " + application.getFlatType());
            System.out.println("Status: " + application.getApplicationStatus());
        }
        else{
            System.out.println(Colour.RED + "No Application found." + Colour.RESET);
        }

    }

}
