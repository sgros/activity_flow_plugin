package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;

public class BatteryNotLowTracker extends BroadcastReceiverConstraintTracker {
   private static final String TAG = Logger.tagWithPrefix("BatteryNotLowTracker");

   public BatteryNotLowTracker(Context var1) {
      super(var1);
   }

   public Boolean getInitialState() {
      IntentFilter var1 = new IntentFilter("android.intent.action.BATTERY_CHANGED");
      Intent var9 = this.mAppContext.registerReceiver((BroadcastReceiver)null, var1);
      if (var9 == null) {
         Logger.get().error(TAG, "getInitialState - null intent received");
         return null;
      } else {
         int var2 = var9.getIntExtra("plugged", 0);
         int var3 = var9.getIntExtra("status", -1);
         int var4 = var9.getIntExtra("level", -1);
         int var5 = var9.getIntExtra("scale", -1);
         float var6 = (float)var4 / (float)var5;
         boolean var7 = true;
         boolean var8 = var7;
         if (var2 == 0) {
            var8 = var7;
            if (var3 != 1) {
               if (var6 > 0.15F) {
                  var8 = var7;
               } else {
                  var8 = false;
               }
            }
         }

         return var8;
      }
   }

   public IntentFilter getIntentFilter() {
      IntentFilter var1 = new IntentFilter();
      var1.addAction("android.intent.action.BATTERY_OKAY");
      var1.addAction("android.intent.action.BATTERY_LOW");
      return var1;
   }

   public void onBroadcastReceive(Context var1, Intent var2) {
      if (var2.getAction() != null) {
         Logger.get().debug(TAG, String.format("Received %s", var2.getAction()));
         String var5 = var2.getAction();
         byte var3 = -1;
         int var4 = var5.hashCode();
         if (var4 != -1980154005) {
            if (var4 == 490310653 && var5.equals("android.intent.action.BATTERY_LOW")) {
               var3 = 1;
            }
         } else if (var5.equals("android.intent.action.BATTERY_OKAY")) {
            var3 = 0;
         }

         switch(var3) {
         case 0:
            this.setState(true);
            break;
         case 1:
            this.setState(false);
         }

      }
   }
}
