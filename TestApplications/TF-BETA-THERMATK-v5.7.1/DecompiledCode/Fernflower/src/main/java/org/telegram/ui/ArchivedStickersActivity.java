package org.telegram.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ArchivedStickerSetCell;
import org.telegram.ui.Cells.LoadingCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.Switch;

public class ArchivedStickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int currentType;
   private EmptyTextProgressView emptyView;
   private boolean endReached;
   private boolean firstLoaded;
   private LinearLayoutManager layoutManager;
   private ArchivedStickersActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private boolean loadingStickers;
   private int rowCount;
   private ArrayList sets = new ArrayList();
   private int stickersEndRow;
   private int stickersLoadingRow;
   private int stickersShadowRow;
   private int stickersStartRow;

   public ArchivedStickersActivity(int var1) {
      this.currentType = var1;
   }

   // $FF: synthetic method
   static int access$1100(ArchivedStickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(ArchivedStickersActivity var0) {
      return var0.currentAccount;
   }

   private void getStickers() {
      if (!this.loadingStickers && !this.endReached) {
         boolean var1 = true;
         this.loadingStickers = true;
         EmptyTextProgressView var2 = this.emptyView;
         if (var2 != null && !this.firstLoaded) {
            var2.showProgress();
         }

         ArchivedStickersActivity.ListAdapter var7 = this.listAdapter;
         if (var7 != null) {
            var7.notifyDataSetChanged();
         }

         TLRPC.TL_messages_getArchivedStickers var3 = new TLRPC.TL_messages_getArchivedStickers();
         long var4;
         if (this.sets.isEmpty()) {
            var4 = 0L;
         } else {
            ArrayList var8 = this.sets;
            var4 = ((TLRPC.StickerSetCovered)var8.get(var8.size() - 1)).set.id;
         }

         var3.offset_id = var4;
         var3.limit = 15;
         if (this.currentType != 1) {
            var1 = false;
         }

         var3.masks = var1;
         int var6 = ConnectionsManager.getInstance(super.currentAccount).sendRequest(var3, new _$$Lambda$ArchivedStickersActivity$8wdae9P9JBu4YoQztgGGYjdp1Pw(this));
         ConnectionsManager.getInstance(super.currentAccount).bindRequestToGuid(var6, super.classGuid);
      }

   }

   private void updateRows() {
      this.rowCount = 0;
      if (!this.sets.isEmpty()) {
         int var1 = this.rowCount;
         this.stickersStartRow = var1;
         this.stickersEndRow = var1 + this.sets.size();
         this.rowCount += this.sets.size();
         if (!this.endReached) {
            var1 = this.rowCount++;
            this.stickersLoadingRow = var1;
            this.stickersShadowRow = -1;
         } else {
            var1 = this.rowCount++;
            this.stickersShadowRow = var1;
            this.stickersLoadingRow = -1;
         }
      } else {
         this.stickersStartRow = -1;
         this.stickersEndRow = -1;
         this.stickersLoadingRow = -1;
         this.stickersShadowRow = -1;
      }

      ArchivedStickersActivity.ListAdapter var2 = this.listAdapter;
      if (var2 != null) {
         var2.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.currentType == 0) {
         super.actionBar.setTitle(LocaleController.getString("ArchivedStickers", 2131558659));
      } else {
         super.actionBar.setTitle(LocaleController.getString("ArchivedMasks", 2131558654));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               ArchivedStickersActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new ArchivedStickersActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.emptyView = new EmptyTextProgressView(var1);
      if (this.currentType == 0) {
         this.emptyView.setText(LocaleController.getString("ArchivedStickersEmpty", 2131558662));
      } else {
         this.emptyView.setText(LocaleController.getString("ArchivedMasksEmpty", 2131558657));
      }

      var2.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      if (this.loadingStickers) {
         this.emptyView.showProgress();
      } else {
         this.emptyView.showTextView();
      }

      this.listView = new RecyclerListView(var1);
      this.listView.setFocusable(true);
      this.listView.setEmptyView(this.emptyView);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$ArchivedStickersActivity$a3I8kBmTW_AQtR9tR_9PNmdkhkA(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrolled(RecyclerView var1, int var2, int var3) {
            if (!ArchivedStickersActivity.this.loadingStickers && !ArchivedStickersActivity.this.endReached && ArchivedStickersActivity.this.layoutManager.findLastVisibleItemPosition() > ArchivedStickersActivity.this.stickersLoadingRow - 2) {
               ArchivedStickersActivity.this.getStickers();
            }

         }
      });
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.needReloadArchivedStickers) {
         this.firstLoaded = false;
         this.endReached = false;
         this.sets.clear();
         this.updateRows();
         EmptyTextProgressView var4 = this.emptyView;
         if (var4 != null) {
            var4.showProgress();
         }

         this.getStickers();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{ArchivedStickerSetCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{LoadingCell.class, TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "emptyListPlaceholder"), new ThemeDescription(this.emptyView, ThemeDescription.FLAG_PROGRESSBAR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{LoadingCell.class}, new String[]{"progressBar"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "progressCircle"), new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{ArchivedStickerSetCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked")};
   }

   // $FF: synthetic method
   public void lambda$createView$0$ArchivedStickersActivity(View var1, int var2) {
      if (var2 >= this.stickersStartRow && var2 < this.stickersEndRow && this.getParentActivity() != null) {
         TLRPC.StickerSetCovered var3 = (TLRPC.StickerSetCovered)this.sets.get(var2);
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
         var5.setInstallDelegate(new ArchivedStickersActivity$2(this, var1));
         this.showDialog(var5);
      }

   }

   // $FF: synthetic method
   public void lambda$getStickers$2$ArchivedStickersActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ArchivedStickersActivity$W1mSPGuq_a6awcQIM1Y_7zj88qM(this, var2, var1));
   }

   // $FF: synthetic method
   public void lambda$null$1$ArchivedStickersActivity(TLRPC.TL_error var1, TLObject var2) {
      if (var1 == null) {
         TLRPC.TL_messages_archivedStickers var4 = (TLRPC.TL_messages_archivedStickers)var2;
         this.sets.addAll(var4.sets);
         boolean var3;
         if (var4.sets.size() != 15) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.endReached = var3;
         this.loadingStickers = false;
         this.firstLoaded = true;
         EmptyTextProgressView var5 = this.emptyView;
         if (var5 != null) {
            var5.showTextView();
         }

         this.updateRows();
      }

   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.getStickers();
      this.updateRows();
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.needReloadArchivedStickers);
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.needReloadArchivedStickers);
   }

   public void onResume() {
      super.onResume();
      ArchivedStickersActivity.ListAdapter var1 = this.listAdapter;
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
         return ArchivedStickersActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 >= ArchivedStickersActivity.this.stickersStartRow && var1 < ArchivedStickersActivity.this.stickersEndRow) {
            return 0;
         } else if (var1 == ArchivedStickersActivity.this.stickersLoadingRow) {
            return 1;
         } else {
            return var1 == ArchivedStickersActivity.this.stickersShadowRow ? 2 : 0;
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
      public void lambda$onCreateViewHolder$0$ArchivedStickersActivity$ListAdapter(Switch var1, boolean var2) {
         int var3 = (Integer)((ArchivedStickerSetCell)var1.getParent()).getTag();
         if (var3 < ArchivedStickersActivity.this.sets.size()) {
            TLRPC.StickerSetCovered var4 = (TLRPC.StickerSetCovered)ArchivedStickersActivity.this.sets.get(var3);
            DataQuery var6 = DataQuery.getInstance(ArchivedStickersActivity.access$1100(ArchivedStickersActivity.this));
            Activity var5 = ArchivedStickersActivity.this.getParentActivity();
            TLRPC.StickerSet var8 = var4.set;
            byte var7;
            if (!var2) {
               var7 = 1;
            } else {
               var7 = 2;
            }

            var6.removeStickersSet(var5, var8, var7, ArchivedStickersActivity.this, false);
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         if (this.getItemViewType(var2) == 0) {
            ArchivedStickerSetCell var3 = (ArchivedStickerSetCell)var1.itemView;
            var3.setTag(var2);
            TLRPC.StickerSetCovered var6 = (TLRPC.StickerSetCovered)ArchivedStickersActivity.this.sets.get(var2);
            int var4 = ArchivedStickersActivity.this.sets.size();
            boolean var5 = true;
            if (var2 == var4 - 1) {
               var5 = false;
            }

            var3.setStickersSet(var6, var5);
            var3.setChecked(DataQuery.getInstance(ArchivedStickersActivity.access$700(ArchivedStickersActivity.this)).isStickerPackInstalled(var6.set.id));
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  var3 = null;
               } else {
                  var3 = new TextInfoPrivacyCell(this.mContext);
                  ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
               }
            } else {
               var3 = new LoadingCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
         } else {
            var3 = new ArchivedStickerSetCell(this.mContext, true);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((ArchivedStickerSetCell)var3).setOnCheckClick(new _$$Lambda$ArchivedStickersActivity$ListAdapter$51_kmSlVaMxy8__NHVDcnb9AD68(this));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }
}
