package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public final class ISBNResultParser extends ResultParser {
   public ISBNParsedResult parse(Result var1) {
      Object var2 = null;
      ISBNParsedResult var4;
      if (var1.getBarcodeFormat() != BarcodeFormat.EAN_13) {
         var4 = (ISBNParsedResult)var2;
      } else {
         String var3 = getMassagedText(var1);
         var4 = (ISBNParsedResult)var2;
         if (var3.length() == 13) {
            if (!var3.startsWith("978")) {
               var4 = (ISBNParsedResult)var2;
               if (!var3.startsWith("979")) {
                  return var4;
               }
            }

            var4 = new ISBNParsedResult(var3);
         }
      }

      return var4;
   }
}
