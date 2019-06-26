package com.google.zxing.client.result;

import java.util.regex.Pattern;

public final class URIParsedResult extends ParsedResult {
   private static final Pattern USER_IN_HOST = Pattern.compile(":/*([^/@]+)@[^/]+");
   private final String title;
   private final String uri;

   public URIParsedResult(String var1, String var2) {
      super(ParsedResultType.URI);
      this.uri = massageURI(var1);
      this.title = var2;
   }

   private static boolean isColonFollowedByPortNumber(String var0, int var1) {
      int var2 = var1 + 1;
      int var3 = var0.indexOf(47, var2);
      var1 = var3;
      if (var3 < 0) {
         var1 = var0.length();
      }

      return ResultParser.isSubstringOfDigits(var0, var2, var1 - var2);
   }

   private static String massageURI(String var0) {
      String var1 = var0.trim();
      int var2 = var1.indexOf(58);
      if (var2 >= 0) {
         var0 = var1;
         if (!isColonFollowedByPortNumber(var1, var2)) {
            return var0;
         }
      }

      var0 = "http://" + var1;
      return var0;
   }

   public String getDisplayResult() {
      StringBuilder var1 = new StringBuilder(30);
      maybeAppend(this.title, var1);
      maybeAppend(this.uri, var1);
      return var1.toString();
   }

   public String getTitle() {
      return this.title;
   }

   public String getURI() {
      return this.uri;
   }

   public boolean isPossiblyMaliciousURI() {
      return USER_IN_HOST.matcher(this.uri).find();
   }
}
