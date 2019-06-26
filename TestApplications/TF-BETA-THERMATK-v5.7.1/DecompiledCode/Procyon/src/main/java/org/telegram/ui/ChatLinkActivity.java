// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import java.util.Collection;
import org.telegram.messenger.Utilities;
import android.view.ViewGroup;
import android.widget.ImageView;
import org.telegram.tgnet.ConnectionsManager;
import android.content.DialogInterface;
import android.content.DialogInterface$OnCancelListener;
import android.os.Bundle;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.Paint;
import org.telegram.ui.Cells.ManageChatTextCell;
import org.telegram.ui.Cells.ManageChatUserCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import android.graphics.drawable.Drawable;
import org.telegram.messenger.ImageLocation;
import android.text.TextUtils$TruncateAt;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.AvatarDrawable;
import android.widget.FrameLayout;
import android.text.TextUtils;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.MessagesStorage;
import android.content.Context;
import android.view.View;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ChatLinkActivity extends BaseFragment implements NotificationCenterDelegate
{
    private static final int search_button = 0;
    private int chatEndRow;
    private int chatStartRow;
    private ArrayList<TLRPC.Chat> chats;
    private int createChatRow;
    private TLRPC.Chat currentChat;
    private int currentChatId;
    private int detailRow;
    private EmptyTextProgressView emptyView;
    private int helpRow;
    private TLRPC.ChatFull info;
    private boolean isChannel;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loadingChats;
    private int removeChatRow;
    private int rowCount;
    private SearchAdapter searchAdapter;
    private ActionBarMenuItem searchItem;
    private boolean searchWas;
    private boolean searching;
    private boolean waitingForChatCreate;
    private TLRPC.Chat waitingForFullChat;
    private AlertDialog waitingForFullChatProgressAlert;
    
    public ChatLinkActivity(final int n) {
        this.chats = new ArrayList<TLRPC.Chat>();
        this.currentChatId = n;
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(n);
        this.isChannel = (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup);
    }
    
    private void linkChat(final TLRPC.Chat chat, final BaseFragment baseFragment) {
        if (chat == null) {
            return;
        }
        if (!ChatObject.isChannel(chat)) {
            MessagesController.getInstance(super.currentAccount).convertToMegaGroup((Context)this.getParentActivity(), chat.id, new _$$Lambda$ChatLinkActivity$KB_csNS4ZyyV341Vg7wZcvTIREM(this, baseFragment));
            return;
        }
        final AlertDialog[] array = { null };
        AlertDialog alertDialog;
        if (baseFragment != null) {
            alertDialog = null;
        }
        else {
            alertDialog = new AlertDialog((Context)this.getParentActivity(), 3);
        }
        array[0] = alertDialog;
        final TLRPC.TL_channels_setDiscussionGroup tl_channels_setDiscussionGroup = new TLRPC.TL_channels_setDiscussionGroup();
        tl_channels_setDiscussionGroup.broadcast = MessagesController.getInputChannel(this.currentChat);
        tl_channels_setDiscussionGroup.group = MessagesController.getInputChannel(chat);
        AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$97kBY0LYAi0P_kzDEVlDTHmupZw(this, array, this.getConnectionsManager().sendRequest(tl_channels_setDiscussionGroup, new _$$Lambda$ChatLinkActivity$Tjh48No2Z3EZ2EiDDrohPMflh_A(this, array, chat, baseFragment))), 500L);
    }
    
    private void loadChats() {
        if (this.info.linked_chat_id != 0) {
            this.chats.clear();
            final TLRPC.Chat chat = this.getMessagesController().getChat(this.info.linked_chat_id);
            if (chat != null) {
                this.chats.add(chat);
            }
            final ActionBarMenuItem searchItem = this.searchItem;
            if (searchItem != null) {
                searchItem.setVisibility(8);
            }
        }
        if (!this.loadingChats && this.isChannel) {
            if (this.info.linked_chat_id == 0) {
                this.loadingChats = true;
                this.getConnectionsManager().sendRequest(new TLRPC.TL_channels_getGroupsForDiscussion(), new _$$Lambda$ChatLinkActivity$tq6x8HdoUQyHzEq8SE366Ggv0uM(this));
            }
        }
    }
    
    private void showLinkAlert(final TLRPC.Chat chat, final boolean b) {
        final TLRPC.ChatFull chatFull = this.getMessagesController().getChatFull(chat.id);
        final int n = 3;
        if (chatFull == null) {
            if (b) {
                this.getMessagesController().loadFullChat(chat.id, 0, true);
                this.waitingForFullChat = chat;
                this.waitingForFullChatProgressAlert = new AlertDialog((Context)this.getParentActivity(), 3);
                AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$XAuETeIZs_27DO0KwtCzWWlThog(this), 500L);
            }
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder((Context)this.getParentActivity());
        final TextView textView = new TextView((Context)this.getParentActivity());
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
        String str;
        if (TextUtils.isEmpty((CharSequence)chat.username)) {
            str = LocaleController.formatString("DiscussionLinkGroupPublicPrivateAlert", 2131559292, chat.title, this.currentChat.title);
        }
        else if (TextUtils.isEmpty((CharSequence)this.currentChat.username)) {
            str = LocaleController.formatString("DiscussionLinkGroupPrivateAlert", 2131559290, chat.title, this.currentChat.title);
        }
        else {
            str = LocaleController.formatString("DiscussionLinkGroupPublicAlert", 2131559291, chat.title, this.currentChat.title);
        }
        String string = str;
        if (chatFull.hidden_prehistory) {
            final StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("\n\n");
            sb.append(LocaleController.getString("DiscussionLinkGroupAlertHistory", 2131559289));
            string = sb.toString();
        }
        textView.setText((CharSequence)AndroidUtilities.replaceTags(string));
        final FrameLayout view = new FrameLayout((Context)this.getParentActivity());
        builder.setView((View)view);
        final AvatarDrawable avatarDrawable = new AvatarDrawable();
        avatarDrawable.setTextSize(AndroidUtilities.dp(12.0f));
        final BackupImageView backupImageView = new BackupImageView((Context)this.getParentActivity());
        backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
        int n3;
        if (LocaleController.isRTL) {
            n3 = 5;
        }
        else {
            n3 = 3;
        }
        view.addView((View)backupImageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(40, 40.0f, n3 | 0x30, 22.0f, 5.0f, 22.0f, 0.0f));
        final TextView textView2 = new TextView((Context)this.getParentActivity());
        textView2.setTextColor(Theme.getColor("actionBarDefaultSubmenuItem"));
        textView2.setTextSize(1, 20.0f);
        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        textView2.setLines(1);
        textView2.setMaxLines(1);
        textView2.setSingleLine(true);
        int n4;
        if (LocaleController.isRTL) {
            n4 = 5;
        }
        else {
            n4 = 3;
        }
        textView2.setGravity(n4 | 0x10);
        textView2.setEllipsize(TextUtils$TruncateAt.END);
        textView2.setText((CharSequence)chat.title);
        int n5;
        if (LocaleController.isRTL) {
            n5 = 5;
        }
        else {
            n5 = 3;
        }
        final boolean isRTL = LocaleController.isRTL;
        final int n6 = 21;
        int n7;
        if (isRTL) {
            n7 = 21;
        }
        else {
            n7 = 76;
        }
        final float n8 = (float)n7;
        int n9 = n6;
        if (LocaleController.isRTL) {
            n9 = 76;
        }
        view.addView((View)textView2, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, n5 | 0x30, n8, 11.0f, (float)n9, 0.0f));
        int n10 = n;
        if (LocaleController.isRTL) {
            n10 = 5;
        }
        view.addView((View)textView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, n10 | 0x30, 24.0f, 57.0f, 24.0f, 9.0f));
        avatarDrawable.setInfo(chat);
        backupImageView.setImage(ImageLocation.getForChat(chat, false), "50_50", avatarDrawable, chat);
        builder.setPositiveButton(LocaleController.getString("DiscussionLinkGroup", 2131559288), (DialogInterface$OnClickListener)new _$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74(this, chatFull, chat));
        builder.setNegativeButton(LocaleController.getString("Cancel", 2131558891), null);
        this.showDialog(builder.create());
    }
    
    private void updateRows() {
        this.currentChat = MessagesController.getInstance(super.currentAccount).getChat(this.currentChatId);
        if (this.currentChat == null) {
            return;
        }
        int visibility = 0;
        this.rowCount = 0;
        this.helpRow = -1;
        this.createChatRow = -1;
        this.chatStartRow = -1;
        this.chatEndRow = -1;
        this.removeChatRow = -1;
        this.detailRow = -1;
        this.helpRow = this.rowCount++;
        if (this.isChannel) {
            if (this.info.linked_chat_id == 0) {
                this.createChatRow = this.rowCount++;
            }
            final int rowCount = this.rowCount;
            this.chatStartRow = rowCount;
            this.rowCount = rowCount + this.chats.size();
            final int rowCount2 = this.rowCount;
            this.chatEndRow = rowCount2;
            if (this.info.linked_chat_id != 0) {
                this.rowCount = rowCount2 + 1;
                this.createChatRow = rowCount2;
            }
            this.detailRow = this.rowCount++;
        }
        else {
            final int rowCount3 = this.rowCount;
            this.chatStartRow = rowCount3;
            this.rowCount = rowCount3 + this.chats.size();
            final int rowCount4 = this.rowCount;
            this.chatEndRow = rowCount4;
            this.rowCount = rowCount4 + 1;
            this.createChatRow = rowCount4;
            this.detailRow = this.rowCount++;
        }
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
        final ActionBarMenuItem searchItem = this.searchItem;
        if (searchItem != null) {
            if (this.chats.size() <= 10) {
                visibility = 8;
            }
            searchItem.setVisibility(visibility);
        }
    }
    
    @Override
    public View createView(final Context context) {
        this.searching = false;
        this.searchWas = false;
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("Discussion", 2131559280));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ChatLinkActivity.this.finishFragment();
                }
            }
        });
        (this.searchItem = super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener((ActionBarMenuItem.ActionBarMenuItemSearchListener)new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            @Override
            public void onSearchCollapse() {
                ChatLinkActivity.this.searchAdapter.searchDialogs(null);
                ChatLinkActivity.this.searching = false;
                ChatLinkActivity.this.searchWas = false;
                ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.listViewAdapter);
                ChatLinkActivity.this.listViewAdapter.notifyDataSetChanged();
                ChatLinkActivity.this.listView.setFastScrollVisible(true);
                ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(false);
                ChatLinkActivity.this.emptyView.setShowAtCenter(false);
                ChatLinkActivity.this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
                ChatLinkActivity.this.fragmentView.setTag((Object)"windowBackgroundGray");
                ChatLinkActivity.this.emptyView.showProgress();
            }
            
            @Override
            public void onSearchExpand() {
                ChatLinkActivity.this.searching = true;
                ChatLinkActivity.this.emptyView.setShowAtCenter(true);
            }
            
            @Override
            public void onTextChanged(final EditText editText) {
                if (ChatLinkActivity.this.searchAdapter == null) {
                    return;
                }
                final String string = editText.getText().toString();
                if (string.length() != 0) {
                    ChatLinkActivity.this.searchWas = true;
                    if (ChatLinkActivity.this.listView != null && ChatLinkActivity.this.listView.getAdapter() != ChatLinkActivity.this.searchAdapter) {
                        ChatLinkActivity.this.listView.setAdapter(ChatLinkActivity.this.searchAdapter);
                        ChatLinkActivity.this.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        ChatLinkActivity.this.fragmentView.setTag((Object)"windowBackgroundWhite");
                        ChatLinkActivity.this.searchAdapter.notifyDataSetChanged();
                        ChatLinkActivity.this.listView.setFastScrollVisible(false);
                        ChatLinkActivity.this.listView.setVerticalScrollBarEnabled(true);
                        ChatLinkActivity.this.emptyView.showProgress();
                    }
                }
                ChatLinkActivity.this.searchAdapter.searchDialogs(string);
            }
        })).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
        this.searchAdapter = new SearchAdapter(context);
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        super.fragmentView.setTag((Object)"windowBackgroundGray");
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).showProgress();
        this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ChatLinkActivity$EId5OESEIJ9T5F7R7Ql_mhkWmi4(this));
        this.updateRows();
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int id, final int n, final Object... array) {
        if (id != NotificationCenter.chatInfoDidLoad) {
            return;
        }
        final TLRPC.ChatFull info = (TLRPC.ChatFull)array[0];
        id = info.id;
        if (id == this.currentChatId) {
            this.info = info;
            this.loadChats();
            this.updateRows();
            return;
        }
        final TLRPC.Chat waitingForFullChat = this.waitingForFullChat;
        if (waitingForFullChat == null || waitingForFullChat.id != id) {
            return;
        }
        while (true) {
            try {
                this.waitingForFullChatProgressAlert.dismiss();
                this.waitingForFullChatProgressAlert = null;
                this.showLinkAlert(this.waitingForFullChat, false);
                this.waitingForFullChat = null;
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU $$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU = new _$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ManageChatUserCell.class, ManageChatTextCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundGray"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, null, null, null, null, "windowBackgroundWhite"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "nameTextView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "windowBackgroundWhiteGrayText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, new String[] { "statusOnlineColor" }, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "windowBackgroundWhiteBlueText"), new ThemeDescription((View)this.listView, 0, new Class[] { ManageChatUserCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$ChatLinkActivity$M285Vuz7T55WG0zESsKh6UUk1tU, "avatar_backgroundPink"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteGrayIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "imageView" }, null, null, null, "windowBackgroundWhiteBlueButton"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[] { ManageChatTextCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlueIcon") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.getNotificationCenter().addObserver(this, NotificationCenter.chatInfoDidLoad);
        this.loadChats();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        this.getNotificationCenter().removeObserver(this, NotificationCenter.chatInfoDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
    }
    
    public void setInfo(final TLRPC.ChatFull info) {
        this.info = info;
    }
    
    public class HintInnerCell extends FrameLayout
    {
        private ImageView imageView;
        private TextView messageTextView;
        
        public HintInnerCell(final Context context) {
            super(context);
            this.imageView = new ImageView(context);
            final ImageView imageView = this.imageView;
            int imageResource;
            if (Theme.getCurrentTheme().isDark()) {
                imageResource = 2131165881;
            }
            else {
                imageResource = 2131165880;
            }
            imageView.setImageResource(imageResource);
            this.addView((View)this.imageView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 20.0f, 8.0f, 0.0f));
            (this.messageTextView = new TextView(context)).setTextColor(Theme.getColor("chats_message"));
            this.messageTextView.setTextSize(1, 14.0f);
            this.messageTextView.setGravity(17);
            if (ChatLinkActivity.this.isChannel) {
                if (ChatLinkActivity.this.info != null && ChatLinkActivity.this.info.linked_chat_id != 0) {
                    final TLRPC.Chat chat = ChatLinkActivity.this.getMessagesController().getChat(ChatLinkActivity.this.info.linked_chat_id);
                    if (chat != null) {
                        this.messageTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionChannelGroupSetHelp", 2131559281, chat.title)));
                    }
                }
                else {
                    this.messageTextView.setText((CharSequence)LocaleController.getString("DiscussionChannelHelp", 2131559282));
                }
            }
            else {
                final TLRPC.Chat chat2 = ChatLinkActivity.this.getMessagesController().getChat(ChatLinkActivity.this.info.linked_chat_id);
                if (chat2 != null) {
                    this.messageTextView.setText((CharSequence)AndroidUtilities.replaceTags(LocaleController.formatString("DiscussionGroupHelp", 2131559285, chat2.title)));
                }
            }
            this.addView((View)this.messageTextView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -2.0f, 51, 52.0f, 124.0f, 52.0f, 27.0f));
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
            if (ChatLinkActivity.this.loadingChats) {
                return 0;
            }
            return ChatLinkActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == ChatLinkActivity.this.helpRow) {
                return 3;
            }
            if (n == ChatLinkActivity.this.createChatRow || n == ChatLinkActivity.this.removeChatRow) {
                return 2;
            }
            if (n >= ChatLinkActivity.this.chatStartRow && n < ChatLinkActivity.this.chatEndRow) {
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
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = false;
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        final ManageChatTextCell manageChatTextCell = (ManageChatTextCell)viewHolder.itemView;
                        if (ChatLinkActivity.this.isChannel) {
                            if (ChatLinkActivity.this.info.linked_chat_id != 0) {
                                manageChatTextCell.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                                manageChatTextCell.setText(LocaleController.getString("DiscussionUnlinkGroup", 2131559296), null, 2131165274, false);
                            }
                            else {
                                manageChatTextCell.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
                                manageChatTextCell.setText(LocaleController.getString("DiscussionCreateGroup", 2131559284), null, 2131165580, true);
                            }
                        }
                        else {
                            manageChatTextCell.setColors("windowBackgroundWhiteRedText5", "windowBackgroundWhiteRedText5");
                            manageChatTextCell.setText(LocaleController.getString("DiscussionUnlinkChannel", 2131559294), null, 2131165274, false);
                        }
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (i == ChatLinkActivity.this.detailRow) {
                        if (ChatLinkActivity.this.isChannel) {
                            textInfoPrivacyCell.setText(LocaleController.getString("DiscussionChannelHelp2", 2131559283));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("DiscussionGroupHelp2", 2131559286));
                        }
                    }
                }
            }
            else {
                final ManageChatUserCell manageChatUserCell = (ManageChatUserCell)viewHolder.itemView;
                manageChatUserCell.setTag((Object)i);
                final TLRPC.Chat chat = ChatLinkActivity.this.chats.get(i - ChatLinkActivity.this.chatStartRow);
                CharSequence string;
                if (TextUtils.isEmpty((CharSequence)chat.username)) {
                    string = null;
                }
                else {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("@");
                    sb.append(chat.username);
                    string = sb.toString();
                }
                if (i != ChatLinkActivity.this.chatEndRow - 1 || ChatLinkActivity.this.info.linked_chat_id != 0) {
                    b = true;
                }
                manageChatUserCell.setData(chat, null, string, b);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        frameLayout = new HintInnerCell(this.mContext);
                    }
                    else {
                        frameLayout = new ManageChatTextCell(this.mContext);
                        ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                    }
                }
                else {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                    ((View)frameLayout).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
            }
            else {
                frameLayout = new ManageChatUserCell(this.mContext, 6, 2, false);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ManageChatUserCell) {
                ((ManageChatUserCell)itemView).recycle();
            }
        }
    }
    
    private class SearchAdapter extends SelectionAdapter
    {
        private Context mContext;
        private ArrayList<TLRPC.Chat> searchResult;
        private ArrayList<CharSequence> searchResultNames;
        private Runnable searchRunnable;
        private int searchStartRow;
        private int totalCount;
        
        public SearchAdapter(final Context mContext) {
            this.searchResult = new ArrayList<TLRPC.Chat>();
            this.searchResultNames = new ArrayList<CharSequence>();
            this.mContext = mContext;
        }
        
        private void processSearch(final String s) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$SearchAdapter$uzR0bcURzUaKW4VFIVGjup_TFMI(this, s));
        }
        
        private void updateSearchResults(final ArrayList<TLRPC.Chat> list, final ArrayList<CharSequence> list2) {
            AndroidUtilities.runOnUIThread(new _$$Lambda$ChatLinkActivity$SearchAdapter$brffftfBeb_mTh3bCNRySLqW8nw(this, list, list2));
        }
        
        public TLRPC.Chat getItem(final int index) {
            return this.searchResult.get(index);
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
            final int itemViewType = viewHolder.getItemViewType();
            boolean b = true;
            if (itemViewType == 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void notifyDataSetChanged() {
            this.totalCount = 0;
            final int size = this.searchResult.size();
            if (size != 0) {
                final int totalCount = this.totalCount;
                this.searchStartRow = totalCount;
                this.totalCount = totalCount + (size + 1);
            }
            else {
                this.searchStartRow = -1;
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            final TLRPC.Chat chat = this.searchResult.get(i);
            final String username = chat.username;
            final CharSequence charSequence = this.searchResultNames.get(i);
            final CharSequence charSequence2 = null;
            CharSequence charSequence3 = charSequence;
            CharSequence charSequence4 = charSequence2;
            if (charSequence != null) {
                charSequence3 = charSequence;
                charSequence4 = charSequence2;
                if (!TextUtils.isEmpty((CharSequence)username)) {
                    final String string = charSequence.toString();
                    final StringBuilder sb = new StringBuilder();
                    sb.append("@");
                    sb.append(username);
                    charSequence3 = charSequence;
                    charSequence4 = charSequence2;
                    if (string.startsWith(sb.toString())) {
                        charSequence4 = charSequence;
                        charSequence3 = null;
                    }
                }
            }
            final ManageChatUserCell manageChatUserCell = (ManageChatUserCell)viewHolder.itemView;
            manageChatUserCell.setTag((Object)i);
            manageChatUserCell.setData(chat, charSequence3, charSequence4, false);
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            final ManageChatUserCell manageChatUserCell = new ManageChatUserCell(this.mContext, 6, 2, false);
            ((View)manageChatUserCell).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            return new RecyclerListView.Holder((View)manageChatUserCell);
        }
        
        @Override
        public void onViewRecycled(final ViewHolder viewHolder) {
            final View itemView = viewHolder.itemView;
            if (itemView instanceof ManageChatUserCell) {
                ((ManageChatUserCell)itemView).recycle();
            }
        }
        
        public void searchDialogs(final String s) {
            if (this.searchRunnable != null) {
                Utilities.searchQueue.cancelRunnable(this.searchRunnable);
                this.searchRunnable = null;
            }
            if (TextUtils.isEmpty((CharSequence)s)) {
                this.searchResult.clear();
                this.searchResultNames.clear();
                this.notifyDataSetChanged();
            }
            else {
                Utilities.searchQueue.postRunnable(this.searchRunnable = new _$$Lambda$ChatLinkActivity$SearchAdapter$TUvgAYvpvIggtszTT20rbb2gK0U(this, s), 300L);
            }
        }
    }
}
