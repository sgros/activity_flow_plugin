package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.List;

public final class VEventResultParser extends ResultParser {
   private static String matchSingleVCardPrefixedField(CharSequence var0, String var1, boolean var2) {
      List var3 = VCardResultParser.matchSingleVCardPrefixedField(var0, var1, var2, false);
      String var4;
      if (var3 != null && !var3.isEmpty()) {
         var4 = (String)var3.get(0);
      } else {
         var4 = null;
      }

      return var4;
   }

   private static String[] matchVCardPrefixedField(CharSequence var0, String var1, boolean var2) {
      List var3 = VCardResultParser.matchVCardPrefixedField(var0, var1, var2, false);
      String[] var6;
      if (var3 != null && !var3.isEmpty()) {
         int var4 = var3.size();
         String[] var7 = new String[var4];
         int var5 = 0;

         while(true) {
            var6 = var7;
            if (var5 >= var4) {
               break;
            }

            var7[var5] = (String)((List)var3.get(var5)).get(0);
            ++var5;
         }
      } else {
         var6 = null;
      }

      return var6;
   }

   private static String stripMailto(String var0) {
      String var1 = var0;
      if (var0 != null) {
         if (!var0.startsWith("mailto:")) {
            var1 = var0;
            if (!var0.startsWith("MAILTO:")) {
               return var1;
            }
         }

         var1 = var0.substring(7);
      }

      return var1;
   }

   public CalendarParsedResult parse(Result var1) {
      String var17 = getMassagedText(var1);
      CalendarParsedResult var18;
      if (var17.indexOf("BEGIN:VEVENT") < 0) {
         var18 = null;
      } else {
         String var2 = matchSingleVCardPrefixedField("SUMMARY", var17, true);
         String var3 = matchSingleVCardPrefixedField("DTSTART", var17, true);
         if (var3 == null) {
            var18 = null;
         } else {
            String var4 = matchSingleVCardPrefixedField("DTEND", var17, true);
            String var5 = matchSingleVCardPrefixedField("DURATION", var17, true);
            String var6 = matchSingleVCardPrefixedField("LOCATION", var17, true);
            String var7 = stripMailto(matchSingleVCardPrefixedField("ORGANIZER", var17, true));
            String[] var8 = matchVCardPrefixedField("ATTENDEE", var17, true);
            int var9;
            if (var8 != null) {
               for(var9 = 0; var9 < var8.length; ++var9) {
                  var8[var9] = stripMailto(var8[var9]);
               }
            }

            String var10 = matchSingleVCardPrefixedField("DESCRIPTION", var17, true);
            var17 = matchSingleVCardPrefixedField("GEO", var17, true);
            double var11;
            double var13;
            if (var17 == null) {
               var11 = Double.NaN;
               var13 = Double.NaN;
            } else {
               var9 = var17.indexOf(59);
               if (var9 < 0) {
                  var18 = null;
                  return var18;
               }

               try {
                  var11 = Double.parseDouble(var17.substring(0, var9));
                  var13 = Double.parseDouble(var17.substring(var9 + 1));
               } catch (NumberFormatException var16) {
                  var18 = null;
                  return var18;
               }
            }

            try {
               var18 = new CalendarParsedResult(var2, var3, var4, var5, var6, var7, var8, var10, var11, var13);
            } catch (IllegalArgumentException var15) {
               var18 = null;
            }
         }
      }

      return var18;
   }
}
