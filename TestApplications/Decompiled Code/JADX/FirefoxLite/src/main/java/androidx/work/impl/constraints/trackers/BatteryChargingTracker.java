package androidx.work.impl.constraints.trackers;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import androidx.work.Logger;

public class BatteryChargingTracker extends BroadcastReceiverConstraintTracker<Boolean> {
    private static final String TAG = Logger.tagWithPrefix("BatteryChrgTracker");

    public BatteryChargingTracker(Context context) {
        super(context);
    }

    public Boolean getInitialState() {
        Intent registerReceiver = this.mAppContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver != null) {
            return Boolean.valueOf(isBatteryChangedIntentCharging(registerReceiver));
        }
        Logger.get().error(TAG, "getInitialState - null intent received", new Throwable[0]);
        return null;
    }

    public IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        if (VERSION.SDK_INT >= 23) {
            intentFilter.addAction("android.os.action.CHARGING");
            intentFilter.addAction("android.os.action.DISCHARGING");
        } else {
            intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
            intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        }
        return intentFilter;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0065  */
    public void onBroadcastReceive(android.content.Context r6, android.content.Intent r7) {
        /*
        r5 = this;
        r6 = r7.getAction();
        if (r6 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r7 = androidx.work.Logger.get();
        r0 = TAG;
        r1 = "Received %s";
        r2 = 1;
        r3 = new java.lang.Object[r2];
        r4 = 0;
        r3[r4] = r6;
        r1 = java.lang.String.format(r1, r3);
        r3 = new java.lang.Throwable[r4];
        r7.debug(r0, r1, r3);
        r7 = -1;
        r0 = r6.hashCode();
        r1 = -1886648615; // 0xffffffff8f8c06d9 float:-1.3807703E-29 double:NaN;
        if (r0 == r1) goto L_0x0056;
    L_0x0028:
        r1 = -54942926; // 0xfffffffffcb9a332 float:-7.711079E36 double:NaN;
        if (r0 == r1) goto L_0x004c;
    L_0x002d:
        r1 = 948344062; // 0x388694fe float:6.41737E-5 double:4.685442215E-315;
        if (r0 == r1) goto L_0x0042;
    L_0x0032:
        r1 = 1019184907; // 0x3cbf870b float:0.023379823 double:5.035442493E-315;
        if (r0 == r1) goto L_0x0038;
    L_0x0037:
        goto L_0x0060;
    L_0x0038:
        r0 = "android.intent.action.ACTION_POWER_CONNECTED";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0060;
    L_0x0040:
        r6 = 2;
        goto L_0x0061;
    L_0x0042:
        r0 = "android.os.action.CHARGING";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0060;
    L_0x004a:
        r6 = 0;
        goto L_0x0061;
    L_0x004c:
        r0 = "android.os.action.DISCHARGING";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0060;
    L_0x0054:
        r6 = 1;
        goto L_0x0061;
    L_0x0056:
        r0 = "android.intent.action.ACTION_POWER_DISCONNECTED";
        r6 = r6.equals(r0);
        if (r6 == 0) goto L_0x0060;
    L_0x005e:
        r6 = 3;
        goto L_0x0061;
    L_0x0060:
        r6 = -1;
    L_0x0061:
        switch(r6) {
            case 0: goto L_0x007d;
            case 1: goto L_0x0075;
            case 2: goto L_0x006d;
            case 3: goto L_0x0065;
            default: goto L_0x0064;
        };
    L_0x0064:
        goto L_0x0084;
    L_0x0065:
        r6 = java.lang.Boolean.valueOf(r4);
        r5.setState(r6);
        goto L_0x0084;
    L_0x006d:
        r6 = java.lang.Boolean.valueOf(r2);
        r5.setState(r6);
        goto L_0x0084;
    L_0x0075:
        r6 = java.lang.Boolean.valueOf(r4);
        r5.setState(r6);
        goto L_0x0084;
    L_0x007d:
        r6 = java.lang.Boolean.valueOf(r2);
        r5.setState(r6);
    L_0x0084:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.constraints.trackers.BatteryChargingTracker.onBroadcastReceive(android.content.Context, android.content.Intent):void");
    }

    private boolean isBatteryChangedIntentCharging(Intent intent) {
        if (VERSION.SDK_INT >= 23) {
            int intExtra = intent.getIntExtra("status", -1);
            if (intExtra == 2 || intExtra == 5) {
                return true;
            }
        } else if (intent.getIntExtra("plugged", 0) != 0) {
            return true;
        }
        return false;
    }
}
