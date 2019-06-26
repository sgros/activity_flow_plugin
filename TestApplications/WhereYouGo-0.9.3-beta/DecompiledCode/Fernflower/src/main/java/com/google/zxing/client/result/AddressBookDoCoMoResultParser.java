package com.google.zxing.client.result;

import com.google.zxing.Result;

public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {
   private static String parseName(String var0) {
      int var1 = var0.indexOf(44);
      String var2 = var0;
      if (var1 >= 0) {
         var2 = var0.substring(var1 + 1) + ' ' + var0.substring(0, var1);
      }

      return var2;
   }

   public AddressBookParsedResult parse(Result var1) {
      String var2 = getMassagedText(var1);
      AddressBookParsedResult var10;
      if (!var2.startsWith("MECARD:")) {
         var10 = null;
      } else {
         String[] var11 = matchDoCoMoPrefixedField("N:", var2, true);
         if (var11 == null) {
            var10 = null;
         } else {
            String var3 = parseName(var11[0]);
            String var4 = matchSingleDoCoMoPrefixedField("SOUND:", var2, true);
            String[] var5 = matchDoCoMoPrefixedField("TEL:", var2, true);
            String[] var6 = matchDoCoMoPrefixedField("EMAIL:", var2, true);
            String var7 = matchSingleDoCoMoPrefixedField("NOTE:", var2, false);
            String[] var8 = matchDoCoMoPrefixedField("ADR:", var2, true);
            String var9 = matchSingleDoCoMoPrefixedField("BDAY:", var2, true);
            String var12 = var9;
            if (!isStringOfDigits(var9, 8)) {
               var12 = null;
            }

            String[] var13 = matchDoCoMoPrefixedField("URL:", var2, true);
            var2 = matchSingleDoCoMoPrefixedField("ORG:", var2, true);
            var10 = new AddressBookParsedResult(maybeWrap(var3), (String[])null, var4, var5, (String[])null, var6, (String[])null, (String)null, var7, var8, (String[])null, var2, var12, (String)null, var13, (String[])null);
         }
      }

      return var10;
   }
}
