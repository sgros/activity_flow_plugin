package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.ConnectivityManager.NetworkCallback;
import android.os.Build.VERSION;
import android.support.v4.net.ConnectivityManagerCompat;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;

public class NetworkStateTracker extends ConstraintTracker {
   static final String TAG = Logger.tagWithPrefix("NetworkStateTracker");
   private NetworkStateTracker.NetworkStateBroadcastReceiver mBroadcastReceiver;
   private final ConnectivityManager mConnectivityManager;
   private NetworkStateTracker.NetworkStateCallback mNetworkCallback;

   public NetworkStateTracker(Context var1) {
      super(var1);
      this.mConnectivityManager = (ConnectivityManager)this.mAppContext.getSystemService("connectivity");
      if (isNetworkCallbackSupported()) {
         this.mNetworkCallback = new NetworkStateTracker.NetworkStateCallback();
      } else {
         this.mBroadcastReceiver = new NetworkStateTracker.NetworkStateBroadcastReceiver();
      }

   }

   private boolean isActiveNetworkValidated() {
      int var1 = VERSION.SDK_INT;
      boolean var2 = false;
      if (var1 < 23) {
         return false;
      } else {
         Network var3 = this.mConnectivityManager.getActiveNetwork();
         NetworkCapabilities var5 = this.mConnectivityManager.getNetworkCapabilities(var3);
         boolean var4 = var2;
         if (var5 != null) {
            var4 = var2;
            if (var5.hasCapability(16)) {
               var4 = true;
            }
         }

         return var4;
      }
   }

   private static boolean isNetworkCallbackSupported() {
      boolean var0;
      if (VERSION.SDK_INT >= 24) {
         var0 = true;
      } else {
         var0 = false;
      }

      return var0;
   }

   NetworkState getActiveNetworkState() {
      NetworkInfo var1 = this.mConnectivityManager.getActiveNetworkInfo();
      boolean var2 = true;
      boolean var3;
      if (var1 != null && var1.isConnected()) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4 = this.isActiveNetworkValidated();
      boolean var5 = ConnectivityManagerCompat.isActiveNetworkMetered(this.mConnectivityManager);
      if (var1 == null || var1.isRoaming()) {
         var2 = false;
      }

      return new NetworkState(var3, var4, var5, var2);
   }

   public NetworkState getInitialState() {
      return this.getActiveNetworkState();
   }

   public void startTracking() {
      if (isNetworkCallbackSupported()) {
         Logger.get().debug(TAG, "Registering network callback");
         this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
      } else {
         Logger.get().debug(TAG, "Registering broadcast receiver");
         this.mAppContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
      }

   }

   public void stopTracking() {
      if (isNetworkCallbackSupported()) {
         try {
            Logger.get().debug(TAG, "Unregistering network callback");
            this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
         } catch (IllegalArgumentException var2) {
            Logger.get().error(TAG, "Received exception while unregistering network callback", var2);
         }
      } else {
         Logger.get().debug(TAG, "Unregistering broadcast receiver");
         this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
      }

   }

   private class NetworkStateBroadcastReceiver extends BroadcastReceiver {
      NetworkStateBroadcastReceiver() {
      }

      public void onReceive(Context var1, Intent var2) {
         if (var2 != null && var2.getAction() != null) {
            if (var2.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
               Logger.get().debug(NetworkStateTracker.TAG, "Network broadcast received");
               NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
            }

         }
      }
   }

   private class NetworkStateCallback extends NetworkCallback {
      NetworkStateCallback() {
      }

      public void onCapabilitiesChanged(Network var1, NetworkCapabilities var2) {
         Logger.get().debug(NetworkStateTracker.TAG, String.format("Network capabilities changed: %s", var2));
         NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
      }

      public void onLost(Network var1) {
         Logger.get().debug(NetworkStateTracker.TAG, "Network connection lost");
         NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
      }
   }
}
