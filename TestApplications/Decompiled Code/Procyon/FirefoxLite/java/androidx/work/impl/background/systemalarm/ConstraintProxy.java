// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.background.systemalarm;

import android.content.Intent;
import androidx.work.Constraints;
import java.util.Iterator;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import java.util.List;
import android.content.Context;
import androidx.work.Logger;
import android.content.BroadcastReceiver;

abstract class ConstraintProxy extends BroadcastReceiver
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("ConstraintProxy");
    }
    
    static void updateAll(final Context context, final List<WorkSpec> list) {
        final Iterator<WorkSpec> iterator = list.iterator();
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        int n5;
        int n6;
        int n7;
        int n8;
        while (true) {
            n5 = n;
            n6 = n2;
            n7 = n3;
            n8 = n4;
            if (!iterator.hasNext()) {
                break;
            }
            final Constraints constraints = iterator.next().constraints;
            n5 = (n | (constraints.requiresBatteryNotLow() ? 1 : 0));
            n6 = (n2 | (constraints.requiresCharging() ? 1 : 0));
            n7 = (n3 | (constraints.requiresStorageNotLow() ? 1 : 0));
            n8 = (n4 | ((constraints.getRequiredNetworkType() != NetworkType.NOT_REQUIRED) ? 1 : 0));
            n = n5;
            n2 = n6;
            n3 = n7;
            n4 = n8;
            if (n5 == 0) {
                continue;
            }
            n = n5;
            n2 = n6;
            n3 = n7;
            n4 = n8;
            if (n6 == 0) {
                continue;
            }
            n = n5;
            n2 = n6;
            n3 = n7;
            n4 = n8;
            if (n7 == 0) {
                continue;
            }
            n = n5;
            n2 = n6;
            n3 = n7;
            if ((n4 = n8) != 0) {
                break;
            }
        }
        context.sendBroadcast(ConstraintProxyUpdateReceiver.newConstraintProxyUpdateIntent(context, (boolean)(n5 != 0), (boolean)(n6 != 0), (boolean)(n7 != 0), (boolean)(n8 != 0)));
    }
    
    public void onReceive(final Context context, final Intent intent) {
        Logger.get().debug(ConstraintProxy.TAG, String.format("onReceive : %s", intent), new Throwable[0]);
        context.startService(CommandHandler.createConstraintsChangedIntent(context));
    }
    
    public static class BatteryChargingProxy extends ConstraintProxy
    {
    }
    
    public static class BatteryNotLowProxy extends ConstraintProxy
    {
    }
    
    public static class NetworkStateProxy extends ConstraintProxy
    {
    }
    
    public static class StorageNotLowProxy extends ConstraintProxy
    {
    }
}
