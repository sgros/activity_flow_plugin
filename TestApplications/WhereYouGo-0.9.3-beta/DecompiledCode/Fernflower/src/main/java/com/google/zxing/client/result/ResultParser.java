package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class ResultParser {
   private static final Pattern AMPERSAND = Pattern.compile("&");
   private static final String BYTE_ORDER_MARK = "\ufeff";
   private static final Pattern DIGITS = Pattern.compile("\\d+");
   private static final Pattern EQUALS = Pattern.compile("=");
   private static final ResultParser[] PARSERS = new ResultParser[]{new BookmarkDoCoMoResultParser(), new AddressBookDoCoMoResultParser(), new EmailDoCoMoResultParser(), new AddressBookAUResultParser(), new VCardResultParser(), new BizcardResultParser(), new VEventResultParser(), new EmailAddressResultParser(), new SMTPResultParser(), new TelResultParser(), new SMSMMSResultParser(), new SMSTOMMSTOResultParser(), new GeoResultParser(), new WifiResultParser(), new URLTOResultParser(), new URIResultParser(), new ISBNResultParser(), new ProductResultParser(), new ExpandedProductResultParser(), new VINResultParser()};

   private static void appendKeyValue(CharSequence var0, Map var1) {
      String[] var2 = EQUALS.split(var0, 2);
      if (var2.length == 2) {
         String var4 = var2[0];
         String var5 = var2[1];

         try {
            var1.put(var4, urlDecode(var5));
         } catch (IllegalArgumentException var3) {
         }
      }

   }

   private static int countPrecedingBackslashes(CharSequence var0, int var1) {
      int var2 = 0;
      --var1;

      while(var1 >= 0 && var0.charAt(var1) == '\\') {
         ++var2;
         --var1;
      }

      return var2;
   }

   protected static String getMassagedText(Result var0) {
      String var1 = var0.getText();
      String var2 = var1;
      if (var1.startsWith("\ufeff")) {
         var2 = var1.substring(1);
      }

      return var2;
   }

   protected static boolean isStringOfDigits(CharSequence var0, int var1) {
      boolean var2;
      if (var0 != null && var1 > 0 && var1 == var0.length() && DIGITS.matcher(var0).matches()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   protected static boolean isSubstringOfDigits(CharSequence var0, int var1, int var2) {
      boolean var3 = false;
      boolean var4 = var3;
      if (var0 != null) {
         if (var2 <= 0) {
            var4 = var3;
         } else {
            var2 += var1;
            var4 = var3;
            if (var0.length() >= var2) {
               var4 = var3;
               if (DIGITS.matcher(var0.subSequence(var1, var2)).matches()) {
                  var4 = true;
               }
            }
         }
      }

      return var4;
   }

   static String[] matchPrefixedField(String var0, String var1, char var2, boolean var3) {
      ArrayList var4 = null;
      int var5 = 0;
      int var6 = var1.length();

      while(var5 < var6) {
         var5 = var1.indexOf(var0, var5);
         if (var5 < 0) {
            break;
         }

         int var7 = var5 + var0.length();
         boolean var8 = true;
         ArrayList var9 = var4;
         var5 = var7;

         while(true) {
            int var10 = var5;
            var5 = var5;
            var4 = var9;
            if (!var8) {
               break;
            }

            var5 = var1.indexOf(var2, var10);
            if (var5 < 0) {
               var5 = var1.length();
               var8 = false;
            } else if (countPrecedingBackslashes(var1, var5) % 2 != 0) {
               ++var5;
            } else {
               var4 = var9;
               if (var9 == null) {
                  var4 = new ArrayList(3);
               }

               String var11 = unescapeBackslash(var1.substring(var7, var5));
               String var13 = var11;
               if (var3) {
                  var13 = var11.trim();
               }

               if (!var13.isEmpty()) {
                  var4.add(var13);
               }

               ++var5;
               var8 = false;
               var9 = var4;
            }
         }
      }

      String[] var12;
      if (var4 != null && !var4.isEmpty()) {
         var12 = (String[])var4.toArray(new String[var4.size()]);
      } else {
         var12 = null;
      }

      return var12;
   }

   static String matchSinglePrefixedField(String var0, String var1, char var2, boolean var3) {
      String[] var4 = matchPrefixedField(var0, var1, var2, var3);
      if (var4 == null) {
         var0 = null;
      } else {
         var0 = var4[0];
      }

      return var0;
   }

   protected static void maybeAppend(String var0, StringBuilder var1) {
      if (var0 != null) {
         var1.append('\n');
         var1.append(var0);
      }

   }

   protected static void maybeAppend(String[] var0, StringBuilder var1) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = var0[var3];
            var1.append('\n');
            var1.append(var4);
         }
      }

   }

   protected static String[] maybeWrap(String var0) {
      String[] var2;
      if (var0 == null) {
         var2 = null;
      } else {
         String[] var1 = new String[]{var0};
         var2 = var1;
      }

      return var2;
   }

   protected static int parseHexDigit(char var0) {
      int var1;
      if (var0 >= '0' && var0 <= '9') {
         var1 = var0 - 48;
      } else if (var0 >= 'a' && var0 <= 'f') {
         var1 = var0 - 97 + 10;
      } else if (var0 >= 'A' && var0 <= 'F') {
         var1 = var0 - 65 + 10;
      } else {
         var1 = -1;
      }

      return var1;
   }

   static Map parseNameValuePairs(String var0) {
      int var1 = var0.indexOf(63);
      HashMap var5;
      if (var1 < 0) {
         var5 = null;
      } else {
         HashMap var2 = new HashMap(3);
         String[] var3 = AMPERSAND.split(var0.substring(var1 + 1));
         int var4 = var3.length;
         var1 = 0;

         while(true) {
            var5 = var2;
            if (var1 >= var4) {
               break;
            }

            appendKeyValue(var3[var1], var2);
            ++var1;
         }
      }

      return var5;
   }

   public static ParsedResult parseResult(Result var0) {
      ResultParser[] var1 = PARSERS;
      int var2 = var1.length;
      int var3 = 0;

      Object var5;
      while(true) {
         if (var3 >= var2) {
            var5 = new TextParsedResult(var0.getText(), (String)null);
            break;
         }

         ParsedResult var4 = var1[var3].parse(var0);
         if (var4 != null) {
            var5 = var4;
            break;
         }

         ++var3;
      }

      return (ParsedResult)var5;
   }

   protected static String unescapeBackslash(String var0) {
      int var1 = var0.indexOf(92);
      if (var1 >= 0) {
         int var2 = var0.length();
         StringBuilder var3 = new StringBuilder(var2 - 1);
         var3.append(var0.toCharArray(), 0, var1);

         for(boolean var4 = false; var1 < var2; ++var1) {
            char var5 = var0.charAt(var1);
            if (!var4 && var5 == '\\') {
               var4 = true;
            } else {
               var3.append(var5);
               var4 = false;
            }
         }

         var0 = var3.toString();
      }

      return var0;
   }

   static String urlDecode(String var0) {
      try {
         var0 = URLDecoder.decode(var0, "UTF-8");
         return var0;
      } catch (UnsupportedEncodingException var1) {
         throw new IllegalStateException(var1);
      }
   }

   public abstract ParsedResult parse(Result var1);
}
