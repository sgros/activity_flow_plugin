package org.mozilla.focus.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.activity.SettingsActivity;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.widget.FocusView;

public class DialogUtils {
   public static Dialog createSpotlightDialog(Activity var0, View var1, ViewGroup var2) {
      int[] var3 = new int[2];
      var1.getLocationInWindow(var3);
      var2.addView(new FocusView(var0, var3[0] + var1.getMeasuredWidth() / 2, var3[1] + var1.getMeasuredHeight() / 2, var0.getResources().getDimensionPixelSize(2131165420)), 0);
      View var4 = var2.findViewById(2131296660);
      LayoutParams var5 = (LayoutParams)var4.getLayoutParams();
      var5.width = var1.getMeasuredWidth();
      var5.height = var1.getMeasuredHeight();
      var5.setMargins(var3[0], var3[1] - ViewUtils.getStatusBarHeight(var0), 0, 0);
      AlertDialog.Builder var6 = new AlertDialog.Builder(var0, 2131820832);
      var6.setView(var2);
      AlertDialog var7 = var6.create();
      var4.setOnClickListener(new _$$Lambda$DialogUtils$RwMJMXy3qh63FhjremxtVkmjptk(var7, var1));
      var4.setOnLongClickListener(new _$$Lambda$DialogUtils$MkweB_OU2aR2wuLefGHxJ6Ab9o0(var7, var1));
      var2.setOnClickListener(new _$$Lambda$DialogUtils$m49kUVkm4vvAyuBKdJaFTFqzps0(var7));
      return var7;
   }

   // $FF: synthetic method
   static void lambda$createSpotlightDialog$7(Dialog var0, View var1, View var2) {
      var0.dismiss();
      var1.performClick();
   }

   // $FF: synthetic method
   static boolean lambda$createSpotlightDialog$8(Dialog var0, View var1, View var2) {
      var0.dismiss();
      return var1.performLongClick();
   }

   // $FF: synthetic method
   static void lambda$createSpotlightDialog$9(Dialog var0, View var1) {
      var0.dismiss();
   }

   // $FF: synthetic method
   static void lambda$showRateAppDialog$0(Context var0, DialogInterface var1) {
      Settings.getInstance(var0).setRateAppDialogDidDismiss();
      telemetryFeedback(var0, "dismiss");
   }

   // $FF: synthetic method
   static void lambda$showRateAppDialog$1(Context var0, AlertDialog var1, View var2) {
      Settings.getInstance(var0).setRateAppDialogDidDismiss();
      var1.dismiss();
      telemetryFeedback(var0, "dismiss");
   }

   // $FF: synthetic method
   static void lambda$showRateAppDialog$2(Context var0, AlertDialog var1, View var2) {
      IntentUtils.goToPlayStore(var0);
      var1.dismiss();
      telemetryFeedback(var0, "positive");
   }

   // $FF: synthetic method
   static void lambda$showRateAppDialog$3(Context var0, AlertDialog var1, View var2) {
      Settings.getInstance(var0).setShareAppDialogDidShow();
      IntentUtils.openUrl(var0, var0.getString(2131755368), true);
      var1.dismiss();
      telemetryFeedback(var0, "negative");
   }

   // $FF: synthetic method
   static void lambda$showShareAppDialog$4(Context var0, DialogInterface var1) {
      telemetryShareApp(var0, "dismiss");
   }

   // $FF: synthetic method
   static void lambda$showShareAppDialog$5(AlertDialog var0, Context var1, View var2) {
      var0.dismiss();
      telemetryShareApp(var1, "dismiss");
   }

   // $FF: synthetic method
   static void lambda$showShareAppDialog$6(Context var0, AlertDialog var1, View var2) {
      Intent var3 = new Intent("android.intent.action.SEND");
      var3.setType("text/plain");
      var3.putExtra("android.intent.extra.SUBJECT", var0.getString(2131755062));
      var3.putExtra("android.intent.extra.TEXT", AppConfigWrapper.getShareAppMessage(var0));
      var0.startActivity(Intent.createChooser(var3, (CharSequence)null));
      var1.dismiss();
      telemetryShareApp(var0, "share");
   }

   public static void showDefaultSettingNotification(Context var0) {
      showDefaultSettingNotification(var0, (String)null);
   }

   public static void showDefaultSettingNotification(Context var0, String var1) {
      PendingIntent var2 = PendingIntent.getBroadcast(var0, 4, IntentUtils.genDefaultBrowserSettingIntentForBroadcastReceiver(var0), 1073741824);
      String var3 = var1;
      if (TextUtils.isEmpty(var1)) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var0.getString(2131755343));
         var4.append("?\ud83d\ude0a");
         var3 = var4.toString();
      }

      NotificationUtil.sendNotification(var0, 1002, NotificationUtil.importantBuilder(var0).setContentTitle(var3).setContentIntent(var2));
      Settings.getInstance(var0).setDefaultBrowserSettingDidShow();
   }

   public static Dialog showMyShotOnBoarding(Activity var0, View var1, OnCancelListener var2, OnClickListener var3) {
      ViewGroup var4 = (ViewGroup)LayoutInflater.from(var0).inflate(2131492995, (ViewGroup)null);
      var4.findViewById(2131296531).setOnClickListener(var3);
      Dialog var5 = createSpotlightDialog(var0, var1, var4);
      var5.setOnCancelListener(var2);
      var5.show();
      return var5;
   }

   public static void showPrivacyPolicyUpdateNotification(Context var0) {
      PendingIntent var1 = PendingIntent.getBroadcast(var0, 5, IntentUtils.genPrivacyPolicyUpdateNotificationActionForBroadcastReceiver(var0), 1073741824);
      NotificationUtil.sendNotification(var0, 1003, NotificationUtil.importantBuilder(var0).setContentTitle(var0.getString(2131755358)).setContentText(var0.getString(2131755357)).setStyle((new NotificationCompat.BigTextStyle()).bigText(var0.getString(2131755357))).setContentIntent(var1));
      NewFeatureNotice.getInstance(var0).setPrivacyPolicyUpdateNoticeDidShow();
   }

   public static void showRateAppDialog(Context var0) {
      if (var0 != null) {
         AlertDialog var1 = (new AlertDialog.Builder(var0)).create();
         var1.setOnCancelListener(new _$$Lambda$DialogUtils$6IBYylLlOhED1uN7iohSoMlVcog(var0));
         View var2 = LayoutInflater.from(var0).inflate(2131492988, (ViewGroup)null);
         ((TextView)var2.findViewById(2131296584)).setText(var0.getString(2131755367, new Object[]{var0.getString(2131755062)}));
         var2.findViewById(2131296402).setOnClickListener(new _$$Lambda$DialogUtils$_X7YflwqscrGGtHVTw1w7wtGJB4(var0, var1));
         TextView var3 = (TextView)var2.findViewById(2131296404);
         String var4 = AppConfigWrapper.getRateAppPositiveString(var0);
         if (!TextUtils.isEmpty(var4)) {
            var3.setText(var4);
         }

         var3.setOnClickListener(new _$$Lambda$DialogUtils$wEBYFVSzBa1L5mrkhE3W7pluRxY(var0, var1));
         String var5 = AppConfigWrapper.getRateAppDialogTitle(var0);
         if (!TextUtils.isEmpty(var5)) {
            ((TextView)var2.findViewById(2131296584)).setText(var5);
         }

         var5 = AppConfigWrapper.getRateAppDialogContent(var0);
         if (!TextUtils.isEmpty(var5)) {
            ((TextView)var2.findViewById(2131296583)).setText(var5);
         }

         var3 = (TextView)var2.findViewById(2131296403);
         var4 = AppConfigWrapper.getRateAppNegativeString(var0);
         if (!TextUtils.isEmpty(var4)) {
            var3.setText(var4);
         }

         var3.setOnClickListener(new _$$Lambda$DialogUtils$crSW_TsUi_bE8SAwjVeDaII7Xlc(var0, var1));
         var1.setView(var2);
         var1.setCanceledOnTouchOutside(true);
         var1.show();
         Settings.getInstance(var0).setRateAppDialogDidShow();
      }
   }

   public static void showRateAppNotification(Context var0) {
      PendingIntent var1 = PendingIntent.getBroadcast(var0, 1, IntentUtils.genFeedbackNotificationClickForBroadcastReceiver(var0), 1073741824);
      StringBuilder var2 = new StringBuilder();
      var2.append(var0.getString(2131755367, new Object[]{var0.getString(2131755062)}));
      var2.append("\ud83d\ude00");
      String var4 = var2.toString();
      NotificationCompat.Builder var3 = NotificationUtil.importantBuilder(var0).setContentText(var4).setContentIntent(var1);
      PendingIntent var5 = PendingIntent.getBroadcast(var0, 2, IntentUtils.genRateStarNotificationActionForBroadcastReceiver(var0), 1073741824);
      var3.addAction(2131230944, var0.getString(2131755370), var5);
      var5 = PendingIntent.getBroadcast(var0, 3, IntentUtils.genFeedbackNotificationActionForBroadcastReceiver(var0), 1073741824);
      var3.addAction(2131230942, var0.getString(2131755369), var5);
      NotificationUtil.sendNotification(var0, 1001, var3);
      Settings.getInstance(var0).setRateAppNotificationDidShow();
   }

   public static void showShareAppDialog(Context var0) {
      if (var0 != null) {
         AlertDialog var1 = (new AlertDialog.Builder(var0)).create();
         var1.setOnCancelListener(new _$$Lambda$DialogUtils$5HFyet6oHYCHE46_DyDOrH6PVuc(var0));
         View var2 = LayoutInflater.from(var0).inflate(2131492989, (ViewGroup)null);
         ((TextView)var2.findViewById(2131296640)).setText(AppConfigWrapper.getShareAppDialogTitle(var0));
         ((TextView)var2.findViewById(2131296639)).setText(AppConfigWrapper.getShareAppDialogContent(var0));
         var2.findViewById(2131296405).setOnClickListener(new _$$Lambda$DialogUtils$28dtCamIzRB4NQvWqNCsRNPQFa4(var1, var0));
         var2.findViewById(2131296406).setOnClickListener(new _$$Lambda$DialogUtils$T1sxZWz76nkX0mgkiZoiYiKYJP4(var0, var1));
         var1.setView(var2);
         var1.setCanceledOnTouchOutside(true);
         var1.show();
         Settings.getInstance(var0).setShareAppDialogDidShow();
      }
   }

   public static Dialog showSpotlight(Activity var0, View var1, OnCancelListener var2, int var3) {
      ViewGroup var4 = (ViewGroup)LayoutInflater.from(var0).inflate(2131493018, (ViewGroup)null);
      ((TextView)var4.findViewById(2131296659)).setText(var3);
      Dialog var5 = createSpotlightDialog(var0, var1, var4);
      var5.setOnCancelListener(var2);
      var5.show();
      return var5;
   }

   private static void telemetryFeedback(Context var0, String var1) {
      if (var0 instanceof MainActivity) {
         TelemetryWrapper.clickRateApp(var1, "contextual_hints");
      } else if (var0 instanceof SettingsActivity) {
         TelemetryWrapper.clickRateApp(var1, "settings");
      }

   }

   private static void telemetryShareApp(Context var0, String var1) {
      if (var0 instanceof MainActivity) {
         TelemetryWrapper.promoteShareClickEvent(var1, "contextual_hints");
      } else if (var0 instanceof SettingsActivity) {
         TelemetryWrapper.promoteShareClickEvent(var1, "settings");
      }

   }
}
