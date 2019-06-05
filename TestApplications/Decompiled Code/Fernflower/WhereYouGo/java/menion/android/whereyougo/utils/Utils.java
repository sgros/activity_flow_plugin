package menion.android.whereyougo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import java.io.Closeable;
import java.io.InputStream;
import java.security.MessageDigest;

public class Utils {
   private static final String TAG = "Utils";
   private static float density = -1.0F;
   private static int densityDpi = 0;
   private static final char[] hexDigit = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
   private static MessageDigest md;
   private static int screenCategory = -1;

   public static String addZerosBefore(String var0, int var1) {
      String var2 = "";

      for(int var3 = 0; var3 < var1 - var0.length(); ++var3) {
         var2 = var2.concat("0");
      }

      return var2 + var0;
   }

   private static String bytesToHex(byte[] var0) {
      int var1 = Math.min(var0.length, 5);
      StringBuilder var2 = new StringBuilder();

      for(int var3 = 0; var3 < var1; ++var3) {
         var2.append(hexDigit[var0[var3] >> 4 & 15]);
         var2.append(hexDigit[var0[var3] & 15]);
      }

      return var2.toString();
   }

   public static void closeStream(Closeable var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (Exception var2) {
            Logger.e("Utils", "closeStream(" + var0 + ")", var2);
         }
      }

   }

   public static float getDensity() {
      return getDpPixels(1.0F);
   }

   public static float getDpPixels(float var0) {
      return getDpPixels(A.getApp(), var0);
   }

   public static float getDpPixels(Context var0, float var1) {
      float var2;
      try {
         if (density == -1.0F) {
            DisplayMetrics var4 = var0.getResources().getDisplayMetrics();
            density = var4.density;
            densityDpi = var4.densityDpi;
         }

         var2 = density;
      } catch (Exception var3) {
         Logger.e("Utils", "getDpPixels(" + var1 + "), e:" + var3);
         var3.printStackTrace();
         return var1;
      }

      var1 *= var2;
      return var1;
   }

   public static int getScreenCategory() {
      if (screenCategory == -1) {
         getDpPixels(1.0F);
         if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 691200) {
            screenCategory = 3;
         } else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 307200) {
            screenCategory = 2;
         } else if (Const.SCREEN_WIDTH * Const.SCREEN_HEIGHT >= 150400) {
            screenCategory = 1;
         } else {
            screenCategory = 0;
         }
      }

      return screenCategory;
   }

   public static int getScreenDpi() {
      getDpPixels(1.0F);
      return densityDpi;
   }

   public static String hashString(String var0) {
      label86: {
         synchronized(Utils.class){}
         if (var0 != null) {
            label85: {
               boolean var5 = false;

               String var1;
               label78: {
                  try {
                     var5 = true;
                     if (var0.length() == 0) {
                        var5 = false;
                        break label85;
                     }

                     if (md == null) {
                        md = MessageDigest.getInstance("SHA1");
                     }

                     md.update(var0.getBytes());
                     var1 = bytesToHex(md.digest());
                     var5 = false;
                     break label78;
                  } catch (Exception var6) {
                     StringBuilder var2 = new StringBuilder();
                     Logger.e("Utils", var2.append("hashString(").append(var0).append(")").toString(), var6);
                     var5 = false;
                  } finally {
                     if (var5) {
                        ;
                     }
                  }

                  var0 = "";
                  break label86;
               }

               var0 = var1;
               break label86;
            }
         }

         var0 = "";
      }

      return var0;
   }

   public static boolean isAndroid201OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 6) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAndroid21OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 7) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAndroid22OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 8) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAndroid23OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 9) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAndroid30OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isAndroidTablet30OrMore() {
      boolean var0;
      if (parseInt(VERSION.SDK) >= 11 && getScreenCategory() == 3) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   public static boolean isIntentAvailable(Intent var0) {
      boolean var1;
      if (A.getApp().getPackageManager().queryIntentActivities(var0, 65536).size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean isIntentAvailable(String var0) {
      return isIntentAvailable(new Intent(var0));
   }

   public static boolean isPermissionAllowed(String var0) {
      boolean var1 = false;

      int var2;
      try {
         var2 = A.getApp().checkPermission(var0, Binder.getCallingPid(), Binder.getCallingUid());
      } catch (Exception var4) {
         Logger.e("Utils", "isPermissionAllowed(" + var0 + ")", var4);
         return var1;
      }

      if (var2 == 0) {
         var1 = true;
      }

      return var1;
   }

   public static boolean parseBoolean(Object var0) {
      return parseBoolean(String.valueOf(var0));
   }

   public static boolean parseBoolean(String var0) {
      boolean var1 = false;

      label19: {
         boolean var2;
         try {
            if (var0.toLowerCase().contains("true")) {
               break label19;
            }

            var2 = var0.contains("1");
         } catch (Exception var3) {
            return var1;
         }

         if (!var2) {
            return var1;
         }
      }

      var1 = true;
      return var1;
   }

   public static double parseDouble(Object var0) {
      return parseDouble(String.valueOf(var0));
   }

   public static double parseDouble(String var0) {
      double var1;
      try {
         var1 = Double.parseDouble(var0.trim());
      } catch (Exception var3) {
         var1 = 0.0D;
      }

      return var1;
   }

   public static float parseFloat(Object var0) {
      return parseFloat(String.valueOf(var0));
   }

   public static float parseFloat(String var0) {
      float var1;
      try {
         var1 = Float.parseFloat(var0.trim());
      } catch (Exception var2) {
         var1 = 0.0F;
      }

      return var1;
   }

   public static int parseInt(Object var0) {
      return parseInt(String.valueOf(var0));
   }

   public static int parseInt(String var0) {
      int var1;
      try {
         var1 = Integer.parseInt(var0.trim());
      } catch (Exception var2) {
         var1 = 0;
      }

      return var1;
   }

   public static Integer parseInteger(Object var0) {
      return parseInteger(String.valueOf(var0));
   }

   public static Integer parseInteger(String var0) {
      Integer var2;
      try {
         var2 = Integer.valueOf(var0.trim());
      } catch (Exception var1) {
         var2 = 0;
      }

      return var2;
   }

   public static long parseLong(Object var0) {
      return parseLong(String.valueOf(var0));
   }

   public static long parseLong(String var0) {
      long var1;
      try {
         var1 = Long.parseLong(var0.trim());
      } catch (Exception var3) {
         var1 = 0L;
      }

      return var1;
   }

   public static String streamToString(InputStream param0) {
      // $FF: Couldn't be decompiled
   }
}
