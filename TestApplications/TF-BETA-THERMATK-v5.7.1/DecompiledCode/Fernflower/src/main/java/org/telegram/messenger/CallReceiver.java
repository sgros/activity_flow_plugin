package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import org.telegram.PhoneFormat.PhoneFormat;

public class CallReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2.getAction().equals("android.intent.action.PHONE_STATE")) {
         String var3 = var2.getStringExtra("state");
         if (TelephonyManager.EXTRA_STATE_RINGING.equals(var3)) {
            var3 = var2.getStringExtra("incoming_number");
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveCall, PhoneFormat.stripExceptNumbers(var3));
         }
      }

   }
}
