// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.support.v4.content.FileProvider;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import android.content.ActivityNotFoundException;
import org.mozilla.focus.notification.NotificationActionBroadcastReceiver;
import org.mozilla.focus.activity.MainActivity;
import android.net.Uri;
import android.content.Intent;
import android.content.Context;

public class IntentUtils
{
    public static Intent createInternalOpenUrlIntent(final Context context, final String s, final boolean b) {
        final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(s), context, (Class)MainActivity.class);
        intent.addFlags(67108864);
        intent.putExtra("is_internal_request", true);
        intent.putExtra("open_new_tab", b);
        return intent;
    }
    
    public static Intent genDefaultBrowserSettingIntentForBroadcastReceiver(final Context context) {
        final Intent intent = new Intent(context, (Class)NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_default_browser", true);
        return intent;
    }
    
    static Intent genFeedbackNotificationActionForBroadcastReceiver(final Context context) {
        final Intent intent = new Intent(context, (Class)NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_action_feedback", true);
        return intent;
    }
    
    static Intent genFeedbackNotificationClickForBroadcastReceiver(final Context context) {
        final Intent intent = new Intent(context, (Class)NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_love_firefox", true);
        return intent;
    }
    
    static Intent genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(final Context context) {
        final Intent intent = new Intent(context, (Class)NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_click_privacy_policy_update", true);
        return intent;
    }
    
    static Intent genRateStarNotificationActionForBroadcastReceiver(final Context context) {
        final Intent intent = new Intent(context, (Class)NotificationActionBroadcastReceiver.class);
        intent.setAction("action_notification");
        intent.putExtra("ex_no_action_rate_star", true);
        return intent;
    }
    
    public static void goToPlayStore(final Context context) {
        goToPlayStore(context, context.getPackageName());
    }
    
    public static void goToPlayStore(final Context context, final String s) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("market://details?id=");
            sb.append(s);
            final Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
            intent.addFlags(268435456);
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("https://play.google.com/store/apps/details?id=");
            sb2.append(s);
            final Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString()));
            intent2.addFlags(268435456);
            context.startActivity(intent2);
        }
    }
    
    public static boolean handleExternalUri(final Context context, final String s) {
        try {
            final Intent uri = Intent.parseUri(s, 0);
            uri.addCategory("android.intent.category.BROWSABLE");
            if (context.getPackageManager().queryIntentActivities(uri, 0).size() > 0) {
                context.startActivity(uri);
            }
            return true;
        }
        catch (URISyntaxException ex) {
            return false;
        }
    }
    
    public static void intentOpenFile(final Context context, final String str, final String s) {
        if (str != null) {
            final Uri uriForFile = FileProvider.getUriForFile(context, "org.mozilla.rocket.provider.fileprovider", new File(URI.create(str).getPath()));
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(uriForFile, s);
            intent.addFlags(268435457);
            try {
                context.startActivity(intent);
            }
            catch (Exception ex) {
                openDownloadPage(context);
            }
        }
        else {
            openDownloadPage(context);
        }
    }
    
    public static boolean openDefaultAppsSettings(final Context context) {
        try {
            context.startActivity(new Intent("android.settings.MANAGE_DEFAULT_APPS_SETTINGS"));
            return true;
        }
        catch (ActivityNotFoundException ex) {
            return false;
        }
    }
    
    private static void openDownloadPage(final Context context) {
        final Intent intent = new Intent("android.intent.action.VIEW_DOWNLOADS");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }
    
    public static void openUrl(final Context context, final String s, final boolean b) {
        context.startActivity(createInternalOpenUrlIntent(context, s, b));
    }
}
