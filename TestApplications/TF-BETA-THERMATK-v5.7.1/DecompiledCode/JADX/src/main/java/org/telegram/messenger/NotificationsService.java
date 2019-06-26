package org.telegram.messenger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.IBinder;
import androidx.core.app.NotificationCompat.Builder;

public class NotificationsService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    public void onCreate() {
        ApplicationLoader.postInitApplication();
        if (VERSION.SDK_INT >= 26) {
            String str = "push_service_channel";
            ((NotificationManager) getSystemService("notification")).createNotificationChannel(new NotificationChannel(str, "Push Notifications Service", 3));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Notifications.md"));
            PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
            Builder builder = new Builder(this, str);
            builder.setContentIntent(activity);
            builder.setShowWhen(false);
            builder.setOngoing(true);
            builder.setSmallIcon(C1067R.C1065drawable.notification);
            builder.setContentText("Push service: tap to learn more");
            startForeground(9999, builder.build());
        }
    }

    public void onDestroy() {
        if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
            sendBroadcast(new Intent("org.telegram.start"));
        }
    }
}
