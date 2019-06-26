// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class VoIPActionsReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if (VoIPBaseService.getSharedInstance() != null) {
            VoIPBaseService.getSharedInstance().handleNotificationAction(intent);
        }
    }
}
