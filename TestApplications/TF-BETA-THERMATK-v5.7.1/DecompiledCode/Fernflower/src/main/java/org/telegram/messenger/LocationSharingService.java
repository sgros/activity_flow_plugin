package org.telegram.messenger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.LaunchActivity;

public class LocationSharingService extends Service implements NotificationCenter.NotificationCenterDelegate {
   private NotificationCompat.Builder builder;
   private Handler handler;
   private Runnable runnable;

   public LocationSharingService() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
   }

   private ArrayList getInfos() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < 3; ++var2) {
         ArrayList var3 = LocationController.getInstance(var2).sharingLocationsUI;
         if (!var3.isEmpty()) {
            var1.addAll(var3);
         }
      }

      return var1;
   }

   private void updateNotification(boolean var1) {
      if (this.builder != null) {
         ArrayList var2 = this.getInfos();
         String var6;
         if (var2.size() == 1) {
            LocationController.SharingLocationInfo var5 = (LocationController.SharingLocationInfo)var2.get(0);
            int var3 = (int)var5.messageObject.getDialogId();
            int var4 = var5.messageObject.currentAccount;
            if (var3 > 0) {
               var6 = UserObject.getFirstName(MessagesController.getInstance(var4).getUser(var3));
            } else {
               TLRPC.Chat var7 = MessagesController.getInstance(var4).getChat(-var3);
               if (var7 != null) {
                  var6 = var7.title;
               } else {
                  var6 = "";
               }
            }
         } else {
            var6 = LocaleController.formatPluralString("Chats", var2.size());
         }

         var6 = String.format(LocaleController.getString("AttachLiveLocationIsSharing", 2131558722), LocaleController.getString("AttachLiveLocation", 2131558721), var6);
         this.builder.setTicker(var6);
         this.builder.setContentText(var6);
         if (var1) {
            NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(6, this.builder.build());
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.liveLocationsChanged) {
         Handler var4 = this.handler;
         if (var4 != null) {
            var4.post(new Runnable() {
               public void run() {
                  if (LocationSharingService.this.getInfos().isEmpty()) {
                     LocationSharingService.this.stopSelf();
                  } else {
                     LocationSharingService.this.updateNotification(true);
                  }

               }
            });
         }
      }

   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onCreate() {
      super.onCreate();
      this.handler = new Handler();
      this.runnable = new Runnable() {
         public void run() {
            LocationSharingService.this.handler.postDelayed(LocationSharingService.this.runnable, 60000L);
            Utilities.stageQueue.postRunnable(new Runnable() {
               public void run() {
                  for(int var1 = 0; var1 < 3; ++var1) {
                     LocationController.getInstance(var1).update();
                  }

               }
            });
         }
      };
      this.handler.postDelayed(this.runnable, 60000L);
   }

   public void onDestroy() {
      Handler var1 = this.handler;
      if (var1 != null) {
         var1.removeCallbacks(this.runnable);
      }

      this.stopForeground(true);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      if (this.getInfos().isEmpty()) {
         this.stopSelf();
      }

      if (this.builder == null) {
         var1 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
         var1.setAction("org.tmessages.openlocations");
         var1.addCategory("android.intent.category.LAUNCHER");
         PendingIntent var4 = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, var1, 0);
         this.builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
         this.builder.setWhen(System.currentTimeMillis());
         this.builder.setSmallIcon(2131165534);
         this.builder.setContentIntent(var4);
         NotificationsController.checkOtherNotificationsChannel();
         this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
         this.builder.setContentTitle(LocaleController.getString("AppName", 2131558635));
         var1 = new Intent(ApplicationLoader.applicationContext, StopLiveLocationReceiver.class);
         this.builder.addAction(0, LocaleController.getString("StopLiveLocation", 2131560823), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, var1, 134217728));
      }

      this.updateNotification(false);
      this.startForeground(6, this.builder.build());
      return 2;
   }
}
