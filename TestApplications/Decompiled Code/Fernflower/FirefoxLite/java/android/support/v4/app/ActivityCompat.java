package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;

public class ActivityCompat extends ContextCompat {
   private static ActivityCompat.PermissionCompatDelegate sDelegate;

   public static void finishAffinity(Activity var0) {
      if (VERSION.SDK_INT >= 16) {
         var0.finishAffinity();
      } else {
         var0.finish();
      }

   }

   public static ActivityCompat.PermissionCompatDelegate getPermissionCompatDelegate() {
      return sDelegate;
   }

   public static void requestPermissions(final Activity var0, final String[] var1, final int var2) {
      if (sDelegate == null || !sDelegate.requestPermissions(var0, var1, var2)) {
         if (VERSION.SDK_INT >= 23) {
            if (var0 instanceof ActivityCompat.RequestPermissionsRequestCodeValidator) {
               ((ActivityCompat.RequestPermissionsRequestCodeValidator)var0).validateRequestPermissionsRequestCode(var2);
            }

            var0.requestPermissions(var1, var2);
         } else if (var0 instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
            (new Handler(Looper.getMainLooper())).post(new Runnable() {
               public void run() {
                  int[] var1x = new int[var1.length];
                  PackageManager var2x = var0.getPackageManager();
                  String var3 = var0.getPackageName();
                  int var4 = var1.length;

                  for(int var5 = 0; var5 < var4; ++var5) {
                     var1x[var5] = var2x.checkPermission(var1[var5], var3);
                  }

                  ((ActivityCompat.OnRequestPermissionsResultCallback)var0).onRequestPermissionsResult(var2, var1, var1x);
               }
            });
         }

      }
   }

   public static boolean shouldShowRequestPermissionRationale(Activity var0, String var1) {
      return VERSION.SDK_INT >= 23 ? var0.shouldShowRequestPermissionRationale(var1) : false;
   }

   public static void startActivityForResult(Activity var0, Intent var1, int var2, Bundle var3) {
      if (VERSION.SDK_INT >= 16) {
         var0.startActivityForResult(var1, var2, var3);
      } else {
         var0.startActivityForResult(var1, var2);
      }

   }

   public interface OnRequestPermissionsResultCallback {
      void onRequestPermissionsResult(int var1, String[] var2, int[] var3);
   }

   public interface PermissionCompatDelegate {
      boolean onActivityResult(Activity var1, int var2, int var3, Intent var4);

      boolean requestPermissions(Activity var1, String[] var2, int var3);
   }

   public interface RequestPermissionsRequestCodeValidator {
      void validateRequestPermissionsRequestCode(int var1);
   }
}
