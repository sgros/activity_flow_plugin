package com.stripe.android.util;

public class StripeTextUtils {
   public static String asCardBrand(String var0) {
      if (isBlank(var0)) {
         return null;
      } else if ("American Express".equalsIgnoreCase(var0)) {
         return "American Express";
      } else if ("MasterCard".equalsIgnoreCase(var0)) {
         return "MasterCard";
      } else if ("Diners Club".equalsIgnoreCase(var0)) {
         return "Diners Club";
      } else if ("Discover".equalsIgnoreCase(var0)) {
         return "Discover";
      } else if ("JCB".equalsIgnoreCase(var0)) {
         return "JCB";
      } else {
         return "Visa".equalsIgnoreCase(var0) ? "Visa" : "Unknown";
      }
   }

   public static String asFundingType(String var0) {
      if (isBlank(var0)) {
         return null;
      } else if ("credit".equalsIgnoreCase(var0)) {
         return "credit";
      } else if ("debit".equalsIgnoreCase(var0)) {
         return "debit";
      } else {
         return "prepaid".equalsIgnoreCase(var0) ? "prepaid" : "unknown";
      }
   }

   public static String asTokenType(String var0) {
      return "card".equals(var0) ? "card" : null;
   }

   public static boolean hasAnyPrefix(String var0, String... var1) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            if (var0.startsWith(var1[var3])) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isBlank(String var0) {
      boolean var1;
      if (var0 != null && var0.trim().length() != 0) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public static boolean isWholePositiveNumber(String var0) {
      if (var0 == null) {
         return false;
      } else {
         for(int var1 = 0; var1 < var0.length(); ++var1) {
            if (!Character.isDigit(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static String nullIfBlank(String var0) {
      String var1 = var0;
      if (isBlank(var0)) {
         var1 = null;
      }

      return var1;
   }
}
