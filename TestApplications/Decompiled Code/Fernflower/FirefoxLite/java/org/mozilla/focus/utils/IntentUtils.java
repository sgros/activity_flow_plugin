package org.mozilla.focus.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.notification.NotificationActionBroadcastReceiver;

public class IntentUtils {
   public static Intent createInternalOpenUrlIntent(Context var0, String var1, boolean var2) {
      Intent var3 = new Intent("android.intent.action.VIEW", Uri.parse(var1), var0, MainActivity.class);
      var3.addFlags(67108864);
      var3.putExtra("is_internal_request", true);
      var3.putExtra("open_new_tab", var2);
      return var3;
   }

   public static Intent genDefaultBrowserSettingIntentForBroadcastReceiver(Context var0) {
      Intent var1 = new Intent(var0, NotificationActionBroadcastReceiver.class);
      var1.setAction("action_notification");
      var1.putExtra("ex_no_click_default_browser", true);
      return var1;
   }

   static Intent genFeedbackNotificationActionForBroadcastReceiver(Context var0) {
      Intent var1 = new Intent(var0, NotificationActionBroadcastReceiver.class);
      var1.setAction("action_notification");
      var1.putExtra("ex_no_action_feedback", true);
      return var1;
   }

   static Intent genFeedbackNotificationClickForBroadcastReceiver(Context var0) {
      Intent var1 = new Intent(var0, NotificationActionBroadcastReceiver.class);
      var1.setAction("action_notification");
      var1.putExtra("ex_no_click_love_firefox", true);
      return var1;
   }

   static Intent genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(Context var0) {
      Intent var1 = new Intent(var0, NotificationActionBroadcastReceiver.class);
      var1.setAction("action_notification");
      var1.putExtra("ex_no_click_privacy_policy_update", true);
      return var1;
   }

   static Intent genRateStarNotificationActionForBroadcastReceiver(Context var0) {
      Intent var1 = new Intent(var0, NotificationActionBroadcastReceiver.class);
      var1.setAction("action_notification");
      var1.putExtra("ex_no_action_rate_star", true);
      return var1;
   }

   public static void goToPlayStore(Context var0) {
      goToPlayStore(var0, var0.getPackageName());
   }

   public static void goToPlayStore(Context var0, String var1) {
      try {
         StringBuilder var3 = new StringBuilder();
         var3.append("market://details?id=");
         var3.append(var1);
         Intent var6 = new Intent("android.intent.action.VIEW", Uri.parse(var3.toString()));
         var6.addFlags(268435456);
         var0.startActivity(var6);
      } catch (ActivityNotFoundException var4) {
         StringBuilder var2 = new StringBuilder();
         var2.append("https://play.google.com/store/apps/details?id=");
         var2.append(var1);
         Intent var5 = new Intent("android.intent.action.VIEW", Uri.parse(var2.toString()));
         var5.addFlags(268435456);
         var0.startActivity(var5);
      }

   }

   public static boolean handleExternalUri(Context var0, String var1) {
      Intent var3;
      try {
         var3 = Intent.parseUri(var1, 0);
      } catch (URISyntaxException var2) {
         return false;
      }

      var3.addCategory("android.intent.category.BROWSABLE");
      if (var0.getPackageManager().queryIntentActivities(var3, 0).size() > 0) {
         var0.startActivity(var3);
      }

      return true;
   }

   public static void intentOpenFile(Context var0, String var1, String var2) {
      if (var1 != null) {
         Uri var5 = FileProvider.getUriForFile(var0, "org.mozilla.rocket.provider.fileprovider", new File(URI.create(var1).getPath()));
         Intent var3 = new Intent("android.intent.action.VIEW");
         var3.setDataAndType(var5, var2);
         var3.addFlags(268435457);

         try {
            var0.startActivity(var3);
         } catch (Exception var4) {
            openDownloadPage(var0);
         }
      } else {
         openDownloadPage(var0);
      }

   }

   public static boolean openDefaultAppsSettings(Context var0) {
      try {
         Intent var1 = new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS");
         var0.startActivity(var1);
         return true;
      } catch (ActivityNotFoundException var2) {
         return false;
      }
   }

   private static void openDownloadPage(Context var0) {
      Intent var1 = new Intent("android.intent.action.VIEW_DOWNLOADS");
      var1.setFlags(268435456);
      var0.startActivity(var1);
   }

   public static void openUrl(Context var0, String var1, boolean var2) {
      var0.startActivity(createInternalOpenUrlIntent(var0, var1, var2));
   }
}
