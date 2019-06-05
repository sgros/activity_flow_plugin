// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus;

import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.screenshot.ScreenshotManager;
import org.mozilla.focus.history.BrowsingHistoryManager;
import org.mozilla.focus.utils.AdjustHelper;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.content.NewsSourceManager;
import org.mozilla.focus.search.SearchEngineManager;
import android.preference.PreferenceManager;
import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import java.io.File;
import android.app.Application$ActivityLifecycleCallbacks;
import org.mozilla.rocket.partner.PartnerActivator;
import org.mozilla.focus.locale.LocaleAwareApplication;

public final class FocusApplication extends LocaleAwareApplication
{
    private boolean isInPrivateProcess;
    public PartnerActivator partnerActivator;
    
    private final void monitorPrivateProcess() {
        this.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)new FocusApplication$monitorPrivateProcess.FocusApplication$monitorPrivateProcess$1(this));
    }
    
    public File getCacheDir() {
        if (this.isInPrivateProcess) {
            final StringBuilder sb = new StringBuilder();
            final File cacheDir = super.getCacheDir();
            Intrinsics.checkExpressionValueIsNotNull(cacheDir, "super.getCacheDir()");
            sb.append(cacheDir.getAbsolutePath());
            sb.append("-");
            sb.append("private_mode");
            return new File(sb.toString());
        }
        final File cacheDir2 = super.getCacheDir();
        Intrinsics.checkExpressionValueIsNotNull(cacheDir2, "super.getCacheDir()");
        return cacheDir2;
    }
    
    public File getDir(final String str, final int n) {
        if (Intrinsics.areEqual(str, "webview") && this.isInPrivateProcess) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("-private_mode");
            final File dir = super.getDir(sb.toString(), n);
            Intrinsics.checkExpressionValueIsNotNull(dir, "super.getDir(\"$name-$PRIVATE_PROCESS_NAME\", mode)");
            return dir;
        }
        final File dir2 = super.getDir(str, n);
        Intrinsics.checkExpressionValueIsNotNull(dir2, "super.getDir(name, mode)");
        return dir2;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        final Context context = (Context)this;
        if (LeakCanary.isInAnalyzerProcess(context)) {
            return;
        }
        LeakCanary.install(this);
        PreferenceManager.setDefaultValues(context, 2132017154, false);
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
        final PartnerActivator partnerActivator = this.partnerActivator;
        if (partnerActivator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("partnerActivator");
        }
        partnerActivator.launch();
        this.monitorPrivateProcess();
    }
    
    public final void setInPrivateProcess(final boolean isInPrivateProcess) {
        this.isInPrivateProcess = isInPrivateProcess;
    }
}
