package org.osmdroid.tileprovider.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.osmdroid.tileprovider.IRegisterReceiver;

public class SimpleRegisterReceiver implements IRegisterReceiver {
   private Context mContext;

   public SimpleRegisterReceiver(Context var1) {
      this.mContext = var1;
   }

   public void destroy() {
      this.mContext = null;
   }

   public Intent registerReceiver(BroadcastReceiver var1, IntentFilter var2) {
      return this.mContext.registerReceiver(var1, var2);
   }

   public void unregisterReceiver(BroadcastReceiver var1) {
      this.mContext.unregisterReceiver(var1);
   }
}
