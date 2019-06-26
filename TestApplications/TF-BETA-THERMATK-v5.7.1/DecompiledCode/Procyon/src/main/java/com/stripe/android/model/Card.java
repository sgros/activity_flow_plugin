// 
// Decompiled by Procyon v0.5.34
// 

package com.stripe.android.model;

import com.stripe.android.util.DateUtils;
import com.stripe.android.util.StripeTextUtils;

public class Card
{
    public static final String[] PREFIXES_AMERICAN_EXPRESS;
    public static final String[] PREFIXES_DINERS_CLUB;
    public static final String[] PREFIXES_DISCOVER;
    public static final String[] PREFIXES_JCB;
    public static final String[] PREFIXES_MASTERCARD;
    public static final String[] PREFIXES_VISA;
    private String addressCity;
    private String addressCountry;
    private String addressLine1;
    private String addressLine2;
    private String addressState;
    private String addressZip;
    private String brand;
    private String country;
    private String currency;
    private String cvc;
    private Integer expMonth;
    private Integer expYear;
    private String fingerprint;
    private String funding;
    private String last4;
    private String name;
    private String number;
    
    static {
        PREFIXES_AMERICAN_EXPRESS = new String[] { "34", "37" };
        PREFIXES_DISCOVER = new String[] { "60", "62", "64", "65" };
        PREFIXES_JCB = new String[] { "35" };
        PREFIXES_DINERS_CLUB = new String[] { "300", "301", "302", "303", "304", "305", "309", "36", "38", "39" };
        PREFIXES_VISA = new String[] { "4" };
        PREFIXES_MASTERCARD = new String[] { "2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55" };
    }
    
    public Card(final String s, final Integer n, final Integer n2, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8, final String s9, final String s10) {
        this(s, n, n2, s2, s3, s4, s5, s6, s7, s8, s9, null, null, null, null, null, s10);
    }
    
    public Card(String last4, final Integer expMonth, final Integer expYear, final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final String s7, final String s8, String brand, final String s9, final String s10, final String s11, final String s12, final String s13) {
        this.number = StripeTextUtils.nullIfBlank(this.normalizeCardNumber(last4));
        this.expMonth = expMonth;
        this.expYear = expYear;
        this.cvc = StripeTextUtils.nullIfBlank(s);
        this.name = StripeTextUtils.nullIfBlank(s2);
        this.addressLine1 = StripeTextUtils.nullIfBlank(s3);
        this.addressLine2 = StripeTextUtils.nullIfBlank(s4);
        this.addressCity = StripeTextUtils.nullIfBlank(s5);
        this.addressState = StripeTextUtils.nullIfBlank(s6);
        this.addressZip = StripeTextUtils.nullIfBlank(s7);
        this.addressCountry = StripeTextUtils.nullIfBlank(s8);
        if (StripeTextUtils.asCardBrand(brand) == null) {
            brand = this.getBrand();
        }
        this.brand = brand;
        if (StripeTextUtils.nullIfBlank(s9) == null) {
            last4 = this.getLast4();
        }
        else {
            last4 = s9;
        }
        this.last4 = last4;
        this.fingerprint = StripeTextUtils.nullIfBlank(s10);
        this.funding = StripeTextUtils.asFundingType(s11);
        this.country = StripeTextUtils.nullIfBlank(s12);
        this.currency = StripeTextUtils.nullIfBlank(s13);
    }
    
    private boolean isValidLuhnNumber(final String s) {
        final int length = s.length();
        boolean b = true;
        int i = length - 1;
        int n = 0;
        int n2 = 1;
        while (i >= 0) {
            final char char1 = s.charAt(i);
            if (!Character.isDigit(char1)) {
                return false;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(char1);
            final int int1 = Integer.parseInt(sb.toString());
            final int n3 = n2 ^ 0x1;
            int n4 = int1;
            if (n3 != 0) {
                n4 = int1 * 2;
            }
            int n5;
            if ((n5 = n4) > 9) {
                n5 = n4 - 9;
            }
            n += n5;
            --i;
            n2 = n3;
        }
        if (n % 10 != 0) {
            b = false;
        }
        return b;
    }
    
    private String normalizeCardNumber(final String s) {
        if (s == null) {
            return null;
        }
        return s.trim().replaceAll("\\s+|-", "");
    }
    
    public String getAddressCity() {
        return this.addressCity;
    }
    
    public String getAddressCountry() {
        return this.addressCountry;
    }
    
    public String getAddressLine1() {
        return this.addressLine1;
    }
    
    public String getAddressLine2() {
        return this.addressLine2;
    }
    
    public String getAddressState() {
        return this.addressState;
    }
    
    public String getAddressZip() {
        return this.addressZip;
    }
    
    public String getBrand() {
        if (StripeTextUtils.isBlank(this.brand) && !StripeTextUtils.isBlank(this.number)) {
            String brand;
            if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_AMERICAN_EXPRESS)) {
                brand = "American Express";
            }
            else if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_DISCOVER)) {
                brand = "Discover";
            }
            else if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_JCB)) {
                brand = "JCB";
            }
            else if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_DINERS_CLUB)) {
                brand = "Diners Club";
            }
            else if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_VISA)) {
                brand = "Visa";
            }
            else if (StripeTextUtils.hasAnyPrefix(this.number, Card.PREFIXES_MASTERCARD)) {
                brand = "MasterCard";
            }
            else {
                brand = "Unknown";
            }
            this.brand = brand;
        }
        return this.brand;
    }
    
    public String getCVC() {
        return this.cvc;
    }
    
    public String getCurrency() {
        return this.currency;
    }
    
    public Integer getExpMonth() {
        return this.expMonth;
    }
    
    public Integer getExpYear() {
        return this.expYear;
    }
    
    public String getLast4() {
        if (!StripeTextUtils.isBlank(this.last4)) {
            return this.last4;
        }
        final String number = this.number;
        if (number != null && number.length() > 4) {
            final String number2 = this.number;
            return this.last4 = number2.substring(number2.length() - 4, this.number.length());
        }
        return null;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNumber() {
        return this.number;
    }
    
    @Deprecated
    public String getType() {
        return this.getBrand();
    }
    
    public boolean validateCVC() {
        final boolean blank = StripeTextUtils.isBlank(this.cvc);
        final boolean b = false;
        if (blank) {
            return false;
        }
        final String trim = this.cvc.trim();
        final String brand = this.getBrand();
        final boolean b2 = (brand == null && trim.length() >= 3 && trim.length() <= 4) || ("American Express".equals(brand) && trim.length() == 4) || trim.length() == 3;
        boolean b3 = b;
        if (StripeTextUtils.isWholePositiveNumber(trim)) {
            b3 = b;
            if (b2) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public boolean validateExpMonth() {
        final Integer expMonth = this.expMonth;
        boolean b = true;
        if (expMonth == null || expMonth < 1 || this.expMonth > 12) {
            b = false;
        }
        return b;
    }
    
    public boolean validateExpYear() {
        final Integer expYear = this.expYear;
        return expYear != null && !DateUtils.hasYearPassed(expYear);
    }
    
    public boolean validateExpiryDate() {
        return this.validateExpMonth() && this.validateExpYear() && (DateUtils.hasMonthPassed(this.expYear, this.expMonth) ^ true);
    }
    
    public boolean validateNumber() {
        final boolean blank = StripeTextUtils.isBlank(this.number);
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        if (blank) {
            return false;
        }
        final String replaceAll = this.number.trim().replaceAll("\\s+|-", "");
        boolean b4 = b2;
        if (!StripeTextUtils.isBlank(replaceAll)) {
            b4 = b2;
            if (StripeTextUtils.isWholePositiveNumber(replaceAll)) {
                if (!this.isValidLuhnNumber(replaceAll)) {
                    b4 = b2;
                }
                else {
                    final String brand = this.getBrand();
                    if ("American Express".equals(brand)) {
                        boolean b5 = b3;
                        if (replaceAll.length() == 15) {
                            b5 = true;
                        }
                        return b5;
                    }
                    if ("Diners Club".equals(brand)) {
                        boolean b6 = b;
                        if (replaceAll.length() == 14) {
                            b6 = true;
                        }
                        return b6;
                    }
                    b4 = b2;
                    if (replaceAll.length() == 16) {
                        b4 = true;
                    }
                }
            }
        }
        return b4;
    }
}
