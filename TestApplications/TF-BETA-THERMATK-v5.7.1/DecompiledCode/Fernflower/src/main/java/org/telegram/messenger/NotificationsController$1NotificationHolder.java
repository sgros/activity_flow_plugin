package org.telegram.messenger;

import android.app.Notification;

class NotificationsController$1NotificationHolder {
   int id;
   Notification notification;
   // $FF: synthetic field
   final NotificationsController this$0;

   NotificationsController$1NotificationHolder(NotificationsController var1, int var2, Notification var3) {
      this.this$0 = var1;
      this.id = var2;
      this.notification = var3;
   }

   void call() {
      if (BuildVars.LOGS_ENABLED) {
         StringBuilder var1 = new StringBuilder();
         var1.append("show dialog notification with id ");
         var1.append(this.id);
         FileLog.w(var1.toString());
      }

      NotificationsController.access$000().notify(this.id, this.notification);
   }
}
