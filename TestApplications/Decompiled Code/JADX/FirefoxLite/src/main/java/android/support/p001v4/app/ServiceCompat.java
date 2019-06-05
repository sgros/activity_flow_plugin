package android.support.p001v4.app;

import android.app.Service;
import android.os.Build.VERSION;

/* renamed from: android.support.v4.app.ServiceCompat */
public final class ServiceCompat {
    public static void stopForeground(Service service, int i) {
        if (VERSION.SDK_INT >= 24) {
            service.stopForeground(i);
            return;
        }
        boolean z = true;
        if ((i & 1) == 0) {
            z = false;
        }
        service.stopForeground(z);
    }
}
