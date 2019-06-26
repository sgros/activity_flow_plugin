// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.voip;

import android.provider.Settings$System;
import android.widget.Toast;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import android.text.TextUtils;
import java.util.ArrayList;
import android.os.Parcelable;
import org.telegram.ui.LaunchActivity;
import android.widget.EditText;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Components.BetterRatingView;
import android.os.Build$VERSION;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import android.view.View$OnClickListener;
import org.telegram.ui.Cells.TextCheckCell;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.annotation.TargetApi;
import android.content.DialogInterface$OnDismissListener;
import android.net.Uri;
import org.telegram.ui.VoIPActivity;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.ApplicationLoader;
import java.io.File;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.FileLog;
import android.content.Context;
import android.content.Intent;
import org.telegram.messenger.voip.VoIPService;
import java.util.Iterator;
import java.util.Collections;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import android.app.Activity;
import org.telegram.tgnet.TLRPC;

public class VoIPHelper
{
    private static final int VOIP_SUPPORT_ID = 4244000;
    public static long lastCallTime;
    
    public static boolean canRateCall(final TLRPC.TL_messageActionPhoneCall tl_messageActionPhoneCall) {
        final TLRPC.PhoneCallDiscardReason reason = tl_messageActionPhoneCall.reason;
        if (!(reason instanceof TLRPC.TL_phoneCallDiscardReasonBusy) && !(reason instanceof TLRPC.TL_phoneCallDiscardReasonMissed)) {
            final Iterator<String> iterator = (Iterator<String>)MessagesController.getNotificationsSettings(UserConfig.selectedAccount).getStringSet("calls_access_hashes", Collections.EMPTY_SET).iterator();
            while (iterator.hasNext()) {
                final String[] split = iterator.next().split(" ");
                if (split.length < 2) {
                    continue;
                }
                final String s = split[0];
                final StringBuilder sb = new StringBuilder();
                sb.append(tl_messageActionPhoneCall.call_id);
                sb.append("");
                if (s.equals(sb.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static void doInitiateCall(final TLRPC.User user, final Activity activity) {
        if (activity != null) {
            if (user != null) {
                if (System.currentTimeMillis() - VoIPHelper.lastCallTime < 2000L) {
                    return;
                }
                VoIPHelper.lastCallTime = System.currentTimeMillis();
                final Intent intent = new Intent((Context)activity, (Class)VoIPService.class);
                intent.putExtra("user_id", user.id);
                intent.putExtra("is_outgoing", true);
                intent.putExtra("start_incall_activity", true);
                intent.putExtra("account", UserConfig.selectedAccount);
                try {
                    activity.startService(intent);
                }
                catch (Throwable t) {
                    FileLog.e(t);
                }
            }
        }
    }
    
    public static int getDataSavingDefault() {
        final boolean lessCallData = DownloadController.getInstance(0).lowPreset.lessCallData;
        final boolean lessCallData2 = DownloadController.getInstance(0).mediumPreset.lessCallData;
        final boolean lessCallData3 = DownloadController.getInstance(0).highPreset.lessCallData;
        if (!lessCallData && !lessCallData2 && !lessCallData3) {
            return 0;
        }
        if (lessCallData && !lessCallData2 && !lessCallData3) {
            return 3;
        }
        if (lessCallData && lessCallData2 && !lessCallData3) {
            return 1;
        }
        if (lessCallData && lessCallData2 && lessCallData3) {
            return 2;
        }
        if (BuildVars.LOGS_ENABLED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid call data saving preset configuration: ");
            sb.append(lessCallData);
            sb.append("/");
            sb.append(lessCallData2);
            sb.append("/");
            sb.append(lessCallData3);
            FileLog.w(sb.toString());
        }
        return 0;
    }
    
    private static File getLogFile(final long n) {
        if (BuildVars.DEBUG_VERSION) {
            final File parent = new File(ApplicationLoader.applicationContext.getExternalFilesDir((String)null), "logs");
            final String[] list = parent.list();
            if (list != null) {
                for (final String child : list) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("voip");
                    sb.append(n);
                    sb.append(".txt");
                    if (child.endsWith(sb.toString())) {
                        return new File(parent, child);
                    }
                }
            }
        }
        final File logsDir = getLogsDir();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(n);
        sb2.append(".log");
        return new File(logsDir, sb2.toString());
    }
    
    public static File getLogsDir() {
        final File file = new File(ApplicationLoader.applicationContext.getCacheDir(), "voip_logs");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
    
    private static void initiateCall(final TLRPC.User user, final Activity activity) {
        if (activity != null) {
            if (user != null) {
                if (VoIPService.getSharedInstance() != null) {
                    final TLRPC.User user2 = VoIPService.getSharedInstance().getUser();
                    if (user2.id != user.id) {
                        new AlertDialog.Builder((Context)activity).setTitle(LocaleController.getString("VoipOngoingAlertTitle", 2131561082)).setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("VoipOngoingAlert", 2131561081, ContactsController.formatName(user2.first_name, user2.last_name), ContactsController.formatName(user.first_name, user.last_name)))).setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                if (VoIPService.getSharedInstance() != null) {
                                    VoIPService.getSharedInstance().hangUp(new Runnable() {
                                        @Override
                                        public void run() {
                                            final DialogInterface$OnClickListener this$0 = (DialogInterface$OnClickListener)DialogInterface$OnClickListener.this;
                                            doInitiateCall(user, activity);
                                        }
                                    });
                                }
                                else {
                                    doInitiateCall(user, activity);
                                }
                            }
                        }).setNegativeButton(LocaleController.getString("Cancel", 2131558891), null).show();
                    }
                    else {
                        activity.startActivity(new Intent((Context)activity, (Class)VoIPActivity.class).addFlags(268435456));
                    }
                }
                else if (VoIPService.callIShouldHavePutIntoIntent == null) {
                    doInitiateCall(user, activity);
                }
            }
        }
    }
    
    @TargetApi(23)
    public static void permissionDenied(final Activity activity, final Runnable runnable) {
        if (!activity.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
            new AlertDialog.Builder((Context)activity).setTitle(LocaleController.getString("AppName", 2131558635)).setMessage(LocaleController.getString("VoipNeedMicPermission", 2131561074)).setPositiveButton(LocaleController.getString("OK", 2131560097), null).setNegativeButton(LocaleController.getString("Settings", 2131560738), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    final Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", activity.getPackageName(), (String)null));
                    activity.startActivity(intent);
                }
            }).show().setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
                public void onDismiss(final DialogInterface dialogInterface) {
                    final Runnable val$onFinish = runnable;
                    if (val$onFinish != null) {
                        val$onFinish.run();
                    }
                }
            });
        }
    }
    
    public static void showCallDebugSettings(final Context context) {
        final SharedPreferences globalMainSettings = MessagesController.getGlobalMainSettings();
        final LinearLayout view = new LinearLayout(context);
        view.setOrientation(1);
        final TextView textView = new TextView(context);
        textView.setTextSize(1, 15.0f);
        textView.setText((CharSequence)"Please only change these settings if you know exactly what they do.");
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 16.0f, 8.0f, 16.0f, 8.0f));
        final TextCheckCell textCheckCell = new TextCheckCell(context);
        textCheckCell.setTextAndCheck("Force TCP", globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false), false);
        textCheckCell.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final boolean boolean1 = globalMainSettings.getBoolean("dbg_force_tcp_in_calls", false);
                final SharedPreferences$Editor edit = globalMainSettings.edit();
                edit.putBoolean("dbg_force_tcp_in_calls", boolean1 ^ true);
                edit.commit();
                textCheckCell.setChecked(boolean1 ^ true);
            }
        });
        view.addView((View)textCheckCell);
        if (BuildVars.DEBUG_VERSION && BuildVars.LOGS_ENABLED) {
            final TextCheckCell textCheckCell2 = new TextCheckCell(context);
            textCheckCell2.setTextAndCheck("Dump detailed stats", globalMainSettings.getBoolean("dbg_dump_call_stats", false), false);
            textCheckCell2.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final boolean boolean1 = globalMainSettings.getBoolean("dbg_dump_call_stats", false);
                    final SharedPreferences$Editor edit = globalMainSettings.edit();
                    edit.putBoolean("dbg_dump_call_stats", boolean1 ^ true);
                    edit.commit();
                    textCheckCell2.setChecked(boolean1 ^ true);
                }
            });
            view.addView((View)textCheckCell2);
        }
        if (Build$VERSION.SDK_INT >= 26) {
            final TextCheckCell textCheckCell3 = new TextCheckCell(context);
            textCheckCell3.setTextAndCheck("Enable ConnectionService", globalMainSettings.getBoolean("dbg_force_connection_service", false), false);
            textCheckCell3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final boolean boolean1 = globalMainSettings.getBoolean("dbg_force_connection_service", false);
                    final SharedPreferences$Editor edit = globalMainSettings.edit();
                    edit.putBoolean("dbg_force_connection_service", boolean1 ^ true);
                    edit.commit();
                    textCheckCell3.setChecked(boolean1 ^ true);
                }
            });
            view.addView((View)textCheckCell3);
        }
        new AlertDialog.Builder(context).setTitle(LocaleController.getString("DebugMenuCallSettings", 2131559210)).setView((View)view).show();
    }
    
    public static void showRateAlert(final Context context, final Runnable runnable, final long n, final long n2, final int n3, final boolean b) {
        final File logFile = getLogFile(n);
        final LinearLayout view = new LinearLayout(context);
        view.setOrientation(1);
        final int dp = AndroidUtilities.dp(16.0f);
        view.setPadding(dp, dp, dp, 0);
        final TextView textView = new TextView(context);
        textView.setTextSize(2, 16.0f);
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setGravity(17);
        textView.setText((CharSequence)LocaleController.getString("VoipRateCallAlert", 2131561088));
        view.addView((View)textView);
        final BetterRatingView betterRatingView = new BetterRatingView(context);
        view.addView((View)betterRatingView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, 1, 0, 16, 0, 0));
        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        final View$OnClickListener onClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final CheckBoxCell checkBoxCell = (CheckBoxCell)view;
                checkBoxCell.setChecked(checkBoxCell.isChecked() ^ true, true);
            }
        };
        final String[] array = { "echo", "noise", "interruptions", "distorted_speech", "silent_local", "silent_remote", "dropped" };
        for (int i = 0; i < array.length; ++i) {
            final CheckBoxCell checkBoxCell = new CheckBoxCell(context, 1);
            checkBoxCell.setClipToPadding(false);
            checkBoxCell.setTag((Object)array[i]);
            CharSequence charSequence = null;
            switch (i) {
                default: {
                    charSequence = null;
                    break;
                }
                case 6: {
                    charSequence = LocaleController.getString("RateCallDropped", 2131560530);
                    break;
                }
                case 5: {
                    charSequence = LocaleController.getString("RateCallSilentRemote", 2131560535);
                    break;
                }
                case 4: {
                    charSequence = LocaleController.getString("RateCallSilentLocal", 2131560534);
                    break;
                }
                case 3: {
                    charSequence = LocaleController.getString("RateCallDistorted", 2131560529);
                    break;
                }
                case 2: {
                    charSequence = LocaleController.getString("RateCallInterruptions", 2131560532);
                    break;
                }
                case 1: {
                    charSequence = LocaleController.getString("RateCallNoise", 2131560533);
                    break;
                }
                case 0: {
                    charSequence = LocaleController.getString("RateCallEcho", 2131560531);
                    break;
                }
            }
            checkBoxCell.setText(charSequence, null, false, false);
            checkBoxCell.setOnClickListener((View$OnClickListener)onClickListener);
            checkBoxCell.setTag((Object)array[i]);
            linearLayout.addView((View)checkBoxCell);
        }
        view.addView((View)linearLayout, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, -8.0f, 0.0f, -8.0f, 0.0f));
        linearLayout.setVisibility(8);
        final EditText editText = new EditText(context);
        editText.setHint((CharSequence)LocaleController.getString("VoipFeedbackCommentHint", 2131561069));
        editText.setInputType(147457);
        editText.setTextColor(Theme.getColor("dialogTextBlack"));
        editText.setHintTextColor(Theme.getColor("dialogTextHint"));
        editText.setBackgroundDrawable(Theme.createEditTextDrawable(context, true));
        editText.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f));
        editText.setTextSize(18.0f);
        editText.setVisibility(8);
        view.addView((View)editText, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, 8.0f, 8.0f, 8.0f, 0.0f));
        final boolean[] array2 = { true };
        final CheckBoxCell checkBoxCell2 = new CheckBoxCell(context, 1);
        final View$OnClickListener view$OnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                final boolean[] val$includeLogs = array2;
                val$includeLogs[0] ^= true;
                checkBoxCell2.setChecked(val$includeLogs[0], true);
            }
        };
        checkBoxCell2.setText(LocaleController.getString("CallReportIncludeLogs", 2131558882), null, true, false);
        checkBoxCell2.setClipToPadding(false);
        checkBoxCell2.setOnClickListener((View$OnClickListener)view$OnClickListener);
        view.addView((View)checkBoxCell2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, -2, -8.0f, 0.0f, -8.0f, 0.0f));
        final TextView textView2 = new TextView(context);
        textView2.setTextSize(2, 14.0f);
        textView2.setTextColor(Theme.getColor("dialogTextGray3"));
        textView2.setText((CharSequence)LocaleController.getString("CallReportLogsExplain", 2131558883));
        textView2.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), 0);
        textView2.setOnClickListener((View$OnClickListener)view$OnClickListener);
        view.addView((View)textView2);
        checkBoxCell2.setVisibility(8);
        textView2.setVisibility(8);
        if (!logFile.exists()) {
            array2[0] = false;
        }
        final AlertDialog create = new AlertDialog.Builder(context).setTitle(LocaleController.getString("CallMessageReportProblem", 2131558878)).setView((View)view).setPositiveButton(LocaleController.getString("Send", 2131560685), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        }).setNegativeButton(LocaleController.getString("Cancel", 2131558891), null).setOnDismissListener((DialogInterface$OnDismissListener)new DialogInterface$OnDismissListener() {
            public void onDismiss(final DialogInterface dialogInterface) {
                final Runnable val$onDismiss = runnable;
                if (val$onDismiss != null) {
                    val$onDismiss.run();
                }
            }
        }).create();
        if (BuildVars.DEBUG_VERSION && logFile.exists()) {
            create.setNeutralButton("Send log", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                public void onClick(final DialogInterface dialogInterface, final int n) {
                    final Intent intent = new Intent(context, (Class)LaunchActivity.class);
                    intent.setAction("android.intent.action.SEND");
                    intent.putExtra("android.intent.extra.STREAM", (Parcelable)Uri.fromFile(logFile));
                    context.startActivity(intent);
                }
            });
        }
        create.show();
        create.getWindow().setSoftInputMode(3);
        final View button = create.getButton(-1);
        button.setEnabled(false);
        betterRatingView.setOnRatingChangeListener((BetterRatingView.OnRatingChangeListener)new BetterRatingView.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(int n) {
                button.setEnabled(n > 0);
                final TextView textView = (TextView)button;
                String s;
                if (n < 4) {
                    n = 2131559911;
                    s = "Next";
                }
                else {
                    n = 2131560685;
                    s = "Send";
                }
                textView.setText((CharSequence)LocaleController.getString(s, n).toUpperCase());
            }
        });
        button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            final /* synthetic */ int[] val$page = { 0 };
            
            public void onClick(final View view) {
                if (betterRatingView.getRating() < 4) {
                    final int[] val$page = this.val$page;
                    if (val$page[0] != 1) {
                        val$page[0] = 1;
                        betterRatingView.setVisibility(8);
                        textView.setVisibility(8);
                        create.setTitle(LocaleController.getString("CallReportHint", 2131558881));
                        editText.setVisibility(0);
                        if (logFile.exists()) {
                            checkBoxCell2.setVisibility(0);
                            textView2.setVisibility(0);
                        }
                        linearLayout.setVisibility(0);
                        ((TextView)button).setText((CharSequence)LocaleController.getString("Send", 2131560685).toUpperCase());
                        return;
                    }
                }
                final int selectedAccount = UserConfig.selectedAccount;
                final TLRPC.TL_phone_setCallRating tl_phone_setCallRating = new TLRPC.TL_phone_setCallRating();
                tl_phone_setCallRating.rating = betterRatingView.getRating();
                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < linearLayout.getChildCount(); ++i) {
                    final CheckBoxCell checkBoxCell = (CheckBoxCell)linearLayout.getChildAt(i);
                    if (checkBoxCell.isChecked()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("#");
                        sb.append(checkBoxCell.getTag());
                        list.add(sb.toString());
                    }
                }
                if (tl_phone_setCallRating.rating < 5) {
                    tl_phone_setCallRating.comment = editText.getText().toString();
                }
                else {
                    tl_phone_setCallRating.comment = "";
                }
                if (!list.isEmpty() && !array2[0]) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(tl_phone_setCallRating.comment);
                    sb2.append(" ");
                    sb2.append(TextUtils.join((CharSequence)" ", (Iterable)list));
                    tl_phone_setCallRating.comment = sb2.toString();
                }
                tl_phone_setCallRating.peer = new TLRPC.TL_inputPhoneCall();
                final TLRPC.TL_inputPhoneCall peer = tl_phone_setCallRating.peer;
                peer.access_hash = n2;
                peer.id = n;
                tl_phone_setCallRating.user_initiative = b;
                ConnectionsManager.getInstance(n3).sendRequest(tl_phone_setCallRating, new RequestDelegate() {
                    @Override
                    public void run(final TLObject tlObject, final TLRPC.TL_error tl_error) {
                        if (tlObject instanceof TLRPC.TL_updates) {
                            MessagesController.getInstance(selectedAccount).processUpdates((TLRPC.Updates)tlObject, false);
                        }
                        final View$OnClickListener this$0 = (View$OnClickListener)View$OnClickListener.this;
                        if (array2[0] && logFile.exists() && tl_phone_setCallRating.rating < 4) {
                            SendMessagesHelper.prepareSendingDocument(logFile.getAbsolutePath(), logFile.getAbsolutePath(), null, TextUtils.join((CharSequence)" ", (Iterable)list), "text/plain", 4244000L, null, null, null);
                            Toast.makeText(context, (CharSequence)LocaleController.getString("CallReportSent", 2131558884), 1).show();
                        }
                    }
                });
                create.dismiss();
            }
        });
    }
    
    public static void showRateAlert(final Context context, final TLRPC.TL_messageActionPhoneCall tl_messageActionPhoneCall) {
        final Iterator<String> iterator = (Iterator<String>)MessagesController.getNotificationsSettings(UserConfig.selectedAccount).getStringSet("calls_access_hashes", Collections.EMPTY_SET).iterator();
        String[] split = null;
        Block_3: {
            while (iterator.hasNext()) {
                split = iterator.next().split(" ");
                if (split.length < 2) {
                    continue;
                }
                final String s = split[0];
                final StringBuilder sb = new StringBuilder();
                sb.append(tl_messageActionPhoneCall.call_id);
                sb.append("");
                if (s.equals(sb.toString())) {
                    break Block_3;
                }
            }
            return;
        }
        try {
            showRateAlert(context, null, tl_messageActionPhoneCall.call_id, Long.parseLong(split[1]), UserConfig.selectedAccount, true);
        }
        catch (Exception ex) {}
    }
    
    public static void startCall(final TLRPC.User user, final Activity activity, final TLRPC.UserFull userFull) {
        boolean b = true;
        if (userFull != null && userFull.phone_calls_private) {
            new AlertDialog.Builder((Context)activity).setTitle(LocaleController.getString("VoipFailed", 2131561068)).setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("CallNotAvailable", 2131558880, ContactsController.formatName(user.first_name, user.last_name)))).setPositiveButton(LocaleController.getString("OK", 2131560097), null).show();
            return;
        }
        if (ConnectionsManager.getInstance(UserConfig.selectedAccount).getConnectionState() != 3) {
            if (Settings$System.getInt(activity.getContentResolver(), "airplane_mode_on", 0) == 0) {
                b = false;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
            int n;
            String s;
            if (b) {
                n = 2131561078;
                s = "VoipOfflineAirplaneTitle";
            }
            else {
                n = 2131561080;
                s = "VoipOfflineTitle";
            }
            final AlertDialog.Builder setTitle = builder.setTitle(LocaleController.getString(s, n));
            int n2;
            String s2;
            if (b) {
                n2 = 2131561077;
                s2 = "VoipOfflineAirplane";
            }
            else {
                n2 = 2131561076;
                s2 = "VoipOffline";
            }
            final AlertDialog.Builder setPositiveButton = setTitle.setMessage(LocaleController.getString(s2, n2)).setPositiveButton(LocaleController.getString("OK", 2131560097), null);
            if (b) {
                final Intent intent = new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    setPositiveButton.setNeutralButton(LocaleController.getString("VoipOfflineOpenSettings", 2131561079), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            activity.startActivity(intent);
                        }
                    });
                }
            }
            setPositiveButton.show();
            return;
        }
        if (Build$VERSION.SDK_INT >= 23 && activity.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            activity.requestPermissions(new String[] { "android.permission.RECORD_AUDIO" }, 101);
        }
        else {
            initiateCall(user, activity);
        }
    }
}
