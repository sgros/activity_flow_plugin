package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class TelResultParser extends ResultParser {
   public TelParsedResult parse(Result var1) {
      Object var2 = null;
      String var3 = getMassagedText(var1);
      TelParsedResult var6;
      if (!var3.startsWith("tel:") && !var3.startsWith("TEL:")) {
         var6 = (TelParsedResult)var2;
      } else {
         String var5;
         if (var3.startsWith("TEL:")) {
            var5 = "tel:" + var3.substring(4);
         } else {
            var5 = var3;
         }

         int var4 = var3.indexOf(63, 4);
         if (var4 < 0) {
            var3 = var3.substring(4);
         } else {
            var3 = var3.substring(4, var4);
         }

         var6 = new TelParsedResult(var3, var5, (String)null);
      }

      return var6;
   }
}
