package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoMessageHeardReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      ApplicationLoader.postInitApplication();
      long var3 = var2.getLongExtra("dialog_id", 0L);
      int var5 = var2.getIntExtra("max_id", 0);
      int var6 = var2.getIntExtra("currentAccount", 0);
      if (var3 != 0L && var5 != 0) {
         MessagesController.getInstance(var6).markDialogAsRead(var3, var5, var5, 0, false, 0, true);
      }

   }
}
