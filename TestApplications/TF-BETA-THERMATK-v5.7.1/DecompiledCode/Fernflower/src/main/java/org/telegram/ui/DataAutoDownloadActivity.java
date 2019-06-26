package org.telegram.ui;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.MaxFileSizeCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckBoxCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class DataAutoDownloadActivity extends BaseFragment {
   private boolean animateChecked;
   private int autoDownloadRow;
   private int autoDownloadSectionRow;
   private int currentPresetNum;
   private int currentType;
   private DownloadController.Preset defaultPreset;
   private int filesRow;
   private DownloadController.Preset highPreset;
   private String key;
   private String key2;
   private LinearLayoutManager layoutManager;
   private DataAutoDownloadActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private DownloadController.Preset lowPreset;
   private DownloadController.Preset mediumPreset;
   private int photosRow;
   private ArrayList presets = new ArrayList();
   private int rowCount;
   private int selectedPreset = 1;
   private int typeHeaderRow;
   private DownloadController.Preset typePreset;
   private int typeSectionRow;
   private int usageHeaderRow;
   private int usageProgressRow;
   private int usageSectionRow;
   private int videosRow;
   private boolean wereAnyChanges;

   public DataAutoDownloadActivity(int var1) {
      this.currentType = var1;
      this.lowPreset = DownloadController.getInstance(super.currentAccount).lowPreset;
      this.mediumPreset = DownloadController.getInstance(super.currentAccount).mediumPreset;
      this.highPreset = DownloadController.getInstance(super.currentAccount).highPreset;
      var1 = this.currentType;
      if (var1 == 0) {
         this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentMobilePreset;
         this.typePreset = DownloadController.getInstance(super.currentAccount).mobilePreset;
         this.defaultPreset = this.mediumPreset;
         this.key = "mobilePreset";
         this.key2 = "currentMobilePreset";
      } else if (var1 == 1) {
         this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentWifiPreset;
         this.typePreset = DownloadController.getInstance(super.currentAccount).wifiPreset;
         this.defaultPreset = this.highPreset;
         this.key = "wifiPreset";
         this.key2 = "currentWifiPreset";
      } else {
         this.currentPresetNum = DownloadController.getInstance(super.currentAccount).currentRoamingPreset;
         this.typePreset = DownloadController.getInstance(super.currentAccount).roamingPreset;
         this.defaultPreset = this.lowPreset;
         this.key = "roamingPreset";
         this.key2 = "currentRoamingPreset";
      }

   }

   // $FF: synthetic method
   static int access$1000(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1200(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$700(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$800(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$900(DataAutoDownloadActivity var0) {
      return var0.currentAccount;
   }

   private void fillPresets() {
      this.presets.clear();
      this.presets.add(this.lowPreset);
      this.presets.add(this.mediumPreset);
      this.presets.add(this.highPreset);
      if (!this.typePreset.equals(this.lowPreset) && !this.typePreset.equals(this.mediumPreset) && !this.typePreset.equals(this.highPreset)) {
         this.presets.add(this.typePreset);
      }

      Collections.sort(this.presets, _$$Lambda$DataAutoDownloadActivity$E0PVxdOLHPC3ZjO_nDkt8nGBYVw.INSTANCE);
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         RecyclerView.ViewHolder var3 = var1.findViewHolderForAdapterPosition(this.usageProgressRow);
         if (var3 != null) {
            var3.itemView.requestLayout();
         } else {
            this.listAdapter.notifyItemChanged(this.usageProgressRow);
         }
      }

      int var2 = this.currentPresetNum;
      if (var2 == 0 || var2 == 3 && this.typePreset.equals(this.lowPreset)) {
         this.selectedPreset = this.presets.indexOf(this.lowPreset);
      } else {
         var2 = this.currentPresetNum;
         if (var2 != 1 && (var2 != 3 || !this.typePreset.equals(this.mediumPreset))) {
            var2 = this.currentPresetNum;
            if (var2 == 2 || var2 == 3 && this.typePreset.equals(this.highPreset)) {
               this.selectedPreset = this.presets.indexOf(this.highPreset);
            } else {
               this.selectedPreset = this.presets.indexOf(this.typePreset);
            }
         } else {
            this.selectedPreset = this.presets.indexOf(this.mediumPreset);
         }
      }

   }

   // $FF: synthetic method
   static int lambda$fillPresets$5(DownloadController.Preset var0, DownloadController.Preset var1) {
      int var2 = DownloadController.typeToIndex(4);
      int var3 = DownloadController.typeToIndex(8);
      int var4 = 0;
      boolean var5 = false;
      boolean var6 = false;

      int[] var7;
      boolean var8;
      boolean var9;
      while(true) {
         var7 = var0.mask;
         var8 = var5;
         var9 = var6;
         if (var4 >= var7.length) {
            break;
         }

         if ((var7[var4] & 4) != 0) {
            var5 = true;
         }

         if ((var0.mask[var4] & 8) != 0) {
            var6 = true;
         }

         if (var5 && var6) {
            var8 = var5;
            var9 = var6;
            break;
         }

         ++var4;
      }

      int var10 = 0;
      var5 = false;
      var6 = false;

      boolean var11;
      boolean var12;
      while(true) {
         var7 = var1.mask;
         var11 = var5;
         var12 = var6;
         if (var10 >= var7.length) {
            break;
         }

         if ((var7[var10] & 4) != 0) {
            var5 = true;
         }

         if ((var1.mask[var10] & 8) != 0) {
            var6 = true;
         }

         if (var5 && var6) {
            var11 = var5;
            var12 = var6;
            break;
         }

         ++var10;
      }

      int var14;
      if (var8) {
         var14 = var0.sizes[var2];
      } else {
         var14 = 0;
      }

      int var13;
      if (var9) {
         var13 = var0.sizes[var3];
      } else {
         var13 = 0;
      }

      int var15 = var14 + var13;
      if (var11) {
         var14 = var1.sizes[var2];
      } else {
         var14 = 0;
      }

      if (var12) {
         var13 = var1.sizes[var3];
      } else {
         var13 = 0;
      }

      var14 += var13;
      if (var15 > var14) {
         return 1;
      } else {
         return var15 < var14 ? -1 : 0;
      }
   }

   // $FF: synthetic method
   static void lambda$null$1(TextCheckCell[] var0, View var1) {
      var0[0].setChecked(var0[0].isChecked() ^ true);
   }

   // $FF: synthetic method
   static void lambda$null$2(BottomSheet.Builder var0, View var1) {
      var0.getDismissRunnable().run();
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.autoDownloadRow = var1;
      var1 = this.rowCount++;
      this.autoDownloadSectionRow = var1;
      if (this.typePreset.enabled) {
         var1 = this.rowCount++;
         this.usageHeaderRow = var1;
         var1 = this.rowCount++;
         this.usageProgressRow = var1;
         var1 = this.rowCount++;
         this.usageSectionRow = var1;
         var1 = this.rowCount++;
         this.typeHeaderRow = var1;
         var1 = this.rowCount++;
         this.photosRow = var1;
         var1 = this.rowCount++;
         this.videosRow = var1;
         var1 = this.rowCount++;
         this.filesRow = var1;
         var1 = this.rowCount++;
         this.typeSectionRow = var1;
      } else {
         this.usageHeaderRow = -1;
         this.usageProgressRow = -1;
         this.usageSectionRow = -1;
         this.typeHeaderRow = -1;
         this.photosRow = -1;
         this.videosRow = -1;
         this.filesRow = -1;
         this.typeSectionRow = -1;
      }

   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      int var2 = this.currentType;
      if (var2 == 0) {
         super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnMobileData", 2131558758));
      } else if (var2 == 1) {
         super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnWiFiData", 2131558763));
      } else if (var2 == 2) {
         super.actionBar.setTitle(LocaleController.getString("AutoDownloadOnRoamingData", 2131558760));
      }

      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               DataAutoDownloadActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new DataAutoDownloadActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var3 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      ((DefaultItemAnimator)this.listView.getItemAnimator()).setDelayAnimations(false);
      RecyclerListView var4 = this.listView;
      LinearLayoutManager var5 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var5;
      var4.setLayoutManager(var5);
      var3.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$DataAutoDownloadActivity$z_MZui0AcXTnHUC_YO0Sopv_aSo(this)));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, NotificationsCheckCell.class, DataAutoDownloadActivity.PresetChooseView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundUnchecked"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundCheckText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlue"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlueChecked"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlueThumb"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlueThumbChecked"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlueSelector"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackBlueSelectorChecked"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{DataAutoDownloadActivity.PresetChooseView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{DataAutoDownloadActivity.PresetChooseView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, 0, new Class[]{DataAutoDownloadActivity.PresetChooseView.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText")};
   }

   // $FF: synthetic method
   public void lambda$createView$4$DataAutoDownloadActivity(View var1, int var2, float var3, float var4) {
      int var5 = this.autoDownloadRow;
      int var6 = 0;
      int var7 = 0;
      boolean var9;
      DownloadController.Preset var10;
      boolean var11;
      String var12;
      Editor var31;
      if (var2 == var5) {
         var2 = this.currentPresetNum;
         if (var2 != 3) {
            if (var2 == 0) {
               this.typePreset.set(this.lowPreset);
            } else if (var2 == 1) {
               this.typePreset.set(this.mediumPreset);
            } else if (var2 == 2) {
               this.typePreset.set(this.highPreset);
            }
         }

         TextCheckCell var8;
         label188: {
            var8 = (TextCheckCell)var1;
            var9 = var8.isChecked();
            if (!var9) {
               var10 = this.typePreset;
               if (var10.enabled) {
                  System.arraycopy(this.defaultPreset.mask, 0, var10.mask, 0, 4);
                  break label188;
               }
            }

            var10 = this.typePreset;
            var10.enabled ^= true;
         }

         var11 = this.typePreset.enabled;
         var12 = "windowBackgroundChecked";
         String var30;
         if (var11) {
            var30 = "windowBackgroundChecked";
         } else {
            var30 = "windowBackgroundUnchecked";
         }

         var1.setTag(var30);
         String var24;
         if (this.typePreset.enabled) {
            var24 = var12;
         } else {
            var24 = "windowBackgroundUnchecked";
         }

         var8.setBackgroundColorAnimated(var9 ^ true, Theme.getColor(var24));
         this.updateRows();
         if (this.typePreset.enabled) {
            this.listAdapter.notifyItemRangeInserted(this.autoDownloadSectionRow + 1, 8);
         } else {
            this.listAdapter.notifyItemRangeRemoved(this.autoDownloadSectionRow + 1, 8);
         }

         this.listAdapter.notifyItemChanged(this.autoDownloadSectionRow);
         var31 = MessagesController.getMainSettings(super.currentAccount).edit();
         var31.putString(this.key, this.typePreset.toString());
         var24 = this.key2;
         this.currentPresetNum = 3;
         var31.putInt(var24, 3);
         var2 = this.currentType;
         if (var2 == 0) {
            DownloadController.getInstance(super.currentAccount).currentMobilePreset = this.currentPresetNum;
         } else if (var2 == 1) {
            DownloadController.getInstance(super.currentAccount).currentWifiPreset = this.currentPresetNum;
         } else {
            DownloadController.getInstance(super.currentAccount).currentRoamingPreset = this.currentPresetNum;
         }

         var31.commit();
         var8.setChecked(var9 ^ true);
         DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
         this.wereAnyChanges = true;
      } else if (var2 == this.photosRow || var2 == this.videosRow || var2 == this.filesRow) {
         if (!var1.isEnabled()) {
            return;
         }

         byte var26;
         if (var2 == this.photosRow) {
            var26 = 1;
         } else if (var2 == this.videosRow) {
            var26 = 4;
         } else {
            var26 = 8;
         }

         int var13 = DownloadController.typeToIndex(var26);
         int var14 = this.currentType;
         String var28;
         if (var14 == 0) {
            var10 = DownloadController.getInstance(super.currentAccount).getCurrentMobilePreset();
            var28 = "mobilePreset";
            var12 = "currentMobilePreset";
         } else if (var14 == 1) {
            var10 = DownloadController.getInstance(super.currentAccount).getCurrentWiFiPreset();
            var28 = "wifiPreset";
            var12 = "currentWifiPreset";
         } else {
            var10 = DownloadController.getInstance(super.currentAccount).getCurrentRoamingPreset();
            var28 = "roamingPreset";
            var12 = "currentRoamingPreset";
         }

         NotificationsCheckCell var33 = (NotificationsCheckCell)var1;
         var9 = var33.isChecked();
         if ((!LocaleController.isRTL || var3 > (float)AndroidUtilities.dp(76.0F)) && (LocaleController.isRTL || var3 < (float)(var1.getMeasuredWidth() - AndroidUtilities.dp(76.0F)))) {
            if (this.getParentActivity() == null) {
               return;
            }

            BottomSheet.Builder var16 = new BottomSheet.Builder(this.getParentActivity());
            var16.setApplyTopPadding(false);
            var16.setApplyBottomPadding(false);
            LinearLayout var17 = new LinearLayout(this.getParentActivity());
            var17.setOrientation(1);
            var16.setCustomView(var17);
            HeaderCell var34 = new HeaderCell(this.getParentActivity(), true, 21, 15, false);
            if (var2 == this.photosRow) {
               var34.setText(LocaleController.getString("AutoDownloadPhotosTitle", 2131558767));
            } else if (var2 == this.videosRow) {
               var34.setText(LocaleController.getString("AutoDownloadVideosTitle", 2131558777));
            } else {
               var34.setText(LocaleController.getString("AutoDownloadFilesTitle", 2131558747));
            }

            var17.addView(var34, LayoutHelper.createFrame(-1, -2.0F));
            MaxFileSizeCell[] var18 = new MaxFileSizeCell[1];
            TextCheckCell[] var19 = new TextCheckCell[1];
            AnimatorSet[] var20 = new AnimatorSet[1];
            TextCheckBoxCell[] var21 = new TextCheckBoxCell[4];
            DownloadController.Preset var35 = var10;
            MaxFileSizeCell[] var29 = var18;

            TextCheckCell[] var36;
            for(var36 = var19; var6 < 4; ++var6) {
               TextCheckBoxCell var41 = new TextCheckBoxCell(this.getParentActivity(), true);
               var21[var6] = var41;
               String var23;
               TextCheckBoxCell var39;
               if (var6 == 0) {
                  var39 = var21[var6];
                  var23 = LocaleController.getString("AutodownloadContacts", 2131558797);
                  if ((var35.mask[0] & var26) != 0) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  var39.setTextAndCheck(var23, var9, true);
               } else if (var6 == 1) {
                  var39 = var21[var6];
                  var23 = LocaleController.getString("AutodownloadPrivateChats", 2131558799);
                  if ((var35.mask[1] & var26) != 0) {
                     var9 = true;
                  } else {
                     var9 = false;
                  }

                  var39.setTextAndCheck(var23, var9, true);
               } else {
                  String var40;
                  TextCheckBoxCell var47;
                  if (var6 == 2) {
                     var47 = var21[var6];
                     var40 = LocaleController.getString("AutodownloadGroupChats", 2131558798);
                     if ((var35.mask[2] & var26) != 0) {
                        var9 = true;
                     } else {
                        var9 = false;
                     }

                     var47.setTextAndCheck(var40, var9, true);
                  } else if (var6 == 3) {
                     var47 = var21[var6];
                     var40 = LocaleController.getString("AutodownloadChannels", 2131558796);
                     if ((var35.mask[3] & var26) != 0) {
                        var9 = true;
                     } else {
                        var9 = false;
                     }

                     if (var2 != this.photosRow) {
                        var11 = true;
                     } else {
                        var11 = false;
                     }

                     var47.setTextAndCheck(var40, var9, var11);
                  }
               }

               var21[var6].setBackgroundDrawable(Theme.getSelectorDrawable(false));
               var21[var6].setOnClickListener(new _$$Lambda$DataAutoDownloadActivity$GvlKaEmbPmrPjkZgpelfWkoLNuU(this, var41, var21, var2, var29, var36, var20));
               var17.addView(var21[var6], LayoutHelper.createFrame(-1, 50.0F));
            }

            if (var2 != this.photosRow) {
               TextInfoPrivacyCell var48 = new TextInfoPrivacyCell(this.getParentActivity());
               var29[0] = new DataAutoDownloadActivity$3(this, this.getParentActivity(), var2, var48, var36, var20);
               MaxFileSizeCell var43 = var29[0];
               var43.setSize((long)var35.sizes[var13]);
               var17.addView(var29[0], LayoutHelper.createLinear(-1, 50));
               TextCheckCell var44 = new TextCheckCell(this.getParentActivity(), 21, true);
               var36[0] = var44;
               var17.addView(var36[0], LayoutHelper.createLinear(-1, 48));
               var36[0].setOnClickListener(new _$$Lambda$DataAutoDownloadActivity$m0gzCqbXGWHGQEKzinvKQh1h1B0(var36));
               Drawable var45 = Theme.getThemedDrawable(this.getParentActivity(), 2131165394, "windowBackgroundGrayShadow");
               CombinedDrawable var46 = new CombinedDrawable(new ColorDrawable(Theme.getColor("windowBackgroundGray")), var45);
               var46.setFullsize(true);
               var48.setBackgroundDrawable(var46);
               var17.addView(var48, LayoutHelper.createLinear(-1, -2));
               if (var2 == this.videosRow) {
                  var29[0].setText(LocaleController.getString("AutoDownloadMaxVideoSize", 2131558752));
                  var36[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadVideo", 2131558771), var35.preloadVideo, false);
                  var48.setText(LocaleController.formatString("AutoDownloadPreloadVideoInfo", 2131558772, AndroidUtilities.formatFileSize((long)var35.sizes[var13])));
               } else {
                  var29[0].setText(LocaleController.getString("AutoDownloadMaxFileSize", 2131558751));
                  var36[0].setTextAndCheck(LocaleController.getString("AutoDownloadPreloadMusic", 2131558769), var35.preloadMusic, false);
                  var48.setText(LocaleController.getString("AutoDownloadPreloadMusicInfo", 2131558770));
               }
            } else {
               var29[0] = null;
               var36[0] = null;
               View var42 = new View(this.getParentActivity());
               var42.setBackgroundColor(Theme.getColor("divider"));
               var17.addView(var42, new LayoutParams(-1, 1));
            }

            if (var2 == this.videosRow) {
               var6 = 0;

               while(true) {
                  if (var6 >= var21.length) {
                     var9 = false;
                     break;
                  }

                  if (var21[var6].isChecked()) {
                     var9 = true;
                     break;
                  }

                  ++var6;
               }

               if (!var9) {
                  var29[0].setEnabled(var9, (ArrayList)null);
                  var36[0].setEnabled(var9, (ArrayList)null);
               }

               if (var35.sizes[var13] <= 2097152) {
                  var36[0].setEnabled(false, (ArrayList)null);
               }
            }

            FrameLayout var37 = new FrameLayout(this.getParentActivity());
            var37.setPadding(AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F), AndroidUtilities.dp(8.0F));
            var17.addView(var37, LayoutHelper.createLinear(-1, 52));
            TextView var38 = new TextView(this.getParentActivity());
            var38.setTextSize(1, 14.0F);
            var38.setTextColor(Theme.getColor("dialogTextBlue2"));
            var38.setGravity(17);
            var38.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var38.setText(LocaleController.getString("Cancel", 2131558891).toUpperCase());
            var38.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
            var37.addView(var38, LayoutHelper.createFrame(-2, 36, 51));
            var38.setOnClickListener(new _$$Lambda$DataAutoDownloadActivity$p8_fIkB1xZiV8Kgpv8cx4j3wbxY(var16));
            var38 = new TextView(this.getParentActivity());
            var38.setTextSize(1, 14.0F);
            var38.setTextColor(Theme.getColor("dialogTextBlue2"));
            var38.setGravity(17);
            var38.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            var38.setText(LocaleController.getString("Save", 2131560626).toUpperCase());
            var38.setPadding(AndroidUtilities.dp(10.0F), 0, AndroidUtilities.dp(10.0F), 0);
            var37.addView(var38, LayoutHelper.createFrame(-2, 36, 53));
            var38.setOnClickListener(new _$$Lambda$DataAutoDownloadActivity$bStK8LWlLSXpGpzkzlpaQV4c9zo(this, var21, var26, var29, var13, var36, var2, var28, var12, var16, var1));
            this.showDialog(var16.create());
         } else {
            var6 = this.currentPresetNum;
            if (var6 != 3) {
               if (var6 == 0) {
                  this.typePreset.set(this.lowPreset);
               } else if (var6 == 1) {
                  this.typePreset.set(this.mediumPreset);
               } else if (var6 == 2) {
                  this.typePreset.set(this.highPreset);
               }
            }

            var6 = 0;

            boolean var27;
            while(true) {
               if (var6 >= this.typePreset.mask.length) {
                  var27 = false;
                  break;
               }

               if ((var10.mask[var6] & var26) != 0) {
                  var27 = true;
                  break;
               }

               ++var6;
            }

            while(true) {
               int[] var32 = this.typePreset.mask;
               if (var7 >= var32.length) {
                  var31 = MessagesController.getMainSettings(super.currentAccount).edit();
                  var31.putString(var28, this.typePreset.toString());
                  this.currentPresetNum = 3;
                  var31.putInt(var12, 3);
                  var5 = this.currentType;
                  if (var5 == 0) {
                     DownloadController.getInstance(super.currentAccount).currentMobilePreset = this.currentPresetNum;
                  } else if (var5 == 1) {
                     DownloadController.getInstance(super.currentAccount).currentWifiPreset = this.currentPresetNum;
                  } else {
                     DownloadController.getInstance(super.currentAccount).currentRoamingPreset = this.currentPresetNum;
                  }

                  var31.commit();
                  var33.setChecked(var9 ^ true);
                  RecyclerView.ViewHolder var25 = this.listView.findContainingViewHolder(var1);
                  if (var25 != null) {
                     this.listAdapter.onBindViewHolder(var25, var2);
                  }

                  DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
                  this.wereAnyChanges = true;
                  this.fillPresets();
                  break;
               }

               if (var9) {
                  var32[var7] &= ~var26;
               } else if (!var27) {
                  var32[var7] |= var26;
               }

               ++var7;
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$DataAutoDownloadActivity(TextCheckBoxCell var1, TextCheckBoxCell[] var2, int var3, MaxFileSizeCell[] var4, TextCheckCell[] var5, AnimatorSet[] var6, View var7) {
      if (var7.isEnabled()) {
         boolean var8 = var1.isChecked();
         boolean var9 = true;
         var1.setChecked(var8 ^ true);
         int var10 = 0;

         while(true) {
            if (var10 >= var2.length) {
               var9 = false;
               break;
            }

            if (var2[var10].isChecked()) {
               break;
            }

            ++var10;
         }

         if (var3 == this.videosRow && var4[0].isEnabled() != var9) {
            ArrayList var11 = new ArrayList();
            var4[0].setEnabled(var9, var11);
            if (var4[0].getSize() > 2097152L) {
               var5[0].setEnabled(var9, var11);
            }

            if (var6[0] != null) {
               var6[0].cancel();
               var6[0] = null;
            }

            var6[0] = new AnimatorSet();
            var6[0].playTogether(var11);
            var6[0].addListener(new DataAutoDownloadActivity$2(this, var6));
            var6[0].setDuration(150L);
            var6[0].start();
         }

      }
   }

   // $FF: synthetic method
   public void lambda$null$3$DataAutoDownloadActivity(TextCheckBoxCell[] var1, int var2, MaxFileSizeCell[] var3, int var4, TextCheckCell[] var5, int var6, String var7, String var8, BottomSheet.Builder var9, View var10, View var11) {
      int var12 = this.currentPresetNum;
      if (var12 != 3) {
         if (var12 == 0) {
            this.typePreset.set(this.lowPreset);
         } else if (var12 == 1) {
            this.typePreset.set(this.mediumPreset);
         } else if (var12 == 2) {
            this.typePreset.set(this.highPreset);
         }
      }

      for(var12 = 0; var12 < 4; ++var12) {
         int[] var15;
         if (var1[var12].isChecked()) {
            var15 = this.typePreset.mask;
            var15[var12] |= var2;
         } else {
            var15 = this.typePreset.mask;
            var15[var12] &= ~var2;
         }
      }

      if (var3[0] != null) {
         var3[0].getSize();
         this.typePreset.sizes[var4] = (int)var3[0].getSize();
      }

      if (var5[0] != null) {
         if (var6 == this.videosRow) {
            this.typePreset.preloadVideo = var5[0].isChecked();
         } else {
            this.typePreset.preloadMusic = var5[0].isChecked();
         }
      }

      Editor var13 = MessagesController.getMainSettings(super.currentAccount).edit();
      var13.putString(var7, this.typePreset.toString());
      this.currentPresetNum = 3;
      var13.putInt(var8, 3);
      var2 = this.currentType;
      if (var2 == 0) {
         DownloadController.getInstance(super.currentAccount).currentMobilePreset = this.currentPresetNum;
      } else if (var2 == 1) {
         DownloadController.getInstance(super.currentAccount).currentWifiPreset = this.currentPresetNum;
      } else {
         DownloadController.getInstance(super.currentAccount).currentRoamingPreset = this.currentPresetNum;
      }

      var13.commit();
      var9.getDismissRunnable().run();
      RecyclerView.ViewHolder var14 = this.listView.findContainingViewHolder(var10);
      if (var14 != null) {
         this.animateChecked = true;
         this.listAdapter.onBindViewHolder(var14, var6);
         this.animateChecked = false;
      }

      DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
      this.wereAnyChanges = true;
      this.fillPresets();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.fillPresets();
      this.updateRows();
      return true;
   }

   public void onPause() {
      super.onPause();
      if (this.wereAnyChanges) {
         DownloadController.getInstance(super.currentAccount).savePresetToServer(this.currentType);
         this.wereAnyChanges = false;
      }

   }

   public void onResume() {
      super.onResume();
      DataAutoDownloadActivity.ListAdapter var1 = this.listAdapter;
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
         return DataAutoDownloadActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == DataAutoDownloadActivity.this.autoDownloadRow) {
            return 0;
         } else if (var1 == DataAutoDownloadActivity.this.usageSectionRow) {
            return 1;
         } else if (var1 != DataAutoDownloadActivity.this.usageHeaderRow && var1 != DataAutoDownloadActivity.this.typeHeaderRow) {
            if (var1 == DataAutoDownloadActivity.this.usageProgressRow) {
               return 3;
            } else {
               return var1 != DataAutoDownloadActivity.this.photosRow && var1 != DataAutoDownloadActivity.this.videosRow && var1 != DataAutoDownloadActivity.this.filesRow ? 5 : 4;
            }
         } else {
            return 2;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != DataAutoDownloadActivity.this.photosRow && var2 != DataAutoDownloadActivity.this.videosRow && var2 != DataAutoDownloadActivity.this.filesRow) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         boolean var12;
         String var15;
         if (var3 != 0) {
            if (var3 != 2) {
               if (var3 != 4) {
                  if (var3 == 5) {
                     TextInfoPrivacyCell var14 = (TextInfoPrivacyCell)var1.itemView;
                     if (var2 == DataAutoDownloadActivity.this.typeSectionRow) {
                        var14.setText(LocaleController.getString("AutoDownloadAudioInfo", 2131558740));
                        var14.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                        var14.setFixedSize(0);
                     } else if (var2 == DataAutoDownloadActivity.this.autoDownloadSectionRow) {
                        if (DataAutoDownloadActivity.this.usageHeaderRow == -1) {
                           var14.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                           if (DataAutoDownloadActivity.this.currentType == 0) {
                              var14.setText(LocaleController.getString("AutoDownloadOnMobileDataInfo", 2131558759));
                           } else if (DataAutoDownloadActivity.this.currentType == 1) {
                              var14.setText(LocaleController.getString("AutoDownloadOnWiFiDataInfo", 2131558764));
                           } else if (DataAutoDownloadActivity.this.currentType == 2) {
                              var14.setText(LocaleController.getString("AutoDownloadOnRoamingDataInfo", 2131558761));
                           }
                        } else {
                           var14.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                           var14.setText((CharSequence)null);
                           var14.setFixedSize(12);
                        }
                     }
                  }
               } else {
                  NotificationsCheckCell var4 = (NotificationsCheckCell)var1.itemView;
                  byte var17;
                  if (var2 == DataAutoDownloadActivity.this.photosRow) {
                     var15 = LocaleController.getString("AutoDownloadPhotos", 2131558765);
                     var17 = 1;
                  } else if (var2 == DataAutoDownloadActivity.this.videosRow) {
                     var15 = LocaleController.getString("AutoDownloadVideos", 2131558775);
                     var17 = 4;
                  } else {
                     var15 = LocaleController.getString("AutoDownloadFiles", 2131558745);
                     var17 = 8;
                  }

                  DownloadController.Preset var5;
                  if (DataAutoDownloadActivity.this.currentType == 0) {
                     var5 = DownloadController.getInstance(DataAutoDownloadActivity.access$2300(DataAutoDownloadActivity.this)).getCurrentMobilePreset();
                  } else if (DataAutoDownloadActivity.this.currentType == 1) {
                     var5 = DownloadController.getInstance(DataAutoDownloadActivity.access$2400(DataAutoDownloadActivity.this)).getCurrentWiFiPreset();
                  } else {
                     var5 = DownloadController.getInstance(DataAutoDownloadActivity.access$2500(DataAutoDownloadActivity.this)).getCurrentRoamingPreset();
                  }

                  int var6 = var5.sizes[DownloadController.typeToIndex(var17)];
                  StringBuilder var7 = new StringBuilder();
                  int var8 = 0;
                  int var9 = 0;

                  while(true) {
                     int[] var10 = var5.mask;
                     if (var8 >= var10.length) {
                        StringBuilder var18;
                        label124: {
                           if (var9 == 4) {
                              var7.setLength(0);
                              if (var2 == DataAutoDownloadActivity.this.photosRow) {
                                 var7.append(LocaleController.getString("AutoDownloadOnAllChats", 2131558756));
                              } else {
                                 var7.append(LocaleController.formatString("AutoDownloadUpToOnAllChats", 2131558774, AndroidUtilities.formatFileSize((long)var6)));
                              }
                           } else {
                              if (var9 != 0) {
                                 if (var2 == DataAutoDownloadActivity.this.photosRow) {
                                    var18 = new StringBuilder(LocaleController.formatString("AutoDownloadOnFor", 2131558757, var7.toString()));
                                 } else {
                                    var18 = new StringBuilder(LocaleController.formatString("AutoDownloadOnUpToFor", 2131558762, AndroidUtilities.formatFileSize((long)var6), var7.toString()));
                                 }
                                 break label124;
                              }

                              var7.append(LocaleController.getString("AutoDownloadOff", 2131558755));
                           }

                           var18 = var7;
                        }

                        if (DataAutoDownloadActivity.this.animateChecked) {
                           if (var9 != 0) {
                              var12 = true;
                           } else {
                              var12 = false;
                           }

                           var4.setChecked(var12);
                        }

                        if (var9 != 0) {
                           var12 = true;
                        } else {
                           var12 = false;
                        }

                        boolean var13;
                        if (var2 != DataAutoDownloadActivity.this.filesRow) {
                           var13 = true;
                        } else {
                           var13 = false;
                        }

                        var4.setTextAndValueAndCheck(var15, var18, var12, 0, true, var13);
                        break;
                     }

                     int var11 = var9;
                     if ((var10[var8] & var17) != 0) {
                        if (var7.length() != 0) {
                           var7.append(", ");
                        }

                        if (var8 != 0) {
                           if (var8 != 1) {
                              if (var8 != 2) {
                                 if (var8 == 3) {
                                    var7.append(LocaleController.getString("AutoDownloadChannels", 2131558741));
                                 }
                              } else {
                                 var7.append(LocaleController.getString("AutoDownloadGroups", 2131558748));
                              }
                           } else {
                              var7.append(LocaleController.getString("AutoDownloadPm", 2131558768));
                           }
                        } else {
                           var7.append(LocaleController.getString("AutoDownloadContacts", 2131558742));
                        }

                        var11 = var9 + 1;
                     }

                     ++var8;
                     var9 = var11;
                  }
               }
            } else {
               HeaderCell var16 = (HeaderCell)var1.itemView;
               if (var2 == DataAutoDownloadActivity.this.usageHeaderRow) {
                  var16.setText(LocaleController.getString("AutoDownloadDataUsage", 2131558744));
               } else if (var2 == DataAutoDownloadActivity.this.typeHeaderRow) {
                  var16.setText(LocaleController.getString("AutoDownloadTypes", 2131558773));
               }
            }
         } else {
            TextCheckCell var19 = (TextCheckCell)var1.itemView;
            if (var2 == DataAutoDownloadActivity.this.autoDownloadRow) {
               var19.setDrawCheckRipple(true);
               var19.setTextAndCheck(LocaleController.getString("AutoDownloadMedia", 2131558753), DataAutoDownloadActivity.this.typePreset.enabled, false);
               var12 = DataAutoDownloadActivity.this.typePreset.enabled;
               String var20 = "windowBackgroundChecked";
               if (var12) {
                  var15 = "windowBackgroundChecked";
               } else {
                  var15 = "windowBackgroundUnchecked";
               }

               var19.setTag(var15);
               if (DataAutoDownloadActivity.this.typePreset.enabled) {
                  var15 = var20;
               } else {
                  var15 = "windowBackgroundUnchecked";
               }

               var19.setBackgroundColor(Theme.getColor(var15));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     if (var2 != 4) {
                        if (var2 != 5) {
                           var3 = null;
                        } else {
                           var3 = new TextInfoPrivacyCell(this.mContext);
                           ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
                        }
                     } else {
                        var3 = new NotificationsCheckCell(this.mContext);
                        ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                     }
                  } else {
                     var3 = DataAutoDownloadActivity.this.new PresetChooseView(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new HeaderCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new ShadowSectionCell(this.mContext);
            }
         } else {
            var3 = new TextCheckCell(this.mContext);
            ((TextCheckCell)var3).setColors("windowBackgroundCheckText", "switchTrackBlue", "switchTrackBlueChecked", "switchTrackBlueThumb", "switchTrackBlueThumbChecked");
            ((TextCheckCell)var3).setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            ((TextCheckCell)var3).setHeight(56);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }

   private class PresetChooseView extends View {
      private int circleSize;
      private String custom;
      private int customSize;
      private int gapSize;
      private String high;
      private int highSize;
      private int lineSize;
      private String low;
      private int lowSize;
      private String medium;
      private int mediumSize;
      private boolean moving;
      private Paint paint = new Paint(1);
      private int sideSide;
      private boolean startMoving;
      private int startMovingPreset;
      private float startX;
      private TextPaint textPaint = new TextPaint(1);

      public PresetChooseView(Context var2) {
         super(var2);
         this.textPaint.setTextSize((float)AndroidUtilities.dp(13.0F));
         this.low = LocaleController.getString("AutoDownloadLow", 2131558750);
         this.lowSize = (int)Math.ceil((double)this.textPaint.measureText(this.low));
         this.medium = LocaleController.getString("AutoDownloadMedium", 2131558754);
         this.mediumSize = (int)Math.ceil((double)this.textPaint.measureText(this.medium));
         this.high = LocaleController.getString("AutoDownloadHigh", 2131558749);
         this.highSize = (int)Math.ceil((double)this.textPaint.measureText(this.high));
         this.custom = LocaleController.getString("AutoDownloadCustom", 2131558743);
         this.customSize = (int)Math.ceil((double)this.textPaint.measureText(this.custom));
      }

      private void setPreset(int var1) {
         DataAutoDownloadActivity.this.selectedPreset = var1;
         DownloadController.Preset var2 = (DownloadController.Preset)DataAutoDownloadActivity.this.presets.get(DataAutoDownloadActivity.this.selectedPreset);
         DownloadController.Preset var3 = DataAutoDownloadActivity.this.lowPreset;
         var1 = 0;
         if (var2 == var3) {
            DataAutoDownloadActivity.this.currentPresetNum = 0;
         } else if (var2 == DataAutoDownloadActivity.this.mediumPreset) {
            DataAutoDownloadActivity.this.currentPresetNum = 1;
         } else if (var2 == DataAutoDownloadActivity.this.highPreset) {
            DataAutoDownloadActivity.this.currentPresetNum = 2;
         } else {
            DataAutoDownloadActivity.this.currentPresetNum = 3;
         }

         if (DataAutoDownloadActivity.this.currentType == 0) {
            DownloadController.getInstance(DataAutoDownloadActivity.access$700(DataAutoDownloadActivity.this)).currentMobilePreset = DataAutoDownloadActivity.this.currentPresetNum;
         } else if (DataAutoDownloadActivity.this.currentType == 1) {
            DownloadController.getInstance(DataAutoDownloadActivity.access$800(DataAutoDownloadActivity.this)).currentWifiPreset = DataAutoDownloadActivity.this.currentPresetNum;
         } else {
            DownloadController.getInstance(DataAutoDownloadActivity.access$900(DataAutoDownloadActivity.this)).currentRoamingPreset = DataAutoDownloadActivity.this.currentPresetNum;
         }

         Editor var4 = MessagesController.getMainSettings(DataAutoDownloadActivity.access$1000(DataAutoDownloadActivity.this)).edit();
         var4.putInt(DataAutoDownloadActivity.this.key2, DataAutoDownloadActivity.this.currentPresetNum);
         var4.commit();
         DownloadController.getInstance(DataAutoDownloadActivity.access$1200(DataAutoDownloadActivity.this)).checkAutodownloadSettings();

         for(; var1 < 3; ++var1) {
            RecyclerView.ViewHolder var5 = DataAutoDownloadActivity.this.listView.findViewHolderForAdapterPosition(DataAutoDownloadActivity.this.photosRow + var1);
            if (var5 != null) {
               DataAutoDownloadActivity.this.listAdapter.onBindViewHolder(var5, DataAutoDownloadActivity.this.photosRow + var1);
            }
         }

         DataAutoDownloadActivity.this.wereAnyChanges = true;
         this.invalidate();
      }

      protected void onDraw(Canvas var1) {
         this.textPaint.setColor(Theme.getColor("windowBackgroundWhiteGrayText"));
         int var2 = this.getMeasuredHeight() / 2 + AndroidUtilities.dp(11.0F);

         for(int var3 = 0; var3 < DataAutoDownloadActivity.this.presets.size(); ++var3) {
            int var4 = this.sideSide;
            int var5 = this.lineSize;
            int var6 = this.gapSize;
            int var7 = this.circleSize;
            var5 = var4 + (var5 + var6 * 2 + var7) * var3 + var7 / 2;
            if (var3 <= DataAutoDownloadActivity.this.selectedPreset) {
               this.paint.setColor(Theme.getColor("switchTrackChecked"));
            } else {
               this.paint.setColor(Theme.getColor("switchTrack"));
            }

            float var8 = (float)var5;
            float var9 = (float)var2;
            if (var3 == DataAutoDownloadActivity.this.selectedPreset) {
               var4 = AndroidUtilities.dp(6.0F);
            } else {
               var4 = this.circleSize / 2;
            }

            var1.drawCircle(var8, var9, (float)var4, this.paint);
            if (var3 != 0) {
               label53: {
                  var4 = this.circleSize / 2;
                  var7 = this.gapSize;
                  var6 = this.lineSize;
                  var7 = var5 - var4 - var7 - var6;
                  if (var3 != DataAutoDownloadActivity.this.selectedPreset) {
                     var4 = var6;
                     if (var3 != DataAutoDownloadActivity.this.selectedPreset + 1) {
                        break label53;
                     }
                  }

                  var4 = var6 - AndroidUtilities.dp(3.0F);
               }

               var6 = var7;
               if (var3 == DataAutoDownloadActivity.this.selectedPreset + 1) {
                  var6 = var7 + AndroidUtilities.dp(3.0F);
               }

               var1.drawRect((float)var6, (float)(var2 - AndroidUtilities.dp(1.0F)), (float)(var6 + var4), (float)(AndroidUtilities.dp(1.0F) + var2), this.paint);
            }

            DownloadController.Preset var10 = (DownloadController.Preset)DataAutoDownloadActivity.this.presets.get(var3);
            String var11;
            if (var10 == DataAutoDownloadActivity.this.lowPreset) {
               var11 = this.low;
               var4 = this.lowSize;
            } else if (var10 == DataAutoDownloadActivity.this.mediumPreset) {
               var11 = this.medium;
               var4 = this.mediumSize;
            } else if (var10 == DataAutoDownloadActivity.this.highPreset) {
               var11 = this.high;
               var4 = this.highSize;
            } else {
               var11 = this.custom;
               var4 = this.customSize;
            }

            if (var3 == 0) {
               var1.drawText(var11, (float)AndroidUtilities.dp(22.0F), (float)AndroidUtilities.dp(28.0F), this.textPaint);
            } else if (var3 == DataAutoDownloadActivity.this.presets.size() - 1) {
               var1.drawText(var11, (float)(this.getMeasuredWidth() - var4 - AndroidUtilities.dp(22.0F)), (float)AndroidUtilities.dp(28.0F), this.textPaint);
            } else {
               var1.drawText(var11, (float)(var5 - var4 / 2), (float)AndroidUtilities.dp(28.0F), this.textPaint);
            }
         }

      }

      protected void onMeasure(int var1, int var2) {
         super.onMeasure(var1, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(74.0F), 1073741824));
         MeasureSpec.getSize(var1);
         this.circleSize = AndroidUtilities.dp(6.0F);
         this.gapSize = AndroidUtilities.dp(2.0F);
         this.sideSide = AndroidUtilities.dp(22.0F);
         this.lineSize = (this.getMeasuredWidth() - this.circleSize * DataAutoDownloadActivity.this.presets.size() - this.gapSize * 2 * (DataAutoDownloadActivity.this.presets.size() - 1) - this.sideSide * 2) / (DataAutoDownloadActivity.this.presets.size() - 1);
      }

      public boolean onTouchEvent(MotionEvent var1) {
         float var2 = var1.getX();
         int var3 = var1.getAction();
         int var4 = 0;
         boolean var5 = false;
         int var6;
         int var7;
         int var8;
         if (var3 == 0) {
            this.getParent().requestDisallowInterceptTouchEvent(true);

            for(var4 = 0; var4 < DataAutoDownloadActivity.this.presets.size(); ++var4) {
               var6 = this.sideSide;
               var3 = this.lineSize;
               var7 = this.gapSize;
               var8 = this.circleSize;
               var3 = var6 + (var3 + var7 * 2 + var8) * var4 + var8 / 2;
               if (var2 > (float)(var3 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var3 + AndroidUtilities.dp(15.0F))) {
                  if (var4 == DataAutoDownloadActivity.this.selectedPreset) {
                     var5 = true;
                  }

                  this.startMoving = var5;
                  this.startX = var2;
                  this.startMovingPreset = DataAutoDownloadActivity.this.selectedPreset;
                  break;
               }
            }
         } else if (var1.getAction() == 2) {
            if (this.startMoving) {
               if (Math.abs(this.startX - var2) >= AndroidUtilities.getPixelsInCM(0.5F, true)) {
                  this.moving = true;
                  this.startMoving = false;
               }
            } else if (this.moving) {
               while(var4 < DataAutoDownloadActivity.this.presets.size()) {
                  var3 = this.sideSide;
                  var7 = this.lineSize;
                  var8 = this.gapSize;
                  var6 = this.circleSize;
                  var3 = var3 + (var8 * 2 + var7 + var6) * var4 + var6 / 2;
                  var6 = var7 / 2 + var6 / 2 + var8;
                  if (var2 > (float)(var3 - var6) && var2 < (float)(var3 + var6)) {
                     if (DataAutoDownloadActivity.this.selectedPreset != var4) {
                        this.setPreset(var4);
                     }
                     break;
                  }

                  ++var4;
               }
            }
         } else if (var1.getAction() == 1 || var1.getAction() == 3) {
            if (!this.moving) {
               for(var4 = 0; var4 < 5; ++var4) {
                  var6 = this.sideSide;
                  var7 = this.lineSize;
                  var3 = this.gapSize;
                  var8 = this.circleSize;
                  var3 = var6 + (var7 + var3 * 2 + var8) * var4 + var8 / 2;
                  if (var2 > (float)(var3 - AndroidUtilities.dp(15.0F)) && var2 < (float)(var3 + AndroidUtilities.dp(15.0F))) {
                     if (DataAutoDownloadActivity.this.selectedPreset != var4) {
                        this.setPreset(var4);
                     }
                     break;
                  }
               }
            } else if (DataAutoDownloadActivity.this.selectedPreset != this.startMovingPreset) {
               this.setPreset(DataAutoDownloadActivity.this.selectedPreset);
            }

            this.startMoving = false;
            this.moving = false;
         }

         return true;
      }
   }
}
