package org.mozilla.rocket.periodic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.DialogUtils;

public final class FirstLaunchWorker extends Worker {
   private static final String ACTION;
   public static final FirstLaunchWorker.Companion Companion = new FirstLaunchWorker.Companion((DefaultConstructorMarker)null);
   private static final String TAG;

   static {
      String var0 = FirstLaunchWorker.class.getSimpleName();
      Intrinsics.checkExpressionValueIsNotNull(var0, "FirstLaunchWorker::class.java.simpleName");
      TAG = var0;
      StringBuilder var1 = new StringBuilder();
      var1.append("org.mozilla.rocket.action.");
      var1.append(TAG);
      ACTION = var1.toString();
   }

   public FirstLaunchWorker(Context var1, WorkerParameters var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "workerParams");
      super(var1, var2);
   }

   public ListenableWorker.Result doWork() {
      String var1 = AppConfigWrapper.getFirstLaunchNotificationMessage(this.getApplicationContext());
      DialogUtils.showDefaultSettingNotification(this.getApplicationContext(), var1);
      TelemetryWrapper.showFirstrunNotification(AppConfigWrapper.getFirstLaunchWorkerTimer(this.getApplicationContext()), var1);
      FirstLaunchWorker.Companion var3 = Companion;
      Context var2 = this.getApplicationContext();
      Intrinsics.checkExpressionValueIsNotNull(var2, "applicationContext");
      var3.setNotificationFired(var2, true);
      ListenableWorker.Result var4 = ListenableWorker.Result.success();
      Intrinsics.checkExpressionValueIsNotNull(var4, "Result.success()");
      return var4;
   }

   public static final class Companion {
      private Companion() {
      }

      // $FF: synthetic method
      public Companion(DefaultConstructorMarker var1) {
         this();
      }

      private final SharedPreferences getSharedPreference(Context var1) {
         SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(var1);
         Intrinsics.checkExpressionValueIsNotNull(var2, "PreferenceManager.getDef…haredPreferences(context)");
         return var2;
      }

      // $FF: synthetic method
      public static boolean isNotificationFired$default(FirstLaunchWorker.Companion var0, Context var1, boolean var2, int var3, Object var4) {
         if ((var3 & 2) != 0) {
            var2 = false;
         }

         return var0.isNotificationFired(var1, var2);
      }

      public final String getACTION() {
         return FirstLaunchWorker.ACTION;
      }

      public final String getTAG() {
         return FirstLaunchWorker.TAG;
      }

      public final boolean isNotificationFired(Context var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         return ((FirstLaunchWorker.Companion)this).getSharedPreference(var1).getBoolean("pref-key-boolean-notification-fired", var2);
      }

      public final void setNotificationFired(Context var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "context");
         Editor var3 = ((FirstLaunchWorker.Companion)this).getSharedPreference(var1).edit();
         var3.putBoolean("pref-key-boolean-notification-fired", var2);
         var3.apply();
      }
   }
}
