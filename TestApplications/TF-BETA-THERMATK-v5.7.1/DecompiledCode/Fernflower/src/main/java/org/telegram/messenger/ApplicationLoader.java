package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.File;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.ForegroundDetector;

public class ApplicationLoader extends Application {
   @SuppressLint({"StaticFieldLeak"})
   public static volatile Context applicationContext;
   public static volatile Handler applicationHandler;
   private static volatile boolean applicationInited;
   private static ConnectivityManager connectivityManager;
   public static volatile NetworkInfo currentNetworkInfo;
   public static volatile boolean externalInterfacePaused;
   public static volatile boolean isScreenOn;
   public static volatile boolean mainInterfacePaused;
   public static volatile boolean mainInterfacePausedStageQueue;
   public static volatile long mainInterfacePausedStageQueueTime;
   private static PendingIntent pendingIntent;
   public static volatile boolean unableGetCurrentNetwork;

   public static int getCurrentNetworkType() {
      if (isConnectedOrConnectingToWiFi()) {
         return 1;
      } else {
         return isRoaming() ? 2 : 0;
      }
   }

   public static File getFilesDirFixed() {
      for(int var0 = 0; var0 < 10; ++var0) {
         File var1 = applicationContext.getFilesDir();
         if (var1 != null) {
            return var1;
         }
      }

      try {
         ApplicationInfo var4 = applicationContext.getApplicationInfo();
         File var2 = new File(var4.dataDir, "files");
         var2.mkdirs();
         return var2;
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
         return new File("/data/data/org.telegram.messenger/files");
      }
   }

   public static boolean isConnectedOrConnectingToWiFi() {
      // $FF: Couldn't be decompiled
   }

   public static boolean isConnectedToWiFi() {
      // $FF: Couldn't be decompiled
   }

   public static boolean isConnectionSlow() {
      int var1;
      try {
         NetworkInfo var0 = ((ConnectivityManager)applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
         if (var0.getType() != 0) {
            return false;
         }

         var1 = var0.getSubtype();
      } catch (Throwable var2) {
         return false;
      }

      if (var1 == 1 || var1 == 2 || var1 == 4 || var1 == 7 || var1 == 11) {
         return true;
      } else {
         return false;
      }
   }

   public static boolean isNetworkOnline() {
      Exception var10000;
      label64: {
         ConnectivityManager var0;
         NetworkInfo var1;
         boolean var10001;
         try {
            var0 = (ConnectivityManager)applicationContext.getSystemService("connectivity");
            var1 = var0.getActiveNetworkInfo();
         } catch (Exception var7) {
            var10000 = var7;
            var10001 = false;
            break label64;
         }

         if (var1 != null) {
            try {
               if (var1.isConnectedOrConnecting() || var1.isAvailable()) {
                  return true;
               }
            } catch (Exception var8) {
               var10000 = var8;
               var10001 = false;
               break label64;
            }
         }

         try {
            var1 = var0.getNetworkInfo(0);
         } catch (Exception var6) {
            var10000 = var6;
            var10001 = false;
            break label64;
         }

         if (var1 != null) {
            try {
               if (var1.isConnectedOrConnecting()) {
                  return true;
               }
            } catch (Exception var5) {
               var10000 = var5;
               var10001 = false;
               break label64;
            }
         }

         NetworkInfo var9;
         try {
            var9 = var0.getNetworkInfo(1);
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label64;
         }

         if (var9 != null) {
            boolean var2;
            try {
               var2 = var9.isConnectedOrConnecting();
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label64;
            }

            if (var2) {
               return true;
            }
         }

         return false;
      }

      Exception var10 = var10000;
      FileLog.e((Throwable)var10);
      return true;
   }

   public static boolean isRoaming() {
      // $FF: Couldn't be decompiled
   }

   public static void postInitApplication() {
      if (!applicationInited) {
         applicationInited = true;

         try {
            LocaleController.getInstance();
         } catch (Exception var7) {
            var7.printStackTrace();
         }

         IntentFilter var1;
         try {
            connectivityManager = (ConnectivityManager)applicationContext.getSystemService("connectivity");
            BroadcastReceiver var0 = new BroadcastReceiver() {
               public void onReceive(Context var1, Intent var2) {
                  try {
                     ApplicationLoader.currentNetworkInfo = ApplicationLoader.connectivityManager.getActiveNetworkInfo();
                  } catch (Throwable var5) {
                  }

                  boolean var3 = ApplicationLoader.isConnectionSlow();

                  for(int var4 = 0; var4 < 3; ++var4) {
                     ConnectionsManager.getInstance(var4).checkConnection();
                     FileLoader.getInstance(var4).onNetworkChanged(var3);
                  }

               }
            };
            var1 = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
            applicationContext.registerReceiver(var0, var1);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         try {
            var1 = new IntentFilter("android.intent.action.SCREEN_ON");
            var1.addAction("android.intent.action.SCREEN_OFF");
            ScreenReceiver var8 = new ScreenReceiver();
            applicationContext.registerReceiver(var8, var1);
         } catch (Exception var5) {
            var5.printStackTrace();
         }

         try {
            isScreenOn = ((PowerManager)applicationContext.getSystemService("power")).isScreenOn();
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var9 = new StringBuilder();
               var9.append("screen state = ");
               var9.append(isScreenOn);
               FileLog.d(var9.toString());
            }
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
         }

         SharedConfig.loadConfig();
         byte var2 = 0;

         int var3;
         for(var3 = 0; var3 < 3; ++var3) {
            UserConfig.getInstance(var3).loadConfig();
            MessagesController.getInstance(var3);
            ConnectionsManager.getInstance(var3);
            TLRPC.User var10 = UserConfig.getInstance(var3).getCurrentUser();
            if (var10 != null) {
               MessagesController.getInstance(var3).putUser(var10, true);
               MessagesController.getInstance(var3).getBlockedUsers(true);
               SendMessagesHelper.getInstance(var3).checkUnsentMessages();
            }
         }

         ApplicationLoader var11 = (ApplicationLoader)applicationContext;
         if (BuildVars.LOGS_ENABLED) {
            FileLog.d("app initied");
         }

         MediaController.getInstance();

         for(var3 = var2; var3 < 3; ++var3) {
            ContactsController.getInstance(var3).checkAppAccount();
            DownloadController.getInstance(var3);
         }

      }
   }

   public static void startPushService() {
      if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
         Log.d("TFOSS", "Trying to start push service every 10 minutes");
         AlarmManager var0 = (AlarmManager)applicationContext.getSystemService("alarm");
         Intent var1 = new Intent(applicationContext, ApplicationLoader.class);
         pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, var1, 0);
         var0.cancel(pendingIntent);
         var0.setRepeating(0, System.currentTimeMillis(), 60000L, pendingIntent);

         try {
            Log.d("TFOSS", "Starting push service...");
            if (VERSION.SDK_INT >= 26) {
               Context var3 = applicationContext;
               var1 = new Intent(applicationContext, NotificationsService.class);
               var3.startForegroundService(var1);
            } else {
               Context var5 = applicationContext;
               Intent var4 = new Intent(applicationContext, NotificationsService.class);
               var5.startService(var4);
            }
         } catch (Throwable var2) {
            Log.d("TFOSS", "Failed to start push service");
         }
      } else {
         stopPushService();
      }

   }

   public static void stopPushService() {
      applicationContext.stopService(new Intent(applicationContext, NotificationsService.class));
      PendingIntent var0 = PendingIntent.getService(applicationContext, 0, new Intent(applicationContext, NotificationsService.class), 0);
      AlarmManager var1 = (AlarmManager)applicationContext.getSystemService("alarm");
      var1.cancel(var0);
      var0 = pendingIntent;
      if (var0 != null) {
         var1.cancel(var0);
      }

   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);

      try {
         LocaleController.getInstance().onDeviceConfigurationChange(var1);
         AndroidUtilities.checkDisplaySize(applicationContext, var1);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void onCreate() {
      try {
         applicationContext = this.getApplicationContext();
      } catch (Throwable var2) {
      }

      super.onCreate();
      if (applicationContext == null) {
         applicationContext = this.getApplicationContext();
      }

      NativeLoader.initNativeLibs(applicationContext);
      ConnectionsManager.native_setJava(false);
      new ForegroundDetector(this);
      applicationHandler = new Handler(applicationContext.getMainLooper());
      AndroidUtilities.runOnUIThread(_$$Lambda$r7IJ1lCIETKiJtvF21QqYn99iv8.INSTANCE);
   }
}
