package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Pattern;

public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {
   private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");

   static boolean isBasicallyValidEmailAddress(String var0) {
      boolean var1;
      if (var0 != null && ATEXT_ALPHANUMERIC.matcher(var0).matches() && var0.indexOf(64) >= 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public EmailAddressParsedResult parse(Result var1) {
      Object var2 = null;
      String var3 = getMassagedText(var1);
      EmailAddressParsedResult var7;
      if (!var3.startsWith("MATMSG:")) {
         var7 = (EmailAddressParsedResult)var2;
      } else {
         String[] var4 = matchDoCoMoPrefixedField("TO:", var3, true);
         var7 = (EmailAddressParsedResult)var2;
         if (var4 != null) {
            int var5 = var4.length;
            int var6 = 0;

            while(true) {
               if (var6 >= var5) {
                  var7 = new EmailAddressParsedResult(var4, (String[])null, (String[])null, matchSingleDoCoMoPrefixedField("SUB:", var3, false), matchSingleDoCoMoPrefixedField("BODY:", var3, false));
                  break;
               }

               var7 = (EmailAddressParsedResult)var2;
               if (!isBasicallyValidEmailAddress(var4[var6])) {
                  break;
               }

               ++var6;
            }
         }
      }

      return var7;
   }
}
