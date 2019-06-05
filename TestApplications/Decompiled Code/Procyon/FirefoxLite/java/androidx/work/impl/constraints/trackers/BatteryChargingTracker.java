// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Build$VERSION;
import android.content.Intent;
import android.content.Context;
import androidx.work.Logger;

public class BatteryChargingTracker extends BroadcastReceiverConstraintTracker<Boolean>
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("BatteryChrgTracker");
    }
    
    public BatteryChargingTracker(final Context context) {
        super(context);
    }
    
    private boolean isBatteryChangedIntentCharging(final Intent intent) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        if (sdk_INT >= 23) {
            final int intExtra = intent.getIntExtra("status", -1);
            boolean b2 = b;
            if (intExtra == 2) {
                return b2;
            }
            if (intExtra == 5) {
                b2 = b;
                return b2;
            }
        }
        else if (intent.getIntExtra("plugged", 0) != 0) {
            return b;
        }
        return false;
    }
    
    @Override
    public Boolean getInitialState() {
        final Intent registerReceiver = this.mAppContext.registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            Logger.get().error(BatteryChargingTracker.TAG, "getInitialState - null intent received", new Throwable[0]);
            return null;
        }
        return this.isBatteryChangedIntentCharging(registerReceiver);
    }
    
    @Override
    public IntentFilter getIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        if (Build$VERSION.SDK_INT >= 23) {
            intentFilter.addAction("android.os.action.CHARGING");
            intentFilter.addAction("android.os.action.DISCHARGING");
        }
        else {
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        }
        return intentFilter;
    }
    
    @Override
    public void onBroadcastReceive(final Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action == null) {
            return;
        }
        Logger.get().debug(BatteryChargingTracker.TAG, String.format("Received %s", action), new Throwable[0]);
        final int hashCode = action.hashCode();
        int n = 0;
        Label_0126: {
            if (hashCode != -1886648615) {
                if (hashCode != -54942926) {
                    if (hashCode != 948344062) {
                        if (hashCode == 1019184907) {
                            if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                                n = 2;
                                break Label_0126;
                            }
                        }
                    }
                    else if (action.equals("android.os.action.CHARGING")) {
                        n = 0;
                        break Label_0126;
                    }
                }
                else if (action.equals("android.os.action.DISCHARGING")) {
                    n = 1;
                    break Label_0126;
                }
            }
            else if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                n = 3;
                break Label_0126;
            }
            n = -1;
        }
        switch (n) {
            case 3: {
                this.setState(false);
                break;
            }
            case 2: {
                this.setState(true);
                break;
            }
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
