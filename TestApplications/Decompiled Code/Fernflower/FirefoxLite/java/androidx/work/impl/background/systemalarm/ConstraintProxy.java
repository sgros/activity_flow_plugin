package androidx.work.impl.background.systemalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.work.Constraints;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import java.util.Iterator;
import java.util.List;

abstract class ConstraintProxy extends BroadcastReceiver {
   private static final String TAG = Logger.tagWithPrefix("ConstraintProxy");

   static void updateAll(Context var0, List var1) {
      Iterator var2 = var1.iterator();
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;

      boolean var7;
      boolean var8;
      boolean var9;
      boolean var10;
      while(true) {
         var7 = var3;
         var8 = var4;
         var9 = var5;
         var10 = var6;
         if (!var2.hasNext()) {
            break;
         }

         Constraints var12 = ((WorkSpec)var2.next()).constraints;
         var7 = var3 | var12.requiresBatteryNotLow();
         var8 = var4 | var12.requiresCharging();
         var9 = var5 | var12.requiresStorageNotLow();
         boolean var11;
         if (var12.getRequiredNetworkType() != NetworkType.NOT_REQUIRED) {
            var11 = true;
         } else {
            var11 = false;
         }

         var10 = var6 | var11;
         var3 = var7;
         var4 = var8;
         var5 = var9;
         var6 = var10;
         if (var7) {
            var3 = var7;
            var4 = var8;
            var5 = var9;
            var6 = var10;
            if (var8) {
               var3 = var7;
               var4 = var8;
               var5 = var9;
               var6 = var10;
               if (var9) {
                  var3 = var7;
                  var4 = var8;
                  var5 = var9;
                  var6 = var10;
                  if (var10) {
                     break;
                  }
               }
            }
         }
      }

      var0.sendBroadcast(ConstraintProxyUpdateReceiver.newConstraintProxyUpdateIntent(var0, var7, var8, var9, var10));
   }

   public void onReceive(Context var1, Intent var2) {
      Logger.get().debug(TAG, String.format("onReceive : %s", var2));
      var1.startService(CommandHandler.createConstraintsChangedIntent(var1));
   }

   public static class BatteryChargingProxy extends ConstraintProxy {
   }

   public static class BatteryNotLowProxy extends ConstraintProxy {
   }

   public static class NetworkStateProxy extends ConstraintProxy {
   }

   public static class StorageNotLowProxy extends ConstraintProxy {
   }
}
