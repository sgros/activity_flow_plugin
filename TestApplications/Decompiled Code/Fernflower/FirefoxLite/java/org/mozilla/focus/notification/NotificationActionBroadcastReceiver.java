package org.mozilla.focus.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import org.mozilla.focus.activity.InfoActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;

public class NotificationActionBroadcastReceiver extends BroadcastReceiver {
   public void onReceive(Context var1, Intent var2) {
      if (var2 != null) {
         String var3 = var2.getAction();
         Bundle var4 = var2.getExtras();
         if (var4 != null && var3 != null && "action_notification".equals(var3)) {
            boolean var5 = var4.getBoolean("ex_no_action_rate_star");
            var2 = null;
            if (var5) {
               IntentUtils.goToPlayStore(var1);
               TelemetryWrapper.clickRateApp("positive", "notification");
               NotificationManagerCompat.from(var1).cancel(1001);
            } else if (var4.getBoolean("ex_no_action_feedback")) {
               var2 = IntentUtils.createInternalOpenUrlIntent(var1, var1.getString(2131755368), true);
               Settings.getInstance(var1).setShareAppDialogDidShow();
               TelemetryWrapper.clickRateApp("negative", "notification");
               NotificationManagerCompat.from(var1).cancel(1001);
            } else if (var4.getBoolean("ex_no_click_default_browser")) {
               if (VERSION.SDK_INT >= 24) {
                  var2 = new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
               } else {
                  StringBuilder var6 = new StringBuilder();
                  var6.append(var1.getString(2131755343));
                  var6.append("\ud83d\ude4c");
                  String var7 = var6.toString();
                  var2 = InfoActivity.getIntentFor(var1, SupportUtils.getSumoURLForTopic(var1, "rocket-default"), var7);
               }

               TelemetryWrapper.clickDefaultSettingNotification();
               NotificationManagerCompat.from(var1).cancel(1002);
            } else if (var4.getBoolean("ex_no_click_love_firefox")) {
               var2 = new Intent();
               var2.setClassName(var1, "org.mozilla.rocket.activity.MainActivity");
               var2.putExtra("show_rate_dialog", true);
               TelemetryWrapper.clickRateApp((String)null, "notification");
               NotificationManagerCompat.from(var1).cancel(1001);
            } else if (var4.getBoolean("ex_no_click_privacy_policy_update")) {
               var2 = IntentUtils.createInternalOpenUrlIntent(var1, SupportUtils.getPrivacyURL(), true);
               NotificationManagerCompat.from(var1).cancel(1003);
            } else {
               Log.e("NotifyActionReceiver", "Not a valid action");
            }

            var4.clear();
            if (var2 != null) {
               var2.addFlags(268435456);
               var1.startActivity(var2);
            }

         }
      }
   }
}
