// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Intent;
import android.app.IntentService;

public class NotificationRepeat extends IntentService
{
    public NotificationRepeat() {
        super("NotificationRepeat");
    }
    
    protected void onHandleIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() {
            final /* synthetic */ int val$currentAccount = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
            
            @Override
            public void run() {
                NotificationsController.getInstance(this.val$currentAccount).repeatNotificationMaybe();
            }
        });
    }
}
