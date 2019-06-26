package org.telegram.messenger;

import android.app.IntentService;
import android.content.Intent;
import org.telegram.p004ui.LaunchActivity;

public class BringAppForegroundService extends IntentService {
    public BringAppForegroundService() {
        super("BringAppForegroundService");
    }

    /* Access modifiers changed, original: protected */
    public void onHandleIntent(Intent intent) {
        intent = new Intent(this, LaunchActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
    }
}