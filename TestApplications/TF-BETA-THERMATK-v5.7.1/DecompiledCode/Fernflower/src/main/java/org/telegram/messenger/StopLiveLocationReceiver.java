package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopLiveLocationReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      for(int var3 = 0; var3 < 3; ++var3) {
         LocationController.getInstance(var3).removeAllLocationSharings();
      }

   }
}
