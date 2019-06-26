package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextColorCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;

public class NotificationsCustomSettingsActivity extends BaseFragment {
   private static final int search_button = 0;
   private NotificationsCustomSettingsActivity.ListAdapter adapter;
   private int alertRow;
   private int alertSection2Row;
   private AnimatorSet animatorSet;
   private int currentType;
   private EmptyTextProgressView emptyView;
   private ArrayList exceptions;
   private int exceptionsAddRow;
   private int exceptionsEndRow;
   private int exceptionsSection2Row;
   private int exceptionsStartRow;
   private int groupSection2Row;
   private RecyclerListView listView;
   private int messageLedRow;
   private int messagePopupNotificationRow;
   private int messagePriorityRow;
   private int messageSectionRow;
   private int messageSoundRow;
   private int messageVibrateRow;
   private int previewRow;
   private int rowCount;
   private NotificationsCustomSettingsActivity.SearchAdapter searchListViewAdapter;
   private boolean searchWas;
   private boolean searching;

   public NotificationsCustomSettingsActivity(int var1, ArrayList var2) {
      this(var1, var2, false);
   }

   public NotificationsCustomSettingsActivity(int var1, ArrayList var2, boolean var3) {
      this.rowCount = 0;
      this.currentType = var1;
      this.exceptions = var2;
      if (var3) {
         this.loadExceptions();
      }

   }

   // $FF: synthetic method
   static int access$1000(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1100(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1200(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1300(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$1600(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2100(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2400(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$2900(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3000(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   // $FF: synthetic method
   static int access$3200(NotificationsCustomSettingsActivity var0) {
      return var0.currentAccount;
   }

   private void checkRowsEnabled() {
      if (this.exceptions.isEmpty()) {
         int var1 = this.listView.getChildCount();
         ArrayList var2 = new ArrayList();
         boolean var3 = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.currentType);

         for(int var4 = 0; var4 < var1; ++var4) {
            View var5 = this.listView.getChildAt(var4);
            RecyclerListView.Holder var8 = (RecyclerListView.Holder)this.listView.getChildViewHolder(var5);
            int var6 = var8.getItemViewType();
            if (var6 != 0) {
               if (var6 != 1) {
                  if (var6 != 3) {
                     if (var6 == 5) {
                        ((TextSettingsCell)var8.itemView).setEnabled(var3, var2);
                     }
                  } else {
                     ((TextColorCell)var8.itemView).setEnabled(var3, var2);
                  }
               } else {
                  ((TextCheckCell)var8.itemView).setEnabled(var3, var2);
               }
            } else {
               HeaderCell var7 = (HeaderCell)var8.itemView;
               if (var8.getAdapterPosition() == this.messageSectionRow) {
                  var7.setEnabled(var3, var2);
               }
            }
         }

         if (!var2.isEmpty()) {
            AnimatorSet var9 = this.animatorSet;
            if (var9 != null) {
               var9.cancel();
            }

            this.animatorSet = new AnimatorSet();
            this.animatorSet.playTogether(var2);
            this.animatorSet.addListener(new AnimatorListenerAdapter() {
               public void onAnimationEnd(Animator var1) {
                  if (var1.equals(NotificationsCustomSettingsActivity.this.animatorSet)) {
                     NotificationsCustomSettingsActivity.this.animatorSet = null;
                  }

               }
            });
            this.animatorSet.setDuration(150L);
            this.animatorSet.start();
         }

      }
   }

   private void loadExceptions() {
      MessagesStorage.getInstance(super.currentAccount).getStorageQueue().postRunnable(new _$$Lambda$NotificationsCustomSettingsActivity$1DHOVp0GIYy95W4ah22F_PhCl_A(this));
   }

   private void updateRows() {
      this.rowCount = 0;
      int var1 = this.currentType;
      if (var1 != -1) {
         int var2 = this.rowCount++;
         this.alertRow = var2;
         var2 = this.rowCount++;
         this.alertSection2Row = var2;
         var2 = this.rowCount++;
         this.messageSectionRow = var2;
         var2 = this.rowCount++;
         this.previewRow = var2;
         var2 = this.rowCount++;
         this.messageLedRow = var2;
         var2 = this.rowCount++;
         this.messageVibrateRow = var2;
         if (var1 == 2) {
            this.messagePopupNotificationRow = -1;
         } else {
            var1 = this.rowCount++;
            this.messagePopupNotificationRow = var1;
         }

         var1 = this.rowCount++;
         this.messageSoundRow = var1;
         if (VERSION.SDK_INT >= 21) {
            var1 = this.rowCount++;
            this.messagePriorityRow = var1;
         } else {
            this.messagePriorityRow = -1;
         }

         var1 = this.rowCount++;
         this.groupSection2Row = var1;
         var1 = this.rowCount++;
         this.exceptionsAddRow = var1;
      } else {
         this.alertRow = -1;
         this.alertSection2Row = -1;
         this.messageSectionRow = -1;
         this.previewRow = -1;
         this.messageLedRow = -1;
         this.messageVibrateRow = -1;
         this.messagePopupNotificationRow = -1;
         this.messageSoundRow = -1;
         this.messagePriorityRow = -1;
         this.groupSection2Row = -1;
         this.exceptionsAddRow = -1;
      }

      ArrayList var3 = this.exceptions;
      if (var3 != null && !var3.isEmpty()) {
         var1 = this.rowCount;
         this.exceptionsStartRow = var1;
         this.rowCount = var1 + this.exceptions.size();
         this.exceptionsEndRow = this.rowCount;
      } else {
         this.exceptionsStartRow = -1;
         this.exceptionsEndRow = -1;
      }

      if (this.currentType == -1) {
         var3 = this.exceptions;
         if (var3 == null || var3.isEmpty()) {
            this.exceptionsSection2Row = -1;
            return;
         }
      }

      var1 = this.rowCount++;
      this.exceptionsSection2Row = var1;
   }

   public View createView(Context var1) {
      this.searching = false;
      this.searchWas = false;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      if (this.currentType == -1) {
         super.actionBar.setTitle(LocaleController.getString("NotificationsExceptions", 2131560064));
      } else {
         super.actionBar.setTitle(LocaleController.getString("Notifications", 2131560055));
      }

      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               NotificationsCustomSettingsActivity.this.finishFragment();
            }

         }
      });
      ArrayList var2 = this.exceptions;
      if (var2 != null && !var2.isEmpty()) {
         super.actionBar.createMenu().addItem(0, 2131165419).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItem.ActionBarMenuItemSearchListener() {
            public void onSearchCollapse() {
               NotificationsCustomSettingsActivity.this.searchListViewAdapter.searchDialogs((String)null);
               NotificationsCustomSettingsActivity.this.searching = false;
               NotificationsCustomSettingsActivity.this.searchWas = false;
               NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoExceptions", 2131559923));
               NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.adapter);
               NotificationsCustomSettingsActivity.this.adapter.notifyDataSetChanged();
               NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(true);
               NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(false);
               NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(false);
            }

            public void onSearchExpand() {
               NotificationsCustomSettingsActivity.this.searching = true;
               NotificationsCustomSettingsActivity.this.emptyView.setShowAtCenter(true);
            }

            public void onTextChanged(EditText var1) {
               if (NotificationsCustomSettingsActivity.this.searchListViewAdapter != null) {
                  String var2 = var1.getText().toString();
                  if (var2.length() != 0) {
                     NotificationsCustomSettingsActivity.this.searchWas = true;
                     if (NotificationsCustomSettingsActivity.this.listView != null) {
                        NotificationsCustomSettingsActivity.this.emptyView.setText(LocaleController.getString("NoResult", 2131559943));
                        NotificationsCustomSettingsActivity.this.listView.setAdapter(NotificationsCustomSettingsActivity.this.searchListViewAdapter);
                        NotificationsCustomSettingsActivity.this.searchListViewAdapter.notifyDataSetChanged();
                        NotificationsCustomSettingsActivity.this.listView.setFastScrollVisible(false);
                        NotificationsCustomSettingsActivity.this.listView.setVerticalScrollBarEnabled(true);
                     }
                  }

                  NotificationsCustomSettingsActivity.this.searchListViewAdapter.searchDialogs(var2);
               }
            }
         }).setSearchFieldHint(LocaleController.getString("Search", 2131560640));
      }

      this.searchListViewAdapter = new NotificationsCustomSettingsActivity.SearchAdapter(var1);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var4 = (FrameLayout)super.fragmentView;
      var4.setBackgroundColor(Theme.getColor("windowBackgroundGray"));
      this.emptyView = new EmptyTextProgressView(var1);
      this.emptyView.setTextSize(18);
      this.emptyView.setText(LocaleController.getString("NoExceptions", 2131559923));
      this.emptyView.showTextView();
      var4.addView(this.emptyView, LayoutHelper.createFrame(-1, -1.0F));
      this.listView = new RecyclerListView(var1);
      this.listView.setEmptyView(this.emptyView);
      this.listView.setLayoutManager(new LinearLayoutManager(var1, 1, false));
      this.listView.setVerticalScrollBarEnabled(false);
      var4.addView(this.listView, LayoutHelper.createFrame(-1, -1.0F));
      RecyclerListView var5 = this.listView;
      NotificationsCustomSettingsActivity.ListAdapter var3 = new NotificationsCustomSettingsActivity.ListAdapter(var1);
      this.adapter = var3;
      var5.setAdapter(var3);
      this.listView.setOnItemClickListener((RecyclerListView.OnItemClickListenerExtended)(new _$$Lambda$NotificationsCustomSettingsActivity$6X_KPwufVk5Y33wBeKoVyvVbszw(this)));
      this.listView.setOnScrollListener(new RecyclerView.OnScrollListener() {
         public void onScrollStateChanged(RecyclerView var1, int var2) {
            if (var2 == 1 && NotificationsCustomSettingsActivity.this.searching && NotificationsCustomSettingsActivity.this.searchWas) {
               AndroidUtilities.hideKeyboard(NotificationsCustomSettingsActivity.this.getParentActivity().getCurrentFocus());
            }

         }

         public void onScrolled(RecyclerView var1, int var2, int var3) {
            super.onScrolled(var1, var2, var3);
         }
      });
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      _$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc var1 = new _$$Lambda$NotificationsCustomSettingsActivity$r6_70T0AtxrwXcm6fzXqKDLMaSc(this);
      ThemeDescription var2 = new ThemeDescription(this.listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{HeaderCell.class, TextCheckCell.class, TextColorCell.class, TextSettingsCell.class, UserCell.class, NotificationsCheckCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite");
      ThemeDescription var3 = new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGray");
      ThemeDescription var4 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var5 = new ThemeDescription(this.listView, ThemeDescription.FLAG_LISTGLOWCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault");
      ThemeDescription var6 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon");
      ThemeDescription var7 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle");
      ThemeDescription var8 = new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector");
      ThemeDescription var9 = new ThemeDescription(this.listView, ThemeDescription.FLAG_SELECTOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "listSelectorSDK21");
      RecyclerListView var10 = this.listView;
      Paint var11 = Theme.dividerPaint;
      ThemeDescription var12 = new ThemeDescription(var10, 0, new Class[]{View.class}, var11, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "divider");
      ThemeDescription var13 = new ThemeDescription(this.listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueHeader");
      ThemeDescription var14 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var23 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2");
      ThemeDescription var15 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack");
      ThemeDescription var16 = new ThemeDescription(this.listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked");
      ThemeDescription var17 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayIcon");
      ThemeDescription var18 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"nameTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText");
      ThemeDescription var19 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteGrayText");
      ThemeDescription var24 = new ThemeDescription(this.listView, 0, new Class[]{UserCell.class}, new String[]{"statusOnlineColor"}, (Paint[])null, (Drawable[])null, var1, "windowBackgroundWhiteBlueText");
      RecyclerListView var20 = this.listView;
      Drawable var21 = Theme.avatar_broadcastDrawable;
      Drawable var22 = Theme.avatar_savedDrawable;
      return new ThemeDescription[]{var2, var3, var4, var5, var6, var7, var8, var9, var12, var13, var14, var23, var15, var16, var17, var18, var19, var24, new ThemeDescription(var20, 0, new Class[]{UserCell.class}, (Paint)null, new Drawable[]{var21, var22}, (ThemeDescription.ThemeDescriptionDelegate)null, "avatar_text"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundRed"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundOrange"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundViolet"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundGreen"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundCyan"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundBlue"), new ThemeDescription((View)null, 0, (Class[])null, (Paint)null, (Drawable[])null, var1, "avatar_backgroundPink"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteGrayText2"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrack"), new ThemeDescription(this.listView, 0, new Class[]{NotificationsCheckCell.class}, new String[]{"checkBox"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "switchTrackChecked"), new ThemeDescription(this.listView, 0, new Class[]{TextColorCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlackText"), new ThemeDescription(this.listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteValueText"), new ThemeDescription(this.listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ShadowSectionCell.class}, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundGrayShadow"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"textView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueButton"), new ThemeDescription(this.listView, ThemeDescription.FLAG_CHECKTAG, new Class[]{TextCell.class}, new String[]{"imageView"}, (Paint[])null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhiteBlueIcon")};
   }

   // $FF: synthetic method
   public void lambda$createView$8$NotificationsCustomSettingsActivity(View var1, int var2, float var3, float var4) {
      if (this.getParentActivity() != null) {
         int var5;
         if (this.listView.getAdapter() != this.searchListViewAdapter && (var2 < this.exceptionsStartRow || var2 >= this.exceptionsEndRow)) {
            boolean var6;
            boolean var8;
            label200: {
               var5 = this.exceptionsAddRow;
               var6 = false;
               if (var2 == var5) {
                  Bundle var24 = new Bundle();
                  var24.putBoolean("onlySelect", true);
                  var24.putBoolean("checkCanWrite", false);
                  var2 = this.currentType;
                  if (var2 == 0) {
                     var24.putInt("dialogsType", 6);
                  } else if (var2 == 2) {
                     var24.putInt("dialogsType", 5);
                  } else {
                     var24.putInt("dialogsType", 4);
                  }

                  DialogsActivity var25 = new DialogsActivity(var24);
                  var25.setDelegate(new _$$Lambda$NotificationsCustomSettingsActivity$b2R0H8L52T9zKW4WbPYtRj3Tixo(this));
                  this.presentFragment(var25);
               } else {
                  if (var2 == this.alertRow) {
                     var8 = NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.currentType);
                     NotificationsCheckCell var32 = (NotificationsCheckCell)var1;
                     RecyclerView.ViewHolder var31 = this.listView.findViewHolderForAdapterPosition(var2);
                     if (!var8) {
                        NotificationsController.getInstance(super.currentAccount).setGlobalNotificationsEnabled(this.currentType, 0);
                        var32.setChecked(var8 ^ true);
                        if (var31 != null) {
                           this.adapter.onBindViewHolder(var31, var2);
                        }

                        this.checkRowsEnabled();
                     } else {
                        AlertsCreator.showCustomNotificationsDialog(this, 0L, this.currentType, this.exceptions, super.currentAccount, new _$$Lambda$NotificationsCustomSettingsActivity$ZmFDQGaW1AFFelUamuZaIwn_BvM(this, var32, var31, var2));
                     }
                     break label200;
                  }

                  SharedPreferences var26;
                  if (var2 == this.previewRow) {
                     if (!var1.isEnabled()) {
                        return;
                     }

                     var26 = MessagesController.getNotificationsSettings(super.currentAccount);
                     Editor var29 = var26.edit();
                     var2 = this.currentType;
                     if (var2 == 1) {
                        var8 = var26.getBoolean("EnablePreviewAll", true);
                        var29.putBoolean("EnablePreviewAll", var8 ^ true);
                     } else if (var2 == 0) {
                        var8 = var26.getBoolean("EnablePreviewGroup", true);
                        var29.putBoolean("EnablePreviewGroup", var8 ^ true);
                     } else {
                        var8 = var26.getBoolean("EnablePreviewChannel", true);
                        var29.putBoolean("EnablePreviewChannel", var8 ^ true);
                     }

                     var29.commit();
                     NotificationsController.getInstance(super.currentAccount).updateServerNotificationsSettings(this.currentType);
                     break label200;
                  }

                  String var27;
                  if (var2 == this.messageSoundRow) {
                     label217: {
                        if (!var1.isEnabled()) {
                           return;
                        }

                        Exception var10000;
                        label218: {
                           Intent var10;
                           Uri var11;
                           boolean var10001;
                           try {
                              var26 = MessagesController.getNotificationsSettings(super.currentAccount);
                              var10 = new Intent("android.intent.action.RINGTONE_PICKER");
                              var10.putExtra("android.intent.extra.ringtone.TYPE", 2);
                              var10.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                              var10.putExtra("android.intent.extra.ringtone.SHOW_SILENT", true);
                              var10.putExtra("android.intent.extra.ringtone.DEFAULT_URI", RingtoneManager.getDefaultUri(2));
                              var11 = System.DEFAULT_NOTIFICATION_URI;
                           } catch (Exception var19) {
                              var10000 = var19;
                              var10001 = false;
                              break label218;
                           }

                           String var9;
                           if (var11 != null) {
                              try {
                                 var9 = var11.getPath();
                              } catch (Exception var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label218;
                              }
                           } else {
                              var9 = null;
                           }

                           label219: {
                              try {
                                 if (this.currentType == 1) {
                                    var27 = var26.getString("GlobalSoundPath", var9);
                                    break label219;
                                 }
                              } catch (Exception var17) {
                                 var10000 = var17;
                                 var10001 = false;
                                 break label218;
                              }

                              try {
                                 if (this.currentType == 0) {
                                    var27 = var26.getString("GroupSoundPath", var9);
                                    break label219;
                                 }
                              } catch (Exception var16) {
                                 var10000 = var16;
                                 var10001 = false;
                                 break label218;
                              }

                              try {
                                 var27 = var26.getString("ChannelSoundPath", var9);
                              } catch (Exception var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label218;
                              }
                           }

                           Uri var28;
                           label168: {
                              label226: {
                                 if (var27 != null) {
                                    label224: {
                                       try {
                                          if (var27.equals("NoSound")) {
                                             break label224;
                                          }

                                          if (var27.equals(var9)) {
                                             break label226;
                                          }
                                       } catch (Exception var14) {
                                          var10000 = var14;
                                          var10001 = false;
                                          break label218;
                                       }

                                       try {
                                          var28 = Uri.parse(var27);
                                          break label168;
                                       } catch (Exception var13) {
                                          var10000 = var13;
                                          var10001 = false;
                                          break label218;
                                       }
                                    }
                                 }

                                 var28 = null;
                                 break label168;
                              }

                              var28 = var11;
                           }

                           try {
                              var10.putExtra("android.intent.extra.ringtone.EXISTING_URI", var28);
                              this.startActivityForResult(var10, var2);
                              break label217;
                           } catch (Exception var12) {
                              var10000 = var12;
                              var10001 = false;
                           }
                        }

                        Exception var30 = var10000;
                        FileLog.e((Throwable)var30);
                     }
                  } else if (var2 == this.messageLedRow) {
                     if (!var1.isEnabled()) {
                        return;
                     }

                     this.showDialog(AlertsCreator.createColorSelectDialog(this.getParentActivity(), 0L, this.currentType, new _$$Lambda$NotificationsCustomSettingsActivity$4rEUWVGkx4_tAGGYZA5DW_owM_s(this, var2)));
                  } else if (var2 == this.messagePopupNotificationRow) {
                     if (!var1.isEnabled()) {
                        return;
                     }

                     this.showDialog(AlertsCreator.createPopupSelectDialog(this.getParentActivity(), this.currentType, new _$$Lambda$NotificationsCustomSettingsActivity$2Ybp2U7lQ3YxdvjXmNbMJQtkwOQ(this, var2)));
                  } else if (var2 == this.messageVibrateRow) {
                     if (!var1.isEnabled()) {
                        return;
                     }

                     var5 = this.currentType;
                     if (var5 == 1) {
                        var27 = "vibrate_messages";
                     } else if (var5 == 0) {
                        var27 = "vibrate_group";
                     } else {
                        var27 = "vibrate_channel";
                     }

                     this.showDialog(AlertsCreator.createVibrationSelectDialog(this.getParentActivity(), 0L, var27, new _$$Lambda$NotificationsCustomSettingsActivity$5JQeM2VZAZohz71seJkzo9Axyl8(this, var2)));
                  } else if (var2 == this.messagePriorityRow) {
                     if (!var1.isEnabled()) {
                        return;
                     }

                     this.showDialog(AlertsCreator.createPrioritySelectDialog(this.getParentActivity(), 0L, this.currentType, new _$$Lambda$NotificationsCustomSettingsActivity$tal7L4g8KTgKJ9Uilx8RMZ5_g8Y(this, var2)));
                  }
               }

               var8 = false;
            }

            if (var1 instanceof TextCheckCell) {
               TextCheckCell var22 = (TextCheckCell)var1;
               if (!var8) {
                  var6 = true;
               }

               var22.setChecked(var6);
            }

         } else {
            RecyclerView.Adapter var20 = this.listView.getAdapter();
            NotificationsCustomSettingsActivity.SearchAdapter var7 = this.searchListViewAdapter;
            ArrayList var21;
            if (var20 == var7) {
               var21 = var7.searchResult;
               var5 = var2;
            } else {
               var21 = this.exceptions;
               var5 = var2 - this.exceptionsStartRow;
            }

            if (var5 >= 0 && var5 < var21.size()) {
               NotificationsSettingsActivity.NotificationException var23 = (NotificationsSettingsActivity.NotificationException)var21.get(var5);
               AlertsCreator.showCustomNotificationsDialog(this, var23.did, -1, (ArrayList)null, super.currentAccount, (MessagesStorage.IntCallback)null, new _$$Lambda$NotificationsCustomSettingsActivity$dRvEr5S0zYnz2y_9i0nz2iSedtM(this, var21, var23, var2));
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$getThemeDescriptions$11$NotificationsCustomSettingsActivity() {
      RecyclerListView var1 = this.listView;
      if (var1 != null) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = this.listView.getChildAt(var3);
            if (var4 instanceof UserCell) {
               ((UserCell)var4).update(0);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$loadExceptions$10$NotificationsCustomSettingsActivity() {
      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      LongSparseArray var4 = new LongSparseArray();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      ArrayList var8 = new ArrayList();
      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();
      int var11 = UserConfig.getInstance(super.currentAccount).clientUserId;
      SharedPreferences var12 = MessagesController.getNotificationsSettings(super.currentAccount);
      Map var13 = var12.getAll();
      Iterator var14 = var13.entrySet().iterator();

      while(true) {
         NotificationsSettingsActivity.NotificationException var21;
         label218:
         while(true) {
            while(true) {
               Entry var15;
               String var17;
               long var19;
               int var23;
               do {
                  do {
                     String var16;
                     do {
                        if (!var14.hasNext()) {
                           byte var48 = 0;
                           if (var4.size() != 0) {
                              label175: {
                                 Exception var33;
                                 label225: {
                                    Exception var10000;
                                    boolean var10001;
                                    boolean var25;
                                    label173: {
                                       label226: {
                                          try {
                                             var25 = var7.isEmpty();
                                          } catch (Exception var32) {
                                             var10000 = var32;
                                             var10001 = false;
                                             break label226;
                                          }

                                          if (!var25) {
                                             try {
                                                MessagesStorage.getInstance(super.currentAccount).getEncryptedChatsInternal(TextUtils.join(",", var7), var10, var5);
                                             } catch (Exception var31) {
                                                var10000 = var31;
                                                var10001 = false;
                                                break label226;
                                             }
                                          }

                                          try {
                                             var25 = var5.isEmpty();
                                             break label173;
                                          } catch (Exception var30) {
                                             var10000 = var30;
                                             var10001 = false;
                                          }
                                       }

                                       var33 = var10000;
                                       break label225;
                                    }

                                    String var36;
                                    MessagesStorage var38;
                                    label160: {
                                       label159: {
                                          if (!var25) {
                                             String var34;
                                             try {
                                                var38 = MessagesStorage.getInstance(super.currentAccount);
                                                var34 = TextUtils.join(",", var5);
                                             } catch (Exception var27) {
                                                var33 = var27;
                                                break label225;
                                             }

                                             try {
                                                var38.getUsersInternal(var34, var8);
                                             } catch (Exception var29) {
                                                var10000 = var29;
                                                var10001 = false;
                                                break label159;
                                             }
                                          }

                                          try {
                                             if (var6.isEmpty()) {
                                                break label175;
                                             }

                                             var38 = MessagesStorage.getInstance(super.currentAccount);
                                             var36 = TextUtils.join(",", var6);
                                             break label160;
                                          } catch (Exception var28) {
                                             var10000 = var28;
                                             var10001 = false;
                                          }
                                       }

                                       var33 = var10000;
                                       break label225;
                                    }

                                    try {
                                       var38.getChatsInternal(var36, var9);
                                       break label175;
                                    } catch (Exception var26) {
                                       var33 = var26;
                                    }
                                 }

                                 FileLog.e((Throwable)var33);
                              }

                              ArrayList var39 = var9;
                              var6 = var8;
                              var23 = var9.size();

                              for(var11 = 0; var11 < var23; ++var11) {
                                 TLRPC.Chat var37 = (TLRPC.Chat)var39.get(var11);
                                 if (!var37.left && !var37.kicked && var37.migrated_to == null) {
                                    NotificationsSettingsActivity.NotificationException var35 = (NotificationsSettingsActivity.NotificationException)var4.get((long)(-var37.id));
                                    var4.remove((long)(-var37.id));
                                    if (var35 != null) {
                                       if (ChatObject.isChannel(var37) && !var37.megagroup) {
                                          var3.add(var35);
                                       } else {
                                          var2.add(var35);
                                       }
                                    }
                                 }
                              }

                              var23 = var8.size();

                              for(var11 = 0; var11 < var23; ++var11) {
                                 TLRPC.User var41 = (TLRPC.User)var6.get(var11);
                                 if (!var41.deleted) {
                                    var4.remove((long)var41.id);
                                 }
                              }

                              var23 = var10.size();

                              for(var11 = 0; var11 < var23; ++var11) {
                                 var4.remove((long)((TLRPC.EncryptedChat)var10.get(var11)).id << 32);
                              }

                              var23 = var4.size();

                              for(var11 = var48; var11 < var23; ++var11) {
                                 if ((int)var4.keyAt(var11) < 0) {
                                    var2.remove(var4.valueAt(var11));
                                    var3.remove(var4.valueAt(var11));
                                 } else {
                                    var1.remove(var4.valueAt(var11));
                                 }
                              }
                           }

                           AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsCustomSettingsActivity$KpcytPX_h4eA3kA__wtdyBIK9_w(this, var8, var9, var10, var1, var2, var3));
                           return;
                        }

                        var15 = (Entry)var14.next();
                        var16 = (String)var15.getKey();
                     } while(!var16.startsWith("notify2_"));

                     var17 = var16.replace("notify2_", "");
                     Long var18 = Utilities.parseLong(var17);
                     var19 = var18;
                  } while(var19 == 0L);
               } while(var19 == (long)var11);

               var21 = new NotificationsSettingsActivity.NotificationException();
               var21.did = var19;
               StringBuilder var22 = new StringBuilder();
               var22.append("custom_");
               var22.append(var19);
               var21.hasCustom = var12.getBoolean(var22.toString(), false);
               var21.notify = (Integer)var15.getValue();
               if (var21.notify != 0) {
                  StringBuilder var40 = new StringBuilder();
                  var40.append("notifyuntil_");
                  var40.append(var17);
                  Integer var43 = (Integer)var13.get(var40.toString());
                  if (var43 != null) {
                     var21.muteUntil = var43;
                  }
               }

               var23 = (int)var19;
               int var24 = (int)(var19 << 32);
               if (var23 != 0) {
                  if (var23 > 0) {
                     TLRPC.User var44 = MessagesController.getInstance(super.currentAccount).getUser(var23);
                     if (var44 == null) {
                        var5.add(var23);
                        var4.put(var19, var21);
                        break label218;
                     }

                     if (!var44.deleted) {
                        break label218;
                     }
                  } else {
                     MessagesController var45 = MessagesController.getInstance(super.currentAccount);
                     var24 = -var23;
                     TLRPC.Chat var46 = var45.getChat(var24);
                     if (var46 == null) {
                        var6.add(var24);
                        var4.put(var19, var21);
                     } else if (!var46.left && !var46.kicked && var46.migrated_to == null) {
                        if (ChatObject.isChannel(var46) && !var46.megagroup) {
                           var3.add(var21);
                           continue;
                        }

                        var2.add(var21);
                        continue;
                     }
                  }

                  var6 = var6;
                  var2 = var2;
               } else if (var24 != 0) {
                  TLRPC.EncryptedChat var47 = MessagesController.getInstance(super.currentAccount).getEncryptedChat(var24);
                  if (var47 == null) {
                     var7.add(var24);
                     var4.put(var19, var21);
                  } else {
                     TLRPC.User var42 = MessagesController.getInstance(super.currentAccount).getUser(var47.user_id);
                     if (var42 == null) {
                        var5.add(var47.user_id);
                        var4.put((long)var47.user_id, var21);
                     } else if (var42.deleted) {
                        continue;
                     }
                  }

                  var1.add(var21);
               }
            }
         }

         var1.add(var21);
      }
   }

   // $FF: synthetic method
   public void lambda$null$0$NotificationsCustomSettingsActivity(ArrayList var1, NotificationsSettingsActivity.NotificationException var2, int var3, int var4) {
      if (var4 == 0) {
         ArrayList var5 = this.exceptions;
         if (var1 != var5) {
            var4 = var5.indexOf(var2);
            if (var4 >= 0) {
               this.exceptions.remove(var4);
            }
         }

         var1.remove(var2);
         if (this.exceptionsAddRow != -1 && var1.isEmpty() && var1 == this.exceptions) {
            this.listView.getAdapter().notifyItemChanged(this.exceptionsAddRow);
         }

         this.listView.getAdapter().notifyItemRemoved(var3);
         this.updateRows();
         this.checkRowsEnabled();
      } else {
         SharedPreferences var6 = MessagesController.getNotificationsSettings(super.currentAccount);
         StringBuilder var7 = new StringBuilder();
         var7.append("custom_");
         var7.append(var2.did);
         var2.hasCustom = var6.getBoolean(var7.toString(), false);
         var7 = new StringBuilder();
         var7.append("notify2_");
         var7.append(var2.did);
         var2.notify = var6.getInt(var7.toString(), 0);
         if (var2.notify != 0) {
            var7 = new StringBuilder();
            var7.append("notifyuntil_");
            var7.append(var2.did);
            var4 = var6.getInt(var7.toString(), -1);
            if (var4 != -1) {
               var2.muteUntil = var4;
            }
         }

         this.listView.getAdapter().notifyItemChanged(var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$1$NotificationsCustomSettingsActivity(NotificationsSettingsActivity.NotificationException var1) {
      this.exceptions.add(0, var1);
      this.updateRows();
      this.adapter.notifyDataSetChanged();
   }

   // $FF: synthetic method
   public void lambda$null$2$NotificationsCustomSettingsActivity(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      Bundle var5 = new Bundle();
      var5.putLong("dialog_id", (Long)var2.get(0));
      var5.putBoolean("exception", true);
      ProfileNotificationsActivity var6 = new ProfileNotificationsActivity(var5);
      var6.setDelegate(new _$$Lambda$NotificationsCustomSettingsActivity$aAVZUf3lzgt6V9lFSmZZFnxR_Ws(this));
      this.presentFragment(var6, true);
   }

   // $FF: synthetic method
   public void lambda$null$3$NotificationsCustomSettingsActivity(NotificationsCheckCell var1, RecyclerView.ViewHolder var2, int var3, int var4) {
      SharedPreferences var5 = MessagesController.getNotificationsSettings(super.currentAccount);
      var4 = this.currentType;
      byte var6 = 0;
      if (var4 == 1) {
         var4 = var5.getInt("EnableAll2", 0);
      } else if (var4 == 0) {
         var4 = var5.getInt("EnableGroup2", 0);
      } else {
         var4 = var5.getInt("EnableChannel2", 0);
      }

      int var7 = ConnectionsManager.getInstance(super.currentAccount).getCurrentTime();
      byte var8;
      if (var4 < var7) {
         var8 = var6;
      } else if (var4 - 31536000 >= var7) {
         var8 = var6;
      } else {
         var8 = 2;
      }

      var1.setChecked(NotificationsController.getInstance(super.currentAccount).isGlobalNotificationsEnabled(this.currentType), var8);
      if (var2 != null) {
         this.adapter.onBindViewHolder(var2, var3);
      }

      this.checkRowsEnabled();
   }

   // $FF: synthetic method
   public void lambda$null$4$NotificationsCustomSettingsActivity(int var1) {
      RecyclerView.ViewHolder var2 = this.listView.findViewHolderForAdapterPosition(var1);
      if (var2 != null) {
         this.adapter.onBindViewHolder(var2, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$5$NotificationsCustomSettingsActivity(int var1) {
      RecyclerView.ViewHolder var2 = this.listView.findViewHolderForAdapterPosition(var1);
      if (var2 != null) {
         this.adapter.onBindViewHolder(var2, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$6$NotificationsCustomSettingsActivity(int var1) {
      RecyclerView.ViewHolder var2 = this.listView.findViewHolderForAdapterPosition(var1);
      if (var2 != null) {
         this.adapter.onBindViewHolder(var2, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$NotificationsCustomSettingsActivity(int var1) {
      RecyclerView.ViewHolder var2 = this.listView.findViewHolderForAdapterPosition(var1);
      if (var2 != null) {
         this.adapter.onBindViewHolder(var2, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$NotificationsCustomSettingsActivity(ArrayList var1, ArrayList var2, ArrayList var3, ArrayList var4, ArrayList var5, ArrayList var6) {
      MessagesController.getInstance(super.currentAccount).putUsers(var1, true);
      MessagesController.getInstance(super.currentAccount).putChats(var2, true);
      MessagesController.getInstance(super.currentAccount).putEncryptedChats(var3, true);
      int var7 = this.currentType;
      if (var7 == 1) {
         this.exceptions = var4;
      } else if (var7 == 0) {
         this.exceptions = var5;
      } else {
         this.exceptions = var6;
      }

      this.updateRows();
      this.adapter.notifyDataSetChanged();
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
      if (var2 == -1) {
         Uri var4 = (Uri)var3.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
         Editor var5 = null;
         String var7 = var5;
         if (var4 != null) {
            Ringtone var6 = RingtoneManager.getRingtone(this.getParentActivity(), var4);
            var7 = var5;
            if (var6 != null) {
               if (var4.equals(System.DEFAULT_NOTIFICATION_URI)) {
                  var7 = LocaleController.getString("SoundDefault", 2131560801);
               } else {
                  var7 = var6.getTitle(this.getParentActivity());
               }

               var6.stop();
            }
         }

         var5 = MessagesController.getNotificationsSettings(super.currentAccount).edit();
         var2 = this.currentType;
         if (var2 == 1) {
            if (var7 != null && var4 != null) {
               var5.putString("GlobalSound", var7);
               var5.putString("GlobalSoundPath", var4.toString());
            } else {
               var5.putString("GlobalSound", "NoSound");
               var5.putString("GlobalSoundPath", "NoSound");
            }
         } else if (var2 == 0) {
            if (var7 != null && var4 != null) {
               var5.putString("GroupSound", var7);
               var5.putString("GroupSoundPath", var4.toString());
            } else {
               var5.putString("GroupSound", "NoSound");
               var5.putString("GroupSoundPath", "NoSound");
            }
         } else if (var2 == 2) {
            if (var7 != null && var4 != null) {
               var5.putString("ChannelSound", var7);
               var5.putString("ChannelSoundPath", var4.toString());
            } else {
               var5.putString("ChannelSound", "NoSound");
               var5.putString("ChannelSoundPath", "NoSound");
            }
         }

         var5.commit();
         NotificationsController.getInstance(super.currentAccount).updateServerNotificationsSettings(this.currentType);
         RecyclerView.ViewHolder var8 = this.listView.findViewHolderForAdapterPosition(var1);
         if (var8 != null) {
            this.adapter.onBindViewHolder(var8, var1);
         }
      }

   }

   public boolean onFragmentCreate() {
      this.updateRows();
      return super.onFragmentCreate();
   }

   public void onResume() {
      super.onResume();
      NotificationsCustomSettingsActivity.ListAdapter var1 = this.adapter;
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
         return NotificationsCustomSettingsActivity.this.rowCount;
      }

      public int getItemViewType(int var1) {
         if (var1 == NotificationsCustomSettingsActivity.this.messageSectionRow) {
            return 0;
         } else if (var1 == NotificationsCustomSettingsActivity.this.previewRow) {
            return 1;
         } else if (var1 >= NotificationsCustomSettingsActivity.this.exceptionsStartRow && var1 < NotificationsCustomSettingsActivity.this.exceptionsEndRow) {
            return 2;
         } else if (var1 == NotificationsCustomSettingsActivity.this.messageLedRow) {
            return 3;
         } else if (var1 != NotificationsCustomSettingsActivity.this.groupSection2Row && var1 != NotificationsCustomSettingsActivity.this.alertSection2Row && var1 != NotificationsCustomSettingsActivity.this.exceptionsSection2Row) {
            if (var1 == NotificationsCustomSettingsActivity.this.alertRow) {
               return 6;
            } else {
               return var1 == NotificationsCustomSettingsActivity.this.exceptionsAddRow ? 7 : 5;
            }
         } else {
            return 4;
         }
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         int var2 = var1.getItemViewType();
         boolean var3;
         if (var2 != 0 && var2 != 4) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         int var3 = var1.getItemViewType();
         int var4 = 0;
         boolean var5 = false;
         boolean var6 = false;
         String var10;
         SharedPreferences var11;
         switch(var3) {
         case 0:
            HeaderCell var15 = (HeaderCell)var1.itemView;
            if (var2 == NotificationsCustomSettingsActivity.this.messageSectionRow) {
               var15.setText(LocaleController.getString("SETTINGS", 2131560623));
            }
            break;
         case 1:
            TextCheckCell var21 = (TextCheckCell)var1.itemView;
            var11 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.access$1600(NotificationsCustomSettingsActivity.this));
            if (var2 == NotificationsCustomSettingsActivity.this.previewRow) {
               if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                  var6 = var11.getBoolean("EnablePreviewAll", true);
               } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                  var6 = var11.getBoolean("EnablePreviewGroup", true);
               } else {
                  var6 = var11.getBoolean("EnablePreviewChannel", true);
               }

               var21.setTextAndCheck(LocaleController.getString("MessagePreview", 2131559854), var6, true);
            }
            break;
         case 2:
            UserCell var14 = (UserCell)var1.itemView;
            NotificationsSettingsActivity.NotificationException var20 = (NotificationsSettingsActivity.NotificationException)NotificationsCustomSettingsActivity.this.exceptions.get(var2 - NotificationsCustomSettingsActivity.this.exceptionsStartRow);
            var6 = var5;
            if (var2 != NotificationsCustomSettingsActivity.this.exceptionsEndRow - 1) {
               var6 = true;
            }

            var14.setException(var20, (CharSequence)null, var6);
            break;
         case 3:
            TextColorCell var13 = (TextColorCell)var1.itemView;
            SharedPreferences var19 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.access$2100(NotificationsCustomSettingsActivity.this));
            if (NotificationsCustomSettingsActivity.this.currentType == 1) {
               var2 = var19.getInt("MessagesLed", -16776961);
            } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
               var2 = var19.getInt("GroupLed", -16776961);
            } else {
               var2 = var19.getInt("ChannelLed", -16776961);
            }

            while(true) {
               var3 = var2;
               if (var4 >= 9) {
                  break;
               }

               if (TextColorCell.colorsToSave[var4] == var2) {
                  var3 = TextColorCell.colors[var4];
                  break;
               }

               ++var4;
            }

            var13.setTextAndColor(LocaleController.getString("LedColor", 2131559747), var3, true);
            break;
         case 4:
            if (var2 == NotificationsCustomSettingsActivity.this.exceptionsSection2Row || var2 == NotificationsCustomSettingsActivity.this.groupSection2Row && NotificationsCustomSettingsActivity.this.exceptionsSection2Row == -1) {
               var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165395, "windowBackgroundGrayShadow"));
            } else {
               var1.itemView.setBackgroundDrawable(Theme.getThemedDrawable(this.mContext, 2131165394, "windowBackgroundGrayShadow"));
            }
            break;
         case 5:
            TextSettingsCell var18 = (TextSettingsCell)var1.itemView;
            var11 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.access$2400(NotificationsCustomSettingsActivity.this));
            if (var2 == NotificationsCustomSettingsActivity.this.messageSoundRow) {
               if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                  var10 = var11.getString("GlobalSound", LocaleController.getString("SoundDefault", 2131560801));
               } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                  var10 = var11.getString("GroupSound", LocaleController.getString("SoundDefault", 2131560801));
               } else {
                  var10 = var11.getString("ChannelSound", LocaleController.getString("SoundDefault", 2131560801));
               }

               String var17 = var10;
               if (var10.equals("NoSound")) {
                  var17 = LocaleController.getString("NoSound", 2131559952);
               }

               var18.setTextAndValue(LocaleController.getString("Sound", 2131560800), var17, true);
            } else if (var2 == NotificationsCustomSettingsActivity.this.messageVibrateRow) {
               if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                  var2 = var11.getInt("vibrate_messages", 0);
               } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                  var2 = var11.getInt("vibrate_group", 0);
               } else {
                  var2 = var11.getInt("vibrate_channel", 0);
               }

               if (var2 == 0) {
                  var18.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDefault", 2131561041), true);
               } else if (var2 == 1) {
                  var18.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Short", 2131560775), true);
               } else if (var2 == 2) {
                  var18.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("VibrationDisabled", 2131561042), true);
               } else if (var2 == 3) {
                  var18.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("Long", 2131559790), true);
               } else if (var2 == 4) {
                  var18.setTextAndValue(LocaleController.getString("Vibrate", 2131561040), LocaleController.getString("OnlyIfSilent", 2131560107), true);
               }
            } else if (var2 == NotificationsCustomSettingsActivity.this.messagePriorityRow) {
               if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                  var2 = var11.getInt("priority_messages", 1);
               } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                  var2 = var11.getInt("priority_group", 1);
               } else {
                  var2 = var11.getInt("priority_channel", 1);
               }

               if (var2 == 0) {
                  var18.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityHigh", 2131560082), true);
               } else if (var2 != 1 && var2 != 2) {
                  if (var2 == 4) {
                     var18.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityLow", 2131560083), true);
                  } else if (var2 == 5) {
                     var18.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityMedium", 2131560084), true);
                  }
               } else {
                  var18.setTextAndValue(LocaleController.getString("NotificationsImportance", 2131560072), LocaleController.getString("NotificationsPriorityUrgent", 2131560086), true);
               }
            } else if (var2 == NotificationsCustomSettingsActivity.this.messagePopupNotificationRow) {
               if (NotificationsCustomSettingsActivity.this.currentType == 1) {
                  var2 = var11.getInt("popupAll", 0);
               } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
                  var2 = var11.getInt("popupGroup", 0);
               } else {
                  var2 = var11.getInt("popupChannel", 0);
               }

               if (var2 == 0) {
                  var10 = LocaleController.getString("NoPopup", 2131559939);
               } else if (var2 == 1) {
                  var10 = LocaleController.getString("OnlyWhenScreenOn", 2131560109);
               } else if (var2 == 2) {
                  var10 = LocaleController.getString("OnlyWhenScreenOff", 2131560108);
               } else {
                  var10 = LocaleController.getString("AlwaysShowPopup", 2131558614);
               }

               var18.setTextAndValue(LocaleController.getString("PopupNotification", 2131560471), var10, true);
            }
            break;
         case 6:
            NotificationsCheckCell var8 = (NotificationsCheckCell)var1.itemView;
            var8.setDrawLine(false);
            StringBuilder var16 = new StringBuilder();
            SharedPreferences var9 = MessagesController.getNotificationsSettings(NotificationsCustomSettingsActivity.access$2900(NotificationsCustomSettingsActivity.this));
            if (NotificationsCustomSettingsActivity.this.currentType == 1) {
               var10 = LocaleController.getString("NotificationsForPrivateChats", 2131560070);
               var2 = var9.getInt("EnableAll2", 0);
            } else if (NotificationsCustomSettingsActivity.this.currentType == 0) {
               var10 = LocaleController.getString("NotificationsForGroups", 2131560069);
               var2 = var9.getInt("EnableGroup2", 0);
            } else {
               var10 = LocaleController.getString("NotificationsForChannels", 2131560067);
               var2 = var9.getInt("EnableChannel2", 0);
            }

            var4 = ConnectionsManager.getInstance(NotificationsCustomSettingsActivity.access$3000(NotificationsCustomSettingsActivity.this)).getCurrentTime();
            if (var2 < var4) {
               var6 = true;
            } else {
               var6 = false;
            }

            byte var12;
            label180: {
               if (var6) {
                  var16.append(LocaleController.getString("NotificationsOn", 2131560080));
               } else {
                  if (var2 - 31536000 < var4) {
                     var16.append(LocaleController.formatString("NotificationsOffUntil", 2131560079, LocaleController.stringForMessageListDate((long)var2)));
                     var12 = 2;
                     break label180;
                  }

                  var16.append(LocaleController.getString("NotificationsOff", 2131560078));
               }

               var12 = 0;
            }

            var8.setTextAndValueAndCheck(var10, var16, var6, var12, false);
            break;
         case 7:
            TextCell var7 = (TextCell)var1.itemView;
            var7.setColors("windowBackgroundWhiteBlueIcon", "windowBackgroundWhiteBlueButton");
            if (var2 == NotificationsCustomSettingsActivity.this.exceptionsAddRow) {
               var10 = LocaleController.getString("NotificationsAddAnException", 2131560056);
               if (NotificationsCustomSettingsActivity.this.exceptionsStartRow != -1) {
                  var6 = true;
               }

               var7.setTextAndIcon(var10, 2131165272, var6);
            }
         }

      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         Object var3;
         switch(var2) {
         case 0:
            var3 = new HeaderCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 1:
            var3 = new TextCheckCell(this.mContext);
            ((TextCheckCell)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 2:
            var3 = new UserCell(this.mContext, 6, 0, false);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 3:
            var3 = new TextColorCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 4:
            var3 = new ShadowSectionCell(this.mContext);
            break;
         case 5:
            var3 = new TextSettingsCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         case 6:
            var3 = new NotificationsCheckCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
            break;
         default:
            var3 = new TextCell(this.mContext);
            ((View)var3).setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         }

         return new RecyclerListView.Holder((View)var3);
      }

      public void onViewAttachedToWindow(RecyclerView.ViewHolder var1) {
         if (NotificationsCustomSettingsActivity.this.exceptions != null && NotificationsCustomSettingsActivity.this.exceptions.isEmpty()) {
            boolean var2 = NotificationsController.getInstance(NotificationsCustomSettingsActivity.access$3200(NotificationsCustomSettingsActivity.this)).isGlobalNotificationsEnabled(NotificationsCustomSettingsActivity.this.currentType);
            int var3 = var1.getItemViewType();
            if (var3 != 0) {
               if (var3 != 1) {
                  if (var3 != 3) {
                     if (var3 == 5) {
                        ((TextSettingsCell)var1.itemView).setEnabled(var2, (ArrayList)null);
                     }
                  } else {
                     ((TextColorCell)var1.itemView).setEnabled(var2, (ArrayList)null);
                  }
               } else {
                  ((TextCheckCell)var1.itemView).setEnabled(var2, (ArrayList)null);
               }
            } else {
               HeaderCell var4 = (HeaderCell)var1.itemView;
               if (var1.getAdapterPosition() == NotificationsCustomSettingsActivity.this.messageSectionRow) {
                  var4.setEnabled(var2, (ArrayList)null);
               } else {
                  var4.setEnabled(true, (ArrayList)null);
               }
            }
         }

      }
   }

   private class SearchAdapter extends RecyclerListView.SelectionAdapter {
      private Context mContext;
      private ArrayList searchResult = new ArrayList();
      private ArrayList searchResultNames = new ArrayList();
      private Timer searchTimer;

      public SearchAdapter(Context var2) {
         this.mContext = var2;
      }

      private void processSearch(String var1) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsCustomSettingsActivity$SearchAdapter$R_yRN0Y21CFdspI_6cy_hAalLcc(this, var1));
      }

      private void updateSearchResults(ArrayList var1, ArrayList var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$NotificationsCustomSettingsActivity$SearchAdapter$f9v_aVxREJMj58yLknoa0t4SGPc(this, var1, var2));
      }

      public int getItemCount() {
         return this.searchResult.size();
      }

      public int getItemViewType(int var1) {
         return 0;
      }

      public boolean isEnabled(RecyclerView.ViewHolder var1) {
         return true;
      }

      // $FF: synthetic method
      public void lambda$null$0$NotificationsCustomSettingsActivity$SearchAdapter(String var1, ArrayList var2) {
         String var3 = var1.trim().toLowerCase();
         if (var3.length() == 0) {
            this.updateSearchResults(new ArrayList(), new ArrayList());
         } else {
            String var4;
            label115: {
               var4 = LocaleController.getInstance().getTranslitString(var3);
               if (!var3.equals(var4)) {
                  var1 = var4;
                  if (var4.length() != 0) {
                     break label115;
                  }
               }

               var1 = null;
            }

            byte var5 = 1;
            byte var6;
            if (var1 != null) {
               var6 = 1;
            } else {
               var6 = 0;
            }

            String[] var7 = new String[var6 + 1];
            var7[0] = var3;
            if (var1 != null) {
               var7[1] = var1;
            }

            ArrayList var8 = new ArrayList();
            ArrayList var9 = new ArrayList();
            String[] var10 = new String[2];
            int var11 = 0;

            label110:
            for(var6 = var5; var11 < var2.size(); ++var11) {
               label120: {
                  NotificationsSettingsActivity.NotificationException var22 = (NotificationsSettingsActivity.NotificationException)var2.get(var11);
                  long var12 = var22.did;
                  int var14 = (int)var12;
                  int var24 = (int)(var12 >> 32);
                  TLRPC.User var19;
                  if (var14 != 0) {
                     if (var14 > 0) {
                        var19 = MessagesController.getInstance(NotificationsCustomSettingsActivity.access$1000(NotificationsCustomSettingsActivity.this)).getUser(var14);
                        if (var19.deleted) {
                           var5 = var6;
                           break label120;
                        }

                        if (var19 != null) {
                           var10[0] = ContactsController.formatName(var19.first_name, var19.last_name);
                           var10[var6] = var19.username;
                        }
                     } else {
                        TLRPC.Chat var20 = MessagesController.getInstance(NotificationsCustomSettingsActivity.access$1100(NotificationsCustomSettingsActivity.this)).getChat(-var14);
                        if (var20 != null) {
                           var5 = var6;
                           if (var20.left) {
                              break label120;
                           }

                           var5 = var6;
                           if (var20.kicked) {
                              break label120;
                           }

                           if (var20.migrated_to != null) {
                              var5 = var6;
                              break label120;
                           }

                           var10[0] = var20.title;
                           var10[var6] = var20.username;
                        }
                     }
                  } else {
                     TLRPC.EncryptedChat var21 = MessagesController.getInstance(NotificationsCustomSettingsActivity.access$1200(NotificationsCustomSettingsActivity.this)).getEncryptedChat(var24);
                     if (var21 != null) {
                        var19 = MessagesController.getInstance(NotificationsCustomSettingsActivity.access$1300(NotificationsCustomSettingsActivity.this)).getUser(var21.user_id);
                        if (var19 != null) {
                           var10[0] = ContactsController.formatName(var19.first_name, var19.last_name);
                           var10[var6] = var19.username;
                        }
                     }
                  }

                  String var15 = var10[0];
                  var10[0] = var10[0].toLowerCase();
                  var4 = LocaleController.getInstance().getTranslitString(var10[0]);
                  var1 = var4;
                  if (var10[0] != null) {
                     var1 = var4;
                     if (var10[0].equals(var4)) {
                        var1 = null;
                     }
                  }

                  var14 = 0;
                  byte var16 = 0;

                  for(var5 = var6; var14 < var7.length; var16 = var6) {
                     label89: {
                        label123: {
                           var4 = var7[var14];
                           StringBuilder var18;
                           if (var10[0] != null) {
                              if (var10[0].startsWith(var4)) {
                                 break label123;
                              }

                              String var17 = var10[0];
                              var18 = new StringBuilder();
                              var18.append(" ");
                              var18.append(var4);
                              if (var17.contains(var18.toString())) {
                                 break label123;
                              }
                           }

                           if (var1 != null) {
                              if (var1.startsWith(var4)) {
                                 break label123;
                              }

                              var18 = new StringBuilder();
                              var18.append(" ");
                              var18.append(var4);
                              if (var1.contains(var18.toString())) {
                                 break label123;
                              }
                           }

                           var6 = var16;
                           if (var10[1] != null) {
                              var6 = var16;
                              if (var10[1].startsWith(var4)) {
                                 var6 = 2;
                              }
                           }
                           break label89;
                        }

                        var6 = 1;
                     }

                     var16 = 1;
                     var5 = 1;
                     if (var6 != 0) {
                        if (var6 == 1) {
                           var9.add(AndroidUtilities.generateSearchName(var15, (String)null, var4));
                        } else {
                           StringBuilder var23 = new StringBuilder();
                           var23.append("@");
                           var23.append(var10[1]);
                           var1 = var23.toString();
                           StringBuilder var25 = new StringBuilder();
                           var25.append("@");
                           var25.append(var4);
                           var9.add(AndroidUtilities.generateSearchName(var1, (String)null, var25.toString()));
                        }

                        var8.add(var22);
                        var6 = var16;
                        continue label110;
                     }

                     ++var14;
                  }
               }

               var6 = var5;
            }

            this.updateSearchResults(var8, var9);
         }
      }

      // $FF: synthetic method
      public void lambda$processSearch$1$NotificationsCustomSettingsActivity$SearchAdapter(String var1) {
         ArrayList var2 = new ArrayList(NotificationsCustomSettingsActivity.this.exceptions);
         Utilities.searchQueue.postRunnable(new _$$Lambda$NotificationsCustomSettingsActivity$SearchAdapter$1sbGs4NEyQ8S6Oqw0ayErmMeXIk(this, var1, var2));
      }

      // $FF: synthetic method
      public void lambda$updateSearchResults$2$NotificationsCustomSettingsActivity$SearchAdapter(ArrayList var1, ArrayList var2) {
         this.searchResult = var1;
         this.searchResultNames = var2;
         this.notifyDataSetChanged();
      }

      public void onBindViewHolder(RecyclerView.ViewHolder var1, int var2) {
         UserCell var3 = (UserCell)var1.itemView;
         NotificationsSettingsActivity.NotificationException var4 = (NotificationsSettingsActivity.NotificationException)this.searchResult.get(var2);
         CharSequence var7 = (CharSequence)this.searchResultNames.get(var2);
         int var5 = this.searchResult.size();
         boolean var6 = true;
         if (var2 == var5 - 1) {
            var6 = false;
         }

         var3.setException(var4, var7, var6);
      }

      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup var1, int var2) {
         UserCell var3 = new UserCell(this.mContext, 9, 0, false);
         var3.setPadding(AndroidUtilities.dp(6.0F), 0, AndroidUtilities.dp(6.0F), 0);
         var3.setBackgroundColor(Theme.getColor("windowBackgroundWhite"));
         return new RecyclerListView.Holder(var3);
      }

      public void searchDialogs(final String var1) {
         try {
            if (this.searchTimer != null) {
               this.searchTimer.cancel();
            }
         } catch (Exception var3) {
            FileLog.e((Throwable)var3);
         }

         if (var1 == null) {
            this.searchResult.clear();
            this.searchResultNames.clear();
            this.notifyDataSetChanged();
         } else {
            this.searchTimer = new Timer();
            this.searchTimer.schedule(new TimerTask() {
               public void run() {
                  try {
                     SearchAdapter.this.searchTimer.cancel();
                     SearchAdapter.this.searchTimer = null;
                  } catch (Exception var2) {
                     FileLog.e((Throwable)var2);
                  }

                  SearchAdapter.this.processSearch(var1);
               }
            }, 200L, 300L);
         }

      }
   }
}
