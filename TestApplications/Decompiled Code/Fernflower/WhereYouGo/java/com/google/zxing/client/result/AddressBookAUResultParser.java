package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;

public final class AddressBookAUResultParser extends ResultParser {
   private static String[] matchMultipleValuePrefix(String var0, int var1, String var2, boolean var3) {
      ArrayList var4 = null;

      ArrayList var7;
      for(int var5 = 1; var5 <= var1; var4 = var7) {
         String var6 = matchSinglePrefixedField(var0 + var5 + ':', var2, '\r', var3);
         if (var6 == null) {
            break;
         }

         var7 = var4;
         if (var4 == null) {
            var7 = new ArrayList(var1);
         }

         var7.add(var6);
         ++var5;
      }

      String[] var8;
      if (var4 == null) {
         var8 = null;
      } else {
         var8 = (String[])var4.toArray(new String[var4.size()]);
      }

      return var8;
   }

   public AddressBookParsedResult parse(Result var1) {
      String var8 = getMassagedText(var1);
      AddressBookParsedResult var9;
      if (var8.contains("MEMORY") && var8.contains("\r\n")) {
         String var2 = matchSinglePrefixedField("NAME1:", var8, '\r', true);
         String var3 = matchSinglePrefixedField("NAME2:", var8, '\r', true);
         String[] var4 = matchMultipleValuePrefix("TEL", 3, var8, true);
         String[] var5 = matchMultipleValuePrefix("MAIL", 3, var8, true);
         String var6 = matchSinglePrefixedField("MEMORY:", var8, '\r', false);
         String var7 = matchSinglePrefixedField("ADD:", var8, '\r', true);
         String[] var10;
         if (var7 == null) {
            var10 = null;
         } else {
            var10 = new String[]{var7};
         }

         var9 = new AddressBookParsedResult(maybeWrap(var2), (String[])null, var3, var4, (String[])null, var5, (String[])null, (String)null, var6, var10, (String[])null, (String)null, (String)null, (String)null, (String[])null, (String[])null);
      } else {
         var9 = null;
      }

      return var9;
   }
}
