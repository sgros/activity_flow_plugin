package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationDismissReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2 != null) {
         MessagesController.getNotificationsSettings(var2.getIntExtra("currentAccount", UserConfig.selectedAccount)).edit().putInt("dismissDate", var2.getIntExtra("messageDate", 0)).commit();
      }
   }
}
