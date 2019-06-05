// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.component;

import android.os.Build$VERSION;
import org.mozilla.rocket.privately.PrivateModeActivity;
import android.os.IBinder;
import org.mozilla.focus.notification.NotificationUtil;
import kotlin.jvm.internal.Intrinsics;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.app.Service;

public final class PrivateSessionNotificationService extends Service
{
    private static final String ACTION_START = "start";
    public static final Companion Companion;
    
    static {
        Companion = new Companion(null);
    }
    
    public static final /* synthetic */ String access$getACTION_START$cp() {
        return PrivateSessionNotificationService.ACTION_START;
    }
    
    public static final Intent buildIntent(final Context context, final boolean b) {
        return PrivateSessionNotificationService.Companion.buildIntent(context, b);
    }
    
    private final PendingIntent buildPendingIntent(final boolean b) {
        final PendingIntent activity = PendingIntent.getActivity(this.getApplicationContext(), 0, PrivateSessionNotificationService.Companion.buildIntent((Context)this, b), 134217728);
        Intrinsics.checkExpressionValueIsNotNull(activity, "PendingIntent.getActivit\u2026tent.FLAG_UPDATE_CURRENT)");
        return activity;
    }
    
    private final void showNotification() {
        this.startForeground(4000, NotificationUtil.baseBuilder(this.getApplicationContext(), NotificationUtil.Channel.PRIVATE).setContentTitle(this.getString(2131755360)).setContentIntent(this.buildPendingIntent(true)).addAction(2131230957, this.getString(2131755361), this.buildPendingIntent(false)).build());
    }
    
    public IBinder onBind(final Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        return null;
    }
    
    public int onStartCommand(final Intent obj, final int n, final int n2) {
        if (obj != null) {
            final String action = obj.getAction();
            if (action != null) {
                if (Intrinsics.areEqual(action, PrivateSessionNotificationService.ACTION_START)) {
                    this.showNotification();
                    return 2;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown intent: ");
                sb.append(obj);
                throw new IllegalStateException(sb.toString());
            }
        }
        return 2;
    }
    
    public void onTaskRemoved(final Intent intent) {
        final Intent buildIntent = PrivateSessionNotificationService.Companion.buildIntent((Context)this, true);
        buildIntent.setFlags(268435456);
        this.startActivity(buildIntent);
        super.onTaskRemoved(intent);
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final Intent buildIntent(final Context context, final boolean b) {
            Intrinsics.checkParameterIsNotNull(context, "applicationContext");
            final Intent intent = new Intent(context, (Class)PrivateModeActivity.class);
            if (b) {
                intent.setAction("intent_extra_sanitize");
            }
            return intent;
        }
        
        public final void start(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            final Intent intent = new Intent(context, (Class)PrivateSessionNotificationService.class);
            intent.setAction(PrivateSessionNotificationService.access$getACTION_START$cp());
            if (Build$VERSION.SDK_INT >= 26) {
                context.startForegroundService(intent);
            }
            else {
                context.startService(intent);
            }
        }
        
        public final void stop$app_focusWebkitRelease(final Context context) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            context.stopService(new Intent(context, (Class)PrivateSessionNotificationService.class));
        }
    }
}
