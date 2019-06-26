// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.os.Build$VERSION;
import android.media.Ringtone;
import android.content.DialogInterface;
import android.widget.Toast;
import java.util.Iterator;
import android.text.TextUtils;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Utilities;
import java.util.Map;
import org.telegram.messenger.UserConfig;
import android.util.LongSparseArray;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.FileLog;
import android.net.Uri;
import android.provider.Settings$System;
import android.os.Parcelable;
import android.media.RingtoneManager;
import android.content.Intent;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.AndroidUtilities;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class NotificationsSettingsActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int accountsAllRow;
    private int accountsInfoRow;
    private int accountsSectionRow;
    private ListAdapter adapter;
    private int androidAutoAlertRow;
    private int badgeNumberMessagesRow;
    private int badgeNumberMutedRow;
    private int badgeNumberSection;
    private int badgeNumberSection2Row;
    private int badgeNumberShowRow;
    private int callsRingtoneRow;
    private int callsSection2Row;
    private int callsSectionRow;
    private int callsVibrateRow;
    private int channelsRow;
    private int contactJoinedRow;
    private int eventsSection2Row;
    private int eventsSectionRow;
    private ArrayList<NotificationException> exceptionChannels;
    private ArrayList<NotificationException> exceptionChats;
    private ArrayList<NotificationException> exceptionUsers;
    private int groupRow;
    private int inappPreviewRow;
    private int inappPriorityRow;
    private int inappSectionRow;
    private int inappSoundRow;
    private int inappVibrateRow;
    private int inchatSoundRow;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private int notificationsSection2Row;
    private int notificationsSectionRow;
    private int notificationsServiceConnectionRow;
    private int notificationsServiceRow;
    private int otherSection2Row;
    private int otherSectionRow;
    private int pinnedMessageRow;
    private int privateRow;
    private int repeatRow;
    private int resetNotificationsRow;
    private int resetSection2Row;
    private int resetSectionRow;
    private boolean reseting;
    private int rowCount;
    
    public NotificationsSettingsActivity() {
        this.reseting = false;
        this.exceptionUsers = null;
        this.exceptionChats = null;
        this.exceptionChannels = null;
        this.rowCount = 0;
    }
    
    private void loadExceptions() {
        MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$NotificationsSettingsActivity$uXiQpIA5YYdlg9kVau5gYRsTuCM(this));
    }
    
    private void showExceptionsAlert(final int n) {
        ArrayList<NotificationException> list = null;
        String s = null;
        Label_0136: {
            if (n == this.privateRow) {
                final ArrayList<NotificationException> exceptionUsers = this.exceptionUsers;
                if ((list = exceptionUsers) != null) {
                    list = exceptionUsers;
                    if (!exceptionUsers.isEmpty()) {
                        s = LocaleController.formatPluralString("ChatsException", exceptionUsers.size());
                        list = exceptionUsers;
                        break Label_0136;
                    }
                }
            }
            else if (n == this.groupRow) {
                final ArrayList<NotificationException> exceptionChats = this.exceptionChats;
                if ((list = exceptionChats) != null) {
                    list = exceptionChats;
                    if (!exceptionChats.isEmpty()) {
                        s = LocaleController.formatPluralString("Groups", exceptionChats.size());
                        list = exceptionChats;
                        break Label_0136;
                    }
                }
            }
            else {
                final ArrayList<NotificationException> exceptionChannels = this.exceptionChannels;
                if ((list = exceptionChannels) != null) {
                    list = exceptionChannels;
                    if (!exceptionChannels.isEmpty()) {
                        final String formatPluralString = LocaleController.formatPluralString("Channels", exceptionChannels.size());
                        list = exceptionChannels;
                        s = formatPluralString;
                        break Label_0136;
                    }
                }
            }
            s = null;
        }
        if (s == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        if (list.size() == 1) {
            builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsSingleAlert", 2131560066, s)));
        }
        else {
            builder.setMessage((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("NotificationsExceptionsAlert", 2131560065, s)));
        }
        builder.setTitle(LocaleController.getString("NotificationsExceptions", 2131560064));
        builder.setNeutralButton(LocaleController.getString("ViewExceptions", 2131561053), (DialogInterface$OnClickListener)new _$$Lambda$NotificationsSettingsActivity$9FhV71oy8_vyXyR3LWFGjX_RReE(this, list));
        builder.setNegativeButton(LocaleController.getString("OK", 2131560097), null);
        this.showDialog(builder.create());
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("NotificationsAndSounds", 2131560057));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    NotificationsSettingsActivity.this.finishFragment();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.adapter = new ListAdapter(context));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$NotificationsSettingsActivity$g4GVhVtYkD5_YPczTHGsTY58dkg(this));
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
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, TextCheckCell.class, TextDetailSettingsCell.class, TextSettingsCell.class, NotificationsCheckCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextDetailSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteLinkText") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String s2;
            final String s = s2 = null;
            if (uri != null) {
                final Ringtone ringtone = RingtoneManager.getRingtone((Context)this.getParentActivity(), uri);
                s2 = s;
                if (ringtone != null) {
                    if (n == this.callsRingtoneRow) {
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
            if (n == this.callsRingtoneRow) {
                if (s2 != null && uri != null) {
                    edit.putString("CallsRingtone", s2);
                    edit.putString("CallsRingtonePath", uri.toString());
                }
                else {
                    edit.putString("CallsRingtone", "NoSound");
                    edit.putString("CallsRingtonePath", "NoSound");
                }
            }
            edit.commit();
            this.adapter.notifyItemChanged(n);
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        MessagesController.getInstance(super.currentAccount).loadSignUpNotificationsSettings();
        this.loadExceptions();
        if (UserConfig.getActivatedAccountsCount() > 1) {
            this.accountsSectionRow = this.rowCount++;
            this.accountsAllRow = this.rowCount++;
            this.accountsInfoRow = this.rowCount++;
        }
        else {
            this.accountsSectionRow = -1;
            this.accountsAllRow = -1;
            this.accountsInfoRow = -1;
        }
        this.notificationsSectionRow = this.rowCount++;
        this.privateRow = this.rowCount++;
        this.groupRow = this.rowCount++;
        this.channelsRow = this.rowCount++;
        this.notificationsSection2Row = this.rowCount++;
        this.callsSectionRow = this.rowCount++;
        this.callsVibrateRow = this.rowCount++;
        this.callsRingtoneRow = this.rowCount++;
        this.eventsSection2Row = this.rowCount++;
        this.badgeNumberSection = this.rowCount++;
        this.badgeNumberShowRow = this.rowCount++;
        this.badgeNumberMutedRow = this.rowCount++;
        this.badgeNumberMessagesRow = this.rowCount++;
        this.badgeNumberSection2Row = this.rowCount++;
        this.inappSectionRow = this.rowCount++;
        this.inappSoundRow = this.rowCount++;
        this.inappVibrateRow = this.rowCount++;
        this.inappPreviewRow = this.rowCount++;
        this.inchatSoundRow = this.rowCount++;
        if (Build$VERSION.SDK_INT >= 21) {
            this.inappPriorityRow = this.rowCount++;
        }
        else {
            this.inappPriorityRow = -1;
        }
        this.callsSection2Row = this.rowCount++;
        this.eventsSectionRow = this.rowCount++;
        this.contactJoinedRow = this.rowCount++;
        this.pinnedMessageRow = this.rowCount++;
        this.otherSection2Row = this.rowCount++;
        this.otherSectionRow = this.rowCount++;
        this.notificationsServiceConnectionRow = this.rowCount++;
        this.androidAutoAlertRow = -1;
        this.repeatRow = this.rowCount++;
        this.resetSection2Row = this.rowCount++;
        this.resetSectionRow = this.rowCount++;
        this.resetNotificationsRow = this.rowCount++;
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        return super.onFragmentCreate();
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter adapter = this.adapter;
        if (adapter != null) {
            ((RecyclerView.Adapter)adapter).notifyDataSetChanged();
        }
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return NotificationsSettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == NotificationsSettingsActivity.this.eventsSectionRow || n == NotificationsSettingsActivity.this.otherSectionRow || n == NotificationsSettingsActivity.this.resetSectionRow || n == NotificationsSettingsActivity.this.callsSectionRow || n == NotificationsSettingsActivity.this.badgeNumberSection || n == NotificationsSettingsActivity.this.inappSectionRow || n == NotificationsSettingsActivity.this.notificationsSectionRow || n == NotificationsSettingsActivity.this.accountsSectionRow) {
                return 0;
            }
            if (n == NotificationsSettingsActivity.this.inappSoundRow || n == NotificationsSettingsActivity.this.inappVibrateRow || n == NotificationsSettingsActivity.this.notificationsServiceConnectionRow || n == NotificationsSettingsActivity.this.inappPreviewRow || n == NotificationsSettingsActivity.this.contactJoinedRow || n == NotificationsSettingsActivity.this.pinnedMessageRow || n == NotificationsSettingsActivity.this.notificationsServiceRow || n == NotificationsSettingsActivity.this.badgeNumberMutedRow || n == NotificationsSettingsActivity.this.badgeNumberMessagesRow || n == NotificationsSettingsActivity.this.badgeNumberShowRow || n == NotificationsSettingsActivity.this.inappPriorityRow || n == NotificationsSettingsActivity.this.inchatSoundRow || n == NotificationsSettingsActivity.this.androidAutoAlertRow || n == NotificationsSettingsActivity.this.accountsAllRow) {
                return 1;
            }
            if (n == NotificationsSettingsActivity.this.resetNotificationsRow) {
                return 2;
            }
            if (n == NotificationsSettingsActivity.this.privateRow || n == NotificationsSettingsActivity.this.groupRow || n == NotificationsSettingsActivity.this.channelsRow) {
                return 3;
            }
            if (n == NotificationsSettingsActivity.this.eventsSection2Row || n == NotificationsSettingsActivity.this.notificationsSection2Row || n == NotificationsSettingsActivity.this.otherSection2Row || n == NotificationsSettingsActivity.this.resetSection2Row || n == NotificationsSettingsActivity.this.callsSection2Row || n == NotificationsSettingsActivity.this.badgeNumberSection2Row) {
                return 4;
            }
            if (n == NotificationsSettingsActivity.this.accountsInfoRow) {
                return 6;
            }
            return 5;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition != NotificationsSettingsActivity.this.notificationsSectionRow && adapterPosition != NotificationsSettingsActivity.this.notificationsSection2Row && adapterPosition != NotificationsSettingsActivity.this.inappSectionRow && adapterPosition != NotificationsSettingsActivity.this.eventsSectionRow && adapterPosition != NotificationsSettingsActivity.this.otherSectionRow && adapterPosition != NotificationsSettingsActivity.this.resetSectionRow && adapterPosition != NotificationsSettingsActivity.this.badgeNumberSection && adapterPosition != NotificationsSettingsActivity.this.otherSection2Row && adapterPosition != NotificationsSettingsActivity.this.resetSection2Row && adapterPosition != NotificationsSettingsActivity.this.callsSection2Row && adapterPosition != NotificationsSettingsActivity.this.callsSectionRow && adapterPosition != NotificationsSettingsActivity.this.badgeNumberSection2Row && adapterPosition != NotificationsSettingsActivity.this.accountsSectionRow && adapterPosition != NotificationsSettingsActivity.this.accountsInfoRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int int1) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                int int2 = 0;
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType != 3) {
                            if (itemViewType != 5) {
                                if (itemViewType == 6) {
                                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                                    if (int1 == NotificationsSettingsActivity.this.accountsInfoRow) {
                                        textInfoPrivacyCell.setText(LocaleController.getString("ShowNotificationsForInfo", 2131560783));
                                    }
                                }
                            }
                            else {
                                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                                final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                                if (int1 == NotificationsSettingsActivity.this.callsRingtoneRow) {
                                    String s;
                                    if ((s = notificationsSettings.getString("CallsRingtone", LocaleController.getString("DefaultRingtone", 2131559226))).equals("NoSound")) {
                                        s = LocaleController.getString("NoSound", 2131559952);
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("VoipSettingsRingtone", 2131561092), s, false);
                                }
                                else if (int1 == NotificationsSettingsActivity.this.callsVibrateRow) {
                                    if (int1 == NotificationsSettingsActivity.this.callsVibrateRow) {
                                        int2 = notificationsSettings.getInt("vibrate_calls", 0);
                                    }
                                    if (int2 == 0) {
                                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
                                    }
                                    else if (int2 == 1) {
                                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
                                    }
                                    else if (int2 == 2) {
                                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
                                    }
                                    else if (int2 == 3) {
                                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
                                    }
                                    else if (int2 == 4) {
                                        textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("OnlyIfSilent", 2131560107), true);
                                    }
                                }
                                else if (int1 == NotificationsSettingsActivity.this.repeatRow) {
                                    int1 = notificationsSettings.getInt("repeat_messages", 60);
                                    String s2;
                                    if (int1 == 0) {
                                        s2 = LocaleController.getString("RepeatNotificationsNever", 2131560564);
                                    }
                                    else if (int1 < 60) {
                                        s2 = LocaleController.formatPluralString("Minutes", int1);
                                    }
                                    else {
                                        s2 = LocaleController.formatPluralString("Hours", int1 / 60);
                                    }
                                    textSettingsCell.setTextAndValue(LocaleController.getString("RepeatNotifications", 2131560563), s2, false);
                                }
                            }
                        }
                        else {
                            final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                            final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                            final int currentTime = ConnectionsManager.getInstance(NotificationsSettingsActivity.this.currentAccount).getCurrentTime();
                            String s3;
                            ArrayList list;
                            int n;
                            if (int1 == NotificationsSettingsActivity.this.privateRow) {
                                s3 = LocaleController.getString("NotificationsPrivateChats", 2131560087);
                                list = NotificationsSettingsActivity.this.exceptionUsers;
                                n = notificationsSettings2.getInt("EnableAll2", 0);
                            }
                            else if (int1 == NotificationsSettingsActivity.this.groupRow) {
                                s3 = LocaleController.getString("NotificationsGroups", 2131560071);
                                list = NotificationsSettingsActivity.this.exceptionChats;
                                n = notificationsSettings2.getInt("EnableGroup2", 0);
                            }
                            else {
                                s3 = LocaleController.getString("NotificationsChannels", 2131560058);
                                list = NotificationsSettingsActivity.this.exceptionChannels;
                                n = notificationsSettings2.getInt("EnableChannel2", 0);
                            }
                            boolean b = n < currentTime;
                            int n2;
                            if (!b && n - 31536000 < currentTime) {
                                n2 = 2;
                            }
                            else {
                                n2 = 0;
                            }
                            final StringBuilder sb = new StringBuilder();
                            if (list != null && !list.isEmpty()) {
                                b = (n < currentTime);
                                if (b) {
                                    sb.append(LocaleController.getString("NotificationsOn", 2131560080));
                                }
                                else if (n - 31536000 >= currentTime) {
                                    sb.append(LocaleController.getString("NotificationsOff", 2131560078));
                                }
                                else {
                                    sb.append(LocaleController.formatString("NotificationsOffUntil", 2131560079, LocaleController.stringForMessageListDate(n)));
                                }
                                if (sb.length() != 0) {
                                    sb.append(", ");
                                }
                                sb.append(LocaleController.formatPluralString("Exception", list.size()));
                            }
                            else {
                                sb.append(LocaleController.getString("TapToChange", 2131560859));
                            }
                            notificationsCheckCell.setTextAndValueAndCheck(s3, sb, b, n2, int1 != NotificationsSettingsActivity.this.channelsRow);
                        }
                    }
                    else {
                        final TextDetailSettingsCell textDetailSettingsCell = (TextDetailSettingsCell)viewHolder.itemView;
                        textDetailSettingsCell.setMultilineDetail(true);
                        if (int1 == NotificationsSettingsActivity.this.resetNotificationsRow) {
                            textDetailSettingsCell.setTextAndValue(LocaleController.getString("ResetAllNotifications", 2131560589), LocaleController.getString("UndoAllCustom", 2131560935), false);
                        }
                    }
                }
                else {
                    final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(NotificationsSettingsActivity.this.currentAccount);
                    if (int1 == NotificationsSettingsActivity.this.inappSoundRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("InAppSounds", 2131559659), notificationsSettings3.getBoolean("EnableInAppSounds", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.inappVibrateRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("InAppVibrate", 2131559660), notificationsSettings3.getBoolean("EnableInAppVibrate", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.inappPreviewRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("InAppPreview", 2131559658), notificationsSettings3.getBoolean("EnableInAppPreview", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.inappPriorityRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("NotificationsImportance", 2131560072), notificationsSettings3.getBoolean("EnableInAppPriority", false), false);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.contactJoinedRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("ContactJoined", 2131559144), notificationsSettings3.getBoolean("EnableContactJoined", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.pinnedMessageRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("PinnedMessages", 2131560452), notificationsSettings3.getBoolean("PinnedMessages", true), false);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.androidAutoAlertRow) {
                        textCheckCell.setTextAndCheck("Android Auto", notificationsSettings3.getBoolean("EnableAutoNotifications", false), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.notificationsServiceConnectionRow) {
                        textCheckCell.setTextAndValueAndCheck(LocaleController.getString("NotificationsServiceConnection", 2131560089), "You won't be notified of new messages, if you disable this", notificationsSettings3.getBoolean("pushConnection", true), true, true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.badgeNumberShowRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberShow", 2131558828), NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeNumber, true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.badgeNumberMutedRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberMutedChats", 2131558827), NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeMuted, true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.badgeNumberMessagesRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("BadgeNumberUnread", 2131558829), NotificationsController.getInstance(NotificationsSettingsActivity.this.currentAccount).showBadgeMessages, false);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.inchatSoundRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("InChatSound", 2131559661), notificationsSettings3.getBoolean("EnableInChatSound", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.callsVibrateRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("Vibrate", 2131561040), notificationsSettings3.getBoolean("EnableCallVibrate", true), true);
                    }
                    else if (int1 == NotificationsSettingsActivity.this.accountsAllRow) {
                        textCheckCell.setTextAndCheck(LocaleController.getString("AllAccounts", 2131558601), MessagesController.getGlobalNotificationsSettings().getBoolean("AllAccounts", true), false);
                    }
                }
            }
            else {
                final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                if (int1 == NotificationsSettingsActivity.this.notificationsSectionRow) {
                    headerCell.setText(LocaleController.getString("NotificationsForChats", 2131560068));
                }
                else if (int1 == NotificationsSettingsActivity.this.inappSectionRow) {
                    headerCell.setText(LocaleController.getString("InAppNotifications", 2131559657));
                }
                else if (int1 == NotificationsSettingsActivity.this.eventsSectionRow) {
                    headerCell.setText(LocaleController.getString("Events", 2131559468));
                }
                else if (int1 == NotificationsSettingsActivity.this.otherSectionRow) {
                    headerCell.setText(LocaleController.getString("NotificationsOther", 2131560081));
                }
                else if (int1 == NotificationsSettingsActivity.this.resetSectionRow) {
                    headerCell.setText(LocaleController.getString("Reset", 2131560583));
                }
                else if (int1 == NotificationsSettingsActivity.this.callsSectionRow) {
                    headerCell.setText(LocaleController.getString("VoipNotificationSettings", 2131561075));
                }
                else if (int1 == NotificationsSettingsActivity.this.badgeNumberSection) {
                    headerCell.setText(LocaleController.getString("BadgeNumber", 2131558826));
                }
                else if (int1 == NotificationsSettingsActivity.this.accountsSectionRow) {
                    headerCell.setText(LocaleController.getString("ShowNotificationsFor", 2131560782));
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        if (n != 3) {
                            if (n != 4) {
                                if (n != 5) {
                                    o = new TextInfoPrivacyCell(this.mContext);
                                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                                }
                                else {
                                    o = new TextSettingsCell(this.mContext);
                                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                                }
                            }
                            else {
                                o = new ShadowSectionCell(this.mContext);
                            }
                        }
                        else {
                            o = new NotificationsCheckCell(this.mContext);
                            ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                    }
                    else {
                        o = new TextDetailSettingsCell(this.mContext);
                        ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    o = new TextCheckCell(this.mContext);
                    ((TextCheckCell)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                o = new HeaderCell(this.mContext);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
    
    public static class NotificationException
    {
        public long did;
        public boolean hasCustom;
        public int muteUntil;
        public int notify;
    }
}
