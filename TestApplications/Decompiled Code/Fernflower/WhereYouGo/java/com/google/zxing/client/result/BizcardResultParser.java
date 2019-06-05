package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;

public final class BizcardResultParser extends AbstractDoCoMoResultParser {
   private static String buildName(String var0, String var1) {
      if (var0 != null) {
         if (var1 == null) {
            var1 = var0;
         } else {
            var1 = var0 + ' ' + var1;
         }
      }

      return var1;
   }

   private static String[] buildPhoneNumbers(String var0, String var1, String var2) {
      ArrayList var3 = new ArrayList(3);
      if (var0 != null) {
         var3.add(var0);
      }

      if (var1 != null) {
         var3.add(var1);
      }

      if (var2 != null) {
         var3.add(var2);
      }

      int var4 = var3.size();
      String[] var5;
      if (var4 == 0) {
         var5 = null;
      } else {
         var5 = (String[])var3.toArray(new String[var4]);
      }

      return var5;
   }

   public AddressBookParsedResult parse(Result var1) {
      String var2 = getMassagedText(var1);
      AddressBookParsedResult var9;
      if (!var2.startsWith("BIZCARD:")) {
         var9 = null;
      } else {
         String var3 = buildName(matchSingleDoCoMoPrefixedField("N:", var2, true), matchSingleDoCoMoPrefixedField("X:", var2, true));
         String var4 = matchSingleDoCoMoPrefixedField("T:", var2, true);
         String var5 = matchSingleDoCoMoPrefixedField("C:", var2, true);
         String[] var6 = matchDoCoMoPrefixedField("A:", var2, true);
         String var10 = matchSingleDoCoMoPrefixedField("B:", var2, true);
         String var7 = matchSingleDoCoMoPrefixedField("M:", var2, true);
         String var8 = matchSingleDoCoMoPrefixedField("F:", var2, true);
         var2 = matchSingleDoCoMoPrefixedField("E:", var2, true);
         var9 = new AddressBookParsedResult(maybeWrap(var3), (String[])null, (String)null, buildPhoneNumbers(var10, var7, var8), (String[])null, maybeWrap(var2), (String[])null, (String)null, (String)null, var6, (String[])null, var5, (String)null, var4, (String[])null, (String[])null);
      }

      return var9;
   }
}
