package org.telegram.messenger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import org.telegram.PhoneFormat.C0278PhoneFormat;

public class CallReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {
            if (TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra("state"))) {
                String stringExtra = intent.getStringExtra("incoming_number");
                NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveCall, C0278PhoneFormat.stripExceptNumbers(stringExtra));
            }
        }
    }
}