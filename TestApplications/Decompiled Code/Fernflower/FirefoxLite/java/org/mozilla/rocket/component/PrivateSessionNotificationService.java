package org.mozilla.rocket.component;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Build.VERSION;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.rocket.privately.PrivateModeActivity;

public final class PrivateSessionNotificationService extends Service {
   private static final String ACTION_START = "start";
   public static final PrivateSessionNotificationService.Companion Companion = new PrivateSessionNotificationService.Companion((DefaultConstructorMarker)null);

   public static final Intent buildIntent(Context var0, boolean var1) {
      return Companion.buildIntent(var0, var1);
   }

   private final PendingIntent buildPendingIntent(boolean var1) {
      Intent var2 = Companion.buildIntent((Context)this, var1);
      PendingIntent var3 = PendingIntent.getActivity(this.getApplicationContext(), 0, var2, 134217728);
      Intrinsics.checkExpressionValueIsNotNull(var3, "PendingIntent.getActivit…tent.FLAG_UPDATE_CURRENT)");
      return var3;
   }

   private final void showNotification() {
      this.startForeground(4000, NotificationUtil.baseBuilder(this.getApplicationContext(), NotificationUtil.Channel.PRIVATE).setContentTitle((CharSequence)this.getString(2131755360)).setContentIntent(this.buildPendingIntent(true)).addAction(2131230957, (CharSequence)this.getString(2131755361), this.buildPendingIntent(false)).build());
   }

   public IBinder onBind(Intent var1) {
      Intrinsics.checkParameterIsNotNull(var1, "intent");
      return null;
   }

   public int onStartCommand(Intent var1, int var2, int var3) {
      if (var1 != null) {
         String var4 = var1.getAction();
         if (var4 != null) {
            if (Intrinsics.areEqual(var4, ACTION_START)) {
               this.showNotification();
               return 2;
            }

            StringBuilder var5 = new StringBuilder();
            var5.append("Unknown intent: ");
            var5.append(var1);
            throw (Throwable)(new IllegalStateException(var5.toString()));
         }
      }

      return 2;
   }

   public void onTaskRemoved(Intent var1) {
      Intent var2 = Companion.buildIntent((Context)this, true);
      var2.setFlags(268435456);
      this.startActivity(var2);
      super.onTaskRemoved(var1);
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      public final Intent buildIntent(Context var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "applicationContext");
         Intent var3 = new Intent(var1, PrivateModeActivity.class);
         if (var2) {
            var3.setAction("intent_extra_sanitize");
         }

         return var3;
      }

      public final void start(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         Intent var2 = new Intent(var1, PrivateSessionNotificationService.class);
         var2.setAction(PrivateSessionNotificationService.ACTION_START);
         if (VERSION.SDK_INT >= 26) {
            var1.startForegroundService(var2);
         } else {
            var1.startService(var2);
         }

      }

      public final void stop$app_focusWebkitRelease(Context var1) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         var1.stopService(new Intent(var1, PrivateSessionNotificationService.class));
      }
   }
}
