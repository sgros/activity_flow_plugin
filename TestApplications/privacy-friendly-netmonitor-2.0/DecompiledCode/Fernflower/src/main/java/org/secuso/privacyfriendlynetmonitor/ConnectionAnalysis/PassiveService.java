package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.secuso.privacyfriendlynetmonitor.Activities.MainActivity;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;

public class PassiveService extends Service {
   private static final int SERVICE_IDENTIFIER = 1;
   public static boolean mInterrupt;
   private final IBinder mBinder = new PassiveService.AnalyzerBinder();
   NotificationCompat.Builder mBuilder = (new NotificationCompat.Builder(this)).setSmallIcon(2131230826).setContentTitle(this.getVersionString(2131623976)).setContentText(this.getVersionString(2131623979));
   private Bitmap mIcon;
   private Thread mThread;

   @TargetApi(9)
   private static String getStringNew(int var0) {
      return RunStore.getContext().getResources().getString(var0);
   }

   @TargetApi(21)
   private static String getStringOld(int var0) {
      return RunStore.getContext().getString(var0);
   }

   private String getVersionString(int var1) {
      return VERSION.SDK_INT >= 21 ? getStringNew(var1) : getStringOld(var1);
   }

   private void interrupt() {
      mInterrupt = true;
      this.stopSelf();
   }

   private void loadNotificationBitmaps() {
      this.mIcon = BitmapFactory.decodeResource(this.getResources(), 2131558400);
   }

   private void showAppNotification() {
      this.mBuilder.setSmallIcon(2131230826);
      this.mBuilder.setLargeIcon(this.mIcon);
      Intent var1 = new Intent(this, MainActivity.class);
      TaskStackBuilder var2 = TaskStackBuilder.create(this);
      var2.addParentStack(MainActivity.class);
      var2.addNextIntent(var1);
      PendingIntent var3 = var2.getPendingIntent(0, 134217728);
      this.mBuilder.setContentIntent(var3);
      this.startForeground(1, this.mBuilder.build());
   }

   private void showWarningNotification() {
      Intent var1 = new Intent(this, MainActivity.class);
      TaskStackBuilder var2 = TaskStackBuilder.create(this);
      var2.addParentStack(MainActivity.class);
      var2.addNextIntent(var1);
      PendingIntent var3 = var2.getPendingIntent(0, 134217728);
      this.mBuilder.setContentIntent(var3);
      ((NotificationManager)this.getSystemService("notification")).notify("NetMonitor", 1, this.mBuilder.build());
   }

   public IBinder onBind(Intent var1) {
      this.startThread();
      return this.mBinder;
   }

   public void onCreate() {
      mInterrupt = false;
      this.loadNotificationBitmaps();
      this.showAppNotification();
      KnownPorts.initPortMap();
   }

   public void onDestroy() {
      this.interrupt();
      super.onDestroy();
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      this.startThread();
      return 1;
   }

   public void onTaskRemoved(Intent var1) {
      this.stopSelf();
   }

   public boolean onUnbind(Intent var1) {
      this.interrupt();
      return super.onUnbind(var1);
   }

   public void startThread() {
      Log.i("NetMonitor", "PassiveService - Thread started");
      if (this.mThread != null) {
         this.mThread.interrupt();
      }

      this.mThread = new Thread(new Runnable() {
         public void run() {
            while(true) {
               try {
                  if (PassiveService.mInterrupt) {
                     break;
                  }

                  Detector.updateReportMap();
                  Collector.updateSettings();
                  if (Collector.isCertVal) {
                     Collector.updateCertVal();
                  }

                  Thread.sleep(1000L);
               } catch (InterruptedException var2) {
                  var2.printStackTrace();
                  break;
               }
            }

            if (PassiveService.mInterrupt) {
               PassiveService.this.mThread.interrupt();
            }

            PassiveService.this.stopSelf();
         }
      }, "AnalyzerThreadRunnable");
      this.mThread.start();
   }

   public class AnalyzerBinder extends Binder {
      PassiveService getService() {
         return PassiveService.this;
      }
   }
}
