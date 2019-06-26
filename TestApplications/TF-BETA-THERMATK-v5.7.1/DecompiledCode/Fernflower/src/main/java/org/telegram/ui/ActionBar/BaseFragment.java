package org.telegram.ui.ActionBar;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;

public class BaseFragment {
   protected ActionBar actionBar;
   protected Bundle arguments;
   protected int classGuid;
   protected int currentAccount;
   private boolean finishing;
   protected View fragmentView;
   protected boolean hasOwnBackground;
   protected boolean inPreviewMode;
   private boolean isFinished;
   protected ActionBarLayout parentLayout;
   protected boolean swipeBackEnabled;
   protected Dialog visibleDialog;

   public BaseFragment() {
      this.currentAccount = UserConfig.selectedAccount;
      this.swipeBackEnabled = true;
      this.hasOwnBackground = false;
      this.classGuid = ConnectionsManager.generateClassGuid();
   }

   public BaseFragment(Bundle var1) {
      this.currentAccount = UserConfig.selectedAccount;
      this.swipeBackEnabled = true;
      this.hasOwnBackground = false;
      this.arguments = var1;
      this.classGuid = ConnectionsManager.generateClassGuid();
   }

   public boolean canBeginSlide() {
      return true;
   }

   protected void clearViews() {
      View var1 = this.fragmentView;
      ViewGroup var4;
      if (var1 != null) {
         var4 = (ViewGroup)var1.getParent();
         if (var4 != null) {
            try {
               this.onRemoveFromParent();
               var4.removeView(this.fragmentView);
            } catch (Exception var3) {
               FileLog.e((Throwable)var3);
            }
         }

         this.fragmentView = null;
      }

      ActionBar var5 = this.actionBar;
      if (var5 != null) {
         var4 = (ViewGroup)var5.getParent();
         if (var4 != null) {
            try {
               var4.removeView(this.actionBar);
            } catch (Exception var2) {
               FileLog.e((Throwable)var2);
            }
         }

         this.actionBar = null;
      }

      this.parentLayout = null;
   }

   protected ActionBar createActionBar(Context var1) {
      ActionBar var2 = new ActionBar(var1);
      var2.setBackgroundColor(Theme.getColor("actionBarDefault"));
      var2.setItemsBackgroundColor(Theme.getColor("actionBarDefaultSelector"), false);
      var2.setItemsBackgroundColor(Theme.getColor("actionBarActionModeDefaultSelector"), true);
      var2.setItemsColor(Theme.getColor("actionBarDefaultIcon"), false);
      var2.setItemsColor(Theme.getColor("actionBarActionModeDefaultIcon"), true);
      if (this.inPreviewMode) {
         var2.setOccupyStatusBar(false);
      }

      return var2;
   }

   public View createView(Context var1) {
      return null;
   }

   public void dismissCurrentDialig() {
      Dialog var1 = this.visibleDialog;
      if (var1 != null) {
         try {
            var1.dismiss();
            this.visibleDialog = null;
         } catch (Exception var2) {
            FileLog.e((Throwable)var2);
         }

      }
   }

   public boolean dismissDialogOnPause(Dialog var1) {
      return true;
   }

   public boolean extendActionMode(Menu var1) {
      return false;
   }

   public void finishFragment() {
      this.finishFragment(true);
   }

   public void finishFragment(boolean var1) {
      if (!this.isFinished) {
         ActionBarLayout var2 = this.parentLayout;
         if (var2 != null) {
            this.finishing = true;
            var2.closeLastFragment(var1);
         }
      }

   }

   public void finishPreviewFragment() {
      this.parentLayout.finishPreviewFragment();
   }

   protected AccountInstance getAccountInstance() {
      return AccountInstance.getInstance(this.currentAccount);
   }

   public ActionBar getActionBar() {
      return this.actionBar;
   }

   public Bundle getArguments() {
      return this.arguments;
   }

   protected ConnectionsManager getConnectionsManager() {
      return this.getAccountInstance().getConnectionsManager();
   }

   protected ContactsController getContactsController() {
      return this.getAccountInstance().getContactsController();
   }

   public int getCurrentAccount() {
      return this.currentAccount;
   }

   protected DataQuery getDataQuery() {
      return this.getAccountInstance().getDataQuery();
   }

   public BaseFragment getFragmentForAlert(int var1) {
      ActionBarLayout var2 = this.parentLayout;
      if (var2 != null && var2.fragmentsStack.size() > var1 + 1) {
         ArrayList var3 = this.parentLayout.fragmentsStack;
         return (BaseFragment)var3.get(var3.size() - 2 - var1);
      } else {
         return this;
      }
   }

   public View getFragmentView() {
      return this.fragmentView;
   }

   protected MessagesController getMessagesController() {
      return this.getAccountInstance().getMessagesController();
   }

   public NotificationCenter getNotificationCenter() {
      return this.getAccountInstance().getNotificationCenter();
   }

   protected NotificationsController getNotificationsController() {
      return this.getAccountInstance().getNotificationsController();
   }

   public Activity getParentActivity() {
      ActionBarLayout var1 = this.parentLayout;
      return var1 != null ? var1.parentActivity : null;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return new ThemeDescription[0];
   }

   public UserConfig getUserConfig() {
      return this.getAccountInstance().getUserConfig();
   }

   public Dialog getVisibleDialog() {
      return this.visibleDialog;
   }

   protected boolean isFinishing() {
      return this.finishing;
   }

   // $FF: synthetic method
   public void lambda$showDialog$0$BaseFragment(OnDismissListener var1, DialogInterface var2) {
      if (var1 != null) {
         var1.onDismiss(var2);
      }

      this.onDialogDismiss(this.visibleDialog);
      this.visibleDialog = null;
   }

   public void movePreviewFragment(float var1) {
      this.parentLayout.movePreviewFragment(var1);
   }

   public boolean needDelayOpenAnimation() {
      return false;
   }

   public void onActivityResultFragment(int var1, int var2, Intent var3) {
   }

   public boolean onBackPressed() {
      return true;
   }

   protected void onBecomeFullyHidden() {
   }

   protected void onBecomeFullyVisible() {
      if (((AccessibilityManager)ApplicationLoader.applicationContext.getSystemService("accessibility")).isEnabled()) {
         ActionBar var1 = this.getActionBar();
         if (var1 != null) {
            String var2 = var1.getTitle();
            if (!TextUtils.isEmpty(var2)) {
               this.setParentActivityTitle(var2);
            }
         }
      }

   }

   public void onBeginSlide() {
      try {
         if (this.visibleDialog != null && this.visibleDialog.isShowing()) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      ActionBar var1 = this.actionBar;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public void onConfigurationChanged(Configuration var1) {
   }

   protected AnimatorSet onCustomTransitionAnimation(boolean var1, Runnable var2) {
      return null;
   }

   protected void onDialogDismiss(Dialog var1) {
   }

   public boolean onFragmentCreate() {
      return true;
   }

   public void onFragmentDestroy() {
      ConnectionsManager.getInstance(this.currentAccount).cancelRequestsForGuid(this.classGuid);
      this.isFinished = true;
      ActionBar var1 = this.actionBar;
      if (var1 != null) {
         var1.setEnabled(false);
      }

   }

   public void onLowMemory() {
   }

   public void onPause() {
      ActionBar var1 = this.actionBar;
      if (var1 != null) {
         var1.onPause();
      }

      try {
         if (this.visibleDialog != null && this.visibleDialog.isShowing() && this.dismissDialogOnPause(this.visibleDialog)) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

   }

   protected void onRemoveFromParent() {
   }

   public void onRequestPermissionsResultFragment(int var1, String[] var2, int[] var3) {
   }

   public void onResume() {
   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
   }

   protected void onTransitionAnimationStart(boolean var1, boolean var2) {
   }

   public boolean presentFragment(BaseFragment var1) {
      ActionBarLayout var2 = this.parentLayout;
      boolean var3;
      if (var2 != null && var2.presentFragment(var1)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean presentFragment(BaseFragment var1, boolean var2) {
      ActionBarLayout var3 = this.parentLayout;
      if (var3 != null && var3.presentFragment(var1, var2)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean presentFragment(BaseFragment var1, boolean var2, boolean var3) {
      ActionBarLayout var4 = this.parentLayout;
      if (var4 != null && var4.presentFragment(var1, var2, var3, true, false)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean presentFragmentAsPreview(BaseFragment var1) {
      ActionBarLayout var2 = this.parentLayout;
      boolean var3;
      if (var2 != null && var2.presentFragmentAsPreview(var1)) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public void removeSelfFromStack() {
      if (!this.isFinished) {
         ActionBarLayout var1 = this.parentLayout;
         if (var1 != null) {
            var1.removeFragmentFromStack(this);
         }
      }

   }

   public void restoreSelfArgs(Bundle var1) {
   }

   public void saveSelfArgs(Bundle var1) {
   }

   public void setCurrentAccount(int var1) {
      if (this.fragmentView == null) {
         this.currentAccount = var1;
      } else {
         throw new IllegalStateException("trying to set current account when fragment UI already created");
      }
   }

   protected void setInPreviewMode(boolean var1) {
      this.inPreviewMode = var1;
      ActionBar var2 = this.actionBar;
      if (var2 != null) {
         boolean var3 = this.inPreviewMode;
         var1 = false;
         if (var3) {
            var2.setOccupyStatusBar(false);
         } else {
            if (VERSION.SDK_INT >= 21) {
               var1 = true;
            }

            var2.setOccupyStatusBar(var1);
         }
      }

   }

   protected void setParentActivityTitle(CharSequence var1) {
      Activity var2 = this.getParentActivity();
      if (var2 != null) {
         var2.setTitle(var1);
      }

   }

   protected void setParentLayout(ActionBarLayout var1) {
      if (this.parentLayout != var1) {
         this.parentLayout = var1;
         View var5 = this.fragmentView;
         ViewGroup var6;
         if (var5 != null) {
            var6 = (ViewGroup)var5.getParent();
            if (var6 != null) {
               try {
                  this.onRemoveFromParent();
                  var6.removeView(this.fragmentView);
               } catch (Exception var4) {
                  FileLog.e((Throwable)var4);
               }
            }

            var1 = this.parentLayout;
            if (var1 != null && var1.getContext() != this.fragmentView.getContext()) {
               this.fragmentView = null;
            }
         }

         if (this.actionBar != null) {
            var1 = this.parentLayout;
            boolean var2;
            if (var1 != null && var1.getContext() != this.actionBar.getContext()) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (this.actionBar.getAddToContainer() || var2) {
               var6 = (ViewGroup)this.actionBar.getParent();
               if (var6 != null) {
                  try {
                     var6.removeView(this.actionBar);
                  } catch (Exception var3) {
                     FileLog.e((Throwable)var3);
                  }
               }
            }

            if (var2) {
               this.actionBar = null;
            }
         }

         var1 = this.parentLayout;
         if (var1 != null && this.actionBar == null) {
            this.actionBar = this.createActionBar(var1.getContext());
            this.actionBar.parentFragment = this;
         }
      }

   }

   public void setVisibleDialog(Dialog var1) {
      this.visibleDialog = var1;
   }

   public Dialog showDialog(Dialog var1) {
      return this.showDialog(var1, false, (OnDismissListener)null);
   }

   public Dialog showDialog(Dialog var1, OnDismissListener var2) {
      return this.showDialog(var1, false, var2);
   }

   public Dialog showDialog(Dialog var1, boolean var2, OnDismissListener var3) {
      if (var1 != null) {
         ActionBarLayout var4 = this.parentLayout;
         if (var4 != null && !var4.animationInProgress && !var4.startedTracking && (var2 || !var4.checkTransitionAnimation())) {
            try {
               if (this.visibleDialog != null) {
                  this.visibleDialog.dismiss();
                  this.visibleDialog = null;
               }
            } catch (Exception var5) {
               FileLog.e((Throwable)var5);
            }

            try {
               this.visibleDialog = var1;
               this.visibleDialog.setCanceledOnTouchOutside(true);
               var1 = this.visibleDialog;
               _$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM var7 = new _$$Lambda$BaseFragment$vXTvtAK8XZpLjv4Env96FSJndOM(this, var3);
               var1.setOnDismissListener(var7);
               this.visibleDialog.show();
               var1 = this.visibleDialog;
               return var1;
            } catch (Exception var6) {
               FileLog.e((Throwable)var6);
            }
         }
      }

      return null;
   }

   public void startActivityForResult(Intent var1, int var2) {
      ActionBarLayout var3 = this.parentLayout;
      if (var3 != null) {
         var3.startActivityForResult(var1, var2);
      }

   }
}
