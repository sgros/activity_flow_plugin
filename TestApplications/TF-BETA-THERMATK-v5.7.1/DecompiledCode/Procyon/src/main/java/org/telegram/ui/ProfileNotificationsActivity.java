// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.messenger.ChatObject;
import android.os.Build$VERSION;
import android.media.Ringtone;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells2.UserCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import android.text.TextUtils$TruncateAt;
import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.FileLog;
import android.net.Uri;
import android.provider.Settings$System;
import android.os.Parcelable;
import android.media.RingtoneManager;
import android.content.Intent;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.RadioCell;
import android.animation.Animator;
import java.util.ArrayList;
import org.telegram.ui.Cells.TextCheckBoxCell;
import android.content.DialogInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import android.content.SharedPreferences;
import android.content.SharedPreferences$Editor;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.content.Context;
import android.os.Bundle;
import org.telegram.ui.Components.RecyclerListView;
import android.animation.AnimatorSet;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ProfileNotificationsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int done_button = 1;
    private ListAdapter adapter;
    private boolean addingException;
    private AnimatorSet animatorSet;
    private int avatarRow;
    private int avatarSectionRow;
    private int callsRow;
    private int callsVibrateRow;
    private int colorRow;
    private boolean customEnabled;
    private int customInfoRow;
    private int customRow;
    private ProfileNotificationsActivityDelegate delegate;
    private long dialog_id;
    private int enableRow;
    private int generalRow;
    private int ledInfoRow;
    private int ledRow;
    private RecyclerListView listView;
    private boolean notificationsEnabled;
    private int popupDisabledRow;
    private int popupEnabledRow;
    private int popupInfoRow;
    private int popupRow;
    private int priorityInfoRow;
    private int priorityRow;
    private int ringtoneInfoRow;
    private int ringtoneRow;
    private int rowCount;
    private int smartRow;
    private int soundRow;
    private int vibrateRow;
    
    public ProfileNotificationsActivity(final Bundle bundle) {
        super(bundle);
        this.dialog_id = bundle.getLong("dialog_id");
        this.addingException = bundle.getBoolean("exception", false);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    if (!ProfileNotificationsActivity.this.addingException && ProfileNotificationsActivity.this.notificationsEnabled && ProfileNotificationsActivity.this.customEnabled) {
                        final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount).edit();
                        final StringBuilder sb = new StringBuilder();
                        sb.append("notify2_");
                        sb.append(ProfileNotificationsActivity.this.dialog_id);
                        edit.putInt(sb.toString(), 0).commit();
                    }
                }
                else if (n == 1) {
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    final SharedPreferences$Editor edit2 = notificationsSettings.edit();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("custom_");
                    sb2.append(ProfileNotificationsActivity.this.dialog_id);
                    edit2.putBoolean(sb2.toString(), true);
                    final TLRPC.Dialog dialog = (TLRPC.Dialog)MessagesController.getInstance(ProfileNotificationsActivity.this.currentAccount).dialogs_dict.get(ProfileNotificationsActivity.this.dialog_id);
                    if (ProfileNotificationsActivity.this.notificationsEnabled) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("notify2_");
                        sb3.append(ProfileNotificationsActivity.this.dialog_id);
                        edit2.putInt(sb3.toString(), 0);
                        MessagesStorage.getInstance(ProfileNotificationsActivity.this.currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialog_id, 0L);
                        if (dialog != null) {
                            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                        }
                    }
                    else {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("notify2_");
                        sb4.append(ProfileNotificationsActivity.this.dialog_id);
                        edit2.putInt(sb4.toString(), 2);
                        NotificationsController.getInstance(ProfileNotificationsActivity.this.currentAccount).removeNotificationsForDialog(ProfileNotificationsActivity.this.dialog_id);
                        MessagesStorage.getInstance(ProfileNotificationsActivity.this.currentAccount).setDialogFlags(ProfileNotificationsActivity.this.dialog_id, 1L);
                        if (dialog != null) {
                            dialog.notify_settings = new TLRPC.TL_peerNotifySettings();
                            dialog.notify_settings.mute_until = Integer.MAX_VALUE;
                        }
                    }
                    edit2.commit();
                    NotificationsController.getInstance(ProfileNotificationsActivity.this.currentAccount).updateServerNotificationsSettings(ProfileNotificationsActivity.this.dialog_id);
                    if (ProfileNotificationsActivity.this.delegate != null) {
                        final NotificationsSettingsActivity.NotificationException ex = new NotificationsSettingsActivity.NotificationException();
                        ex.did = ProfileNotificationsActivity.this.dialog_id;
                        ex.hasCustom = true;
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("notify2_");
                        sb5.append(ProfileNotificationsActivity.this.dialog_id);
                        ex.notify = notificationsSettings.getInt(sb5.toString(), 0);
                        if (ex.notify != 0) {
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("notifyuntil_");
                            sb6.append(ProfileNotificationsActivity.this.dialog_id);
                            ex.muteUntil = notificationsSettings.getInt(sb6.toString(), 0);
                        }
                        ProfileNotificationsActivity.this.delegate.didCreateNewException(ex);
                    }
                }
                ProfileNotificationsActivity.this.finishFragment();
            }
        });
        if (this.addingException) {
            super.actionBar.setTitle(LocaleController.getString("NotificationsNewException", 2131560077));
            super.actionBar.createMenu().addItemWithWidth(1, 2131165439, AndroidUtilities.dp(56.0f));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("CustomNotifications", 2131559188));
        }
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        frameLayout.addView((View)(this.listView = new RecyclerListView(context)), (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.adapter = new ListAdapter(context));
        this.listView.setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new RecyclerListView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                final int access$1200 = ProfileNotificationsActivity.this.customRow;
                final int n = 0;
                if (i == access$1200 && view instanceof TextCheckBoxCell) {
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    final ProfileNotificationsActivity this$0 = ProfileNotificationsActivity.this;
                    this$0.customEnabled ^= true;
                    final ProfileNotificationsActivity this$2 = ProfileNotificationsActivity.this;
                    this$2.notificationsEnabled = this$2.customEnabled;
                    final SharedPreferences$Editor edit = notificationsSettings.edit();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("custom_");
                    sb.append(ProfileNotificationsActivity.this.dialog_id);
                    edit.putBoolean(sb.toString(), ProfileNotificationsActivity.this.customEnabled).commit();
                    ((TextCheckBoxCell)view).setChecked(ProfileNotificationsActivity.this.customEnabled);
                    final int childCount = ProfileNotificationsActivity.this.listView.getChildCount();
                    final ArrayList<Animator> list = new ArrayList<Animator>();
                    Holder holder;
                    int itemViewType;
                    for (i = n; i < childCount; ++i) {
                        holder = (Holder)ProfileNotificationsActivity.this.listView.getChildViewHolder(ProfileNotificationsActivity.this.listView.getChildAt(i));
                        itemViewType = ((RecyclerView.ViewHolder)holder).getItemViewType();
                        if (((RecyclerView.ViewHolder)holder).getAdapterPosition() != ProfileNotificationsActivity.this.customRow && itemViewType != 0) {
                            if (itemViewType != 1) {
                                if (itemViewType != 2) {
                                    if (itemViewType != 3) {
                                        if (itemViewType == 4) {
                                            ((RadioCell)holder.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, list);
                                        }
                                    }
                                    else {
                                        ((TextColorCell)holder.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, list);
                                    }
                                }
                                else {
                                    ((TextInfoPrivacyCell)holder.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, list);
                                }
                            }
                            else {
                                ((TextSettingsCell)holder.itemView).setEnabled(ProfileNotificationsActivity.this.customEnabled, list);
                            }
                        }
                    }
                    if (!list.isEmpty()) {
                        if (ProfileNotificationsActivity.this.animatorSet != null) {
                            ProfileNotificationsActivity.this.animatorSet.cancel();
                        }
                        ProfileNotificationsActivity.this.animatorSet = new AnimatorSet();
                        ProfileNotificationsActivity.this.animatorSet.playTogether((Collection)list);
                        ProfileNotificationsActivity.this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                            public void onAnimationEnd(final Animator animator) {
                                if (animator.equals(ProfileNotificationsActivity.this.animatorSet)) {
                                    ProfileNotificationsActivity.this.animatorSet = null;
                                }
                            }
                        });
                        ProfileNotificationsActivity.this.animatorSet.setDuration(150L);
                        ProfileNotificationsActivity.this.animatorSet.start();
                    }
                }
                else if (ProfileNotificationsActivity.this.customEnabled) {
                    final int access$1201 = ProfileNotificationsActivity.this.soundRow;
                    final Parcelable parcelable = null;
                    final Parcelable parcelable2 = null;
                    if (i == access$1201) {
                        try {
                            final Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                            intent.putExtra("android.intent.extra.ringtone.TYPE", 2);
                            intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                            intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                            intent.putExtra("android.intent.extra.ringtone.DEFAULT_URI", (Parcelable)RingtoneManager.getDefaultUri(2));
                            final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                            final Uri default_NOTIFICATION_URI = Settings$System.DEFAULT_NOTIFICATION_URI;
                            String path;
                            if (default_NOTIFICATION_URI != null) {
                                path = default_NOTIFICATION_URI.getPath();
                            }
                            else {
                                path = null;
                            }
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("sound_path_");
                            sb2.append(ProfileNotificationsActivity.this.dialog_id);
                            final String string = notificationsSettings2.getString(sb2.toString(), path);
                            Object parse = parcelable2;
                            if (string != null) {
                                parse = parcelable2;
                                if (!string.equals("NoSound")) {
                                    if (string.equals(path)) {
                                        parse = default_NOTIFICATION_URI;
                                    }
                                    else {
                                        parse = Uri.parse(string);
                                    }
                                }
                            }
                            intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", (Parcelable)parse);
                            ProfileNotificationsActivity.this.startActivityForResult(intent, 12);
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                    }
                    else if (i == ProfileNotificationsActivity.this.ringtoneRow) {
                        try {
                            final Intent intent2 = new Intent("android.intent.action.RINGTONE_PICKER");
                            intent2.putExtra("android.intent.extra.ringtone.TYPE", 1);
                            intent2.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                            intent2.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                            intent2.putExtra("android.intent.extra.ringtone.DEFAULT_URI", (Parcelable)RingtoneManager.getDefaultUri(1));
                            final SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                            final Uri default_NOTIFICATION_URI2 = Settings$System.DEFAULT_NOTIFICATION_URI;
                            String path2;
                            if (default_NOTIFICATION_URI2 != null) {
                                path2 = default_NOTIFICATION_URI2.getPath();
                            }
                            else {
                                path2 = null;
                            }
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("ringtone_path_");
                            sb3.append(ProfileNotificationsActivity.this.dialog_id);
                            final String string2 = notificationsSettings3.getString(sb3.toString(), path2);
                            Object parse2 = parcelable;
                            if (string2 != null) {
                                parse2 = parcelable;
                                if (!string2.equals("NoSound")) {
                                    if (string2.equals(path2)) {
                                        parse2 = default_NOTIFICATION_URI2;
                                    }
                                    else {
                                        parse2 = Uri.parse(string2);
                                    }
                                }
                            }
                            intent2.putExtra("android.intent.extra.ringtone.EXISTING_URI", (Parcelable)parse2);
                            ProfileNotificationsActivity.this.startActivityForResult(intent2, 13);
                        }
                        catch (Exception ex2) {
                            FileLog.e(ex2);
                        }
                    }
                    else if (i == ProfileNotificationsActivity.this.vibrateRow) {
                        final ProfileNotificationsActivity this$3 = ProfileNotificationsActivity.this;
                        this$3.showDialog(AlertsCreator.createVibrationSelectDialog(this$3.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, false, false, new _$$Lambda$ProfileNotificationsActivity$3$2IDon9XZ3wORVvesLoqeJ8GuKLA(this)));
                    }
                    else if (i == ProfileNotificationsActivity.this.enableRow) {
                        final TextCheckCell textCheckCell = (TextCheckCell)view;
                        ProfileNotificationsActivity.this.notificationsEnabled = (textCheckCell.isChecked() ^ true);
                        textCheckCell.setChecked(ProfileNotificationsActivity.this.notificationsEnabled);
                    }
                    else if (i == ProfileNotificationsActivity.this.callsVibrateRow) {
                        final ProfileNotificationsActivity this$4 = ProfileNotificationsActivity.this;
                        this$4.showDialog(AlertsCreator.createVibrationSelectDialog(this$4.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, "calls_vibrate_", new _$$Lambda$ProfileNotificationsActivity$3$PrZXv2W2aGcQ_SHFSnntK6Tp69w(this)));
                    }
                    else if (i == ProfileNotificationsActivity.this.priorityRow) {
                        final ProfileNotificationsActivity this$5 = ProfileNotificationsActivity.this;
                        this$5.showDialog(AlertsCreator.createPrioritySelectDialog(this$5.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, -1, new _$$Lambda$ProfileNotificationsActivity$3$mwfzE5pr44nJotHZxBv3FvA5wOc(this)));
                    }
                    else if (i == ProfileNotificationsActivity.this.smartRow) {
                        if (ProfileNotificationsActivity.this.getParentActivity() == null) {
                            return;
                        }
                        final Activity parentActivity = ProfileNotificationsActivity.this.getParentActivity();
                        final SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("smart_max_count_");
                        sb4.append(ProfileNotificationsActivity.this.dialog_id);
                        final int int1 = notificationsSettings4.getInt(sb4.toString(), 2);
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("smart_delay_");
                        sb5.append(ProfileNotificationsActivity.this.dialog_id);
                        final int int2 = notificationsSettings4.getInt(sb5.toString(), 180);
                        if ((i = int1) == 0) {
                            i = 2;
                        }
                        final int n2 = int2 / 60;
                        final RecyclerListView view2 = new RecyclerListView((Context)ProfileNotificationsActivity.this.getParentActivity());
                        view2.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
                        view2.setClipToPadding(true);
                        view2.setAdapter(new SelectionAdapter() {
                            final /* synthetic */ int val$selected = (n2 - 1) * 10 + i - 1;
                            
                            @Override
                            public int getItemCount() {
                                return 100;
                            }
                            
                            @Override
                            public boolean isEnabled(final ViewHolder viewHolder) {
                                return true;
                            }
                            
                            @Override
                            public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
                                final TextView textView = (TextView)viewHolder.itemView;
                                String s;
                                if (n == this.val$selected) {
                                    s = "dialogTextGray";
                                }
                                else {
                                    s = "dialogTextBlack";
                                }
                                textView.setTextColor(Theme.getColor(s));
                                textView.setText((CharSequence)LocaleController.formatString("SmartNotificationsDetail", 2131560790, LocaleController.formatPluralString("Times", n % 10 + 1), LocaleController.formatPluralString("Minutes", n / 10 + 1)));
                            }
                            
                            @Override
                            public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
                                final TextView textView = new TextView(parentActivity) {
                                    protected void onMeasure(final int n, final int n2) {
                                        super.onMeasure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f), 1073741824));
                                    }
                                };
                                textView.setGravity(17);
                                textView.setTextSize(1, 18.0f);
                                textView.setSingleLine(true);
                                textView.setEllipsize(TextUtils$TruncateAt.END);
                                textView.setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
                                return new RecyclerListView.Holder((View)textView);
                            }
                        });
                        view2.setPadding(0, AndroidUtilities.dp(12.0f), 0, AndroidUtilities.dp(8.0f));
                        view2.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ProfileNotificationsActivity$3$NllPggjTkVFXgR__HalhnaOhR0w(this));
                        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)ProfileNotificationsActivity.this.getParentActivity());
                        builder.setTitle(LocaleController.getString("SmartNotificationsAlert", 2131560789));
                        builder.setView((View)view2);
                        builder.setPositiveButton(LocaleController.getString("Cancel", 2131558891), null);
                        builder.setNegativeButton(LocaleController.getString("SmartNotificationsDisabled", 2131560791), (DialogInterface$OnClickListener)new _$$Lambda$ProfileNotificationsActivity$3$C5kBfrMiInE1eRjHc3NVVjNb77Q(this));
                        ProfileNotificationsActivity.this.showDialog(builder.create());
                    }
                    else if (i == ProfileNotificationsActivity.this.colorRow) {
                        if (ProfileNotificationsActivity.this.getParentActivity() == null) {
                            return;
                        }
                        final ProfileNotificationsActivity this$6 = ProfileNotificationsActivity.this;
                        this$6.showDialog(AlertsCreator.createColorSelectDialog(this$6.getParentActivity(), ProfileNotificationsActivity.this.dialog_id, -1, new _$$Lambda$ProfileNotificationsActivity$3$9hbVVL7iBMMo3IfpTFOUhufLFBM(this)));
                    }
                    else if (i == ProfileNotificationsActivity.this.popupEnabledRow) {
                        final SharedPreferences$Editor edit2 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount).edit();
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("popup_");
                        sb6.append(ProfileNotificationsActivity.this.dialog_id);
                        edit2.putInt(sb6.toString(), 1).commit();
                        ((RadioCell)view).setChecked(true, true);
                        view = ProfileNotificationsActivity.this.listView.findViewWithTag((Object)2);
                        if (view != null) {
                            ((RadioCell)view).setChecked(false, true);
                        }
                    }
                    else if (i == ProfileNotificationsActivity.this.popupDisabledRow) {
                        final SharedPreferences$Editor edit3 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount).edit();
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("popup_");
                        sb7.append(ProfileNotificationsActivity.this.dialog_id);
                        edit3.putInt(sb7.toString(), 2).commit();
                        ((RadioCell)view).setChecked(true, true);
                        view = ProfileNotificationsActivity.this.listView.findViewWithTag((Object)1);
                        if (view != null) {
                            ((RadioCell)view).setChecked(false, true);
                        }
                    }
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.notificationsSettingsUpdated) {
            this.adapter.notifyDataSetChanged();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M $$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M = new _$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, TextSettingsCell.class, TextColorCell.class, RadioCell.class, UserCell.class, TextCheckCell.class, TextCheckBoxCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { TextColorCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { RadioCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOX, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackground"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKBOXCHECK, new Class[] { RadioCell.class }, new String[] { "radioButton" }, null, null, null, "radioBackgroundChecked"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ProfileNotificationsActivity$7A70wCk_sskw_wSSS5mOEgsRl6M, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareUnchecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareDisabled"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareBackground"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckBoxCell.class }, null, null, null, "checkboxSquareCheck") };
    }
    
    @Override
    public void onActivityResultFragment(int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            if (intent == null) {
                return;
            }
            final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String s2;
            final String s = s2 = null;
            if (uri != null) {
                final Ringtone ringtone = RingtoneManager.getRingtone(ApplicationLoader.applicationContext, uri);
                s2 = s;
                if (ringtone != null) {
                    if (n == 13) {
                        if (uri.equals((Object)Settings$System.DEFAULT_RINGTONE_URI)) {
                            s2 = LocaleController.getString("DefaultRingtone", 2131559226);
                        }
                        else {
                            s2 = ringtone.getTitle((Context)this.getParentActivity());
                        }
                    }
                    else if (uri.equals((Object)Settings$System.DEFAULT_NOTIFICATION_URI)) {
                        s2 = LocaleController.getString("SoundDefault", 2131560801);
                    }
                    else {
                        s2 = ringtone.getTitle((Context)this.getParentActivity());
                    }
                    ringtone.stop();
                }
            }
            final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(super.currentAccount).edit();
            if (n == 12) {
                if (s2 != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("sound_");
                    sb.append(this.dialog_id);
                    edit.putString(sb.toString(), s2);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("sound_path_");
                    sb2.append(this.dialog_id);
                    edit.putString(sb2.toString(), uri.toString());
                }
                else {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("sound_");
                    sb3.append(this.dialog_id);
                    edit.putString(sb3.toString(), "NoSound");
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("sound_path_");
                    sb4.append(this.dialog_id);
                    edit.putString(sb4.toString(), "NoSound");
                }
            }
            else if (n == 13) {
                if (s2 != null) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("ringtone_");
                    sb5.append(this.dialog_id);
                    edit.putString(sb5.toString(), s2);
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append("ringtone_path_");
                    sb6.append(this.dialog_id);
                    edit.putString(sb6.toString(), uri.toString());
                }
                else {
                    final StringBuilder sb7 = new StringBuilder();
                    sb7.append("ringtone_");
                    sb7.append(this.dialog_id);
                    edit.putString(sb7.toString(), "NoSound");
                    final StringBuilder sb8 = new StringBuilder();
                    sb8.append("ringtone_path_");
                    sb8.append(this.dialog_id);
                    edit.putString(sb8.toString(), "NoSound");
                }
            }
            edit.commit();
            final ListAdapter adapter = this.adapter;
            if (adapter != null) {
                if (n == 13) {
                    n = this.ringtoneRow;
                }
                else {
                    n = this.soundRow;
                }
                ((RecyclerView.Adapter)adapter).notifyItemChanged(n);
            }
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.rowCount = 0;
        if (this.addingException) {
            this.avatarRow = this.rowCount++;
            this.avatarSectionRow = this.rowCount++;
            this.customRow = -1;
            this.customInfoRow = -1;
        }
        else {
            this.avatarRow = -1;
            this.avatarSectionRow = -1;
            this.customRow = this.rowCount++;
            this.customInfoRow = this.rowCount++;
        }
        this.generalRow = this.rowCount++;
        if (this.addingException) {
            this.enableRow = this.rowCount++;
        }
        else {
            this.enableRow = -1;
        }
        this.soundRow = this.rowCount++;
        this.vibrateRow = this.rowCount++;
        if ((int)this.dialog_id < 0) {
            this.smartRow = this.rowCount++;
        }
        else {
            this.smartRow = -1;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.priorityRow = this.rowCount++;
        }
        else {
            this.priorityRow = -1;
        }
        this.priorityInfoRow = this.rowCount++;
        final int n = (int)this.dialog_id;
        boolean b = false;
        Label_0316: {
            if (n < 0) {
                final TLRPC.Chat chat = MessagesController.getInstance(super.currentAccount).getChat(-n);
                if (ChatObject.isChannel(chat) && !chat.megagroup) {
                    b = true;
                    break Label_0316;
                }
            }
            b = false;
        }
        if (n != 0 && !b) {
            this.popupRow = this.rowCount++;
            this.popupEnabledRow = this.rowCount++;
            this.popupDisabledRow = this.rowCount++;
            this.popupInfoRow = this.rowCount++;
        }
        else {
            this.popupRow = -1;
            this.popupEnabledRow = -1;
            this.popupDisabledRow = -1;
            this.popupInfoRow = -1;
        }
        if (n > 0) {
            this.callsRow = this.rowCount++;
            this.callsVibrateRow = this.rowCount++;
            this.ringtoneRow = this.rowCount++;
            this.ringtoneInfoRow = this.rowCount++;
        }
        else {
            this.callsRow = -1;
            this.callsVibrateRow = -1;
            this.ringtoneRow = -1;
            this.ringtoneInfoRow = -1;
        }
        this.ledRow = this.rowCount++;
        this.colorRow = this.rowCount++;
        this.ledInfoRow = this.rowCount++;
        final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(super.currentAccount);
        final StringBuilder sb = new StringBuilder();
        sb.append("custom_");
        sb.append(this.dialog_id);
        this.customEnabled = (notificationsSettings.getBoolean(sb.toString(), false) || this.addingException);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("notify2_");
        sb2.append(this.dialog_id);
        final boolean contains = notificationsSettings.contains(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("notify2_");
        sb3.append(this.dialog_id);
        final int int1 = notificationsSettings.getInt(sb3.toString(), 0);
        if (int1 == 0) {
            if (contains) {
                this.notificationsEnabled = true;
            }
            else {
                this.notificationsEnabled = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.dialog_id);
            }
        }
        else if (int1 == 1) {
            this.notificationsEnabled = true;
        }
        else if (int1 == 2) {
            this.notificationsEnabled = false;
        }
        else {
            this.notificationsEnabled = false;
        }
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }
    
    public void setDelegate(final ProfileNotificationsActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class ListAdapter extends Adapter
    {
        private Context context;
        
        public ListAdapter(final Context context) {
            this.context = context;
        }
        
        @Override
        public int getItemCount() {
            return ProfileNotificationsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ProfileNotificationsActivity.this.generalRow || n == ProfileNotificationsActivity.this.popupRow || n == ProfileNotificationsActivity.this.ledRow || n == ProfileNotificationsActivity.this.callsRow) {
                return 0;
            }
            if (n == ProfileNotificationsActivity.this.soundRow || n == ProfileNotificationsActivity.this.vibrateRow || n == ProfileNotificationsActivity.this.priorityRow || n == ProfileNotificationsActivity.this.smartRow || n == ProfileNotificationsActivity.this.ringtoneRow || n == ProfileNotificationsActivity.this.callsVibrateRow) {
                return 1;
            }
            if (n == ProfileNotificationsActivity.this.popupInfoRow || n == ProfileNotificationsActivity.this.ledInfoRow || n == ProfileNotificationsActivity.this.priorityInfoRow || n == ProfileNotificationsActivity.this.customInfoRow || n == ProfileNotificationsActivity.this.ringtoneInfoRow) {
                return 2;
            }
            if (n == ProfileNotificationsActivity.this.colorRow) {
                return 3;
            }
            if (n == ProfileNotificationsActivity.this.popupEnabledRow || n == ProfileNotificationsActivity.this.popupDisabledRow) {
                return 4;
            }
            if (n == ProfileNotificationsActivity.this.customRow) {
                return 5;
            }
            if (n == ProfileNotificationsActivity.this.avatarRow) {
                return 6;
            }
            if (n == ProfileNotificationsActivity.this.avatarSectionRow) {
                return 7;
            }
            if (n == ProfileNotificationsActivity.this.enableRow) {
                return 8;
            }
            return 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = true;
            final boolean b2 = true;
            final boolean b3 = true;
            final boolean b4 = true;
            final boolean b5 = true;
            final boolean b6 = true;
            final boolean b7 = true;
            final boolean b8 = true;
            boolean b9 = false;
            switch (itemViewType) {
                case 8: {
                    final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                    MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    if (i == ProfileNotificationsActivity.this.enableRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("Notifications", 2131560055), ProfileNotificationsActivity.this.notificationsEnabled, true);
                        break;
                    }
                    break;
                }
                case 6: {
                    final UserCell userCell = (UserCell)viewHolder.itemView;
                    i = (int)ProfileNotificationsActivity.this.dialog_id;
                    TLObject tlObject;
                    if (i > 0) {
                        tlObject = MessagesController.getInstance(ProfileNotificationsActivity.this.currentAccount).getUser(i);
                    }
                    else {
                        tlObject = MessagesController.getInstance(ProfileNotificationsActivity.this.currentAccount).getChat(-i);
                    }
                    userCell.setData(tlObject, null, null, 0);
                    break;
                }
                case 5: {
                    final TextCheckBoxCell textCheckBoxCell = (TextCheckBoxCell)viewHolder.itemView;
                    MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    textCheckBoxCell.setTextAndCheck(LocaleController.getString("NotificationsEnableCustom", 2131560063), ProfileNotificationsActivity.this.customEnabled && ProfileNotificationsActivity.this.notificationsEnabled && b8, false);
                    break;
                }
                case 4: {
                    final RadioCell radioCell = (RadioCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    final StringBuilder sb = new StringBuilder();
                    sb.append("popup_");
                    sb.append(ProfileNotificationsActivity.this.dialog_id);
                    int int1;
                    if ((int1 = notificationsSettings.getInt(sb.toString(), 0)) == 0) {
                        String s;
                        if ((int)ProfileNotificationsActivity.this.dialog_id < 0) {
                            s = "popupGroup";
                        }
                        else {
                            s = "popupAll";
                        }
                        if (notificationsSettings.getInt(s, 0) != 0) {
                            int1 = 1;
                        }
                        else {
                            int1 = 2;
                        }
                    }
                    if (i == ProfileNotificationsActivity.this.popupEnabledRow) {
                        final String string = LocaleController.getString("PopupEnabled", 2131560470);
                        if (int1 == 1) {
                            b9 = true;
                        }
                        radioCell.setText(string, b9, true);
                        radioCell.setTag((Object)1);
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.popupDisabledRow) {
                        radioCell.setText(LocaleController.getString("PopupDisabled", 2131560469), int1 == 2 && b, false);
                        radioCell.setTag((Object)2);
                        break;
                    }
                    break;
                }
                case 3: {
                    final TextColorCell textColorCell = (TextColorCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("color_");
                    sb2.append(ProfileNotificationsActivity.this.dialog_id);
                    if (notificationsSettings2.contains(sb2.toString())) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("color_");
                        sb3.append(ProfileNotificationsActivity.this.dialog_id);
                        i = notificationsSettings2.getInt(sb3.toString(), -16776961);
                    }
                    else if ((int)ProfileNotificationsActivity.this.dialog_id < 0) {
                        i = notificationsSettings2.getInt("GroupLed", -16776961);
                    }
                    else {
                        i = notificationsSettings2.getInt("MessagesLed", -16776961);
                    }
                    int n = 0;
                    int n2;
                    while (true) {
                        n2 = i;
                        if (n >= 9) {
                            break;
                        }
                        if (TextColorCell.colorsToSave[n] == i) {
                            n2 = TextColorCell.colors[n];
                            break;
                        }
                        ++n;
                    }
                    textColorCell.setTextAndColor(LocaleController.getString("NotificationsLedColor", 2131560074), n2, false);
                    break;
                }
                case 2: {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (i == ProfileNotificationsActivity.this.popupInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("ProfilePopupNotificationInfo", 2131560515));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.ledInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("NotificationsLedInfo", 2131560075));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165395, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.priorityInfoRow) {
                        if (ProfileNotificationsActivity.this.priorityRow == -1) {
                            textInfoPrivacyCell.setText("");
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("PriorityInfo", 2131560473));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.customInfoRow) {
                        textInfoPrivacyCell.setText(null);
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.ringtoneInfoRow) {
                        textInfoPrivacyCell.setText(LocaleController.getString("VoipRingtoneInfo", 2131561091));
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.context, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    break;
                }
                case 1: {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(ProfileNotificationsActivity.this.currentAccount);
                    if (i == ProfileNotificationsActivity.this.soundRow) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("sound_");
                        sb4.append(ProfileNotificationsActivity.this.dialog_id);
                        String s2;
                        if ((s2 = notificationsSettings3.getString(sb4.toString(), LocaleController.getString("SoundDefault", 2131560801))).equals("NoSound")) {
                            s2 = LocaleController.getString("NoSound", 2131559952);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("Sound", 2131560800), s2, true);
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.ringtoneRow) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("ringtone_");
                        sb5.append(ProfileNotificationsActivity.this.dialog_id);
                        String s3;
                        if ((s3 = notificationsSettings3.getString(sb5.toString(), LocaleController.getString("DefaultRingtone", 2131559226))).equals("NoSound")) {
                            s3 = LocaleController.getString("NoSound", 2131559952);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131561092), s3, false);
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.vibrateRow) {
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("vibrate_");
                        sb6.append(ProfileNotificationsActivity.this.dialog_id);
                        i = notificationsSettings3.getInt(sb6.toString(), 0);
                        if (i == 0 || i == 4) {
                            final String string2 = LocaleController.getString("Vibrate", 2131561040);
                            final String string3 = LocaleController.getString("VibrationDefault", 2131561041);
                            boolean b10 = b5;
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                b10 = (ProfileNotificationsActivity.this.priorityRow != -1 && b5);
                            }
                            textSettingsCell.setTextAndValue(string2, string3, b10);
                            break;
                        }
                        if (i == 1) {
                            final String string4 = LocaleController.getString("Vibrate", 2131561040);
                            final String string5 = LocaleController.getString("Short", 2131560775);
                            boolean b11 = b2;
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                b11 = (ProfileNotificationsActivity.this.priorityRow != -1 && b2);
                            }
                            textSettingsCell.setTextAndValue(string4, string5, b11);
                            break;
                        }
                        if (i == 2) {
                            final String string6 = LocaleController.getString("Vibrate", 2131561040);
                            final String string7 = LocaleController.getString("VibrationDisabled", 2131561042);
                            boolean b12 = b3;
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                b12 = (ProfileNotificationsActivity.this.priorityRow != -1 && b3);
                            }
                            textSettingsCell.setTextAndValue(string6, string7, b12);
                            break;
                        }
                        if (i == 3) {
                            final String string8 = LocaleController.getString("Vibrate", 2131561040);
                            final String string9 = LocaleController.getString("Long", 2131559790);
                            boolean b13 = b4;
                            if (ProfileNotificationsActivity.this.smartRow == -1) {
                                b13 = (ProfileNotificationsActivity.this.priorityRow != -1 && b4);
                            }
                            textSettingsCell.setTextAndValue(string8, string9, b13);
                            break;
                        }
                        break;
                    }
                    else if (i == ProfileNotificationsActivity.this.priorityRow) {
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("priority_");
                        sb7.append(ProfileNotificationsActivity.this.dialog_id);
                        i = notificationsSettings3.getInt(sb7.toString(), 3);
                        if (i == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityHigh", 2131560082), false);
                            break;
                        }
                        if (i == 1 || i == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityUrgent", 2131560086), false);
                            break;
                        }
                        if (i == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPrioritySettings", 2131560085), false);
                            break;
                        }
                        if (i == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityLow", 2131560083), false);
                            break;
                        }
                        if (i == 5) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityMedium", 2131560084), false);
                            break;
                        }
                        break;
                    }
                    else if (i == ProfileNotificationsActivity.this.smartRow) {
                        final StringBuilder sb8 = new StringBuilder();
                        sb8.append("smart_max_count_");
                        sb8.append(ProfileNotificationsActivity.this.dialog_id);
                        final int int2 = notificationsSettings3.getInt(sb8.toString(), 2);
                        final StringBuilder sb9 = new StringBuilder();
                        sb9.append("smart_delay_");
                        sb9.append(ProfileNotificationsActivity.this.dialog_id);
                        i = notificationsSettings3.getInt(sb9.toString(), 180);
                        if (int2 == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("SmartNotifications", 2131560788), LocaleController.getString("SmartNotificationsDisabled", 2131560791), ProfileNotificationsActivity.this.priorityRow != -1 && b6);
                            break;
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("SmartNotifications", 2131560788), LocaleController.formatString("SmartNotificationsInfo", 2131560792, int2, LocaleController.formatPluralString("Minutes", i / 60)), ProfileNotificationsActivity.this.priorityRow != -1 && b7);
                        break;
                    }
                    else {
                        if (i != ProfileNotificationsActivity.this.callsVibrateRow) {
                            break;
                        }
                        final StringBuilder sb10 = new StringBuilder();
                        sb10.append("calls_vibrate_");
                        sb10.append(ProfileNotificationsActivity.this.dialog_id);
                        i = notificationsSettings3.getInt(sb10.toString(), 0);
                        if (i == 0 || i == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
                            break;
                        }
                        if (i == 1) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
                            break;
                        }
                        if (i == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
                            break;
                        }
                        if (i == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 0: {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (i == ProfileNotificationsActivity.this.generalRow) {
                        headerCell.setText(LocaleController.getString("General", 2131559587));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.popupRow) {
                        headerCell.setText(LocaleController.getString("ProfilePopupNotification", 2131560514));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.ledRow) {
                        headerCell.setText(LocaleController.getString("NotificationsLed", 2131560073));
                        break;
                    }
                    if (i == ProfileNotificationsActivity.this.callsRow) {
                        headerCell.setText(LocaleController.getString("VoipNotificationSettings", 2131561075));
                        break;
                    }
                    break;
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o = null;
            switch (n) {
                default: {
                    o = new TextCheckCell(this.context);
                    ((TextCheckCell)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 7: {
                    o = new ShadowSectionCell(this.context);
                    break;
                }
                case 6: {
                    o = new UserCell(this.context, 4, 0);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 5: {
                    o = new TextCheckBoxCell(this.context);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 4: {
                    o = new RadioCell(this.context);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 3: {
                    o = new TextColorCell(this.context);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 2: {
                    o = new TextInfoPrivacyCell(this.context);
                    break;
                }
                case 1: {
                    o = new TextSettingsCell(this.context);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 0: {
                    o = new HeaderCell(this.context);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
            }
            ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() != 0) {
                final int itemViewType = viewHolder.getItemViewType();
                final boolean b = false;
                final boolean b2 = false;
                final boolean b3 = false;
                final boolean b4 = false;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            if (itemViewType == 4) {
                                final RadioCell radioCell = (RadioCell)viewHolder.itemView;
                                boolean b5 = b4;
                                if (ProfileNotificationsActivity.this.customEnabled) {
                                    b5 = b4;
                                    if (ProfileNotificationsActivity.this.notificationsEnabled) {
                                        b5 = true;
                                    }
                                }
                                radioCell.setEnabled(b5, null);
                            }
                        }
                        else {
                            final TextColorCell textColorCell = (TextColorCell)viewHolder.itemView;
                            boolean b6 = b;
                            if (ProfileNotificationsActivity.this.customEnabled) {
                                b6 = b;
                                if (ProfileNotificationsActivity.this.notificationsEnabled) {
                                    b6 = true;
                                }
                            }
                            textColorCell.setEnabled(b6, null);
                        }
                    }
                    else {
                        final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                        boolean b7 = b2;
                        if (ProfileNotificationsActivity.this.customEnabled) {
                            b7 = b2;
                            if (ProfileNotificationsActivity.this.notificationsEnabled) {
                                b7 = true;
                            }
                        }
                        textInfoPrivacyCell.setEnabled(b7, null);
                    }
                }
                else {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    boolean b8 = b3;
                    if (ProfileNotificationsActivity.this.customEnabled) {
                        b8 = b3;
                        if (ProfileNotificationsActivity.this.notificationsEnabled) {
                            b8 = true;
                        }
                    }
                    textSettingsCell.setEnabled(b8, null);
                }
            }
        }
    }
    
    public interface ProfileNotificationsActivityDelegate
    {
        void didCreateNewException(final NotificationsSettingsActivity.NotificationException p0);
    }
}
