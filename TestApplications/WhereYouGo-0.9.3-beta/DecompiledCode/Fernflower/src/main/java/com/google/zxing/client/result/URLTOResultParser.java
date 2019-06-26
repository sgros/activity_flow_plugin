package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class URLTOResultParser extends ResultParser {
   public URIParsedResult parse(Result var1) {
      Object var2 = null;
      Object var3 = null;
      String var4 = getMassagedText(var1);
      URIParsedResult var6;
      if (!var4.startsWith("urlto:") && !var4.startsWith("URLTO:")) {
         var6 = (URIParsedResult)var3;
      } else {
         int var5 = var4.indexOf(58, 6);
         var6 = (URIParsedResult)var3;
         if (var5 >= 0) {
            String var7;
            if (var5 <= 6) {
               var7 = (String)var2;
            } else {
               var7 = var4.substring(6, var5);
            }

            var6 = new URIParsedResult(var4.substring(var5 + 1), var7);
         }
      }

      return var6;
   }
}
