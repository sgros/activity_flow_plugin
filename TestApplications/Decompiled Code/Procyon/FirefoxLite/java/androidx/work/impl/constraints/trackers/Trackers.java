// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.content.Context;

public class Trackers
{
    private static Trackers sInstance;
    private BatteryChargingTracker mBatteryChargingTracker;
    private BatteryNotLowTracker mBatteryNotLowTracker;
    private NetworkStateTracker mNetworkStateTracker;
    private StorageNotLowTracker mStorageNotLowTracker;
    
    private Trackers(Context applicationContext) {
        applicationContext = applicationContext.getApplicationContext();
        this.mBatteryChargingTracker = new BatteryChargingTracker(applicationContext);
        this.mBatteryNotLowTracker = new BatteryNotLowTracker(applicationContext);
        this.mNetworkStateTracker = new NetworkStateTracker(applicationContext);
        this.mStorageNotLowTracker = new StorageNotLowTracker(applicationContext);
    }
    
    public static Trackers getInstance(final Context context) {
        synchronized (Trackers.class) {
            if (Trackers.sInstance == null) {
                Trackers.sInstance = new Trackers(context);
            }
            return Trackers.sInstance;
        }
    }
    
    public BatteryChargingTracker getBatteryChargingTracker() {
        return this.mBatteryChargingTracker;
    }
    
    public BatteryNotLowTracker getBatteryNotLowTracker() {
        return this.mBatteryNotLowTracker;
    }
    
    public NetworkStateTracker getNetworkStateTracker() {
        return this.mNetworkStateTracker;
    }
    
    public StorageNotLowTracker getStorageNotLowTracker() {
        return this.mStorageNotLowTracker;
    }
}
