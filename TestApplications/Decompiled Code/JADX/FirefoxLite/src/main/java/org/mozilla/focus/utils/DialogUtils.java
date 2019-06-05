package org.mozilla.focus.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.support.p001v4.app.NotificationCompat;
import android.support.p001v4.app.NotificationCompat.BigTextStyle;
import android.support.p004v7.app.AlertDialog;
import android.support.p004v7.app.AlertDialog.Builder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.widget.FocusView;
import org.mozilla.rocket.C0769R;

public class DialogUtils {
    public static void showRateAppDialog(Context context) {
        if (context != null) {
            AlertDialog create = new Builder(context).create();
            create.setOnCancelListener(new C0528-$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog(context));
            View inflate = LayoutInflater.from(context).inflate(C0769R.layout.layout_rate_app_dialog, null);
            ((TextView) inflate.findViewById(C0427R.C0426id.rate_app_dialog_textview_title)).setText(context.getString(C0769R.string.rate_app_dialog_text_title, new Object[]{context.getString(C0769R.string.app_name)}));
            inflate.findViewById(C0427R.C0426id.dialog_rate_app_btn_close).setOnClickListener(new C0532-$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4(context, create));
            TextView textView = (TextView) inflate.findViewById(C0427R.C0426id.dialog_rate_app_btn_go_rate);
            String rateAppPositiveString = AppConfigWrapper.getRateAppPositiveString(context);
            if (!TextUtils.isEmpty(rateAppPositiveString)) {
                textView.setText(rateAppPositiveString);
            }
            textView.setOnClickListener(new C0535-$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY(context, create));
            String rateAppDialogTitle = AppConfigWrapper.getRateAppDialogTitle(context);
            if (!TextUtils.isEmpty(rateAppDialogTitle)) {
                ((TextView) inflate.findViewById(C0427R.C0426id.rate_app_dialog_textview_title)).setText(rateAppDialogTitle);
            }
            String rateAppDialogContent = AppConfigWrapper.getRateAppDialogContent(context);
            if (!TextUtils.isEmpty(rateAppDialogContent)) {
                ((TextView) inflate.findViewById(C0427R.C0426id.rate_app_dialog_text_content)).setText(rateAppDialogContent);
            }
            TextView textView2 = (TextView) inflate.findViewById(C0427R.C0426id.dialog_rate_app_btn_feedback);
            rateAppDialogTitle = AppConfigWrapper.getRateAppNegativeString(context);
            if (!TextUtils.isEmpty(rateAppDialogTitle)) {
                textView2.setText(rateAppDialogTitle);
            }
            textView2.setOnClickListener(new C0533-$$Lambda$DialogUtils$crSW_TsUi-bE8SAwjVeDaII7Xlc(context, create));
            create.setView(inflate);
            create.setCanceledOnTouchOutside(true);
            create.show();
            Settings.getInstance(context).setRateAppDialogDidShow();
        }
    }

    static /* synthetic */ void lambda$showRateAppDialog$0(Context context, DialogInterface dialogInterface) {
        Settings.getInstance(context).setRateAppDialogDidDismiss();
        telemetryFeedback(context, "dismiss");
    }

    static /* synthetic */ void lambda$showRateAppDialog$1(Context context, AlertDialog alertDialog, View view) {
        Settings.getInstance(context).setRateAppDialogDidDismiss();
        alertDialog.dismiss();
        telemetryFeedback(context, "dismiss");
    }

    static /* synthetic */ void lambda$showRateAppDialog$2(Context context, AlertDialog alertDialog, View view) {
        IntentUtils.goToPlayStore(context);
        alertDialog.dismiss();
        telemetryFeedback(context, "positive");
    }

    static /* synthetic */ void lambda$showRateAppDialog$3(Context context, AlertDialog alertDialog, View view) {
        Settings.getInstance(context).setShareAppDialogDidShow();
        IntentUtils.openUrl(context, context.getString(C0769R.string.rate_app_feedback_url), true);
        alertDialog.dismiss();
        telemetryFeedback(context, "negative");
    }

    private static void telemetryFeedback(Context context, String str) {
        if (context instanceof MainActivity) {
            TelemetryWrapper.clickRateApp(str, "contextual_hints");
        } else if (context instanceof SettingsActivity) {
            TelemetryWrapper.clickRateApp(str, "settings");
        }
    }

    public static void showShareAppDialog(Context context) {
        if (context != null) {
            AlertDialog create = new Builder(context).create();
            create.setOnCancelListener(new C0527-$$Lambda$DialogUtils$5HFyet6oHYCHE46-DyDOrH6PVuc(context));
            View inflate = LayoutInflater.from(context).inflate(C0769R.layout.layout_share_app_dialog, null);
            ((TextView) inflate.findViewById(C0427R.C0426id.share_app_dialog_textview_title)).setText(AppConfigWrapper.getShareAppDialogTitle(context));
            ((TextView) inflate.findViewById(C0427R.C0426id.share_app_dialog_textview_content)).setText(AppConfigWrapper.getShareAppDialogContent(context));
            inflate.findViewById(C0427R.C0426id.dialog_share_app_btn_close).setOnClickListener(new C0526-$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4(create, context));
            inflate.findViewById(C0427R.C0426id.dialog_share_app_btn_share).setOnClickListener(new C0531-$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4(context, create));
            create.setView(inflate);
            create.setCanceledOnTouchOutside(true);
            create.show();
            Settings.getInstance(context).setShareAppDialogDidShow();
        }
    }

    static /* synthetic */ void lambda$showShareAppDialog$5(AlertDialog alertDialog, Context context, View view) {
        alertDialog.dismiss();
        telemetryShareApp(context, "dismiss");
    }

    static /* synthetic */ void lambda$showShareAppDialog$6(Context context, AlertDialog alertDialog, View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(C0769R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", AppConfigWrapper.getShareAppMessage(context));
        context.startActivity(Intent.createChooser(intent, null));
        alertDialog.dismiss();
        telemetryShareApp(context, "share");
    }

    private static void telemetryShareApp(Context context, String str) {
        if (context instanceof MainActivity) {
            TelemetryWrapper.promoteShareClickEvent(str, "contextual_hints");
        } else if (context instanceof SettingsActivity) {
            TelemetryWrapper.promoteShareClickEvent(str, "settings");
        }
    }

    public static void showRateAppNotification(Context context) {
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 1, IntentUtils.genFeedbackNotificationClickForBroadcastReceiver(context), 1073741824);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getString(C0769R.string.rate_app_dialog_text_title, new Object[]{context.getString(C0769R.string.app_name)}));
        stringBuilder.append("😀");
        NotificationCompat.Builder contentIntent = NotificationUtil.importantBuilder(context).setContentText(stringBuilder.toString()).setContentIntent(broadcast);
        contentIntent.addAction(2131230944, context.getString(C0769R.string.rate_app_notification_action_rate), PendingIntent.getBroadcast(context, 2, IntentUtils.genRateStarNotificationActionForBroadcastReceiver(context), 1073741824));
        contentIntent.addAction(2131230942, context.getString(C0769R.string.rate_app_notification_action_feedback), PendingIntent.getBroadcast(context, 3, IntentUtils.genFeedbackNotificationActionForBroadcastReceiver(context), 1073741824));
        NotificationUtil.sendNotification(context, 1001, contentIntent);
        Settings.getInstance(context).setRateAppNotificationDidShow();
    }

    public static void showDefaultSettingNotification(Context context) {
        showDefaultSettingNotification(context, null);
    }

    public static void showDefaultSettingNotification(Context context, String str) {
        CharSequence str2;
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 4, IntentUtils.genDefaultBrowserSettingIntentForBroadcastReceiver(context), 1073741824);
        if (TextUtils.isEmpty(str2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(context.getString(C0769R.string.preference_default_browser));
            stringBuilder.append("?😊");
            str2 = stringBuilder.toString();
        }
        NotificationUtil.sendNotification(context, 1002, NotificationUtil.importantBuilder(context).setContentTitle(str2).setContentIntent(broadcast));
        Settings.getInstance(context).setDefaultBrowserSettingDidShow();
    }

    public static void showPrivacyPolicyUpdateNotification(Context context) {
        NotificationUtil.sendNotification(context, 1003, NotificationUtil.importantBuilder(context).setContentTitle(context.getString(C0769R.string.privacy_policy_update_notification_title)).setContentText(context.getString(C0769R.string.privacy_policy_update_notification_action)).setStyle(new BigTextStyle().bigText(context.getString(C0769R.string.privacy_policy_update_notification_action))).setContentIntent(PendingIntent.getBroadcast(context, 5, IntentUtils.genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(context), 1073741824)));
        NewFeatureNotice.getInstance(context).setPrivacyPolicyUpdateNoticeDidShow();
    }

    public static Dialog showSpotlight(Activity activity, View view, OnCancelListener onCancelListener, int i) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(activity).inflate(C0769R.layout.spotlight, null);
        ((TextView) viewGroup.findViewById(C0427R.C0426id.spotlight_message)).setText(i);
        Dialog createSpotlightDialog = createSpotlightDialog(activity, view, viewGroup);
        createSpotlightDialog.setOnCancelListener(onCancelListener);
        createSpotlightDialog.show();
        return createSpotlightDialog;
    }

    public static Dialog showMyShotOnBoarding(Activity activity, View view, OnCancelListener onCancelListener, OnClickListener onClickListener) {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(activity).inflate(C0769R.layout.myshot_onboarding, null);
        viewGroup.findViewById(C0427R.C0426id.my_shot_category_learn_more).setOnClickListener(onClickListener);
        Dialog createSpotlightDialog = createSpotlightDialog(activity, view, viewGroup);
        createSpotlightDialog.setOnCancelListener(onCancelListener);
        createSpotlightDialog.show();
        return createSpotlightDialog;
    }

    public static Dialog createSpotlightDialog(Activity activity, View view, ViewGroup viewGroup) {
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        viewGroup.addView(new FocusView(activity, iArr[0] + (view.getMeasuredWidth() / 2), iArr[1] + (view.getMeasuredHeight() / 2), activity.getResources().getDimensionPixelSize(C0769R.dimen.myshot_focus_view_radius)), 0);
        View findViewById = viewGroup.findViewById(C0427R.C0426id.spotlight_mock_menu);
        LayoutParams layoutParams = (LayoutParams) findViewById.getLayoutParams();
        layoutParams.width = view.getMeasuredWidth();
        layoutParams.height = view.getMeasuredHeight();
        layoutParams.setMargins(iArr[0], iArr[1] - ViewUtils.getStatusBarHeight(activity), 0, 0);
        Builder builder = new Builder(activity, C0769R.style.TabTrayTheme);
        builder.setView(viewGroup);
        AlertDialog create = builder.create();
        findViewById.setOnClickListener(new C0530-$$Lambda$DialogUtils$RwMJMXy3qh63FhjremxtVkmjptk(create, view));
        findViewById.setOnLongClickListener(new C0529-$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0(create, view));
        viewGroup.setOnClickListener(new C0534-$$Lambda$DialogUtils$m49kUVkm4vvAyuBKdJaFTFqzps0(create));
        return create;
    }

    static /* synthetic */ void lambda$createSpotlightDialog$7(Dialog dialog, View view, View view2) {
        dialog.dismiss();
        view.performClick();
    }
}
