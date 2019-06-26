// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.ViewGroup;
import android.app.Activity;
import org.telegram.messenger.DataQuery;
import org.telegram.ui.Components.Switch;
import java.util.Collection;
import org.telegram.messenger.AndroidUtilities;
import android.app.Dialog;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.LoadingCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.ArchivedStickerSetCell;
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
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class ArchivedStickersActivity extends BaseFragment implements NotificationCenterDelegate
{
    private int currentType;
    private EmptyTextProgressView emptyView;
    private boolean endReached;
    private boolean firstLoaded;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private boolean loadingStickers;
    private int rowCount;
    private ArrayList<TLRPC.StickerSetCovered> sets;
    private int stickersEndRow;
    private int stickersLoadingRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    
    public ArchivedStickersActivity(final int currentType) {
        this.sets = new ArrayList<TLRPC.StickerSetCovered>();
        this.currentType = currentType;
    }
    
    private void getStickers() {
        if (!this.loadingStickers) {
            if (!this.endReached) {
                boolean masks = true;
                this.loadingStickers = true;
                final EmptyTextProgressView emptyView = this.emptyView;
                if (emptyView != null && !this.firstLoaded) {
                    emptyView.showProgress();
                }
                final ListAdapter listAdapter = this.listAdapter;
                if (listAdapter != null) {
                    ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
                }
                final TLRPC.TL_messages_getArchivedStickers tl_messages_getArchivedStickers = new TLRPC.TL_messages_getArchivedStickers();
                long id;
                if (this.sets.isEmpty()) {
                    id = 0L;
                }
                else {
                    final ArrayList<TLRPC.StickerSetCovered> sets = this.sets;
                    id = sets.get(sets.size() - 1).set.id;
                }
                tl_messages_getArchivedStickers.offset_id = id;
                tl_messages_getArchivedStickers.limit = 15;
                if (this.currentType != 1) {
                    masks = false;
                }
                tl_messages_getArchivedStickers.masks = masks;
                ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(super.currentAccount).sendRequest(tl_messages_getArchivedStickers, new _$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw(this)), super.classGuid);
            }
        }
    }
    
    private void updateRows() {
        this.rowCount = 0;
        if (!this.sets.isEmpty()) {
            final int rowCount = this.rowCount;
            this.stickersStartRow = rowCount;
            this.stickersEndRow = rowCount + this.sets.size();
            this.rowCount += this.sets.size();
            if (!this.endReached) {
                this.stickersLoadingRow = this.rowCount++;
                this.stickersShadowRow = -1;
            }
            else {
                this.stickersShadowRow = this.rowCount++;
                this.stickersLoadingRow = -1;
            }
        }
        else {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersLoadingRow = -1;
            this.stickersShadowRow = -1;
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        if (this.currentType == 0) {
            super.actionBar.setTitle(LocaleController.getString("ArchivedStickers", 2131558659));
        }
        else {
            super.actionBar.setTitle(LocaleController.getString("ArchivedMasks", 2131558654));
        }
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    ArchivedStickersActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        this.emptyView = new EmptyTextProgressView(context);
        if (this.currentType == 0) {
            this.emptyView.setText(LocaleController.getString("ArchivedStickersEmpty", 2131558662));
        }
        else {
            this.emptyView.setText(LocaleController.getString("ArchivedMasksEmpty", 2131558657));
        }
        frameLayout.addView((View)this.emptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        if (this.loadingStickers) {
            this.emptyView.showProgress();
        }
        else {
            this.emptyView.showTextView();
        }
        (this.listView = new RecyclerListView(context)).setFocusable(true);
        this.listView.setEmptyView((View)this.emptyView);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)(this.layoutManager = new LinearLayoutManager(context, 1, false)));
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$ArchivedStickersActivity$a3I8kBmTW_AQtR9tR_9PNmdkhkA(this));
        this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                if (!ArchivedStickersActivity.this.loadingStickers && !ArchivedStickersActivity.this.endReached && ArchivedStickersActivity.this.layoutManager.findLastVisibleItemPosition() > ArchivedStickersActivity.this.stickersLoadingRow - 2) {
                    ArchivedStickersActivity.this.getStickers();
                }
            }
        });
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.needReloadArchivedStickers) {
            this.firstLoaded = false;
            this.endReached = false;
            this.sets.clear();
            this.updateRows();
            final EmptyTextProgressView emptyView = this.emptyView;
            if (emptyView != null) {
                emptyView.showProgress();
            }
            this.getStickers();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { ArchivedStickerSetCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { LoadingCell.class, TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, "emptyListPlaceholder"), new ThemeDescription((View)this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { LoadingCell.class }, new String[] { "progressBar" }, null, null, null, "progressCircle"), new ThemeDescription((View)this.listView, 0, new Class[] { ArchivedStickerSetCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { ArchivedStickerSetCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { ArchivedStickerSetCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrack"), new ThemeDescription((View)this.listView, 0, new Class[] { ArchivedStickerSetCell.class }, new String[] { "checkBox" }, null, null, null, "switchTrackChecked") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        this.getStickers();
        this.updateRows();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.needReloadArchivedStickers);
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.needReloadArchivedStickers);
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
            return ArchivedStickersActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n >= ArchivedStickersActivity.this.stickersStartRow && n < ArchivedStickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (n == ArchivedStickersActivity.this.stickersLoadingRow) {
                return 1;
            }
            if (n == ArchivedStickersActivity.this.stickersShadowRow) {
                return 2;
            }
            return 0;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return viewHolder.getItemViewType() == 0;
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int n) {
            if (this.getItemViewType(n) == 0) {
                final ArchivedStickerSetCell archivedStickerSetCell = (ArchivedStickerSetCell)viewHolder.itemView;
                archivedStickerSetCell.setTag((Object)n);
                final TLRPC.StickerSetCovered stickerSetCovered = ArchivedStickersActivity.this.sets.get(n);
                final int size = ArchivedStickersActivity.this.sets.size();
                boolean b = true;
                if (n == size - 1) {
                    b = false;
                }
                archivedStickerSetCell.setStickersSet(stickerSetCovered, b);
                archivedStickerSetCell.setChecked(DataQuery.getInstance(ArchivedStickersActivity.this.currentAccount).isStickerPackInstalled(stickerSetCovered.set.id));
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        frameLayout = null;
                    }
                    else {
                        frameLayout = new TextInfoPrivacyCell(this.mContext);
                        ((View)frameLayout).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                    }
                }
                else {
                    frameLayout = new LoadingCell(this.mContext);
                    ((View)frameLayout).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
            }
            else {
                frameLayout = new ArchivedStickerSetCell(this.mContext, true);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                ((ArchivedStickerSetCell)frameLayout).setOnCheckClick(new _$$Lambda$ArchivedStickersActivity$ListAdapter$51_kmSlVaMxy8__NHVDcnb9AD68(this));
            }
            ((View)frameLayout).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
