package controller;

import java.util.regex.Pattern;
import utils.Colour;

public class PasswordOperations {

    private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");

    private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
 
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
 
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");

    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasUpper = HAS_UPPER.matcher(password).find();
        boolean hasLower = HAS_LOWER.matcher(password).find();
        boolean hasNumber = HAS_NUMBER.matcher(password).find();
        boolean hasSpecial = HAS_SPECIAL.matcher(password).find();

        return hasUpper && hasLower && hasNumber && hasSpecial;
    }

    public static String getPasswordRequirements() {
        return Colour.PURPLE + "Password Requirements:" + Colour.RESET + "\n" +
                "- Be at least 8 characters long\n" +
                "- Contain at least one uppercase letter\n" +
                "- Contain at least one lowercase letter\n" +
                "- Contain at least one number\n" +
                "- Contain at least one special character";
    }
}