// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.periodic;

import android.content.SharedPreferences$Editor;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import androidx.work.Worker;

public final class FirstLaunchWorker extends Worker
{
    private static final String ACTION;
    public static final Companion Companion;
    private static final String TAG;
    
    static {
        Companion = new Companion(null);
        final String simpleName = FirstLaunchWorker.class.getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "FirstLaunchWorker::class.java.simpleName");
        TAG = simpleName;
        final StringBuilder sb = new StringBuilder();
        sb.append("org.mozilla.rocket.action.");
        sb.append(FirstLaunchWorker.TAG);
        ACTION = sb.toString();
    }
    
    public FirstLaunchWorker(final Context context, final WorkerParameters workerParameters) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(workerParameters, "workerParams");
        super(context, workerParameters);
    }
    
    public static final /* synthetic */ String access$getACTION$cp() {
        return FirstLaunchWorker.ACTION;
    }
    
    public static final /* synthetic */ String access$getTAG$cp() {
        return FirstLaunchWorker.TAG;
    }
    
    @Override
    public Result doWork() {
        final String firstLaunchNotificationMessage = AppConfigWrapper.getFirstLaunchNotificationMessage(this.getApplicationContext());
        DialogUtils.showDefaultSettingNotification(this.getApplicationContext(), firstLaunchNotificationMessage);
        TelemetryWrapper.showFirstrunNotification(AppConfigWrapper.getFirstLaunchWorkerTimer(this.getApplicationContext()), firstLaunchNotificationMessage);
        final Companion companion = FirstLaunchWorker.Companion;
        final Context applicationContext = this.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
        companion.setNotificationFired(applicationContext, true);
        final Result success = Result.success();
        Intrinsics.checkExpressionValueIsNotNull(success, "Result.success()");
        return success;
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        private final SharedPreferences getSharedPreference(final Context context) {
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef\u2026haredPreferences(context)");
            return defaultSharedPreferences;
        }
        
        public final String getACTION() {
            return FirstLaunchWorker.access$getACTION$cp();
        }
        
        public final String getTAG() {
            return FirstLaunchWorker.access$getTAG$cp();
        }
        
        public final boolean isNotificationFired(final Context context, final boolean b) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return this.getSharedPreference(context).getBoolean("pref-key-boolean-notification-fired", b);
        }
        
        public final void setNotificationFired(final Context context, final boolean b) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            final SharedPreferences$Editor edit = this.getSharedPreference(context).edit();
            edit.putBoolean("pref-key-boolean-notification-fired", b);
            edit.apply();
        }
    }
}
