package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.oned.UPCEReader;

public final class ProductResultParser extends ResultParser {
   public ProductParsedResult parse(Result var1) {
      Object var2 = null;
      BarcodeFormat var3 = var1.getBarcodeFormat();
      ProductParsedResult var5;
      if (var3 != BarcodeFormat.UPC_A && var3 != BarcodeFormat.UPC_E && var3 != BarcodeFormat.EAN_8 && var3 != BarcodeFormat.EAN_13) {
         var5 = (ProductParsedResult)var2;
      } else {
         String var4 = getMassagedText(var1);
         var5 = (ProductParsedResult)var2;
         if (isStringOfDigits(var4, var4.length())) {
            String var6;
            if (var3 == BarcodeFormat.UPC_E && var4.length() == 8) {
               var6 = UPCEReader.convertUPCEtoUPCA(var4);
            } else {
               var6 = var4;
            }

            var5 = new ProductParsedResult(var4, var6);
         }
      }

      return var5;
   }
}
