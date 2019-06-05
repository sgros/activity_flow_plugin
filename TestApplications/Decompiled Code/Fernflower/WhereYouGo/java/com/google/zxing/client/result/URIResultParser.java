package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class URIResultParser extends ResultParser {
   private static final Pattern URL_WITHOUT_PROTOCOL_PATTERN = Pattern.compile("([a-zA-Z0-9\\-]+\\.){1,6}[a-zA-Z]{2,}(:\\d{1,5})?(/|\\?|$)");
   private static final Pattern URL_WITH_PROTOCOL_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9+-.]+:");

   static boolean isBasicallyValidURI(String var0) {
      boolean var1 = false;
      boolean var2;
      if (var0.contains(" ")) {
         var2 = var1;
      } else {
         Matcher var3 = URL_WITH_PROTOCOL_PATTERN.matcher(var0);
         if (var3.find() && var3.start() == 0) {
            var2 = true;
         } else {
            Matcher var4 = URL_WITHOUT_PROTOCOL_PATTERN.matcher(var0);
            var2 = var1;
            if (var4.find()) {
               var2 = var1;
               if (var4.start() == 0) {
                  var2 = true;
               }
            }
         }
      }

      return var2;
   }

   public URIParsedResult parse(Result var1) {
      String var2 = getMassagedText(var1);
      URIParsedResult var3;
      if (!var2.startsWith("URL:") && !var2.startsWith("URI:")) {
         var2 = var2.trim();
         if (isBasicallyValidURI(var2)) {
            var3 = new URIParsedResult(var2, (String)null);
         } else {
            var3 = null;
         }
      } else {
         var3 = new URIParsedResult(var2.substring(4).trim(), (String)null);
      }

      return var3;
   }
}
