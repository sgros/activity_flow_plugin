package com.stripe.android.model;

import com.stripe.android.util.DateUtils;
import com.stripe.android.util.StripeTextUtils;

public class Card {
    public static final String[] PREFIXES_AMERICAN_EXPRESS = new String[]{"34", "37"};
    public static final String[] PREFIXES_DINERS_CLUB = new String[]{"300", "301", "302", "303", "304", "305", "309", "36", "38", "39"};
    public static final String[] PREFIXES_DISCOVER = new String[]{"60", "62", "64", "65"};
    public static final String[] PREFIXES_JCB = new String[]{"35"};
    public static final String[] PREFIXES_MASTERCARD = new String[]{"2221", "2222", "2223", "2224", "2225", "2226", "2227", "2228", "2229", "223", "224", "225", "226", "227", "228", "229", "23", "24", "25", "26", "270", "271", "2720", "50", "51", "52", "53", "54", "55"};
    public static final String[] PREFIXES_VISA = new String[]{"4"};
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

    public Card(String str, Integer num, Integer num2, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10, String str11, String str12, String str13, String str14, String str15) {
        this.number = StripeTextUtils.nullIfBlank(normalizeCardNumber(str));
        this.expMonth = num;
        this.expYear = num2;
        this.cvc = StripeTextUtils.nullIfBlank(str2);
        this.name = StripeTextUtils.nullIfBlank(str3);
        this.addressLine1 = StripeTextUtils.nullIfBlank(str4);
        this.addressLine2 = StripeTextUtils.nullIfBlank(str5);
        this.addressCity = StripeTextUtils.nullIfBlank(str6);
        this.addressState = StripeTextUtils.nullIfBlank(str7);
        this.addressZip = StripeTextUtils.nullIfBlank(str8);
        this.addressCountry = StripeTextUtils.nullIfBlank(str9);
        this.brand = StripeTextUtils.asCardBrand(str10) == null ? getBrand() : str10;
        this.last4 = StripeTextUtils.nullIfBlank(str11) == null ? getLast4() : str11;
        this.fingerprint = StripeTextUtils.nullIfBlank(str12);
        this.funding = StripeTextUtils.asFundingType(str13);
        this.country = StripeTextUtils.nullIfBlank(str14);
        this.currency = StripeTextUtils.nullIfBlank(str15);
    }

    public Card(String str, Integer num, Integer num2, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        this(str, num, num2, str2, str3, str4, str5, str6, str7, str8, str9, null, null, null, null, null, str10);
    }

    public boolean validateNumber() {
        boolean z = false;
        if (StripeTextUtils.isBlank(this.number)) {
            return false;
        }
        String replaceAll = this.number.trim().replaceAll("\\s+|-", "");
        if (!StripeTextUtils.isBlank(replaceAll) && StripeTextUtils.isWholePositiveNumber(replaceAll) && isValidLuhnNumber(replaceAll)) {
            String brand = getBrand();
            if ("American Express".equals(brand)) {
                if (replaceAll.length() == 15) {
                    z = true;
                }
                return z;
            } else if ("Diners Club".equals(brand)) {
                if (replaceAll.length() == 14) {
                    z = true;
                }
                return z;
            } else if (replaceAll.length() == 16) {
                z = true;
            }
        }
        return z;
    }

    public boolean validateExpiryDate() {
        if (validateExpMonth() && validateExpYear()) {
            return DateUtils.hasMonthPassed(this.expYear.intValue(), this.expMonth.intValue()) ^ 1;
        }
        return false;
    }

    public boolean validateCVC() {
        boolean z = false;
        if (StripeTextUtils.isBlank(this.cvc)) {
            return false;
        }
        String trim = this.cvc.trim();
        String brand = getBrand();
        Object obj = ((brand != null || trim.length() < 3 || trim.length() > 4) && !(("American Express".equals(brand) && trim.length() == 4) || trim.length() == 3)) ? null : 1;
        if (StripeTextUtils.isWholePositiveNumber(trim) && obj != null) {
            z = true;
        }
        return z;
    }

    public boolean validateExpMonth() {
        Integer num = this.expMonth;
        return num != null && num.intValue() >= 1 && this.expMonth.intValue() <= 12;
    }

    public boolean validateExpYear() {
        Integer num = this.expYear;
        return (num == null || DateUtils.hasYearPassed(num.intValue())) ? false : true;
    }

    public String getNumber() {
        return this.number;
    }

    public String getCVC() {
        return this.cvc;
    }

    public Integer getExpMonth() {
        return this.expMonth;
    }

    public Integer getExpYear() {
        return this.expYear;
    }

    public String getName() {
        return this.name;
    }

    public String getAddressLine1() {
        return this.addressLine1;
    }

    public String getAddressLine2() {
        return this.addressLine2;
    }

    public String getAddressCity() {
        return this.addressCity;
    }

    public String getAddressZip() {
        return this.addressZip;
    }

    public String getAddressState() {
        return this.addressState;
    }

    public String getAddressCountry() {
        return this.addressCountry;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getLast4() {
        if (!StripeTextUtils.isBlank(this.last4)) {
            return this.last4;
        }
        String str = this.number;
        if (str == null || str.length() <= 4) {
            return null;
        }
        str = this.number;
        this.last4 = str.substring(str.length() - 4, this.number.length());
        return this.last4;
    }

    @Deprecated
    public String getType() {
        return getBrand();
    }

    public String getBrand() {
        if (StripeTextUtils.isBlank(this.brand) && !StripeTextUtils.isBlank(this.number)) {
            String str = StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_AMERICAN_EXPRESS) ? "American Express" : StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_DISCOVER) ? "Discover" : StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_JCB) ? "JCB" : StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_DINERS_CLUB) ? "Diners Club" : StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_VISA) ? "Visa" : StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_MASTERCARD) ? "MasterCard" : "Unknown";
            this.brand = str;
        }
        return this.brand;
    }

    private boolean isValidLuhnNumber(String str) {
        boolean z = true;
        int i = 0;
        int i2 = 1;
        for (int length = str.length() - 1; length >= 0; length--) {
            char charAt = str.charAt(length);
            if (!Character.isDigit(charAt)) {
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(charAt);
            int parseInt = Integer.parseInt(stringBuilder.toString());
            i2 ^= 1;
            if (i2 != 0) {
                parseInt *= 2;
            }
            if (parseInt > 9) {
                parseInt -= 9;
            }
            i += parseInt;
        }
        if (i % 10 != 0) {
            z = false;
        }
        return z;
    }

    private String normalizeCardNumber(String str) {
        return str == null ? null : str.trim().replaceAll("\\s+|-", "");
    }
}
