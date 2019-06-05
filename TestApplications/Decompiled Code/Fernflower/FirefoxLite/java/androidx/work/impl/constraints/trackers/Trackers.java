package androidx.work.impl.constraints.trackers;

import android.content.Context;

public class Trackers {
   private static Trackers sInstance;
   private BatteryChargingTracker mBatteryChargingTracker;
   private BatteryNotLowTracker mBatteryNotLowTracker;
   private NetworkStateTracker mNetworkStateTracker;
   private StorageNotLowTracker mStorageNotLowTracker;

   private Trackers(Context var1) {
      var1 = var1.getApplicationContext();
      this.mBatteryChargingTracker = new BatteryChargingTracker(var1);
      this.mBatteryNotLowTracker = new BatteryNotLowTracker(var1);
      this.mNetworkStateTracker = new NetworkStateTracker(var1);
      this.mStorageNotLowTracker = new StorageNotLowTracker(var1);
   }

   public static Trackers getInstance(Context var0) {
      synchronized(Trackers.class){}

      Trackers var4;
      try {
         if (sInstance == null) {
            Trackers var1 = new Trackers(var0);
            sInstance = var1;
         }

         var4 = sInstance;
      } finally {
         ;
      }

      return var4;
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
