package androidx.work.impl.background.systemalarm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import androidx.work.Logger;
import androidx.work.impl.utils.PackageManagerHelper;

public class ConstraintProxyUpdateReceiver extends BroadcastReceiver {
   private static final String TAG = Logger.tagWithPrefix("ConstrntProxyUpdtRecvr");

   public static Intent newConstraintProxyUpdateIntent(Context var0, boolean var1, boolean var2, boolean var3, boolean var4) {
      Intent var5 = new Intent("androidx.work.impl.background.systemalarm.UpdateProxies");
      var5.setComponent(new ComponentName(var0, ConstraintProxyUpdateReceiver.class));
      var5.putExtra("KEY_BATTERY_NOT_LOW_PROXY_ENABLED", var1).putExtra("KEY_BATTERY_CHARGING_PROXY_ENABLED", var2).putExtra("KEY_STORAGE_NOT_LOW_PROXY_ENABLED", var3).putExtra("KEY_NETWORK_STATE_PROXY_ENABLED", var4);
      return var5;
   }

   public void onReceive(Context var1, Intent var2) {
      String var3;
      if (var2 != null) {
         var3 = var2.getAction();
      } else {
         var3 = null;
      }

      if (!"androidx.work.impl.background.systemalarm.UpdateProxies".equals(var3)) {
         Logger.get().debug(TAG, String.format("Ignoring unknown action %s", var3));
      } else {
         boolean var4 = var2.getBooleanExtra("KEY_BATTERY_NOT_LOW_PROXY_ENABLED", false);
         boolean var5 = var2.getBooleanExtra("KEY_BATTERY_CHARGING_PROXY_ENABLED", false);
         boolean var6 = var2.getBooleanExtra("KEY_STORAGE_NOT_LOW_PROXY_ENABLED", false);
         boolean var7 = var2.getBooleanExtra("KEY_NETWORK_STATE_PROXY_ENABLED", false);
         Logger.get().debug(TAG, String.format("Updating proxies: BatteryNotLowProxy enabled (%s), BatteryChargingProxy enabled (%s), StorageNotLowProxy (%s), NetworkStateProxy enabled (%s)", var4, var5, var6, var7));
         PackageManagerHelper.setComponentEnabled(var1, ConstraintProxy.BatteryNotLowProxy.class, var4);
         PackageManagerHelper.setComponentEnabled(var1, ConstraintProxy.BatteryChargingProxy.class, var5);
         PackageManagerHelper.setComponentEnabled(var1, ConstraintProxy.StorageNotLowProxy.class, var6);
         PackageManagerHelper.setComponentEnabled(var1, ConstraintProxy.NetworkStateProxy.class, var7);
      }

   }
}
