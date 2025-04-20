package controller.applicant.helper;
import container.*;
import entity.*;
import utils.BackButton;
import utils.Colour;
import utils.ClearScreen;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import controller.applicant.template.IApplicantMakeEnquiry;;
public class ApplicantMakeEnquiry implements IApplicantMakeEnquiry{
    private Applicant applicant;
    private ProjectList projectList;
    private EnquiryList enquiryList;
    private Scanner sc;

    public ApplicantMakeEnquiry(Applicant applicant, ProjectList projectList, EnquiryList enquiryList){
        this.applicant = applicant;
        this.projectList = projectList;
        this.enquiryList = enquiryList;
        this.sc = new Scanner(System.in);
    }

    public void makeEnquiry(){
        int index = 1;
        System.out.println("What project you want to enquire?");
        for (Project project : projectList.getProjectList()){
            System.out.println(index + ")" + project.getProjectName());
            index++;
        }

        System.out.print("Enter the project number: ");
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

        Project project = projectList.getProjectList().get(choice-1);

        System.out.print("Enter your message: ");
        String message = sc.nextLine();

        Enquiry enquiry = new Enquiry(applicant, project, message);
        enquiryList.addEnquiry(enquiry);

        System.out.println(Colour.GREEN + "\nSuccess Enquiry!" + Colour.RESET);
        // BackButton.goBack();
    }

    public void viewEnquiry(){
        System.out.println("\n===== My Enquiries =====");
        boolean found = false;
        int index = 1;

        for (Enquiry enquiry : enquiryList.getEnquiries()) {
            if (enquiry.getApplicant().equals(applicant)) {
                System.out.println(index + ") " + enquiry);
                found = true;
                index++;
            }
        }
        if(found == false){
            System.out.println(Colour.RED + "No enquiries found." + Colour.RESET);
        }

        // BackButton.goBack();
    }

    public void editEnquiry(){
        List<Enquiry> applicantEnquiries = new ArrayList<>();
        int index = 1;

        System.out.println("\n===== Select Enquiry to Edit =====");
        for (Enquiry enquiry : enquiryList.getEnquiries()) {
            if (enquiry.getApplicant().equals(applicant) && enquiry.getStatus() == Enquiry.EnquiryStatus.PENDING) {
                System.out.println(index + ") " + enquiry);
                applicantEnquiries.add(enquiry);
                index++;
            }
        }

        if (applicantEnquiries.isEmpty()) {
            System.out.println(Colour.YELLOW + "No pending enquiries to edit." + Colour.RESET);
            // BackButton.goBack();
            return;
        }

        System.out.print("Enter enquiry number to edit: ");
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

        if (choice < 1 || choice > applicantEnquiries.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            // BackButton.goBack();
            return;
        }

        System.out.print("Enter new message: ");
        String message = sc.nextLine();
        applicantEnquiries.get(choice - 1).setMessage(message);

        System.out.println(Colour.GREEN + "Enquiry updated." + Colour.RESET);
        // BackButton.goBack();
    }

    public void deleteEnquiry() {
        List<Enquiry> applicantEnquiries = new ArrayList<>();
        int index = 1;

        System.out.println("\n===== Select Enquiry to Delete =====");
        for (Enquiry enquiry : enquiryList.getEnquiries()) {
            if (enquiry.getApplicant().equals(applicant) && enquiry.getStatus() == Enquiry.EnquiryStatus.PENDING) {
                System.out.println(index + ") " + enquiry);
                applicantEnquiries.add(enquiry);
                index++;
            }
        }

        if (applicantEnquiries.isEmpty()) {
            System.out.println(Colour.RED + "No pending enquiries to delete." + Colour.RESET);
            // BackButton.goBack();
            return;
        }

        System.out.print("Enter enquiry number to delete: ");
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

        if (choice < 1 || choice > applicantEnquiries.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            // BackButton.goBack();
            return;
        }

        enquiryList.removeEnquiry(applicantEnquiries.get(choice - 1));
        System.out.println(Colour.GREEN + "Enquiry deleted." + Colour.RESET);
        // BackButton.goBack();
    }


}
