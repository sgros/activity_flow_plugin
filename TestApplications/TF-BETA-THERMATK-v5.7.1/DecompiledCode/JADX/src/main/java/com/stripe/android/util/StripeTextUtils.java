package com.stripe.android.util;

public class StripeTextUtils {
    public static boolean hasAnyPrefix(String str, String... strArr) {
        if (str == null) {
            return false;
        }
        for (String startsWith : strArr) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWholePositiveNumber(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String nullIfBlank(String str) {
        return isBlank(str) ? null : str;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String asCardBrand(String str) {
        if (isBlank(str)) {
            return null;
        }
        String str2 = "American Express";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "MasterCard";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "Diners Club";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "Discover";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "JCB";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "Visa";
        return str2.equalsIgnoreCase(str) ? str2 : "Unknown";
    }

    public static String asFundingType(String str) {
        if (isBlank(str)) {
            return null;
        }
        String str2 = "credit";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "debit";
        if (str2.equalsIgnoreCase(str)) {
            return str2;
        }
        str2 = "prepaid";
        return str2.equalsIgnoreCase(str) ? str2 : "unknown";
    }

    public static String asTokenType(String str) {
        String str2 = "card";
        return str2.equals(str) ? str2 : null;
    }
}
