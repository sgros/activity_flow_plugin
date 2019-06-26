package com.google.zxing.client.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

public final class InactivityTimer {
   private static final long INACTIVITY_DELAY_MS = 300000L;
   private static final String TAG = InactivityTimer.class.getSimpleName();
   private Runnable callback;
   private final Context context;
   private Handler handler;
   private boolean onBattery;
   private final BroadcastReceiver powerStatusReceiver;
   private boolean registered = false;

   public InactivityTimer(Context var1, Runnable var2) {
      this.context = var1;
      this.callback = var2;
      this.powerStatusReceiver = new InactivityTimer.PowerStatusReceiver();
      this.handler = new Handler();
   }

   private void cancelCallback() {
      this.handler.removeCallbacksAndMessages((Object)null);
   }

   private void onBattery(boolean var1) {
      this.onBattery = var1;
      if (this.registered) {
         this.activity();
      }

   }

   private void registerReceiver() {
      if (!this.registered) {
         this.context.registerReceiver(this.powerStatusReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
         this.registered = true;
      }

   }

   private void unregisterReceiver() {
      if (this.registered) {
         this.context.unregisterReceiver(this.powerStatusReceiver);
         this.registered = false;
      }

   }

   public void activity() {
      this.cancelCallback();
      if (this.onBattery) {
         this.handler.postDelayed(this.callback, 300000L);
      }

   }

   public void cancel() {
      this.cancelCallback();
      this.unregisterReceiver();
   }

   public void start() {
      this.registerReceiver();
      this.activity();
   }

   private final class PowerStatusReceiver extends BroadcastReceiver {
      private PowerStatusReceiver() {
      }

      // $FF: synthetic method
      PowerStatusReceiver(Object var2) {
         this();
      }

      public void onReceive(Context var1, Intent var2) {
         if ("android.intent.action.BATTERY_CHANGED".equals(var2.getAction())) {
            final boolean var3;
            if (var2.getIntExtra("plugged", -1) <= 0) {
               var3 = true;
            } else {
               var3 = false;
            }

            InactivityTimer.this.handler.post(new Runnable() {
               public void run() {
                  InactivityTimer.this.onBattery(var3);
               }
            });
         }

      }
   }
}
