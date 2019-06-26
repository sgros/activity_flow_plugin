package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class BookmarkDoCoMoResultParser extends AbstractDoCoMoResultParser {
   public URIParsedResult parse(Result var1) {
      Object var2 = null;
      String var5 = var1.getText();
      URIParsedResult var6;
      if (!var5.startsWith("MEBKM:")) {
         var6 = (URIParsedResult)var2;
      } else {
         String var3 = matchSingleDoCoMoPrefixedField("TITLE:", var5, true);
         String[] var4 = matchDoCoMoPrefixedField("URL:", var5, true);
         var6 = (URIParsedResult)var2;
         if (var4 != null) {
            String var7 = var4[0];
            var6 = (URIParsedResult)var2;
            if (URIResultParser.isBasicallyValidURI(var7)) {
               var6 = new URIParsedResult(var7, var3);
            }
         }
      }

      return var6;
   }
}
