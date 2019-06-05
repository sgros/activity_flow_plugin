package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;

public abstract class BroadcastReceiverConstraintTracker extends ConstraintTracker {
   private static final String TAG = Logger.tagWithPrefix("BrdcstRcvrCnstrntTrckr");
   private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         if (var2 != null) {
            BroadcastReceiverConstraintTracker.this.onBroadcastReceive(var1, var2);
         }

      }
   };

   public BroadcastReceiverConstraintTracker(Context var1) {
      super(var1);
   }

   public abstract IntentFilter getIntentFilter();

   public abstract void onBroadcastReceive(Context var1, Intent var2);

   public void startTracking() {
      Logger.get().debug(TAG, String.format("%s: registering receiver", this.getClass().getSimpleName()));
      this.mAppContext.registerReceiver(this.mBroadcastReceiver, this.getIntentFilter());
   }

   public void stopTracking() {
      Logger.get().debug(TAG, String.format("%s: unregistering receiver", this.getClass().getSimpleName()));
      this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
   }
}
