// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.util.TimerTask;
import org.telegram.messenger.ContactsController;
import java.util.Timer;
import android.view.ViewGroup;
import android.media.Ringtone;
import org.telegram.tgnet.ConnectionsManager;
import java.util.Iterator;
import android.text.TextUtils;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.Utilities;
import java.util.Map;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import android.util.LongSparseArray;
import android.content.SharedPreferences$Editor;
import android.content.SharedPreferences;
import org.telegram.messenger.FileLog;
import android.net.Uri;
import android.provider.Settings$System;
import android.os.Parcelable;
import android.media.RingtoneManager;
import android.content.Intent;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.AlertsCreator;
import android.os.Bundle;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.messenger.AndroidUtilities;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import android.os.Build$VERSION;
import org.telegram.messenger.MessagesStorage;
import android.animation.Animator$AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import java.util.Collection;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.messenger.NotificationsController;
import android.animation.Animator;
import org.telegram.ui.Components.RecyclerListView;
import java.util.ArrayList;
import org.telegram.ui.Components.EmptyTextProgressView;
import android.animation.AnimatorSet;
import org.telegram.ui.ActionBar.BaseFragment;

public class NotificationsCustomSettingsActivity extends BaseFragment
{
    private static final int search_button = 0;
    private ListAdapter adapter;
    private int alertRow;
    private int alertSection2Row;
    private AnimatorSet animatorSet;
    private int currentType;
    private EmptyTextProgressView emptyView;
    private ArrayList<NotificationsSettingsActivity.NotificationException> exceptions;
    private int exceptionsAddRow;
    private int exceptionsEndRow;
    private int exceptionsSection2Row;
    private int exceptionsStartRow;
    private int groupSection2Row;
    private RecyclerListView listView;
    private int messageLedRow;
    private int messagePopupNotificationRow;
    private int messagePriorityRow;
    private int messageSectionRow;
    private int messageSoundRow;
    private int messageVibrateRow;
    private int previewRow;
    private int rowCount;
    private SearchAdapter searchListViewAdapter;
    private boolean searchWas;
    private boolean searching;
    
    public NotificationsCustomSettingsActivity(final int n, final ArrayList<NotificationsSettingsActivity.NotificationException> list) {
        this(n, list, false);
    }
    
    public NotificationsCustomSettingsActivity(final int currentType, final ArrayList<NotificationsSettingsActivity.NotificationException> exceptions, final boolean b) {
        this.rowCount = 0;
        this.currentType = currentType;
        this.exceptions = exceptions;
        if (b) {
            this.loadExceptions();
        }
    }
    
    private void checkRowsEnabled() {
        if (!this.exceptions.isEmpty()) {
            return;
        }
        final int childCount = this.listView.getChildCount();
        final ArrayList<Animator> list = new ArrayList<Animator>();
        final boolean globalNotificationsEnabled = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.currentType);
        for (int i = 0; i < childCount; ++i) {
            final RecyclerListView.Holder holder = (RecyclerListView.Holder)this.listView.getChildViewHolder(this.listView.getChildAt(i));
            final int itemViewType = ((RecyclerView.ViewHolder)holder).getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 3) {
                        if (itemViewType == 5) {
                            ((TextSettingsCell)holder.itemView).setEnabled(globalNotificationsEnabled, list);
                        }
                    }
                    else {
                        ((TextColorCell)holder.itemView).setEnabled(globalNotificationsEnabled, list);
                    }
                }
                else {
                    ((TextCheckCell)holder.itemView).setEnabled(globalNotificationsEnabled, list);
                }
            }
            else {
                final HeaderCell headerCell = (HeaderCell)holder.itemView;
                if (((RecyclerView.ViewHolder)holder).getAdapterPosition() == this.messageSectionRow) {
                    headerCell.setEnabled(globalNotificationsEnabled, list);
                }
            }
        }
        if (!list.isEmpty()) {
            final AnimatorSet animatorSet = this.animatorSet;
            if (animatorSet != null) {
                animatorSet.cancel();
            }
            (this.animatorSet = new AnimatorSet()).playTogether((Collection)list);
            this.animatorSet.addListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    if (animator.equals(NotificationsCustomSettingsActivity.this.animatorSet)) {
                        NotificationsCustomSettingsActivity.this.animatorSet = null;
                    }
                }
            });
            this.animatorSet.setDuration(150L);
            this.animatorSet.start();
        }
    }
    
    private void loadExceptions() {
        MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$NotificationsCustomSettingsActivity$1DHOVp0GIYy95W4ah22F_PhCl_A(this));
    }
    
    private void updateRows() {
        this.rowCount = 0;
        final int currentType = this.currentType;
        if (currentType != -1) {
            this.alertRow = this.rowCount++;
            this.alertSection2Row = this.rowCount++;
            this.messageSectionRow = this.rowCount++;
            this.previewRow = this.rowCount++;
            this.messageLedRow = this.rowCount++;
            this.messageVibrateRow = this.rowCount++;
            if (currentType == 2) {
                this.messagePopupNotificationRow = -1;
            }
            else {
                this.messagePopupNotificationRow = this.rowCount++;
            }
            this.messageSoundRow = this.rowCount++;
            if (Build$VERSION.SDK_INT >= 21) {
                this.messagePriorityRow = this.rowCount++;
            }
            else {
                this.messagePriorityRow = -1;
            }
            this.groupSection2Row = this.rowCount++;
            this.exceptionsAddRow = this.rowCount++;
        }
        else {
            this.alertRow = -1;
            this.alertSection2Row = -1;
            this.messageSectionRow = -1;
            this.previewRow = -1;
            this.messageLedRow = -1;
            this.messageVibrateRow = -1;
            this.messagePopupNotificationRow = -1;
            this.messageSoundRow = -1;
            this.messagePriorityRow = -1;
            this.groupSection2Row = -1;
            this.exceptionsAddRow = -1;
        }
        final ArrayList<NotificationsSettingsActivity.NotificationException> exceptions = this.exceptions;
        if (exceptions != null && !exceptions.isEmpty()) {
            final int rowCount = this.rowCount;
            this.exceptionsStartRow = rowCount;
            this.rowCount = rowCount + this.exceptions.size();
            this.exceptionsEndRow = this.rowCount;
        }
        else {
            this.exceptionsStartRow = -1;
            this.exceptionsEndRow = -1;
        }
        if (this.currentType == -1) {
            final ArrayList<NotificationsSettingsActivity.NotificationException> exceptions2 = this.exceptions;
            if (exceptions2 == null || exceptions2.isEmpty()) {
                this.exceptionsSection2Row = -1;
                return;
            }
        }
        this.exceptionsSection2Row = this.rowCount++;
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == -1) {
            super.actionBar.setTitle(LocaleController.getString("NotificationsExceptions", 2131560064));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("Notifications", 2131560055));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    NotificationsCustomSettingsActivity.this.finishFragment();
                }
            }
        });
        final ArrayList<NotificationsSettingsActivity.NotificationException> exceptions = this.exceptions;
        if (exceptions != null && !exceptions.isEmpty()) {
            super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
                @Override
                public void onSearchCollapse() {
                    NotificationsCustomSettingsActivity.this.searchListViewAdapter.searchDialogs(null);
                    NotificationsCustomSettingsActivity.this.searching = false;
                    NotificationsCustomSettingsActivity.this.searchWas = false;
                    NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoExceptions", 2131559923));
                    NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.adapter);
                    NotificationsCustomSettingsActivity.this.adapter.notifyDataSetChanged();
                    NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(true);
                    NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(false);
                    NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(false);
                }
                
                @Override
                public void onSearchExpand() {
                    NotificationsCustomSettingsActivity.this.searching = true;
                    NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(true);
                }
                
                @Override
                public void onTextChanged(final EditText editText) {
                    if (NotificationsCustomSettingsActivity.this.searchListViewAdapter == null) {
                        return;
                    }
                    final String string = editText.getText().toString();
                    if (string.length() != 0) {
                        NotificationsCustomSettingsActivity.this.searchWas = true;
                        if (NotificationsCustomSettingsActivity.this.listView != null) {
                            NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                            NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.searchListViewAdapter);
                            NotificationsCustomSettingsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                            NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(false);
                            NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(true);
                        }
                    }
                    NotificationsCustomSettingsActivity.this.searchListViewAdapter.searchDialogs(string);
                }
            }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        }
        this.searchListViewAdapter = new SearchAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.emptyView = new EmptyTextProgressView(context)).setTextSize(18);
        this.emptyView.setText(LocaleController.getString("NoExceptions", 2131559923));
        this.emptyView.showTextView();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.adapter = new ListAdapter(context));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)new _$$Lambda$NotificationsCustomSettingsActivity$6X_KPwufVk5Y33wBeKoVyvVbszw(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int n) {
                if (n == 1 && NotificationsCustomSettingsActivity.this.searching && NotificationsCustomSettingsActivity.this.searchWas) {
                    AndroidUtilities.hideKeyboard(NotificationsCustomSettingsActivity.this.getParentActivity().getCurrentFocus());
                }
            }
            
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                super.onScrolled(recyclerView, n, n2);
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc $$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc = new _$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { HeaderCell.class, TextCheckCell.class, TextColorCell.class, TextSettingsCell.class, UserCell.class, NotificationsCheckCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { TextCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { UserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { NotificationsCheckCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked"), new ThemeDescription((View)this.listView, 0, new Class[] { TextColorCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteValueText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueButton"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { TextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteBlueIcon") };
    }
    
    @Override
    public void onActivityResultFragment(final int n, int currentType, final Intent intent) {
        if (currentType == -1) {
            final Uri uri = (Uri)intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            String s2;
            final String s = s2 = null;
            if (uri != null) {
                final Ringtone ringtone = RingtoneManager.getRingtone((Context)this.getParentActivity(), uri);
                s2 = s;
                if (ringtone != null) {
                    if (uri.equals((Object)Settings$System.DEFAULT_NOTIFICATION_URI)) {
                        s2 = LocaleController.getString("SoundDefault", 2131560801);
                    }
                    else {
                        s2 = ringtone.getTitle((Context)this.getParentActivity());
                    }
                    ringtone.stop();
                }
            }
            final SharedPreferences$Editor edit = MessagesController.getNotificationsSettings(super.currentAccount).edit();
            currentType = this.currentType;
            if (currentType == 1) {
                if (s2 != null && uri != null) {
                    edit.putString("GlobalSound", s2);
                    edit.putString("GlobalSoundPath", uri.toString());
                }
                else {
                    edit.putString("GlobalSound", "NoSound");
                    edit.putString("GlobalSoundPath", "NoSound");
                }
            }
            else if (currentType == 0) {
                if (s2 != null && uri != null) {
                    edit.putString("GroupSound", s2);
                    edit.putString("GroupSoundPath", uri.toString());
                }
                else {
                    edit.putString("GroupSound", "NoSound");
                    edit.putString("GroupSoundPath", "NoSound");
                }
            }
            else if (currentType == 2) {
                if (s2 != null && uri != null) {
                    edit.putString("ChannelSound", s2);
                    edit.putString("ChannelSoundPath", uri.toString());
                }
                else {
                    edit.putString("ChannelSound", "NoSound");
                    edit.putString("ChannelSoundPath", "NoSound");
                }
            }
            edit.commit();
            NotificationsController.getInstance(super.currentAccount).updateServerNotificationsSettings(this.currentType);
            final RecyclerView.ViewHolder viewHolderForAdapterPosition = this.listView.findViewHolderForAdapterPosition(n);
            if (viewHolderForAdapterPosition != null) {
                this.adapter.onBindViewHolder(viewHolderForAdapterPosition, n);
            }
        }
    }
    
    @Override
    public boolean onFragmentCreate() {
        this.updateRows();
        return super.onFragmentCreate();
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
            return NotificationsCustomSettingsActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                return 0;
            }
            if (n == NotificationsCustomSettingsActivity.this.previewRow) {
                return 1;
            }
            if (n >= NotificationsCustomSettingsActivity.this.exceptionsStartRow && n < NotificationsCustomSettingsActivity.this.exceptionsEndRow) {
                return 2;
            }
            if (n == NotificationsCustomSettingsActivity.this.messageLedRow) {
                return 3;
            }
            if (n == NotificationsCustomSettingsActivity.this.groupSection2Row || n == NotificationsCustomSettingsActivity.this.alertSection2Row || n == NotificationsCustomSettingsActivity.this.exceptionsSection2Row) {
                return 4;
            }
            if (n == NotificationsCustomSettingsActivity.this.alertRow) {
                return 6;
            }
            if (n == NotificationsCustomSettingsActivity.this.exceptionsAddRow) {
                return 7;
            }
            return 5;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            return itemViewType != 0 && itemViewType != 4;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int n) {
            final int itemViewType = viewHolder.getItemViewType();
            int n2 = 0;
            final boolean b = false;
            boolean b2 = false;
            switch (itemViewType) {
                case 7: {
                    final TextCell textCell = (TextCell)viewHolder.itemView;
                    textCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                    if (n == NotificationsCustomSettingsActivity.this.exceptionsAddRow) {
                        final String string = LocaleController.getString("NotificationsAddAnException", 2131560056);
                        if (NotificationsCustomSettingsActivity.this.exceptionsStartRow != -1) {
                            b2 = true;
                        }
                        textCell.setTextAndIcon(string, 2131165272, b2);
                        break;
                    }
                    break;
                }
                case 6: {
                    final NotificationsCheckCell notificationsCheckCell = (NotificationsCheckCell)viewHolder.itemView;
                    notificationsCheckCell.setDrawLine(false);
                    final StringBuilder sb = new StringBuilder();
                    final SharedPreferences notificationsSettings = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.this.currentAccount);
                    String s;
                    if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                        s = LocaleController.getString("NotificationsForPrivateChats", 2131560070);
                        n = notificationsSettings.getInt("EnableAll2", 0);
                    }
                    else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                        s = LocaleController.getString("NotificationsForGroups", 2131560069);
                        n = notificationsSettings.getInt("EnableGroup2", 0);
                    }
                    else {
                        s = LocaleController.getString("NotificationsForChannels", 2131560067);
                        n = notificationsSettings.getInt("EnableChannel2", 0);
                    }
                    final int currentTime = ConnectionsManager.getInstance(NotificationsCustomSettingsActivity.this.currentAccount).getCurrentTime();
                    final boolean b3 = n < currentTime;
                    Label_0355: {
                        if (b3) {
                            sb.append(LocaleController.getString("NotificationsOn", 2131560080));
                        }
                        else {
                            if (n - 31536000 < currentTime) {
                                sb.append(LocaleController.formatString("NotificationsOffUntil", 2131560079, LocaleController.stringForMessageListDate(n)));
                                n = 2;
                                break Label_0355;
                            }
                            sb.append(LocaleController.getString("NotificationsOff", 2131560078));
                        }
                        n = 0;
                    }
                    notificationsCheckCell.setTextAndValueAndCheck(s, sb, b3, n, false);
                    break;
                }
                case 5: {
                    final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings2 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.this.currentAccount);
                    if (n == NotificationsCustomSettingsActivity.this.messageSoundRow) {
                        String s2;
                        if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                            s2 = notificationsSettings2.getString("GlobalSound", LocaleController.getString("SoundDefault", 2131560801));
                        }
                        else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            s2 = notificationsSettings2.getString("GroupSound", LocaleController.getString("SoundDefault", 2131560801));
                        }
                        else {
                            s2 = notificationsSettings2.getString("ChannelSound", LocaleController.getString("SoundDefault", 2131560801));
                        }
                        String string2 = s2;
                        if (s2.equals("NoSound")) {
                            string2 = LocaleController.getString("NoSound", 2131559952);
                        }
                        textSettingsCell.setTextAndValue(LocaleController.getString("Sound", 2131560800), string2, true);
                        break;
                    }
                    if (n == NotificationsCustomSettingsActivity.this.messageVibrateRow) {
                        if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                            n = notificationsSettings2.getInt("vibrate_messages", 0);
                        }
                        else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            n = notificationsSettings2.getInt("vibrate_group", 0);
                        }
                        else {
                            n = notificationsSettings2.getInt("vibrate_channel", 0);
                        }
                        if (n == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
                            break;
                        }
                        if (n == 1) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
                            break;
                        }
                        if (n == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
                            break;
                        }
                        if (n == 3) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
                            break;
                        }
                        if (n == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("OnlyIfSilent", 2131560107), true);
                            break;
                        }
                        break;
                    }
                    else if (n == NotificationsCustomSettingsActivity.this.messagePriorityRow) {
                        if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                            n = notificationsSettings2.getInt("priority_messages", 1);
                        }
                        else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            n = notificationsSettings2.getInt("priority_group", 1);
                        }
                        else {
                            n = notificationsSettings2.getInt("priority_channel", 1);
                        }
                        if (n == 0) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityHigh", 2131560082), true);
                            break;
                        }
                        if (n == 1 || n == 2) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityUrgent", 2131560086), true);
                            break;
                        }
                        if (n == 4) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityLow", 2131560083), true);
                            break;
                        }
                        if (n == 5) {
                            textSettingsCell.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityMedium", 2131560084), true);
                            break;
                        }
                        break;
                    }
                    else {
                        if (n == NotificationsCustomSettingsActivity.this.messagePopupNotificationRow) {
                            if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                                n = notificationsSettings2.getInt("popupAll", 0);
                            }
                            else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                                n = notificationsSettings2.getInt("popupGroup", 0);
                            }
                            else {
                                n = notificationsSettings2.getInt("popupChannel", 0);
                            }
                            String s3;
                            if (n == 0) {
                                s3 = LocaleController.getString("NoPopup", 2131559939);
                            }
                            else if (n == 1) {
                                s3 = LocaleController.getString("OnlyWhenScreenOn", 2131560109);
                            }
                            else if (n == 2) {
                                s3 = LocaleController.getString("OnlyWhenScreenOff", 2131560108);
                            }
                            else {
                                s3 = LocaleController.getString("AlwaysShowPopup", 2131558614);
                            }
                            textSettingsCell.setTextAndValue(LocaleController.getString("PopupNotification", 2131560471), s3, true);
                            break;
                        }
                        break;
                    }
                    break;
                }
                case 4: {
                    if (n != NotificationsCustomSettingsActivity.this.exceptionsSection2Row && (n != NotificationsCustomSettingsActivity.this.groupSection2Row || NotificationsCustomSettingsActivity.this.exceptionsSection2Row != -1)) {
                        viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        break;
                    }
                    viewHolder.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    break;
                }
                case 3: {
                    final TextColorCell textColorCell = (TextColorCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings3 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.this.currentAccount);
                    if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                        n = notificationsSettings3.getInt("MessagesLed", -16776961);
                    }
                    else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                        n = notificationsSettings3.getInt("GroupLed", -16776961);
                    }
                    else {
                        n = notificationsSettings3.getInt("ChannelLed", -16776961);
                    }
                    int n3;
                    while (true) {
                        n3 = n;
                        if (n2 >= 9) {
                            break;
                        }
                        if (TextColorCell.colorsToSave[n2] == n) {
                            n3 = TextColorCell.colors[n2];
                            break;
                        }
                        ++n2;
                    }
                    textColorCell.setTextAndColor(LocaleController.getString("LedColor", 2131559747), n3, true);
                    break;
                }
                case 2: {
                    final UserCell userCell = (UserCell)viewHolder.itemView;
                    final NotificationsSettingsActivity.NotificationException ex = NotificationsCustomSettingsActivity.this.exceptions.get(n - NotificationsCustomSettingsActivity.this.exceptionsStartRow);
                    boolean b4 = b;
                    if (n != NotificationsCustomSettingsActivity.this.exceptionsEndRow - 1) {
                        b4 = true;
                    }
                    userCell.setException(ex, null, b4);
                    break;
                }
                case 1: {
                    final TextCheckCell textCheckCell = (TextCheckCell)viewHolder.itemView;
                    final SharedPreferences notificationsSettings4 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.this.currentAccount);
                    if (n == NotificationsCustomSettingsActivity.this.previewRow) {
                        boolean b5;
                        if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                            b5 = notificationsSettings4.getBoolean("EnablePreviewAll", true);
                        }
                        else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                            b5 = notificationsSettings4.getBoolean("EnablePreviewGroup", true);
                        }
                        else {
                            b5 = notificationsSettings4.getBoolean("EnablePreviewChannel", true);
                        }
                        textCheckCell.setTextAndCheck(LocaleController.getString("MessagePreview", 2131559854), b5, true);
                        break;
                    }
                    break;
                }
                case 0: {
                    final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                    if (n == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                        headerCell.setText(LocaleController.getString("SETTINGS", 2131560623));
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
                    o = new TextCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 6: {
                    o = new NotificationsCheckCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 5: {
                    o = new TextSettingsCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 4: {
                    o = new ShadowSectionCell(this.mContext);
                    break;
                }
                case 3: {
                    o = new TextColorCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 2: {
                    o = new UserCell(this.mContext, 6, 0, false);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 1: {
                    o = new TextCheckCell(this.mContext);
                    ((TextCheckCell)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
                case 0: {
                    o = new HeaderCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    break;
                }
            }
            return new RecyclerListView.Holder((View)o);
        }
        
        @Override
        public void onViewAttachedToWindow(final ViewHolder viewHolder) {
            if (NotificationsCustomSettingsActivity.this.exceptions != null) {
                if (NotificationsCustomSettingsActivity.this.exceptions.isEmpty()) {
                    final boolean globalNotificationsEnabled = NotificationsController.getInstance(NotificationsCustomSettingsActivity.this.currentAccount).isGlobalNotificationsEnabled(NotificationsCustomSettingsActivity.this.currentType);
                    final int itemViewType = viewHolder.getItemViewType();
                    if (itemViewType != 0) {
                        if (itemViewType != 1) {
                            if (itemViewType != 3) {
                                if (itemViewType == 5) {
                                    ((TextSettingsCell)viewHolder.itemView).setEnabled(globalNotificationsEnabled, null);
                                }
                            }
                            else {
                                ((TextColorCell)viewHolder.itemView).setEnabled(globalNotificationsEnabled, null);
                            }
                        }
                        else {
                            ((TextCheckCell)viewHolder.itemView).setEnabled(globalNotificationsEnabled, null);
                        }
                    }
                    else {
                        final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                        if (viewHolder.getAdapterPosition() == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                            headerCell.setEnabled(globalNotificationsEnabled, null);
                        }
                        else {
                            headerCell.setEnabled(true, null);
                        }
                    }
                }
            }
        }
    }
    
    private class SearchAdapter extends SelectionAdapter
    {
        private Context mContext;
        private ArrayList<NotificationsSettingsActivity.NotificationException> searchResult;
        private ArrayList<CharSequence> searchResultNames;
        private Timer searchTimer;
        
        public SearchAdapter(final Context mContext) {
            this.searchResult = new ArrayList<NotificationsSettingsActivity.NotificationException>();
            this.searchResultNames = new ArrayList<CharSequence>();
            this.mContext = mContext;
        }
        
        private void processSearch(final String s) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsCustomSettingsActivity$SearchAdapter$R_yRN0Y21CFdspI_6cy_hAalLcc(this, s));
        }
        
        private void updateSearchResults(final ArrayList<NotificationsSettingsActivity.NotificationException> list, final ArrayList<CharSequence> list2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsCustomSettingsActivity$SearchAdapter$f9v_aVxREJMj58yLknoa0t4SGPc(this, list, list2));
        }
        
        @Override
        public int getItemCount() {
            return this.searchResult.size();
        }
        
        @Override
        public int getItemViewType(final int n) {
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return true;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final UserCell userCell = (UserCell)viewHolder.itemView;
            final NotificationsSettingsActivity.NotificationException ex = this.searchResult.get(n);
            final CharSequence charSequence = this.searchResultNames.get(n);
            final int size = this.searchResult.size();
            boolean b = true;
            if (n == size - 1) {
                b = false;
            }
            userCell.setException(ex, charSequence, b);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final UserCell userCell = new UserCell(this.mContext, 9, 0, false);
            ((View)userCell).setPadding(AndroidUtilities.dp(6.0f), 0, AndroidUtilities.dp(6.0f), 0);
            ((View)userCell).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            return new RecyclerListView.Holder((View)userCell);
        }
        
        public void searchDialogs(final String s) {
            try {
                if (this.searchTimer != null) {
                    this.searchTimer.cancel();
                }
            }
            catch (Exception ex) {
                FileLog.e(ex);
            }
            if (s == null) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                this.notifyDataSetChanged();
            }
            else {
                (this.searchTimer = new Timer()).schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            SearchAdapter.this.searchTimer.cancel();
                            SearchAdapter.this.searchTimer = null;
                        }
                        catch (Exception ex) {
                            FileLog.e(ex);
                        }
                        SearchAdapter.this.processSearch(s);
                    }
                }, 200L, 300L);
            }
        }
    }
}
