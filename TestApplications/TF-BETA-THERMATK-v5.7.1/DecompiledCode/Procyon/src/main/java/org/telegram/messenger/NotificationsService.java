// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import androidx.core.app.NotificationCompat;
import android.content.Context;
import android.app.PendingIntent;
import android.net.Uri;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build$VERSION;
import android.os.IBinder;
import android.content.Intent;
import android.app.Service;

public class NotificationsService extends Service
{
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        ApplicationLoader.postInitApplication();
        if (Build$VERSION.SDK_INT >= 26) {
            ((NotificationManager)this.getSystemService("notification")).createNotificationChannel(new NotificationChannel("push_service_channel", (CharSequence)"Push Notifications Service", 3));
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Notifications.md"));
            final PendingIntent activity = PendingIntent.getActivity((Context)this, 0, intent, 0);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder((Context)this, "push_service_channel");
            builder.setContentIntent(activity);
            builder.setShowWhen(false);
            builder.setOngoing(true);
            builder.setSmallIcon(2131165698);
            builder.setContentText("Push service: tap to learn more");
            this.startForeground(9999, builder.build());
        }
    }
    
    public void onDestroy() {
        if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
            this.sendBroadcast(new Intent("org.telegram.start"));
        }
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        return 1;
    }
}
