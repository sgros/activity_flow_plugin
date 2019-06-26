// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components;

import android.view.View$MeasureSpec;
import android.view.ViewGroup;
import org.telegram.ui.Cells.EmptyCell;
import java.util.HashMap;
import android.util.SparseArray;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.Cells.StickerEmojiCell;
import android.graphics.drawable.Drawable;
import org.telegram.tgnet.TLObject;
import org.telegram.messenger.LocaleController;
import org.telegram.ui.ActionBar.Theme;
import android.view.ViewGroup$LayoutParams;
import android.view.View;
import android.view.View$OnTouchListener;
import org.telegram.messenger.AndroidUtilities;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.ui.ContentPreviewViewer;
import android.view.MotionEvent;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.UserConfig;
import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import android.widget.TextView;
import org.telegram.tgnet.TLRPC;
import java.util.ArrayList;
import org.telegram.messenger.NotificationCenter;
import android.widget.FrameLayout;

public class StickerMasksView extends FrameLayout implements NotificationCenterDelegate
{
    private int currentAccount;
    private int currentType;
    private int lastNotifyWidth;
    private Listener listener;
    private ArrayList<TLRPC.Document>[] recentStickers;
    private int recentTabBum;
    private ScrollSlidingTabStrip scrollSlidingTabStrip;
    private ArrayList<TLRPC.TL_messages_stickerSet>[] stickerSets;
    private TextView stickersEmptyView;
    private StickersGridAdapter stickersGridAdapter;
    private RecyclerListView stickersGridView;
    private GridLayoutManager stickersLayoutManager;
    private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
    private int stickersTabOffset;
    
    public StickerMasksView(final Context context) {
        super(context);
        this.currentAccount = UserConfig.selectedAccount;
        this.stickerSets = (ArrayList<TLRPC.TL_messages_stickerSet>[])new ArrayList[] { new ArrayList(), new ArrayList() };
        this.recentStickers = (ArrayList<TLRPC.Document>[])new ArrayList[] { new ArrayList(), new ArrayList() };
        this.currentType = 1;
        this.recentTabBum = -2;
        this.setBackgroundColor(-14540254);
        this.setClickable(true);
        DataQuery.getInstance(this.currentAccount).checkStickers(0);
        DataQuery.getInstance(this.currentAccount).checkStickers(1);
        (this.stickersGridView = new RecyclerListView(context) {
            @Override
            public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
                final boolean onInterceptTouchEvent = ContentPreviewViewer.getInstance().onInterceptTouchEvent(motionEvent, StickerMasksView.this.stickersGridView, StickerMasksView.this.getMeasuredHeight(), null);
                return super.onInterceptTouchEvent(motionEvent) || onInterceptTouchEvent;
            }
        }).setLayoutManager((RecyclerView.LayoutManager)(this.stickersLayoutManager = new GridLayoutManager(context, 5)));
        this.stickersLayoutManager.setSpanSizeLookup((GridLayoutManager.SpanSizeLookup)new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(final int n) {
                if (n == StickerMasksView.this.stickersGridAdapter.totalItems) {
                    return StickerMasksView.this.stickersGridAdapter.stickersPerRow;
                }
                return 1;
            }
        });
        this.stickersGridView.setPadding(0, AndroidUtilities.dp(4.0f), 0, 0);
        this.stickersGridView.setClipToPadding(false);
        this.stickersGridView.setAdapter(this.stickersGridAdapter = new StickersGridAdapter(context));
        this.stickersGridView.setOnTouchListener((View$OnTouchListener)new _$$Lambda$StickerMasksView$UsFOY2iHtoAth4G0Jx5Am_K6Gjw(this));
        this.stickersOnItemClickListener = new _$$Lambda$StickerMasksView$dIIbEIRSoGx8_DynOnZBV9n5UHM(this);
        this.stickersGridView.setOnItemClickListener(this.stickersOnItemClickListener);
        this.stickersGridView.setGlowColor(-657673);
        this.addView((View)this.stickersGridView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, -1.0f, 51, 0.0f, 48.0f, 0.0f, 0.0f));
        (this.stickersEmptyView = new TextView(context)).setTextSize(1, 18.0f);
        this.stickersEmptyView.setTextColor(-7829368);
        this.addView((View)this.stickersEmptyView, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-2, -2.0f, 17, 0.0f, 48.0f, 0.0f, 0.0f));
        this.stickersGridView.setEmptyView((View)this.stickersEmptyView);
        (this.scrollSlidingTabStrip = new ScrollSlidingTabStrip(context)).setBackgroundColor(-16777216);
        this.scrollSlidingTabStrip.setUnderlineHeight(AndroidUtilities.dp(1.0f));
        this.scrollSlidingTabStrip.setIndicatorColor(-10305560);
        this.scrollSlidingTabStrip.setUnderlineColor(-15066598);
        this.scrollSlidingTabStrip.setIndicatorHeight(AndroidUtilities.dp(1.0f) + 1);
        this.addView((View)this.scrollSlidingTabStrip, (ViewGroup$LayoutParams)LayoutHelper.createFrame(-1, 48, 51));
        this.updateStickerTabs();
        this.scrollSlidingTabStrip.setDelegate((ScrollSlidingTabStrip.ScrollSlidingTabStripDelegate)new _$$Lambda$StickerMasksView$AhwYOOvx1aUeFtWqQPYn16OGWYU(this));
        this.stickersGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                StickerMasksView.this.checkScroll();
            }
        });
    }
    
    private void checkDocuments() {
        final int size = this.recentStickers[this.currentType].size();
        this.recentStickers[this.currentType] = DataQuery.getInstance(this.currentAccount).getRecentStickers(this.currentType);
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        if (size != this.recentStickers[this.currentType].size()) {
            this.updateStickerTabs();
        }
    }
    
    private void checkPanels() {
        if (this.scrollSlidingTabStrip == null) {
            return;
        }
        final int firstVisibleItemPosition = this.stickersLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition != -1) {
            final ScrollSlidingTabStrip scrollSlidingTabStrip = this.scrollSlidingTabStrip;
            final int tabForPosition = this.stickersGridAdapter.getTabForPosition(firstVisibleItemPosition);
            int n = this.recentTabBum;
            if (n <= 0) {
                n = this.stickersTabOffset;
            }
            scrollSlidingTabStrip.onPageScrolled(tabForPosition + 1, n + 1);
        }
    }
    
    private void checkScroll() {
        final int firstVisibleItemPosition = this.stickersLayoutManager.findFirstVisibleItemPosition();
        if (firstVisibleItemPosition == -1) {
            return;
        }
        this.checkStickersScroll(firstVisibleItemPosition);
    }
    
    private void checkStickersScroll(int n) {
        if (this.stickersGridView == null) {
            return;
        }
        final ScrollSlidingTabStrip scrollSlidingTabStrip = this.scrollSlidingTabStrip;
        final int tabForPosition = this.stickersGridAdapter.getTabForPosition(n);
        n = this.recentTabBum;
        if (n <= 0) {
            n = this.stickersTabOffset;
        }
        scrollSlidingTabStrip.onPageScrolled(tabForPosition + 1, n + 1);
    }
    
    private void reloadStickersAdapter() {
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        if (ContentPreviewViewer.getInstance().isVisible()) {
            ContentPreviewViewer.getInstance().close();
        }
        ContentPreviewViewer.getInstance().reset();
    }
    
    private void updateStickerTabs() {
        final ScrollSlidingTabStrip scrollSlidingTabStrip = this.scrollSlidingTabStrip;
        if (scrollSlidingTabStrip == null) {
            return;
        }
        this.recentTabBum = -2;
        this.stickersTabOffset = 0;
        final int currentPosition = scrollSlidingTabStrip.getCurrentPosition();
        this.scrollSlidingTabStrip.removeTabs();
        if (this.currentType == 0) {
            final Drawable drawable = this.getContext().getResources().getDrawable(2131165449);
            Theme.setDrawableColorByKey(drawable, "chat_emojiPanelIcon");
            this.scrollSlidingTabStrip.addIconTab(drawable);
            this.stickersEmptyView.setText((CharSequence)LocaleController.getString("NoStickers", 2131559953));
        }
        else {
            final Drawable drawable2 = this.getContext().getResources().getDrawable(2131165451);
            Theme.setDrawableColorByKey(drawable2, "chat_emojiPanelIcon");
            this.scrollSlidingTabStrip.addIconTab(drawable2);
            this.stickersEmptyView.setText((CharSequence)LocaleController.getString("NoMasks", 2131559928));
        }
        if (!this.recentStickers[this.currentType].isEmpty()) {
            final int stickersTabOffset = this.stickersTabOffset;
            this.recentTabBum = stickersTabOffset;
            this.stickersTabOffset = stickersTabOffset + 1;
            this.scrollSlidingTabStrip.addIconTab(Theme.createEmojiIconSelectorDrawable(this.getContext(), 2131165450, Theme.getColor("chat_emojiPanelMasksIcon"), Theme.getColor("chat_emojiPanelMasksIconSelected")));
        }
        this.stickerSets[this.currentType].clear();
        final ArrayList<TLRPC.TL_messages_stickerSet> stickerSets = DataQuery.getInstance(this.currentAccount).getStickerSets(this.currentType);
        for (int i = 0; i < stickerSets.size(); ++i) {
            final TLRPC.TL_messages_stickerSet e = stickerSets.get(i);
            if (!e.set.archived) {
                final ArrayList<TLRPC.Document> documents = e.documents;
                if (documents != null) {
                    if (!documents.isEmpty()) {
                        this.stickerSets[this.currentType].add(e);
                    }
                }
            }
        }
        for (int j = 0; j < this.stickerSets[this.currentType].size(); ++j) {
            final TLRPC.TL_messages_stickerSet set = this.stickerSets[this.currentType].get(j);
            final TLRPC.Document document = set.documents.get(0);
            this.scrollSlidingTabStrip.addStickerTab(document, document, set);
        }
        this.scrollSlidingTabStrip.updateTabStyles();
        if (currentPosition != 0) {
            this.scrollSlidingTabStrip.onPageScrolled(currentPosition, currentPosition);
        }
        this.checkPanels();
    }
    
    public void addRecentSticker(final TLRPC.Document document) {
        if (document == null) {
            return;
        }
        DataQuery.getInstance(this.currentAccount).addRecentSticker(this.currentType, null, document, (int)(System.currentTimeMillis() / 1000L), false);
        final boolean empty = this.recentStickers[this.currentType].isEmpty();
        this.recentStickers[this.currentType] = DataQuery.getInstance(this.currentAccount).getRecentStickers(this.currentType);
        final StickersGridAdapter stickersGridAdapter = this.stickersGridAdapter;
        if (stickersGridAdapter != null) {
            stickersGridAdapter.notifyDataSetChanged();
        }
        if (empty) {
            this.updateStickerTabs();
        }
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.stickersDidLoad) {
            if ((int)array[0] == this.currentType) {
                this.updateStickerTabs();
                this.reloadStickersAdapter();
                this.checkPanels();
            }
        }
        else if (n == NotificationCenter.recentDocumentsDidLoad && !(boolean)array[0] && (int)array[1] == this.currentType) {
            this.checkDocuments();
        }
    }
    
    public int getCurrentType() {
        return this.currentType;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentImagesDidLoad);
        AndroidUtilities.runOnUIThread(new _$$Lambda$StickerMasksView$77XyfxOn3O_qUeajE_5_7BnPFnM(this));
    }
    
    public void onDestroy() {
        if (this.stickersGridAdapter != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.recentDocumentsDidLoad);
        }
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        final int lastNotifyWidth = this.lastNotifyWidth;
        final int lastNotifyWidth2 = n3 - n;
        if (lastNotifyWidth != lastNotifyWidth2) {
            this.lastNotifyWidth = lastNotifyWidth2;
            this.reloadStickersAdapter();
        }
        super.onLayout(b, n, n2, n3, n4);
    }
    
    public void setListener(final Listener listener) {
        this.listener = listener;
    }
    
    public void setVisibility(final int visibility) {
        super.setVisibility(visibility);
        if (visibility != 8) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.recentDocumentsDidLoad);
            this.updateStickerTabs();
            this.reloadStickersAdapter();
            this.checkDocuments();
            DataQuery.getInstance(this.currentAccount).loadRecents(0, false, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(1, false, true, false);
            DataQuery.getInstance(this.currentAccount).loadRecents(2, false, true, false);
        }
    }
    
    public interface Listener
    {
        void onStickerSelected(final Object p0, final TLRPC.Document p1);
        
        void onTypeChanged();
    }
    
    private class StickersGridAdapter extends SelectionAdapter
    {
        private SparseArray<TLRPC.Document> cache;
        private Context context;
        private HashMap<TLRPC.TL_messages_stickerSet, Integer> packStartRow;
        private SparseArray<TLRPC.TL_messages_stickerSet> positionsToSets;
        private SparseArray<TLRPC.TL_messages_stickerSet> rowStartPack;
        private int stickersPerRow;
        private int totalItems;
        
        public StickersGridAdapter(final Context context) {
            this.rowStartPack = (SparseArray<TLRPC.TL_messages_stickerSet>)new SparseArray();
            this.packStartRow = new HashMap<TLRPC.TL_messages_stickerSet, Integer>();
            this.cache = (SparseArray<TLRPC.Document>)new SparseArray();
            this.positionsToSets = (SparseArray<TLRPC.TL_messages_stickerSet>)new SparseArray();
            this.context = context;
        }
        
        public Object getItem(final int n) {
            return this.cache.get(n);
        }
        
        @Override
        public int getItemCount() {
            int totalItems = this.totalItems;
            if (totalItems != 0) {
                ++totalItems;
            }
            else {
                totalItems = 0;
            }
            return totalItems;
        }
        
        @Override
        public int getItemViewType(final int n) {
            if (this.cache.get(n) != null) {
                return 0;
            }
            return 1;
        }
        
        public int getPositionForPack(final TLRPC.TL_messages_stickerSet key) {
            return this.packStartRow.get(key) * this.stickersPerRow;
        }
        
        public int getTabForPosition(int n) {
            if (this.stickersPerRow == 0) {
                int n2;
                if ((n2 = StickerMasksView.this.getMeasuredWidth()) == 0) {
                    n2 = AndroidUtilities.displaySize.x;
                }
                this.stickersPerRow = n2 / AndroidUtilities.dp(72.0f);
            }
            n /= this.stickersPerRow;
            final TLRPC.TL_messages_stickerSet o = (TLRPC.TL_messages_stickerSet)this.rowStartPack.get(n);
            if (o == null) {
                return StickerMasksView.this.recentTabBum;
            }
            return StickerMasksView.this.stickerSets[StickerMasksView.this.currentType].indexOf(o) + StickerMasksView.this.stickersTabOffset;
        }
        
        @Override
        public boolean isEnabled(final ViewHolder viewHolder) {
            return false;
        }
        
        @Override
        public void notifyDataSetChanged() {
            int n;
            if ((n = StickerMasksView.this.getMeasuredWidth()) == 0) {
                n = AndroidUtilities.displaySize.x;
            }
            this.stickersPerRow = n / AndroidUtilities.dp(72.0f);
            StickerMasksView.this.stickersLayoutManager.setSpanCount(this.stickersPerRow);
            this.rowStartPack.clear();
            this.packStartRow.clear();
            this.cache.clear();
            this.positionsToSets.clear();
            this.totalItems = 0;
            final ArrayList list = StickerMasksView.this.stickerSets[StickerMasksView.this.currentType];
            for (int i = -1; i < list.size(); ++i) {
                TLRPC.TL_messages_stickerSet key = null;
                final int j = this.totalItems / this.stickersPerRow;
                ArrayList<TLRPC.Document> documents;
                if (i == -1) {
                    documents = StickerMasksView.this.recentStickers[StickerMasksView.this.currentType];
                }
                else {
                    key = list.get(i);
                    documents = key.documents;
                    this.packStartRow.put(key, j);
                }
                if (!documents.isEmpty()) {
                    final int n2 = (int)Math.ceil(documents.size() / (float)this.stickersPerRow);
                    for (int k = 0; k < documents.size(); ++k) {
                        this.cache.put(this.totalItems + k, (Object)documents.get(k));
                        this.positionsToSets.put(this.totalItems + k, (Object)key);
                    }
                    this.totalItems += this.stickersPerRow * n2;
                    for (int l = 0; l < n2; ++l) {
                        this.rowStartPack.put(j + l, (Object)key);
                    }
                }
            }
            super.notifyDataSetChanged();
        }
        
        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int height) {
            final int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != 0) {
                if (itemViewType == 1) {
                    if (height == this.totalItems) {
                        height = (height - 1) / this.stickersPerRow;
                        final TLRPC.TL_messages_stickerSet set = (TLRPC.TL_messages_stickerSet)this.rowStartPack.get(height);
                        if (set == null) {
                            ((EmptyCell)viewHolder.itemView).setHeight(1);
                        }
                        else {
                            height = StickerMasksView.this.stickersGridView.getMeasuredHeight() - (int)Math.ceil(set.documents.size() / (float)this.stickersPerRow) * AndroidUtilities.dp(82.0f);
                            final EmptyCell emptyCell = (EmptyCell)viewHolder.itemView;
                            if (height <= 0) {
                                height = 1;
                            }
                            emptyCell.setHeight(height);
                        }
                    }
                    else {
                        ((EmptyCell)viewHolder.itemView).setHeight(AndroidUtilities.dp(82.0f));
                    }
                }
            }
            else {
                ((StickerEmojiCell)viewHolder.itemView).setSticker((TLRPC.Document)this.cache.get(height), this.positionsToSets.get(height), false);
            }
        }
        
        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
            Object o;
            if (n != 0) {
                if (n != 1) {
                    o = null;
                }
                else {
                    o = new EmptyCell(this.context);
                }
            }
            else {
                o = new StickerEmojiCell(this.context) {
                    public void onMeasure(final int n, final int n2) {
                        super.onMeasure(n, View$MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0f), 1073741824));
                    }
                };
            }
            return new RecyclerListView.Holder((View)o);
        }
    }
}
