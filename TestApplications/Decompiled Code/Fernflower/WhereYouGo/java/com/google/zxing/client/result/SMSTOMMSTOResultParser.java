package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class SMSTOMMSTOResultParser extends ResultParser {
   public SMSParsedResult parse(Result var1) {
      String var2 = null;
      String var5 = getMassagedText(var1);
      SMSParsedResult var6;
      if (!var5.startsWith("smsto:") && !var5.startsWith("SMSTO:") && !var5.startsWith("mmsto:") && !var5.startsWith("MMSTO:")) {
         var6 = var2;
      } else {
         String var3 = var5.substring(6);
         var2 = null;
         int var4 = var3.indexOf(58);
         var5 = var3;
         if (var4 >= 0) {
            var2 = var3.substring(var4 + 1);
            var5 = var3.substring(0, var4);
         }

         var6 = new SMSParsedResult(var5, (String)null, (String)null, var2);
      }

      return var6;
   }
}
