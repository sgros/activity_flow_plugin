package org.osmdroid.tileprovider.modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;

public class NetworkAvailabliltyCheck implements INetworkAvailablityCheck {
   private final ConnectivityManager mConnectionManager;
   private final boolean mHasNetworkStatePermission;
   private final boolean mIsX86;

   public NetworkAvailabliltyCheck(Context var1) {
      this.mConnectionManager = (ConnectivityManager)var1.getSystemService("connectivity");
      this.mIsX86 = "Android-x86".equalsIgnoreCase(Build.BRAND);
      boolean var2;
      if (var1.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", var1.getPackageName()) == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mHasNetworkStatePermission = var2;
   }

   public boolean getNetworkAvailable() {
      boolean var1 = this.mHasNetworkStatePermission;
      boolean var2 = true;
      if (!var1) {
         return true;
      } else {
         NetworkInfo var3 = this.mConnectionManager.getActiveNetworkInfo();
         if (var3 == null) {
            return false;
         } else if (var3.isConnected()) {
            return true;
         } else if (VERSION.SDK_INT <= 13) {
            return false;
         } else {
            if (!this.mIsX86 || var3.getType() != 9) {
               var2 = false;
            }

            return var2;
         }
      }
   }
}
