// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.activity.MainActivity;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.content.DialogInterface$OnCancelListener;
import org.mozilla.focus.notification.NotificationUtil;
import android.text.TextUtils;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.DialogInterface;
import android.view.View$OnLongClickListener;
import android.view.View$OnClickListener;
import android.support.v7.app.AlertDialog;
import android.widget.RelativeLayout$LayoutParams;
import android.content.Context;
import org.mozilla.focus.widget.FocusView;
import android.app.Dialog;
import android.view.ViewGroup;
import android.view.View;
import android.app.Activity;

public class DialogUtils
{
    public static Dialog createSpotlightDialog(final Activity activity, final View view, final ViewGroup view2) {
        final int[] array = new int[2];
        view.getLocationInWindow(array);
        view2.addView((View)new FocusView((Context)activity, array[0] + view.getMeasuredWidth() / 2, array[1] + view.getMeasuredHeight() / 2, activity.getResources().getDimensionPixelSize(2131165420)), 0);
        final View viewById = view2.findViewById(2131296660);
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = (RelativeLayout$LayoutParams)viewById.getLayoutParams();
        relativeLayout$LayoutParams.width = view.getMeasuredWidth();
        relativeLayout$LayoutParams.height = view.getMeasuredHeight();
        relativeLayout$LayoutParams.setMargins(array[0], array[1] - ViewUtils.getStatusBarHeight(activity), 0, 0);
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity, 2131820832);
        builder.setView((View)view2);
        final AlertDialog create = builder.create();
        viewById.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$RwMJMXy3qh63FhjremxtVkmjptk(create, view));
        viewById.setOnLongClickListener((View$OnLongClickListener)new _$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0(create, view));
        view2.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$m49kUVkm4vvAyuBKdJaFTFqzps0(create));
        return create;
    }
    
    public static void showDefaultSettingNotification(final Context context) {
        showDefaultSettingNotification(context, null);
    }
    
    public static void showDefaultSettingNotification(final Context context, final String s) {
        final PendingIntent broadcast = PendingIntent.getBroadcast(context, 4, IntentUtils.genDefaultBrowserSettingIntentForBroadcastReceiver(context), 1073741824);
        String string = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getString(2131755343));
            sb.append("?\ud83d\ude0a");
            string = sb.toString();
        }
        NotificationUtil.sendNotification(context, 1002, NotificationUtil.importantBuilder(context).setContentTitle(string).setContentIntent(broadcast));
        Settings.getInstance(context).setDefaultBrowserSettingDidShow();
    }
    
    public static Dialog showMyShotOnBoarding(final Activity activity, final View view, final DialogInterface$OnCancelListener onCancelListener, final View$OnClickListener onClickListener) {
        final ViewGroup viewGroup = (ViewGroup)LayoutInflater.from((Context)activity).inflate(2131492995, (ViewGroup)null);
        viewGroup.findViewById(2131296531).setOnClickListener(onClickListener);
        final Dialog spotlightDialog = createSpotlightDialog(activity, view, viewGroup);
        spotlightDialog.setOnCancelListener(onCancelListener);
        spotlightDialog.show();
        return spotlightDialog;
    }
    
    public static void showPrivacyPolicyUpdateNotification(final Context context) {
        NotificationUtil.sendNotification(context, 1003, NotificationUtil.importantBuilder(context).setContentTitle(context.getString(2131755358)).setContentText(context.getString(2131755357)).setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(2131755357))).setContentIntent(PendingIntent.getBroadcast(context, 5, IntentUtils.genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(context), 1073741824)));
        NewFeatureNotice.getInstance(context).setPrivacyPolicyUpdateNoticeDidShow();
    }
    
    public static void showRateAppDialog(final Context context) {
        if (context == null) {
            return;
        }
        final AlertDialog create = new AlertDialog.Builder(context).create();
        create.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog(context));
        final View inflate = LayoutInflater.from(context).inflate(2131492988, (ViewGroup)null);
        ((TextView)inflate.findViewById(2131296584)).setText((CharSequence)context.getString(2131755367, new Object[] { context.getString(2131755062) }));
        inflate.findViewById(2131296402).setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4(context, create));
        final TextView textView = (TextView)inflate.findViewById(2131296404);
        final String rateAppPositiveString = AppConfigWrapper.getRateAppPositiveString(context);
        if (!TextUtils.isEmpty((CharSequence)rateAppPositiveString)) {
            textView.setText((CharSequence)rateAppPositiveString);
        }
        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY(context, create));
        final String rateAppDialogTitle = AppConfigWrapper.getRateAppDialogTitle(context);
        if (!TextUtils.isEmpty((CharSequence)rateAppDialogTitle)) {
            ((TextView)inflate.findViewById(2131296584)).setText((CharSequence)rateAppDialogTitle);
        }
        final String rateAppDialogContent = AppConfigWrapper.getRateAppDialogContent(context);
        if (!TextUtils.isEmpty((CharSequence)rateAppDialogContent)) {
            ((TextView)inflate.findViewById(2131296583)).setText((CharSequence)rateAppDialogContent);
        }
        final TextView textView2 = (TextView)inflate.findViewById(2131296403);
        final String rateAppNegativeString = AppConfigWrapper.getRateAppNegativeString(context);
        if (!TextUtils.isEmpty((CharSequence)rateAppNegativeString)) {
            textView2.setText((CharSequence)rateAppNegativeString);
        }
        textView2.setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$crSW_TsUi_bE8SAwjVeDaII7Xlc(context, create));
        create.setView(inflate);
        create.setCanceledOnTouchOutside(true);
        create.show();
        Settings.getInstance(context).setRateAppDialogDidShow();
    }
    
    public static void showRateAppNotification(final Context context) {
        final PendingIntent broadcast = PendingIntent.getBroadcast(context, 1, IntentUtils.genFeedbackNotificationClickForBroadcastReceiver(context), 1073741824);
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getString(2131755367, new Object[] { context.getString(2131755062) }));
        sb.append("\ud83d\ude00");
        final NotificationCompat.Builder setContentIntent = NotificationUtil.importantBuilder(context).setContentText(sb.toString()).setContentIntent(broadcast);
        setContentIntent.addAction(2131230944, context.getString(2131755370), PendingIntent.getBroadcast(context, 2, IntentUtils.genRateStarNotificationActionForBroadcastReceiver(context), 1073741824));
        setContentIntent.addAction(2131230942, context.getString(2131755369), PendingIntent.getBroadcast(context, 3, IntentUtils.genFeedbackNotificationActionForBroadcastReceiver(context), 1073741824));
        NotificationUtil.sendNotification(context, 1001, setContentIntent);
        Settings.getInstance(context).setRateAppNotificationDidShow();
    }
    
    public static void showShareAppDialog(final Context context) {
        if (context == null) {
            return;
        }
        final AlertDialog create = new AlertDialog.Builder(context).create();
        create.setOnCancelListener((DialogInterface$OnCancelListener)new _$$Lambda$DialogUtils$5HFyet6oHYCHE46_DyDOrH6PVuc(context));
        final View inflate = LayoutInflater.from(context).inflate(2131492989, (ViewGroup)null);
        ((TextView)inflate.findViewById(2131296640)).setText((CharSequence)AppConfigWrapper.getShareAppDialogTitle(context));
        ((TextView)inflate.findViewById(2131296639)).setText((CharSequence)AppConfigWrapper.getShareAppDialogContent(context));
        inflate.findViewById(2131296405).setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4(create, context));
        inflate.findViewById(2131296406).setOnClickListener((View$OnClickListener)new _$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4(context, create));
        create.setView(inflate);
        create.setCanceledOnTouchOutside(true);
        create.show();
        Settings.getInstance(context).setShareAppDialogDidShow();
    }
    
    public static Dialog showSpotlight(final Activity activity, final View view, final DialogInterface$OnCancelListener onCancelListener, final int text) {
        final ViewGroup viewGroup = (ViewGroup)LayoutInflater.from((Context)activity).inflate(2131493018, (ViewGroup)null);
        ((TextView)viewGroup.findViewById(2131296659)).setText(text);
        final Dialog spotlightDialog = createSpotlightDialog(activity, view, viewGroup);
        spotlightDialog.setOnCancelListener(onCancelListener);
        spotlightDialog.show();
        return spotlightDialog;
    }
    
    private static void telemetryFeedback(final Context context, final String s) {
        if (context instanceof MainActivity) {
            TelemetryWrapper.clickRateApp(s, "contextual_hints");
        }
        else if (context instanceof SettingsActivity) {
            TelemetryWrapper.clickRateApp(s, "settings");
        }
    }
    
    private static void telemetryShareApp(final Context context, final String s) {
        if (context instanceof MainActivity) {
            TelemetryWrapper.promoteShareClickEvent(s, "contextual_hints");
        }
        else if (context instanceof SettingsActivity) {
            TelemetryWrapper.promoteShareClickEvent(s, "settings");
        }
    }
}
