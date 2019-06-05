package com.adjust.sdk.plugin;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class MacAddressUtil {
   public static String getMacAddress(Context var0) {
      String var1 = getRawMacAddress(var0);
      return var1 == null ? null : removeSpaceString(var1.toUpperCase(Locale.US));
   }

   private static String getRawMacAddress(Context var0) {
      String var1 = loadAddress("wlan0");
      if (var1 != null) {
         return var1;
      } else {
         var1 = loadAddress("eth0");
         if (var1 != null) {
            return var1;
         } else {
            String var3;
            try {
               var3 = ((WifiManager)var0.getSystemService("wifi")).getConnectionInfo().getMacAddress();
            } catch (Exception var2) {
               return null;
            }

            if (var3 != null) {
               return var3;
            } else {
               return null;
            }
         }
      }
   }

   private static String loadAddress(String var0) {
      boolean var10001;
      StringBuilder var9;
      BufferedReader var10;
      char[] var11;
      try {
         StringBuilder var1 = new StringBuilder();
         var1.append("/sys/class/net/");
         var1.append(var0);
         var1.append("/address");
         String var2 = var1.toString();
         var9 = new StringBuilder(1000);
         FileReader var3 = new FileReader(var2);
         var10 = new BufferedReader(var3, 1024);
         var11 = new char[1024];
      } catch (IOException var8) {
         var10001 = false;
         return null;
      }

      while(true) {
         int var4;
         try {
            var4 = var10.read(var11);
         } catch (IOException var6) {
            var10001 = false;
            break;
         }

         if (var4 == -1) {
            try {
               var10.close();
               var0 = var9.toString();
               return var0;
            } catch (IOException var5) {
               var10001 = false;
               break;
            }
         }

         try {
            var9.append(String.valueOf(var11, 0, var4));
         } catch (IOException var7) {
            var10001 = false;
            break;
         }
      }

      return null;
   }

   private static String removeSpaceString(String var0) {
      if (var0 == null) {
         return null;
      } else {
         var0 = var0.replaceAll("\\s", "");
         return TextUtils.isEmpty(var0) ? null : var0;
      }
   }
}
