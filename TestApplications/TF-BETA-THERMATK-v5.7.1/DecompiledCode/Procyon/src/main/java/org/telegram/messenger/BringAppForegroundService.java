// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.content.Context;
import org.telegram.ui.LaunchActivity;
import android.content.Intent;
import android.app.IntentService;

public class BringAppForegroundService extends IntentService
{
    public BringAppForegroundService() {
        super("BringAppForegroundService");
    }
    
    protected void onHandleIntent(Intent intent) {
        intent = new Intent((Context)this, (Class)LaunchActivity.class);
        intent.setFlags(268435456);
        this.startActivity(intent);
    }
}
