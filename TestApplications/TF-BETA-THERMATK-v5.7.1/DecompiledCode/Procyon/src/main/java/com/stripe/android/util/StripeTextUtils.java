// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.util;

public class StripeTextUtils
{
    public static String asCardBrand(final String s) {
        if (isBlank(s)) {
            return null;
        }
        if ("American Express".equalsIgnoreCase(s)) {
            return "American Express";
        }
        if ("MasterCard".equalsIgnoreCase(s)) {
            return "MasterCard";
        }
        if ("Diners Club".equalsIgnoreCase(s)) {
            return "Diners Club";
        }
        if ("Discover".equalsIgnoreCase(s)) {
            return "Discover";
        }
        if ("JCB".equalsIgnoreCase(s)) {
            return "JCB";
        }
        if ("Visa".equalsIgnoreCase(s)) {
            return "Visa";
        }
        return "Unknown";
    }
    
    public static String asFundingType(final String anotherString) {
        if (isBlank(anotherString)) {
            return null;
        }
        if ("credit".equalsIgnoreCase(anotherString)) {
            return "credit";
        }
        if ("debit".equalsIgnoreCase(anotherString)) {
            return "debit";
        }
        if ("prepaid".equalsIgnoreCase(anotherString)) {
            return "prepaid";
        }
        return "unknown";
    }
    
    public static String asTokenType(final String anObject) {
        if ("card".equals(anObject)) {
            return "card";
        }
        return null;
    }
    
    public static boolean hasAnyPrefix(final String s, final String... array) {
        if (s == null) {
            return false;
        }
        for (int length = array.length, i = 0; i < length; ++i) {
            if (s.startsWith(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlank(final String s) {
        return s == null || s.trim().length() == 0;
    }
    
    public static boolean isWholePositiveNumber(final String s) {
        if (s == null) {
            return false;
        }
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isDigit(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public static String nullIfBlank(final String s) {
        String s2 = s;
        if (isBlank(s)) {
            s2 = null;
        }
        return s2;
    }
}
