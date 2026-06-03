package com.app.fooddelivery.validation;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.isBlank();
    }
}
