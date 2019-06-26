// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class NotificationDismissReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        MessagesController.getNotificationsSettings(intent.getIntExtra("currentAccount", UserConfig.selectedAccount)).edit().putInt("dismissDate", intent.getIntExtra("messageDate", 0)).commit();
    }
}
