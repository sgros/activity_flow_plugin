package org.telegram.ui;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.VoIPHelper;

public class DataSettingsActivity extends BaseFragment {
   private AnimatorSet animatorSet;
   private int autoplayGifsRow;
   private int autoplayHeaderRow;
   private int autoplaySectionRow;
   private int autoplayVideoRow;
   private int callsSection2Row;
   private int callsSectionRow;
   private int dataUsageRow;
   private int enableAllStreamInfoRow;
   private int enableAllStreamRow;
   private int enableCacheStreamRow;
   private int enableMkvRow;
   private int enableStreamRow;
   private LinearLayoutManager layoutManager;
   private DataSettingsActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int mediaDownloadSection2Row;
   private int mediaDownloadSectionRow;
   private int mobileRow;
   private int proxyRow;
   private int proxySection2Row;
   private int proxySectionRow;
   private int quickRepliesRow;
   private int resetDownloadRow;
   private int roamingRow;
   private int rowCount;
   private int storageUsageRow;
   private int streamSectionRow;
   private int usageSection2Row;
   private int usageSectionRow;
   private int useLessDataForCallsRow;
   private int wifiRow;

   // $FF: synthetic method
   static int access$2200(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2300(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2500(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2600(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2700(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2800(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2900(DataSettingsActivity var0) {
      return var0.currentAccount;
   }

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("DataSettings", 2131559193));
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               DataSettingsActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new DataSettingsActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      RecyclerListView var3 = this.listView;
      LinearLayoutManager var4 = new LinearLayoutManager(var1, 1, false);
      this.layoutManager = var4;
      var3.setLayoutManager(var4);
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$DataSettingsActivity$ltmANPI19HrezwGNNfIa2VQKaYc(this)));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, TextCheckCell.class, HeaderCell.class, NotificationsCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var9 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var10 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack");
      ThemeDescription var11 = new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked");
      ThemeDescription var12 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var13 = this.listView;
      Paint var14 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, new ThemeDescription(var13, 0, new Class[]{View.class}, var14, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4")};
   }

   // $FF: synthetic method
   public void lambda$createView$2$DataSettingsActivity(View var1, int var2, float var3, float var4) {
      int var5 = this.mobileRow;
      byte var6 = 2;
      byte var7 = 0;
      String var10;
      String var13;
      if (var2 != var5 && var2 != this.roamingRow && var2 != this.wifiRow) {
         if (var2 == this.resetDownloadRow) {
            if (this.getParentActivity() == null || !var1.isEnabled()) {
               return;
            }

            AlertDialog.Builder var19 = new AlertDialog.Builder(this.getParentActivity());
            var19.setTitle(LocaleController.getString("ResetAutomaticMediaDownloadAlertTitle", 2131560592));
            var19.setMessage(LocaleController.getString("ResetAutomaticMediaDownloadAlert", 2131560591));
            var19.setPositiveButton(LocaleController.getString("Reset", 2131560583), new _$$Lambda$DataSettingsActivity$z7kMV53_j7S_p3GIoSYZ6qy2_tA(this));
            var19.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            AlertDialog var20 = var19.create();
            this.showDialog(var20);
            TextView var21 = (TextView)var20.getButton(-1);
            if (var21 != null) {
               var21.setTextColor(Theme.getColor("dialogTextRed2"));
            }
         } else if (var2 == this.storageUsageRow) {
            this.presentFragment(new CacheControlActivity());
         } else if (var2 == this.useLessDataForCallsRow) {
            SharedPreferences var8;
            label114: {
               var8 = MessagesController.getGlobalMainSettings();
               int var23 = var8.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
               if (var23 != 0) {
                  if (var23 == 1) {
                     var7 = 2;
                     break label114;
                  }

                  if (var23 == 2) {
                     var7 = 3;
                     break label114;
                  }

                  if (var23 == 3) {
                     var7 = 1;
                     break label114;
                  }
               }

               var7 = 0;
            }

            Activity var24 = this.getParentActivity();
            var10 = LocaleController.getString("UseLessDataNever", 2131560968);
            String var17 = LocaleController.getString("UseLessDataOnRoaming", 2131560970);
            String var27 = LocaleController.getString("UseLessDataOnMobile", 2131560969);
            String var26 = LocaleController.getString("UseLessDataAlways", 2131560967);
            var13 = LocaleController.getString("VoipUseLessData", 2131561093);
            _$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw var22 = new _$$Lambda$DataSettingsActivity$ClwlcXUhT2KAlvJObQobk4HWNyw(this, var8, var2);
            Dialog var18 = AlertsCreator.createSingleChoiceDialog(var24, new String[]{var10, var17, var27, var26}, var13, var7, var22);
            this.setVisibleDialog(var18);
            var18.show();
         } else if (var2 == this.dataUsageRow) {
            this.presentFragment(new DataUsageActivity());
         } else if (var2 == this.proxyRow) {
            this.presentFragment(new ProxyListActivity());
         } else if (var2 == this.enableStreamRow) {
            SharedConfig.toggleStreamMedia();
            ((TextCheckCell)var1).setChecked(SharedConfig.streamMedia);
         } else if (var2 == this.enableAllStreamRow) {
            SharedConfig.toggleStreamAllVideo();
            ((TextCheckCell)var1).setChecked(SharedConfig.streamAllVideo);
         } else if (var2 == this.enableMkvRow) {
            SharedConfig.toggleStreamMkv();
            ((TextCheckCell)var1).setChecked(SharedConfig.streamMkv);
         } else if (var2 == this.enableCacheStreamRow) {
            SharedConfig.toggleSaveStreamMedia();
            ((TextCheckCell)var1).setChecked(SharedConfig.saveStreamMedia);
         } else if (var2 == this.quickRepliesRow) {
            this.presentFragment(new QuickRepliesSettingsActivity());
         } else if (var2 == this.autoplayGifsRow) {
            SharedConfig.toggleAutoplayGifs();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.autoplayGifs);
            }
         } else if (var2 == this.autoplayVideoRow) {
            SharedConfig.toggleAutoplayVideo();
            if (var1 instanceof TextCheckCell) {
               ((TextCheckCell)var1).setChecked(SharedConfig.autoplayVideo);
            }
         }
      } else if (LocaleController.isRTL && var3 <= (float)AndroidUtilities.dp(76.0F) || !LocaleController.isRTL && var3 >= (float)(var1.getMeasuredWidth() - AndroidUtilities.dp(76.0F))) {
         boolean var14 = this.listAdapter.isRowEnabled(this.resetDownloadRow);
         NotificationsCheckCell var12 = (NotificationsCheckCell)var1;
         boolean var15 = var12.isChecked();
         DownloadController.Preset var9;
         DownloadController.Preset var11;
         if (var2 == this.mobileRow) {
            var9 = DownloadController.getInstance(super.currentAccount).mobilePreset;
            var11 = DownloadController.getInstance(super.currentAccount).mediumPreset;
            var10 = "mobilePreset";
            var13 = "currentMobilePreset";
         } else if (var2 == this.wifiRow) {
            var9 = DownloadController.getInstance(super.currentAccount).wifiPreset;
            var11 = DownloadController.getInstance(super.currentAccount).highPreset;
            var10 = "wifiPreset";
            var13 = "currentWifiPreset";
            var7 = 1;
         } else {
            var9 = DownloadController.getInstance(super.currentAccount).roamingPreset;
            var11 = DownloadController.getInstance(super.currentAccount).lowPreset;
            var10 = "roamingPreset";
            var13 = "currentRoamingPreset";
            var7 = 2;
         }

         if (!var15 && var9.enabled) {
            var9.set(var11);
         } else {
            var9.enabled ^= true;
         }

         Editor var25 = MessagesController.getMainSettings(super.currentAccount).edit();
         var25.putString(var10, var9.toString());
         var25.putInt(var13, 3);
         var25.commit();
         var12.setChecked(var15 ^ true);
         RecyclerView.ViewHolder var16 = this.listView.findContainingViewHolder(var1);
         if (var16 != null) {
            this.listAdapter.onBindViewHolder(var16, var2);
         }

         DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
         DownloadController.getInstance(super.currentAccount).savePresetToServer(var7);
         if (var14 != this.listAdapter.isRowEnabled(this.resetDownloadRow)) {
            this.listAdapter.notifyItemChanged(this.resetDownloadRow);
         }
      } else {
         if (var2 == this.mobileRow) {
            var7 = 0;
         } else {
            var7 = var6;
            if (var2 == this.wifiRow) {
               var7 = 1;
            }
         }

         this.presentFragment(new DataAutoDownloadActivity(var7));
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$DataSettingsActivity(DialogInterface var1, int var2) {
      Editor var3 = MessagesController.getMainSettings(super.currentAccount).edit();
      byte var4 = 0;

      for(var2 = 0; var2 < 3; ++var2) {
         DownloadController.Preset var5;
         String var6;
         DownloadController.Preset var7;
         if (var2 == 0) {
            var7 = DownloadController.getInstance(super.currentAccount).mobilePreset;
            var5 = DownloadController.getInstance(super.currentAccount).mediumPreset;
            var6 = "mobilePreset";
         } else if (var2 == 1) {
            var7 = DownloadController.getInstance(super.currentAccount).wifiPreset;
            var5 = DownloadController.getInstance(super.currentAccount).highPreset;
            var6 = "wifiPreset";
         } else {
            var7 = DownloadController.getInstance(super.currentAccount).roamingPreset;
            var5 = DownloadController.getInstance(super.currentAccount).lowPreset;
            var6 = "roamingPreset";
         }

         var7.set(var5);
         var7.enabled = var5.isEnabled();
         DownloadController.getInstance(super.currentAccount).currentMobilePreset = 3;
         var3.putInt("currentMobilePreset", 3);
         DownloadController.getInstance(super.currentAccount).currentWifiPreset = 3;
         var3.putInt("currentWifiPreset", 3);
         DownloadController.getInstance(super.currentAccount).currentRoamingPreset = 3;
         var3.putInt("currentRoamingPreset", 3);
         var3.putString(var6, var7.toString());
      }

      var3.commit();
      DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();

      for(var2 = var4; var2 < 3; ++var2) {
         DownloadController.getInstance(super.currentAccount).savePresetToServer(var2);
      }

      this.listAdapter.notifyItemRangeChanged(this.mobileRow, 4);
   }

   // $FF: synthetic method
   public void lambda$null$1$DataSettingsActivity(SharedPreferences var1, int var2, DialogInterface var3, int var4) {
      byte var5 = 3;
      if (var4 != 0) {
         if (var4 != 1) {
            if (var4 != 2) {
               if (var4 != 3) {
                  var5 = -1;
               } else {
                  var5 = 2;
               }
            } else {
               var5 = 1;
            }
         }
      } else {
         var5 = 0;
      }

      if (var5 != -1) {
         var1.edit().putInt("VoipDataSaving", var5).commit();
      }

      DataSettingsActivity.ListAdapter var6 = this.listAdapter;
      if (var6 != null) {
         var6.notifyItemChanged(var2);
      }

   }

   protected void onDialogDismiss(Dialog var1) {
      DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      DownloadController.getInstance(super.currentAccount).loadAutoDownloadConfig(true);
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.usageSectionRow = var1;
      var1 = this.rowCount++;
      this.storageUsageRow = var1;
      var1 = this.rowCount++;
      this.dataUsageRow = var1;
      var1 = this.rowCount++;
      this.usageSection2Row = var1;
      var1 = this.rowCount++;
      this.mediaDownloadSectionRow = var1;
      var1 = this.rowCount++;
      this.mobileRow = var1;
      var1 = this.rowCount++;
      this.wifiRow = var1;
      var1 = this.rowCount++;
      this.roamingRow = var1;
      var1 = this.rowCount++;
      this.resetDownloadRow = var1;
      var1 = this.rowCount++;
      this.mediaDownloadSection2Row = var1;
      var1 = this.rowCount++;
      this.autoplayHeaderRow = var1;
      var1 = this.rowCount++;
      this.autoplayGifsRow = var1;
      var1 = this.rowCount++;
      this.autoplayVideoRow = var1;
      var1 = this.rowCount++;
      this.autoplaySectionRow = var1;
      var1 = this.rowCount++;
      this.streamSectionRow = var1;
      var1 = this.rowCount++;
      this.enableStreamRow = var1;
      if (BuildVars.DEBUG_VERSION) {
         var1 = this.rowCount++;
         this.enableMkvRow = var1;
         var1 = this.rowCount++;
         this.enableAllStreamRow = var1;
      } else {
         this.enableAllStreamRow = -1;
         this.enableMkvRow = -1;
      }

      var1 = this.rowCount++;
      this.enableAllStreamInfoRow = var1;
      this.enableCacheStreamRow = -1;
      var1 = this.rowCount++;
      this.callsSectionRow = var1;
      var1 = this.rowCount++;
      this.useLessDataForCallsRow = var1;
      var1 = this.rowCount++;
      this.quickRepliesRow = var1;
      var1 = this.rowCount++;
      this.callsSection2Row = var1;
      var1 = this.rowCount++;
      this.proxySectionRow = var1;
      var1 = this.rowCount++;
      this.proxyRow = var1;
      var1 = this.rowCount++;
      this.proxySection2Row = var1;
      return true;
   }

   public void onResume() {
      super.onResume();
      DataSettingsActivity.ListAdapter var1 = this.listAdapter;
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
         return DataSettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 != DataSettingsActivity.this.mediaDownloadSection2Row && var1 != DataSettingsActivity.this.usageSection2Row && var1 != DataSettingsActivity.this.callsSection2Row && var1 != DataSettingsActivity.this.proxySection2Row && var1 != DataSettingsActivity.this.autoplaySectionRow) {
            if (var1 != DataSettingsActivity.this.mediaDownloadSectionRow && var1 != DataSettingsActivity.this.streamSectionRow && var1 != DataSettingsActivity.this.callsSectionRow && var1 != DataSettingsActivity.this.usageSectionRow && var1 != DataSettingsActivity.this.proxySectionRow && var1 != DataSettingsActivity.this.autoplayHeaderRow) {
               if (var1 != DataSettingsActivity.this.enableCacheStreamRow && var1 != DataSettingsActivity.this.enableStreamRow && var1 != DataSettingsActivity.this.enableAllStreamRow && var1 != DataSettingsActivity.this.enableMkvRow && var1 != DataSettingsActivity.this.autoplayGifsRow && var1 != DataSettingsActivity.this.autoplayVideoRow) {
                  if (var1 == DataSettingsActivity.this.enableAllStreamInfoRow) {
                     return 4;
                  } else {
                     return var1 != DataSettingsActivity.this.mobileRow && var1 != DataSettingsActivity.this.wifiRow && var1 != DataSettingsActivity.this.roamingRow ? 1 : 5;
                  }
               } else {
                  return 3;
               }
            } else {
               return 2;
            }
         } else {
            return 0;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return this.isRowEnabled(var1.getAdapterPosition());
      }

      public boolean isRowEnabled(int var1) {
         int var2 = DataSettingsActivity.this.resetDownloadRow;
         boolean var3 = false;
         boolean var4 = false;
         if (var1 == var2) {
            DownloadController var5 = DownloadController.getInstance(DataSettingsActivity.access$2900(DataSettingsActivity.this));
            if (!var5.lowPreset.equals(var5.getCurrentRoamingPreset()) || var5.lowPreset.isEnabled() != var5.roamingPreset.enabled || !var5.mediumPreset.equals(var5.getCurrentMobilePreset()) || var5.mediumPreset.isEnabled() != var5.mobilePreset.enabled || !var5.highPreset.equals(var5.getCurrentWiFiPreset()) || var5.highPreset.isEnabled() != var5.wifiPreset.enabled) {
               var4 = true;
            }

            return var4;
         } else {
            if (var1 != DataSettingsActivity.this.mobileRow && var1 != DataSettingsActivity.this.roamingRow && var1 != DataSettingsActivity.this.wifiRow && var1 != DataSettingsActivity.this.storageUsageRow && var1 != DataSettingsActivity.this.useLessDataForCallsRow && var1 != DataSettingsActivity.this.dataUsageRow && var1 != DataSettingsActivity.this.proxyRow && var1 != DataSettingsActivity.this.enableCacheStreamRow && var1 != DataSettingsActivity.this.enableStreamRow && var1 != DataSettingsActivity.this.enableAllStreamRow && var1 != DataSettingsActivity.this.enableMkvRow && var1 != DataSettingsActivity.this.quickRepliesRow && var1 != DataSettingsActivity.this.autoplayVideoRow) {
               var4 = var3;
               if (var1 != DataSettingsActivity.this.autoplayGifsRow) {
                  return var4;
               }
            }

            var4 = true;
            return var4;
         }
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            boolean var4 = false;
            String var16;
            if (var3 != 1) {
               if (var3 != 2) {
                  if (var3 != 3) {
                     if (var3 != 4) {
                        if (var3 == 5) {
                           NotificationsCheckCell var5 = (NotificationsCheckCell)var1.itemView;
                           StringBuilder var6 = new StringBuilder();
                           DownloadController.Preset var7;
                           if (var2 == DataSettingsActivity.this.mobileRow) {
                              var16 = LocaleController.getString("WhenUsingMobileData", 2131561113);
                              var4 = DownloadController.getInstance(DataSettingsActivity.access$2200(DataSettingsActivity.this)).mobilePreset.enabled;
                              var7 = DownloadController.getInstance(DataSettingsActivity.access$2300(DataSettingsActivity.this)).getCurrentMobilePreset();
                           } else if (var2 == DataSettingsActivity.this.wifiRow) {
                              var16 = LocaleController.getString("WhenConnectedOnWiFi", 2131561111);
                              var4 = DownloadController.getInstance(DataSettingsActivity.access$2500(DataSettingsActivity.this)).wifiPreset.enabled;
                              var7 = DownloadController.getInstance(DataSettingsActivity.access$2600(DataSettingsActivity.this)).getCurrentWiFiPreset();
                           } else {
                              var16 = LocaleController.getString("WhenRoaming", 2131561112);
                              var4 = DownloadController.getInstance(DataSettingsActivity.access$2700(DataSettingsActivity.this)).roamingPreset.enabled;
                              var7 = DownloadController.getInstance(DataSettingsActivity.access$2800(DataSettingsActivity.this)).getCurrentRoamingPreset();
                           }

                           int var8 = 0;
                           boolean var9 = false;
                           var2 = 0;
                           boolean var10 = false;
                           boolean var11 = false;

                           while(true) {
                              int[] var12 = var7.mask;
                              if (var8 >= var12.length) {
                                 if (var7.enabled && var2 != 0) {
                                    if (var9) {
                                       var6.append(LocaleController.getString("AutoDownloadPhotosOn", 2131558766));
                                    }

                                    if (var10) {
                                       if (var6.length() > 0) {
                                          var6.append(", ");
                                       }

                                       var6.append(LocaleController.getString("AutoDownloadVideosOn", 2131558776));
                                       var6.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize((long)var7.sizes[DownloadController.typeToIndex(4)], true)));
                                    }

                                    if (var11) {
                                       if (var6.length() > 0) {
                                          var6.append(", ");
                                       }

                                       var6.append(LocaleController.getString("AutoDownloadFilesOn", 2131558746));
                                       var6.append(String.format(" (%1$s)", AndroidUtilities.formatFileSize((long)var7.sizes[DownloadController.typeToIndex(8)], true)));
                                    }
                                 } else {
                                    var6.append(LocaleController.getString("NoMediaAutoDownload", 2131559930));
                                 }

                                 if ((var9 || var10 || var11) && var4) {
                                    var4 = true;
                                 } else {
                                    var4 = false;
                                 }

                                 var5.setTextAndValueAndCheck(var16, var6, var4, 0, true, true);
                                 break;
                              }

                              boolean var13 = var9;
                              var3 = var2;
                              if (!var9) {
                                 var13 = var9;
                                 var3 = var2;
                                 if ((var12[var8] & 1) != 0) {
                                    var3 = var2 + 1;
                                    var13 = true;
                                 }
                              }

                              int var24 = var3;
                              boolean var14 = var10;
                              if (!var10) {
                                 var24 = var3;
                                 var14 = var10;
                                 if ((var7.mask[var8] & 4) != 0) {
                                    var24 = var3 + 1;
                                    var14 = true;
                                 }
                              }

                              var2 = var24;
                              boolean var20 = var11;
                              if (!var11) {
                                 var2 = var24;
                                 var20 = var11;
                                 if ((var7.mask[var8] & 8) != 0) {
                                    var2 = var24 + 1;
                                    var20 = true;
                                 }
                              }

                              ++var8;
                              var9 = var13;
                              var10 = var14;
                              var11 = var20;
                           }
                        }
                     } else {
                        TextInfoPrivacyCell var17 = (TextInfoPrivacyCell)var1.itemView;
                        if (var2 == DataSettingsActivity.this.enableAllStreamInfoRow) {
                           var17.setText(LocaleController.getString("EnableAllStreamingInfo", 2131559347));
                        }
                     }
                  } else {
                     TextCheckCell var18 = (TextCheckCell)var1.itemView;
                     if (var2 == DataSettingsActivity.this.enableStreamRow) {
                        String var22 = LocaleController.getString("EnableStreaming", 2131559349);
                        boolean var15 = SharedConfig.streamMedia;
                        if (DataSettingsActivity.this.enableAllStreamRow != -1) {
                           var4 = true;
                        }

                        var18.setTextAndCheck(var22, var15, var4);
                     } else if (var2 != DataSettingsActivity.this.enableCacheStreamRow) {
                        if (var2 == DataSettingsActivity.this.enableMkvRow) {
                           var18.setTextAndCheck("(beta only) Show MKV as Video", SharedConfig.streamMkv, true);
                        } else if (var2 == DataSettingsActivity.this.enableAllStreamRow) {
                           var18.setTextAndCheck("(beta only) Stream All Videos", SharedConfig.streamAllVideo, false);
                        } else if (var2 == DataSettingsActivity.this.autoplayGifsRow) {
                           var18.setTextAndCheck(LocaleController.getString("AutoplayGIF", 2131558803), SharedConfig.autoplayGifs, true);
                        } else if (var2 == DataSettingsActivity.this.autoplayVideoRow) {
                           var18.setTextAndCheck(LocaleController.getString("AutoplayVideo", 2131558805), SharedConfig.autoplayVideo, false);
                        }
                     }
                  }
               } else {
                  HeaderCell var19 = (HeaderCell)var1.itemView;
                  if (var2 == DataSettingsActivity.this.mediaDownloadSectionRow) {
                     var19.setText(LocaleController.getString("AutomaticMediaDownload", 2131558802));
                  } else if (var2 == DataSettingsActivity.this.usageSectionRow) {
                     var19.setText(LocaleController.getString("DataUsage", 2131559194));
                  } else if (var2 == DataSettingsActivity.this.callsSectionRow) {
                     var19.setText(LocaleController.getString("Calls", 2131558888));
                  } else if (var2 == DataSettingsActivity.this.proxySectionRow) {
                     var19.setText(LocaleController.getString("Proxy", 2131560516));
                  } else if (var2 == DataSettingsActivity.this.streamSectionRow) {
                     var19.setText(LocaleController.getString("Streaming", 2131560833));
                  } else if (var2 == DataSettingsActivity.this.autoplayHeaderRow) {
                     var19.setText(LocaleController.getString("AutoplayMedia", 2131558804));
                  }
               }
            } else {
               TextSettingsCell var23 = (TextSettingsCell)var1.itemView;
               var23.setCanDisable(false);
               var23.setTextColor(Theme.getColor("windowBackgroundWhiteBlackText"));
               if (var2 == DataSettingsActivity.this.storageUsageRow) {
                  var23.setText(LocaleController.getString("StorageUsage", 2131560832), true);
               } else if (var2 == DataSettingsActivity.this.useLessDataForCallsRow) {
                  SharedPreferences var21 = MessagesController.getGlobalMainSettings();
                  var16 = null;
                  var2 = var21.getInt("VoipDataSaving", VoIPHelper.getDataSavingDefault());
                  if (var2 != 0) {
                     if (var2 != 1) {
                        if (var2 != 2) {
                           if (var2 == 3) {
                              var16 = LocaleController.getString("UseLessDataOnRoaming", 2131560970);
                           }
                        } else {
                           var16 = LocaleController.getString("UseLessDataAlways", 2131560967);
                        }
                     } else {
                        var16 = LocaleController.getString("UseLessDataOnMobile", 2131560969);
                     }
                  } else {
                     var16 = LocaleController.getString("UseLessDataNever", 2131560968);
                  }

                  var23.setTextAndValue(LocaleController.getString("VoipUseLessData", 2131561093), var16, true);
               } else if (var2 == DataSettingsActivity.this.dataUsageRow) {
                  var23.setText(LocaleController.getString("NetworkUsage", 2131559889), false);
               } else if (var2 == DataSettingsActivity.this.proxyRow) {
                  var23.setText(LocaleController.getString("ProxySettings", 2131560519), false);
               } else if (var2 == DataSettingsActivity.this.resetDownloadRow) {
                  var23.setCanDisable(true);
                  var23.setTextColor(Theme.getColor("windowBackgroundWhiteRedText"));
                  var23.setText(LocaleController.getString("ResetAutomaticMediaDownload", 2131560590), false);
               } else if (var2 == DataSettingsActivity.this.quickRepliesRow) {
                  var23.setText(LocaleController.getString("VoipQuickReplies", 2131561086), false);
               }
            }
         } else if (var2 == DataSettingsActivity.this.proxySection2Row) {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
         } else {
            var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
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
                           var3 = new NotificationsCheckCell(this.mContext);
                           ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                        }
                     } else {
                        var3 = new TextInfoPrivacyCell(this.mContext);
                        ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                     }
                  } else {
                     var3 = new TextCheckCell(this.mContext);
                     ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new HeaderCell(this.mContext);
                  ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
               }
            } else {
               var3 = new TextSettingsCell(this.mContext);
               ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new ShadowSectionCell(this.mContext);
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         if (var1.getItemViewType() == 3) {
            TextCheckCell var2 = (TextCheckCell)var1.itemView;
            int var3 = var1.getAdapterPosition();
            if (var3 == DataSettingsActivity.this.enableCacheStreamRow) {
               var2.setChecked(SharedConfig.saveStreamMedia);
            } else if (var3 == DataSettingsActivity.this.enableStreamRow) {
               var2.setChecked(SharedConfig.streamMedia);
            } else if (var3 == DataSettingsActivity.this.enableAllStreamRow) {
               var2.setChecked(SharedConfig.streamAllVideo);
            } else if (var3 == DataSettingsActivity.this.enableMkvRow) {
               var2.setChecked(SharedConfig.streamMkv);
            } else if (var3 == DataSettingsActivity.this.autoplayGifsRow) {
               var2.setChecked(SharedConfig.autoplayGifs);
            } else if (var3 == DataSettingsActivity.this.autoplayVideoRow) {
               var2.setChecked(SharedConfig.autoplayVideo);
            }
         }

      }
   }
}
