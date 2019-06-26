package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppStartReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      AndroidUtilities.runOnUIThread(new Runnable() {
         public void run() {
            ApplicationLoader.startPushService();
         }
      });
   }
}
