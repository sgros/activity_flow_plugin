package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;

public abstract class BroadcastReceiverConstraintTracker<T> extends ConstraintTracker<T> {
    private static final String TAG = Logger.tagWithPrefix("BrdcstRcvrCnstrntTrckr");
    private final BroadcastReceiver mBroadcastReceiver = new C02781();

    /* renamed from: androidx.work.impl.constraints.trackers.BroadcastReceiverConstraintTracker$1 */
    class C02781 extends BroadcastReceiver {
        C02781() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                BroadcastReceiverConstraintTracker.this.onBroadcastReceive(context, intent);
            }
        }
    }

    public abstract IntentFilter getIntentFilter();

    public abstract void onBroadcastReceive(Context context, Intent intent);

    public BroadcastReceiverConstraintTracker(Context context) {
        super(context);
    }

    public void startTracking() {
        Logger.get().debug(TAG, String.format("%s: registering receiver", new Object[]{getClass().getSimpleName()}), new Throwable[0]);
        this.mAppContext.registerReceiver(this.mBroadcastReceiver, getIntentFilter());
    }

    public void stopTracking() {
        Logger.get().debug(TAG, String.format("%s: unregistering receiver", new Object[]{getClass().getSimpleName()}), new Throwable[0]);
        this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
    }
}
