package com.google.android.exoplayer2.source.dash.manifest;

import java.util.Locale;

public final class UrlTemplate {
   private final int identifierCount;
   private final String[] identifierFormatTags;
   private final int[] identifiers;
   private final String[] urlPieces;

   private UrlTemplate(String[] var1, int[] var2, String[] var3, int var4) {
      this.urlPieces = var1;
      this.identifiers = var2;
      this.identifierFormatTags = var3;
      this.identifierCount = var4;
   }

   public static UrlTemplate compile(String var0) {
      String[] var1 = new String[5];
      int[] var2 = new int[4];
      String[] var3 = new String[4];
      return new UrlTemplate(var1, var2, var3, parseTemplate(var0, var1, var2, var3));
   }

   private static int parseTemplate(String var0, String[] var1, int[] var2, String[] var3) {
      var1[0] = "";
      int var4 = 0;
      int var5 = 0;

      while(var4 < var0.length()) {
         int var6 = var0.indexOf("$", var4);
         byte var7 = -1;
         StringBuilder var8;
         if (var6 == -1) {
            var8 = new StringBuilder();
            var8.append(var1[var5]);
            var8.append(var0.substring(var4));
            var1[var5] = var8.toString();
            var4 = var0.length();
         } else if (var6 != var4) {
            var8 = new StringBuilder();
            var8.append(var1[var5]);
            var8.append(var0.substring(var4, var6));
            var1[var5] = var8.toString();
            var4 = var6;
         } else if (var0.startsWith("$$", var4)) {
            var8 = new StringBuilder();
            var8.append(var1[var5]);
            var8.append("$");
            var1[var5] = var8.toString();
            var4 += 2;
         } else {
            ++var4;
            var6 = var0.indexOf("$", var4);
            String var9 = var0.substring(var4, var6);
            if (var9.equals("RepresentationID")) {
               var2[var5] = 1;
            } else {
               var4 = var9.indexOf("%0");
               String var13;
               if (var4 != -1) {
                  String var10 = var9.substring(var4);
                  var13 = var10;
                  if (!var10.endsWith("d")) {
                     var8 = new StringBuilder();
                     var8.append(var10);
                     var8.append("d");
                     var13 = var8.toString();
                  }

                  var9 = var9.substring(0, var4);
               } else {
                  var13 = "%01d";
               }

               var4 = var9.hashCode();
               byte var12;
               if (var4 != -1950496919) {
                  if (var4 != 2606829) {
                     if (var4 != 38199441) {
                        var12 = var7;
                     } else {
                        var12 = var7;
                        if (var9.equals("Bandwidth")) {
                           var12 = 1;
                        }
                     }
                  } else {
                     var12 = var7;
                     if (var9.equals("Time")) {
                        var12 = 2;
                     }
                  }
               } else {
                  var12 = var7;
                  if (var9.equals("Number")) {
                     var12 = 0;
                  }
               }

               if (var12 != 0) {
                  if (var12 != 1) {
                     if (var12 != 2) {
                        StringBuilder var11 = new StringBuilder();
                        var11.append("Invalid template: ");
                        var11.append(var0);
                        throw new IllegalArgumentException(var11.toString());
                     }

                     var2[var5] = 4;
                  } else {
                     var2[var5] = 3;
                  }
               } else {
                  var2[var5] = 2;
               }

               var3[var5] = var13;
            }

            ++var5;
            var1[var5] = "";
            var4 = var6 + 1;
         }
      }

      return var5;
   }

   public String buildUri(String var1, long var2, int var4, long var5) {
      StringBuilder var7 = new StringBuilder();
      int var8 = 0;

      while(true) {
         int var9 = this.identifierCount;
         if (var8 >= var9) {
            var7.append(this.urlPieces[var9]);
            return var7.toString();
         }

         var7.append(this.urlPieces[var8]);
         int[] var10 = this.identifiers;
         if (var10[var8] == 1) {
            var7.append(var1);
         } else if (var10[var8] == 2) {
            var7.append(String.format(Locale.US, this.identifierFormatTags[var8], var2));
         } else if (var10[var8] == 3) {
            var7.append(String.format(Locale.US, this.identifierFormatTags[var8], var4));
         } else if (var10[var8] == 4) {
            var7.append(String.format(Locale.US, this.identifierFormatTags[var8], var5));
         }

         ++var8;
      }
   }
}
