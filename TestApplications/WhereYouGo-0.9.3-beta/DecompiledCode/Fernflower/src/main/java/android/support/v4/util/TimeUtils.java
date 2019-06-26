package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
   public static final int HUNDRED_DAY_FIELD_LEN = 19;
   private static final int SECONDS_PER_DAY = 86400;
   private static final int SECONDS_PER_HOUR = 3600;
   private static final int SECONDS_PER_MINUTE = 60;
   private static char[] sFormatStr = new char[24];
   private static final Object sFormatSync = new Object();

   private TimeUtils() {
   }

   private static int accumField(int var0, int var1, boolean var2, int var3) {
      if (var0 <= 99 && (!var2 || var3 < 3)) {
         if (var0 > 9 || var2 && var3 >= 2) {
            var0 = var1 + 2;
         } else if (!var2 && var0 <= 0) {
            var0 = 0;
         } else {
            var0 = var1 + 1;
         }
      } else {
         var0 = var1 + 3;
      }

      return var0;
   }

   public static void formatDuration(long var0, long var2, PrintWriter var4) {
      if (var0 == 0L) {
         var4.print("--");
      } else {
         formatDuration(var0 - var2, var4, 0);
      }

   }

   public static void formatDuration(long var0, PrintWriter var2) {
      formatDuration(var0, var2, 0);
   }

   public static void formatDuration(long param0, PrintWriter param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static void formatDuration(long param0, StringBuilder param2) {
      // $FF: Couldn't be decompiled
   }

   private static int formatDurationLocked(long var0, int var2) {
      if (sFormatStr.length < var2) {
         sFormatStr = new char[var2];
      }

      char[] var3 = sFormatStr;
      if (var0 != 0L) {
         byte var4;
         if (var0 > 0L) {
            var4 = 43;
         } else {
            var4 = 45;
            var0 = -var0;
         }

         int var5 = (int)(var0 % 1000L);
         int var6 = (int)Math.floor((double)(var0 / 1000L));
         int var7 = 0;
         int var8 = 0;
         int var9 = 0;
         int var10 = var6;
         if (var6 > 86400) {
            var7 = var6 / 86400;
            var10 = var6 - 86400 * var7;
         }

         var6 = var10;
         if (var10 > 3600) {
            var8 = var10 / 3600;
            var6 = var10 - var8 * 3600;
         }

         int var11 = var6;
         if (var6 > 60) {
            var9 = var6 / 60;
            var11 = var6 - var9 * 60;
         }

         int var12 = 0;
         byte var13 = 0;
         boolean var14;
         byte var17;
         if (var2 != 0) {
            var10 = accumField(var7, 1, false, 0);
            if (var10 > 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var10 += accumField(var8, 1, var14, 2);
            if (var10 > 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var10 += accumField(var9, 1, var14, 2);
            if (var10 > 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var6 = var10 + accumField(var11, 1, var14, 2);
            if (var6 > 0) {
               var17 = 3;
            } else {
               var17 = 0;
            }

            var6 += accumField(var5, 2, true, var17) + 1;
            var10 = var13;

            while(true) {
               var12 = var10;
               if (var6 >= var2) {
                  break;
               }

               var3[var10] = (char)32;
               ++var10;
               ++var6;
            }
         }

         var3[var12] = (char)var4;
         var6 = var12 + 1;
         boolean var15;
         if (var2 != 0) {
            var15 = true;
         } else {
            var15 = false;
         }

         var7 = printField(var3, var7, 'd', var6, false, 0);
         if (var7 != var6) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var8 = printField(var3, var8, 'h', var7, var14, var17);
         if (var8 != var6) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var9 = printField(var3, var9, 'm', var8, var14, var17);
         if (var9 != var6) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var15) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var10 = printField(var3, var11, 's', var9, var14, var17);
         byte var16;
         if (var15 && var10 != var6) {
            var16 = 3;
         } else {
            var16 = 0;
         }

         var2 = printField(var3, var5, 'm', var10, true, var16);
         var3[var2] = (char)115;
         ++var2;
      } else {
         while(var2 - 1 < 0) {
            var3[0] = (char)32;
         }

         var3[0] = (char)48;
         var2 = 1;
      }

      return var2;
   }

   private static int printField(char[] var0, int var1, char var2, int var3, boolean var4, int var5) {
      int var6;
      if (!var4) {
         var6 = var3;
         if (var1 <= 0) {
            return var6;
         }
      }

      int var7;
      label40: {
         if (!var4 || var5 < 3) {
            var6 = var1;
            var7 = var3;
            if (var1 <= 99) {
               break label40;
            }
         }

         var6 = var1 / 100;
         var0[var3] = (char)((char)(var6 + 48));
         var7 = var3 + 1;
         var6 = var1 - var6 * 100;
      }

      label41: {
         if ((!var4 || var5 < 2) && var6 <= 9) {
            var5 = var6;
            var1 = var7;
            if (var3 == var7) {
               break label41;
            }
         }

         var3 = var6 / 10;
         var0[var7] = (char)((char)(var3 + 48));
         var1 = var7 + 1;
         var5 = var6 - var3 * 10;
      }

      var0[var1] = (char)((char)(var5 + 48));
      ++var1;
      var0[var1] = (char)var2;
      var6 = var1 + 1;
      return var6;
   }
}
