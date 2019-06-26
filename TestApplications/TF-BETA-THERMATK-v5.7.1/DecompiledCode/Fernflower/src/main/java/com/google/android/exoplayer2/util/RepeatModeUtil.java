package com.google.android.exoplayer2.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class RepeatModeUtil {
   public static final int REPEAT_TOGGLE_MODE_ALL = 2;
   public static final int REPEAT_TOGGLE_MODE_NONE = 0;
   public static final int REPEAT_TOGGLE_MODE_ONE = 1;

   private RepeatModeUtil() {
   }

   public static int getNextRepeatMode(int var0, int var1) {
      for(int var2 = 1; var2 <= 2; ++var2) {
         int var3 = (var0 + var2) % 3;
         if (isRepeatModeEnabled(var3, var1)) {
            return var3;
         }
      }

      return var0;
   }

   public static boolean isRepeatModeEnabled(int var0, int var1) {
      boolean var2 = true;
      boolean var3 = true;
      boolean var4 = var2;
      if (var0 != 0) {
         if (var0 != 1) {
            if (var0 != 2) {
               return false;
            }

            if ((var1 & 2) != 0) {
               var4 = var3;
            } else {
               var4 = false;
            }

            return var4;
         }

         if ((var1 & 1) != 0) {
            var4 = var2;
         } else {
            var4 = false;
         }
      }

      return var4;
   }

   @Documented
   @Retention(RetentionPolicy.SOURCE)
   public @interface RepeatToggleModes {
   }
}
