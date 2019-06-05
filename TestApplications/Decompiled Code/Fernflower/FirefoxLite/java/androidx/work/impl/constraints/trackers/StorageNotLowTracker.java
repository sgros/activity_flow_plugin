package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.work.Logger;

public class StorageNotLowTracker extends BroadcastReceiverConstraintTracker {
   private static final String TAG = Logger.tagWithPrefix("StorageNotLowTracker");

   public StorageNotLowTracker(Context var1) {
      super(var1);
   }

   public Boolean getInitialState() {
      Intent var1 = this.mAppContext.registerReceiver((BroadcastReceiver)null, this.getIntentFilter());
      if (var1 != null && var1.getAction() != null) {
         String var4 = var1.getAction();
         byte var2 = -1;
         int var3 = var4.hashCode();
         if (var3 != -1181163412) {
            if (var3 == -730838620 && var4.equals("android.intent.action.DEVICE_STORAGE_OK")) {
               var2 = 0;
            }
         } else if (var4.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
            var2 = 1;
         }

         switch(var2) {
         case 0:
            return true;
         case 1:
            return false;
         default:
            return null;
         }
      } else {
         return true;
      }
   }

   public IntentFilter getIntentFilter() {
      IntentFilter var1 = new IntentFilter();
      var1.addAction("android.intent.action.DEVICE_STORAGE_OK");
      var1.addAction("android.intent.action.DEVICE_STORAGE_LOW");
      return var1;
   }

   public void onBroadcastReceive(Context var1, Intent var2) {
      if (var2.getAction() != null) {
         Logger.get().debug(TAG, String.format("Received %s", var2.getAction()));
         String var5 = var2.getAction();
         byte var3 = -1;
         int var4 = var5.hashCode();
         if (var4 != -1181163412) {
            if (var4 == -730838620 && var5.equals("android.intent.action.DEVICE_STORAGE_OK")) {
               var3 = 0;
            }
         } else if (var5.equals("android.intent.action.DEVICE_STORAGE_LOW")) {
            var3 = 1;
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
