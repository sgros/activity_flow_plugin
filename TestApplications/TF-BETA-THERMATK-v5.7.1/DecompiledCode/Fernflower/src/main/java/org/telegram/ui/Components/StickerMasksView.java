package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ContentPreviewViewer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.StickerEmojiCell;

public class StickerMasksView extends FrameLayout implements NotificationCenter.NotificationCenterDelegate {
   private int currentAccount;
   private int currentType;
   private int lastNotifyWidth;
   private StickerMasksView.Listener listener;
   private ArrayList[] recentStickers;
   private int recentTabBum;
   private ScrollSlidingTabStrip scrollSlidingTabStrip;
   private ArrayList[] stickerSets;
   private TextView stickersEmptyView;
   private StickerMasksView.StickersGridAdapter stickersGridAdapter;
   private RecyclerListView stickersGridView;
   private GridLayoutManager stickersLayoutManager;
   private RecyclerListView.OnItemClickListener stickersOnItemClickListener;
   private int stickersTabOffset;

   public StickerMasksView(Context var1) {
      super(var1);
      this.currentAccount = UserConfig.selectedAccount;
      this.stickerSets = new ArrayList[]{new ArrayList(), new ArrayList()};
      this.recentStickers = new ArrayList[]{new ArrayList(), new ArrayList()};
      this.currentType = 1;
      this.recentTabBum = -2;
      this.setBackgroundColor(-14540254);
      this.setClickable(true);
      DataQuery.getInstance(this.currentAccount).checkStickers(0);
      DataQuery.getInstance(this.currentAccount).checkStickers(1);
      this.stickersGridView = new RecyclerListView(var1) {
         public boolean onInterceptTouchEvent(MotionEvent var1) {
            boolean var2 = ContentPreviewViewer.getInstance().onInterceptTouchEvent(var1, StickerMasksView.this.stickersGridView, StickerMasksView.this.getMeasuredHeight(), (ContentPreviewViewer.ContentPreviewViewerDelegate)null);
            if (!super.onInterceptTouchEvent(var1) && !var2) {
               var2 = false;
            } else {
               var2 = true;
            }

            return var2;
         }
      };
      RecyclerListView var2 = this.stickersGridView;
      GridLayoutManager var3 = new GridLayoutManager(var1, 5);
      this.stickersLayoutManager = var3;
      var2.setLayoutManager(var3);
      this.stickersLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
         public int getSpanSize(int var1) {
            return var1 == StickerMasksView.this.stickersGridAdapter.totalItems ? StickerMasksView.this.stickersGridAdapter.stickersPerRow : 1;
         }
      });
      this.stickersGridView.setPadding(0, AndroidUtilities.dp(4.0F), 0, 0);
      this.stickersGridView.setClipToPadding(false);
      RecyclerListView var5 = this.stickersGridView;
      StickerMasksView.StickersGridAdapter var4 = new StickerMasksView.StickersGridAdapter(var1);
      this.stickersGridAdapter = var4;
      var5.setAdapter(var4);
      this.stickersGridView.setOnTouchListener(new _$$Lambda$StickerMasksView$UsFOY2iHtoAth4G0Jx5Am_K6Gjw(this));
      this.stickersOnItemClickListener = new _$$Lambda$StickerMasksView$dIIbEIRSoGx8_DynOnZBV9n5UHM(this);
      this.stickersGridView.setOnItemClickListener(this.stickersOnItemClickListener);
      this.stickersGridView.setGlowColor(-657673);
      this.addView(this.stickersGridView, LayoutHelper.createFrame(-1, -1.0F, 51, 0.0F, 48.0F, 0.0F, 0.0F));
      this.stickersEmptyView = new TextView(var1);
      this.stickersEmptyView.setTextSize(1, 18.0F);
      this.stickersEmptyView.setTextColor(-7829368);
      this.addView(this.stickersEmptyView, LayoutHelper.createFrame(-2, -2.0F, 17, 0.0F, 48.0F, 0.0F, 0.0F));
      this.stickersGridView.setEmptyView(this.stickersEmptyView);
      this.scrollSlidingTabStrip = new ScrollSlidingTabStrip(var1);
      this.scrollSlidingTabStrip.setBackgroundColor(-16777216);
      this.scrollSlidingTabStrip.setUnderlineHeight(AndroidUtilities.dp(1.0F));
      this.scrollSlidingTabStrip.setIndicatorColor(-10305560);
      this.scrollSlidingTabStrip.setUnderlineColor(-15066598);
      this.scrollSlidingTabStrip.setIndicatorHeight(AndroidUtilities.dp(1.0F) + 1);
      this.addView(this.scrollSlidingTabStrip, LayoutHelper.createFrame(-1, 48, 51));
      this.updateStickerTabs();
      this.scrollSlidingTabStrip.setDelegate(new _$$Lambda$StickerMasksView$AhwYOOvx1aUeFtWqQPYn16OGWYU(this));
      this.stickersGridView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            StickerMasksView.this.checkScroll();
         }
      });
   }

   private void checkDocuments() {
      int var1 = this.recentStickers[this.currentType].size();
      this.recentStickers[this.currentType] = DataQuery.getInstance(this.currentAccount).getRecentStickers(this.currentType);
      StickerMasksView.StickersGridAdapter var2 = this.stickersGridAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

      if (var1 != this.recentStickers[this.currentType].size()) {
         this.updateStickerTabs();
      }

   }

   private void checkPanels() {
      if (this.scrollSlidingTabStrip != null) {
         int var1 = this.stickersLayoutManager.findFirstVisibleItemPosition();
         if (var1 != -1) {
            ScrollSlidingTabStrip var2 = this.scrollSlidingTabStrip;
            int var3 = this.stickersGridAdapter.getTabForPosition(var1);
            var1 = this.recentTabBum;
            if (var1 <= 0) {
               var1 = this.stickersTabOffset;
            }

            var2.onPageScrolled(var3 + 1, var1 + 1);
         }

      }
   }

   private void checkScroll() {
      int var1 = this.stickersLayoutManager.findFirstVisibleItemPosition();
      if (var1 != -1) {
         this.checkStickersScroll(var1);
      }
   }

   private void checkStickersScroll(int var1) {
      if (this.stickersGridView != null) {
         ScrollSlidingTabStrip var2 = this.scrollSlidingTabStrip;
         int var3 = this.stickersGridAdapter.getTabForPosition(var1);
         var1 = this.recentTabBum;
         if (var1 <= 0) {
            var1 = this.stickersTabOffset;
         }

         var2.onPageScrolled(var3 + 1, var1 + 1);
      }
   }

   private void reloadStickersAdapter() {
      StickerMasksView.StickersGridAdapter var1 = this.stickersGridAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

      if (ContentPreviewViewer.getInstance().isVisible()) {
         ContentPreviewViewer.getInstance().close();
      }

      ContentPreviewViewer.getInstance().reset();
   }

   private void updateStickerTabs() {
      ScrollSlidingTabStrip var1 = this.scrollSlidingTabStrip;
      if (var1 != null) {
         this.recentTabBum = -2;
         this.stickersTabOffset = 0;
         int var2 = var1.getCurrentPosition();
         this.scrollSlidingTabStrip.removeTabs();
         Drawable var6;
         if (this.currentType == 0) {
            var6 = this.getContext().getResources().getDrawable(2131165449);
            Theme.setDrawableColorByKey(var6, "chat_emojiPanelIcon");
            this.scrollSlidingTabStrip.addIconTab(var6);
            this.stickersEmptyView.setText(LocaleController.getString("NoStickers", 2131559953));
         } else {
            var6 = this.getContext().getResources().getDrawable(2131165451);
            Theme.setDrawableColorByKey(var6, "chat_emojiPanelIcon");
            this.scrollSlidingTabStrip.addIconTab(var6);
            this.stickersEmptyView.setText(LocaleController.getString("NoMasks", 2131559928));
         }

         int var3;
         if (!this.recentStickers[this.currentType].isEmpty()) {
            var3 = this.stickersTabOffset;
            this.recentTabBum = var3;
            this.stickersTabOffset = var3 + 1;
            this.scrollSlidingTabStrip.addIconTab(Theme.createEmojiIconSelectorDrawable(this.getContext(), 2131165450, Theme.getColor("chat_emojiPanelMasksIcon"), Theme.getColor("chat_emojiPanelMasksIconSelected")));
         }

         this.stickerSets[this.currentType].clear();
         ArrayList var4 = DataQuery.getInstance(this.currentAccount).getStickerSets(this.currentType);

         for(var3 = 0; var3 < var4.size(); ++var3) {
            TLRPC.TL_messages_stickerSet var5 = (TLRPC.TL_messages_stickerSet)var4.get(var3);
            if (!var5.set.archived) {
               ArrayList var7 = var5.documents;
               if (var7 != null && !var7.isEmpty()) {
                  this.stickerSets[this.currentType].add(var5);
               }
            }
         }

         for(var3 = 0; var3 < this.stickerSets[this.currentType].size(); ++var3) {
            TLRPC.TL_messages_stickerSet var8 = (TLRPC.TL_messages_stickerSet)this.stickerSets[this.currentType].get(var3);
            TLRPC.Document var9 = (TLRPC.Document)var8.documents.get(0);
            this.scrollSlidingTabStrip.addStickerTab(var9, var9, var8);
         }

         this.scrollSlidingTabStrip.updateTabStyles();
         if (var2 != 0) {
            this.scrollSlidingTabStrip.onPageScrolled(var2, var2);
         }

         this.checkPanels();
      }
   }

   public void addRecentSticker(TLRPC.Document var1) {
      if (var1 != null) {
         DataQuery.getInstance(this.currentAccount).addRecentSticker(this.currentType, (Object)null, var1, (int)(System.currentTimeMillis() / 1000L), false);
         boolean var2 = this.recentStickers[this.currentType].isEmpty();
         this.recentStickers[this.currentType] = DataQuery.getInstance(this.currentAccount).getRecentStickers(this.currentType);
         StickerMasksView.StickersGridAdapter var3 = this.stickersGridAdapter;
         if (var3 != null) {
            var3.notifyDataSetChanged();
         }

         if (var2) {
            this.updateStickerTabs();
         }

      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.stickersDidLoad) {
         if ((Integer)var3[0] == this.currentType) {
            this.updateStickerTabs();
            this.reloadStickersAdapter();
            this.checkPanels();
         }
      } else if (var1 == NotificationCenter.recentDocumentsDidLoad && !(Boolean)var3[0] && (Integer)var3[1] == this.currentType) {
         this.checkDocuments();
      }

   }

   public int getCurrentType() {
      return this.currentType;
   }

   // $FF: synthetic method
   public boolean lambda$new$0$StickerMasksView(View var1, MotionEvent var2) {
      return ContentPreviewViewer.getInstance().onTouch(var2, this.stickersGridView, this.getMeasuredHeight(), this.stickersOnItemClickListener, (ContentPreviewViewer.ContentPreviewViewerDelegate)null);
   }

   // $FF: synthetic method
   public void lambda$new$1$StickerMasksView(View var1, int var2) {
      if (var1 instanceof StickerEmojiCell) {
         ContentPreviewViewer.getInstance().reset();
         StickerEmojiCell var3 = (StickerEmojiCell)var1;
         if (!var3.isDisabled()) {
            TLRPC.Document var4 = var3.getSticker();
            Object var5 = var3.getParentObject();
            this.listener.onStickerSelected(var5, var4);
            DataQuery.getInstance(this.currentAccount).addRecentSticker(1, var5, var4, (int)(System.currentTimeMillis() / 1000L), false);
            MessagesController.getInstance(this.currentAccount).saveRecentSticker(var5, var4, true);
         }
      }
   }

   // $FF: synthetic method
   public void lambda$new$2$StickerMasksView(int var1) {
      if (var1 == 0) {
         if (this.currentType == 0) {
            this.currentType = 1;
         } else {
            this.currentType = 0;
         }

         StickerMasksView.Listener var2 = this.listener;
         if (var2 != null) {
            var2.onTypeChanged();
         }

         this.recentStickers[this.currentType] = DataQuery.getInstance(this.currentAccount).getRecentStickers(this.currentType);
         this.stickersLayoutManager.scrollToPositionWithOffset(0, 0);
         this.updateStickerTabs();
         this.reloadStickersAdapter();
         this.checkDocuments();
         this.checkPanels();
      } else if (var1 == this.recentTabBum + 1) {
         this.stickersLayoutManager.scrollToPositionWithOffset(0, 0);
      } else {
         int var3 = var1 - 1 - this.stickersTabOffset;
         var1 = var3;
         if (var3 >= this.stickerSets[this.currentType].size()) {
            var1 = this.stickerSets[this.currentType].size() - 1;
         }

         this.stickersLayoutManager.scrollToPositionWithOffset(this.stickersGridAdapter.getPositionForPack((TLRPC.TL_messages_stickerSet)this.stickerSets[this.currentType].get(var1)), 0);
         this.checkScroll();
      }
   }

   // $FF: synthetic method
   public void lambda$onAttachedToWindow$3$StickerMasksView() {
      this.updateStickerTabs();
      this.reloadStickersAdapter();
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

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      int var6 = this.lastNotifyWidth;
      int var7 = var4 - var2;
      if (var6 != var7) {
         this.lastNotifyWidth = var7;
         this.reloadStickersAdapter();
      }

      super.onLayout(var1, var2, var3, var4, var5);
   }

   public void setListener(StickerMasksView.Listener var1) {
      this.listener = var1;
   }

   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (var1 != 8) {
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

   public interface Listener {
      void onStickerSelected(Object var1, TLRPC.Document var2);

      void onTypeChanged();
   }

   private class StickersGridAdapter extends RecyclerListView.SelectionAdapter {
      private SparseArray cache = new SparseArray();
      private Context context;
      private HashMap packStartRow = new HashMap();
      private SparseArray positionsToSets = new SparseArray();
      private SparseArray rowStartPack = new SparseArray();
      private int stickersPerRow;
      private int totalItems;

      public StickersGridAdapter(Context var2) {
         this.context = var2;
      }

      public Object getItem(int var1) {
         return this.cache.get(var1);
      }

      public int getItemCount() {
         int var1 = this.totalItems;
         if (var1 != 0) {
            ++var1;
         } else {
            var1 = 0;
         }

         return var1;
      }

      public int getItemViewType(int var1) {
         return this.cache.get(var1) != null ? 0 : 1;
      }

      public int getPositionForPack(TLRPC.TL_messages_stickerSet var1) {
         return (Integer)this.packStartRow.get(var1) * this.stickersPerRow;
      }

      public int getTabForPosition(int var1) {
         if (this.stickersPerRow == 0) {
            int var2 = StickerMasksView.this.getMeasuredWidth();
            int var3 = var2;
            if (var2 == 0) {
               var3 = AndroidUtilities.displaySize.x;
            }

            this.stickersPerRow = var3 / AndroidUtilities.dp(72.0F);
         }

         var1 /= this.stickersPerRow;
         TLRPC.TL_messages_stickerSet var4 = (TLRPC.TL_messages_stickerSet)this.rowStartPack.get(var1);
         return var4 == null ? StickerMasksView.this.recentTabBum : StickerMasksView.this.stickerSets[StickerMasksView.this.currentType].indexOf(var4) + StickerMasksView.this.stickersTabOffset;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return false;
      }

      public void notifyDataSetChanged() {
         int var1 = StickerMasksView.this.getMeasuredWidth();
         int var2 = var1;
         if (var1 == 0) {
            var2 = AndroidUtilities.displaySize.x;
         }

         this.stickersPerRow = var2 / AndroidUtilities.dp(72.0F);
         StickerMasksView.this.stickersLayoutManager.setSpanCount(this.stickersPerRow);
         this.rowStartPack.clear();
         this.packStartRow.clear();
         this.cache.clear();
         this.positionsToSets.clear();
         this.totalItems = 0;
         ArrayList var3 = StickerMasksView.this.stickerSets[StickerMasksView.this.currentType];

         for(var2 = -1; var2 < var3.size(); ++var2) {
            TLRPC.TL_messages_stickerSet var4 = null;
            int var5 = this.totalItems / this.stickersPerRow;
            ArrayList var6;
            if (var2 == -1) {
               var6 = StickerMasksView.this.recentStickers[StickerMasksView.this.currentType];
            } else {
               var4 = (TLRPC.TL_messages_stickerSet)var3.get(var2);
               var6 = var4.documents;
               this.packStartRow.put(var4, var5);
            }

            if (!var6.isEmpty()) {
               int var7 = (int)Math.ceil((double)((float)var6.size() / (float)this.stickersPerRow));

               for(var1 = 0; var1 < var6.size(); ++var1) {
                  this.cache.put(this.totalItems + var1, var6.get(var1));
                  this.positionsToSets.put(this.totalItems + var1, var4);
               }

               this.totalItems += this.stickersPerRow * var7;

               for(var1 = 0; var1 < var7; ++var1) {
                  this.rowStartPack.put(var5 + var1, var4);
               }
            }
         }

         super.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 == 1) {
               if (var2 == this.totalItems) {
                  var2 = (var2 - 1) / this.stickersPerRow;
                  TLRPC.TL_messages_stickerSet var4 = (TLRPC.TL_messages_stickerSet)this.rowStartPack.get(var2);
                  if (var4 == null) {
                     ((EmptyCell)var1.itemView).setHeight(1);
                  } else {
                     var2 = StickerMasksView.this.stickersGridView.getMeasuredHeight() - (int)Math.ceil((double)((float)var4.documents.size() / (float)this.stickersPerRow)) * AndroidUtilities.dp(82.0F);
                     EmptyCell var5 = (EmptyCell)var1.itemView;
                     if (var2 <= 0) {
                        var2 = 1;
                     }

                     var5.setHeight(var2);
                  }
               } else {
                  ((EmptyCell)var1.itemView).setHeight(AndroidUtilities.dp(82.0F));
               }
            }
         } else {
            TLRPC.Document var6 = (TLRPC.Document)this.cache.get(var2);
            ((StickerEmojiCell)var1.itemView).setSticker(var6, this.positionsToSets.get(var2), false);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = null;
            } else {
               var3 = new EmptyCell(this.context);
            }
         } else {
            var3 = new StickerEmojiCell(this.context) {
               public void onMeasure(int var1, int var2) {
                  super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(82.0F), 1073741824));
               }
            };
         }

         return new RecyclerListView.Holder((View)var3);
      }
   }
}
