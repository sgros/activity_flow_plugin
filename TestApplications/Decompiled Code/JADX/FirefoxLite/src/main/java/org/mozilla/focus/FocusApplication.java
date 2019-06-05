package org.mozilla.focus;

import android.content.Context;
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
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.rocket.partner.PartnerActivator;

/* compiled from: FocusApplication.kt */
public final class FocusApplication extends LocaleAwareApplication {
    private boolean isInPrivateProcess;
    public PartnerActivator partnerActivator;

    public final void setInPrivateProcess(boolean z) {
        this.isInPrivateProcess = z;
    }

    public File getCacheDir() {
        if (this.isInPrivateProcess) {
            StringBuilder stringBuilder = new StringBuilder();
            File cacheDir = super.getCacheDir();
            Intrinsics.checkExpressionValueIsNotNull(cacheDir, "super.getCacheDir()");
            stringBuilder.append(cacheDir.getAbsolutePath());
            stringBuilder.append("-");
            stringBuilder.append("private_mode");
            return new File(stringBuilder.toString());
        }
        File cacheDir2 = super.getCacheDir();
        Intrinsics.checkExpressionValueIsNotNull(cacheDir2, "super.getCacheDir()");
        return cacheDir2;
    }

    public File getDir(String str, int i) {
        File dir;
        if (Intrinsics.areEqual(str, "webview") && this.isInPrivateProcess) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append("-private_mode");
            dir = super.getDir(stringBuilder.toString(), i);
            Intrinsics.checkExpressionValueIsNotNull(dir, "super.getDir(\"$name-$PRIVATE_PROCESS_NAME\", mode)");
            return dir;
        }
        dir = super.getDir(str, i);
        Intrinsics.checkExpressionValueIsNotNull(dir, "super.getDir(name, mode)");
        return dir;
    }

    public void onCreate() {
        super.onCreate();
        Context context = this;
        if (!LeakCanary.isInAnalyzerProcess(context)) {
            LeakCanary.install(this);
            PreferenceManager.setDefaultValues(context, C0769R.xml.settings, false);
            Inject.enableStrictMode();
            SearchEngineManager.getInstance().init(context);
            NewsSourceManager.getInstance().init(context);
            TelemetryWrapper.INSTANCE.init(context);
            AdjustHelper.setupAdjustIfNeeded(context);
            BrowsingHistoryManager.getInstance().init(context);
            ScreenshotManager.getInstance().init(context);
            DownloadInfoManager.getInstance();
            DownloadInfoManager.init(context);
            NotificationUtil.init(context);
            this.partnerActivator = new PartnerActivator(context);
            PartnerActivator partnerActivator = this.partnerActivator;
            if (partnerActivator == null) {
                Intrinsics.throwUninitializedPropertyAccessException("partnerActivator");
            }
            partnerActivator.launch();
            monitorPrivateProcess();
        }
    }

    private final void monitorPrivateProcess() {
        registerActivityLifecycleCallbacks(new FocusApplication$monitorPrivateProcess$1(this));
    }
}
