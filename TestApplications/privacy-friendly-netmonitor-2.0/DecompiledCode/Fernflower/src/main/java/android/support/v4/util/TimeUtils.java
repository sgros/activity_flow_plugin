package android.support.v4.util;

import android.support.annotation.RestrictTo;
import java.io.PrintWriter;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class TimeUtils {
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static final int HUNDRED_DAY_FIELD_LEN = 19;
   private static final int SECONDS_PER_DAY = 86400;
   private static final int SECONDS_PER_HOUR = 3600;
   private static final int SECONDS_PER_MINUTE = 60;
   private static char[] sFormatStr = new char[24];
   private static final Object sFormatSync = new Object();

   private TimeUtils() {
   }

   private static int accumField(int var0, int var1, boolean var2, int var3) {
      if (var0 > 99 || var2 && var3 >= 3) {
         return 3 + var1;
      } else if (var0 > 9 || var2 && var3 >= 2) {
         return 2 + var1;
      } else {
         return !var2 && var0 <= 0 ? 0 : 1 + var1;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static void formatDuration(long var0, long var2, PrintWriter var4) {
      if (var0 == 0L) {
         var4.print("--");
      } else {
         formatDuration(var0 - var2, var4, 0);
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static void formatDuration(long var0, PrintWriter var2) {
      formatDuration(var0, var2, 0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static void formatDuration(long param0, PrintWriter param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public static void formatDuration(long param0, StringBuilder param2) {
      // $FF: Couldn't be decompiled
   }

   private static int formatDurationLocked(long var0, int var2) {
      if (sFormatStr.length < var2) {
         sFormatStr = new char[var2];
      }

      char[] var3 = sFormatStr;
      if (var0 == 0L) {
         while(var2 - 1 > 0) {
            var3[0] = (char)32;
         }

         var3[0] = (char)48;
         return 1;
      } else {
         byte var4;
         if (var0 > 0L) {
            var4 = 43;
         } else {
            var4 = 45;
            var0 = -var0;
         }

         int var5 = (int)(var0 % 1000L);
         int var6 = (int)Math.floor((double)(var0 / 1000L));
         int var7;
         if (var6 > 86400) {
            var7 = var6 / 86400;
            var6 -= 86400 * var7;
         } else {
            var7 = 0;
         }

         int var8;
         if (var6 > 3600) {
            var8 = var6 / 3600;
            var6 -= var8 * 3600;
         } else {
            var8 = 0;
         }

         int var9;
         int var10;
         if (var6 > 60) {
            var9 = var6 / 60;
            var10 = var6 - var9 * 60;
         } else {
            var9 = 0;
            var10 = var6;
         }

         boolean var11;
         int var13;
         byte var17;
         if (var2 != 0) {
            var6 = accumField(var7, 1, false, 0);
            if (var6 > 0) {
               var11 = true;
            } else {
               var11 = false;
            }

            var6 += accumField(var8, 1, var11, 2);
            if (var6 > 0) {
               var11 = true;
            } else {
               var11 = false;
            }

            var6 += accumField(var9, 1, var11, 2);
            if (var6 > 0) {
               var11 = true;
            } else {
               var11 = false;
            }

            int var12 = var6 + accumField(var10, 1, var11, 2);
            if (var12 > 0) {
               var17 = 3;
            } else {
               var17 = 0;
            }

            var12 += accumField(var5, 2, true, var17) + 1;
            var6 = 0;

            while(true) {
               var13 = var6;
               if (var12 >= var2) {
                  break;
               }

               var3[var6] = (char)32;
               ++var6;
               ++var12;
            }
         } else {
            var13 = 0;
         }

         var3[var13] = (char)var4;
         int var16 = var13 + 1;
         boolean var14;
         if (var2 != 0) {
            var14 = true;
         } else {
            var14 = false;
         }

         var7 = printField(var3, var7, 'd', var16, false, 0);
         if (var7 != var16) {
            var11 = true;
         } else {
            var11 = false;
         }

         if (var14) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var8 = printField(var3, var8, 'h', var7, var11, var17);
         if (var8 != var16) {
            var11 = true;
         } else {
            var11 = false;
         }

         if (var14) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var8 = printField(var3, var9, 'm', var8, var11, var17);
         if (var8 != var16) {
            var11 = true;
         } else {
            var11 = false;
         }

         if (var14) {
            var17 = 2;
         } else {
            var17 = 0;
         }

         var6 = printField(var3, var10, 's', var8, var11, var17);
         byte var15;
         if (var14 && var6 != var16) {
            var15 = 3;
         } else {
            var15 = 0;
         }

         var2 = printField(var3, var5, 'm', var6, true, var15);
         var3[var2] = (char)115;
         return var2 + 1;
      }
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
      if ((!var4 || var5 < 3) && var1 <= 99) {
         var6 = var3;
      } else {
         var7 = var1 / 100;
         var0[var3] = (char)((char)(var7 + 48));
         var6 = var3 + 1;
         var1 -= var7 * 100;
      }

      label42: {
         if ((!var4 || var5 < 2) && var1 <= 9) {
            var7 = var6;
            var5 = var1;
            if (var3 == var6) {
               break label42;
            }
         }

         var3 = var1 / 10;
         var0[var6] = (char)((char)(var3 + 48));
         var7 = var6 + 1;
         var5 = var1 - var3 * 10;
      }

      var0[var7] = (char)((char)(var5 + 48));
      var1 = var7 + 1;
      var0[var1] = (char)var2;
      var6 = var1 + 1;
      return var6;
   }
}
