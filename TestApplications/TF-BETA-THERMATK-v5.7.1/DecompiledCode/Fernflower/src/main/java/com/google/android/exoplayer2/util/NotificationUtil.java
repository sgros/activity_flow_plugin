package com.google.android.exoplayer2.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SuppressLint({"InlinedApi"})
public final class NotificationUtil {
   public static final int IMPORTANCE_DEFAULT = 3;
   public static final int IMPORTANCE_HIGH = 4;
   public static final int IMPORTANCE_LOW = 2;
   public static final int IMPORTANCE_MIN = 1;
   public static final int IMPORTANCE_NONE = 0;
   public static final int IMPORTANCE_UNSPECIFIED = -1000;

   private NotificationUtil() {
   }

   public static void createNotificationChannel(Context var0, String var1, int var2, int var3) {
      if (Util.SDK_INT >= 26) {
         ((NotificationManager)var0.getSystemService("notification")).createNotificationChannel(new NotificationChannel(var1, var0.getString(var2), var3));
      }

   }

   public static void setNotification(Context var0, int var1, Notification var2) {
      NotificationManager var3 = (NotificationManager)var0.getSystemService("notification");
      if (var2 != null) {
         var3.notify(var1, var2);
      } else {
         var3.cancel(var1);
      }

   }

   @Documented
   @Retention(RetentionPolicy.SOURCE)
   public @interface Importance {
   }
}
