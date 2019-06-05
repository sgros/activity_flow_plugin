package android.support.v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build.VERSION;

public final class AppOpsManagerCompat {
   public static int noteProxyOpNoThrow(Context var0, String var1, String var2) {
      return VERSION.SDK_INT >= 23 ? ((AppOpsManager)var0.getSystemService(AppOpsManager.class)).noteProxyOpNoThrow(var1, var2) : 1;
   }

   public static String permissionToOp(String var0) {
      return VERSION.SDK_INT >= 23 ? AppOpsManager.permissionToOp(var0) : null;
   }
}
