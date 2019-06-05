// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.app;

import android.os.Build$VERSION;
import android.app.Service;

public final class ServiceCompat
{
    public static void stopForeground(final Service service, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            service.stopForeground(n);
        }
        else {
            boolean b = true;
            if ((n & 0x1) == 0x0) {
                b = false;
            }
            service.stopForeground(b);
        }
    }
}
