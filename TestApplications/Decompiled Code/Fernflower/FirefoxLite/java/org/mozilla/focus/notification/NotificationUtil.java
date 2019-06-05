package org.mozilla.focus.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

public class NotificationUtil {
   public static NotificationCompat.Builder baseBuilder(Context var0, NotificationUtil.Channel var1) {
      String var3 = getChannelD(var1);
      if (!TextUtils.isEmpty(var3)) {
         NotificationCompat.Builder var2 = (new NotificationCompat.Builder(var0, var3)).setSmallIcon(2131230896).setLargeIcon(BitmapFactory.decodeResource(var0.getResources(), 2131623936)).setColor(ContextCompat.getColor(var0, 2131099879)).setAutoCancel(true);
         if (VERSION.SDK_INT >= 24) {
            var2.setShowWhen(false);
         }

         return var2;
      } else {
         throw new IllegalStateException("No such channel");
      }
   }

   private static void createNotificationChannel(Context var0, String var1, int var2, int var3) {
      NotificationManager var4 = (NotificationManager)var0.getSystemService("notification");
      if (var4 != null) {
         var4.createNotificationChannel(new NotificationChannel(var1, var0.getString(var2), var3));
      }
   }

   private static String getChannelD(NotificationUtil.Channel var0) {
      switch(var0) {
      case IMPORTANT:
         return "default_channel_id";
      case PRIVATE:
         return "private_mode_channel_id";
      default:
         return "";
      }
   }

   public static NotificationCompat.Builder importantBuilder(Context var0) {
      return baseBuilder(var0, NotificationUtil.Channel.IMPORTANT).setDefaults(2).setPriority(1);
   }

   public static void init(Context var0) {
      if (VERSION.SDK_INT >= 26) {
         createNotificationChannel(var0, "default_channel_id", 2131755062, 4);
         createNotificationChannel(var0, "private_mode_channel_id", 2131755362, 2);
      }
   }

   public static void sendNotification(Context var0, int var1, NotificationCompat.Builder var2) {
      NotificationManager var3 = (NotificationManager)var0.getSystemService("notification");
      if (var3 != null) {
         var3.notify(var1, var2.build());
      }

   }

   public static enum Channel {
      IMPORTANT,
      PRIVATE;
   }
}
