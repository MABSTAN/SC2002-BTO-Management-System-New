package controller;
import entity.User;
import java.util.Scanner;

import utils.BackButton;
import utils.ClearScreen;
import utils.Colour;

public class PasswordService {
    public static void changePassWord(User u) {
        ClearScreen.clear();
        Scanner sc = new Scanner(System.in);
        
        System.out.println(PasswordOperations.getPasswordRequirements());

        String newPassWord;

        do {
            System.out.print("Enter new password: ");
            newPassWord = sc.nextLine();
            
            if (!PasswordOperations.isPasswordValid(newPassWord)) {
                ClearScreen.clear();
                System.out.print(Colour.RED + "Password does not meet the requirements!\n" + Colour.RESET);
                System.out.println(PasswordOperations.getPasswordRequirements());
            }

        } while (!PasswordOperations.isPasswordValid(newPassWord));

        String confirmPassword;
        do {
            System.out.print("Confirm new password: ");
            confirmPassword = sc.nextLine();

            if (!newPassWord.equals(confirmPassword)) {
                ClearScreen.clear();
                System.out.println(Colour.RED + "\nPasswords do not match!" + Colour.RESET);
                System.out.print("Please re-enter new password: ");
                newPassWord = sc.nextLine();
            }
        } while (!newPassWord.equals(confirmPassword));

        System.out.println(Colour.GREEN + "\nPassword is valid!" + Colour.RESET);

        u.changePassword(newPassWord);
        System.out.println(Colour.GREEN + "Password changed successfully! Please login again." + Colour.RESET);
        BackButton.goBack();
    }
}