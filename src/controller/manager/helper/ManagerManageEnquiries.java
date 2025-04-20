package controller.manager.helper;
import java.util.ArrayList;
import java.util.Scanner;
import entity.*;
import utils.Colour;
import container.*;
import controller.manager.template.IManagerManageEnquiries;
public class ManagerManageEnquiries implements IManagerManageEnquiries{
    private EnquiryList enquiryList;
    private Scanner scanner;

    public ManagerManageEnquiries(EnquiryList enquiryList) {
        this.enquiryList = enquiryList;
        this.scanner = new Scanner(System.in);
    }

    // View all enquiries (not restricted to managed projects)
    public void viewEnquiry() {
        ArrayList<Enquiry> enquiries = enquiryList.getEnquiries();
        if (enquiries.isEmpty()) {
            System.out.println(Colour.RED + "No enquiries found." + Colour.RESET);
            return;
        }

        System.out.println("===== All Enquiries =====");
        for (Enquiry enquiry : enquiries) {
            System.out.println(enquiry);
        }
    }

    // View enquiries only for the project the manager is currently handling
    public void viewHandledProjectEnquiry(Project currentProject) {
        ArrayList<Enquiry> enquiries = enquiryList.getEnquiriesByProject(currentProject);
        if (enquiries.isEmpty()) {
            System.out.println(Colour.RED + "No enquiries for your project." + Colour.RESET);
            return;
        }

        System.out.println("===== Enquiries for Managed Project: " + currentProject.getProjectName() + " =====");
        for (Enquiry enquiry : enquiries) {
            System.out.println(enquiry);
        }
    }

    // Reply to pending enquiries for the project the manager is currently handling
    public void replyHandledProjectEnquiry(Project currentProject) {
        ArrayList<Enquiry> pendingEnquiries = enquiryList.getPendingEnquiriesByProject(currentProject);
        if (pendingEnquiries.isEmpty()) {
            System.out.println(Colour.RED + "No pending enquiries for your project." + Colour.RESET);
            return;
        }

        System.out.println("Pending Enquiries for Project: " + currentProject.getProjectName());
        for (int i = 0; i < pendingEnquiries.size(); i++) {
            System.out.println((i + 1) + ". " + pendingEnquiries.get(i));
        }

        System.out.print("Select an enquiry to reply (enter number): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println(Colour.RED + "Invalid input." + Colour.RESET);
            return;
        }

        if (choice < 0 || choice >= pendingEnquiries.size()) {
            System.out.println(Colour.RED + "Invalid choice." + Colour.RESET);
            return;
        }

        Enquiry enquiry = pendingEnquiries.get(choice);
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();
        enquiry.setReply(reply);
        enquiry.setStatus(Enquiry.EnquiryStatus.RESPONDED);

        System.out.println(Colour.GREEN + "Enquiry replied successfully." + Colour.RESET);
    }
}