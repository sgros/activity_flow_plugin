package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class VINResultParser extends ResultParser {
   private static final Pattern AZ09 = Pattern.compile("[A-Z0-9]{17}");
   private static final Pattern IOQ = Pattern.compile("[IOQ]");

   private static char checkChar(int var0) {
      char var1;
      if (var0 < 10) {
         char var2 = (char)(var0 + 48);
         var1 = var2;
      } else {
         if (var0 != 10) {
            throw new IllegalArgumentException();
         }

         byte var3 = 88;
         var1 = (char)var3;
      }

      return var1;
   }

   private static boolean checkChecksum(CharSequence var0) {
      int var1 = 0;

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         var1 += vinPositionWeight(var2 + 1) * vinCharValue(var0.charAt(var2));
      }

      boolean var3;
      if (var0.charAt(8) == checkChar(var1 % 11)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   private static String countryCode(CharSequence var0) {
      char var1 = var0.charAt(0);
      char var2 = var0.charAt(1);
      String var3;
      switch(var1) {
      case '1':
      case '4':
      case '5':
         var3 = "US";
         return var3;
      case '2':
         var3 = "CA";
         return var3;
      case '3':
         if (var2 >= 'A' && var2 <= 'W') {
            var3 = "MX";
            return var3;
         }
         break;
      case '9':
         if (var2 >= 'A' && var2 <= 'E' || var2 >= '3' && var2 <= '9') {
            var3 = "BR";
            return var3;
         }
         break;
      case 'J':
         if (var2 >= 'A' && var2 <= 'T') {
            var3 = "JP";
            return var3;
         }
         break;
      case 'K':
         if (var2 >= 'L' && var2 <= 'R') {
            var3 = "KO";
            return var3;
         }
         break;
      case 'L':
         var3 = "CN";
         return var3;
      case 'M':
         if (var2 >= 'A' && var2 <= 'E') {
            var3 = "IN";
            return var3;
         }
         break;
      case 'S':
         if (var2 >= 'A' && var2 <= 'M') {
            var3 = "UK";
            return var3;
         }

         if (var2 >= 'N' && var2 <= 'T') {
            var3 = "DE";
            return var3;
         }
         break;
      case 'V':
         if (var2 >= 'F' && var2 <= 'R') {
            var3 = "FR";
            return var3;
         }

         if (var2 >= 'S' && var2 <= 'W') {
            var3 = "ES";
            return var3;
         }
         break;
      case 'W':
         var3 = "DE";
         return var3;
      case 'X':
         if (var2 != '0' && (var2 < '3' || var2 > '9')) {
            break;
         }

         var3 = "RU";
         return var3;
      case 'Z':
         if (var2 >= 'A' && var2 <= 'R') {
            var3 = "IT";
            return var3;
         }
      }

      var3 = null;
      return var3;
   }

   private static int modelYear(char var0) {
      int var1;
      if (var0 >= 'E' && var0 <= 'H') {
         var1 = var0 - 69 + 1984;
      } else if (var0 >= 'J' && var0 <= 'N') {
         var1 = var0 - 74 + 1988;
      } else if (var0 == 'P') {
         var1 = 1993;
      } else if (var0 >= 'R' && var0 <= 'T') {
         var1 = var0 - 82 + 1994;
      } else if (var0 >= 'V' && var0 <= 'Y') {
         var1 = var0 - 86 + 1997;
      } else if (var0 >= '1' && var0 <= '9') {
         var1 = var0 - 49 + 2001;
      } else {
         if (var0 < 'A' || var0 > 'D') {
            throw new IllegalArgumentException();
         }

         var1 = var0 - 65 + 2010;
      }

      return var1;
   }

   private static int vinCharValue(char var0) {
      int var1;
      if (var0 >= 'A' && var0 <= 'I') {
         var1 = var0 - 65 + 1;
      } else if (var0 >= 'J' && var0 <= 'R') {
         var1 = var0 - 74 + 1;
      } else if (var0 >= 'S' && var0 <= 'Z') {
         var1 = var0 - 83 + 2;
      } else {
         if (var0 < '0' || var0 > '9') {
            throw new IllegalArgumentException();
         }

         var1 = var0 - 48;
      }

      return var1;
   }

   private static int vinPositionWeight(int var0) {
      int var1 = 10;
      if (var0 > 0 && var0 <= 7) {
         var1 = 9 - var0;
      } else if (var0 != 8) {
         if (var0 == 9) {
            var1 = 0;
         } else {
            if (var0 < 10 || var0 > 17) {
               throw new IllegalArgumentException();
            }

            var1 = 19 - var0;
         }
      }

      return var1;
   }

   public VINParsedResult parse(Result var1) {
      VINParsedResult var5;
      if (var1.getBarcodeFormat() != BarcodeFormat.CODE_39) {
         var5 = null;
      } else {
         String var6 = var1.getText();
         String var2 = IOQ.matcher(var6).replaceAll("").trim();
         if (!AZ09.matcher(var2).matches()) {
            var5 = null;
         } else {
            try {
               if (checkChecksum(var2)) {
                  String var3 = var2.substring(0, 3);
                  var5 = new VINParsedResult(var2, var3, var2.substring(3, 9), var2.substring(9, 17), countryCode(var3), var2.substring(3, 8), modelYear(var2.charAt(9)), var2.charAt(10), var2.substring(11));
                  return var5;
               }
            } catch (IllegalArgumentException var4) {
               var5 = null;
               return var5;
            }

            var5 = null;
         }
      }

      return var5;
   }
}
