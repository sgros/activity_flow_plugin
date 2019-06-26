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

   public Card(String var1, Integer var2, Integer var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12) {
      this(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, (String)null, (String)null, (String)null, (String)null, (String)null, var12);
   }

   public Card(String var1, Integer var2, Integer var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17) {
      this.number = StripeTextUtils.nullIfBlank(this.normalizeCardNumber(var1));
      this.expMonth = var2;
      this.expYear = var3;
      this.cvc = StripeTextUtils.nullIfBlank(var4);
      this.name = StripeTextUtils.nullIfBlank(var5);
      this.addressLine1 = StripeTextUtils.nullIfBlank(var6);
      this.addressLine2 = StripeTextUtils.nullIfBlank(var7);
      this.addressCity = StripeTextUtils.nullIfBlank(var8);
      this.addressState = StripeTextUtils.nullIfBlank(var9);
      this.addressZip = StripeTextUtils.nullIfBlank(var10);
      this.addressCountry = StripeTextUtils.nullIfBlank(var11);
      if (StripeTextUtils.asCardBrand(var12) == null) {
         var12 = this.getBrand();
      }

      this.brand = var12;
      if (StripeTextUtils.nullIfBlank(var13) == null) {
         var1 = this.getLast4();
      } else {
         var1 = var13;
      }

      this.last4 = var1;
      this.fingerprint = StripeTextUtils.nullIfBlank(var14);
      this.funding = StripeTextUtils.asFundingType(var15);
      this.country = StripeTextUtils.nullIfBlank(var16);
      this.currency = StripeTextUtils.nullIfBlank(var17);
   }

   private boolean isValidLuhnNumber(String var1) {
      int var2 = var1.length();
      boolean var3 = true;
      int var4 = var2 - 1;
      int var5 = 0;

      boolean var9;
      for(boolean var10 = true; var4 >= 0; var10 = var9) {
         char var6 = var1.charAt(var4);
         if (!Character.isDigit(var6)) {
            return false;
         }

         StringBuilder var7 = new StringBuilder();
         var7.append("");
         var7.append(var6);
         int var8 = Integer.parseInt(var7.toString());
         var9 = var10 ^ true;
         var2 = var8;
         if (var9) {
            var2 = var8 * 2;
         }

         var8 = var2;
         if (var2 > 9) {
            var8 = var2 - 9;
         }

         var5 += var8;
         --var4;
      }

      if (var5 % 10 != 0) {
         var3 = false;
      }

      return var3;
   }

   private String normalizeCardNumber(String var1) {
      return var1 == null ? null : var1.trim().replaceAll("\\s+|-", "");
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
         String var1;
         if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_AMERICAN_EXPRESS)) {
            var1 = "American Express";
         } else if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_DISCOVER)) {
            var1 = "Discover";
         } else if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_JCB)) {
            var1 = "JCB";
         } else if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_DINERS_CLUB)) {
            var1 = "Diners Club";
         } else if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_VISA)) {
            var1 = "Visa";
         } else if (StripeTextUtils.hasAnyPrefix(this.number, PREFIXES_MASTERCARD)) {
            var1 = "MasterCard";
         } else {
            var1 = "Unknown";
         }

         this.brand = var1;
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
      } else {
         String var1 = this.number;
         if (var1 != null && var1.length() > 4) {
            var1 = this.number;
            this.last4 = var1.substring(var1.length() - 4, this.number.length());
            return this.last4;
         } else {
            return null;
         }
      }
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
      boolean var1 = StripeTextUtils.isBlank(this.cvc);
      boolean var2 = false;
      if (var1) {
         return false;
      } else {
         String var3 = this.cvc.trim();
         String var4 = this.getBrand();
         boolean var5;
         if ((var4 != null || var3.length() < 3 || var3.length() > 4) && (!"American Express".equals(var4) || var3.length() != 4) && var3.length() != 3) {
            var5 = false;
         } else {
            var5 = true;
         }

         var1 = var2;
         if (StripeTextUtils.isWholePositiveNumber(var3)) {
            var1 = var2;
            if (var5) {
               var1 = true;
            }
         }

         return var1;
      }
   }

   public boolean validateExpMonth() {
      Integer var1 = this.expMonth;
      boolean var2 = true;
      if (var1 == null || var1 < 1 || this.expMonth > 12) {
         var2 = false;
      }

      return var2;
   }

   public boolean validateExpYear() {
      Integer var1 = this.expYear;
      boolean var2;
      if (var1 != null && !DateUtils.hasYearPassed(var1)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean validateExpiryDate() {
      if (!this.validateExpMonth()) {
         return false;
      } else {
         return !this.validateExpYear() ? false : DateUtils.hasMonthPassed(this.expYear, this.expMonth) ^ true;
      }
   }

   public boolean validateNumber() {
      boolean var1 = StripeTextUtils.isBlank(this.number);
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      if (var1) {
         return false;
      } else {
         String var5 = this.number.trim().replaceAll("\\s+|-", "");
         var1 = var3;
         if (!StripeTextUtils.isBlank(var5)) {
            var1 = var3;
            if (StripeTextUtils.isWholePositiveNumber(var5)) {
               if (!this.isValidLuhnNumber(var5)) {
                  var1 = var3;
               } else {
                  String var6 = this.getBrand();
                  if ("American Express".equals(var6)) {
                     var1 = var4;
                     if (var5.length() == 15) {
                        var1 = true;
                     }

                     return var1;
                  }

                  if ("Diners Club".equals(var6)) {
                     var1 = var2;
                     if (var5.length() == 14) {
                        var1 = true;
                     }

                     return var1;
                  }

                  var1 = var3;
                  if (var5.length() == 16) {
                     var1 = true;
                  }
               }
            }
         }

         return var1;
      }
   }
}
