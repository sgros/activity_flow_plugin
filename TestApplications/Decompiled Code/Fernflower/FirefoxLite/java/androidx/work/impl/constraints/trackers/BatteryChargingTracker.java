package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import androidx.work.Logger;

public class BatteryChargingTracker extends BroadcastReceiverConstraintTracker {
   private static final String TAG = Logger.tagWithPrefix("BatteryChrgTracker");

   public BatteryChargingTracker(Context var1) {
      super(var1);
   }

   private boolean isBatteryChangedIntentCharging(Intent var1) {
      int var2 = VERSION.SDK_INT;
      boolean var3 = true;
      boolean var4;
      if (var2 >= 23) {
         var2 = var1.getIntExtra("status", -1);
         var4 = var3;
         if (var2 == 2) {
            return var4;
         }

         if (var2 == 5) {
            var4 = var3;
            return var4;
         }
      } else if (var1.getIntExtra("plugged", 0) != 0) {
         var4 = var3;
         return var4;
      }

      var4 = false;
      return var4;
   }

   public Boolean getInitialState() {
      IntentFilter var1 = new IntentFilter("android.intent.action.BATTERY_CHANGED");
      Intent var2 = this.mAppContext.registerReceiver((BroadcastReceiver)null, var1);
      if (var2 == null) {
         Logger.get().error(TAG, "getInitialState - null intent received");
         return null;
      } else {
         return this.isBatteryChangedIntentCharging(var2);
      }
   }

   public IntentFilter getIntentFilter() {
      IntentFilter var1 = new IntentFilter();
      if (VERSION.SDK_INT >= 23) {
         var1.addAction("android.os.action.CHARGING");
         var1.addAction("android.os.action.DISCHARGING");
      } else {
         var1.addAction("android.intent.action.ACTION_POWER_CONNECTED");
         var1.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
      }

      return var1;
   }

   public void onBroadcastReceive(Context var1, Intent var2) {
      String var4 = var2.getAction();
      if (var4 != null) {
         byte var5;
         label41: {
            Logger.get().debug(TAG, String.format("Received %s", var4));
            int var3 = var4.hashCode();
            if (var3 != -1886648615) {
               if (var3 != -54942926) {
                  if (var3 != 948344062) {
                     if (var3 == 1019184907 && var4.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                        var5 = 2;
                        break label41;
                     }
                  } else if (var4.equals("android.os.action.CHARGING")) {
                     var5 = 0;
                     break label41;
                  }
               } else if (var4.equals("android.os.action.DISCHARGING")) {
                  var5 = 1;
                  break label41;
               }
            } else if (var4.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
               var5 = 3;
               break label41;
            }

            var5 = -1;
         }

         switch(var5) {
         case 0:
            this.setState(true);
            break;
         case 1:
            this.setState(false);
            break;
         case 2:
            this.setState(true);
            break;
         case 3:
            this.setState(false);
         }

      }
   }
}
