package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GeoResultParser extends ResultParser {
   private static final Pattern GEO_URL_PATTERN = Pattern.compile("geo:([\\-0-9.]+),([\\-0-9.]+)(?:,([\\-0-9.]+))?(?:\\?(.*))?", 2);

   public GeoParsedResult parse(Result var1) {
      Object var2 = null;
      String var17 = getMassagedText(var1);
      Matcher var3 = GEO_URL_PATTERN.matcher(var17);
      GeoParsedResult var18;
      if (!var3.matches()) {
         var18 = (GeoParsedResult)var2;
      } else {
         String var4 = var3.group(4);

         double var5;
         double var7;
         double var9;
         label77: {
            label62: {
               boolean var10001;
               try {
                  var5 = Double.parseDouble(var3.group(1));
               } catch (NumberFormatException var16) {
                  var10001 = false;
                  break label62;
               }

               var18 = (GeoParsedResult)var2;
               if (var5 > 90.0D) {
                  return var18;
               }

               var18 = (GeoParsedResult)var2;
               if (var5 < -90.0D) {
                  return var18;
               }

               try {
                  var7 = Double.parseDouble(var3.group(2));
               } catch (NumberFormatException var15) {
                  var10001 = false;
                  break label62;
               }

               var18 = (GeoParsedResult)var2;
               if (var7 > 180.0D) {
                  return var18;
               }

               var18 = (GeoParsedResult)var2;
               if (var7 < -180.0D) {
                  return var18;
               }

               try {
                  var17 = var3.group(3);
               } catch (NumberFormatException var14) {
                  var10001 = false;
                  break label62;
               }

               if (var17 == null) {
                  var9 = 0.0D;
                  break label77;
               }

               double var11;
               try {
                  var11 = Double.parseDouble(var3.group(3));
               } catch (NumberFormatException var13) {
                  var10001 = false;
                  break label62;
               }

               var9 = var11;
               if (var11 < 0.0D) {
                  var18 = (GeoParsedResult)var2;
                  return var18;
               }
               break label77;
            }

            var18 = (GeoParsedResult)var2;
            return var18;
         }

         var18 = new GeoParsedResult(var5, var7, var9, var4);
      }

      return var18;
   }
}
