// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import java.util.Collection;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import android.os.Bundle;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.ProfileSearchCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import androidx.recyclerview.widget.RecyclerView;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.ui.ActionBar.BaseFragment;

public class CommonGroupsActivity extends BaseFragment
{
    private ArrayList<TLRPC.Chat> chats;
    private EmptyTextProgressView emptyView;
    private boolean endReached;
    private boolean firstLoaded;
    private LinearLayoutManager layoutManager;
    private RecyclerListView listView;
    private ListAdapter listViewAdapter;
    private boolean loading;
    private int userId;
    
    public CommonGroupsActivity(final int userId) {
        this.chats = new ArrayList<TLRPC.Chat>();
        this.userId = userId;
    }
    
    private void getChats(int sendRequest, final int limit) {
        if (this.loading) {
            return;
        }
        this.loading = true;
        final EmptyTextProgressView emptyView = this.emptyView;
        if (emptyView != null && !this.firstLoaded) {
            emptyView.showProgress();
        }
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
        }
        final TLRPC.TL_messages_getCommonChats tl_messages_getCommonChats = new TLRPC.TL_messages_getCommonChats();
        tl_messages_getCommonChats.user_id = MessagesController.getInstance(super.currentAccount).getInputUser(this.userId);
        if (tl_messages_getCommonChats.user_id instanceof TLRPC.TL_inputUserEmpty) {
            return;
        }
        tl_messages_getCommonChats.limit = limit;
        tl_messages_getCommonChats.max_id = sendRequest;
        sendRequest = ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getCommonChats, new _$$Lambda$CommonGroupsActivity$uEpDyQSDsMXTcD9ZAD3h6fxSQJA(this, limit));
        ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(sendRequest, super.classGuid);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        final ActionBar actionBar = super.actionBar;
        int verticalScrollbarPosition = 1;
        actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("GroupsInCommonTitle", 2131559627));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    CommonGroupsActivity.this.finishFragment();
                }
            }
        });
        (super.fragmentView = (View)new FrameLayout(context)).setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        (this.emptyView = new EmptyTextProgressView(context)).setText(LocaleController.getString("NoGroupsInCommon", 2131559925));
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        (this.listView = new RecyclerListView(context)).setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        this.listView.setAdapter(this.listViewAdapter = new ListAdapter(context));
        final RecyclerListView listView = this.listView;
        if (!LocaleController.isRTL) {
            verticalScrollbarPosition = 2;
        }
        listView.setVerticalScrollbarPosition(verticalScrollbarPosition);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$CommonGroupsActivity$J3Yy_YOVYpXyEU8UkIqEdngAT6I(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int n, int firstVisibleItemPosition) {
                firstVisibleItemPosition = CommonGroupsActivity.this.layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition == -1) {
                    n = 0;
                }
                else {
                    n = Math.abs(CommonGroupsActivity.this.layoutManager.findLastVisibleItemPosition() - firstVisibleItemPosition) + 1;
                }
                if (n > 0) {
                    final int itemCount = CommonGroupsActivity.this.listViewAdapter.getItemCount();
                    if (!CommonGroupsActivity.this.endReached && !CommonGroupsActivity.this.loading && !CommonGroupsActivity.this.chats.isEmpty() && firstVisibleItemPosition + n >= itemCount - 5) {
                        final CommonGroupsActivity this$0 = CommonGroupsActivity.this;
                        this$0.getChats(((TLRPC.Chat)this$0.chats.get(CommonGroupsActivity.this.chats.size() - 1)).id, 100);
                    }
                }
            }
        });
        if (this.loading) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        return super.fragmentView;
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        final _$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw $$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw = new _$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw(this);
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { LoadingCell.class, ProfileSearchCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { TextInfoPrivacyCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteGrayText4"), new ThemeDescription((View)this.listView, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_namePaint, (Paint)Theme.dialogs_searchNamePaint }, null, null, "chats_name"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Paint[] { (Paint)Theme.dialogs_nameEncryptedPaint, (Paint)Theme.dialogs_searchNameEncryptedPaint }, null, null, "chats_secretName"), new ThemeDescription((View)this.listView, 0, new Class[] { ProfileSearchCell.class }, null, new Drawable[] { Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable }, null, "avatar_text"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundRed"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundOrange"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundViolet"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundGreen"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundCyan"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundBlue"), new ThemeDescription(null, 0, null, null, null, (ThemeDescription.ThemeDescriptionDelegate)$$Lambda$CommonGroupsActivity$CM0EpwYGPtlvUyQJ8b7pz0B_Dbw, "avatar_backgroundPink") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.getChats(0, 50);
        return true;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        final ListAdapter listViewAdapter = this.listViewAdapter;
        if (listViewAdapter != null) {
            ((RecyclerView.Adapter)listViewAdapter).notifyDataSetChanged();
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
            int size;
            int n = size = CommonGroupsActivity.this.chats.size();
            if (!CommonGroupsActivity.this.chats.isEmpty()) {
                size = ++n;
                if (!CommonGroupsActivity.this.endReached) {
                    size = n + 1;
                }
            }
            return size;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n < CommonGroupsActivity.this.chats.size()) {
                return 0;
            }
            if (!CommonGroupsActivity.this.endReached && n == CommonGroupsActivity.this.chats.size()) {
                return 1;
            }
            return 2;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getAdapterPosition() != CommonGroupsActivity.this.chats.size();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int index) {
            if (viewHolder.getItemViewType() == 0) {
                final ProfileSearchCell profileSearchCell = (ProfileSearchCell)viewHolder.itemView;
                profileSearchCell.setData(CommonGroupsActivity.this.chats.get(index), null, null, null, false, false);
                final int size = CommonGroupsActivity.this.chats.size();
                boolean useSeparator = true;
                if (index == size - 1) {
                    useSeparator = (!CommonGroupsActivity.this.endReached && useSeparator);
                }
                profileSearchCell.useSeparator = useSeparator;
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = new TextInfoPrivacyCell(this.mContext);
                    ((View)o).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
                else {
                    o = new LoadingCell(this.mContext);
                    ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                }
            }
            else {
                o = new ProfileSearchCell(this.mContext);
                ((View)o).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
}
