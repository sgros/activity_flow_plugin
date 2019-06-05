package com.bumptech.glide.manager;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class DefaultConnectivityMonitorFactory implements ConnectivityMonitorFactory {
   public ConnectivityMonitor build(Context var1, ConnectivityMonitor.ConnectivityListener var2) {
      boolean var3;
      if (ContextCompat.checkSelfPermission(var1, "android.permission.ACCESS_NETWORK_STATE") == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      Object var4;
      if (var3) {
         var4 = new DefaultConnectivityMonitor(var1, var2);
      } else {
         var4 = new NullConnectivityMonitor();
      }

      return (ConnectivityMonitor)var4;
   }
}
