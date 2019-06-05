package org.mozilla.focus.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public abstract class FirebaseMessagingServiceWrapper extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }
}
