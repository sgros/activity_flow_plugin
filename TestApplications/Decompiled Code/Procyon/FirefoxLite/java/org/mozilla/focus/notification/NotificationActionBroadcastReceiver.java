// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.notification;

import android.os.Bundle;
import android.util.Log;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.utils.SupportUtils;
import android.os.Build$VERSION;
import org.mozilla.focus.utils.Settings;
import android.support.v4.app.NotificationManagerCompat;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class NotificationActionBroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        final String action = intent.getAction();
        final Bundle extras = intent.getExtras();
        if (extras != null && action != null && "action_notification".equals(action)) {
            final boolean boolean1 = extras.getBoolean("ex_no_action_rate_star");
            intent = null;
            if (boolean1) {
                IntentUtils.goToPlayStore(context);
                TelemetryWrapper.clickRateApp("positive", "notification");
                NotificationManagerCompat.from(context).cancel(1001);
            }
            else if (extras.getBoolean("ex_no_action_feedback")) {
                intent = IntentUtils.createInternalOpenUrlIntent(context, context.getString(2131755368), true);
                Settings.getInstance(context).setShareAppDialogDidShow();
                TelemetryWrapper.clickRateApp("negative", "notification");
                NotificationManagerCompat.from(context).cancel(1001);
            }
            else if (extras.getBoolean("ex_no_click_default_browser")) {
                if (Build$VERSION.SDK_INT >= 24) {
                    intent = new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(context.getString(2131755343));
                    sb.append("\ud83d\ude4c");
                    intent = InfoActivity.getIntentFor(context, SupportUtils.getSumoURLForTopic(context, "rocket-default"), sb.toString());
                }
                TelemetryWrapper.clickDefaultSettingNotification();
                NotificationManagerCompat.from(context).cancel(1002);
            }
            else if (extras.getBoolean("ex_no_click_love_firefox")) {
                intent = new Intent();
                intent.setClassName(context, "org.mozilla.rocket.activity.MainActivity");
                intent.putExtra("show_rate_dialog", true);
                TelemetryWrapper.clickRateApp(null, "notification");
                NotificationManagerCompat.from(context).cancel(1001);
            }
            else if (extras.getBoolean("ex_no_click_privacy_policy_update")) {
                intent = IntentUtils.createInternalOpenUrlIntent(context, SupportUtils.getPrivacyURL(), true);
                NotificationManagerCompat.from(context).cancel(1003);
            }
            else {
                Log.e("NotifyActionReceiver", "Not a valid action");
            }
            extras.clear();
            if (intent != null) {
                intent.addFlags(268435456);
                context.startActivity(intent);
            }
        }
    }
}
