// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.text.TextUtils;
import org.telegram.tgnet.TLObject;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.content.DialogInterface;
import java.util.Iterator;
import android.os.Bundle;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ManageChatTextCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.tgnet.TLRPC;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import android.view.View;
import org.telegram.ui.Cells.ManageChatUserCell;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.messenger.LocaleController;
import android.content.Context;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.MessagesController;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class PrivacyUsersActivity extends BaseFragment implements NotificationCenterDelegate, ContactsActivityDelegate
{
    private int blockUserDetailRow;
    private int blockUserRow;
    private boolean blockedUsersActivity;
    private PrivacyActivityDelegate delegate;
    private EmptyTextProgressView emptyView;
    private boolean isAlwaysShare;
    private boolean isGroup;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private int rowCount;
    private ArrayList<Integer> uidArray;
    private int usersDetailRow;
    private int usersEndRow;
    private int usersHeaderRow;
    private int usersStartRow;
    
    public PrivacyUsersActivity() {
        this.blockedUsersActivity = true;
    }
    
    public PrivacyUsersActivity(final ArrayList<Integer> uidArray, final boolean isGroup, final boolean isAlwaysShare) {
        this.uidArray = uidArray;
        this.isAlwaysShare = isAlwaysShare;
        this.isGroup = isGroup;
        this.blockedUsersActivity = false;
    }
    
    private void showUnblockAlert(final int n) {
        if (this.getParentActivity() == null) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        CharSequence[] array;
        if (this.blockedUsersActivity) {
            array = new CharSequence[] { LocaleController.getString("Unblock", 2131560932) };
        }
        else {
            array = new CharSequence[] { LocaleController.getString("Delete", 2131559227) };
        }
        builder.setItems(array, (DialogInterface$OnClickListener)new _$$Lambda$PrivacyUsersActivity$8Pgo4HS6PGjX_d7DerbhGOGCfXc(this, n));
        this.showDialog(builder.create());
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (!this.blockedUsersActivity || !this.getMessagesController().loadingBlockedUsers) {
            this.blockUserRow = this.rowCount++;
            this.blockUserDetailRow = this.rowCount++;
            int n;
            if (this.blockedUsersActivity) {
                n = this.getMessagesController().blockedUsers.size();
            }
            else {
                n = this.uidArray.size();
            }
            if (n != 0) {
                this.usersHeaderRow = this.rowCount++;
                final int rowCount = this.rowCount;
                this.usersStartRow = rowCount;
                this.rowCount = rowCount + n;
                final int rowCount2 = this.rowCount;
                this.usersEndRow = rowCount2;
                this.rowCount = rowCount2 + 1;
                this.usersDetailRow = rowCount2;
            }
            else {
                this.usersHeaderRow = -1;
                this.usersStartRow = -1;
                this.usersEndRow = -1;
                this.usersDetailRow = -1;
            }
        }
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    private void updateVisibleRows(final int n) {
        final RecyclerListView listView = this.listView;
        if (listView == null) {
            return;
        }
        for (int childCount = listView.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.listView.getChildAt(i);
            if (child instanceof ManageChatUserCell) {
                ((ManageChatUserCell)child).update(n);
            }
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        if (this.blockedUsersActivity) {
            super.actionBar.setTitle(LocaleController.getString("BlockedUsers", 2131558835));
        }
        else if (this.isGroup) {
            if (this.isAlwaysShare) {
                super.actionBar.setTitle(LocaleController.getString("AlwaysAllow", 2131558611));
            }
            else {
                super.actionBar.setTitle(LocaleController.getString("NeverAllow", 2131559894));
            }
        }
        else if (this.isAlwaysShare) {
            super.actionBar.setTitle(LocaleController.getString("AlwaysShareWithTitle", 2131558613));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("NeverShareWithTitle", 2131559896));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    PrivacyUsersActivity.this.finishFragment();
                }
            }
        });
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.emptyView = new EmptyTextProgressView(context);
        if (this.blockedUsersActivity) {
            this.emptyView.setText(LocaleController.getString("NoBlocked", 2131559913));
        }
        else {
            this.emptyView.setText(LocaleController.getString("NoContacts", 2131559921));
        }
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setVerticalScrollBarEnabled(false);
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$PrivacyUsersActivity$D3bLTU7NAbbHWcoiH45oxyckkb4(this));
        this.listView.setOnItemLongClickListener((RecyclerListView.OnItemLongClickListener)new _$$Lambda$PrivacyUsersActivity$r_SA004H7_gb5NSQvdo4AkX5yCs(this));
        if (this.getMessagesController().loadingBlockedUsers) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        this.updateRows();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.updateInterfaces) {
            intValue = (int)array[0];
            if ((intValue & 0x2) != 0x0 || (intValue & 0x1) != 0x0) {
                this.updateVisibleRows(intValue);
            }
        }
        else if (intValue == NotificationCenter.blockedUsersDidLoad) {
            this.emptyView.showTextView();
            this.updateRows();
        }
    }
    
    @Override
    public void didSelectContact(final TLRPC.User user, final String s, final ContactsActivity contactsActivity) {
        if (user == null) {
            return;
        }
        this.getMessagesController().blockUser(user.id);
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw $$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw = new _$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw(this);
        return new ThemeDescription[] { new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ManageChatUserCell.class, ManageChatTextCell.class, HeaderCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { ShadowSectionCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$PrivacyUsersActivity$MXkjS07yNtFLRhkACbitVef2EDw, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, 0, new Class[] { HeaderCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteBlueButton"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueIcon") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        if (this.blockedUsersActivity) {
            NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoad);
            this.getMessagesController().getBlockedUsers(false);
        }
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        if (this.blockedUsersActivity) {
            NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoad);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    public void setDelegate(final PrivacyActivityDelegate delegate) {
        this.delegate = delegate;
    }
    
    private class ListAdapter extends SelectionAdapter
    {
        private Context mContext;
        
        public ListAdapter(final Context mContext) {
            this.mContext = mContext;
        }
        
        @Override
        public int getItemCount() {
            return PrivacyUsersActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == PrivacyUsersActivity.this.usersHeaderRow) {
                return 3;
            }
            if (n == PrivacyUsersActivity.this.blockUserRow) {
                return 2;
            }
            if (n != PrivacyUsersActivity.this.blockUserDetailRow && n != PrivacyUsersActivity.this.usersDetailRow) {
                return 0;
            }
            return 1;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int itemViewType = viewHolder.getItemViewType();
            return itemViewType == 0 || itemViewType == 2;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            final boolean b = false;
            boolean b2 = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType != 2) {
                        if (itemViewType == 3) {
                            final HeaderCell headerCell = (HeaderCell)viewHolder.itemView;
                            if (n == PrivacyUsersActivity.this.usersHeaderRow) {
                                if (PrivacyUsersActivity.this.blockedUsersActivity) {
                                    headerCell.setText(LocaleController.formatPluralString("BlockedUsersCount", BaseFragment.this.getMessagesController().blockedUsers.size()));
                                }
                                else {
                                    headerCell.setText(LocaleController.getString("PrivacyExceptions", 2131560482));
                                }
                            }
                        }
                    }
                    else {
                        final ManageChatTextCell manageChatTextCell = (ManageChatTextCell)viewHolder.itemView;
                        manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                        if (PrivacyUsersActivity.this.blockedUsersActivity) {
                            manageChatTextCell.setText(LocaleController.getString("BlockUser", 2131558834), null, 2131165272, false);
                        }
                        else {
                            manageChatTextCell.setText(LocaleController.getString("PrivacyAddAnException", 2131560474), null, 2131165272, false);
                        }
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == PrivacyUsersActivity.this.blockUserDetailRow) {
                        if (PrivacyUsersActivity.this.blockedUsersActivity) {
                            textInfoPrivacyCell.setText(LocaleController.getString("BlockedUsersInfo", 2131558842));
                        }
                        else {
                            textInfoPrivacyCell.setText(null);
                        }
                        if (PrivacyUsersActivity.this.usersStartRow == -1) {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                        else {
                            textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        }
                    }
                    else if (n == PrivacyUsersActivity.this.usersDetailRow) {
                        textInfoPrivacyCell.setText("");
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final ManageChatUserCell manageChatUserCell = (ManageChatUserCell)viewHolder.itemView;
                int n2;
                if (PrivacyUsersActivity.this.blockedUsersActivity) {
                    n2 = BaseFragment.this.getMessagesController().blockedUsers.keyAt(n - PrivacyUsersActivity.this.usersStartRow);
                }
                else {
                    n2 = PrivacyUsersActivity.this.uidArray.get(n - PrivacyUsersActivity.this.usersStartRow);
                }
                manageChatUserCell.setTag((Object)n2);
                if (n2 > 0) {
                    final TLRPC.User user = BaseFragment.this.getMessagesController().getUser(n2);
                    if (user != null) {
                        String s;
                        if (user.bot) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(LocaleController.getString("Bot", 2131558848).substring(0, 1).toUpperCase());
                            sb.append(LocaleController.getString("Bot", 2131558848).substring(1));
                            s = sb.toString();
                        }
                        else {
                            final String phone = user.phone;
                            if (phone != null && phone.length() != 0) {
                                final PhoneFormat instance = PhoneFormat.getInstance();
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("+");
                                sb2.append(user.phone);
                                s = instance.format(sb2.toString());
                            }
                            else {
                                s = LocaleController.getString("NumberUnknown", 2131560096);
                            }
                        }
                        if (n != PrivacyUsersActivity.this.usersEndRow - 1) {
                            b2 = true;
                        }
                        manageChatUserCell.setData(user, null, s, b2);
                    }
                }
                else {
                    final TLRPC.Chat chat = BaseFragment.this.getMessagesController().getChat(-n2);
                    if (chat != null) {
                        final int participants_count = chat.participants_count;
                        String s2;
                        if (participants_count != 0) {
                            s2 = LocaleController.formatPluralString("Members", participants_count);
                        }
                        else if (TextUtils.isEmpty((CharSequence)chat.username)) {
                            s2 = LocaleController.getString("MegaPrivate", 2131559831);
                        }
                        else {
                            s2 = LocaleController.getString("MegaPublic", 2131559834);
                        }
                        boolean b3 = b;
                        if (n != PrivacyUsersActivity.this.usersEndRow - 1) {
                            b3 = true;
                        }
                        manageChatUserCell.setData(chat, null, s2, b3);
                    }
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        frameLayout = new HeaderCell(this.mContext, false, 21, 11, false);
                        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        ((HeaderCell)frameLayout).setHeight(43);
                    }
                    else {
                        frameLayout = new ManageChatTextCell(this.mContext);
                        ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                frameLayout = new ManageChatUserCell(this.mContext, 7, 6, true);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                ((ManageChatUserCell)frameLayout).setDelegate((ManageChatUserCell.ManageChatUserCellDelegate)new _$$Lambda$PrivacyUsersActivity$ListAdapter$ah_jQyMOHlRewlEcZgEQccTwPTg(this));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
    
    public interface PrivacyActivityDelegate
    {
        void didUpdateUserList(final ArrayList<Integer> p0, final boolean p1);
    }
}
