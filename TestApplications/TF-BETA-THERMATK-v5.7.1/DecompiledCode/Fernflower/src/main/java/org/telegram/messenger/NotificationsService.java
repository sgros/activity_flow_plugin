package org.telegram.messenger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Build.VERSION;
import androidx.core.app.NotificationCompat;

public class NotificationsService extends Service {
   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCreate() {
      ApplicationLoader.postInitApplication();
      if (VERSION.SDK_INT >= 26) {
         ((NotificationManager)this.getSystemService("notification")).createNotificationChannel(new NotificationChannel("push_service_channel", "Push Notifications Service", 3));
         Intent var1 = new Intent("android.intent.action.VIEW");
         var1.setData(Uri.parse("https://github.com/Telegram-FOSS-Team/Telegram-FOSS/blob/master/Notifications.md"));
         PendingIntent var3 = PendingIntent.getActivity(this, 0, var1, 0);
         NotificationCompat.Builder var2 = new NotificationCompat.Builder(this, "push_service_channel");
         var2.setContentIntent(var3);
         var2.setShowWhen(false);
         var2.setOngoing(true);
         var2.setSmallIcon(2131165698);
         var2.setContentText("Push service: tap to learn more");
         this.startForeground(9999, var2.build());
      }

   }

   public void onDestroy() {
      if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
         this.sendBroadcast(new Intent("org.telegram.start"));
      }

   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      return 1;
   }
}
