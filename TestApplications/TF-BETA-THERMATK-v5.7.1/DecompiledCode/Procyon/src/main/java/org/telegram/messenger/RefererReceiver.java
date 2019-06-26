// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class RefererReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        try {
            MessagesController.getInstance(UserConfig.selectedAccount).setReferer(intent.getExtras().getString("referrer"));
        }
        catch (Exception ex) {}
    }
}
