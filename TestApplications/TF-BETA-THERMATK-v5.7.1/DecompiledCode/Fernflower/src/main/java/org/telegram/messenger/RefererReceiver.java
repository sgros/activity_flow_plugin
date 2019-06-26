package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RefererReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      try {
         MessagesController.getInstance(UserConfig.selectedAccount).setReferer(var2.getExtras().getString("referrer"));
      } catch (Exception var3) {
      }

   }
}
