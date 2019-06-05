package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.Map;
import java.util.regex.Pattern;

public final class EmailAddressResultParser extends ResultParser {
   private static final Pattern COMMA = Pattern.compile(",");

   public EmailAddressParsedResult parse(Result var1) {
      String[] var2 = null;
      String var3 = getMassagedText(var1);
      EmailAddressParsedResult var13;
      if (!var3.startsWith("mailto:") && !var3.startsWith("MAILTO:")) {
         var13 = var2;
         if (EmailDoCoMoResultParser.isBasicallyValidEmailAddress(var3)) {
            var13 = new EmailAddressParsedResult(var3);
         }
      } else {
         String var4 = var3.substring(7);
         int var5 = var4.indexOf(63);
         String var12 = var4;
         if (var5 >= 0) {
            var12 = var4.substring(0, var5);
         }

         try {
            var4 = urlDecode(var12);
         } catch (IllegalArgumentException var11) {
            var13 = var2;
            return var13;
         }

         String[] var14 = null;
         if (!var4.isEmpty()) {
            var14 = COMMA.split(var4);
         }

         Map var6 = parseNameValuePairs(var3);
         String[] var7 = null;
         Object var8 = null;
         String[] var15 = null;
         Object var9 = null;
         String var10 = null;
         var3 = null;
         var2 = var14;
         if (var6 != null) {
            var2 = var14;
            if (var14 == null) {
               var4 = (String)var6.get("to");
               var2 = var14;
               if (var4 != null) {
                  var2 = COMMA.split(var4);
               }
            }

            var4 = (String)var6.get("cc");
            var14 = (String[])var8;
            if (var4 != null) {
               var14 = COMMA.split(var4);
            }

            var3 = (String)var6.get("bcc");
            var15 = (String[])var9;
            if (var3 != null) {
               var15 = COMMA.split(var3);
            }

            var10 = (String)var6.get("subject");
            var3 = (String)var6.get("body");
            var7 = var14;
         }

         var13 = new EmailAddressParsedResult(var2, var7, var15, var10, var3);
      }

      return var13;
   }
}
