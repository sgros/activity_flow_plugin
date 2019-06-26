// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.text.Spannable;
import org.telegram.messenger.ApplicationLoader;
import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuff$Mode;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.NotificationsSettingsActivity;
import org.telegram.ui.NotificationsCustomSettingsActivity;
import org.telegram.ui.ProfileNotificationsActivity;
import org.telegram.ui.ChatActivity;
import org.telegram.messenger.FileLog;
import android.util.Base64;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.SerializedData;
import org.telegram.messenger.SecretChatHelper;
import android.widget.Toast;
import org.telegram.ui.ReportOtherActivity;
import android.os.Bundle;
import org.telegram.messenger.NotificationsController;
import org.telegram.ui.LanguageSelectActivity;
import org.telegram.ui.CacheControlActivity;
import java.util.ArrayList;
import android.content.DialogInterface$OnCancelListener;
import java.util.Locale;
import android.content.SharedPreferences$Editor;
import android.content.DialogInterface;
import android.content.DialogInterface$OnDismissListener;
import org.telegram.messenger.Utilities;
import android.text.style.URLSpan;
import android.text.SpannableString;
import android.text.Html;
import org.telegram.ui.ActionBar.BottomSheet;
import android.text.method.MovementMethod;
import android.text.SpannableStringBuilder;
import org.telegram.ui.LaunchActivity;
import org.telegram.messenger.ContactsController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import android.util.SparseArray;
import org.telegram.messenger.MessageObject;
import android.content.SharedPreferences;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.RadioColorCell;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.MessagesController;
import android.text.TextUtils$TruncateAt;
import android.widget.FrameLayout;
import android.text.TextUtils;
import org.telegram.messenger.ChatObject;
import android.widget.TextView;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Cells.AccountSelectCell;
import android.widget.LinearLayout;
import android.content.Context;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.AlertDialog;
import android.app.Activity;
import java.util.Calendar;

public class AlertsCreator
{
    private static void checkPickerDate(final NumberPicker numberPicker, final NumberPicker numberPicker2, final NumberPicker numberPicker3) {
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        final int value = instance.get(1);
        final int value2 = instance.get(2);
        final int value3 = instance.get(5);
        if (value > numberPicker3.getValue()) {
            numberPicker3.setValue(value);
        }
        if (numberPicker3.getValue() == value) {
            if (value2 > numberPicker2.getValue()) {
                numberPicker2.setValue(value2);
            }
            if (value2 == numberPicker2.getValue() && value3 > numberPicker.getValue()) {
                numberPicker.setValue(value3);
            }
        }
    }
    
    public static AlertDialog createAccountSelectDialog(final Activity activity, final AccountSelectDelegate accountSelectDelegate) {
        if (UserConfig.getActivatedAccountsCount() < 2) {
            return null;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        final Runnable dismissRunnable = builder.getDismissRunnable();
        final AlertDialog[] array = { null };
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        for (int i = 0; i < 3; ++i) {
            if (UserConfig.getInstance(i).getCurrentUser() != null) {
                final AccountSelectCell accountSelectCell = new AccountSelectCell((Context)activity);
                accountSelectCell.setAccount(i, false);
                accountSelectCell.setPadding(AndroidUtilities.dp(14.0f), 0, AndroidUtilities.dp(14.0f), 0);
                accountSelectCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                view.addView((View)accountSelectCell, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 50));
                accountSelectCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$ZO55ojXzRG9oFnxZDRUXz_6_1OA(array, dismissRunnable, accountSelectDelegate));
            }
        }
        builder.setTitle(LocaleController.getString("SelectAccount", 2131560676));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
        return array[0] = builder.create();
    }
    
    public static void createClearOrDeleteDialogAlert(final BaseFragment baseFragment, final boolean b, final TLRPC.Chat chat, final TLRPC.User user, final boolean b2, final MessagesStorage.BooleanCallback booleanCallback) {
        createClearOrDeleteDialogAlert(baseFragment, b, false, false, chat, user, b2, booleanCallback);
    }
    
    public static void createClearOrDeleteDialogAlert(final BaseFragment baseFragment, final boolean b, final boolean b2, final boolean b3, final TLRPC.Chat info, final TLRPC.User info2, final boolean b4, final MessagesStorage.BooleanCallback booleanCallback) {
        if (baseFragment != null && baseFragment.getParentActivity() != null) {
            if (info != null || info2 != null) {
                final int currentAccount = baseFragment.getCurrentAccount();
                final Activity parentActivity = baseFragment.getParentActivity();
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)parentActivity);
                final int clientUserId = UserConfig.getInstance(currentAccount).getClientUserId();
                final CheckBoxCell[] array = { null };
                final TextView textView = new TextView((Context)parentActivity);
                textView.setTextColor(Theme.getColor("dialogTextBlack"));
                textView.setTextSize(1, 16.0f);
                int n;
                if (LocaleController.isRTL) {
                    n = 5;
                }
                else {
                    n = 3;
                }
                textView.setGravity(n | 0x30);
                final boolean b5 = ChatObject.isChannel(info) && !TextUtils.isEmpty((CharSequence)info.username);
                final FrameLayout view = new FrameLayout(parentActivity) {
                    protected void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, n2);
                        if (array[0] != null) {
                            this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight() + array[0].getMeasuredHeight() + AndroidUtilities.dp(7.0f));
                        }
                    }
                };
                builder.setView((View)view);
                final AvatarDrawable avatarDrawable = new AvatarDrawable();
                avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
                final BackupImageView backupImageView = new BackupImageView((Context)parentActivity);
                backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
                int n2;
                if (LocaleController.isRTL) {
                    n2 = 5;
                }
                else {
                    n2 = 3;
                }
                view.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n2 | 0x30, 22.0f, 5.0f, 22.0f, 0.0f));
                final TextView textView2 = new TextView((Context)parentActivity);
                textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
                textView2.setTextSize(1, 20.0f);
                textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                textView2.setLines(1);
                textView2.setMaxLines(1);
                textView2.setSingleLine(true);
                int n3;
                if (LocaleController.isRTL) {
                    n3 = 5;
                }
                else {
                    n3 = 3;
                }
                textView2.setGravity(n3 | 0x10);
                textView2.setEllipsize(TextUtils$TruncateAt.END);
                if (b) {
                    if (b5) {
                        textView2.setText((CharSequence)LocaleController.getString("ClearHistoryCache", 2131559108));
                    }
                    else {
                        textView2.setText((CharSequence)LocaleController.getString("ClearHistory", 2131559107));
                    }
                }
                else if (b2) {
                    if (ChatObject.isChannel(info)) {
                        if (info.megagroup) {
                            textView2.setText((CharSequence)LocaleController.getString("DeleteMegaMenu", 2131559249));
                        }
                        else {
                            textView2.setText((CharSequence)LocaleController.getString("ChannelDeleteMenu", 2131558947));
                        }
                    }
                    else {
                        textView2.setText((CharSequence)LocaleController.getString("DeleteMegaMenu", 2131559249));
                    }
                }
                else {
                    textView2.setText((CharSequence)LocaleController.getString("DeleteChatUser", 2131559240));
                }
                int n4;
                if (LocaleController.isRTL) {
                    n4 = 5;
                }
                else {
                    n4 = 3;
                }
                int n5;
                if (LocaleController.isRTL) {
                    n5 = 21;
                }
                else {
                    n5 = 76;
                }
                final float n6 = (float)n5;
                int n7;
                if (LocaleController.isRTL) {
                    n7 = 76;
                }
                else {
                    n7 = 21;
                }
                view.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n4 | 0x30, n6, 11.0f, (float)n7, 0.0f));
                int n8;
                if (LocaleController.isRTL) {
                    n8 = 5;
                }
                else {
                    n8 = 3;
                }
                view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n8 | 0x30, 24.0f, 57.0f, 24.0f, 9.0f));
                final boolean b6 = info2 != null && !info2.bot && info2.id != clientUserId && MessagesController.getInstance(currentAccount).canRevokePmInbox;
                int n9;
                if (info2 != null) {
                    n9 = MessagesController.getInstance(currentAccount).revokeTimePmLimit;
                }
                else {
                    n9 = MessagesController.getInstance(currentAccount).revokeTimeLimit;
                }
                final boolean b7 = !b4 && info2 != null && b6 && n9 == Integer.MAX_VALUE;
                final boolean[] array2 = { false };
                if (!b3 && b7) {
                    (array[0] = new CheckBoxCell((Context)parentActivity, 1)).setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    if (b) {
                        array[0].setText(LocaleController.formatString("ClearHistoryOptionAlso", 2131559109, UserObject.getFirstName(info2)), "", false, false);
                    }
                    else {
                        array[0].setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131559251, UserObject.getFirstName(info2)), "", false, false);
                    }
                    final CheckBoxCell checkBoxCell = array[0];
                    int n10;
                    if (LocaleController.isRTL) {
                        n10 = AndroidUtilities.dp(16.0f);
                    }
                    else {
                        n10 = AndroidUtilities.dp(8.0f);
                    }
                    float n11 = 16.0f;
                    if (LocaleController.isRTL) {
                        n11 = 8.0f;
                    }
                    checkBoxCell.setPadding(n10, 0, AndroidUtilities.dp(n11), 0);
                    view.addView((View)array[0], (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 83, 0.0f, 0.0f, 0.0f, 0.0f));
                    array[0].setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$yLbyuHJw0N3q2V_bASpmXNgLM5I(array2));
                }
                if (info2 != null) {
                    if (info2.id == clientUserId) {
                        avatarDrawable.setAvatarType(2);
                        backupImageView.setImage(null, null, avatarDrawable, info2);
                    }
                    else {
                        avatarDrawable.setInfo(info2);
                        backupImageView.setImage(ImageLocation.getForUser(info2, false), "50_50", avatarDrawable, info2);
                    }
                }
                else if (info != null) {
                    avatarDrawable.setInfo(info);
                    backupImageView.setImage(ImageLocation.getForChat(info, false), "50_50", avatarDrawable, info);
                }
                if (b3) {
                    textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("DeleteAllMessagesAlert", 2131559233)));
                }
                else if (b) {
                    if (info2 != null) {
                        if (b4) {
                            textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithSecretUser", 2131558676, UserObject.getUserName(info2))));
                        }
                        else if (info2.id == clientUserId) {
                            textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureClearHistorySavedMessages", 2131558674)));
                        }
                        else {
                            textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithUser", 2131558677, UserObject.getUserName(info2))));
                        }
                    }
                    else if (info != null) {
                        if (ChatObject.isChannel(info) && (!info.megagroup || !TextUtils.isEmpty((CharSequence)info.username))) {
                            if (info.megagroup) {
                                textView.setText((CharSequence)LocaleController.getString("AreYouSureClearHistoryGroup", 2131558673));
                            }
                            else {
                                textView.setText((CharSequence)LocaleController.getString("AreYouSureClearHistoryChannel", 2131558671));
                            }
                        }
                        else {
                            textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureClearHistoryWithChat", 2131558675, info.title)));
                        }
                    }
                }
                else if (b2) {
                    if (ChatObject.isChannel(info)) {
                        if (info.megagroup) {
                            textView.setText((CharSequence)LocaleController.getString("MegaDeleteAlert", 2131559827));
                        }
                        else {
                            textView.setText((CharSequence)LocaleController.getString("ChannelDeleteAlert", 2131558944));
                        }
                    }
                    else {
                        textView.setText((CharSequence)LocaleController.getString("AreYouSureDeleteAndExit", 2131558678));
                    }
                }
                else if (info2 != null) {
                    if (b4) {
                        textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithSecretUser", 2131558691, UserObject.getUserName(info2))));
                    }
                    else if (info2.id == clientUserId) {
                        textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("AreYouSureDeleteThisChatSavedMessages", 2131558689)));
                    }
                    else {
                        textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteThisChatWithUser", 2131558692, UserObject.getUserName(info2))));
                    }
                }
                else if (ChatObject.isChannel(info)) {
                    if (info.megagroup) {
                        textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("MegaLeaveAlertWithName", 2131559830, info.title)));
                    }
                    else {
                        textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("ChannelLeaveAlertWithName", 2131558958, info.title)));
                    }
                }
                else {
                    textView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureDeleteAndExitName", 2131558679, info.title)));
                }
                String s;
                if (b3) {
                    s = LocaleController.getString("DeleteAll", 2131559231);
                }
                else if (b) {
                    if (b5) {
                        s = LocaleController.getString("ClearHistoryCache", 2131559108);
                    }
                    else {
                        s = LocaleController.getString("ClearHistory", 2131559107);
                    }
                }
                else if (b2) {
                    if (ChatObject.isChannel(info)) {
                        if (info.megagroup) {
                            s = LocaleController.getString("DeleteMega", 2131559248);
                        }
                        else {
                            s = LocaleController.getString("ChannelDelete", 2131558943);
                        }
                    }
                    else {
                        s = LocaleController.getString("DeleteMega", 2131559248);
                    }
                }
                else if (ChatObject.isChannel(info)) {
                    if (info.megagroup) {
                        s = LocaleController.getString("LeaveMegaMenu", 2131559746);
                    }
                    else {
                        s = LocaleController.getString("LeaveChannelMenu", 2131559745);
                    }
                }
                else {
                    s = LocaleController.getString("DeleteChatUser", 2131559240);
                }
                builder.setPositiveButton(s, (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$g2LSO2I4rkrZ9986IhA1PoJ9pZ0(info2, b5, b3, array2, baseFragment, b, b2, info, b4, booleanCallback));
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                final AlertDialog create = builder.create();
                baseFragment.showDialog(create);
                final TextView textView3 = (TextView)create.getButton(-1);
                if (textView3 != null) {
                    textView3.setTextColor(Theme.getColor("dialogTextRed2"));
                }
            }
        }
    }
    
    public static Dialog createColorSelectDialog(final Activity activity, final long n, final int n2, final Runnable runnable) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        int n3;
        if (n != 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("color_");
            sb.append(n);
            if (notificationsSettings.contains(sb.toString())) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("color_");
                sb2.append(n);
                n3 = notificationsSettings.getInt(sb2.toString(), -16776961);
            }
            else if ((int)n < 0) {
                n3 = notificationsSettings.getInt("GroupLed", -16776961);
            }
            else {
                n3 = notificationsSettings.getInt("MessagesLed", -16776961);
            }
        }
        else if (n2 == 1) {
            n3 = notificationsSettings.getInt("MessagesLed", -16776961);
        }
        else if (n2 == 0) {
            n3 = notificationsSettings.getInt("GroupLed", -16776961);
        }
        else {
            n3 = notificationsSettings.getInt("ChannelLed", -16776961);
        }
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        final String string = LocaleController.getString("ColorRed", 2131559127);
        final String string2 = LocaleController.getString("ColorOrange", 2131559125);
        final String string3 = LocaleController.getString("ColorYellow", 2131559133);
        final String string4 = LocaleController.getString("ColorGreen", 2131559124);
        final String string5 = LocaleController.getString("ColorCyan", 2131559122);
        final String string6 = LocaleController.getString("ColorBlue", 2131559121);
        final String string7 = LocaleController.getString("ColorViolet", 2131559131);
        final String string8 = LocaleController.getString("ColorPink", 2131559126);
        final String string9 = LocaleController.getString("ColorWhite", 2131559132);
        final int[] array = { n3 };
        for (int i = 0; i < 9; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            final int[] colors = TextColorCell.colors;
            radioColorCell.setCheckColor(colors[i], colors[i]);
            radioColorCell.setTextAndValue((new String[] { string, string2, string3, string4, string5, string6, string7, string8, string9 })[i], n3 == TextColorCell.colorsToSave[i]);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$EEBJwM4EJMpcSWE5sZNxj3sr1GA(view, array));
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        builder.setTitle(LocaleController.getString("LedColor", 2131559747));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Set", 2131560727), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$Rj9lP7vArvQH1Tiv_8DxDTBh7S8(n, array, n2, runnable));
        builder.setNeutralButton(LocaleController.getString("LedDisabled", 2131559748), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$kEfeT_ztr5ipao3BeuOGHHEiQuA(n, n2, runnable));
        if (n != 0L) {
            builder.setNegativeButton(LocaleController.getString("Default", 2131559225), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$q25wyhHIGGMNBJfPiuQPbQVDd_I(n, runnable));
        }
        return builder.create();
    }
    
    public static AlertDialog.Builder createContactsPermissionDialog(final Activity activity, final MessagesStorage.IntCallback intCallback) {
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        builder.setTopImage(2131165737, Theme.getColor("dialogTopBackground"));
        builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.getString("ContactsPermissionAlert", 2131559150)));
        builder.setPositiveButton(LocaleController.getString("ContactsPermissionAlertContinue", 2131559151), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$mK_3N6UsQGpxwpYn5q6EG9jwH5M(intCallback));
        builder.setNegativeButton(LocaleController.getString("ContactsPermissionAlertNotNow", 2131559152), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE(intCallback));
        return builder;
    }
    
    public static AlertDialog.Builder createDatePickerDialog(final Context context, final int n, final int n2, final int n3, final int value, final int value2, final int value3, final String title, final boolean b, final DatePickerDelegate datePickerDelegate) {
        if (context == null) {
            return null;
        }
        final LinearLayout view = new LinearLayout(context);
        view.setOrientation(0);
        view.setWeightSum(1.0f);
        final NumberPicker numberPicker = new NumberPicker(context);
        final NumberPicker numberPicker2 = new NumberPicker(context);
        final NumberPicker numberPicker3 = new NumberPicker(context);
        view.addView((View)numberPicker2, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker2.setOnScrollListener((NumberPicker.OnScrollListener)new _$$Lambda$AlertsCreator$gs8o89qyKUnWVnMUXk35_sRm5SI(b, numberPicker2, numberPicker, numberPicker3));
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(11);
        view.addView((View)numberPicker, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 0.3f));
        numberPicker.setFormatter((NumberPicker.Formatter)_$$Lambda$AlertsCreator$OhyVjdpGLg_SrRDIQ3KPC_TfuKY.INSTANCE);
        numberPicker.setOnValueChangedListener((NumberPicker.OnValueChangeListener)new _$$Lambda$AlertsCreator$8JhkY0KQHgsrUGNU82e_Gw1etjI(numberPicker2, numberPicker, numberPicker3));
        numberPicker.setOnScrollListener((NumberPicker.OnScrollListener)new _$$Lambda$AlertsCreator$CdjKbi3tjQh9DAk90YIRTCSaBIs(b, numberPicker2, numberPicker, numberPicker3));
        final Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        final int value4 = instance.get(1);
        numberPicker3.setMinValue(value4 + n);
        numberPicker3.setMaxValue(value4 + n2);
        numberPicker3.setValue(value4 + n3);
        view.addView((View)numberPicker3, (ViewGroup$LayoutParams)LayoutHelper.createLinear(0, -2, 0.4f));
        numberPicker3.setOnValueChangedListener((NumberPicker.OnValueChangeListener)new _$$Lambda$AlertsCreator$mo66TgHnBRhv_TioolPkrpTopo0(numberPicker2, numberPicker, numberPicker3));
        numberPicker3.setOnScrollListener((NumberPicker.OnScrollListener)new _$$Lambda$AlertsCreator$X9eAz_vGDgt2Lf0L8benu7NuJlM(b, numberPicker2, numberPicker, numberPicker3));
        updateDayPicker(numberPicker2, numberPicker, numberPicker3);
        if (b) {
            checkPickerDate(numberPicker2, numberPicker, numberPicker3);
        }
        if (value != -1) {
            numberPicker2.setValue(value);
            numberPicker.setValue(value2);
            numberPicker3.setValue(value3);
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Set", 2131560727), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$p9qqpsGxGjsXlhjAywpkomgKWGU(b, numberPicker2, numberPicker, numberPicker3, datePickerDelegate));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder;
    }
    
    public static void createDeleteMessagesAlert(final BaseFragment baseFragment, final TLRPC.User user, final TLRPC.Chat chat, final TLRPC.EncryptedChat encryptedChat, final TLRPC.ChatFull chatFull, final long n, final MessageObject messageObject, final SparseArray<MessageObject>[] array, final MessageObject.GroupedMessages groupedMessages, int n2, final Runnable runnable) {
        if (baseFragment != null) {
            if (user != null || chat != null || encryptedChat != null) {
                final Activity parentActivity = baseFragment.getParentActivity();
                if (parentActivity == null) {
                    return;
                }
                final int currentAccount = baseFragment.getCurrentAccount();
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)parentActivity);
                int size;
                if (groupedMessages != null) {
                    size = groupedMessages.messages.size();
                }
                else if (messageObject != null) {
                    size = 1;
                }
                else {
                    size = array[0].size() + array[1].size();
                }
                final boolean[] array2 = new boolean[3];
                boolean[] array3 = { false };
                final boolean b = user != null && MessagesController.getInstance(currentAccount).canRevokePmInbox;
                int n3;
                if (user != null) {
                    n3 = MessagesController.getInstance(currentAccount).revokeTimePmLimit;
                }
                else {
                    n3 = MessagesController.getInstance(currentAccount).revokeTimeLimit;
                }
                final boolean b2 = encryptedChat == null && user != null && b && n3 == Integer.MAX_VALUE;
                TLRPC.User user4;
                int n12;
                int n13;
                int n14;
                int n15;
                if (chat != null && chat.megagroup) {
                    final boolean canBlockUsers = ChatObject.canBlockUsers(chat);
                    final int currentTime = ConnectionsManager.getInstance(currentAccount).getCurrentTime();
                    boolean[] array4 = null;
                    int n5 = 0;
                    TLRPC.User user2 = null;
                    Label_0683: {
                        if (messageObject == null) {
                            int i = 1;
                            int n4 = -1;
                            while (true) {
                                while (i >= 0) {
                                    int from_id;
                                    for (int j = 0; j < array[i].size(); ++j, n4 = from_id) {
                                        final MessageObject messageObject2 = (MessageObject)array[i].valueAt(j);
                                        if ((from_id = n4) == -1) {
                                            from_id = messageObject2.messageOwner.from_id;
                                        }
                                        if (from_id < 0 || from_id != messageObject2.messageOwner.from_id) {
                                            n4 = -2;
                                            break;
                                        }
                                    }
                                    if (n4 == -2) {
                                        final int k = n4;
                                        array4 = array3;
                                        int l = 1;
                                        n5 = 0;
                                        while (l >= 0) {
                                            int n7;
                                            for (int n6 = 0; n6 < array[l].size(); ++n6, n5 = n7) {
                                                final MessageObject messageObject3 = (MessageObject)array[l].valueAt(n6);
                                                n7 = n5;
                                                if (l == 1) {
                                                    n7 = n5;
                                                    if (messageObject3.isOut()) {
                                                        final TLRPC.Message messageOwner = messageObject3.messageOwner;
                                                        n7 = n5;
                                                        if (messageOwner.action == null) {
                                                            n7 = n5;
                                                            if (currentTime - messageOwner.date <= n3) {
                                                                n7 = n5 + 1;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            --l;
                                        }
                                        if (k != -1) {
                                            user2 = MessagesController.getInstance(currentAccount).getUser(k);
                                            break Label_0683;
                                        }
                                        user2 = null;
                                        break Label_0683;
                                    }
                                    else {
                                        --i;
                                    }
                                }
                                array4 = array3;
                                final int k = n4;
                                continue;
                            }
                        }
                        final TLRPC.MessageAction action = messageObject.messageOwner.action;
                        TLRPC.User user3;
                        if (action != null && !(action instanceof TLRPC.TL_messageActionEmpty) && !(action instanceof TLRPC.TL_messageActionChatDeleteUser) && !(action instanceof TLRPC.TL_messageActionChatJoinedByLink) && !(action instanceof TLRPC.TL_messageActionChatAddUser)) {
                            user3 = null;
                        }
                        else {
                            user3 = MessagesController.getInstance(currentAccount).getUser(messageObject.messageOwner.from_id);
                        }
                        boolean b3 = false;
                        Label_0369: {
                            if (!messageObject.isSendError() && messageObject.getDialogId() == n) {
                                final TLRPC.MessageAction action2 = messageObject.messageOwner.action;
                                if ((action2 == null || action2 instanceof TLRPC.TL_messageActionEmpty) && messageObject.isOut() && currentTime - messageObject.messageOwner.date <= n3) {
                                    b3 = true;
                                    break Label_0369;
                                }
                            }
                            b3 = false;
                        }
                        if (b3) {
                            n5 = 1;
                        }
                        else {
                            n5 = 0;
                        }
                        final boolean[] array5 = array3;
                        user2 = user3;
                        array4 = array5;
                    }
                    if (user2 != null && user2.id != UserConfig.getInstance(currentAccount).getClientUserId()) {
                        if (n2 == 1 && !chat.creator) {
                            final AlertDialog[] array6 = { new AlertDialog((Context)parentActivity, 3) };
                            final TLRPC.TL_channels_getParticipant tl_channels_getParticipant = new TLRPC.TL_channels_getParticipant();
                            tl_channels_getParticipant.channel = MessagesController.getInputChannel(chat);
                            tl_channels_getParticipant.user_id = MessagesController.getInstance(currentAccount).getInputUser(user2);
                            AndroidUtilities.runOnUIThread(new _$$Lambda$AlertsCreator$D1rCqnpmmZ_i7oeXROOeLU5F9Lw(array6, currentAccount, ConnectionsManager.getInstance(currentAccount).sendRequest(tl_channels_getParticipant, new _$$Lambda$AlertsCreator$6XiNqgiL1KSmRW1vwfOi1CneU60(array6, baseFragment, user, chat, encryptedChat, chatFull, n, messageObject, array, groupedMessages, runnable)), baseFragment), 1000L);
                            return;
                        }
                        final FrameLayout view = new FrameLayout((Context)parentActivity);
                        int m = 0;
                        int n8 = 0;
                        while (m < 3) {
                            if ((n2 != 2 && canBlockUsers) || m != 0) {
                                final CheckBoxCell checkBoxCell = new CheckBoxCell((Context)parentActivity, 1);
                                checkBoxCell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                                checkBoxCell.setTag((Object)m);
                                if (m == 0) {
                                    checkBoxCell.setText(LocaleController.getString("DeleteBanUser", 2131559237), "", false, false);
                                }
                                else if (m == 1) {
                                    checkBoxCell.setText(LocaleController.getString("DeleteReportSpam", 2131559258), "", false, false);
                                }
                                else if (m == 2) {
                                    checkBoxCell.setText(LocaleController.formatString("DeleteAllFrom", 2131559232, ContactsController.formatName(user2.first_name, user2.last_name)), "", false, false);
                                }
                                int n9;
                                if (LocaleController.isRTL) {
                                    n9 = AndroidUtilities.dp(16.0f);
                                }
                                else {
                                    n9 = AndroidUtilities.dp(8.0f);
                                }
                                int n10;
                                if (LocaleController.isRTL) {
                                    n10 = AndroidUtilities.dp(8.0f);
                                }
                                else {
                                    n10 = AndroidUtilities.dp(16.0f);
                                }
                                checkBoxCell.setPadding(n9, 0, n10, 0);
                                view.addView((View)checkBoxCell, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, (float)(n8 * 48), 0.0f, 0.0f));
                                checkBoxCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$8lY3m8434m5TxXZClUU2olEbfQc(array2));
                                ++n8;
                            }
                            ++m;
                        }
                        builder.setView((View)view);
                        n2 = 0;
                    }
                    else {
                        final AlertDialog.Builder builder2 = builder;
                        if (n5 > 0) {
                            final FrameLayout view2 = new FrameLayout((Context)parentActivity);
                            final CheckBoxCell checkBoxCell2 = new CheckBoxCell((Context)parentActivity, 1);
                            checkBoxCell2.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            checkBoxCell2.setText(LocaleController.getString("DeleteMessagesOption", 2131559250), "", false, false);
                            if (LocaleController.isRTL) {
                                n2 = AndroidUtilities.dp(16.0f);
                            }
                            else {
                                n2 = AndroidUtilities.dp(8.0f);
                            }
                            int n11;
                            if (LocaleController.isRTL) {
                                n11 = AndroidUtilities.dp(8.0f);
                            }
                            else {
                                n11 = AndroidUtilities.dp(16.0f);
                            }
                            checkBoxCell2.setPadding(n2, 0, n11, 0);
                            view2.addView((View)checkBoxCell2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                            checkBoxCell2.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$cEBDgpO_FSHMoQA0xI0BgbUin8I(array4));
                            builder2.setView((View)view2);
                            builder2.setCustomViewOffset(9);
                            n2 = 1;
                        }
                        else {
                            n2 = 0;
                            user2 = null;
                        }
                    }
                    user4 = user2;
                    n12 = size;
                    n13 = 0;
                    n14 = n5;
                    array3 = array4;
                    n15 = currentAccount;
                }
                else {
                    n2 = currentAccount;
                    int n16;
                    int n28;
                    if (!ChatObject.isChannel(chat) && encryptedChat == null) {
                        final int currentTime2 = ConnectionsManager.getInstance(n2).getCurrentTime();
                        if ((user != null && user.id != UserConfig.getInstance(n2).getClientUserId() && !user.bot) || chat != null) {
                            if (messageObject != null) {
                                boolean b4 = false;
                                Label_1469: {
                                    if (!messageObject.isSendError()) {
                                        final TLRPC.MessageAction action3 = messageObject.messageOwner.action;
                                        if ((action3 == null || action3 instanceof TLRPC.TL_messageActionEmpty || action3 instanceof TLRPC.TL_messageActionPhoneCall) && (messageObject.isOut() || b || ChatObject.hasAdminRights(chat)) && currentTime2 - messageObject.messageOwner.date <= n3) {
                                            b4 = true;
                                            break Label_1469;
                                        }
                                    }
                                    b4 = false;
                                }
                                if (b4) {
                                    n16 = 1;
                                }
                                else {
                                    n16 = 0;
                                }
                                final int n17 = (messageObject.isOut() ^ true) ? 1 : 0;
                                n15 = n2;
                                n2 = n17;
                            }
                            else {
                                int n18 = 1;
                                int n19 = 0;
                                int n20 = 0;
                                while (n18 >= 0) {
                                    int n22;
                                    int n23;
                                    for (int n21 = 0; n21 < array[n18].size(); ++n21, n19 = n22, n20 = n23) {
                                        final MessageObject messageObject4 = (MessageObject)array[n18].valueAt(n21);
                                        final TLRPC.MessageAction action4 = messageObject4.messageOwner.action;
                                        if (action4 != null && !(action4 instanceof TLRPC.TL_messageActionEmpty) && !(action4 instanceof TLRPC.TL_messageActionPhoneCall)) {
                                            n22 = n19;
                                            n23 = n20;
                                        }
                                        else {
                                            if (!messageObject4.isOut() && !b) {
                                                n22 = n19;
                                                n23 = n20;
                                                if (chat == null) {
                                                    continue;
                                                }
                                                n22 = n19;
                                                n23 = n20;
                                                if (!ChatObject.canBlockUsers(chat)) {
                                                    continue;
                                                }
                                            }
                                            n22 = n19;
                                            n23 = n20;
                                            if (currentTime2 - messageObject4.messageOwner.date <= n3) {
                                                ++n20;
                                                n22 = n19;
                                                n23 = n20;
                                                if (n19 == 0) {
                                                    n22 = n19;
                                                    n23 = n20;
                                                    if (!messageObject4.isOut()) {
                                                        n22 = 1;
                                                        n23 = n20;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    --n18;
                                }
                                final int n24 = n2;
                                n2 = n19;
                                n16 = n20;
                                n15 = n24;
                            }
                        }
                        else {
                            n16 = 0;
                            final int n25 = 0;
                            n15 = n2;
                            n2 = n25;
                        }
                        if (n16 > 0) {
                            final FrameLayout view3 = new FrameLayout((Context)parentActivity);
                            final CheckBoxCell checkBoxCell3 = new CheckBoxCell((Context)parentActivity, 1);
                            checkBoxCell3.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            if (b2) {
                                checkBoxCell3.setText(LocaleController.formatString("DeleteMessagesOptionAlso", 2131559251, UserObject.getFirstName(user)), "", false, false);
                            }
                            else if (chat != null && (n2 != 0 || n16 == size)) {
                                checkBoxCell3.setText(LocaleController.getString("DeleteForAll", 2131559243), "", false, false);
                            }
                            else {
                                checkBoxCell3.setText(LocaleController.getString("DeleteMessagesOption", 2131559250), "", false, false);
                            }
                            int n26;
                            if (LocaleController.isRTL) {
                                n26 = AndroidUtilities.dp(16.0f);
                            }
                            else {
                                n26 = AndroidUtilities.dp(8.0f);
                            }
                            int n27;
                            if (LocaleController.isRTL) {
                                n27 = AndroidUtilities.dp(8.0f);
                            }
                            else {
                                n27 = AndroidUtilities.dp(16.0f);
                            }
                            checkBoxCell3.setPadding(n26, 0, n27, 0);
                            view3.addView((View)checkBoxCell3, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                            checkBoxCell3.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$394GBGS7wRI0qRQjkwqQ4GWlXk8(array3));
                            builder.setView((View)view3);
                            builder.setCustomViewOffset(9);
                            n28 = 1;
                        }
                        else {
                            n28 = 0;
                        }
                    }
                    else {
                        n28 = 0;
                        n16 = 0;
                        final int n29 = 0;
                        n15 = n2;
                        n2 = n29;
                    }
                    user4 = null;
                    n12 = size;
                    n13 = n2;
                    n14 = n16;
                    n2 = n28;
                }
                builder.setPositiveButton(LocaleController.getString("Delete", 2131559227), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$OgrqERKmCgdU4Q6tzVkBNsq_WCY(messageObject, groupedMessages, encryptedChat, n15, array3, array, user4, array2, chat, chatFull, runnable));
                if (n12 == 1) {
                    builder.setTitle(LocaleController.getString("DeleteSingleMessagesTitle", 2131559259));
                }
                else {
                    builder.setTitle(LocaleController.formatString("DeleteMessagesTitle", 2131559255, LocaleController.formatPluralString("messages", n12)));
                }
                if (chat != null && n13 != 0) {
                    if (n2 != 0 && n14 != n12) {
                        builder.setMessage(LocaleController.formatString("DeleteMessagesTextGroupPart", 2131559254, LocaleController.formatPluralString("messages", n14)));
                    }
                    else if (n12 == 1) {
                        builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131558686));
                    }
                    else {
                        builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131558682));
                    }
                }
                else if (n2 != 0 && !b2 && n14 != n12) {
                    if (chat != null) {
                        builder.setMessage(LocaleController.formatString("DeleteMessagesTextGroup", 2131559253, LocaleController.formatPluralString("messages", n14)));
                    }
                    else {
                        builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("DeleteMessagesText", 2131559252, LocaleController.formatPluralString("messages", n14), UserObject.getFirstName(user))));
                    }
                }
                else if (chat != null && chat.megagroup) {
                    if (n12 == 1) {
                        builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessageMega", 2131558687));
                    }
                    else {
                        builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessagesMega", 2131558683));
                    }
                }
                else if (n12 == 1) {
                    builder.setMessage(LocaleController.getString("AreYouSureDeleteSingleMessage", 2131558686));
                }
                else {
                    builder.setMessage(LocaleController.getString("AreYouSureDeleteFewMessages", 2131558682));
                }
                builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
                final AlertDialog create = builder.create();
                baseFragment.showDialog(create);
                final TextView textView = (TextView)create.getButton(-1);
                if (textView != null) {
                    textView.setTextColor(Theme.getColor("dialogTextRed2"));
                }
            }
        }
    }
    
    public static Dialog createFreeSpaceDialog(final LaunchActivity launchActivity) {
        final int[] array = { 0 };
        final int int1 = MessagesController.getGlobalMainSettings().getInt("keep_media", 2);
        final int n = 3;
        if (int1 == 2) {
            array[0] = 3;
        }
        else if (int1 == 0) {
            array[0] = 1;
        }
        else if (int1 == 1) {
            array[0] = 2;
        }
        else if (int1 == 3) {
            array[0] = 0;
        }
        final String[] array2 = { LocaleController.formatPluralString("Days", 3), LocaleController.formatPluralString("Weeks", 1), LocaleController.formatPluralString("Months", 1), LocaleController.getString("LowDiskSpaceNeverRemove", 2131559792) };
        final LinearLayout view = new LinearLayout((Context)launchActivity);
        view.setOrientation(1);
        final TextView textView = new TextView((Context)launchActivity);
        textView.setText((CharSequence)LocaleController.getString("LowDiskSpaceTitle2", 2131559794));
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x30);
        int n3 = n;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n3 | 0x30, 24, 0, 24, 8));
        for (int i = 0; i < array2.length; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)launchActivity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array2[i], array[0] == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$MUScg_WxzdOfhXudgtHT1hpQ36U(array, view));
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)launchActivity);
        builder.setTitle(LocaleController.getString("LowDiskSpaceTitle", 2131559793));
        builder.setMessage(LocaleController.getString("LowDiskSpaceMessage", 2131559791));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$k_sgLR2K0GmG2e1LcJ2h3j2LTL4(array));
        builder.setNeutralButton(LocaleController.getString("ClearMediaCache", 2131559110), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$xGMadtvSKvLVwUxwu6lfN7pshfU(launchActivity));
        return builder.create();
    }
    
    public static AlertDialog.Builder createLanguageAlert(final LaunchActivity launchActivity, final TLRPC.TL_langPackLanguage tl_langPackLanguage) {
        if (tl_langPackLanguage == null) {
            return null;
        }
        tl_langPackLanguage.lang_code = tl_langPackLanguage.lang_code.replace('-', '_').toLowerCase();
        tl_langPackLanguage.plural_code = tl_langPackLanguage.plural_code.replace('-', '_').toLowerCase();
        final String base_lang_code = tl_langPackLanguage.base_lang_code;
        if (base_lang_code != null) {
            tl_langPackLanguage.base_lang_code = base_lang_code.replace('-', '_').toLowerCase();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)launchActivity);
        String s;
        if (LocaleController.getInstance().getCurrentLocaleInfo().shortName.equals(tl_langPackLanguage.lang_code)) {
            builder.setTitle(LocaleController.getString("Language", 2131559715));
            s = LocaleController.formatString("LanguageSame", 2131559722, tl_langPackLanguage.name);
            builder.setNegativeButton(LocaleController.getString("OK", 2131560097), null);
            builder.setNeutralButton(LocaleController.getString("SETTINGS", 2131560623), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$LEkyy2uvzoVwagVSlDPe5F3R2jI(launchActivity));
        }
        else if (tl_langPackLanguage.strings_count == 0) {
            builder.setTitle(LocaleController.getString("LanguageUnknownTitle", 2131559725));
            s = LocaleController.formatString("LanguageUnknownCustomAlert", 2131559724, tl_langPackLanguage.name);
            builder.setNegativeButton(LocaleController.getString("OK", 2131560097), null);
        }
        else {
            builder.setTitle(LocaleController.getString("LanguageTitle", 2131559723));
            if (tl_langPackLanguage.official) {
                s = LocaleController.formatString("LanguageAlert", 2131559716, tl_langPackLanguage.name, (int)Math.ceil(tl_langPackLanguage.translated_count / (float)tl_langPackLanguage.strings_count * 100.0f));
            }
            else {
                s = LocaleController.formatString("LanguageCustomAlert", 2131559719, tl_langPackLanguage.name, (int)Math.ceil(tl_langPackLanguage.translated_count / (float)tl_langPackLanguage.strings_count * 100.0f));
            }
            builder.setPositiveButton(LocaleController.getString("Change", 2131558905), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$TOP_U4EklnBKZLMjFtDvABD1mPg(tl_langPackLanguage, launchActivity));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        }
        final SpannableStringBuilder text = new SpannableStringBuilder((CharSequence)AndroidUtilities.replaceTags(s));
        final int index = TextUtils.indexOf((CharSequence)text, '[');
        int index2;
        if (index != -1) {
            final int n = index + 1;
            final int n2 = index2 = TextUtils.indexOf((CharSequence)text, (char)93, n);
            if (index != -1 && (index2 = n2) != -1) {
                text.delete(n2, n2 + 1);
                text.delete(index, n);
                index2 = n2;
            }
        }
        else {
            index2 = -1;
        }
        if (index != -1 && index2 != -1) {
            text.setSpan((Object)new URLSpanNoUnderline(tl_langPackLanguage.translations_url) {
                @Override
                public void onClick(final View view) {
                    builder.getDismissRunnable().run();
                    super.onClick(view);
                }
            }, index, index2 - 1, 33);
        }
        final TextView view = new TextView((Context)launchActivity);
        view.setText((CharSequence)text);
        view.setTextSize(1, 16.0f);
        view.setLinkTextColor(Theme.getColor("dialogTextLink"));
        view.setHighlightColor(Theme.getColor("dialogLinkSelection"));
        view.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
        view.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
        view.setTextColor(Theme.getColor("dialogTextBlack"));
        builder.setView((View)view);
        return builder;
    }
    
    public static Dialog createLocationUpdateDialog(final Activity activity, final TLRPC.User user, final MessagesStorage.IntCallback intCallback) {
        final int[] array = { 0 };
        final int n = 3;
        final String[] array2 = { LocaleController.getString("SendLiveLocationFor15m", 2131560696), LocaleController.getString("SendLiveLocationFor1h", 2131560697), LocaleController.getString("SendLiveLocationFor8h", 2131560698) };
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        final TextView textView = new TextView((Context)activity);
        if (user != null) {
            textView.setText((CharSequence)LocaleController.formatString("LiveLocationAlertPrivate", 2131559766, UserObject.getFirstName(user)));
        }
        else {
            textView.setText((CharSequence)LocaleController.getString("LiveLocationAlertGroup", 2131559765));
        }
        textView.setTextColor(Theme.getColor("dialogTextBlack"));
        textView.setTextSize(1, 16.0f);
        int n2;
        if (LocaleController.isRTL) {
            n2 = 5;
        }
        else {
            n2 = 3;
        }
        textView.setGravity(n2 | 0x30);
        int n3 = n;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-2, -2, n3 | 0x30, 24, 0, 24, 8));
        for (int i = 0; i < array2.length; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array2[i], array[0] == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$KrxSVo5KH_kGkjf6bCbY8P17cD4(array, view));
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        builder.setTopImage(new ShareLocationDrawable((Context)activity, false), Theme.getColor("dialogTopBackground"));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("ShareFile", 2131560748), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$fbJmmwvIKPVs4mcvuv3lmtxjCfU(array, intCallback));
        builder.setNeutralButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder.create();
    }
    
    public static Dialog createMuteAlert(final Context context, final long n) {
        if (context == null) {
            return null;
        }
        final BottomSheet.Builder builder = new BottomSheet.Builder(context);
        builder.setTitle(LocaleController.getString("Notifications", 2131560055));
        builder.setItems(new CharSequence[] { LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 1)), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 8)), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Days", 2)), LocaleController.getString("MuteDisable", 2131559885) }, (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$V32PpNrShc6YGjfaIJjE0yrRz3k(n));
        return builder.create();
    }
    
    public static Dialog createPopupSelectDialog(final Activity activity, final int n, final Runnable runnable) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] array = { 0 };
        if (n == 1) {
            array[0] = notificationsSettings.getInt("popupAll", 0);
        }
        else if (n == 0) {
            array[0] = notificationsSettings.getInt("popupGroup", 0);
        }
        else {
            array[0] = notificationsSettings.getInt("popupChannel", 0);
        }
        final String[] array2 = { LocaleController.getString("NoPopup", 2131559939), LocaleController.getString("OnlyWhenScreenOn", 2131560109), LocaleController.getString("OnlyWhenScreenOff", 2131560108), LocaleController.getString("AlwaysShowPopup", 2131558614) };
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        for (int i = 0; i < array2.length; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setTag((Object)i);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array2[i], array[0] == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$U04Fb9kdJEva90xAKw9vhhYkzns(array, n, builder, runnable));
        }
        builder.setTitle(LocaleController.getString("PopupNotification", 2131560471));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder.create();
    }
    
    public static Dialog createPrioritySelectDialog(final Activity activity, final long lng, final int n, final Runnable runnable) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] array = { 0 };
        String[] array2;
        if (lng != 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("priority_");
            sb.append(lng);
            array[0] = notificationsSettings.getInt(sb.toString(), 3);
            if (array[0] == 3) {
                array[0] = 0;
            }
            else if (array[0] == 4) {
                array[0] = 1;
            }
            else if (array[0] == 5) {
                array[0] = 2;
            }
            else if (array[0] == 0) {
                array[0] = 3;
            }
            else {
                array[0] = 4;
            }
            array2 = new String[] { LocaleController.getString("NotificationsPrioritySettings", 2131560085), LocaleController.getString("NotificationsPriorityLow", 2131560083), LocaleController.getString("NotificationsPriorityMedium", 2131560084), LocaleController.getString("NotificationsPriorityHigh", 2131560082), LocaleController.getString("NotificationsPriorityUrgent", 2131560086) };
        }
        else {
            if (lng == 0L) {
                if (n == 1) {
                    array[0] = notificationsSettings.getInt("priority_messages", 1);
                }
                else if (n == 0) {
                    array[0] = notificationsSettings.getInt("priority_group", 1);
                }
                else if (n == 2) {
                    array[0] = notificationsSettings.getInt("priority_channel", 1);
                }
            }
            if (array[0] == 4) {
                array[0] = 0;
            }
            else if (array[0] == 5) {
                array[0] = 1;
            }
            else if (array[0] == 0) {
                array[0] = 2;
            }
            else {
                array[0] = 3;
            }
            array2 = new String[] { LocaleController.getString("NotificationsPriorityLow", 2131560083), LocaleController.getString("NotificationsPriorityMedium", 2131560084), LocaleController.getString("NotificationsPriorityHigh", 2131560082), LocaleController.getString("NotificationsPriorityUrgent", 2131560086) };
        }
        final LinearLayout linearLayout = new LinearLayout((Context)activity);
        linearLayout.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        int i = 0;
        final LinearLayout view = linearLayout;
        while (i < array2.length) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array2[i], array[0] == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$DVGlkIX7hR4sQs3b_3TsrRLkHrw(array, lng, n, notificationsSettings, builder, runnable));
            ++i;
        }
        builder.setTitle(LocaleController.getString("NotificationsImportance", 2131560072));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder.create();
    }
    
    public static void createReportAlert(final Context context, final long n, final int n2, final BaseFragment baseFragment) {
        if (context != null) {
            if (baseFragment != null) {
                final BottomSheet.Builder builder = new BottomSheet.Builder(context);
                builder.setTitle(LocaleController.getString("ReportChat", 2131560568));
                builder.setItems(new CharSequence[] { LocaleController.getString("ReportChatSpam", 2131560574), LocaleController.getString("ReportChatViolence", 2131560575), LocaleController.getString("ReportChatChild", 2131560569), LocaleController.getString("ReportChatPornography", 2131560572), LocaleController.getString("ReportChatOther", 2131560571) }, (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$72Ijnyr0wy8R08YUf_bNvhf4MNU(n, n2, baseFragment, context));
                baseFragment.showDialog(builder.create());
            }
        }
    }
    
    public static AlertDialog.Builder createSimpleAlert(final Context context, final String message) {
        if (message == null) {
            return null;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        builder.setMessage(message);
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        return builder;
    }
    
    public static Dialog createSingleChoiceDialog(final Activity activity, final String[] array, final String title, final int n, final DialogInterface$OnClickListener dialogInterface$OnClickListener) {
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        for (int i = 0; i < array.length; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array[i], n == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$NzxH3UcpchY_thDrzzdwxnXbOG4(builder, dialogInterface$OnClickListener));
        }
        builder.setTitle(title);
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder.create();
    }
    
    public static AlertDialog createSupportAlert(final BaseFragment baseFragment) {
        if (baseFragment != null && baseFragment.getParentActivity() != null) {
            final TextView view = new TextView((Context)baseFragment.getParentActivity());
            final SpannableString text = new SpannableString((CharSequence)Html.fromHtml(LocaleController.getString("AskAQuestionInfo", 2131558707).replace("\n", "<br>")));
            final URLSpan[] array = (URLSpan[])((Spannable)text).getSpans(0, ((Spannable)text).length(), (Class)URLSpan.class);
            for (int i = 0; i < array.length; ++i) {
                final URLSpan urlSpan = array[i];
                final int spanStart = ((Spannable)text).getSpanStart((Object)urlSpan);
                final int spanEnd = ((Spannable)text).getSpanEnd((Object)urlSpan);
                ((Spannable)text).removeSpan((Object)urlSpan);
                ((Spannable)text).setSpan((Object)new URLSpanNoUnderline(urlSpan.getURL()) {
                    @Override
                    public void onClick(final View view) {
                        baseFragment.dismissCurrentDialig();
                        super.onClick(view);
                    }
                }, spanStart, spanEnd, 0);
            }
            view.setText((CharSequence)text);
            view.setTextSize(1, 16.0f);
            view.setLinkTextColor(Theme.getColor("dialogTextLink"));
            view.setHighlightColor(Theme.getColor("dialogLinkSelection"));
            view.setPadding(AndroidUtilities.dp(23.0f), 0, AndroidUtilities.dp(23.0f), 0);
            view.setMovementMethod((MovementMethod)new AndroidUtilities.LinkMovementMethodMy());
            view.setTextColor(Theme.getColor("dialogTextBlack"));
            final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
            builder.setView((View)view);
            builder.setTitle(LocaleController.getString("AskAQuestion", 2131558706));
            builder.setPositiveButton(LocaleController.getString("AskButton", 2131558708), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$KOZG0pVJG6nMdk1njo_1lCbwC_Q(baseFragment));
            builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
            return builder.create();
        }
        return null;
    }
    
    public static AlertDialog.Builder createTTLAlert(final Context context, final TLRPC.EncryptedChat encryptedChat) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(LocaleController.getString("MessageLifetime", 2131559846));
        final NumberPicker view = new NumberPicker(context);
        view.setMinValue(0);
        view.setMaxValue(20);
        final int ttl = encryptedChat.ttl;
        if (ttl > 0 && ttl < 16) {
            view.setValue(ttl);
        }
        else {
            final int ttl2 = encryptedChat.ttl;
            if (ttl2 == 30) {
                view.setValue(16);
            }
            else if (ttl2 == 60) {
                view.setValue(17);
            }
            else if (ttl2 == 3600) {
                view.setValue(18);
            }
            else if (ttl2 == 86400) {
                view.setValue(19);
            }
            else if (ttl2 == 604800) {
                view.setValue(20);
            }
            else if (ttl2 == 0) {
                view.setValue(0);
            }
        }
        view.setFormatter((NumberPicker.Formatter)_$$Lambda$AlertsCreator$vV37v3yPGSQ3dc7MqZN0Qqbwa20.INSTANCE);
        builder.setView((View)view);
        builder.setNegativeButton(LocaleController.getString("Done", 2131559299), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g(encryptedChat, view));
        return builder;
    }
    
    public static Dialog createVibrationSelectDialog(final Activity activity, final long lng, final String str, final Runnable runnable) {
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(UserConfig.selectedAccount);
        final int[] array = { 0 };
        String[] array2;
        if (lng != 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(lng);
            array[0] = notificationsSettings.getInt(sb.toString(), 0);
            if (array[0] == 3) {
                array[0] = 2;
            }
            else if (array[0] == 2) {
                array[0] = 3;
            }
            array2 = new String[] { LocaleController.getString("VibrationDefault", 2131561041), LocaleController.getString("Short", 2131560775), LocaleController.getString("Long", 2131559790), LocaleController.getString("VibrationDisabled", 2131561042) };
        }
        else {
            array[0] = notificationsSettings.getInt(str, 0);
            if (array[0] == 0) {
                array[0] = 1;
            }
            else if (array[0] == 1) {
                array[0] = 2;
            }
            else if (array[0] == 2) {
                array[0] = 0;
            }
            array2 = new String[] { LocaleController.getString("VibrationDisabled", 2131561042), LocaleController.getString("VibrationDefault", 2131561041), LocaleController.getString("Short", 2131560775), LocaleController.getString("Long", 2131559790), LocaleController.getString("OnlyIfSilent", 2131560107) };
        }
        final LinearLayout view = new LinearLayout((Context)activity);
        view.setOrientation(1);
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        for (int i = 0; i < array2.length; ++i) {
            final RadioColorCell radioColorCell = new RadioColorCell((Context)activity);
            radioColorCell.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
            radioColorCell.setTag((Object)i);
            radioColorCell.setCheckColor(Theme.getColor("radioBackground"), Theme.getColor("dialogRadioBackgroundChecked"));
            radioColorCell.setTextAndValue(array2[i], array[0] == i);
            view.addView((View)radioColorCell);
            radioColorCell.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$hGz1nBNKJB7FgFrku7OBrjp5nlM(array, lng, str, builder, runnable));
        }
        builder.setTitle(LocaleController.getString("Vibrate", 2131561040));
        builder.setView((View)view);
        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
        return builder.create();
    }
    
    public static Dialog createVibrationSelectDialog(final Activity activity, final long n, final boolean b, final boolean b2, final Runnable runnable) {
        String s;
        if (n != 0L) {
            s = "vibrate_";
        }
        else if (b) {
            s = "vibrate_group";
        }
        else {
            s = "vibrate_messages";
        }
        return createVibrationSelectDialog(activity, n, s, runnable);
    }
    
    private static String getFloodWaitString(String s) {
        final int intValue = Utilities.parseInt(s);
        if (intValue < 60) {
            s = LocaleController.formatPluralString("Seconds", intValue);
        }
        else {
            s = LocaleController.formatPluralString("Minutes", intValue / 60);
        }
        return LocaleController.formatString("FloodWaitTime", 2131559496, s);
    }
    
    private static void performAskAQuestion(final BaseFragment baseFragment) {
        final int currentAccount = baseFragment.getCurrentAccount();
        final SharedPreferences mainSettings = MessagesController.getMainSettings(currentAccount);
        final int int1 = mainSettings.getInt("support_id", 0);
        TLRPC.User user2;
        final TLRPC.User user = user2 = null;
        if (int1 != 0) {
            user2 = MessagesController.getInstance(currentAccount).getUser(int1);
            if (user2 == null) {
                final String string = mainSettings.getString("support_user", (String)null);
                if (string != null) {
                    try {
                        final byte[] decode = Base64.decode(string, 0);
                        if (decode != null) {
                            final SerializedData serializedData = new SerializedData(decode);
                            final TLRPC.User tLdeserialize = TLRPC.User.TLdeserialize(serializedData, serializedData.readInt32(false), false);
                            if ((user2 = tLdeserialize) != null) {
                                user2 = tLdeserialize;
                                if (tLdeserialize.id == 333000) {
                                    user2 = null;
                                }
                            }
                            serializedData.cleanup();
                        }
                    }
                    catch (Exception ex) {
                        FileLog.e(ex);
                        user2 = user;
                    }
                }
            }
        }
        if (user2 == null) {
            final AlertDialog alertDialog = new AlertDialog((Context)baseFragment.getParentActivity(), 3);
            alertDialog.setCanCacnel(false);
            alertDialog.show();
            ConnectionsManager.getInstance(currentAccount).sendRequest(new TLRPC.TL_help_getSupport(), new _$$Lambda$AlertsCreator$vTt7GpWddJNaVuBDzHfJsdiKdl4(mainSettings, alertDialog, currentAccount, baseFragment));
        }
        else {
            MessagesController.getInstance(currentAccount).putUser(user2, true);
            final Bundle bundle = new Bundle();
            bundle.putInt("user_id", user2.id);
            baseFragment.presentFragment(new ChatActivity(bundle));
        }
    }
    
    public static Dialog processError(int n, final TLRPC.TL_error tl_error, final BaseFragment baseFragment, final TLObject tlObject, final Object... array) {
        final int code = tl_error.code;
        if (code != 406) {
            final String text = tl_error.text;
            if (text != null) {
                if (!(tlObject instanceof TLRPC.TL_account_saveSecureValue) && !(tlObject instanceof TLRPC.TL_account_getAuthorizationForm)) {
                    if (!(tlObject instanceof TLRPC.TL_channels_joinChannel) && !(tlObject instanceof TLRPC.TL_channels_editAdmin) && !(tlObject instanceof TLRPC.TL_channels_inviteToChannel) && !(tlObject instanceof TLRPC.TL_messages_addChatUser) && !(tlObject instanceof TLRPC.TL_messages_startBot) && !(tlObject instanceof TLRPC.TL_channels_editBanned) && !(tlObject instanceof TLRPC.TL_messages_editChatDefaultBannedRights) && !(tlObject instanceof TLRPC.TL_messages_editChatAdmin)) {
                        if (tlObject instanceof TLRPC.TL_messages_createChat) {
                            if (text.startsWith("FLOOD_WAIT")) {
                                showFloodWaitAlert(tl_error.text, baseFragment);
                            }
                            else {
                                showAddUserAlert(tl_error.text, baseFragment, false);
                            }
                        }
                        else if (tlObject instanceof TLRPC.TL_channels_createChannel) {
                            if (text.startsWith("FLOOD_WAIT")) {
                                showFloodWaitAlert(tl_error.text, baseFragment);
                            }
                            else {
                                showAddUserAlert(tl_error.text, baseFragment, false);
                            }
                        }
                        else if (tlObject instanceof TLRPC.TL_messages_editMessage) {
                            if (!text.equals("MESSAGE_NOT_MODIFIED")) {
                                if (baseFragment != null) {
                                    showSimpleAlert(baseFragment, LocaleController.getString("EditMessageError", 2131559324));
                                }
                                else {
                                    showSimpleToast(baseFragment, LocaleController.getString("EditMessageError", 2131559324));
                                }
                            }
                        }
                        else if (!(tlObject instanceof TLRPC.TL_messages_sendMessage) && !(tlObject instanceof TLRPC.TL_messages_sendMedia) && !(tlObject instanceof TLRPC.TL_messages_sendBroadcast) && !(tlObject instanceof TLRPC.TL_messages_sendInlineBotResult) && !(tlObject instanceof TLRPC.TL_messages_forwardMessages) && !(tlObject instanceof TLRPC.TL_messages_sendMultiMedia)) {
                            if (tlObject instanceof TLRPC.TL_messages_importChatInvite) {
                                if (text.startsWith("FLOOD_WAIT")) {
                                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                }
                                else if (tl_error.text.equals("USERS_TOO_MUCH")) {
                                    showSimpleAlert(baseFragment, LocaleController.getString("JoinToGroupErrorFull", 2131559704));
                                }
                                else {
                                    showSimpleAlert(baseFragment, LocaleController.getString("JoinToGroupErrorNotExist", 2131559705));
                                }
                            }
                            else if (tlObject instanceof TLRPC.TL_messages_getAttachedStickers) {
                                if (baseFragment != null && baseFragment.getParentActivity() != null) {
                                    final Activity parentActivity = baseFragment.getParentActivity();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(LocaleController.getString("ErrorOccurred", 2131559375));
                                    sb.append("\n");
                                    sb.append(tl_error.text);
                                    Toast.makeText((Context)parentActivity, (CharSequence)sb.toString(), 0).show();
                                }
                            }
                            else if (!(tlObject instanceof TLRPC.TL_account_confirmPhone) && !(tlObject instanceof TLRPC.TL_account_verifyPhone) && !(tlObject instanceof TLRPC.TL_account_verifyEmail)) {
                                if (tlObject instanceof TLRPC.TL_auth_resendCode) {
                                    if (text.contains("PHONE_NUMBER_INVALID")) {
                                        return showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                                    }
                                    if (tl_error.text.contains("PHONE_CODE_EMPTY") || tl_error.text.contains("PHONE_CODE_INVALID")) {
                                        return showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131559671));
                                    }
                                    if (tl_error.text.contains("PHONE_CODE_EXPIRED")) {
                                        return showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131559120));
                                    }
                                    if (tl_error.text.startsWith("FLOOD_WAIT")) {
                                        return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                    }
                                    if (tl_error.code != -1000) {
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append(LocaleController.getString("ErrorOccurred", 2131559375));
                                        sb2.append("\n");
                                        sb2.append(tl_error.text);
                                        return showSimpleAlert(baseFragment, sb2.toString());
                                    }
                                }
                                else if (tlObject instanceof TLRPC.TL_account_sendConfirmPhoneCode) {
                                    if (code == 400) {
                                        return showSimpleAlert(baseFragment, LocaleController.getString("CancelLinkExpired", 2131558895));
                                    }
                                    if (text != null) {
                                        if (text.startsWith("FLOOD_WAIT")) {
                                            return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                        }
                                        return showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131559375));
                                    }
                                }
                                else if (tlObject instanceof TLRPC.TL_account_changePhone) {
                                    if (text.contains("PHONE_NUMBER_INVALID")) {
                                        showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                                    }
                                    else if (!tl_error.text.contains("PHONE_CODE_EMPTY") && !tl_error.text.contains("PHONE_CODE_INVALID")) {
                                        if (tl_error.text.contains("PHONE_CODE_EXPIRED")) {
                                            showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131559120));
                                        }
                                        else if (tl_error.text.startsWith("FLOOD_WAIT")) {
                                            showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                        }
                                        else {
                                            showSimpleAlert(baseFragment, tl_error.text);
                                        }
                                    }
                                    else {
                                        showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131559671));
                                    }
                                }
                                else if (tlObject instanceof TLRPC.TL_account_sendChangePhoneCode) {
                                    if (text.contains("PHONE_NUMBER_INVALID")) {
                                        showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                                    }
                                    else if (!tl_error.text.contains("PHONE_CODE_EMPTY") && !tl_error.text.contains("PHONE_CODE_INVALID")) {
                                        if (tl_error.text.contains("PHONE_CODE_EXPIRED")) {
                                            showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131559120));
                                        }
                                        else if (tl_error.text.startsWith("FLOOD_WAIT")) {
                                            showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                        }
                                        else if (tl_error.text.startsWith("PHONE_NUMBER_OCCUPIED")) {
                                            showSimpleAlert(baseFragment, LocaleController.formatString("ChangePhoneNumberOccupied", 2131558915, (String)array[0]));
                                        }
                                        else {
                                            showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131559375));
                                        }
                                    }
                                    else {
                                        showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131559671));
                                    }
                                }
                                else if (tlObject instanceof TLRPC.TL_updateUserName) {
                                    n = -1;
                                    final int hashCode = text.hashCode();
                                    if (hashCode != 288843630) {
                                        if (hashCode == 533175271) {
                                            if (text.equals("USERNAME_OCCUPIED")) {
                                                n = 1;
                                            }
                                        }
                                    }
                                    else if (text.equals("USERNAME_INVALID")) {
                                        n = 0;
                                    }
                                    if (n != 0) {
                                        if (n != 1) {
                                            showSimpleAlert(baseFragment, LocaleController.getString("ErrorOccurred", 2131559375));
                                        }
                                        else {
                                            showSimpleAlert(baseFragment, LocaleController.getString("UsernameInUse", 2131561027));
                                        }
                                    }
                                    else {
                                        showSimpleAlert(baseFragment, LocaleController.getString("UsernameInvalid", 2131561028));
                                    }
                                }
                                else if (tlObject instanceof TLRPC.TL_contacts_importContacts) {
                                    if (tl_error != null && !text.startsWith("FLOOD_WAIT")) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append(LocaleController.getString("ErrorOccurred", 2131559375));
                                        sb3.append("\n");
                                        sb3.append(tl_error.text);
                                        showSimpleAlert(baseFragment, sb3.toString());
                                    }
                                    else {
                                        showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                    }
                                }
                                else if (!(tlObject instanceof TLRPC.TL_account_getPassword) && !(tlObject instanceof TLRPC.TL_account_getTmpPassword)) {
                                    if (tlObject instanceof TLRPC.TL_payments_sendPaymentForm) {
                                        n = -1;
                                        final int hashCode2 = text.hashCode();
                                        if (hashCode2 != -1144062453) {
                                            if (hashCode2 == -784238410) {
                                                if (text.equals("PAYMENT_FAILED")) {
                                                    n = 1;
                                                }
                                            }
                                        }
                                        else if (text.equals("BOT_PRECHECKOUT_FAILED")) {
                                            n = 0;
                                        }
                                        if (n != 0) {
                                            if (n != 1) {
                                                showSimpleToast(baseFragment, tl_error.text);
                                            }
                                            else {
                                                showSimpleToast(baseFragment, LocaleController.getString("PaymentFailed", 2131560374));
                                            }
                                        }
                                        else {
                                            showSimpleToast(baseFragment, LocaleController.getString("PaymentPrecheckoutFailed", 2131560387));
                                        }
                                    }
                                    else if (tlObject instanceof TLRPC.TL_payments_validateRequestedInfo) {
                                        n = -1;
                                        if (text.hashCode() == 1758025548) {
                                            if (text.equals("SHIPPING_NOT_AVAILABLE")) {
                                                n = 0;
                                            }
                                        }
                                        if (n != 0) {
                                            showSimpleToast(baseFragment, tl_error.text);
                                        }
                                        else {
                                            showSimpleToast(baseFragment, LocaleController.getString("PaymentNoShippingMethod", 2131560376));
                                        }
                                    }
                                }
                                else if (tl_error.text.startsWith("FLOOD_WAIT")) {
                                    showSimpleToast(baseFragment, getFloodWaitString(tl_error.text));
                                }
                                else {
                                    showSimpleToast(baseFragment, tl_error.text);
                                }
                            }
                            else {
                                if (tl_error.text.contains("PHONE_CODE_EMPTY") || tl_error.text.contains("PHONE_CODE_INVALID") || tl_error.text.contains("CODE_INVALID") || tl_error.text.contains("CODE_EMPTY")) {
                                    return showSimpleAlert(baseFragment, LocaleController.getString("InvalidCode", 2131559671));
                                }
                                if (tl_error.text.contains("PHONE_CODE_EXPIRED") || tl_error.text.contains("EMAIL_VERIFY_EXPIRED")) {
                                    return showSimpleAlert(baseFragment, LocaleController.getString("CodeExpired", 2131559120));
                                }
                                if (tl_error.text.startsWith("FLOOD_WAIT")) {
                                    return showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                                }
                                return showSimpleAlert(baseFragment, tl_error.text);
                            }
                        }
                        else if (tl_error.text.equals("PEER_FLOOD")) {
                            NotificationCenter.getInstance(n).postNotificationName(NotificationCenter.needShowAlert, 0);
                        }
                        else if (tl_error.text.equals("USER_BANNED_IN_CHANNEL")) {
                            NotificationCenter.getInstance(n).postNotificationName(NotificationCenter.needShowAlert, 5);
                        }
                    }
                    else if (baseFragment != null) {
                        showAddUserAlert(tl_error.text, baseFragment, (boolean)array[0]);
                    }
                    else if (tl_error.text.equals("PEER_FLOOD")) {
                        NotificationCenter.getInstance(n).postNotificationName(NotificationCenter.needShowAlert, 1);
                    }
                }
                else if (tl_error.text.contains("PHONE_NUMBER_INVALID")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("InvalidPhoneNumber", 2131559674));
                }
                else if (tl_error.text.startsWith("FLOOD_WAIT")) {
                    showSimpleAlert(baseFragment, LocaleController.getString("FloodWait", 2131559495));
                }
                else if ("APP_VERSION_OUTDATED".equals(tl_error.text)) {
                    showUpdateAppAlert((Context)baseFragment.getParentActivity(), LocaleController.getString("UpdateAppAlert", 2131560951), true);
                }
                else {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(LocaleController.getString("ErrorOccurred", 2131559375));
                    sb4.append("\n");
                    sb4.append(tl_error.text);
                    showSimpleAlert(baseFragment, sb4.toString());
                }
                return null;
            }
        }
        return null;
    }
    
    public static void showAddUserAlert(final String str, final BaseFragment baseFragment, final boolean b) {
        if (str != null && baseFragment != null) {
            if (baseFragment.getParentActivity() != null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                switch (str) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(LocaleController.getString("ErrorOccurred", 2131559375));
                        sb.append("\n");
                        sb.append(str);
                        builder.setMessage(sb.toString());
                        break;
                    }
                    case "USER_ADMIN_INVALID": {
                        builder.setMessage(LocaleController.getString("AddBannedErrorAdmin", 2131558564));
                        break;
                    }
                    case "CHAT_ADMIN_INVITE_REQUIRED": {
                        builder.setMessage(LocaleController.getString("AddAdminErrorNotAMember", 2131558558));
                        break;
                    }
                    case "CHAT_ADMIN_BAN_REQUIRED":
                    case "USER_KICKED": {
                        builder.setMessage(LocaleController.getString("AddAdminErrorBlacklisted", 2131558557));
                        break;
                    }
                    case "YOU_BLOCKED_USER": {
                        builder.setMessage(LocaleController.getString("YouBlockedUser", 2131561137));
                        break;
                    }
                    case "USER_RESTRICTED": {
                        builder.setMessage(LocaleController.getString("UserRestricted", 2131560993));
                        break;
                    }
                    case "USERS_TOO_FEW": {
                        builder.setMessage(LocaleController.getString("CreateGroupError", 2131559168));
                        break;
                    }
                    case "USER_PRIVACY_RESTRICTED": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("InviteToChannelError", 2131559687));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("InviteToGroupError", 2131559689));
                        break;
                    }
                    case "BOTS_TOO_MUCH": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("ChannelUserCantBot", 2131559011));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("GroupUserCantBot", 2131559622));
                        break;
                    }
                    case "ADMINS_TOO_MUCH": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("ChannelUserCantAdmin", 2131559010));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("GroupUserCantAdmin", 2131559621));
                        break;
                    }
                    case "USER_NOT_MUTUAL_CONTACT": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("ChannelUserLeftError", 2131559012));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("GroupUserLeftError", 2131559623));
                        break;
                    }
                    case "USERS_TOO_MUCH": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("ChannelUserAddLimit", 2131559008));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("GroupUserAddLimit", 2131559619));
                        break;
                    }
                    case "USER_BLOCKED":
                    case "USER_BOT":
                    case "USER_ID_INVALID": {
                        if (b) {
                            builder.setMessage(LocaleController.getString("ChannelUserCantAdd", 2131559009));
                            break;
                        }
                        builder.setMessage(LocaleController.getString("GroupUserCantAdd", 2131559620));
                        break;
                    }
                    case "PEER_FLOOD": {
                        builder.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131559958));
                        builder.setNegativeButton(LocaleController.getString("MoreInfo", 2131559883), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$6u18oE86Tvr4pJTZ_ZJYwdHJU6U(baseFragment));
                        break;
                    }
                }
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                baseFragment.showDialog(builder.create(), true, null);
            }
        }
    }
    
    public static void showCustomNotificationsDialog(final BaseFragment baseFragment, final long n, final int n2, final ArrayList<NotificationsSettingsActivity.NotificationException> list, final int n3, final MessagesStorage.IntCallback intCallback) {
        showCustomNotificationsDialog(baseFragment, n, n2, list, n3, intCallback, null);
    }
    
    public static void showCustomNotificationsDialog(final BaseFragment baseFragment, final long n, final int n2, final ArrayList<NotificationsSettingsActivity.NotificationException> list, final int n3, final MessagesStorage.IntCallback intCallback, final MessagesStorage.IntCallback intCallback2) {
        if (baseFragment != null) {
            if (baseFragment.getParentActivity() != null) {
                final boolean globalNotificationsEnabled = NotificationsController.getInstance(n3).isGlobalNotificationsEnabled(n);
                final String[] array = { LocaleController.getString("NotificationsTurnOn", 2131560093), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Hours", 1)), LocaleController.formatString("MuteFor", 2131559886, LocaleController.formatPluralString("Days", 2)), null, null };
                final Drawable drawable = null;
                String string;
                if (n == 0L && baseFragment instanceof NotificationsCustomSettingsActivity) {
                    string = null;
                }
                else {
                    string = LocaleController.getString("NotificationsCustomize", 2131560060);
                }
                array[3] = string;
                array[4] = LocaleController.getString("NotificationsTurnOff", 2131560092);
                final int[] array3;
                final int[] array2 = array3 = new int[5];
                array3[0] = 2131165713;
                array3[1] = 2131165710;
                array3[2] = 2131165711;
                array3[3] = 2131165714;
                array3[4] = 2131165712;
                final LinearLayout view = new LinearLayout((Context)baseFragment.getParentActivity());
                view.setOrientation(1);
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
                int i = 0;
                final Drawable drawable2 = drawable;
                final int[] array4 = array2;
                while (i < array.length) {
                    if (array[i] != null) {
                        final TextView textView = new TextView((Context)baseFragment.getParentActivity());
                        final Drawable drawable3 = baseFragment.getParentActivity().getResources().getDrawable(array4[i]);
                        if (i == array.length - 1) {
                            textView.setTextColor(Theme.getColor("dialogTextRed"));
                            drawable3.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogRedIcon"), PorterDuff$Mode.MULTIPLY));
                        }
                        else {
                            textView.setTextColor(Theme.getColor("dialogTextBlack"));
                            drawable3.setColorFilter((ColorFilter)new PorterDuffColorFilter(Theme.getColor("dialogIcon"), PorterDuff$Mode.MULTIPLY));
                        }
                        textView.setTextSize(1, 16.0f);
                        textView.setLines(1);
                        textView.setMaxLines(1);
                        textView.setCompoundDrawablesWithIntrinsicBounds(drawable3, drawable2, drawable2, drawable2);
                        textView.setTag((Object)i);
                        textView.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        textView.setPadding(AndroidUtilities.dp(24.0f), 0, AndroidUtilities.dp(24.0f), 0);
                        textView.setSingleLine(true);
                        textView.setGravity(19);
                        textView.setCompoundDrawablePadding(AndroidUtilities.dp(26.0f));
                        textView.setText((CharSequence)array[i]);
                        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createLinear(-1, 48, 51));
                        textView.setOnClickListener((View$OnClickListener)new _$$Lambda$AlertsCreator$Z4kCfZq2Csg4JqBOo8whPkThoNo(n, n3, globalNotificationsEnabled, intCallback2, n2, baseFragment, list, intCallback, builder));
                    }
                    ++i;
                }
                builder.setTitle(LocaleController.getString("Notifications", 2131560055));
                builder.setView((View)view);
                baseFragment.showDialog(builder.create());
            }
        }
    }
    
    public static void showFloodWaitAlert(String s, final BaseFragment baseFragment) {
        if (s != null && s.startsWith("FLOOD_WAIT") && baseFragment != null) {
            if (baseFragment.getParentActivity() != null) {
                final int intValue = Utilities.parseInt(s);
                if (intValue < 60) {
                    s = LocaleController.formatPluralString("Seconds", intValue);
                }
                else {
                    s = LocaleController.formatPluralString("Minutes", intValue / 60);
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", 2131558635));
                builder.setMessage(LocaleController.formatString("FloodWaitTime", 2131559496, s));
                builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
                baseFragment.showDialog(builder.create(), true, null);
            }
        }
    }
    
    public static AlertDialog showSecretLocationAlert(final Context context, int availableMapProviders, final Runnable runnable, final boolean b) {
        final ArrayList<String> list = new ArrayList<String>();
        availableMapProviders = MessagesController.getInstance(availableMapProviders).availableMapProviders;
        if ((availableMapProviders & 0x1) != 0x0) {
            list.add(LocaleController.getString("MapPreviewProviderTelegram", 2131559804));
        }
        if ((availableMapProviders & 0x2) != 0x0) {
            list.add(LocaleController.getString("MapPreviewProviderGoogle", 2131559802));
        }
        if ((availableMapProviders & 0x4) != 0x0) {
            list.add(LocaleController.getString("MapPreviewProviderYandex", 2131559805));
        }
        list.add(LocaleController.getString("MapPreviewProviderNobody", 2131559803));
        final AlertDialog.Builder setItems = new AlertDialog.Builder(context).setTitle(LocaleController.getString("ChooseMapPreviewProvider", 2131559090)).setItems(list.toArray(new String[0]), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$RtoxnZeC0UJlx6OiaOAo9lMRs7E(runnable));
        if (!b) {
            setItems.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        }
        final AlertDialog show = setItems.show();
        if (b) {
            show.setCanceledOnTouchOutside(false);
        }
        return show;
    }
    
    public static void showSendMediaAlert(final int n, final BaseFragment baseFragment) {
        if (n == 0) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)baseFragment.getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", 2131558635));
        if (n == 1) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickers", 2131559380));
        }
        else if (n == 2) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedMedia", 2131559376));
        }
        else if (n == 3) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPolls", 2131559378));
        }
        else if (n == 4) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedStickersAll", 2131559381));
        }
        else if (n == 5) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedMediaAll", 2131559377));
        }
        else if (n == 6) {
            builder.setMessage(LocaleController.getString("ErrorSendRestrictedPollsAll", 2131559379));
        }
        builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
        baseFragment.showDialog(builder.create(), true, null);
    }
    
    public static Dialog showSimpleAlert(final BaseFragment baseFragment, final String s) {
        if (s != null && baseFragment != null && baseFragment.getParentActivity() != null) {
            final AlertDialog create = createSimpleAlert((Context)baseFragment.getParentActivity(), s).create();
            baseFragment.showDialog(create);
            return create;
        }
        return null;
    }
    
    public static Toast showSimpleToast(final BaseFragment baseFragment, final String s) {
        if (s == null) {
            return null;
        }
        Object o;
        if (baseFragment != null && baseFragment.getParentActivity() != null) {
            o = baseFragment.getParentActivity();
        }
        else {
            o = ApplicationLoader.applicationContext;
        }
        final Toast text = Toast.makeText((Context)o, (CharSequence)s, 1);
        text.show();
        return text;
    }
    
    public static AlertDialog showUpdateAppAlert(final Context context, final String message, final boolean b) {
        if (context != null && message != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(LocaleController.getString("AppName", 2131558635));
            builder.setMessage(message);
            builder.setPositiveButton(LocaleController.getString("OK", 2131560097), null);
            if (b) {
                builder.setNegativeButton(LocaleController.getString("UpdateApp", 2131560950), (DialogInterface$OnClickListener)new _$$Lambda$AlertsCreator$msGS4QN_R2Ivdo98cFI__iWFJUI(context));
            }
            return builder.show();
        }
        return null;
    }
    
    private static void updateDayPicker(final NumberPicker numberPicker, final NumberPicker numberPicker2, final NumberPicker numberPicker3) {
        final Calendar instance = Calendar.getInstance();
        instance.set(2, numberPicker2.getValue());
        instance.set(1, numberPicker3.getValue());
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(instance.getActualMaximum(5));
    }
    
    public interface AccountSelectDelegate
    {
        void didSelectAccount(final int p0);
    }
    
    public interface DatePickerDelegate
    {
        void didSelectDate(final int p0, final int p1, final int p2);
    }
    
    public interface PaymentAlertDelegate
    {
        void didPressedNewCard();
    }
}
