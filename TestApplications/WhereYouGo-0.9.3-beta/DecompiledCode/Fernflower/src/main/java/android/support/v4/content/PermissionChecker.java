package android.support.v4.content;

import android.content.Context;
import android.os.Binder;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.v4.app.AppOpsManagerCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class PermissionChecker {
   public static final int PERMISSION_DENIED = -1;
   public static final int PERMISSION_DENIED_APP_OP = -2;
   public static final int PERMISSION_GRANTED = 0;

   private PermissionChecker() {
   }

   public static int checkCallingOrSelfPermission(@NonNull Context var0, @NonNull String var1) {
      String var2;
      if (Binder.getCallingPid() == Process.myPid()) {
         var2 = var0.getPackageName();
      } else {
         var2 = null;
      }

      return checkPermission(var0, var1, Binder.getCallingPid(), Binder.getCallingUid(), var2);
   }

   public static int checkCallingPermission(@NonNull Context var0, @NonNull String var1, String var2) {
      int var3;
      if (Binder.getCallingPid() == Process.myPid()) {
         var3 = -1;
      } else {
         var3 = checkPermission(var0, var1, Binder.getCallingPid(), Binder.getCallingUid(), var2);
      }

      return var3;
   }

   public static int checkPermission(@NonNull Context var0, @NonNull String var1, int var2, int var3, String var4) {
      byte var5 = -1;
      byte var8;
      if (var0.checkPermission(var1, var2, var3) == -1) {
         var8 = var5;
      } else {
         String var6 = AppOpsManagerCompat.permissionToOp(var1);
         if (var6 == null) {
            var8 = 0;
         } else {
            var1 = var4;
            if (var4 == null) {
               String[] var7 = var0.getPackageManager().getPackagesForUid(var3);
               var8 = var5;
               if (var7 == null) {
                  return var8;
               }

               var8 = var5;
               if (var7.length <= 0) {
                  return var8;
               }

               var1 = var7[0];
            }

            if (AppOpsManagerCompat.noteProxyOp(var0, var6, var1) != 0) {
               var8 = -2;
            } else {
               var8 = 0;
            }
         }
      }

      return var8;
   }

   public static int checkSelfPermission(@NonNull Context var0, @NonNull String var1) {
      return checkPermission(var0, var1, Process.myPid(), Process.myUid(), var0.getPackageName());
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface PermissionResult {
   }
}
