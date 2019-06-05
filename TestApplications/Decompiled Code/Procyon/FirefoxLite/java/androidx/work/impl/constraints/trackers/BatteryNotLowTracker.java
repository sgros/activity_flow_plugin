// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Context;
import androidx.work.Logger;

public class BatteryNotLowTracker extends BroadcastReceiverConstraintTracker<Boolean>
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("BatteryNotLowTracker");
    }
    
    public BatteryNotLowTracker(final Context context) {
        super(context);
    }
    
    @Override
    public Boolean getInitialState() {
        final Intent registerReceiver = this.mAppContext.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            Logger.get().error(BatteryNotLowTracker.TAG, "getInitialState - null intent received", new Throwable[0]);
            return null;
        }
        final int intExtra = registerReceiver.getIntExtra("plugged", 0);
        final int intExtra2 = registerReceiver.getIntExtra("status", -1);
        final float n = registerReceiver.getIntExtra("level", -1) / (float)registerReceiver.getIntExtra("scale", -1);
        boolean b2;
        final boolean b = b2 = true;
        if (intExtra == 0) {
            b2 = b;
            if (intExtra2 != 1) {
                b2 = (n > 0.15f && b);
            }
        }
        return b2;
    }
    
    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_OKAY");
        intentFilter.addAction("android.intent.action.BATTERY_LOW");
        return intentFilter;
    }
    
    @Override
    public void onBroadcastReceive(final Context context, final Intent intent) {
        if (intent.getAction() == null) {
            return;
        }
        Logger.get().debug(BatteryNotLowTracker.TAG, String.format("Received %s", intent.getAction()), new Throwable[0]);
        final String action = intent.getAction();
        int n = -1;
        final int hashCode = action.hashCode();
        if (hashCode != -1980154005) {
            if (hashCode == 490310653) {
                if (action.equals("android.intent.action.BATTERY_LOW")) {
                    n = 1;
                }
            }
        }
        else if (action.equals("android.intent.action.BATTERY_OKAY")) {
            n = 0;
        }
        switch (n) {
            case 1: {
                this.setState(false);
                break;
            }
            case 0: {
                this.setState(true);
                break;
            }
        }
    }
}
