package org.telegram.ui;

import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class LogoutActivity extends BaseFragment {
   private int addAccountRow;
   private int alternativeHeaderRow;
   private int alternativeSectionRow;
   private AnimatorSet animatorSet;
   private int cacheRow;
   private LogoutActivity.ListAdapter listAdapter;
   private RecyclerListView listView;
   private int logoutRow;
   private int logoutSectionRow;
   private int passcodeRow;
   private int phoneRow;
   private int rowCount;
   private int supportRow;

   public View createView(Context var1) {
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setTitle(LocaleController.getString("LogOutTitle", 2131559785));
      if (AndroidUtilities.isTablet()) {
         super.actionBar.setOccupyStatusBar(false);
      }

      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               LogoutActivity.this.finishFragment();
            }

         }
      });
      this.listAdapter = new LogoutActivity.ListAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      super.fragmentView.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      FrameLayout var2 = (FrameLayout)super.fragmentView;
      this.listView = new RecyclerListView(var1);
      this.listView.setVerticalScrollBarEnabled(false);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      var2.addView(this.listView, LayoutHelper.createFrame(-1, -1, 51));
      this.listView.setAdapter(this.listAdapter);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$LogoutActivity$m1rAFJ32lHZiphjBl2dyKZG7_Z0(this)));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      ThemeDescription var1 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextDetailSettingsCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var2 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var3 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var4 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var8 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var9 = this.listView;
      Paint var10 = Theme.dividerPaint;
      return new ThemeDescription[]{var1, var2, var3, var4, var5, var6, var7, var8, new ThemeDescription(var9, 0, new Class[]{View.class}, var10, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteRedText5"), new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText4"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{TextDetailSettingsCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$1$LogoutActivity(View var1, int var2, float var3, float var4) {
      int var5 = this.addAccountRow;
      byte var6 = 0;
      if (var2 == var5) {
         byte var8 = -1;
         var2 = var6;

         int var9;
         while(true) {
            var9 = var8;
            if (var2 >= 3) {
               break;
            }

            if (!UserConfig.getInstance(var2).isClientActivated()) {
               var9 = var2;
               break;
            }

            ++var2;
         }

         if (var9 >= 0) {
            this.presentFragment(new LoginActivity(var9));
         }
      } else if (var2 == this.passcodeRow) {
         this.presentFragment(new PasscodeActivity(0));
      } else if (var2 == this.cacheRow) {
         this.presentFragment(new CacheControlActivity());
      } else if (var2 == this.phoneRow) {
         this.presentFragment(new ChangePhoneHelpActivity());
      } else if (var2 == this.supportRow) {
         this.showDialog(AlertsCreator.createSupportAlert(this));
      } else if (var2 == this.logoutRow) {
         if (this.getParentActivity() == null) {
            return;
         }

         AlertDialog.Builder var7 = new AlertDialog.Builder(this.getParentActivity());
         var7.setMessage(LocaleController.getString("AreYouSureLogout", 2131558694));
         var7.setTitle(LocaleController.getString("AppName", 2131558635));
         var7.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LogoutActivity$j4TP2hjvfLt3Pmf_Vc0G6Kn6GSI(this));
         var7.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
         this.showDialog(var7.create());
      }

   }

   // $FF: synthetic method
   public void lambda$null$0$LogoutActivity(DialogInterface var1, int var2) {
      MessagesController.getInstance(super.currentAccount).performLogout(1);
   }

   protected void onDialogDismiss(Dialog var1) {
      DownloadController.getInstance(super.currentAccount).checkAutodownloadSettings();
   }

   public boolean onFragmentCreate() {
      super.onFragmentCreate();
      this.rowCount = 0;
      int var1 = this.rowCount++;
      this.alternativeHeaderRow = var1;
      if (UserConfig.getActivatedAccountsCount() < 3) {
         var1 = this.rowCount++;
         this.addAccountRow = var1;
      } else {
         this.addAccountRow = -1;
      }

      if (SharedConfig.passcodeHash.length() <= 0) {
         var1 = this.rowCount++;
         this.passcodeRow = var1;
      } else {
         this.passcodeRow = -1;
      }

      var1 = this.rowCount++;
      this.cacheRow = var1;
      var1 = this.rowCount++;
      this.phoneRow = var1;
      var1 = this.rowCount++;
      this.supportRow = var1;
      var1 = this.rowCount++;
      this.alternativeSectionRow = var1;
      var1 = this.rowCount++;
      this.logoutRow = var1;
      var1 = this.rowCount++;
      this.logoutSectionRow = var1;
      return true;
   }

   public void onResume() {
      super.onResume();
      LogoutActivity.ListAdapter var1 = this.listAdapter;
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
         return LogoutActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == LogoutActivity.this.alternativeHeaderRow) {
            return 0;
         } else if (var1 != LogoutActivity.this.addAccountRow && var1 != LogoutActivity.this.passcodeRow && var1 != LogoutActivity.this.cacheRow && var1 != LogoutActivity.this.phoneRow && var1 != LogoutActivity.this.supportRow) {
            if (var1 == LogoutActivity.this.alternativeSectionRow) {
               return 2;
            } else {
               return var1 == LogoutActivity.this.logoutRow ? 3 : 4;
            }
         } else {
            return 1;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getAdapterPosition();
         boolean var3;
         if (var2 != LogoutActivity.this.addAccountRow && var2 != LogoutActivity.this.passcodeRow && var2 != LogoutActivity.this.cacheRow && var2 != LogoutActivity.this.phoneRow && var2 != LogoutActivity.this.supportRow && var2 != LogoutActivity.this.logoutRow) {
            var3 = false;
         } else {
            var3 = true;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 != 3) {
                  if (var3 == 4) {
                     TextInfoPrivacyCell var4 = (TextInfoPrivacyCell)var1.itemView;
                     if (var2 == LogoutActivity.this.logoutSectionRow) {
                        var4.setText(LocaleController.getString("LogOutInfo", 2131559784));
                     }
                  }
               } else {
                  TextSettingsCell var5 = (TextSettingsCell)var1.itemView;
                  if (var2 == LogoutActivity.this.logoutRow) {
                     var5.setTextColor(Theme.getColor("windowBackgroundWhiteRedText5"));
                     var5.setText(LocaleController.getString("LogOutTitle", 2131559785), false);
                  }
               }
            } else {
               TextDetailSettingsCell var6 = (TextDetailSettingsCell)var1.itemView;
               if (var2 == LogoutActivity.this.addAccountRow) {
                  var6.setTextAndValueAndIcon(LocaleController.getString("AddAnotherAccount", 2131558562), LocaleController.getString("AddAnotherAccountInfo", 2131558563), 2131165272, true);
               } else if (var2 == LogoutActivity.this.passcodeRow) {
                  var6.setTextAndValueAndIcon(LocaleController.getString("SetPasscode", 2131560734), LocaleController.getString("SetPasscodeInfo", 2131560735), 2131165590, true);
               } else if (var2 == LogoutActivity.this.cacheRow) {
                  var6.setTextAndValueAndIcon(LocaleController.getString("ClearCache", 2131559103), LocaleController.getString("ClearCacheInfo", 2131559105), 2131165575, true);
               } else if (var2 == LogoutActivity.this.phoneRow) {
                  var6.setTextAndValueAndIcon(LocaleController.getString("ChangePhoneNumber", 2131558913), LocaleController.getString("ChangePhoneNumberInfo", 2131558914), 2131165587, true);
               } else if (var2 == LogoutActivity.this.supportRow) {
                  var6.setTextAndValueAndIcon(LocaleController.getString("ContactSupport", 2131559147), LocaleController.getString("ContactSupportInfo", 2131559148), 2131165600, false);
               }
            }
         } else {
            HeaderCell var7 = (HeaderCell)var1.itemView;
            if (var2 == LogoutActivity.this.alternativeHeaderRow) {
               var7.setText(LocaleController.getString("AlternativeOptions", 2131558610));
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         if (var2 != 0) {
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 != 3) {
                     var3 = new TextInfoPrivacyCell(this.mContext);
                     ((View)var3).setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
                  } else {
                     var3 = new TextSettingsCell(this.mContext);
                     ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
                  }
               } else {
                  var3 = new ShadowSectionCell(this.mContext);
               }
            } else {
               var3 = new TextDetailSettingsCell(this.mContext);
               ((TextDetailSettingsCell)var3).setMultilineDetail(true);
               ((FrameLayout)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            }
         } else {
            var3 = new HeaderCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         ((View)var3).setLayoutParams(new RecyclerView.LayoutParams(-1, -2));
         return new RecyclerListView.Holder((View)var3);
      }
   }
}
