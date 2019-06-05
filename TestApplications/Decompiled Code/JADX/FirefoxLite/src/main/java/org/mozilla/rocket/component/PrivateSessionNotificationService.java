package org.mozilla.rocket.component;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.IBinder;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.notification.NotificationUtil.Channel;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.privately.PrivateModeActivity;

/* compiled from: PrivateSessionNotificationService.kt */
public final class PrivateSessionNotificationService extends Service {
    private static final String ACTION_START = ACTION_START;
    public static final Companion Companion = new Companion();

    /* compiled from: PrivateSessionNotificationService.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final void start(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intent intent = new Intent(context, PrivateSessionNotificationService.class);
            intent.setAction(PrivateSessionNotificationService.ACTION_START);
            if (VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }

        public final void stop$app_focusWebkitRelease(Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            context.stopService(new Intent(context, PrivateSessionNotificationService.class));
        }

        public final Intent buildIntent(Context context, boolean z) {
            Intrinsics.checkParameterIsNotNull(context, "applicationContext");
            Intent intent = new Intent(context, PrivateModeActivity.class);
            if (z) {
                intent.setAction("intent_extra_sanitize");
            }
            return intent;
        }
    }

    public static final Intent buildIntent(Context context, boolean z) {
        return Companion.buildIntent(context, z);
    }

    public IBinder onBind(Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (Intrinsics.areEqual(action, ACTION_START)) {
                    showNotification();
                    return 2;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown intent: ");
                stringBuilder.append(intent);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
        return 2;
    }

    private final void showNotification() {
        startForeground(4000, NotificationUtil.baseBuilder(getApplicationContext(), Channel.PRIVATE).setContentTitle(getString(C0769R.string.private_browsing_erase_message)).setContentIntent(buildPendingIntent(true)).addAction(2131230957, getString(C0769R.string.private_browsing_open_action), buildPendingIntent(false)).build());
    }

    public void onTaskRemoved(Intent intent) {
        Intent buildIntent = Companion.buildIntent(this, true);
        buildIntent.setFlags(268435456);
        startActivity(buildIntent);
        super.onTaskRemoved(intent);
    }

    private final PendingIntent buildPendingIntent(boolean z) {
        PendingIntent activity = PendingIntent.getActivity(getApplicationContext(), 0, Companion.buildIntent(this, z), 134217728);
        Intrinsics.checkExpressionValueIsNotNull(activity, "PendingIntent.getActivit…tent.FLAG_UPDATE_CURRENT)");
        return activity;
    }
}
