package org.telegram.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.FeaturedStickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;

public class FeaturedStickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private LongSparseArray installingStickerSets = new LongSparseArray();
   private LinearLayoutManager layoutManager;
   private FeaturedStickersActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int rowCount;
   private int stickersEndRow;
   private int stickersShadowRow;
   private int stickersStartRow;
   private ArrayList unreadStickers = null;

   // $FF: synthetic method
   static int access$100(FeaturedStickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(FeaturedStickersActivity var0) {
      return var0.currentAccount;
   }

   private void updateRows() {
      this.rowCount = 0;
      ArrayList var1 = DataQuery.getInstance(super.currentAccount).getFeaturedStickerSets();
      if (!var1.isEmpty()) {
         int var2 = this.rowCount;
         this.stickersStartRow = var2;
         this.stickersEndRow = var2 + var1.size();
         this.rowCount += var1.size();
         var2 = this.rowCount++;
         this.stickersShadowRow = var2;
      } else {
         this.stickersStartRow = -1;
         this.stickersEndRow = -1;
         this.stickersShadowRow = -1;
      }

      FeaturedStickersActivity.ListAdapter var3 = this.listAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

      DataQuery.getInstance(super.currentAccount).markFaturedStickersAsRead(true);
   }

   private void updateVisibleTrendingSets() {
      LinearLayoutManager var1 = this.layoutManager;
      if (var1 != null) {
         int var2 = var1.findFirstVisibleItemPosition();
         if (var2 != -1) {
            int var3 = this.layoutManager.findLastVisibleItemPosition();
            if (var3 != -1) {
               this.listAdapter.notifyItemRangeChanged(var2, var3 - var2 + 1);
            }
         }
      }
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setTitle(LocaleController.getString("FeaturedStickers", 2131559479));
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               FeaturedStickersActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new FeaturedStickersActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setItemAnimator((RecyclerView.ItemAnimator)null);
      this.listView.setLayoutAnimation((LayoutAnimationController)null);
      this.listView.setFocusable(true);
      this.listView.setTag(14);
      this.layoutManager = new LinearLayoutManager(var1) {
         public boolean supportsPredictiveItemAnimations() {
            return false;
         }
      };
      this.layoutManager.setOrientation(1);
      this.listView.setLayoutManager(this.layoutManager);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$FeaturedStickersActivity$HF_uvC2bkOnxNGqLkTScu_kNf5M(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.featuredStickersDidLoad) {
         if (this.unreadStickers == null) {
            this.unreadStickers = DataQuery.getInstance(super.currentAccount).getUnreadStickerSets();
         }

         this.updateRows();
      } else if (var1 == NotificationCenter.stickersDidLoad) {
         this.updateVisibleTrendingSets();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{FeaturedStickerSetCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"progressPaint"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_buttonProgress"), new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_buttonText"), new ThemeDescription(this.listView, 0, new Class[]{FeaturedStickerSetCell.class}, new String[]{"checkImage"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addedIcon"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addButton"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{FeaturedStickerSetCell.class}, new String[]{"addButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "featuredStickers_addButtonPressed")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$FeaturedStickersActivity(View var1, int var2) {
      if (var2 >= this.stickersStartRow && var2 < this.stickersEndRow && this.getParentActivity() != null) {
         TLRPC.StickerSetCovered var3 = (TLRPC.StickerSetCovered)DataQuery.getInstance(super.currentAccount).getFeaturedStickerSets().get(var2);
         Object var4;
         if (var3.set.id != 0L) {
            var4 = new TLRPC.TL_inputStickerSetID();
            ((TLRPC.InputStickerSet)var4).id = var3.set.id;
         } else {
            var4 = new TLRPC.TL_inputStickerSetShortName();
            ((TLRPC.InputStickerSet)var4).short_name = var3.set.short_name;
         }

         ((TLRPC.InputStickerSet)var4).access_hash = var3.set.access_hash;
         StickersAlert var5 = new StickersAlert(this.getParentActivity(), this, (TLRPC.InputStickerSet)var4, (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null);
         var5.setInstallDelegate(new FeaturedStickersActivity$3(this, var1, var3));
         this.showDialog(var5);
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
      ArrayList var1 = DataQuery.getInstance(super.currentAccount).getUnreadStickerSets();
      if (var1 != null) {
         this.unreadStickers = new ArrayList(var1);
      }

      this.updateRows();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
   }

   public void onResume() {
      super.onResume();
      FeaturedStickersActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      public int getItemCount() {
         return FeaturedStickersActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 >= FeaturedStickersActivity.this.stickersStartRow && var1 < FeaturedStickersActivity.this.stickersEndRow) {
            return 0;
         } else {
            return var1 == FeaturedStickersActivity.this.stickersShadowRow ? 1 : 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         boolean var2;
         if (var1.getItemViewType() == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$0$FeaturedStickersActivity$ListAdapter(View var1) {
         FeaturedStickerSetCell var3 = (FeaturedStickerSetCell)var1.getParent();
         TLRPC.StickerSetCovered var2 = var3.getStickerSet();
         if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(var2.set.id) < 0) {
            FeaturedStickersActivity.this.installingStickerSets.put(var2.set.id, var2);
            DataQuery.getInstance(FeaturedStickersActivity.access$700(FeaturedStickersActivity.this)).removeStickersSet(FeaturedStickersActivity.this.getParentActivity(), var2.set, 2, FeaturedStickersActivity.this, false);
            var3.setDrawProgress(true);
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (this.getItemViewType(var2) == 0) {
            ArrayList var3 = DataQuery.getInstance(FeaturedStickersActivity.access$100(FeaturedStickersActivity.this)).getFeaturedStickerSets();
            FeaturedStickerSetCell var9 = (FeaturedStickerSetCell)var1.itemView;
            var9.setTag(var2);
            TLRPC.StickerSetCovered var4 = (TLRPC.StickerSetCovered)var3.get(var2);
            int var5 = var3.size();
            boolean var6 = true;
            boolean var7;
            if (var2 != var5 - 1) {
               var7 = true;
            } else {
               var7 = false;
            }

            boolean var8;
            if (FeaturedStickersActivity.this.unreadStickers != null && FeaturedStickersActivity.this.unreadStickers.contains(var4.set.id)) {
               var8 = true;
            } else {
               var8 = false;
            }

            var9.setStickersSet(var4, var7, var8);
            if (FeaturedStickersActivity.this.installingStickerSets.indexOfKey(var4.set.id) >= 0) {
               var7 = var6;
            } else {
               var7 = false;
            }

            var8 = var7;
            if (var7) {
               var8 = var7;
               if (var9.isInstalled()) {
                  FeaturedStickersActivity.this.installingStickerSets.remove(var4.set.id);
                  var9.setDrawProgress(false);
                  var8 = false;
               }
            }

            var9.setDrawProgress(var8);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               var3 = null;
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
         } else {
            var3 = new FeaturedStickerSetCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((FeaturedStickerSetCell)var3).setAddOnClickListener(new _$$Lambda$FeaturedStickersActivity$ListAdapter$NM7kVZE2xCqvk5vi5R9BLMmu2pk(this));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }
}
