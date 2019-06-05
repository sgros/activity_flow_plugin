package android.support.p001v4.app;

import android.app.ActivityManager;
import android.os.Build.VERSION;

/* renamed from: android.support.v4.app.ActivityManagerCompat */
public final class ActivityManagerCompat {
    public static boolean isLowRamDevice(ActivityManager activityManager) {
        return VERSION.SDK_INT >= 19 ? activityManager.isLowRamDevice() : false;
    }
}
