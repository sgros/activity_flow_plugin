package com.google.zxing.client.result;

public abstract class ParsedResult {
   private final ParsedResultType type;

   protected ParsedResult(ParsedResultType var1) {
      this.type = var1;
   }

   public static void maybeAppend(String var0, StringBuilder var1) {
      if (var0 != null && !var0.isEmpty()) {
         if (var1.length() > 0) {
            var1.append('\n');
         }

         var1.append(var0);
      }

   }

   public static void maybeAppend(String[] var0, StringBuilder var1) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            maybeAppend(var0[var3], var1);
         }
      }

   }

   public abstract String getDisplayResult();

   public final ParsedResultType getType() {
      return this.type;
   }

   public final String toString() {
      return this.getDisplayResult();
   }
}
