package org.telegram.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;

public class ExternalActionActivity extends Activity implements ActionBarLayout.ActionBarLayoutDelegate {
   private static ArrayList layerFragmentsStack = new ArrayList();
   private static ArrayList mainFragmentsStack = new ArrayList();
   private ActionBarLayout actionBarLayout;
   private View backgroundTablet;
   protected DrawerLayoutContainer drawerLayoutContainer;
   private boolean finished;
   private ActionBarLayout layersActionBarLayout;
   private Runnable lockRunnable;
   private Intent passcodeSaveIntent;
   private int passcodeSaveIntentAccount;
   private boolean passcodeSaveIntentIsNew;
   private boolean passcodeSaveIntentIsRestore;
   private int passcodeSaveIntentState;
   private PasscodeView passcodeView;

   private boolean handleIntent(Intent var1, boolean var2, boolean var3, boolean var4, int var5, int var6) {
      if (var4 || !AndroidUtilities.needShowPasscode(true) && !SharedConfig.isWaitingForPasscodeEnter) {
         if ("org.telegram.passport.AUTHORIZE".equals(var1.getAction())) {
            AlertDialog var12;
            if (var6 == 0) {
               int var7 = UserConfig.getActivatedAccountsCount();
               if (var7 == 0) {
                  this.passcodeSaveIntent = var1;
                  this.passcodeSaveIntentIsNew = var2;
                  this.passcodeSaveIntentIsRestore = var3;
                  this.passcodeSaveIntentAccount = var5;
                  this.passcodeSaveIntentState = var6;
                  LoginActivity var13 = new LoginActivity();
                  if (AndroidUtilities.isTablet()) {
                     this.layersActionBarLayout.addFragmentToStack(var13);
                  } else {
                     this.actionBarLayout.addFragmentToStack(var13);
                  }

                  if (!AndroidUtilities.isTablet()) {
                     this.backgroundTablet.setVisibility(8);
                  }

                  this.actionBarLayout.showLastFragment();
                  if (AndroidUtilities.isTablet()) {
                     this.layersActionBarLayout.showLastFragment();
                  }

                  AlertDialog.Builder var14 = new AlertDialog.Builder(this);
                  var14.setTitle(LocaleController.getString("AppName", 2131558635));
                  var14.setMessage(LocaleController.getString("PleaseLoginPassport", 2131560458));
                  var14.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var14.show();
                  return true;
               }

               if (var7 >= 2) {
                  var12 = AlertsCreator.createAccountSelectDialog(this, new _$$Lambda$ExternalActionActivity$7MenW_e7ZVDhaPYTW6k7H5_jjBA(this, var5, var1, var2, var3, var4));
                  var12.show();
                  var12.setCanceledOnTouchOutside(false);
                  var12.setOnDismissListener(new _$$Lambda$ExternalActionActivity$HPinU7ZiWQ3pid8nvHDiLL5fIOA(this));
                  return true;
               }
            }

            var6 = var1.getIntExtra("bot_id", 0);
            String var8 = var1.getStringExtra("nonce");
            String var9 = var1.getStringExtra("payload");
            TLRPC.TL_account_getAuthorizationForm var10 = new TLRPC.TL_account_getAuthorizationForm();
            var10.bot_id = var6;
            var10.scope = var1.getStringExtra("scope");
            var10.public_key = var1.getStringExtra("public_key");
            if (var6 == 0 || TextUtils.isEmpty(var9) && TextUtils.isEmpty(var8) || TextUtils.isEmpty(var10.scope) || TextUtils.isEmpty(var10.public_key)) {
               this.finish();
               return false;
            }

            int[] var11 = new int[]{0};
            var12 = new AlertDialog(this, 3);
            var12.setOnCancelListener(new _$$Lambda$ExternalActionActivity$JSGDZffGA_C8G3znC_C8fmn13dg(var5, var11));
            var12.show();
            var11[0] = ConnectionsManager.getInstance(var5).sendRequest(var10, new _$$Lambda$ExternalActionActivity$7IhkCSknxlX6e3UV6VR8IaZyD4g(this, var11, var5, var12, var10, var9, var8), 10);
         } else {
            if (AndroidUtilities.isTablet()) {
               if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                  this.layersActionBarLayout.addFragmentToStack(new CacheControlActivity());
               }
            } else if (this.actionBarLayout.fragmentsStack.isEmpty()) {
               this.actionBarLayout.addFragmentToStack(new CacheControlActivity());
            }

            if (!AndroidUtilities.isTablet()) {
               this.backgroundTablet.setVisibility(8);
            }

            this.actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
               this.layersActionBarLayout.showLastFragment();
            }

            var1.setAction((String)null);
         }

         return false;
      } else {
         this.showPasscodeActivity();
         this.passcodeSaveIntent = var1;
         this.passcodeSaveIntentIsNew = var2;
         this.passcodeSaveIntentIsRestore = var3;
         this.passcodeSaveIntentAccount = var5;
         this.passcodeSaveIntentState = var6;
         UserConfig.getInstance(var5).saveConfig(false);
         return false;
      }
   }

   // $FF: synthetic method
   static void lambda$handleIntent$5(int var0, int[] var1, DialogInterface var2) {
      ConnectionsManager.getInstance(var0).cancelRequest(var1[0], true);
   }

   // $FF: synthetic method
   static void lambda$onCreate$1(View var0) {
   }

   private void onFinish() {
      if (!this.finished) {
         Runnable var1 = this.lockRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
            this.lockRunnable = null;
         }

         this.finished = true;
      }
   }

   private void onPasscodePause() {
      Runnable var1 = this.lockRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.lockRunnable = null;
      }

      if (SharedConfig.passcodeHash.length() != 0) {
         SharedConfig.lastPauseTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
         this.lockRunnable = new Runnable() {
            public void run() {
               if (ExternalActionActivity.this.lockRunnable == this) {
                  if (AndroidUtilities.needShowPasscode(true)) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("lock app");
                     }

                     ExternalActionActivity.this.showPasscodeActivity();
                  } else if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("didn't pass lock check");
                  }

                  ExternalActionActivity.this.lockRunnable = null;
               }

            }
         };
         if (SharedConfig.appLocked) {
            AndroidUtilities.runOnUIThread(this.lockRunnable, 1000L);
         } else {
            int var2 = SharedConfig.autoLockIn;
            if (var2 != 0) {
               AndroidUtilities.runOnUIThread(this.lockRunnable, (long)var2 * 1000L + 1000L);
            }
         }
      } else {
         SharedConfig.lastPauseTime = 0;
      }

      SharedConfig.saveConfig();
   }

   private void onPasscodeResume() {
      Runnable var1 = this.lockRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.lockRunnable = null;
      }

      if (AndroidUtilities.needShowPasscode(true)) {
         this.showPasscodeActivity();
      }

      if (SharedConfig.lastPauseTime != 0) {
         SharedConfig.lastPauseTime = 0;
         SharedConfig.saveConfig();
      }

   }

   private void showPasscodeActivity() {
      if (this.passcodeView != null) {
         SharedConfig.appLocked = true;
         if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(false, false);
         } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(false, true);
         } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(false, true);
         }

         this.passcodeView.onShow();
         SharedConfig.isWaitingForPasscodeEnter = true;
         this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
         this.passcodeView.setDelegate(new _$$Lambda$ExternalActionActivity$gW5CjfzBu63uWJgg9FkpT1P0Byc(this));
      }
   }

   public void fixLayout() {
      if (AndroidUtilities.isTablet()) {
         ActionBarLayout var1 = this.actionBarLayout;
         if (var1 != null) {
            var1.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
               public void onGlobalLayout() {
                  ExternalActionActivity.this.needLayout();
                  if (ExternalActionActivity.this.actionBarLayout != null) {
                     ExternalActionActivity.this.actionBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                  }

               }
            });
         }
      }
   }

   // $FF: synthetic method
   public void lambda$handleIntent$10$ExternalActionActivity(int[] var1, int var2, AlertDialog var3, TLRPC.TL_account_getAuthorizationForm var4, String var5, String var6, TLObject var7, TLRPC.TL_error var8) {
      TLRPC.TL_account_authorizationForm var9 = (TLRPC.TL_account_authorizationForm)var7;
      if (var9 != null) {
         TLRPC.TL_account_getPassword var10 = new TLRPC.TL_account_getPassword();
         var1[0] = ConnectionsManager.getInstance(var2).sendRequest(var10, new _$$Lambda$ExternalActionActivity$JR_1IB3pX75b9s0gpd__Ar_xSw8(this, var3, var2, var9, var4, var5, var6));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$ExternalActionActivity$_9fe3mEaNJ9F6qOli_eauMz3QBw(this, var3, var8));
      }

   }

   // $FF: synthetic method
   public void lambda$handleIntent$3$ExternalActionActivity(int var1, Intent var2, boolean var3, boolean var4, boolean var5, int var6) {
      if (var6 != var1) {
         this.switchToAccount(var6);
      }

      this.handleIntent(var2, var3, var4, var5, var6, 1);
   }

   // $FF: synthetic method
   public void lambda$handleIntent$4$ExternalActionActivity(DialogInterface var1) {
      this.setResult(0);
      this.finish();
   }

   // $FF: synthetic method
   public void lambda$null$6$ExternalActionActivity(AlertDialog var1, TLObject var2, int var3, TLRPC.TL_account_authorizationForm var4, TLRPC.TL_account_getAuthorizationForm var5, String var6, String var7) {
      try {
         var1.dismiss();
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
      }

      if (var2 != null) {
         TLRPC.TL_account_password var9 = (TLRPC.TL_account_password)var2;
         MessagesController.getInstance(var3).putUsers(var4.users, false);
         PassportActivity var10 = new PassportActivity(5, var5.bot_id, var5.scope, var5.public_key, var6, var7, (String)null, var4, var9);
         var10.setNeedActivityResult(true);
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.addFragmentToStack(var10);
         } else {
            this.actionBarLayout.addFragmentToStack(var10);
         }

         if (!AndroidUtilities.isTablet()) {
            this.backgroundTablet.setVisibility(8);
         }

         this.actionBarLayout.showLastFragment();
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$7$ExternalActionActivity(AlertDialog var1, int var2, TLRPC.TL_account_authorizationForm var3, TLRPC.TL_account_getAuthorizationForm var4, String var5, String var6, TLObject var7, TLRPC.TL_error var8) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$ExternalActionActivity$Okpye_XNIzLiHpMbfSsCYU0NOVg(this, var1, var7, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$null$8$ExternalActionActivity(TLRPC.TL_error var1, DialogInterface var2) {
      this.setResult(1, (new Intent()).putExtra("error", var1.text));
      this.finish();
   }

   // $FF: synthetic method
   public void lambda$null$9$ExternalActionActivity(AlertDialog var1, TLRPC.TL_error var2) {
      Exception var10000;
      label71: {
         boolean var3;
         boolean var10001;
         try {
            var1.dismiss();
            var3 = "APP_VERSION_OUTDATED".equals(var2.text);
         } catch (Exception var11) {
            var10000 = var11;
            var10001 = false;
            break label71;
         }

         Intent var13;
         if (var3) {
            label47: {
               AlertDialog var4;
               try {
                  var4 = AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", 2131560951), true);
               } catch (Exception var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label47;
               }

               if (var4 != null) {
                  try {
                     _$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k var12 = new _$$Lambda$ExternalActionActivity$tLYtJXpiU7Sr8cawZGmHGmD9I2k(this, var2);
                     var4.setOnDismissListener(var12);
                     return;
                  } catch (Exception var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               } else {
                  try {
                     var13 = new Intent();
                     this.setResult(1, var13.putExtra("error", var2.text));
                     this.finish();
                     return;
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }
            }
         } else {
            label67: {
               label75: {
                  try {
                     if (!"BOT_INVALID".equals(var2.text) && !"PUBLIC_KEY_REQUIRED".equals(var2.text) && !"PUBLIC_KEY_INVALID".equals(var2.text) && !"SCOPE_EMPTY".equals(var2.text) && !"PAYLOAD_EMPTY".equals(var2.text)) {
                        break label75;
                     }
                  } catch (Exception var10) {
                     var10000 = var10;
                     var10001 = false;
                     break label67;
                  }

                  try {
                     var13 = new Intent();
                     this.setResult(1, var13.putExtra("error", var2.text));
                     this.finish();
                     return;
                  } catch (Exception var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label67;
                  }
               }

               try {
                  this.setResult(0);
                  this.finish();
                  return;
               } catch (Exception var8) {
                  var10000 = var8;
                  var10001 = false;
               }
            }
         }
      }

      Exception var14 = var10000;
      FileLog.e((Throwable)var14);
   }

   // $FF: synthetic method
   public boolean lambda$onCreate$0$ExternalActionActivity(View var1, MotionEvent var2) {
      if (!this.actionBarLayout.fragmentsStack.isEmpty() && var2.getAction() == 1) {
         float var3 = var2.getX();
         float var4 = var2.getY();
         int[] var7 = new int[2];
         this.layersActionBarLayout.getLocationOnScreen(var7);
         int var5 = var7[0];
         int var6 = var7[1];
         if (!this.layersActionBarLayout.checkTransitionAnimation() && (var3 <= (float)var5 || var3 >= (float)(var5 + this.layersActionBarLayout.getWidth()) || var4 <= (float)var6 || var4 >= (float)(var6 + this.layersActionBarLayout.getHeight()))) {
            if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
               while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                  ActionBarLayout var8 = this.layersActionBarLayout;
                  var8.removeFragmentFromStack((BaseFragment)var8.fragmentsStack.get(0));
               }

               this.layersActionBarLayout.closeLastFragment(true);
            }

            return true;
         }
      }

      return false;
   }

   // $FF: synthetic method
   public void lambda$showPasscodeActivity$2$ExternalActionActivity() {
      SharedConfig.isWaitingForPasscodeEnter = false;
      Intent var1 = this.passcodeSaveIntent;
      if (var1 != null) {
         this.handleIntent(var1, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, this.passcodeSaveIntentAccount, this.passcodeSaveIntentState);
         this.passcodeSaveIntent = null;
      }

      this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
      this.actionBarLayout.showLastFragment();
      if (AndroidUtilities.isTablet()) {
         this.layersActionBarLayout.showLastFragment();
      }

   }

   public boolean needAddFragmentToStack(BaseFragment var1, ActionBarLayout var2) {
      return true;
   }

   public boolean needCloseLastFragment(ActionBarLayout var1) {
      if (AndroidUtilities.isTablet()) {
         if (var1 == this.actionBarLayout && var1.fragmentsStack.size() <= 1) {
            this.onFinish();
            this.finish();
            return false;
         }

         if (var1 == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
            this.onFinish();
            this.finish();
            return false;
         }
      } else if (var1.fragmentsStack.size() <= 1) {
         this.onFinish();
         this.finish();
         return false;
      }

      return true;
   }

   public void needLayout() {
      if (AndroidUtilities.isTablet()) {
         LayoutParams var1 = (LayoutParams)this.layersActionBarLayout.getLayoutParams();
         var1.leftMargin = (AndroidUtilities.displaySize.x - var1.width) / 2;
         int var2;
         if (VERSION.SDK_INT >= 21) {
            var2 = AndroidUtilities.statusBarHeight;
         } else {
            var2 = 0;
         }

         var1.topMargin = var2 + (AndroidUtilities.displaySize.y - var1.height - var2) / 2;
         this.layersActionBarLayout.setLayoutParams(var1);
         if (AndroidUtilities.isSmallTablet() && this.getResources().getConfiguration().orientation != 2) {
            var1 = (LayoutParams)this.actionBarLayout.getLayoutParams();
            var1.width = -1;
            var1.height = -1;
            this.actionBarLayout.setLayoutParams(var1);
         } else {
            int var3 = AndroidUtilities.displaySize.x / 100 * 35;
            var2 = var3;
            if (var3 < AndroidUtilities.dp(320.0F)) {
               var2 = AndroidUtilities.dp(320.0F);
            }

            var1 = (LayoutParams)this.actionBarLayout.getLayoutParams();
            var1.width = var2;
            var1.height = -1;
            this.actionBarLayout.setLayoutParams(var1);
            if (AndroidUtilities.isSmallTablet() && this.actionBarLayout.fragmentsStack.size() == 2) {
               ((BaseFragment)this.actionBarLayout.fragmentsStack.get(1)).onPause();
               this.actionBarLayout.fragmentsStack.remove(1);
               this.actionBarLayout.showLastFragment();
            }
         }
      }

   }

   public boolean needPresentFragment(BaseFragment var1, boolean var2, boolean var3, ActionBarLayout var4) {
      return true;
   }

   public void onBackPressed() {
      if (this.passcodeView.getVisibility() == 0) {
         this.finish();
      } else {
         if (PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
         } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
         } else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
               this.layersActionBarLayout.onBackPressed();
            } else {
               this.actionBarLayout.onBackPressed();
            }
         } else {
            this.actionBarLayout.onBackPressed();
         }

      }
   }

   public void onConfigurationChanged(Configuration var1) {
      AndroidUtilities.checkDisplaySize(this, var1);
      super.onConfigurationChanged(var1);
      this.fixLayout();
   }

   protected void onCreate(Bundle var1) {
      ApplicationLoader.postInitApplication();
      this.requestWindowFeature(1);
      this.setTheme(2131624206);
      this.getWindow().setBackgroundDrawableResource(2131165891);
      if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
         try {
            this.getWindow().setFlags(8192, 8192);
         } catch (Exception var7) {
            FileLog.e((Throwable)var7);
         }
      }

      super.onCreate(var1);
      if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
         SharedConfig.lastPauseTime = ConnectionsManager.getInstance(UserConfig.selectedAccount).getCurrentTime();
      }

      int var3 = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (var3 > 0) {
         AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(var3);
      }

      Theme.createDialogsResources(this);
      Theme.createChatResources(this, false);
      this.actionBarLayout = new ActionBarLayout(this);
      this.drawerLayoutContainer = new DrawerLayoutContainer(this);
      this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
      this.setContentView(this.drawerLayoutContainer, new android.view.ViewGroup.LayoutParams(-1, -1));
      TileMode var5;
      if (AndroidUtilities.isTablet()) {
         this.getWindow().setSoftInputMode(16);
         RelativeLayout var2 = new RelativeLayout(this);
         this.drawerLayoutContainer.addView(var2);
         android.widget.FrameLayout.LayoutParams var4 = (android.widget.FrameLayout.LayoutParams)var2.getLayoutParams();
         var4.width = -1;
         var4.height = -1;
         var2.setLayoutParams(var4);
         this.backgroundTablet = new View(this);
         BitmapDrawable var12 = (BitmapDrawable)this.getResources().getDrawable(2131165338);
         var5 = TileMode.REPEAT;
         var12.setTileModeXY(var5, var5);
         this.backgroundTablet.setBackgroundDrawable(var12);
         var2.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
         var2.addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
         FrameLayout var13 = new FrameLayout(this);
         var13.setBackgroundColor(2130706432);
         var2.addView(var13, LayoutHelper.createRelative(-1, -1));
         var13.setOnTouchListener(new _$$Lambda$ExternalActionActivity$uCjV_BmhLs5mtl2FVWuHvttAbEQ(this));
         var13.setOnClickListener(_$$Lambda$ExternalActionActivity$u8HNxR8AzNk3ipgQhpw5QlDqvFw.INSTANCE);
         this.layersActionBarLayout = new ActionBarLayout(this);
         this.layersActionBarLayout.setRemoveActionBarExtraHeight(true);
         this.layersActionBarLayout.setBackgroundView(var13);
         this.layersActionBarLayout.setUseAlphaAnimations(true);
         this.layersActionBarLayout.setBackgroundResource(2131165322);
         ActionBarLayout var14 = this.layersActionBarLayout;
         short var11;
         if (AndroidUtilities.isSmallTablet()) {
            var11 = 528;
         } else {
            var11 = 700;
         }

         var2.addView(var14, LayoutHelper.createRelative(530, var11));
         this.layersActionBarLayout.init(layerFragmentsStack);
         this.layersActionBarLayout.setDelegate(this);
         this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
      } else {
         RelativeLayout var15 = new RelativeLayout(this);
         this.drawerLayoutContainer.addView(var15, LayoutHelper.createFrame(-1, -1.0F));
         this.backgroundTablet = new View(this);
         BitmapDrawable var8 = (BitmapDrawable)this.getResources().getDrawable(2131165338);
         var5 = TileMode.REPEAT;
         var8.setTileModeXY(var5, var5);
         this.backgroundTablet.setBackgroundDrawable(var8);
         var15.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
         var15.addView(this.actionBarLayout, LayoutHelper.createRelative(-1, -1));
      }

      this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
      this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
      this.actionBarLayout.init(mainFragmentsStack);
      this.actionBarLayout.setDelegate(this);
      this.passcodeView = new PasscodeView(this);
      this.drawerLayoutContainer.addView(this.passcodeView, LayoutHelper.createFrame(-1, -1.0F));
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
      this.actionBarLayout.removeAllFragments();
      ActionBarLayout var9 = this.layersActionBarLayout;
      if (var9 != null) {
         var9.removeAllFragments();
      }

      Intent var10 = this.getIntent();
      boolean var6;
      if (var1 != null) {
         var6 = true;
      } else {
         var6 = false;
      }

      this.handleIntent(var10, false, var6, false, UserConfig.selectedAccount, 0);
      this.needLayout();
   }

   protected void onDestroy() {
      super.onDestroy();
      this.onFinish();
   }

   public void onFinishLogin() {
      this.handleIntent(this.passcodeSaveIntent, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true, this.passcodeSaveIntentAccount, this.passcodeSaveIntentState);
      this.actionBarLayout.removeAllFragments();
      ActionBarLayout var1 = this.layersActionBarLayout;
      if (var1 != null) {
         var1.removeAllFragments();
      }

      View var2 = this.backgroundTablet;
      if (var2 != null) {
         var2.setVisibility(0);
      }

   }

   public void onLowMemory() {
      super.onLowMemory();
      this.actionBarLayout.onLowMemory();
      if (AndroidUtilities.isTablet()) {
         this.layersActionBarLayout.onLowMemory();
      }

   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.handleIntent(var1, true, false, false, UserConfig.selectedAccount, 0);
   }

   protected void onPause() {
      super.onPause();
      this.actionBarLayout.onPause();
      if (AndroidUtilities.isTablet()) {
         this.layersActionBarLayout.onPause();
      }

      ApplicationLoader.externalInterfacePaused = true;
      this.onPasscodePause();
      PasscodeView var1 = this.passcodeView;
      if (var1 != null) {
         var1.onPause();
      }

   }

   public boolean onPreIme() {
      return false;
   }

   public void onRebuildAllFragments(ActionBarLayout var1, boolean var2) {
      if (AndroidUtilities.isTablet() && var1 == this.layersActionBarLayout) {
         this.actionBarLayout.rebuildAllFragmentViews(var2, var2);
      }

   }

   protected void onResume() {
      super.onResume();
      this.actionBarLayout.onResume();
      if (AndroidUtilities.isTablet()) {
         this.layersActionBarLayout.onResume();
      }

      ApplicationLoader.externalInterfacePaused = false;
      this.onPasscodeResume();
      if (this.passcodeView.getVisibility() != 0) {
         this.actionBarLayout.onResume();
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.onResume();
         }
      } else {
         this.actionBarLayout.dismissDialogs();
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.dismissDialogs();
         }

         this.passcodeView.onResume();
      }

   }

   public void presentFragment(BaseFragment var1) {
      this.actionBarLayout.presentFragment(var1);
   }

   public boolean presentFragment(BaseFragment var1, boolean var2, boolean var3) {
      return this.actionBarLayout.presentFragment(var1, var2, var3, true, false);
   }

   public void switchToAccount(int var1) {
      int var2 = UserConfig.selectedAccount;
      if (var1 != var2) {
         ConnectionsManager.getInstance(var2).setAppPaused(true, false);
         UserConfig.selectedAccount = var1;
         UserConfig.getInstance(0).saveConfig(false);
         if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(UserConfig.selectedAccount).setAppPaused(false, false);
         }

      }
   }
}
