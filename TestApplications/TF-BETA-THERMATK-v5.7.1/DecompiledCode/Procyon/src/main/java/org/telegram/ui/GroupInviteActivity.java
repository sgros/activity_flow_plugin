// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import org.telegram.messenger.ChatObject;
import android.content.DialogInterface;
import org.telegram.messenger.AndroidUtilities;
import android.widget.Toast;
import android.content.ClipData;
import org.telegram.messenger.ApplicationLoader;
import android.content.ClipboardManager;
import android.app.Dialog;
import android.content.DialogInterface$OnClickListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.messenger.FileLog;
import android.content.Intent;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.TextBlockCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class GroupInviteActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int chat_id;
    private int copyLinkRow;
    private EmptyTextProgressView emptyView;
    private TLRPC.ExportedChatInvite invite;
    private int linkInfoRow;
    private int linkRow;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loading;
    private int revokeLinkRow;
    private int rowCount;
    private int shadowRow;
    private int shareLinkRow;
    
    public GroupInviteActivity(final int chat_id) {
        this.chat_id = chat_id;
    }
    
    private void generateLink(final boolean b) {
        this.loading = true;
        final TLRPC.TL_messages_exportChatInvite tl_messages_exportChatInvite = new TLRPC.TL_messages_exportChatInvite();
        tl_messages_exportChatInvite.peer = MessagesController.getInstance(super.currentAccount).getInputPeer(-this.chat_id);
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_exportChatInvite, new _$$Lambda$GroupInviteActivity$GRgS5oE61g396ll3j_eBPQt5fnk(this, b)), super.classGuid);
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("InviteLink", 2131559679));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    GroupInviteActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.emptyView = new EmptyTextProgressView(context)).showProgress();
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        (this.listView = new RecyclerListView(context)).setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(context, 1, false));
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setVerticalScrollBarEnabled(false);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1, 51));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$GroupInviteActivity$0RYe3qGmyjz46oz7qfkeGrq_SgU(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(int intValue, final int n, final Object... array) {
        if (intValue == NotificationCenter.chatInfoDidLoad) {
            final TLRPC.ChatFull chatFull = (TLRPC.ChatFull)array[0];
            intValue = (int)array[1];
            if (chatFull.id == this.chat_id && intValue == super.classGuid) {
                this.invite = MessagesController.getInstance(super.currentAccount).getExportedInvite(this.chat_id);
                if (!(this.invite instanceof TLRPC.TL_chatInviteExported)) {
                    this.generateLink(false);
                }
                else {
                    this.loading = false;
                    final ListAdapter listAdapter = this.listAdapter;
                    if (listAdapter != null) {
                        ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
                    }
                }
            }
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { TextSettingsCell.class, TextBlockCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { TextSettingsCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { TextBlockCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoad);
        MessagesController.getInstance(super.currentAccount).loadFullChat(this.chat_id, super.classGuid, true);
        this.loading = true;
        this.rowCount = 0;
        this.linkRow = this.rowCount++;
        this.linkInfoRow = this.rowCount++;
        this.copyLinkRow = this.rowCount++;
        this.revokeLinkRow = this.rowCount++;
        this.shareLinkRow = this.rowCount++;
        this.shadowRow = this.rowCount++;
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoad);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
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
            int access$500;
            if (GroupInviteActivity.this.loading) {
                access$500 = 0;
            }
            else {
                access$500 = GroupInviteActivity.this.rowCount;
            }
            return access$500;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n == GroupInviteActivity.this.copyLinkRow || n == GroupInviteActivity.this.shareLinkRow || n == GroupInviteActivity.this.revokeLinkRow) {
                return 0;
            }
            if (n == GroupInviteActivity.this.shadowRow || n == GroupInviteActivity.this.linkInfoRow) {
                return 1;
            }
            if (n == GroupInviteActivity.this.linkRow) {
                return 2;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            final int adapterPosition = viewHolder.getAdapterPosition();
            return adapterPosition == GroupInviteActivity.this.revokeLinkRow || adapterPosition == GroupInviteActivity.this.copyLinkRow || adapterPosition == GroupInviteActivity.this.shareLinkRow || adapterPosition == GroupInviteActivity.this.linkRow;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType != 1) {
                    if (itemViewType == 2) {
                        final TextBlockCell textBlockCell = (TextBlockCell)viewHolder.itemView;
                        String link;
                        if (GroupInviteActivity.this.invite != null) {
                            link = GroupInviteActivity.this.invite.link;
                        }
                        else {
                            link = "error";
                        }
                        textBlockCell.setText(link, false);
                    }
                }
                else {
                    final TextInfoPrivacyCell textInfoPrivacyCell = (TextInfoPrivacyCell)viewHolder.itemView;
                    if (n == GroupInviteActivity.this.shadowRow) {
                        textInfoPrivacyCell.setText("");
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                    else if (n == GroupInviteActivity.this.linkInfoRow) {
                        final TLRPC.Chat chat = MessagesController.getInstance(GroupInviteActivity.this.currentAccount).getChat(GroupInviteActivity.this.chat_id);
                        if (ChatObject.isChannel(chat) && !chat.megagroup) {
                            textInfoPrivacyCell.setText(LocaleController.getString("ChannelLinkInfo", 2131558959));
                        }
                        else {
                            textInfoPrivacyCell.setText(LocaleController.getString("LinkInfo", 2131559754));
                        }
                        textInfoPrivacyCell.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                    }
                }
            }
            else {
                final TextSettingsCell textSettingsCell = (TextSettingsCell)viewHolder.itemView;
                if (n == GroupInviteActivity.this.copyLinkRow) {
                    textSettingsCell.setText(LocaleController.getString("CopyLink", 2131559164), true);
                }
                else if (n == GroupInviteActivity.this.shareLinkRow) {
                    textSettingsCell.setText(LocaleController.getString("ShareLink", 2131560749), false);
                }
                else if (n == GroupInviteActivity.this.revokeLinkRow) {
                    textSettingsCell.setText(LocaleController.getString("RevokeLink", 2131560620), true);
                }
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    frameLayout = new TextBlockCell(this.mContext);
                    ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
                else {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                }
            }
            else {
                frameLayout = new TextSettingsCell(this.mContext);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
