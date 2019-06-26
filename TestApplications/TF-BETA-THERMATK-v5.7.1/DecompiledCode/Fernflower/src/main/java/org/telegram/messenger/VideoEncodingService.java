package org.telegram.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class VideoEncodingService extends Service implements NotificationCenter.NotificationCenterDelegate {
   private NotificationCompat.Builder builder;
   private int currentAccount;
   private int currentProgress;
   private String path;

   public VideoEncodingService() {
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.stopEncodingService);
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      int var4 = NotificationCenter.FileUploadProgressChanged;
      boolean var5 = true;
      String var7;
      if (var1 == var4) {
         String var6 = (String)var3[0];
         if (var2 == this.currentAccount) {
            var7 = this.path;
            if (var7 != null && var7.equals(var6)) {
               Float var11 = (Float)var3[1];
               Boolean var9 = (Boolean)var3[2];
               this.currentProgress = (int)(var11 * 100.0F);
               NotificationCompat.Builder var10 = this.builder;
               var1 = this.currentProgress;
               if (var1 != 0) {
                  var5 = false;
               }

               var10.setProgress(100, var1, var5);

               try {
                  NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(4, this.builder.build());
               } catch (Throwable var8) {
                  FileLog.e(var8);
               }
            }
         }
      } else if (var1 == NotificationCenter.stopEncodingService) {
         var7 = (String)var3[0];
         if ((Integer)var3[1] == this.currentAccount && (var7 == null || var7.equals(this.path))) {
            this.stopSelf();
         }
      }

   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onDestroy() {
      this.stopForeground(true);
      NotificationManagerCompat.from(ApplicationLoader.applicationContext).cancel(4);
      NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.stopEncodingService);
      NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileUploadProgressChanged);
      if (BuildVars.LOGS_ENABLED) {
         FileLog.d("destroy video service");
      }

   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      this.path = var1.getStringExtra("path");
      var2 = this.currentAccount;
      this.currentAccount = var1.getIntExtra("currentAccount", UserConfig.selectedAccount);
      if (var2 != this.currentAccount) {
         NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.FileUploadProgressChanged);
         NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileUploadProgressChanged);
      }

      boolean var4 = false;
      boolean var5 = var1.getBooleanExtra("gif", false);
      if (this.path == null) {
         this.stopSelf();
         return 2;
      } else {
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start video service");
         }

         if (this.builder == null) {
            NotificationsController.checkOtherNotificationsChannel();
            this.builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext);
            this.builder.setSmallIcon(17301640);
            this.builder.setWhen(System.currentTimeMillis());
            this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            this.builder.setContentTitle(LocaleController.getString("AppName", 2131558635));
            if (var5) {
               this.builder.setTicker(LocaleController.getString("SendingGif", 2131560712));
               this.builder.setContentText(LocaleController.getString("SendingGif", 2131560712));
            } else {
               this.builder.setTicker(LocaleController.getString("SendingVideo", 2131560715));
               this.builder.setContentText(LocaleController.getString("SendingVideo", 2131560715));
            }
         }

         this.currentProgress = 0;
         NotificationCompat.Builder var6 = this.builder;
         var2 = this.currentProgress;
         if (var2 == 0) {
            var4 = true;
         }

         var6.setProgress(100, var2, var4);
         this.startForeground(4, this.builder.build());
         NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(4, this.builder.build());
         return 2;
      }
   }
}
