package org.mozilla.rocket.partner;

import android.content.ContentResolver;
import android.provider.Settings.Secure;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

final class PartnerUtil {
   private static final String LOG_TAG = "PartnerUtil";
   static boolean propNA;

   static boolean debugMock() {
      return Log.isLoggable("vendor.partner.mock", 3);
   }

   static String getDeviceIdentifier(ContentResolver var0) {
      return Secure.getString(var0, "android_id");
   }

   private static String getPropertiesFromProcess(String var0) throws Exception {
      String var1 = "";
      Process var2 = (new ProcessBuilder(new String[]{"/system/bin/getprop", var0})).redirectErrorStream(false).start();
      BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.getInputStream(), StandardCharsets.UTF_8));
      var0 = var1;

      while(true) {
         label107: {
            Throwable var16;
            try {
               var1 = var3.readLine();
               break label107;
            } catch (Throwable var14) {
               var16 = var14;
            } finally {
               ;
            }

            try {
               throw var16;
            } finally {
               if (var16 != null) {
                  try {
                     var3.close();
                  } catch (Throwable var12) {
                     var16.addSuppressed(var12);
                  }
               } else {
                  var3.close();
               }

               throw var1;
            }
         }

         if (var1 == null) {
            var3.close();
            var2.destroy();
            return var0;
         }

         var0 = var1;
      }
   }

   private static String getPropertiesFromSystem(String var0) throws Exception {
      return (String)Class.forName("android.os.SystemProperties").getDeclaredMethod("get", String.class).invoke((Object)null, var0);
   }

   static String getProperty(String var0) {
      if (propNA) {
         return null;
      } else if (debugMock() && var0.equals("ro.vendor.partner")) {
         return "moz/1/DEVCFA";
      } else {
         try {
            String var1 = getPropertiesFromSystem(var0);
            return var1;
         } catch (Exception var3) {
            try {
               var0 = getPropertiesFromProcess(var0);
               return var0;
            } catch (Exception var2) {
               propNA = true;
               return null;
            }
         }
      }
   }

   static int log(String var0) {
      return log((Throwable)null, var0);
   }

   static int log(Throwable var0, String var1) {
      if (!Log.isLoggable("vendor.partner", 3)) {
         return 0;
      } else {
         return var0 != null ? Log.d(LOG_TAG, var1, var0) : Log.d(LOG_TAG, var1);
      }
   }
}
