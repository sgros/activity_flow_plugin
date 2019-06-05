// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import java.util.Iterator;
import android.os.PowerManager;
import android.content.Context;
import java.util.Map;
import java.util.HashMap;
import androidx.work.Logger;
import android.os.PowerManager$WakeLock;
import java.util.WeakHashMap;

public class WakeLocks
{
    private static final String TAG;
    private static final WeakHashMap<PowerManager$WakeLock, String> sWakeLocks;
    
    static {
        TAG = Logger.tagWithPrefix("WakeLocks");
        sWakeLocks = new WeakHashMap<PowerManager$WakeLock, String>();
    }
    
    public static void checkWakeLocks() {
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        Object o = WakeLocks.sWakeLocks;
        synchronized (o) {
            hashMap.putAll(WakeLocks.sWakeLocks);
            // monitorexit(o)
            o = hashMap.keySet().iterator();
            while (((Iterator)o).hasNext()) {
                final PowerManager$WakeLock powerManager$WakeLock = ((Iterator<PowerManager$WakeLock>)o).next();
                if (powerManager$WakeLock != null && powerManager$WakeLock.isHeld()) {
                    Logger.get().warning(WakeLocks.TAG, String.format("WakeLock held for %s", hashMap.get(powerManager$WakeLock)), new Throwable[0]);
                }
            }
        }
    }
    
    public static PowerManager$WakeLock newWakeLock(final Context context, String string) {
        final PowerManager powerManager = (PowerManager)context.getApplicationContext().getSystemService("power");
        final StringBuilder sb = new StringBuilder();
        sb.append("WorkManager: ");
        sb.append(string);
        string = sb.toString();
        final PowerManager$WakeLock wakeLock = powerManager.newWakeLock(1, string);
        synchronized (WakeLocks.sWakeLocks) {
            WakeLocks.sWakeLocks.put(wakeLock, string);
            return wakeLock;
        }
    }
}
