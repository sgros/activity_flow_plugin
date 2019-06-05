package org.mozilla.rocket.periodic;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import androidx.work.ListenableWorker.Result;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.DialogUtils;

/* compiled from: FirstLaunchWorker.kt */
public final class FirstLaunchWorker extends Worker {
    private static final String ACTION;
    public static final Companion Companion = new Companion();
    private static final String TAG;

    /* compiled from: FirstLaunchWorker.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final String getTAG() {
            return FirstLaunchWorker.TAG;
        }

        public final String getACTION() {
            return FirstLaunchWorker.ACTION;
        }

        public static /* synthetic */ boolean isNotificationFired$default(Companion companion, Context context, boolean z, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            return companion.isNotificationFired(context, z);
        }

        public final boolean isNotificationFired(Context context, boolean z) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            return getSharedPreference(context).getBoolean("pref-key-boolean-notification-fired", z);
        }

        public final void setNotificationFired(Context context, boolean z) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Editor edit = getSharedPreference(context).edit();
            edit.putBoolean("pref-key-boolean-notification-fired", z);
            edit.apply();
        }

        private final SharedPreferences getSharedPreference(Context context) {
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            Intrinsics.checkExpressionValueIsNotNull(defaultSharedPreferences, "PreferenceManager.getDef…haredPreferences(context)");
            return defaultSharedPreferences;
        }
    }

    public FirstLaunchWorker(Context context, WorkerParameters workerParameters) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(workerParameters, "workerParams");
        super(context, workerParameters);
    }

    static {
        String simpleName = FirstLaunchWorker.class.getSimpleName();
        Intrinsics.checkExpressionValueIsNotNull(simpleName, "FirstLaunchWorker::class.java.simpleName");
        TAG = simpleName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("org.mozilla.rocket.action.");
        stringBuilder.append(TAG);
        ACTION = stringBuilder.toString();
    }

    public Result doWork() {
        String firstLaunchNotificationMessage = AppConfigWrapper.getFirstLaunchNotificationMessage(getApplicationContext());
        DialogUtils.showDefaultSettingNotification(getApplicationContext(), firstLaunchNotificationMessage);
        TelemetryWrapper.showFirstrunNotification(AppConfigWrapper.getFirstLaunchWorkerTimer(getApplicationContext()), firstLaunchNotificationMessage);
        Companion companion = Companion;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
        companion.setNotificationFired(applicationContext, true);
        Result success = Result.success();
        Intrinsics.checkExpressionValueIsNotNull(success, "Result.success()");
        return success;
    }
}
