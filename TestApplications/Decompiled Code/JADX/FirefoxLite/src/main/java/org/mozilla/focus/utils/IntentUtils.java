package org.mozilla.focus.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.p001v4.content.FileProvider;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.notification.NotificationActionBroadcastReceiver;

public class IntentUtils {
    public static boolean handleExternalUri(Context context, String str) {
        try {
            Intent parseUri = Intent.parseUri(str, 0);
            parseUri.addCategory("android.intent.category.BROWSABLE");
            if (context.getPackageManager().queryIntentActivities(parseUri, 0).size() > 0) {
                context.startActivity(parseUri);
            }
            return true;
        } catch (URISyntaxException unused) {
            return false;
        }
    }

    public static void intentOpenFile(Context context, String str, String str2) {
        if (str != null) {
            Uri uriForFile = FileProvider.getUriForFile(context, "org.mozilla.rocket.provider.fileprovider", new File(URI.create(str).getPath()));
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriForFile, str2);
            intent.addFlags(268435457);
            try {
                context.startActivity(intent);
                return;
            } catch (Exception unused) {
                openDownloadPage(context);
                return;
            }
        }
        openDownloadPage(context);
    }

    private static void openDownloadPage(Context context) {
        Intent intent = new Intent("android.intent.action.VIEW_DOWNLOADS");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    public static void openUrl(Context context, String str, boolean z) {
        context.startActivity(createInternalOpenUrlIntent(context, str, z));
    }

    public static Intent createInternalOpenUrlIntent(Context context, String str, boolean z) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str), context, MainActivity.class);
        intent.addFlags(67108864);
        intent.putExtra("is_internal_request", true);
        intent.putExtra("open_new_tab", z);
        return intent;
    }

    public static void goToPlayStore(Context context) {
        goToPlayStore(context, context.getPackageName());
    }

    public static void goToPlayStore(Context context, String str) {
        StringBuilder stringBuilder;
        Intent intent;
        try {
            stringBuilder = new StringBuilder();
            stringBuilder.append("market://details?id=");
            stringBuilder.append(str);
            intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("https://play.google.com/store/apps/details?id=");
            stringBuilder.append(str);
            intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
    }

    public static Intent genDefaultBrowserSettingIntentForBroadcastReceiver(Context context) {
        Intent intent = new Intent(context, NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_default_browser", true);
        return intent;
    }

    static Intent genRateStarNotificationActionForBroadcastReceiver(Context context) {
        Intent intent = new Intent(context, NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_action_rate_star", true);
        return intent;
    }

    static Intent genFeedbackNotificationActionForBroadcastReceiver(Context context) {
        Intent intent = new Intent(context, NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_action_feedback", true);
        return intent;
    }

    static Intent genFeedbackNotificationClickForBroadcastReceiver(Context context) {
        Intent intent = new Intent(context, NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_love_firefox", true);
        return intent;
    }

    static Intent genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(Context context) {
        Intent intent = new Intent(context, NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_privacy_policy_update", true);
        return intent;
    }

    public static boolean openDefaultAppsSettings(Context context) {
        try {
            context.startActivity(new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS"));
            return true;
        } catch (ActivityNotFoundException unused) {
            return false;
        }
    }
}
