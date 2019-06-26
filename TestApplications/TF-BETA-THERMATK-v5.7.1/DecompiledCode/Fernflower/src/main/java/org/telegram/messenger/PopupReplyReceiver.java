package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PopupReplyReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2 != null) {
         ApplicationLoader.postInitApplication();
         NotificationsController.getInstance(var2.getIntExtra("currentAccount", UserConfig.selectedAccount)).forceShowPopupForReply();
      }
   }
}
