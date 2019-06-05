package android.support.p001v4.app;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build.VERSION;

/* renamed from: android.support.v4.app.AppOpsManagerCompat */
public final class AppOpsManagerCompat {
    public static String permissionToOp(String str) {
        return VERSION.SDK_INT >= 23 ? AppOpsManager.permissionToOp(str) : null;
    }

    public static int noteProxyOpNoThrow(Context context, String str, String str2) {
        return VERSION.SDK_INT >= 23 ? ((AppOpsManager) context.getSystemService(AppOpsManager.class)).noteProxyOpNoThrow(str, str2) : 1;
    }
}
