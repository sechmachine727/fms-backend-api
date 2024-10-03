package org.fms.training.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    private Validation() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[\\w-_.+]*[\\w-_.]@(\\w+\\.)+\\w+\\w$";

        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
