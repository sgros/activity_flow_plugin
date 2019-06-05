package org.mozilla.focus;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.squareup.leakcanary.LeakCanary;
import java.io.File;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.locale.LocaleAwareApplication;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.screenshot.ScreenshotManager;
import org.mozilla.focus.search.SearchEngineManager;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AdjustHelper;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.rocket.partner.PartnerActivator;
import org.mozilla.rocket.privately.PrivateModeActivity;

public final class FocusApplication extends LocaleAwareApplication {
   private boolean isInPrivateProcess;
   public PartnerActivator partnerActivator;

   private final void monitorPrivateProcess() {
      this.registerActivityLifecycleCallbacks((ActivityLifecycleCallbacks)(new ActivityLifecycleCallbacks() {
         public void onActivityCreated(Activity var1, Bundle var2) {
            if (var1 instanceof PrivateModeActivity) {
               FocusApplication.this.setInPrivateProcess(true);
            }

         }

         public void onActivityDestroyed(Activity var1) {
         }

         public void onActivityPaused(Activity var1) {
         }

         public void onActivityResumed(Activity var1) {
         }

         public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
         }

         public void onActivityStarted(Activity var1) {
         }

         public void onActivityStopped(Activity var1) {
         }
      }));
   }

   public File getCacheDir() {
      if (this.isInPrivateProcess) {
         StringBuilder var3 = new StringBuilder();
         File var2 = super.getCacheDir();
         Intrinsics.checkExpressionValueIsNotNull(var2, "super.getCacheDir()");
         var3.append(var2.getAbsolutePath());
         var3.append("-");
         var3.append("private_mode");
         return new File(var3.toString());
      } else {
         File var1 = super.getCacheDir();
         Intrinsics.checkExpressionValueIsNotNull(var1, "super.getCacheDir()");
         return var1;
      }
   }

   public File getDir(String var1, int var2) {
      File var4;
      if (Intrinsics.areEqual(var1, "webview") && this.isInPrivateProcess) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append("-private_mode");
         var4 = super.getDir(var3.toString(), var2);
         Intrinsics.checkExpressionValueIsNotNull(var4, "super.getDir(\"$name-$PRIVATE_PROCESS_NAME\", mode)");
         return var4;
      } else {
         var4 = super.getDir(var1, var2);
         Intrinsics.checkExpressionValueIsNotNull(var4, "super.getDir(name, mode)");
         return var4;
      }
   }

   public void onCreate() {
      super.onCreate();
      Context var1 = (Context)this;
      if (!LeakCanary.isInAnalyzerProcess(var1)) {
         LeakCanary.install((Application)this);
         PreferenceManager.setDefaultValues(var1, 2132017154, false);
         Inject.enableStrictMode();
         SearchEngineManager.getInstance().init(var1);
         NewsSourceManager.getInstance().init(var1);
         TelemetryWrapper.INSTANCE.init(var1);
         AdjustHelper.setupAdjustIfNeeded(var1);
         BrowsingHistoryManager.getInstance().init(var1);
         ScreenshotManager.getInstance().init(var1);
         DownloadInfoManager.getInstance();
         DownloadInfoManager.init(var1);
         NotificationUtil.init(var1);
         this.partnerActivator = new PartnerActivator(var1);
         PartnerActivator var2 = this.partnerActivator;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("partnerActivator");
         }

         var2.launch();
         this.monitorPrivateProcess();
      }
   }

   public final void setInPrivateProcess(boolean var1) {
      this.isInPrivateProcess = var1;
   }
}
