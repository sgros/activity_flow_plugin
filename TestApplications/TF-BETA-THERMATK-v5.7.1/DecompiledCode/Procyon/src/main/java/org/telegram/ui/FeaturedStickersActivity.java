// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui;

import android.view.View$OnClickListener;
import android.view.ViewGroup;
import java.util.Collection;
import android.app.Dialog;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import org.telegram.ui.Cells.FeaturedStickerSetCell;
import org.telegram.ui.ActionBar.ThemeDescription;
import android.view.ViewGroup$LayoutParams;
import org.telegram.ui.Components.LayoutHelper;
import android.view.animation.LayoutAnimationController;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ActionBar.Theme;
import android.widget.FrameLayout;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.messenger.LocaleController;
import android.view.View;
import android.content.Context;
import org.telegram.messenger.DataQuery;
import java.util.ArrayList;
import org.telegram.ui.Components.RecyclerListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import org.telegram.tgnet.TLRPC;
import android.util.LongSparseArray;
import org.telegram.messenger.NotificationCenter;
import org.telegram.ui.ActionBar.BaseFragment;

public class FeaturedStickersActivity extends BaseFragment implements NotificationCenterDelegate
{
    private LongSparseArray<TLRPC.StickerSetCovered> installingStickerSets;
    private LinearLayoutManager layoutManager;
    private ListAdapter listAdapter;
    private RecyclerListView listView;
    private int rowCount;
    private int stickersEndRow;
    private int stickersShadowRow;
    private int stickersStartRow;
    private ArrayList<Long> unreadStickers;
    
    public FeaturedStickersActivity() {
        this.unreadStickers = null;
        this.installingStickerSets = (LongSparseArray<TLRPC.StickerSetCovered>)new LongSparseArray();
    }
    
    private void updateRows() {
        this.rowCount = 0;
        final ArrayList<TLRPC.StickerSetCovered> featuredStickerSets = DataQuery.getInstance(super.currentAccount).getFeaturedStickerSets();
        if (!featuredStickerSets.isEmpty()) {
            final int rowCount = this.rowCount;
            this.stickersStartRow = rowCount;
            this.stickersEndRow = rowCount + featuredStickerSets.size();
            this.rowCount += featuredStickerSets.size();
            this.stickersShadowRow = this.rowCount++;
        }
        else {
            this.stickersStartRow = -1;
            this.stickersEndRow = -1;
            this.stickersShadowRow = -1;
        }
        final ListAdapter listAdapter = this.listAdapter;
        if (listAdapter != null) {
            ((RecyclerView.Adapter)listAdapter).notifyDataSetChanged();
        }
        DataQuery.getInstance(super.currentAccount).markFaturedStickersAsRead(true);
    }
    
    private void updateVisibleTrendingSets() {
        final LinearLayoutManager layoutManager = this.layoutManager;
        if (layoutManager == null) {
            return;
        }
        final int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == -1) {
            return;
        }
        final int lastVisibleItemPosition = this.layoutManager.findLastVisibleItemPosition();
        if (lastVisibleItemPosition == -1) {
            return;
        }
        this.listAdapter.notifyItemRangeChanged(firstVisibleItemPosition, lastVisibleItemPosition - firstVisibleItemPosition + 1);
    }
    
    @Override
    public View createView(final Context context) {
        super.actionBar.setBackButtonImage(2131165409);
        super.actionBar.setAllowOverlayTitle(true);
        super.actionBar.setTitle(LocaleController.getString("FeaturedStickers", 2131559479));
        super.actionBar.setActionBarMenuOnItemClick((ActionBar.ActionBarMenuOnItemClick)new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(final int n) {
                if (n == -1) {
                    FeaturedStickersActivity.this.finishFragment();
                }
            }
        });
        this.listAdapter = new ListAdapter(context);
        super.fragmentView = (View)new FrameLayout(context);
        final FrameLayout frameLayout = (FrameLayout)super.fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
        (this.listView = new RecyclerListView(context)).setItemAnimator(null);
        this.listView.setLayoutAnimation((LayoutAnimationController)null);
        this.listView.setFocusable(true);
        this.listView.setTag((Object)14);
        (this.layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        }).setOrientation(1);
        this.listView.setLayoutManager((RecyclerView.LayoutManager)this.layoutManager);
        frameLayout.addView((View)this.listView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f));
        this.listView.setAdapter(this.listAdapter);
        this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)new _$$Lambda$FeaturedStickersActivity$HF_uvC2bkOnxNGqLkTScu_kNf5M(this));
        return super.fragmentView;
    }
    
    @Override
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.featuredStickersDidLoad) {
            if (this.unreadStickers == null) {
                this.unreadStickers = DataQuery.getInstance(super.currentAccount).getUnreadStickerSets();
            }
            this.updateRows();
        }
        else if (n == NotificationCenter.stickersDidLoad) {
            this.updateVisibleTrendingSets();
        }
    }
    
    @Override
    public ThemeDescription[] getThemeDescriptions() {
        return new ThemeDescription[] { new ThemeDescription((View)this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[] { FeaturedStickerSetCell.class }, null, null, null, "windowBackgroundWhite"), new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "windowBackgroundGray"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, "actionBarDefault"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, "actionBarDefaultIcon"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, "actionBarDefaultTitle"), new ThemeDescription((View)super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, "actionBarDefaultSelector"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, "listSelectorSDK21"), new ThemeDescription((View)this.listView, 0, new Class[] { View.class }, Theme.dividerPaint, null, null, "divider"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[] { TextInfoPrivacyCell.class }, null, null, null, "windowBackgroundGrayShadow"), new ThemeDescription((View)this.listView, 0, new Class[] { FeaturedStickerSetCell.class }, new String[] { "progressPaint" }, null, null, null, "featuredStickers_buttonProgress"), new ThemeDescription((View)this.listView, 0, new Class[] { FeaturedStickerSetCell.class }, new String[] { "textView" }, null, null, null, "windowBackgroundWhiteBlackText"), new ThemeDescription((View)this.listView, 0, new Class[] { FeaturedStickerSetCell.class }, new String[] { "valueTextView" }, null, null, null, "windowBackgroundWhiteGrayText2"), new ThemeDescription((View)this.listView, 0, new Class[] { FeaturedStickerSetCell.class }, new String[] { "addButton" }, null, null, null, "featuredStickers_buttonText"), new ThemeDescription((View)this.listView, 0, new Class[] { FeaturedStickerSetCell.class }, new String[] { "checkImage" }, null, null, null, "featuredStickers_addedIcon"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[] { FeaturedStickerSetCell.class }, new String[] { "addButton" }, null, null, null, "featuredStickers_addButton"), new ThemeDescription((View)this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[] { FeaturedStickerSetCell.class }, new String[] { "addButton" }, null, null, null, "featuredStickers_addButtonPressed") };
    }
    
    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();
        DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        final ArrayList<Long> unreadStickerSets = DataQuery.getInstance(super.currentAccount).getUnreadStickerSets();
        if (unreadStickerSets != null) {
            this.unreadStickers = new ArrayList<Long>(unreadStickerSets);
        }
        this.updateRows();
        return true;
    }
    
    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
        NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
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
            return FeaturedStickersActivity.this.rowCount;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (n >= FeaturedStickersActivity.this.stickersStartRow && n < FeaturedStickersActivity.this.stickersEndRow) {
                return 0;
            }
            if (n == FeaturedStickersActivity.this.stickersShadowRow) {
                return 1;
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
                final ArrayList<TLRPC.StickerSetCovered> featuredStickerSets = DataQuery.getInstance(FeaturedStickersActivity.this.currentAccount).getFeaturedStickerSets();
                final FeaturedStickerSetCell featuredStickerSetCell = (FeaturedStickerSetCell)viewHolder.itemView;
                featuredStickerSetCell.setTag((Object)n);
                final TLRPC.StickerSetCovered stickerSetCovered = featuredStickerSets.get(n);
                final int size = featuredStickerSets.size();
                final boolean b = true;
                featuredStickerSetCell.setStickersSet(stickerSetCovered, n != size - 1, FeaturedStickersActivity.this.unreadStickers != null && FeaturedStickersActivity.this.unreadStickers.contains(stickerSetCovered.set.id));
                boolean drawProgress;
                final boolean b2 = drawProgress = (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(stickerSetCovered.set.id) >= 0 && b);
                if (b2) {
                    drawProgress = b2;
                    if (featuredStickerSetCell.isInstalled()) {
                        FeaturedStickersActivity.this.installingStickerSets.remove(stickerSetCovered.set.id);
                        featuredStickerSetCell.setDrawProgress(false);
                        drawProgress = false;
                    }
                }
                featuredStickerSetCell.setDrawProgress(drawProgress);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            FrameLayout frameLayout;
            if (n != 0) {
                if (n != 1) {
                    frameLayout = null;
                }
                else {
                    frameLayout = new TextInfoPrivacyCell(this.mContext);
                    ((View)frameLayout).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                }
            }
            else {
                frameLayout = new FeaturedStickerSetCell(this.mContext);
                ((View)frameLayout).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                ((FeaturedStickerSetCell)frameLayout).setAddOnClickListener((View$OnClickListener)new _$$Lambda$FeaturedStickersActivity$ListAdapter$NM7kVZE2xCqvk5vi5R9BLMmu2pk(this));
            }
            ((View)frameLayout).setLayoutParams((ViewGroup$LayoutParams)new RecyclerView.LayoutParams(-1, -2));
            return new RecyclerListView.Holder((View)frameLayout);
        }
    }
}
