// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.content.IntentFilter;
import android.content.Intent;
import android.content.Context;
import androidx.work.Logger;
import android.content.BroadcastReceiver;

public abstract class BroadcastReceiverConstraintTracker<T> extends ConstraintTracker<T>
{
    private static final String TAG;
    private final BroadcastReceiver mBroadcastReceiver;
    
    static {
        TAG = Logger.tagWithPrefix("BrdcstRcvrCnstrntTrckr");
    }
    
    public BroadcastReceiverConstraintTracker(final Context context) {
        super(context);
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent != null) {
                    BroadcastReceiverConstraintTracker.this.onBroadcastReceive(context, intent);
                }
            }
        };
    }
    
    public abstract IntentFilter getIntentFilter();
    
    public abstract void onBroadcastReceive(final Context p0, final Intent p1);
    
    @Override
    public void startTracking() {
        Logger.get().debug(BroadcastReceiverConstraintTracker.TAG, String.format("%s: registering receiver", this.getClass().getSimpleName()), new Throwable[0]);
        this.mAppContext.registerReceiver(this.mBroadcastReceiver, this.getIntentFilter());
    }
    
    @Override
    public void stopTracking() {
        Logger.get().debug(BroadcastReceiverConstraintTracker.TAG, String.format("%s: unregistering receiver", this.getClass().getSimpleName()), new Throwable[0]);
        this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
    }
}
