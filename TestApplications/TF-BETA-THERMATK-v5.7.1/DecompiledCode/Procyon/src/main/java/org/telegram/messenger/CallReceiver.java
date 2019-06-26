// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.PhoneFormat.PhoneFormat;
import android.telephony.TelephonyManager;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class CallReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals("android.intent.action.PHONE_STATE") && TelephonyManager.EXTRA_STATE_RINGING.equals(intent.getStringExtra("state"))) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveCall, PhoneFormat.stripExceptNumbers(intent.getStringExtra("incoming_number")));
        }
    }
}
