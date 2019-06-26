package com.google.android.exoplayer2.util;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import java.util.Iterator;

public final class UriUtil {
   private static final int FRAGMENT = 3;
   private static final int INDEX_COUNT = 4;
   private static final int PATH = 1;
   private static final int QUERY = 2;
   private static final int SCHEME_COLON = 0;

   private UriUtil() {
   }

   private static int[] getUriIndices(String var0) {
      int[] var1 = new int[4];
      if (TextUtils.isEmpty(var0)) {
         var1[0] = -1;
         return var1;
      } else {
         int var2 = var0.length();
         int var3 = var0.indexOf(35);
         if (var3 == -1) {
            var3 = var2;
         }

         int var4;
         label50: {
            var4 = var0.indexOf(63);
            if (var4 != -1) {
               var2 = var4;
               if (var4 <= var3) {
                  break label50;
               }
            }

            var2 = var3;
         }

         int var5;
         label45: {
            var5 = var0.indexOf(47);
            if (var5 != -1) {
               var4 = var5;
               if (var5 <= var2) {
                  break label45;
               }
            }

            var4 = var2;
         }

         int var6 = var0.indexOf(58);
         var5 = var6;
         if (var6 > var4) {
            var5 = -1;
         }

         var4 = var5 + 2;
         boolean var7;
         if (var4 < var2 && var0.charAt(var5 + 1) == '/' && var0.charAt(var4) == '/') {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var7) {
            label32: {
               var6 = var0.indexOf(47, var5 + 3);
               if (var6 != -1) {
                  var4 = var6;
                  if (var6 <= var2) {
                     break label32;
                  }
               }

               var4 = var2;
            }
         } else {
            var4 = var5 + 1;
         }

         var1[0] = var5;
         var1[1] = var4;
         var1[2] = var2;
         var1[3] = var3;
         return var1;
      }
   }

   private static String removeDotSegments(StringBuilder var0, int var1, int var2) {
      if (var1 >= var2) {
         return var0.toString();
      } else {
         int var3 = var1;
         if (var0.charAt(var1) == '/') {
            var3 = var1 + 1;
         }

         var1 = var2;
         var2 = var3;

         while(true) {
            while(true) {
               int var4 = var2;

               int var5;
               while(true) {
                  if (var4 > var1) {
                     return var0.toString();
                  }

                  if (var4 == var1) {
                     var5 = var4;
                     break;
                  }

                  if (var0.charAt(var4) == '/') {
                     var5 = var4 + 1;
                     break;
                  }

                  ++var4;
               }

               int var6 = var2 + 1;
               if (var4 == var6 && var0.charAt(var2) == '.') {
                  var0.delete(var2, var5);
                  var1 -= var5 - var2;
               } else if (var4 == var2 + 2 && var0.charAt(var2) == '.' && var0.charAt(var6) == '.') {
                  var2 = var0.lastIndexOf("/", var2 - 2) + 1;
                  if (var2 > var3) {
                     var4 = var2;
                  } else {
                     var4 = var3;
                  }

                  var0.delete(var4, var5);
                  var1 -= var5 - var4;
               } else {
                  var2 = var4 + 1;
               }
            }
         }
      }
   }

   public static Uri removeQueryParameter(Uri var0, String var1) {
      Builder var2 = var0.buildUpon();
      var2.clearQuery();
      Iterator var3 = var0.getQueryParameterNames().iterator();

      while(true) {
         String var4;
         do {
            if (!var3.hasNext()) {
               return var2.build();
            }

            var4 = (String)var3.next();
         } while(var4.equals(var1));

         Iterator var5 = var0.getQueryParameters(var4).iterator();

         while(var5.hasNext()) {
            var2.appendQueryParameter(var4, (String)var5.next());
         }
      }
   }

   public static String resolve(String var0, String var1) {
      StringBuilder var2 = new StringBuilder();
      String var3 = var0;
      if (var0 == null) {
         var3 = "";
      }

      var0 = var1;
      if (var1 == null) {
         var0 = "";
      }

      int[] var6 = getUriIndices(var0);
      if (var6[0] != -1) {
         var2.append(var0);
         removeDotSegments(var2, var6[1], var6[2]);
         return var2.toString();
      } else {
         int[] var4 = getUriIndices(var3);
         if (var6[3] == 0) {
            var2.append(var3, 0, var4[3]);
            var2.append(var0);
            return var2.toString();
         } else if (var6[2] == 0) {
            var2.append(var3, 0, var4[2]);
            var2.append(var0);
            return var2.toString();
         } else {
            int var5;
            if (var6[1] != 0) {
               var5 = var4[0] + 1;
               var2.append(var3, 0, var5);
               var2.append(var0);
               return removeDotSegments(var2, var6[1] + var5, var5 + var6[2]);
            } else if (var0.charAt(var6[1]) == '/') {
               var2.append(var3, 0, var4[1]);
               var2.append(var0);
               return removeDotSegments(var2, var4[1], var4[1] + var6[2]);
            } else if (var4[0] + 2 < var4[1] && var4[1] == var4[2]) {
               var2.append(var3, 0, var4[1]);
               var2.append('/');
               var2.append(var0);
               return removeDotSegments(var2, var4[1], var4[1] + var6[2] + 1);
            } else {
               var5 = var3.lastIndexOf(47, var4[2] - 1);
               if (var5 == -1) {
                  var5 = var4[1];
               } else {
                  ++var5;
               }

               var2.append(var3, 0, var5);
               var2.append(var0);
               return removeDotSegments(var2, var4[1], var5 + var6[2]);
            }
         }
      }
   }

   public static Uri resolveToUri(String var0, String var1) {
      return Uri.parse(resolve(var0, var1));
   }
}
