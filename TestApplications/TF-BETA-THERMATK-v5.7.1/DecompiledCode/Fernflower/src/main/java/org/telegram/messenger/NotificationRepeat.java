package org.telegram.messenger;

import android.app.IntentService;
import android.content.Intent;

public class NotificationRepeat extends IntentService {
   public NotificationRepeat() {
      super("NotificationRepeat");
   }

   protected void onHandleIntent(Intent var1) {
      if (var1 != null) {
         AndroidUtilities.runOnUIThread(new Runnable(var1.getIntExtra("currentAccount", UserConfig.selectedAccount)) {
            // $FF: synthetic field
            final int val$currentAccount;

            {
               this.val$currentAccount = var2;
            }

            public void run() {
               NotificationsController.getInstance(this.val$currentAccount).repeatNotificationMaybe();
            }
         });
      }
   }
}
