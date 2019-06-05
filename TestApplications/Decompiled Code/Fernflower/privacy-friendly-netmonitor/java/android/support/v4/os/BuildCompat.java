package android.support.v4.os;

import android.os.Build.VERSION;

public class BuildCompat {
   private BuildCompat() {
   }

   @Deprecated
   public static boolean isAtLeastN() {
      boolean var0;
      if (VERSION.SDK_INT >= 24) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   @Deprecated
   public static boolean isAtLeastNMR1() {
      boolean var0;
      if (VERSION.SDK_INT >= 25) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAtLeastO() {
      boolean var0;
      if (VERSION.SDK_INT >= 26) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAtLeastOMR1() {
      boolean var0;
      if (!VERSION.CODENAME.startsWith("OMR") && !isAtLeastP()) {
         var0 = false;
      } else {
         var0 = true;
      }

      return var0;
   }

   public static boolean isAtLeastP() {
      return VERSION.CODENAME.equals("P");
   }
}
