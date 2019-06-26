package org.telegram.messenger.voip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class VoIPActionsReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (VoIPBaseService.getSharedInstance() != null) {
         VoIPBaseService.getSharedInstance().handleNotificationAction(var2);
      }

   }
}
