package androidx.work.impl.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import androidx.work.Logger;

public class PackageManagerHelper {
   private static final String TAG = Logger.tagWithPrefix("PackageManagerHelper");

   public static void setComponentEnabled(Context var0, Class var1, boolean var2) {
      String var6;
      String var10;
      Logger var14;
      Exception var10000;
      label44: {
         PackageManager var3;
         ComponentName var4;
         boolean var10001;
         try {
            var3 = var0.getPackageManager();
            var4 = new ComponentName(var0, var1.getName());
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label44;
         }

         byte var5;
         if (var2) {
            var5 = 1;
         } else {
            var5 = 2;
         }

         String var12;
         try {
            var3.setComponentEnabledSetting(var4, var5, 1);
            var14 = Logger.get();
            var6 = TAG;
            var12 = var1.getName();
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
            break label44;
         }

         if (var2) {
            var10 = "enabled";
         } else {
            var10 = "disabled";
         }

         try {
            var14.debug(var6, String.format("%s %s", var12, var10));
            return;
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
         }
      }

      Exception var13 = var10000;
      var14 = Logger.get();
      var6 = TAG;
      String var11 = var1.getName();
      if (var2) {
         var10 = "enabled";
      } else {
         var10 = "disabled";
      }

      var14.debug(var6, String.format("%s could not be %s", var11, var10), var13);
   }
}
