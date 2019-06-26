package org.telegram.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.StickerSetCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.URLSpanNoUnderline;

public class StickersActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {
   private int archivedInfoRow;
   private int archivedRow;
   private int currentType;
   private int featuredInfoRow;
   private int featuredRow;
   private LinearLayoutManager layoutManager;
   private StickersActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int masksInfoRow;
   private int masksRow;
   private boolean needReorder;
   private int rowCount;
   private int stickersEndRow;
   private int stickersShadowRow;
   private int stickersStartRow;
   private int suggestInfoRow;
   private int suggestRow;

   public StickersActivity(int var1) {
      this.currentType = var1;
   }

   // $FF: synthetic method
   static int access$1500(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1600(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1700(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1800(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1900(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2000(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2100(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$600(StickersActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static void lambda$sendReorder$2(TLObject var0, TLRPC.TL_error var1) {
   }

   private void sendReorder() {
      if (this.needReorder) {
         DataQuery.getInstance(super.currentAccount).calcNewHash(this.currentType);
         this.needReorder = false;
         TLRPC.TL_messages_reorderStickerSets var1 = new TLRPC.TL_messages_reorderStickerSets();
         boolean var2;
         if (this.currentType == 1) {
            var2 = true;
         } else {
            var2 = false;
         }

         var1.masks = var2;
         ArrayList var3 = DataQuery.getInstance(super.currentAccount).getStickerSets(this.currentType);

         for(int var4 = 0; var4 < var3.size(); ++var4) {
            var1.order.add(((TLRPC.TL_messages_stickerSet)var3.get(var4)).set.id);
         }

         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var1, _$$Lambda$StickersActivity$eNJ1JVvMbECvyP_jXUZlvL1o67I.INSTANCE);
         NotificationCenter.getInstance(super.currentAccount).postNotificationName(NotificationCenter.stickersDidLoad, this.currentType);
      }
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1;
      if (this.currentType == 0) {
         var1 = this.rowCount++;
         this.suggestRow = var1;
         var1 = this.rowCount++;
         this.featuredRow = var1;
         var1 = this.rowCount++;
         this.featuredInfoRow = var1;
         var1 = this.rowCount++;
         this.masksRow = var1;
         var1 = this.rowCount++;
         this.masksInfoRow = var1;
      } else {
         this.featuredRow = -1;
         this.featuredInfoRow = -1;
         this.masksRow = -1;
         this.masksInfoRow = -1;
      }

      if (DataQuery.getInstance(super.currentAccount).getArchivedStickersCount(this.currentType) != 0) {
         var1 = this.rowCount++;
         this.archivedRow = var1;
         var1 = this.rowCount++;
         this.archivedInfoRow = var1;
      } else {
         this.archivedRow = -1;
         this.archivedInfoRow = -1;
      }

      ArrayList var2 = DataQuery.getInstance(super.currentAccount).getStickerSets(this.currentType);
      if (!var2.isEmpty()) {
         var1 = this.rowCount;
         this.stickersStartRow = var1;
         this.stickersEndRow = var1 + var2.size();
         this.rowCount += var2.size();
         var1 = this.rowCount++;
         this.stickersShadowRow = var1;
      } else {
         this.stickersStartRow = -1;
         this.stickersEndRow = -1;
         this.stickersShadowRow = -1;
      }

      StickersActivity.ListAdapter var3 = this.listAdapter;
      if (var3 != null) {
         var3.notifyDataSetChanged();
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.currentType == 0) {
         super.actionBar.setTitle(LocaleController.getString("StickersName", 2131560810));
      } else {
         super.actionBar.setTitle(LocaleController.getString("Masks", 2131559809));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               StickersActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new StickersActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      var2.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.listView = new RecyclerListView(var1);
      this.listView.setFocusable(true);
      this.listView.setTag(7);
      this.layoutManager = new LinearLayoutManager(var1);
      this.layoutManager.setOrientation(1);
      this.listView.setLayoutManager(this.layoutManager);
      (new ItemTouchHelper(new StickersActivity.TouchHelperCallback())).attachToRecyclerView(this.listView);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$StickersActivity$52bQnWSJW3OnfBam1s3y37TWiNA(this)));
      return super.fragmentView;
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.stickersDidLoad) {
         if ((Integer)var3[0] == this.currentType) {
            this.updateRows();
         }
      } else if (var1 == NotificationCenter.featuredStickersDidLoad) {
         StickersActivity.ListAdapter var4 = this.listAdapter;
         if (var4 != null) {
            var4.notifyItemChanged(0);
         }
      } else if (var1 == NotificationCenter.archivedStickersCountDidLoad && (Integer)var3[0] == this.currentType) {
         this.updateRows();
      }

   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{StickerSetCell.class, TextSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteLinkText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menuSelector"), new ThemeDescription(this.listView, 0, new Class[]{StickerSetCell.class}, new String[]{"optionsButton"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "stickers_menu")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$StickersActivity(View var1, int var2) {
      if (var2 >= this.stickersStartRow && var2 < this.stickersEndRow && this.getParentActivity() != null) {
         this.sendReorder();
         TLRPC.TL_messages_stickerSet var9 = (TLRPC.TL_messages_stickerSet)DataQuery.getInstance(super.currentAccount).getStickerSets(this.currentType).get(var2 - this.stickersStartRow);
         ArrayList var8 = var9.documents;
         if (var8 == null || var8.isEmpty()) {
            return;
         }

         this.showDialog(new StickersAlert(this.getParentActivity(), this, (TLRPC.InputStickerSet)null, var9, (StickersAlert.StickersAlertDelegate)null));
      } else if (var2 == this.featuredRow) {
         this.sendReorder();
         this.presentFragment(new FeaturedStickersActivity());
      } else if (var2 == this.archivedRow) {
         this.sendReorder();
         this.presentFragment(new ArchivedStickersActivity(this.currentType));
      } else if (var2 == this.masksRow) {
         this.presentFragment(new StickersActivity(1));
      } else if (var2 == this.suggestRow) {
         AlertDialog.Builder var4 = new AlertDialog.Builder(this.getParentActivity());
         var4.setTitle(LocaleController.getString("SuggestStickers", 2131560843));
         String var5 = LocaleController.getString("SuggestStickersAll", 2131560844);
         String var6 = LocaleController.getString("SuggestStickersInstalled", 2131560845);
         String var3 = LocaleController.getString("SuggestStickersNone", 2131560846);
         _$$Lambda$StickersActivity$43apBJTj1nQwZFS8l5r8_cQqass var7 = new _$$Lambda$StickersActivity$43apBJTj1nQwZFS8l5r8_cQqass(this);
         var4.setItems(new CharSequence[]{var5, var6, var3}, var7);
         this.showDialog(var4.create());
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$StickersActivity(DialogInterface var1, int var2) {
      SharedConfig.setSuggestStickers(var2);
      this.listAdapter.notifyItemChanged(this.suggestRow);
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      DataQuery.getInstance(super.currentAccount).checkStickers(this.currentType);
      if (this.currentType == 0) {
         DataQuery.getInstance(super.currentAccount).checkFeaturedStickers();
      }

      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.stickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.archivedStickersCountDidLoad);
      NotificationCenter.getInstance(super.currentAccount).addObserver(this, NotificationCenter.featuredStickersDidLoad);
      this.updateRows();
      return true;
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.stickersDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.archivedStickersCountDidLoad);
      NotificationCenter.getInstance(super.currentAccount).removeObserver(this, NotificationCenter.featuredStickersDidLoad);
      this.sendReorder();
   }

   public void onResume() {
      super.onResume();
      StickersActivity.ListAdapter var1 = this.listAdapter;
      if (var1 != null) {
         var1.notifyDataSetChanged();
      }

   }

   private class ListAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;

      public ListAdapter(Context var2) {
         this.mContext = var2;
      }

      private void processSelectionOption(int var1, TLRPC.TL_messages_stickerSet var2) {
         if (var1 == 0) {
            DataQuery var3 = DataQuery.getInstance(StickersActivity.access$1500(StickersActivity.this));
            Activity var4 = StickersActivity.this.getParentActivity();
            TLRPC.StickerSet var9 = var2.set;
            byte var8;
            if (!var9.archived) {
               var8 = 1;
            } else {
               var8 = 2;
            }

            var3.removeStickersSet(var4, var9, var8, StickersActivity.this, true);
         } else if (var1 == 1) {
            DataQuery.getInstance(StickersActivity.access$1600(StickersActivity.this)).removeStickersSet(StickersActivity.this.getParentActivity(), var2.set, 0, StickersActivity.this, true);
         } else if (var1 == 2) {
            try {
               Intent var12 = new Intent("android.intent.action.SEND");
               var12.setType("text/plain");
               Locale var10 = Locale.US;
               StringBuilder var5 = new StringBuilder();
               var5.append("https://");
               var5.append(MessagesController.getInstance(StickersActivity.access$1700(StickersActivity.this)).linkPrefix);
               var5.append("/addstickers/%s");
               var12.putExtra("android.intent.extra.TEXT", String.format(var10, var5.toString(), var2.set.short_name));
               StickersActivity.this.getParentActivity().startActivityForResult(Intent.createChooser(var12, LocaleController.getString("StickersShare", 2131560813)), 500);
            } catch (Exception var7) {
               FileLog.e((Throwable)var7);
            }
         } else if (var1 == 3) {
            try {
               ClipboardManager var14 = (ClipboardManager)ApplicationLoader.applicationContext.getSystemService("clipboard");
               Locale var13 = Locale.US;
               StringBuilder var11 = new StringBuilder();
               var11.append("https://");
               var11.append(MessagesController.getInstance(StickersActivity.access$1800(StickersActivity.this)).linkPrefix);
               var11.append("/addstickers/%s");
               var14.setPrimaryClip(ClipData.newPlainText("label", String.format(var13, var11.toString(), var2.set.short_name)));
               Toast.makeText(StickersActivity.this.getParentActivity(), LocaleController.getString("LinkCopied", 2131559751), 0).show();
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }

      }

      public int getItemCount() {
         return StickersActivity.this.rowCount;
      }

      public long getItemId(int var1) {
         if (var1 >= StickersActivity.this.stickersStartRow && var1 < StickersActivity.this.stickersEndRow) {
            return ((TLRPC.TL_messages_stickerSet)DataQuery.getInstance(StickersActivity.access$600(StickersActivity.this)).getStickerSets(StickersActivity.this.currentType).get(var1 - StickersActivity.this.stickersStartRow)).set.id;
         } else {
            return var1 != StickersActivity.this.suggestRow && var1 != StickersActivity.this.suggestInfoRow && var1 != StickersActivity.this.archivedRow && var1 != StickersActivity.this.archivedInfoRow && var1 != StickersActivity.this.featuredRow && var1 != StickersActivity.this.featuredInfoRow && var1 != StickersActivity.this.masksRow && var1 != StickersActivity.this.masksInfoRow ? (long)var1 : -2147483648L;
         }
      }

      public int getItemViewType(int var1) {
         if (var1 >= StickersActivity.this.stickersStartRow && var1 < StickersActivity.this.stickersEndRow) {
            return 0;
         } else if (var1 != StickersActivity.this.featuredInfoRow && var1 != StickersActivity.this.archivedInfoRow && var1 != StickersActivity.this.masksInfoRow) {
            if (var1 != StickersActivity.this.featuredRow && var1 != StickersActivity.this.archivedRow && var1 != StickersActivity.this.masksRow && var1 != StickersActivity.this.suggestRow) {
               return var1 != StickersActivity.this.stickersShadowRow && var1 != StickersActivity.this.suggestInfoRow ? 0 : 3;
            } else {
               return 2;
            }
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3;
         if (var2 != 0 && var2 != 2) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      // $FF: synthetic method
      public void lambda$null$0$StickersActivity$ListAdapter(int[] var1, TLRPC.TL_messages_stickerSet var2, DialogInterface var3, int var4) {
         this.processSelectionOption(var1[var4], var2);
      }

      // $FF: synthetic method
      public void lambda$onCreateViewHolder$1$StickersActivity$ListAdapter(View var1) {
         StickersActivity.this.sendReorder();
         TLRPC.TL_messages_stickerSet var2 = ((StickerSetCell)var1.getParent()).getStickersSet();
         AlertDialog.Builder var3 = new AlertDialog.Builder(StickersActivity.this.getParentActivity());
         var3.setTitle(var2.set.title);
         CharSequence[] var4;
         int[] var5;
         if (StickersActivity.this.currentType == 0) {
            if (var2.set.official) {
               var5 = new int[]{0};
               var4 = new CharSequence[]{LocaleController.getString("StickersHide", 2131560809)};
            } else {
               var5 = new int[]{0, 1, 2, 3};
               var4 = new CharSequence[]{LocaleController.getString("StickersHide", 2131560809), LocaleController.getString("StickersRemove", 2131560811), LocaleController.getString("StickersShare", 2131560813), LocaleController.getString("StickersCopy", 2131560808)};
            }
         } else if (var2.set.official) {
            var5 = new int[]{0};
            var4 = new CharSequence[]{LocaleController.getString("StickersRemove", 2131560809)};
         } else {
            var5 = new int[]{0, 1, 2, 3};
            var4 = new CharSequence[]{LocaleController.getString("StickersHide", 2131560809), LocaleController.getString("StickersRemove", 2131560811), LocaleController.getString("StickersShare", 2131560813), LocaleController.getString("StickersCopy", 2131560808)};
         }

         var3.setItems(var4, new _$$Lambda$StickersActivity$ListAdapter$4oHDK9bhePlEa0rc89WtPkp_42Q(this, var5, var2));
         StickersActivity.this.showDialog(var3.create());
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var4 = false;
         if (var3 != 0) {
            String var6;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 == 3) {
                     if (var2 == StickersActivity.this.stickersShadowRow) {
                        var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                     } else if (var2 == StickersActivity.this.suggestInfoRow) {
                        var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                     }
                  }
               } else if (var2 == StickersActivity.this.featuredRow) {
                  var2 = DataQuery.getInstance(StickersActivity.access$2100(StickersActivity.this)).getUnreadStickerSets().size();
                  TextSettingsCell var5 = (TextSettingsCell)var1.itemView;
                  var6 = LocaleController.getString("FeaturedStickers", 2131559479);
                  String var9;
                  if (var2 != 0) {
                     var9 = String.format("%d", var2);
                  } else {
                     var9 = "";
                  }

                  var5.setTextAndValue(var6, var9, false);
               } else if (var2 == StickersActivity.this.archivedRow) {
                  if (StickersActivity.this.currentType == 0) {
                     ((TextSettingsCell)var1.itemView).setText(LocaleController.getString("ArchivedStickers", 2131558659), false);
                  } else {
                     ((TextSettingsCell)var1.itemView).setText(LocaleController.getString("ArchivedMasks", 2131558654), false);
                  }
               } else if (var2 == StickersActivity.this.masksRow) {
                  ((TextSettingsCell)var1.itemView).setText(LocaleController.getString("Masks", 2131559809), false);
               } else if (var2 == StickersActivity.this.suggestRow) {
                  var2 = SharedConfig.suggestStickers;
                  if (var2 != 0) {
                     if (var2 != 1) {
                        var6 = LocaleController.getString("SuggestStickersNone", 2131560846);
                     } else {
                        var6 = LocaleController.getString("SuggestStickersInstalled", 2131560845);
                     }
                  } else {
                     var6 = LocaleController.getString("SuggestStickersAll", 2131560844);
                  }

                  ((TextSettingsCell)var1.itemView).setTextAndValue(LocaleController.getString("SuggestStickers", 2131560843), var6, true);
               }
            } else if (var2 == StickersActivity.this.featuredInfoRow) {
               var6 = LocaleController.getString("FeaturedStickersInfo", 2131559480);
               var2 = var6.indexOf("@stickers");
               if (var2 != -1) {
                  try {
                     SpannableStringBuilder var7 = new SpannableStringBuilder(var6);
                     URLSpanNoUnderline var11 = new URLSpanNoUnderline("@stickers") {
                        public void onClick(View var1) {
                           MessagesController.getInstance(StickersActivity.access$2000(StickersActivity.this)).openByUserName("stickers", StickersActivity.this, 1);
                        }
                     };
                     var7.setSpan(var11, var2, var2 + 9, 18);
                     ((TextInfoPrivacyCell)var1.itemView).setText(var7);
                  } catch (Exception var8) {
                     FileLog.e((Throwable)var8);
                     ((TextInfoPrivacyCell)var1.itemView).setText(var6);
                  }
               } else {
                  ((TextInfoPrivacyCell)var1.itemView).setText(var6);
               }
            } else if (var2 == StickersActivity.this.archivedInfoRow) {
               if (StickersActivity.this.currentType == 0) {
                  ((TextInfoPrivacyCell)var1.itemView).setText(LocaleController.getString("ArchivedStickersInfo", 2131558663));
               } else {
                  ((TextInfoPrivacyCell)var1.itemView).setText(LocaleController.getString("ArchivedMasksInfo", 2131558658));
               }
            } else if (var2 == StickersActivity.this.masksInfoRow) {
               ((TextInfoPrivacyCell)var1.itemView).setText(LocaleController.getString("MasksInfo", 2131559816));
            }
         } else {
            ArrayList var13 = DataQuery.getInstance(StickersActivity.access$1900(StickersActivity.this)).getStickerSets(StickersActivity.this.currentType);
            var2 -= StickersActivity.this.stickersStartRow;
            StickerSetCell var12 = (StickerSetCell)var1.itemView;
            TLRPC.TL_messages_stickerSet var10 = (TLRPC.TL_messages_stickerSet)var13.get(var2);
            if (var2 != var13.size() - 1) {
               var4 = true;
            }

            var12.setStickersSet(var10, var4);
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = null;
                  } else {
                     var3 = new ShadowSectionCell(this.mContext);
                  }
               } else {
                  var3 = new TextSettingsCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextInfoPrivacyCell(this.mContext);
               ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            }
         } else {
            var3 = new StickerSetCell(this.mContext, 1);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            ((StickerSetCell)var3).setOnOptionsClick(new _$$Lambda$StickersActivity$ListAdapter$xPbh5swVjrfEVlhBcsKoz2sSeSI(this));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }

      public void swapElements(int var1, int var2) {
         if (var1 != var2) {
            StickersActivity.this.needReorder = true;
         }

         ArrayList var3 = DataQuery.getInstance(StickersActivity.access$2400(StickersActivity.this)).getStickerSets(StickersActivity.this.currentType);
         TLRPC.TL_messages_stickerSet var4 = (TLRPC.TL_messages_stickerSet)var3.get(var1 - StickersActivity.this.stickersStartRow);
         var3.set(var1 - StickersActivity.this.stickersStartRow, var3.get(var2 - StickersActivity.this.stickersStartRow));
         var3.set(var2 - StickersActivity.this.stickersStartRow, var4);
         this.notifyItemMoved(var1, var2);
      }
   }

   public class TouchHelperCallback extends ItemTouchHelper.Callback {
      public void clearView(RecyclerView var1, RecyclerView.ViewHolder var2) {
         super.clearView(var1, var2);
         var2.itemView.setPressed(false);
      }

      public int getMovementFlags(RecyclerView var1, RecyclerView.ViewHolder var2) {
         return var2.getItemViewType() != 0 ? ItemTouchHelper.Callback.makeMovementFlags(0, 0) : ItemTouchHelper.Callback.makeMovementFlags(3, 0);
      }

      public boolean isLongPressDragEnabled() {
         return true;
      }

      public void onChildDraw(Canvas var1, RecyclerView var2, RecyclerView.ViewHolder var3, float var4, float var5, int var6, boolean var7) {
         super.onChildDraw(var1, var2, var3, var4, var5, var6, var7);
      }

      public boolean onMove(RecyclerView var1, RecyclerView.ViewHolder var2, RecyclerView.ViewHolder var3) {
         if (var2.getItemViewType() != var3.getItemViewType()) {
            return false;
         } else {
            StickersActivity.this.listAdapter.swapElements(var2.getAdapterPosition(), var3.getAdapterPosition());
            return true;
         }
      }

      public void onSelectedChanged(RecyclerView.ViewHolder var1, int var2) {
         if (var2 != 0) {
            StickersActivity.this.listView.cancelClickRunnables(false);
            var1.itemView.setPressed(true);
         }

         super.onSelectedChanged(var1, var2);
      }

      public void onSwiped(RecyclerView.ViewHolder var1, int var2) {
      }
   }
}
