package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class SMTPResultParser extends ResultParser {
   public EmailAddressParsedResult parse(Result var1) {
      String var2 = null;
      String var8 = getMassagedText(var1);
      EmailAddressParsedResult var9;
      if (!var8.startsWith("smtp:") && !var8.startsWith("SMTP:")) {
         var9 = var2;
      } else {
         String var3 = var8.substring(5);
         var8 = null;
         Object var4 = null;
         int var5 = var3.indexOf(58);
         String var6 = (String)var4;
         var2 = var3;
         if (var5 >= 0) {
            String var7 = var3.substring(var5 + 1);
            var3 = var3.substring(0, var5);
            var5 = var7.indexOf(58);
            var8 = var7;
            var6 = (String)var4;
            var2 = var3;
            if (var5 >= 0) {
               var6 = var7.substring(var5 + 1);
               var8 = var7.substring(0, var5);
               var2 = var3;
            }
         }

         var9 = new EmailAddressParsedResult(new String[]{var2}, (String[])null, (String[])null, var8, var6);
      }

      return var9;
   }
}
