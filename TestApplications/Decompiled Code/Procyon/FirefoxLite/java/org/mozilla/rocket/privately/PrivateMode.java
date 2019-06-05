// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately;

import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.AppConstants;
import android.content.ComponentName;
import java.util.Iterator;
import kotlin.TypeCastException;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import android.app.ActivityManager$RunningServiceInfo;
import android.app.ActivityManager;
import kotlin.jvm.internal.Intrinsics;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import org.mozilla.fileutils.FileUtils;
import java.io.File;
import android.content.Context;

public final class PrivateMode
{
    public static final Companion Companion;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final boolean hasPrivateSession(final Context context) {
        return PrivateMode.Companion.hasPrivateSession(context);
    }
    
    public static final boolean isEnable(final Context context) {
        return PrivateMode.Companion.isEnable(context);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        private final void clean(final File file, final Context context) {
            if (!FileUtils.deleteDirectory(file)) {
                final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                if (defaultSharedPreferences != null) {
                    final SharedPreferences$Editor edit = defaultSharedPreferences.edit();
                    if (edit != null) {
                        final SharedPreferences$Editor putString = edit.putString("pref_key_sanitize_reminder", file.getAbsolutePath());
                        if (putString != null) {
                            putString.apply();
                        }
                    }
                }
            }
        }
        
        public final boolean hasPrivateSession(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            final Object systemService = context.getSystemService("activity");
            if (systemService != null) {
                for (final ActivityManager$RunningServiceInfo activityManager$RunningServiceInfo : ((ActivityManager)systemService).getRunningServices(Integer.MAX_VALUE)) {
                    final String name = PrivateSessionNotificationService.class.getName();
                    final ComponentName service = activityManager$RunningServiceInfo.service;
                    Intrinsics.checkExpressionValueIsNotNull(service, "service.service");
                    if (Intrinsics.areEqual(name, service.getClassName())) {
                        return true;
                    }
                }
                return false;
            }
            throw new TypeCastException("null cannot be cast to non-null type android.app.ActivityManager");
        }
        
        public final boolean isEnable(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            if (!AppConstants.isReleaseBuild() && !AppConstants.isBetaBuild()) {
                return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_key_private_mode_enabled", true);
            }
            return AppConfigWrapper.isPrivateModeEnabled(context);
        }
        
        public final void sanitize(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            ThreadUtils.postToBackgroundThread((Runnable)new PrivateMode$Companion$sanitize.PrivateMode$Companion$sanitize$1(context));
        }
    }
}
