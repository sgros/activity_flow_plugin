// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class NotificationCallbackReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (intent == null) {
            return;
        }
        ApplicationLoader.postInitApplication();
        SendMessagesHelper.getInstance(intent.getIntExtra("currentAccount", UserConfig.selectedAccount)).sendNotificationCallback(intent.getLongExtra("did", 777000L), intent.getIntExtra("mid", 0), intent.getByteArrayExtra("data"));
    }
}
