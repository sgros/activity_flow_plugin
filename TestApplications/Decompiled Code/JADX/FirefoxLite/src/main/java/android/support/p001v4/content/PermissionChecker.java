package android.support.p001v4.content;

import android.content.Context;
import android.os.Process;
import android.support.p001v4.app.AppOpsManagerCompat;

/* renamed from: android.support.v4.content.PermissionChecker */
public final class PermissionChecker {
    public static int checkPermission(Context context, String str, int i, int i2, String str2) {
        if (context.checkPermission(str, i, i2) == -1) {
            return -1;
        }
        str = AppOpsManagerCompat.permissionToOp(str);
        if (str == null) {
            return 0;
        }
        if (str2 == null) {
            String[] packagesForUid = context.getPackageManager().getPackagesForUid(i2);
            if (packagesForUid == null || packagesForUid.length <= 0) {
                return -1;
            }
            str2 = packagesForUid[0];
        }
        if (AppOpsManagerCompat.noteProxyOpNoThrow(context, str, str2) != 0) {
            return -2;
        }
        return 0;
    }

    public static int checkSelfPermission(Context context, String str) {
        return PermissionChecker.checkPermission(context, str, Process.myPid(), Process.myUid(), context.getPackageName());
    }
}
