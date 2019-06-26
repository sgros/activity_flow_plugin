package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationCallbackReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2 != null) {
         ApplicationLoader.postInitApplication();
         int var3 = var2.getIntExtra("currentAccount", UserConfig.selectedAccount);
         long var4 = var2.getLongExtra("did", 777000L);
         byte[] var7 = var2.getByteArrayExtra("data");
         int var6 = var2.getIntExtra("mid", 0);
         SendMessagesHelper.getInstance(var3).sendNotificationCallback(var4, var6, var7);
      }
   }
}
