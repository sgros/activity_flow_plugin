package org.mozilla.focus.components;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.rocket.component.ConfigActivity;

public class ComponentToggleService extends Service {
   public static final IntentFilter SERVICE_STOP_INTENT_FILTER = new IntentFilter();
   private static final IntentFilter sIntentFilter;
   private BroadcastReceiver mPackageStatusReceiver;
   private Timer timer = new Timer();

   static {
      SERVICE_STOP_INTENT_FILTER.addAction("_component_service_stopped_");
      sIntentFilter = new IntentFilter();
      sIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
      sIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
      sIntentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
      sIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
      sIntentFilter.addDataScheme("package");
   }

   private PendingIntent buildIntent() {
      return PendingIntent.getActivity(this.getApplicationContext(), 38183, new Intent(this.getApplicationContext(), SettingsActivity.class), 134217728);
   }

   public static boolean isAlive(Context var0) {
      Iterator var1 = ((ActivityManager)var0.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE).iterator();

      RunningServiceInfo var2;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         var2 = (RunningServiceInfo)var1.next();
      } while(!ComponentToggleService.class.getName().equals(var2.service.getClassName()));

      return true;
   }

   private void removeFromForeground() {
      Notification var1 = NotificationUtil.importantBuilder(this.getApplicationContext()).setContentTitle(this.getString(2131755395)).setAutoCancel(true).setContentIntent(this.buildIntent()).build();
      NotificationManagerCompat.from(this.getApplicationContext()).notify(57083, var1);
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

      this.stopSelf();
   }

   private void startToForeground() {
      this.startForeground(57082, NotificationUtil.importantBuilder(this.getApplicationContext()).setContentTitle(this.getString(2131755397)).setContentText(this.getString(2131755396)).setAutoCancel(false).setOngoing(true).setContentIntent(this.buildIntent()).build());
   }

   public IBinder onBind(Intent var1) {
      return null;
   }

   public void onConfigurationChanged(Configuration var1) {
      super.onConfigurationChanged(var1);
   }

   public void onCreate() {
      super.onCreate();
      this.mPackageStatusReceiver = new ComponentToggleService.PackageStatusReceiver(new ComponentToggleService.PackageStatusReceiver.Listener() {
         public void onPackageChanged(Intent var1) {
            if (ComponentToggleService.sIntentFilter.hasAction(var1.getAction())) {
               String var2 = var1.getData().getEncodedSchemeSpecificPart();
               if (ComponentToggleService.this.getPackageName().equals(var2)) {
                  ComponentToggleService.this.startService(new Intent(ComponentToggleService.this.getApplicationContext(), ComponentToggleService.class));
               }

            }
         }
      });
      this.timer.schedule(new ComponentToggleService.BombTask(this), 30000L);
      this.registerReceiver(this.mPackageStatusReceiver, sIntentFilter);
   }

   public void onDestroy() {
      this.unregisterReceiver(this.mPackageStatusReceiver);
      if (this.timer != null) {
         this.timer.cancel();
         this.timer = null;
      }

      LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(new Intent("_component_service_stopped_"));
      this.stopForeground(true);
      super.onDestroy();
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      super.onStartCommand(var1, var2, var3);
      boolean var4 = Browsers.hasDefaultBrowser(this.getApplicationContext());
      boolean var5 = Browsers.isDefaultBrowser(this.getApplicationContext());
      boolean var6 = false;
      boolean var10;
      if (!var5 && !var4) {
         var10 = true;
      } else {
         var10 = false;
      }

      PackageManager var9 = this.getPackageManager();
      ComponentName var7 = new ComponentName(this.getApplicationContext(), ConfigActivity.class);
      boolean var11;
      if (var9.getComponentEnabledSetting(var7) == 1) {
         var11 = true;
      } else {
         var11 = false;
      }

      boolean var8;
      if (!var11 && var4 && !var5) {
         var8 = true;
      } else {
         var8 = false;
      }

      label31: {
         if (!var10) {
            var10 = var6;
            if (!var11) {
               break label31;
            }
         }

         var10 = true;
      }

      if (var8) {
         var9.setComponentEnabledSetting(var7, 1, 1);
         this.startToForeground();
      } else if (var10) {
         var9.setComponentEnabledSetting(var7, 2, 1);
         this.removeFromForeground();
      }

      return 1;
   }

   public boolean onUnbind(Intent var1) {
      return super.onUnbind(var1);
   }

   private static final class BombTask extends TimerTask {
      final WeakReference service;

      BombTask(ComponentToggleService var1) {
         this.service = new WeakReference(var1);
      }

      public void run() {
         ComponentToggleService var1 = (ComponentToggleService)this.service.get();
         if (var1 != null) {
            var1.removeFromForeground();
         }

      }
   }

   private static final class PackageStatusReceiver extends BroadcastReceiver {
      private final ComponentToggleService.PackageStatusReceiver.Listener mListener;

      PackageStatusReceiver(ComponentToggleService.PackageStatusReceiver.Listener var1) {
         this.mListener = var1;
      }

      public void onReceive(Context var1, Intent var2) {
         if (var2 != null && var2.getData() != null) {
            this.mListener.onPackageChanged(var2);
         }

      }

      interface Listener {
         void onPackageChanged(Intent var1);
      }
   }
}
