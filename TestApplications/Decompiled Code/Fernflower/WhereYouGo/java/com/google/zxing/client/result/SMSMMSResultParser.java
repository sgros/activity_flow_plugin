package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class SMSMMSResultParser extends ResultParser {
   private static void addNumberVia(Collection var0, Collection var1, String var2) {
      int var3 = var2.indexOf(59);
      if (var3 < 0) {
         var0.add(var2);
         var1.add((Object)null);
      } else {
         var0.add(var2.substring(0, var3));
         String var4 = var2.substring(var3 + 1);
         if (var4.startsWith("via=")) {
            var4 = var4.substring(4);
         } else {
            var4 = null;
         }

         var1.add(var4);
      }

   }

   public SMSParsedResult parse(Result var1) {
      String var2 = getMassagedText(var1);
      SMSParsedResult var11;
      if (!var2.startsWith("sms:") && !var2.startsWith("SMS:") && !var2.startsWith("mms:") && !var2.startsWith("MMS:")) {
         var11 = null;
      } else {
         Map var3 = parseNameValuePairs(var2);
         String var4 = null;
         ArrayList var5 = null;
         boolean var6 = false;
         String var7 = var5;
         boolean var8 = var6;
         String var9 = var4;
         if (var3 != null) {
            var7 = var5;
            var8 = var6;
            var9 = var4;
            if (!var3.isEmpty()) {
               var9 = (String)var3.get("subject");
               var7 = (String)var3.get("body");
               var8 = true;
            }
         }

         int var12 = var2.indexOf(63, 4);
         if (var12 >= 0 && var8) {
            var4 = var2.substring(4, var12);
         } else {
            var4 = var2.substring(4);
         }

         int var13 = -1;
         ArrayList var10 = new ArrayList(1);
         var5 = new ArrayList(1);

         while(true) {
            var12 = var4.indexOf(44, var13 + 1);
            if (var12 <= var13) {
               addNumberVia(var10, var5, var4.substring(var13 + 1));
               var11 = new SMSParsedResult((String[])var10.toArray(new String[var10.size()]), (String[])var5.toArray(new String[var5.size()]), var9, var7);
               break;
            }

            addNumberVia(var10, var5, var4.substring(var13 + 1, var12));
            var13 = var12;
         }
      }

      return var11;
   }
}
