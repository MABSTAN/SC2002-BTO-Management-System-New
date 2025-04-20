package controller.officer.helper;
import container.*;
import entity.*;
import utils.ClearScreen;
import utils.Colour;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;

import controller.officer.template.IOfficerManageEnquiries;
public class OfficerManageEnquiries implements IOfficerManageEnquiries{
    private Officer officer;
    private EnquiryList enquiryList;
    private Scanner scanner;

    public OfficerManageEnquiries(Officer officer, EnquiryList enquiryList) {
        this.officer = officer;
        this.enquiryList = enquiryList;
        this.scanner = new Scanner(System.in);
    }

    public void viewEnquiries() {
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        ArrayList<Enquiry> enquiries = enquiryList.getEnquiriesByProject(assignedProject);
        if (enquiries.isEmpty()) {
            System.out.println(Colour.RED + "No enquiries found for your project." + Colour.RESET);
            return;
        }

        System.out.println("\n-- Enquiries for Project: " + assignedProject.getProjectName() + " --");
        for (int i = 0; i < enquiries.size(); i++) {
            Enquiry enquiry = enquiries.get(i);
            System.out.println((i + 1) + ") " + enquiry.toString());
        }
    }

    public void replyToEnquiry() {
        Project assignedProject = officer.getAssignedProject();
        if (assignedProject == null) {
            System.out.println(Colour.RED + "You are not assigned to any project." + Colour.RESET);
            return;
        }

        ArrayList<Enquiry> enquiries = enquiryList.getEnquiriesByProject(assignedProject);
        if (enquiries.isEmpty()) {
            System.out.println(Colour.RED + "No enquiries to reply to." + Colour.RESET);
            return;
        }

        viewEnquiries();

        System.out.print("Select enquiry number to reply: ");
        int choice;
        try{
            choice = scanner.nextInt();
        }
        catch(InputMismatchException e){
            ClearScreen.clear();
            System.out.println(Colour.RED + "Please input an integer!" + Colour.RESET);
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (choice < 1 || choice > enquiries.size()) {
            System.out.println(Colour.RED + "Invalid selection." + Colour.RESET);
            return;
        }

        Enquiry selectedEnquiry = enquiries.get(choice - 1);
        System.out.println("Enquiry: " + selectedEnquiry.getMessage());
        System.out.print("Enter your reply: ");
        String reply = scanner.nextLine();

        selectedEnquiry.setReply(reply);
        selectedEnquiry.setStatus(Enquiry.EnquiryStatus.RESPONDED);
        System.out.println(Colour.GREEN + "Reply sent successfully." + Colour.RESET);
    }
}
