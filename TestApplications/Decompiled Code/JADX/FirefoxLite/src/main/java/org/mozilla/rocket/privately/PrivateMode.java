package org.mozilla.rocket.privately;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import java.io.File;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: PrivateMode.kt */
public final class PrivateMode {
    public static final Companion Companion = new Companion();

    /* compiled from: PrivateMode.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final boolean isEnable(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            if (AppConstants.isReleaseBuild() || AppConstants.isBetaBuild()) {
                return AppConfigWrapper.isPrivateModeEnabled(context);
            }
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_key_private_mode_enabled", true);
        }

        public final void sanitize(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            ThreadUtils.postToBackgroundThread((Runnable) new PrivateMode$Companion$sanitize$1(context));
        }

        public final boolean hasPrivateSession(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Object systemService = context.getSystemService("activity");
            if (systemService != null) {
                for (RunningServiceInfo runningServiceInfo : ((ActivityManager) systemService).getRunningServices(Integer.MAX_VALUE)) {
                    String name = PrivateSessionNotificationService.class.getName();
                    ComponentName componentName = runningServiceInfo.service;
                    Intrinsics.checkExpressionValueIsNotNull(componentName, "service.service");
                    if (Intrinsics.areEqual(name, componentName.getClassName())) {
                        return true;
                    }
                }
                return false;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.app.ActivityManager");
        }

        private final void clean(File file, Context context) {
            if (!FileUtils.deleteDirectory(file)) {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                if (defaultSharedPreferences != null) {
                    Editor edit = defaultSharedPreferences.edit();
                    if (edit != null) {
                        Editor putString = edit.putString("pref_key_sanitize_reminder", file.getAbsolutePath());
                        if (putString != null) {
                            putString.apply();
                        }
                    }
                }
            }
        }
    }

    public static final boolean hasPrivateSession(Context context) {
        return Companion.hasPrivateSession(context);
    }

    public static final boolean isEnable(Context context) {
        return Companion.isEnable(context);
    }
}
