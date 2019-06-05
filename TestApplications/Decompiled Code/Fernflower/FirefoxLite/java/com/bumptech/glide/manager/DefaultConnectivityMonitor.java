package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class DefaultConnectivityMonitor implements ConnectivityMonitor {
   private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
      public void onReceive(Context var1, Intent var2) {
         boolean var3 = DefaultConnectivityMonitor.this.isConnected;
         DefaultConnectivityMonitor.this.isConnected = DefaultConnectivityMonitor.this.isConnected(var1);
         if (var3 != DefaultConnectivityMonitor.this.isConnected) {
            DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
         }

      }
   };
   private final Context context;
   boolean isConnected;
   private boolean isRegistered;
   final ConnectivityMonitor.ConnectivityListener listener;

   public DefaultConnectivityMonitor(Context var1, ConnectivityMonitor.ConnectivityListener var2) {
      this.context = var1.getApplicationContext();
      this.listener = var2;
   }

   private void register() {
      if (!this.isRegistered) {
         this.isConnected = this.isConnected(this.context);
         this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
         this.isRegistered = true;
      }
   }

   private void unregister() {
      if (this.isRegistered) {
         this.context.unregisterReceiver(this.connectivityReceiver);
         this.isRegistered = false;
      }
   }

   @SuppressLint({"MissingPermission"})
   boolean isConnected(Context var1) {
      NetworkInfo var3 = ((ConnectivityManager)var1.getSystemService("connectivity")).getActiveNetworkInfo();
      boolean var2;
      if (var3 != null && var3.isConnected()) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public void onDestroy() {
   }

   public void onStart() {
      this.register();
   }

   public void onStop() {
      this.unregister();
   }
}
