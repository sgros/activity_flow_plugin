package org.mozilla.focus.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.p001v4.app.NotificationManagerCompat;
import android.util.Log;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.rocket.C0769R;

public class NotificationActionBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            if (extras != null && action != null && "action_notification".equals(action)) {
                Intent intent2 = null;
                Intent intent3;
                if (extras.getBoolean("ex_no_action_rate_star")) {
                    IntentUtils.goToPlayStore(context);
                    TelemetryWrapper.clickRateApp("positive", "notification");
                    NotificationManagerCompat.from(context).cancel(1001);
                } else if (extras.getBoolean("ex_no_action_feedback")) {
                    intent2 = IntentUtils.createInternalOpenUrlIntent(context, context.getString(C0769R.string.rate_app_feedback_url), true);
                    Settings.getInstance(context).setShareAppDialogDidShow();
                    TelemetryWrapper.clickRateApp("negative", "notification");
                    NotificationManagerCompat.from(context).cancel(1001);
                } else if (extras.getBoolean("ex_no_click_default_browser")) {
                    if (VERSION.SDK_INT >= 24) {
                        intent3 = new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(context.getString(C0769R.string.preference_default_browser));
                        stringBuilder.append("🙌");
                        intent3 = InfoActivity.getIntentFor(context, SupportUtils.getSumoURLForTopic(context, "rocket-default"), stringBuilder.toString());
                    }
                    intent2 = intent3;
                    TelemetryWrapper.clickDefaultSettingNotification();
                    NotificationManagerCompat.from(context).cancel(1002);
                } else if (extras.getBoolean("ex_no_click_love_firefox")) {
                    intent3 = new Intent();
                    intent3.setClassName(context, "org.mozilla.rocket.activity.MainActivity");
                    intent3.putExtra("show_rate_dialog", true);
                    TelemetryWrapper.clickRateApp(null, "notification");
                    NotificationManagerCompat.from(context).cancel(1001);
                    intent2 = intent3;
                } else if (extras.getBoolean("ex_no_click_privacy_policy_update")) {
                    intent2 = IntentUtils.createInternalOpenUrlIntent(context, SupportUtils.getPrivacyURL(), true);
                    NotificationManagerCompat.from(context).cancel(1003);
                } else {
                    Log.e("NotifyActionReceiver", "Not a valid action");
                }
                extras.clear();
                if (intent2 != null) {
                    intent2.addFlags(268435456);
                    context.startActivity(intent2);
                }
            }
        }
    }
}
