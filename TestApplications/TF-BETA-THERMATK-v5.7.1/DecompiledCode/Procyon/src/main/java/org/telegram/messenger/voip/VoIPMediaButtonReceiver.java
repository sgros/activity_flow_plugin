// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger.voip;

import android.view.KeyEvent;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class VoIPMediaButtonReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        if ("android.intent.action.MEDIA_BUTTON".equals(intent.getAction())) {
            if (VoIPService.getSharedInstance() == null) {
                return;
            }
            VoIPService.getSharedInstance().onMediaButtonEvent((KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT"));
        }
    }
}
