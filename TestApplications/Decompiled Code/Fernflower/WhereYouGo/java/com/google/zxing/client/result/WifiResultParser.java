package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class WifiResultParser extends ResultParser {
   public WifiParsedResult parse(Result var1) {
      String var2 = null;
      String var3 = getMassagedText(var1);
      WifiParsedResult var6;
      if (!var3.startsWith("WIFI:")) {
         var6 = var2;
      } else {
         String var4 = matchSinglePrefixedField("S:", var3, ';', false);
         var6 = var2;
         if (var4 != null) {
            var6 = var2;
            if (!var4.isEmpty()) {
               String var5 = matchSinglePrefixedField("P:", var3, ';', false);
               var2 = matchSinglePrefixedField("T:", var3, ';', false);
               String var7 = var2;
               if (var2 == null) {
                  var7 = "nopass";
               }

               var6 = new WifiParsedResult(var7, var4, var5, Boolean.parseBoolean(matchSinglePrefixedField("H:", var3, ';', false)));
            }
         }
      }

      return var6;
   }
}
