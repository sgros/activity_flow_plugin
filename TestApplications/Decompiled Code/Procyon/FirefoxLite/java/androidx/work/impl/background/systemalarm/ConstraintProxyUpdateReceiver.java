// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import androidx.work.impl.utils.PackageManagerHelper;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import androidx.work.Logger;
import android.content.BroadcastReceiver;

public class ConstraintProxyUpdateReceiver extends BroadcastReceiver
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("ConstrntProxyUpdtRecvr");
    }
    
    public static Intent newConstraintProxyUpdateIntent(final Context context, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        final Intent intent = new Intent("androidx.work.impl.background.systemalarm.UpdateProxies");
        intent.setComponent(new ComponentName(context, (Class)ConstraintProxyUpdateReceiver.class));
        intent.putExtra("KEY_BATTERY_NOT_LOW_PROXY_ENABLED", b).putExtra("KEY_BATTERY_CHARGING_PROXY_ENABLED", b2).putExtra("KEY_STORAGE_NOT_LOW_PROXY_ENABLED", b3).putExtra("KEY_NETWORK_STATE_PROXY_ENABLED", b4);
        return intent;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        String action;
        if (intent != null) {
            action = intent.getAction();
        }
        else {
            action = null;
        }
        if (!"androidx.work.impl.background.systemalarm.UpdateProxies".equals(action)) {
            Logger.get().debug(ConstraintProxyUpdateReceiver.TAG, String.format("Ignoring unknown action %s", action), new Throwable[0]);
        }
        else {
            final boolean booleanExtra = intent.getBooleanExtra("KEY_BATTERY_NOT_LOW_PROXY_ENABLED", false);
            final boolean booleanExtra2 = intent.getBooleanExtra("KEY_BATTERY_CHARGING_PROXY_ENABLED", false);
            final boolean booleanExtra3 = intent.getBooleanExtra("KEY_STORAGE_NOT_LOW_PROXY_ENABLED", false);
            final boolean booleanExtra4 = intent.getBooleanExtra("KEY_NETWORK_STATE_PROXY_ENABLED", false);
            Logger.get().debug(ConstraintProxyUpdateReceiver.TAG, String.format("Updating proxies: BatteryNotLowProxy enabled (%s), BatteryChargingProxy enabled (%s), StorageNotLowProxy (%s), NetworkStateProxy enabled (%s)", booleanExtra, booleanExtra2, booleanExtra3, booleanExtra4), new Throwable[0]);
            PackageManagerHelper.setComponentEnabled(context, ConstraintProxy.BatteryNotLowProxy.class, booleanExtra);
            PackageManagerHelper.setComponentEnabled(context, ConstraintProxy.BatteryChargingProxy.class, booleanExtra2);
            PackageManagerHelper.setComponentEnabled(context, ConstraintProxy.StorageNotLowProxy.class, booleanExtra3);
            PackageManagerHelper.setComponentEnabled(context, ConstraintProxy.NetworkStateProxy.class, booleanExtra4);
        }
    }
}
