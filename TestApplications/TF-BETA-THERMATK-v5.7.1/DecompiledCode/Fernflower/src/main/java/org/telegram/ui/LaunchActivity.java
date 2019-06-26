package org.telegram.ui;

import android.app.Activity;
import android.app.ActivityManager.TaskDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StatFs;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.core.view.inputmethod.InputContentInfoCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.LocationController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.camera.CameraController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.DrawerLayoutContainer;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Adapters.DrawerLayoutAdapter;
import org.telegram.ui.Cells.DrawerAddCell;
import org.telegram.ui.Cells.DrawerUserCell;
import org.telegram.ui.Cells.LanguageCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BlockingUpdateView;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.JoinGroupAlert;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PasscodeView;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.TermsOfServiceView;
import org.telegram.ui.Components.ThemeEditorView;
import org.telegram.ui.Components.UpdateAppAlertDialog;

public class LaunchActivity extends Activity implements ActionBarLayout.ActionBarLayoutDelegate, NotificationCenter.NotificationCenterDelegate, DialogsActivity.DialogsActivityDelegate {
   private static ArrayList layerFragmentsStack = new ArrayList();
   private static ArrayList mainFragmentsStack = new ArrayList();
   private static ArrayList rightFragmentsStack = new ArrayList();
   private ActionBarLayout actionBarLayout;
   private View backgroundTablet;
   private BlockingUpdateView blockingUpdateView;
   private ArrayList contactsToSend;
   private Uri contactsToSendUri;
   private int currentAccount;
   private int currentConnectionState;
   private String documentsMimeType;
   private ArrayList documentsOriginalPathsArray;
   private ArrayList documentsPathsArray;
   private ArrayList documentsUrisArray;
   private DrawerLayoutAdapter drawerLayoutAdapter;
   protected DrawerLayoutContainer drawerLayoutContainer;
   private HashMap englishLocaleStrings;
   private boolean finished;
   private ActionBarLayout layersActionBarLayout;
   private boolean loadingLocaleDialog;
   private AlertDialog localeDialog;
   private final Pattern locationRegex = Pattern.compile("geo: ?(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)(,|\\?z=)(-?\\d+)");
   private Runnable lockRunnable;
   private OnGlobalLayoutListener onGlobalLayoutListener;
   private Intent passcodeSaveIntent;
   private boolean passcodeSaveIntentIsNew;
   private boolean passcodeSaveIntentIsRestore;
   private PasscodeView passcodeView;
   private ArrayList photoPathsArray;
   private AlertDialog proxyErrorDialog;
   private ActionBarLayout rightActionBarLayout;
   private Location sendingLocation;
   private String sendingText;
   private FrameLayout shadowTablet;
   private FrameLayout shadowTabletSide;
   private RecyclerListView sideMenu;
   private HashMap systemLocaleStrings;
   private boolean tabletFullSize;
   private TermsOfServiceView termsOfServiceView;
   private String videoPath;
   private ActionMode visibleActionMode;
   private AlertDialog visibleDialog;

   private void checkCurrentAccount() {
      int var1 = this.currentAccount;
      if (var1 != UserConfig.selectedAccount) {
         NotificationCenter.getInstance(var1).removeObserver(this, NotificationCenter.appDidLogout);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
         NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
      }

      this.currentAccount = UserConfig.selectedAccount;
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.appDidLogout);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.mainUserInfoChanged);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdateConnectionState);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.needShowAlert);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.openArticle);
      NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.hasNewContactsToImport);
      this.updateCurrentConnectionState(this.currentAccount);
   }

   private void checkFreeDiscSpace() {
      if (VERSION.SDK_INT < 26) {
         Utilities.globalQueue.postRunnable(new _$$Lambda$LaunchActivity$OhtB5MFVTEjvucCdsB03AKPPH2Y(this), 2000L);
      }
   }

   private void checkLayout() {
      if (AndroidUtilities.isTablet() && this.rightActionBarLayout != null) {
         boolean var1 = AndroidUtilities.isInMultiwindow;
         byte var2 = 0;
         byte var3 = 0;
         BaseFragment var4;
         View var5;
         if (!var1 && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
            this.tabletFullSize = false;
            if (this.actionBarLayout.fragmentsStack.size() >= 2) {
               while(1 < this.actionBarLayout.fragmentsStack.size()) {
                  var4 = (BaseFragment)this.actionBarLayout.fragmentsStack.get(1);
                  if (var4 instanceof ChatActivity) {
                     ((ChatActivity)var4).setIgnoreAttachOnPause(true);
                  }

                  var4.onPause();
                  this.actionBarLayout.fragmentsStack.remove(1);
                  this.rightActionBarLayout.fragmentsStack.add(var4);
               }

               if (this.passcodeView.getVisibility() != 0) {
                  this.actionBarLayout.showLastFragment();
                  this.rightActionBarLayout.showLastFragment();
               }
            }

            ActionBarLayout var6 = this.rightActionBarLayout;
            if (var6.fragmentsStack.isEmpty()) {
               var2 = 8;
            } else {
               var2 = 0;
            }

            var6.setVisibility(var2);
            var5 = this.backgroundTablet;
            if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
               var2 = 0;
            } else {
               var2 = 8;
            }

            var5.setVisibility(var2);
            FrameLayout var7 = this.shadowTabletSide;
            if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
               var2 = var3;
            } else {
               var2 = 8;
            }

            var7.setVisibility(var2);
         } else {
            this.tabletFullSize = true;
            if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
               while(this.rightActionBarLayout.fragmentsStack.size() > 0) {
                  var4 = (BaseFragment)this.rightActionBarLayout.fragmentsStack.get(0);
                  if (var4 instanceof ChatActivity) {
                     ((ChatActivity)var4).setIgnoreAttachOnPause(true);
                  }

                  var4.onPause();
                  this.rightActionBarLayout.fragmentsStack.remove(0);
                  this.actionBarLayout.fragmentsStack.add(var4);
               }

               if (this.passcodeView.getVisibility() != 0) {
                  this.actionBarLayout.showLastFragment();
               }
            }

            this.shadowTabletSide.setVisibility(8);
            this.rightActionBarLayout.setVisibility(8);
            var5 = this.backgroundTablet;
            if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
               var2 = 8;
            }

            var5.setVisibility(var2);
         }
      }

   }

   private String getStringForLanguageAlert(HashMap var1, String var2, int var3) {
      String var4 = (String)var1.get(var2);
      String var5 = var4;
      if (var4 == null) {
         var5 = LocaleController.getString(var2, var3);
      }

      return var5;
   }

   private boolean handleIntent(Intent param1, boolean param2, boolean param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   // $FF: synthetic method
   static void lambda$didReceivedNotification$40(int var0, DialogInterface var1, int var2) {
      if (!mainFragmentsStack.isEmpty()) {
         MessagesController var4 = MessagesController.getInstance(var0);
         ArrayList var3 = mainFragmentsStack;
         var4.openByUserName("spambot", (BaseFragment)var3.get(var3.size() - 1), 1);
      }

   }

   // $FF: synthetic method
   static void lambda$didReceivedNotification$44(int var0, HashMap var1, boolean var2, boolean var3, DialogInterface var4, int var5) {
      ContactsController.getInstance(var0).syncPhoneBookByAlert(var1, var2, var3, false);
   }

   // $FF: synthetic method
   static void lambda$didReceivedNotification$45(int var0, HashMap var1, boolean var2, boolean var3, DialogInterface var4, int var5) {
      ContactsController.getInstance(var0).syncPhoneBookByAlert(var1, var2, var3, true);
   }

   // $FF: synthetic method
   static void lambda$didReceivedNotification$46(int var0, HashMap var1, boolean var2, boolean var3, DialogInterface var4, int var5) {
      ContactsController.getInstance(var0).syncPhoneBookByAlert(var1, var2, var3, true);
   }

   // $FF: synthetic method
   static void lambda$null$42(HashMap var0, int var1, TLRPC.MessageMedia var2, int var3) {
      Iterator var5 = var0.entrySet().iterator();

      while(var5.hasNext()) {
         MessageObject var4 = (MessageObject)((Entry)var5.next()).getValue();
         SendMessagesHelper.getInstance(var1).sendMessage((TLRPC.MessageMedia)var2, var4.getDialogId(), var4, (TLRPC.ReplyMarkup)null, (HashMap)null);
      }

   }

   // $FF: synthetic method
   static void lambda$null$6(int[] var0, long var1, TLRPC.MessageMedia var3, int var4) {
      SendMessagesHelper.getInstance(var0[0]).sendMessage((TLRPC.MessageMedia)var3, var1, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
   }

   // $FF: synthetic method
   static void lambda$onCreate$1(View var0) {
   }

   // $FF: synthetic method
   static void lambda$onCreate$3(View var0) {
      int var1 = var0.getMeasuredHeight();
      int var2 = var1;
      if (VERSION.SDK_INT >= 21) {
         var2 = var1 - AndroidUtilities.statusBarHeight;
      }

      if (var2 > AndroidUtilities.dp(100.0F) && var2 < AndroidUtilities.displaySize.y) {
         var1 = AndroidUtilities.dp(100.0F);
         Point var3 = AndroidUtilities.displaySize;
         if (var1 + var2 > var3.y) {
            var3.y = var2;
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var4 = new StringBuilder();
               var4.append("fix display size y to ");
               var4.append(AndroidUtilities.displaySize.y);
               FileLog.d(var4.toString());
            }
         }
      }

   }

   // $FF: synthetic method
   static void lambda$onPause$38() {
      ApplicationLoader.mainInterfacePausedStageQueue = true;
      ApplicationLoader.mainInterfacePausedStageQueueTime = 0L;
   }

   // $FF: synthetic method
   static void lambda$onResume$39() {
      ApplicationLoader.mainInterfacePausedStageQueue = false;
      ApplicationLoader.mainInterfacePausedStageQueueTime = System.currentTimeMillis();
   }

   // $FF: synthetic method
   static void lambda$runLinkRequest$33(int var0, int[] var1, DialogInterface var2) {
      ConnectionsManager.getInstance(var0).cancelRequest(var1[0], true);
   }

   // $FF: synthetic method
   static void lambda$showLanguageAlertInternal$49(LocaleController.LocaleInfo[] var0, LanguageCell[] var1, View var2) {
      Integer var3 = (Integer)var2.getTag();
      var0[0] = ((LanguageCell)var2).getCurrentLocale();

      for(int var4 = 0; var4 < var1.length; ++var4) {
         LanguageCell var6 = var1[var4];
         boolean var5;
         if (var4 == var3) {
            var5 = true;
         } else {
            var5 = false;
         }

         var6.setLanguageSelected(var5);
      }

   }

   private void onFinish() {
      if (!this.finished) {
         this.finished = true;
         Runnable var1 = this.lockRunnable;
         if (var1 != null) {
            AndroidUtilities.cancelRunOnUIThread(var1);
            this.lockRunnable = null;
         }

         int var2 = this.currentAccount;
         if (var2 != -1) {
            NotificationCenter.getInstance(var2).removeObserver(this, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.mainUserInfoChanged);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdateConnectionState);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.needShowAlert);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.wasUnableToFindCurrentLocation);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.openArticle);
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.hasNewContactsToImport);
         }

         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needShowAlert);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.suggestedLangpack);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.reloadInterface);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewTheme);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.needSetDayNightTheme);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.closeOtherAppActivities);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetPasscode);
         NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.notificationsCountUpdated);
      }
   }

   private void onPasscodePause() {
      Runnable var1 = this.lockRunnable;
      if (var1 != null) {
         AndroidUtilities.cancelRunOnUIThread(var1);
         this.lockRunnable = null;
      }

      if (SharedConfig.passcodeHash.length() != 0) {
         SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
         this.lockRunnable = new Runnable() {
            public void run() {
               if (LaunchActivity.this.lockRunnable == this) {
                  if (AndroidUtilities.needShowPasscode(true)) {
                     if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("lock app");
                     }

                     LaunchActivity.this.showPasscodeActivity();
                  } else if (BuildVars.LOGS_ENABLED) {
                     FileLog.d("didn't pass lock check");
                  }

                  LaunchActivity.this.lockRunnable = null;
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

   private void runLinkRequest(int var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, Integer var9, Integer var10, String var11, HashMap var12, String var13, String var14, String var15, TLRPC.TL_wallPaper var16, int var17) {
      if (var17 == 0 && UserConfig.getActivatedAccountsCount() >= 2 && var12 != null) {
         AlertsCreator.createAccountSelectDialog(this, new _$$Lambda$LaunchActivity$oQA0vgj3X1YdD8d67UmGyFJyjHY(this, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16)).show();
      } else {
         boolean var18 = true;
         if (var15 != null) {
            if (NotificationCenter.getGlobalInstance().hasObservers(NotificationCenter.didReceiveSmsCode)) {
               NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.didReceiveSmsCode, var15);
            } else {
               AlertDialog.Builder var43 = new AlertDialog.Builder(this);
               var43.setTitle(LocaleController.getString("AppName", 2131558635));
               var43.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("OtherLoginCode", 2131560130, var15)));
               var43.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               this.showAlertDialog(var43);
            }

         } else {
            AlertDialog var19 = new AlertDialog(this, 3);
            int[] var20 = new int[]{0};
            AlertDialog var24;
            if (var2 != null) {
               TLRPC.TL_contacts_resolveUsername var25 = new TLRPC.TL_contacts_resolveUsername();
               var25.username = var2;
               var20[0] = ConnectionsManager.getInstance(var1).sendRequest(var25, new _$$Lambda$LaunchActivity$RpMN6VrBiIorLulFIXuvXk8Fkc4(this, var19, var11, var1, var6, var5, var9));
               var24 = var19;
            } else if (var3 != null) {
               label98: {
                  if (var17 == 0) {
                     TLRPC.TL_messages_checkChatInvite var21 = new TLRPC.TL_messages_checkChatInvite();
                     var21.hash = var3;
                     var20[0] = ConnectionsManager.getInstance(var1).sendRequest(var21, new _$$Lambda$LaunchActivity$Y1VGiKNTbvASO61ltlphoI7h8RU(this, var19, var1, var3, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16), 2);
                  } else if (var17 == 1) {
                     TLRPC.TL_messages_importChatInvite var29 = new TLRPC.TL_messages_importChatInvite();
                     var29.hash = var3;
                     ConnectionsManager var28 = ConnectionsManager.getInstance(var1);
                     var24 = var19;
                     var28.sendRequest(var29, new _$$Lambda$LaunchActivity$Zfo5ch07dnPuGi7d59vdlVijeac(this, var1, var19), 2);
                     break label98;
                  }

                  var24 = var19;
               }
            } else {
               BaseFragment var35;
               if (var4 != null) {
                  if (!mainFragmentsStack.isEmpty()) {
                     TLRPC.TL_inputStickerSetShortName var42 = new TLRPC.TL_inputStickerSetShortName();
                     var42.short_name = var4;
                     ArrayList var37 = mainFragmentsStack;
                     var35 = (BaseFragment)var37.get(var37.size() - 1);
                     var35.showDialog(new StickersAlert(this, var35, var42, (TLRPC.TL_messages_stickerSet)null, (StickersAlert.StickersAlertDelegate)null));
                  }

                  return;
               }

               if (var7 != null) {
                  Bundle var26 = new Bundle();
                  var26.putBoolean("onlySelect", true);
                  DialogsActivity var27 = new DialogsActivity(var26);
                  var27.setDelegate(new _$$Lambda$LaunchActivity$RTwp0QB0Dkt4irgon9YXmRC5sCk(this, var8, var1, var7));
                  this.presentFragment(var27, false, true);
                  var24 = var19;
               } else if (var12 != null) {
                  var17 = Utilities.parseInt((String)var12.get("bot_id"));
                  if (var17 == 0) {
                     return;
                  }

                  var2 = (String)var12.get("payload");
                  var4 = (String)var12.get("nonce");
                  var3 = (String)var12.get("callback_url");
                  TLRPC.TL_account_getAuthorizationForm var38 = new TLRPC.TL_account_getAuthorizationForm();
                  var38.bot_id = var17;
                  var38.scope = (String)var12.get("scope");
                  var38.public_key = (String)var12.get("public_key");
                  var20[0] = ConnectionsManager.getInstance(var1).sendRequest(var38, new _$$Lambda$LaunchActivity$Art3qkoRK5cBs4Y4xN6A8ukcezg(this, var20, var1, var19, var38, var2, var4, var3));
                  var24 = var19;
               } else if (var14 != null) {
                  TLRPC.TL_help_getDeepLinkInfo var32 = new TLRPC.TL_help_getDeepLinkInfo();
                  var32.path = var14;
                  var20[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var32, new _$$Lambda$LaunchActivity$qH1w5GBYl7awDhwaFS3FGWwAWCs(this, var19));
                  var24 = var19;
               } else if (var13 != null) {
                  TLRPC.TL_langpack_getLanguage var36 = new TLRPC.TL_langpack_getLanguage();
                  var36.lang_code = var13;
                  var36.lang_pack = "android";
                  var20[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var36, new _$$Lambda$LaunchActivity$BzXeKQ_qpCSdfuqJJ22744bcPM0(this, var19));
                  var24 = var19;
               } else if (var16 != null) {
                  boolean var44;
                  label80: {
                     if (TextUtils.isEmpty(var16.slug)) {
                        label78: {
                           try {
                              WallpapersListActivity.ColorWallpaper var30 = new WallpapersListActivity.ColorWallpaper(-100L, var16.settings.background_color);
                              WallpaperActivity var39 = new WallpaperActivity(var30, (Bitmap)null);
                              _$$Lambda$LaunchActivity$zROn3pMkFlHNUe2IsaYIfrrTR3o var31 = new _$$Lambda$LaunchActivity$zROn3pMkFlHNUe2IsaYIfrrTR3o(this, var39);
                              AndroidUtilities.runOnUIThread(var31);
                           } catch (Exception var23) {
                              FileLog.e((Throwable)var23);
                              break label78;
                           }

                           var44 = var18;
                           break label80;
                        }
                     }

                     var44 = false;
                  }

                  var24 = var19;
                  if (!var44) {
                     TLRPC.TL_account_getWallPaper var34 = new TLRPC.TL_account_getWallPaper();
                     TLRPC.TL_inputWallPaperSlug var40 = new TLRPC.TL_inputWallPaperSlug();
                     var40.slug = var16.slug;
                     var34.wallpaper = var40;
                     var20[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var34, new _$$Lambda$LaunchActivity$KNg5IaCFYbOpTWjqZaxL1vwTVgk(this, var19, var16));
                     var24 = var19;
                  }
               } else {
                  var24 = var19;
                  if (var10 != null) {
                     var24 = var19;
                     if (var9 != null) {
                        label106: {
                           Bundle var33 = new Bundle();
                           var33.putInt("chat_id", var10);
                           var33.putInt("message_id", var9);
                           if (!mainFragmentsStack.isEmpty()) {
                              ArrayList var41 = mainFragmentsStack;
                              var35 = (BaseFragment)var41.get(var41.size() - 1);
                           } else {
                              var35 = null;
                           }

                           if (var35 != null) {
                              var24 = var19;
                              if (!MessagesController.getInstance(var1).checkCanOpenChat(var33, var35)) {
                                 break label106;
                              }
                           }

                           AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$PRISru_yyJ8MquHeYUjmNXD8lxM(this, var33, var10, var20, var19, var35, var1));
                           var24 = var19;
                        }
                     }
                  }
               }
            }

            if (var20[0] != 0) {
               var24.setOnCancelListener(new _$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM(var1, var20));

               try {
                  var24.show();
               } catch (Exception var22) {
               }
            }

         }
      }
   }

   private void showLanguageAlert(boolean var1) {
      Exception var10000;
      label163: {
         boolean var10001;
         try {
            if (this.loadingLocaleDialog || ApplicationLoader.mainInterfacePaused) {
               return;
            }
         } catch (Exception var18) {
            var10000 = var18;
            var10001 = false;
            break label163;
         }

         String var2;
         String var3;
         try {
            var2 = MessagesController.getGlobalMainSettings().getString("language_showed2", "");
            var3 = MessagesController.getInstance(this.currentAccount).suggestedLangCode;
         } catch (Exception var17) {
            var10000 = var17;
            var10001 = false;
            break label163;
         }

         if (!var1) {
            try {
               if (var2.equals(var3)) {
                  if (BuildVars.LOGS_ENABLED) {
                     StringBuilder var25 = new StringBuilder();
                     var25.append("alert already showed for ");
                     var25.append(var2);
                     FileLog.d(var25.toString());
                  }

                  return;
               }
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label163;
            }
         }

         String var4;
         LocaleController.LocaleInfo[] var5;
         label143: {
            try {
               var5 = new LocaleController.LocaleInfo[2];
               if (var3.contains("-")) {
                  var4 = var3.split("-")[0];
                  break label143;
               }
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label163;
            }

            var4 = var3;
         }

         label160: {
            label133: {
               try {
                  if (!"in".equals(var4)) {
                     break label133;
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label163;
               }

               var2 = "id";
               break label160;
            }

            label161: {
               try {
                  if ("iw".equals(var4)) {
                     break label161;
                  }
               } catch (Exception var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label163;
               }

               label121: {
                  try {
                     if ("jw".equals(var4)) {
                        break label121;
                     }
                  } catch (Exception var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label163;
                  }

                  var2 = null;
                  break label160;
               }

               var2 = "jv";
               break label160;
            }

            var2 = "he";
         }

         int var6 = 0;

         while(true) {
            LocaleController.LocaleInfo var7;
            label111: {
               try {
                  if (var6 >= LocaleController.getInstance().languages.size()) {
                     break;
                  }

                  var7 = (LocaleController.LocaleInfo)LocaleController.getInstance().languages.get(var6);
                  if (!var7.shortName.equals("en")) {
                     break label111;
                  }
               } catch (Exception var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label163;
               }

               var5[0] = var7;
            }

            label105: {
               try {
                  if (!var7.shortName.replace("_", "-").equals(var3) && !var7.shortName.equals(var4) && !var7.shortName.equals(var2)) {
                     break label105;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label163;
               }

               var5[1] = var7;
            }

            if (var5[0] != null && var5[1] != null) {
               break;
            }

            ++var6;
         }

         if (var5[0] == null || var5[1] == null || var5[0] == var5[1]) {
            return;
         }

         try {
            if (BuildVars.LOGS_ENABLED) {
               StringBuilder var19 = new StringBuilder();
               var19.append("show lang alert for ");
               var19.append(var5[0].getKey());
               var19.append(" and ");
               var19.append(var5[1].getKey());
               FileLog.d(var19.toString());
            }
         } catch (Exception var9) {
            var10000 = var9;
            var10001 = false;
            break label163;
         }

         try {
            this.systemLocaleStrings = null;
            this.englishLocaleStrings = null;
            this.loadingLocaleDialog = true;
            TLRPC.TL_langpack_getStrings var22 = new TLRPC.TL_langpack_getStrings();
            var22.lang_code = var5[1].getLangCode();
            var22.keys.add("English");
            var22.keys.add("ChooseYourLanguage");
            var22.keys.add("ChooseYourLanguageOther");
            var22.keys.add("ChangeLanguageLater");
            ConnectionsManager var26 = ConnectionsManager.getInstance(this.currentAccount);
            _$$Lambda$LaunchActivity$zUSUlC8LSla6kKMHoMSEb1G2TkE var21 = new _$$Lambda$LaunchActivity$zUSUlC8LSla6kKMHoMSEb1G2TkE(this, var5, var3);
            var26.sendRequest(var22, var21, 8);
            TLRPC.TL_langpack_getStrings var23 = new TLRPC.TL_langpack_getStrings();
            var23.lang_code = var5[0].getLangCode();
            var23.keys.add("English");
            var23.keys.add("ChooseYourLanguage");
            var23.keys.add("ChooseYourLanguageOther");
            var23.keys.add("ChangeLanguageLater");
            var26 = ConnectionsManager.getInstance(this.currentAccount);
            _$$Lambda$LaunchActivity$3j0ynD_14Ne6162eLDw5z0libqA var24 = new _$$Lambda$LaunchActivity$3j0ynD_14Ne6162eLDw5z0libqA(this, var5, var3);
            var26.sendRequest(var23, var24, 8);
            return;
         } catch (Exception var8) {
            var10000 = var8;
            var10001 = false;
         }
      }

      Exception var20 = var10000;
      FileLog.e((Throwable)var20);
   }

   private void showLanguageAlertInternal(LocaleController.LocaleInfo var1, LocaleController.LocaleInfo var2, String var3) {
      Exception var10000;
      label103: {
         boolean var10001;
         try {
            this.loadingLocaleDialog = false;
         } catch (Exception var21) {
            var10000 = var21;
            var10001 = false;
            break label103;
         }

         boolean var4;
         label96: {
            label95: {
               try {
                  if (!var1.builtIn && !LocaleController.getInstance().isCurrentLocalLocale()) {
                     break label95;
                  }
               } catch (Exception var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label103;
               }

               var4 = true;
               break label96;
            }

            var4 = false;
         }

         AlertDialog.Builder var5;
         LinearLayout var6;
         LanguageCell[] var7;
         LocaleController.LocaleInfo[] var8;
         LocaleController.LocaleInfo[] var9;
         String var10;
         try {
            var5 = new AlertDialog.Builder(this);
            var5.setTitle(this.getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguage", 2131559098));
            var5.setSubtitle(this.getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguage", 2131559098));
            var6 = new LinearLayout(this);
            var6.setOrientation(1);
            var7 = new LanguageCell[2];
            var8 = new LocaleController.LocaleInfo[1];
            var9 = new LocaleController.LocaleInfo[2];
            var10 = this.getStringForLanguageAlert(this.systemLocaleStrings, "English", 2131559365);
         } catch (Exception var19) {
            var10000 = var19;
            var10001 = false;
            break label103;
         }

         LocaleController.LocaleInfo var11;
         if (var4) {
            var11 = var1;
         } else {
            var11 = var2;
         }

         var9[0] = var11;
         if (var4) {
            var11 = var2;
         } else {
            var11 = var1;
         }

         var9[1] = var11;
         if (!var4) {
            var1 = var2;
         }

         var8[0] = var1;

         LanguageCell var22;
         for(int var28 = 0; var28 < 2; ++var28) {
            try {
               var22 = new LanguageCell(this, true);
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label103;
            }

            var7[var28] = var22;
            LanguageCell var29 = var7[var28];
            LocaleController.LocaleInfo var12 = var9[var28];
            String var24;
            if (var9[var28] == var2) {
               var24 = var10;
            } else {
               var24 = null;
            }

            try {
               var29.setLanguage(var12, var24, true);
               var7[var28].setTag(var28);
               var7[var28].setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor("dialogButtonSelector"), 2));
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label103;
            }

            var22 = var7[var28];
            boolean var13;
            if (var28 == 0) {
               var13 = true;
            } else {
               var13 = false;
            }

            try {
               var22.setLanguageSelected(var13);
               var6.addView(var7[var28], LayoutHelper.createLinear(-1, 50));
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label103;
            }

            var22 = var7[var28];

            try {
               _$$Lambda$LaunchActivity$xW_6R4h9aa4jorowKHJF_yJiIQM var30 = new _$$Lambda$LaunchActivity$xW_6R4h9aa4jorowKHJF_yJiIQM(var8, var7);
               var22.setOnClickListener(var30);
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label103;
            }
         }

         try {
            var22 = new LanguageCell(this, true);
            var22.setValue(this.getStringForLanguageAlert(this.systemLocaleStrings, "ChooseYourLanguageOther", 2131559099), this.getStringForLanguageAlert(this.englishLocaleStrings, "ChooseYourLanguageOther", 2131559099));
            _$$Lambda$LaunchActivity$H9lUoWlciprQBKCCa3uBhilp70I var23 = new _$$Lambda$LaunchActivity$H9lUoWlciprQBKCCa3uBhilp70I(this);
            var22.setOnClickListener(var23);
            var6.addView(var22, LayoutHelper.createLinear(-1, 50));
            var5.setView(var6);
            String var25 = LocaleController.getString("OK", 2131560097);
            _$$Lambda$LaunchActivity$__OYvDrFInFakn0WORCpq62kH08 var27 = new _$$Lambda$LaunchActivity$__OYvDrFInFakn0WORCpq62kH08(this, var8);
            var5.setNegativeButton(var25, var27);
            this.localeDialog = this.showAlertDialog(var5);
            MessagesController.getGlobalMainSettings().edit().putString("language_showed2", var3).commit();
            return;
         } catch (Exception var14) {
            var10000 = var14;
            var10001 = false;
         }
      }

      Exception var26 = var10000;
      FileLog.e((Throwable)var26);
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
         this.passcodeView.setDelegate(new _$$Lambda$LaunchActivity$V1zg09F7Jz3e7nYXLjhnV60E_O8(this));
         this.actionBarLayout.setVisibility(4);
         if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
               this.layersActionBarLayout.setVisibility(4);
            }

            this.rightActionBarLayout.setVisibility(4);
         }

      }
   }

   private void showTosActivity(int var1, TLRPC.TL_help_termsOfService var2) {
      if (this.termsOfServiceView == null) {
         this.termsOfServiceView = new TermsOfServiceView(this);
         this.drawerLayoutContainer.addView(this.termsOfServiceView, LayoutHelper.createFrame(-1, -1.0F));
         this.termsOfServiceView.setDelegate(new TermsOfServiceView.TermsOfServiceViewDelegate() {
            public void onAcceptTerms(int var1) {
               UserConfig.getInstance(var1).unacceptedTermsOfService = null;
               UserConfig.getInstance(var1).saveConfig(false);
               LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
               LaunchActivity.this.termsOfServiceView.setVisibility(8);
            }

            public void onDeclineTerms(int var1) {
               LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
               LaunchActivity.this.termsOfServiceView.setVisibility(8);
            }
         });
      }

      TLRPC.TL_help_termsOfService var3 = UserConfig.getInstance(var1).unacceptedTermsOfService;
      if (var3 != var2 && (var3 == null || !var3.id.data.equals(var2.id.data))) {
         UserConfig.getInstance(var1).unacceptedTermsOfService = var2;
         UserConfig.getInstance(var1).saveConfig(false);
      }

      this.termsOfServiceView.show(var1, var2);
      this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
   }

   private void showUpdateActivity(int var1, TLRPC.TL_help_appUpdate var2) {
      if (this.blockingUpdateView == null) {
         this.blockingUpdateView = new BlockingUpdateView(this) {
            public void setVisibility(int var1) {
               super.setVisibility(var1);
               if (var1 == 8) {
                  LaunchActivity.this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
               }

            }
         };
         this.drawerLayoutContainer.addView(this.blockingUpdateView, LayoutHelper.createFrame(-1, -1.0F));
      }

      this.blockingUpdateView.show(var1, var2);
      this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
   }

   private void switchToAvailableAccountOrLogout() {
      int var1 = 0;

      while(true) {
         if (var1 >= 3) {
            var1 = -1;
            break;
         }

         if (UserConfig.getInstance(var1).isClientActivated()) {
            break;
         }

         ++var1;
      }

      TermsOfServiceView var2 = this.termsOfServiceView;
      if (var2 != null) {
         var2.setVisibility(8);
      }

      if (var1 != -1) {
         this.switchToAccount(var1, true);
      } else {
         DrawerLayoutAdapter var3 = this.drawerLayoutAdapter;
         if (var3 != null) {
            var3.notifyDataSetChanged();
         }

         Iterator var4 = this.actionBarLayout.fragmentsStack.iterator();

         while(var4.hasNext()) {
            ((BaseFragment)var4.next()).onFragmentDestroy();
         }

         this.actionBarLayout.fragmentsStack.clear();
         if (AndroidUtilities.isTablet()) {
            var4 = this.layersActionBarLayout.fragmentsStack.iterator();

            while(var4.hasNext()) {
               ((BaseFragment)var4.next()).onFragmentDestroy();
            }

            this.layersActionBarLayout.fragmentsStack.clear();
            var4 = this.rightActionBarLayout.fragmentsStack.iterator();

            while(var4.hasNext()) {
               ((BaseFragment)var4.next()).onFragmentDestroy();
            }

            this.rightActionBarLayout.fragmentsStack.clear();
         }

         this.startActivity(new Intent(this, IntroActivity.class));
         this.onFinish();
         this.finish();
      }

   }

   private void updateCurrentConnectionState(int var1) {
      if (this.actionBarLayout != null) {
         var1 = 0;
         this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
         int var2 = this.currentConnectionState;
         _$$Lambda$LaunchActivity$zB6R9P65ZPbtKEyhZOKuv4q935o var3 = null;
         String var4;
         if (var2 == 2) {
            var1 = 2131561102;
            var4 = "WaitingForNetwork";
         } else if (var2 == 5) {
            var1 = 2131560962;
            var4 = "Updating";
         } else if (var2 == 4) {
            var1 = 2131559139;
            var4 = "ConnectingToProxy";
         } else if (var2 == 1) {
            var1 = 2131559137;
            var4 = "Connecting";
         } else {
            var4 = null;
         }

         var2 = this.currentConnectionState;
         if (var2 == 1 || var2 == 4) {
            var3 = new _$$Lambda$LaunchActivity$zB6R9P65ZPbtKEyhZOKuv4q935o(this);
         }

         this.actionBarLayout.setTitleOverlayText(var4, var1, var3);
      }
   }

   public void checkAppUpdate(boolean var1) {
      if ((var1 || !BuildVars.DEBUG_VERSION) && (var1 || BuildVars.CHECK_UPDATES)) {
         if (var1 || Math.abs(System.currentTimeMillis() - UserConfig.getInstance(0).lastUpdateCheckTime) >= 86400000L) {
            TLRPC.TL_help_getAppUpdate var2 = new TLRPC.TL_help_getAppUpdate();

            try {
               var2.source = ApplicationLoader.applicationContext.getPackageManager().getInstallerPackageName(ApplicationLoader.applicationContext.getPackageName());
            } catch (Exception var5) {
            }

            if (var2.source == null) {
               var2.source = "";
            }

            int var3 = this.currentAccount;
            ConnectionsManager.getInstance(var3).sendRequest(var2, new _$$Lambda$LaunchActivity$1W6W_t_pAiSBx5i38jWX5eUMkro(this, var3));
         }
      }
   }

   public void didReceivedNotification(int var1, int var2, Object... var3) {
      if (var1 == NotificationCenter.appDidLogout) {
         this.switchToAvailableAccountOrLogout();
      } else {
         int var4 = NotificationCenter.closeOtherAppActivities;
         byte var5 = 0;
         boolean var6 = false;
         if (var1 == var4) {
            if (var3[0] != this) {
               this.onFinish();
               this.finish();
            }
         } else if (var1 == NotificationCenter.didUpdateConnectionState) {
            var1 = ConnectionsManager.getInstance(var2).getConnectionState();
            if (this.currentConnectionState != var1) {
               if (BuildVars.LOGS_ENABLED) {
                  StringBuilder var13 = new StringBuilder();
                  var13.append("switch to state ");
                  var13.append(var1);
                  FileLog.d(var13.toString());
               }

               this.currentConnectionState = var1;
               this.updateCurrentConnectionState(var2);
            }
         } else if (var1 == NotificationCenter.mainUserInfoChanged) {
            this.drawerLayoutAdapter.notifyDataSetChanged();
         } else {
            ArrayList var14;
            if (var1 == NotificationCenter.needShowAlert) {
               Integer var7 = (Integer)var3[0];
               if (var7 == 3 && this.proxyErrorDialog != null) {
                  return;
               }

               if (var7 == 4) {
                  this.showTosActivity(var2, (TLRPC.TL_help_termsOfService)var3[1]);
                  return;
               }

               AlertDialog.Builder var8 = new AlertDialog.Builder(this);
               var8.setTitle(LocaleController.getString("AppName", 2131558635));
               if (var7 != 2 && var7 != 3) {
                  var8.setNegativeButton(LocaleController.getString("MoreInfo", 2131559883), new _$$Lambda$LaunchActivity$sNXuL3LKkNwiULIaBmkQorXYhkE(var2));
               }

               if (var7 == 5) {
                  var8.setMessage(LocaleController.getString("NobodyLikesSpam3", 2131559959));
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               } else if (var7 == 0) {
                  var8.setMessage(LocaleController.getString("NobodyLikesSpam1", 2131559957));
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               } else if (var7 == 1) {
                  var8.setMessage(LocaleController.getString("NobodyLikesSpam2", 2131559958));
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
               } else if (var7 == 2) {
                  var8.setMessage((String)var3[1]);
                  if (((String)var3[2]).startsWith("AUTH_KEY_DROP_")) {
                     var8.setPositiveButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
                     var8.setNegativeButton(LocaleController.getString("LogOut", 2131559783), new _$$Lambda$LaunchActivity$KI45KCZymdE2FpdMIxAM9_1Ovt8(this));
                  } else {
                     var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  }
               } else if (var7 == 3) {
                  var8.setMessage(LocaleController.getString("UseProxyTelegramError", 2131560983));
                  var8.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  this.proxyErrorDialog = this.showAlertDialog(var8);
                  return;
               }

               if (!mainFragmentsStack.isEmpty()) {
                  var14 = mainFragmentsStack;
                  ((BaseFragment)var14.get(var14.size() - 1)).showDialog(var8.create());
               }
            } else {
               HashMap var23;
               if (var1 == NotificationCenter.wasUnableToFindCurrentLocation) {
                  var23 = (HashMap)var3[0];
                  AlertDialog.Builder var15 = new AlertDialog.Builder(this);
                  var15.setTitle(LocaleController.getString("AppName", 2131558635));
                  var15.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
                  var15.setNegativeButton(LocaleController.getString("ShareYouLocationUnableManually", 2131560758), new _$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64(this, var23, var2));
                  var15.setMessage(LocaleController.getString("ShareYouLocationUnable", 2131560757));
                  if (!mainFragmentsStack.isEmpty()) {
                     ArrayList var24 = mainFragmentsStack;
                     ((BaseFragment)var24.get(var24.size() - 1)).showDialog(var15.create());
                  }
               } else {
                  RecyclerListView var16;
                  if (var1 == NotificationCenter.didSetNewWallpapper) {
                     var16 = this.sideMenu;
                     if (var16 != null) {
                        View var17 = var16.getChildAt(0);
                        if (var17 != null) {
                           var17.invalidate();
                        }
                     }
                  } else if (var1 == NotificationCenter.didSetPasscode) {
                     if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
                        try {
                           this.getWindow().setFlags(8192, 8192);
                        } catch (Exception var12) {
                           FileLog.e((Throwable)var12);
                        }
                     } else if (!MediaController.getInstance().hasFlagSecureFragment()) {
                        try {
                           this.getWindow().clearFlags(8192);
                        } catch (Exception var11) {
                           FileLog.e((Throwable)var11);
                        }
                     }
                  } else {
                     boolean var9;
                     if (var1 == NotificationCenter.reloadInterface) {
                        var9 = var6;
                        if (mainFragmentsStack.size() > 1) {
                           var14 = mainFragmentsStack;
                           var9 = var6;
                           if (var14.get(var14.size() - 1) instanceof SettingsActivity) {
                              var9 = true;
                           }
                        }

                        this.rebuildAllFragments(var9);
                     } else if (var1 == NotificationCenter.suggestedLangpack) {
                        this.showLanguageAlert(false);
                     } else if (var1 == NotificationCenter.openArticle) {
                        if (mainFragmentsStack.isEmpty()) {
                           return;
                        }

                        ArticleViewer var25 = ArticleViewer.getInstance();
                        ArrayList var21 = mainFragmentsStack;
                        var25.setParentActivity(this, (BaseFragment)var21.get(var21.size() - 1));
                        ArticleViewer.getInstance().open((TLRPC.TL_webPage)var3[0], (String)var3[1]);
                     } else if (var1 == NotificationCenter.hasNewContactsToImport) {
                        ActionBarLayout var26 = this.actionBarLayout;
                        if (var26 == null || var26.fragmentsStack.isEmpty()) {
                           return;
                        }

                        (Integer)var3[0];
                        var23 = (HashMap)var3[1];
                        var9 = (Boolean)var3[2];
                        var6 = (Boolean)var3[3];
                        var14 = this.actionBarLayout.fragmentsStack;
                        BaseFragment var18 = (BaseFragment)var14.get(var14.size() - 1);
                        AlertDialog.Builder var22 = new AlertDialog.Builder(this);
                        var22.setTitle(LocaleController.getString("UpdateContactsTitle", 2131560953));
                        var22.setMessage(LocaleController.getString("UpdateContactsMessage", 2131560952));
                        var22.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LaunchActivity$V0ixus5t_k3sjeAzTAOnXbxIAXc(var2, var23, var9, var6));
                        var22.setNegativeButton(LocaleController.getString("Cancel", 2131558891), new _$$Lambda$LaunchActivity$lR7vjLvAzG_cCRS6htHMjvKKewI(var2, var23, var9, var6));
                        var22.setOnBackButtonListener(new _$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk(var2, var23, var9, var6));
                        AlertDialog var27 = var22.create();
                        var18.showDialog(var27);
                        var27.setCanceledOnTouchOutside(false);
                     } else if (var1 == NotificationCenter.didSetNewTheme) {
                        if (!(Boolean)var3[0]) {
                           var16 = this.sideMenu;
                           if (var16 != null) {
                              var16.setBackgroundColor(Theme.getColor("chats_menuBackground"));
                              this.sideMenu.setGlowColor(Theme.getColor("chats_menuBackground"));
                              this.sideMenu.setListSelectorColor(Theme.getColor("listSelectorSDK21"));
                              this.sideMenu.getAdapter().notifyDataSetChanged();
                           }

                           if (VERSION.SDK_INT >= 21) {
                              try {
                                 TaskDescription var19 = new TaskDescription((String)null, (Bitmap)null, Theme.getColor("actionBarDefault") | -16777216);
                                 this.setTaskDescription(var19);
                              } catch (Exception var10) {
                              }
                           }
                        }

                        this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
                     } else if (var1 == NotificationCenter.needSetDayNightTheme) {
                        Theme.ThemeInfo var28 = (Theme.ThemeInfo)var3[0];
                        var9 = (Boolean)var3[1];
                        this.actionBarLayout.animateThemedValues(var28, var9);
                        if (AndroidUtilities.isTablet()) {
                           this.layersActionBarLayout.animateThemedValues(var28, var9);
                           this.rightActionBarLayout.animateThemedValues(var28, var9);
                        }
                     } else if (var1 == NotificationCenter.notificationsCountUpdated) {
                        RecyclerListView var29 = this.sideMenu;
                        if (var29 != null) {
                           Integer var20 = (Integer)var3[0];
                           var2 = var29.getChildCount();

                           for(var1 = var5; var1 < var2; ++var1) {
                              View var30 = this.sideMenu.getChildAt(var1);
                              if (var30 instanceof DrawerUserCell && ((DrawerUserCell)var30).getAccountNumber() == var20) {
                                 var30.invalidate();
                                 break;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      byte var5 = 0;
      long var6 = (Long)var2.get(0);
      int var8 = (int)var6;
      int var9 = (int)(var6 >> 32);
      Bundle var14 = new Bundle();
      int var10;
      if (var1 != null) {
         var10 = var1.getCurrentAccount();
      } else {
         var10 = this.currentAccount;
      }

      var14.putBoolean("scrollToTopOnResume", true);
      if (!AndroidUtilities.isTablet()) {
         NotificationCenter.getInstance(var10).postNotificationName(NotificationCenter.closeChats);
      }

      if (var8 != 0) {
         if (var9 == 1) {
            var14.putInt("chat_id", var8);
         } else if (var8 > 0) {
            var14.putInt("user_id", var8);
         } else if (var8 < 0) {
            var14.putInt("chat_id", -var8);
         }
      } else {
         var14.putInt("enc_id", var9);
      }

      if (MessagesController.getInstance(var10).checkCanOpenChat(var14, var1)) {
         ChatActivity var16 = new ChatActivity(var14);
         var2 = this.contactsToSend;
         boolean var11;
         if (var2 != null && var2.size() == 1) {
            if (this.contactsToSend.size() == 1) {
               PhonebookShareActivity var17 = new PhonebookShareActivity((ContactsController.Contact)null, this.contactsToSendUri, (File)null, (String)null);
               var17.setDelegate(new _$$Lambda$LaunchActivity$Uzy9GRHh4_9QiCM66VVA1Ftqfj0(this, var16, var10, var6));
               ActionBarLayout var18 = this.actionBarLayout;
               if (var1 != null) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var1 == null) {
                  var11 = true;
               } else {
                  var11 = false;
               }

               var18.presentFragment(var17, var4, var11, true, false);
            }
         } else {
            ActionBarLayout var12 = this.actionBarLayout;
            if (var1 != null) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (var1 == null) {
               var11 = true;
            } else {
               var11 = false;
            }

            var2 = null;
            var12.presentFragment(var16, var4, var11, true, false);
            String var13 = this.videoPath;
            if (var13 != null) {
               var16.openVideoEditor(var13, this.sendingText);
               this.sendingText = var2;
            }

            if (this.photoPathsArray != null) {
               var13 = this.sendingText;
               if (var13 != null && var13.length() <= 1024 && this.photoPathsArray.size() == 1) {
                  ((SendMessagesHelper.SendingMediaInfo)this.photoPathsArray.get(0)).caption = this.sendingText;
                  this.sendingText = var2;
               }

               SendMessagesHelper.prepareSendingMedia(this.photoPathsArray, var6, (MessageObject)null, (InputContentInfoCompat)null, false, false, (MessageObject)null);
            }

            ArrayList var15;
            if (this.documentsPathsArray != null || this.documentsUrisArray != null) {
               label110: {
                  var13 = this.sendingText;
                  if (var13 != null && var13.length() <= 1024) {
                     var15 = this.documentsPathsArray;
                     if (var15 != null) {
                        var9 = var15.size();
                     } else {
                        var9 = 0;
                     }

                     var15 = this.documentsUrisArray;
                     if (var15 != null) {
                        var8 = var15.size();
                     } else {
                        var8 = 0;
                     }

                     if (var9 + var8 == 1) {
                        var13 = this.sendingText;
                        this.sendingText = var2;
                        break label110;
                     }
                  }

                  var13 = var2;
               }

               SendMessagesHelper.prepareSendingDocuments(this.documentsPathsArray, this.documentsOriginalPathsArray, this.documentsUrisArray, var13, this.documentsMimeType, var6, (MessageObject)null, (InputContentInfoCompat)null, (MessageObject)null);
            }

            Location var19 = this.sendingLocation;
            if (var19 != null) {
               SendMessagesHelper.prepareSendingLocation(var19, var6);
               this.sendingText = var2;
            }

            var13 = this.sendingText;
            if (var13 != null) {
               SendMessagesHelper.prepareSendingText(var13, var6);
            }

            var15 = this.contactsToSend;
            if (var15 != null && !var15.isEmpty()) {
               for(var9 = var5; var9 < this.contactsToSend.size(); ++var9) {
                  TLRPC.User var20 = (TLRPC.User)this.contactsToSend.get(var9);
                  SendMessagesHelper.getInstance(var10).sendMessage((TLRPC.User)var20, var6, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
               }
            }
         }

         this.photoPathsArray = null;
         this.videoPath = null;
         this.sendingText = null;
         this.sendingLocation = null;
         this.documentsPathsArray = null;
         this.documentsOriginalPathsArray = null;
         this.contactsToSend = null;
         this.contactsToSendUri = null;
      }
   }

   public boolean dispatchKeyEvent(KeyEvent var1) {
      var1.getKeyCode();
      if (!mainFragmentsStack.isEmpty() && (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible()) && var1.getRepeatCount() == 0 && var1.getAction() == 0 && (var1.getKeyCode() == 24 || var1.getKeyCode() == 25)) {
         ArrayList var2 = mainFragmentsStack;
         BaseFragment var3 = (BaseFragment)var2.get(var2.size() - 1);
         if (var3 instanceof ChatActivity && ((ChatActivity)var3).maybePlayVisibleVideo()) {
            return true;
         }

         if (AndroidUtilities.isTablet() && !rightFragmentsStack.isEmpty()) {
            var2 = rightFragmentsStack;
            var3 = (BaseFragment)var2.get(var2.size() - 1);
            if (var3 instanceof ChatActivity && ((ChatActivity)var3).maybePlayVisibleVideo()) {
               return true;
            }
         }
      }

      return super.dispatchKeyEvent(var1);
   }

   public ActionBarLayout getActionBarLayout() {
      return this.actionBarLayout;
   }

   public ActionBarLayout getLayersActionBarLayout() {
      return this.layersActionBarLayout;
   }

   public int getMainFragmentsCount() {
      return mainFragmentsStack.size();
   }

   public ActionBarLayout getRightActionBarLayout() {
      return this.rightActionBarLayout;
   }

   public void hideVisibleActionMode() {
      ActionMode var1 = this.visibleActionMode;
      if (var1 != null) {
         var1.finish();
      }
   }

   // $FF: synthetic method
   public void lambda$checkAppUpdate$35$LaunchActivity(int var1, TLObject var2, TLRPC.TL_error var3) {
      UserConfig.getInstance(0).lastUpdateCheckTime = System.currentTimeMillis();
      UserConfig.getInstance(0).saveConfig(false);
      if (var2 instanceof TLRPC.TL_help_appUpdate) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$zIyXV0iNbVBNj3pWgG_YQ490Bu4(this, (TLRPC.TL_help_appUpdate)var2, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$checkFreeDiscSpace$48$LaunchActivity() {
      if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         SharedPreferences var1;
         boolean var10001;
         File var2;
         try {
            var1 = MessagesController.getGlobalMainSettings();
            if (Math.abs(var1.getLong("last_space_check", 0L) - System.currentTimeMillis()) < 259200000L) {
               return;
            }

            var2 = FileLoader.getDirectory(4);
         } catch (Throwable var9) {
            var10001 = false;
            return;
         }

         if (var2 != null) {
            long var4;
            label43: {
               StatFs var3;
               try {
                  var3 = new StatFs(var2.getAbsolutePath());
                  if (VERSION.SDK_INT < 18) {
                     var4 = (long)Math.abs(var3.getAvailableBlocks() * var3.getBlockSize());
                     break label43;
                  }
               } catch (Throwable var8) {
                  var10001 = false;
                  return;
               }

               try {
                  var4 = var3.getAvailableBlocksLong();
                  var4 = var3.getBlockSizeLong() * var4;
               } catch (Throwable var7) {
                  var10001 = false;
                  return;
               }
            }

            if (var4 < 104857600L) {
               try {
                  var1.edit().putLong("last_space_check", System.currentTimeMillis()).commit();
                  _$$Lambda$LaunchActivity$XotpJadOtesdoG_ogV2rSalfs0Y var10 = new _$$Lambda$LaunchActivity$XotpJadOtesdoG_ogV2rSalfs0Y(this);
                  AndroidUtilities.runOnUIThread(var10);
               } catch (Throwable var6) {
                  var10001 = false;
               }
            }

         }
      }
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$41$LaunchActivity(DialogInterface var1, int var2) {
      MessagesController.getInstance(this.currentAccount).performLogout(2);
   }

   // $FF: synthetic method
   public void lambda$didReceivedNotification$43$LaunchActivity(HashMap var1, int var2, DialogInterface var3, int var4) {
      if (!mainFragmentsStack.isEmpty()) {
         ArrayList var5 = mainFragmentsStack;
         if (AndroidUtilities.isGoogleMapsInstalled((BaseFragment)var5.get(var5.size() - 1))) {
            LocationActivity var6 = new LocationActivity(0);
            var6.setDelegate(new _$$Lambda$LaunchActivity$vKuTfZca44pasLbBQmx8DozvnHc(var1, var2));
            this.presentFragment(var6);
         }
      }
   }

   // $FF: synthetic method
   public void lambda$didSelectDialogs$36$LaunchActivity(ChatActivity var1, int var2, long var3, TLRPC.User var5) {
      this.actionBarLayout.presentFragment(var1, true, false, true, false);
      SendMessagesHelper.getInstance(var2).sendMessage((TLRPC.User)var5, var3, (MessageObject)null, (TLRPC.ReplyMarkup)null, (HashMap)null);
   }

   // $FF: synthetic method
   public void lambda$handleIntent$5$LaunchActivity(Bundle var1) {
      this.presentFragment(new CancelAccountDeletionActivity(var1));
   }

   // $FF: synthetic method
   public void lambda$handleIntent$7$LaunchActivity(int[] var1, LocationController.SharingLocationInfo var2) {
      var1[0] = var2.messageObject.currentAccount;
      this.switchToAccount(var1[0], true);
      LocationActivity var3 = new LocationActivity(2);
      var3.setMessageObject(var2.messageObject);
      var3.setDelegate(new _$$Lambda$LaunchActivity$4e8vXS2t3YJcvvp0fszXn1LM3wk(var1, var2.messageObject.getDialogId()));
      this.presentFragment(var3);
   }

   // $FF: synthetic method
   public void lambda$null$10$LaunchActivity(int var1, TLRPC.User var2, String var3, DialogsActivity var4, ArrayList var5, CharSequence var6, boolean var7) {
      long var8 = (Long)var5.get(0);
      Bundle var12 = new Bundle();
      var12.putBoolean("scrollToTopOnResume", true);
      int var10 = -((int)var8);
      var12.putInt("chat_id", var10);
      if (!mainFragmentsStack.isEmpty()) {
         MessagesController var13 = MessagesController.getInstance(var1);
         ArrayList var11 = mainFragmentsStack;
         if (!var13.checkCanOpenChat(var12, (BaseFragment)var11.get(var11.size() - 1))) {
            return;
         }
      }

      NotificationCenter.getInstance(var1).postNotificationName(NotificationCenter.closeChats);
      MessagesController.getInstance(var1).addUserToChat(var10, var2, (TLRPC.ChatFull)null, 0, var3, (BaseFragment)null, (Runnable)null);
      this.actionBarLayout.presentFragment(new ChatActivity(var12), true, false, true, false);
   }

   // $FF: synthetic method
   public void lambda$null$11$LaunchActivity(AlertDialog var1, TLObject var2, TLRPC.TL_error var3, String var4, int var5, String var6, String var7, Integer var8) {
      if (!this.isFinishing()) {
         try {
            var1.dismiss();
         } catch (Exception var17) {
            FileLog.e((Throwable)var17);
         }

         TLRPC.TL_contacts_resolvedPeer var9 = (TLRPC.TL_contacts_resolvedPeer)var2;
         boolean var10 = false;
         if (var3 != null || this.actionBarLayout == null || var4 != null && (var4 == null || var9.users.isEmpty())) {
            try {
               Toast.makeText(this, LocaleController.getString("NoUsernameFound", 2131559955), 0).show();
            } catch (Exception var16) {
               FileLog.e((Throwable)var16);
            }
         } else {
            MessagesController.getInstance(var5).putUsers(var9.users, false);
            MessagesController.getInstance(var5).putChats(var9.chats, false);
            MessagesStorage.getInstance(var5).putUsersAndChats(var9.users, var9.chats, false, true);
            if (var4 != null) {
               boolean var11;
               DialogsActivity var26;
               label102: {
                  label101: {
                     Bundle var25 = new Bundle();
                     var25.putBoolean("onlySelect", true);
                     var25.putBoolean("cantSendToChannels", true);
                     var25.putInt("dialogsType", 1);
                     var25.putString("selectAlertString", LocaleController.getString("SendGameTo", 2131560691));
                     var25.putString("selectAlertStringGroup", LocaleController.getString("SendGameToGroup", 2131560692));
                     var26 = new DialogsActivity(var25);
                     var26.setDelegate(new _$$Lambda$LaunchActivity$u_E5fg1gn1kuY6fPnql7A5bYvUE(this, var4, var5, var9));
                     ArrayList var24;
                     if (AndroidUtilities.isTablet()) {
                        if (this.layersActionBarLayout.fragmentsStack.size() > 0) {
                           var24 = this.layersActionBarLayout.fragmentsStack;
                           if (var24.get(var24.size() - 1) instanceof DialogsActivity) {
                              break label101;
                           }
                        }
                     } else if (this.actionBarLayout.fragmentsStack.size() > 1) {
                        var24 = this.actionBarLayout.fragmentsStack;
                        if (var24.get(var24.size() - 1) instanceof DialogsActivity) {
                           break label101;
                        }
                     }

                     var11 = false;
                     break label102;
                  }

                  var11 = true;
               }

               this.actionBarLayout.presentFragment(var26, var11, true, true, false);
               if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
                  SecretMediaViewer.getInstance().closePhoto(false, false);
               } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
                  PhotoViewer.getInstance().closePhoto(false, true);
               } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
                  ArticleViewer.getInstance().close(false, true);
               }

               this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
               if (AndroidUtilities.isTablet()) {
                  this.actionBarLayout.showLastFragment();
                  this.rightActionBarLayout.showLastFragment();
               } else {
                  this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
               }
            } else {
               BaseFragment var18 = null;
               var2 = null;
               Bundle var20;
               if (var6 != null) {
                  TLRPC.User var22 = var2;
                  if (!var9.users.isEmpty()) {
                     var22 = (TLRPC.User)var9.users.get(0);
                  }

                  if (var22 == null || var22.bot && var22.bot_nochats) {
                     try {
                        Toast.makeText(this, LocaleController.getString("BotCantJoinGroups", 2131558849), 0).show();
                     } catch (Exception var15) {
                        FileLog.e((Throwable)var15);
                     }

                     return;
                  }

                  var20 = new Bundle();
                  var20.putBoolean("onlySelect", true);
                  var20.putInt("dialogsType", 2);
                  var20.putString("addToGroupAlertString", LocaleController.formatString("AddToTheGroupTitle", 2131558596, UserObject.getUserName(var22), "%1$s"));
                  DialogsActivity var23 = new DialogsActivity(var20);
                  var23.setDelegate(new _$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ(this, var5, var22, var6));
                  this.presentFragment(var23);
               } else {
                  var20 = new Bundle();
                  int var12;
                  if (!var9.chats.isEmpty()) {
                     var20.putInt("chat_id", ((TLRPC.Chat)var9.chats.get(0)).id);
                     var12 = -((TLRPC.Chat)var9.chats.get(0)).id;
                  } else {
                     var20.putInt("user_id", ((TLRPC.User)var9.users.get(0)).id);
                     var12 = ((TLRPC.User)var9.users.get(0)).id;
                  }

                  long var13 = (long)var12;
                  boolean var27 = var10;
                  if (var7 != null) {
                     var27 = var10;
                     if (var9.users.size() > 0) {
                        var27 = var10;
                        if (((TLRPC.User)var9.users.get(0)).bot) {
                           var20.putString("botUser", var7);
                           var27 = true;
                        }
                     }
                  }

                  if (var8 != null) {
                     var20.putInt("message_id", var8);
                  }

                  if (!mainFragmentsStack.isEmpty()) {
                     ArrayList var19 = mainFragmentsStack;
                     var18 = (BaseFragment)var19.get(var19.size() - 1);
                  }

                  if (var18 == null || MessagesController.getInstance(var5).checkCanOpenChat(var20, var18)) {
                     ChatActivity var21;
                     if (var27 && var18 instanceof ChatActivity) {
                        var21 = (ChatActivity)var18;
                        if (var21.getDialogId() == var13) {
                           var21.setBotUser(var7);
                           return;
                        }
                     }

                     var21 = new ChatActivity(var20);
                     this.actionBarLayout.presentFragment(var21);
                  }
               }
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$13$LaunchActivity(int var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, Integer var9, Integer var10, String var11, HashMap var12, String var13, String var14, String var15, TLRPC.TL_wallPaper var16, DialogInterface var17, int var18) {
      this.runLinkRequest(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, 1);
   }

   // $FF: synthetic method
   public void lambda$null$14$LaunchActivity(AlertDialog var1, TLRPC.TL_error var2, TLObject var3, int var4, String var5, String var6, String var7, String var8, String var9, String var10, boolean var11, Integer var12, Integer var13, String var14, HashMap var15, String var16, String var17, String var18, TLRPC.TL_wallPaper var19) {
      if (!this.isFinishing()) {
         try {
            var1.dismiss();
         } catch (Exception var20) {
            FileLog.e((Throwable)var20);
         }

         if (var2 == null && this.actionBarLayout != null) {
            TLRPC.ChatInvite var22 = (TLRPC.ChatInvite)var3;
            TLRPC.Chat var24 = var22.chat;
            ArrayList var27;
            if (var24 != null) {
               label93: {
                  if (ChatObject.isLeftFromChat(var24)) {
                     var24 = var22.chat;
                     if (var24.kicked || TextUtils.isEmpty(var24.username)) {
                        break label93;
                     }
                  }

                  MessagesController.getInstance(var4).putChat(var22.chat, false);
                  var27 = new ArrayList();
                  var27.add(var22.chat);
                  MessagesStorage.getInstance(var4).putUsersAndChats((ArrayList)null, var27, false, true);
                  Bundle var28 = new Bundle();
                  var28.putInt("chat_id", var22.chat.id);
                  if (!mainFragmentsStack.isEmpty()) {
                     MessagesController var29 = MessagesController.getInstance(var4);
                     ArrayList var23 = mainFragmentsStack;
                     if (!var29.checkCanOpenChat(var28, (BaseFragment)var23.get(var23.size() - 1))) {
                        return;
                     }
                  }

                  ChatActivity var25 = new ChatActivity(var28);
                  NotificationCenter.getInstance(var4).postNotificationName(NotificationCenter.closeChats);
                  this.actionBarLayout.presentFragment(var25, false, true, true, false);
                  return;
               }
            }

            label84: {
               if (var22.chat != null || var22.channel && !var22.megagroup) {
                  var24 = var22.chat;
                  if (var24 == null || ChatObject.isChannel(var24) && !var22.chat.megagroup) {
                     break label84;
                  }
               }

               if (!mainFragmentsStack.isEmpty()) {
                  var27 = mainFragmentsStack;
                  BaseFragment var31 = (BaseFragment)var27.get(var27.size() - 1);
                  var31.showDialog(new JoinGroupAlert(this, var22, var5, var31));
                  return;
               }
            }

            AlertDialog.Builder var32 = new AlertDialog.Builder(this);
            var32.setTitle(LocaleController.getString("AppName", 2131558635));
            TLRPC.Chat var30 = var22.chat;
            String var26;
            if (var30 != null) {
               var26 = var30.title;
            } else {
               var26 = var22.title;
            }

            var32.setMessage(LocaleController.formatString("ChannelJoinTo", 2131558955, var26));
            var32.setPositiveButton(LocaleController.getString("OK", 2131560097), new _$$Lambda$LaunchActivity$0lbK3R9cg_xyRcV1YTV_bo7OG68(this, var4, var6, var5, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17, var18, var19));
            var32.setNegativeButton(LocaleController.getString("Cancel", 2131558891), (OnClickListener)null);
            this.showAlertDialog(var32);
         } else {
            AlertDialog.Builder var21 = new AlertDialog.Builder(this);
            var21.setTitle(LocaleController.getString("AppName", 2131558635));
            if (var2.text.startsWith("FLOOD_WAIT")) {
               var21.setMessage(LocaleController.getString("FloodWait", 2131559495));
            } else {
               var21.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", 2131559705));
            }

            var21.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            this.showAlertDialog(var21);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$16$LaunchActivity(AlertDialog var1, TLRPC.TL_error var2, TLObject var3, int var4) {
      if (!this.isFinishing()) {
         try {
            var1.dismiss();
         } catch (Exception var5) {
            FileLog.e((Throwable)var5);
         }

         if (var2 == null) {
            if (this.actionBarLayout != null) {
               TLRPC.Updates var6 = (TLRPC.Updates)var3;
               if (!var6.chats.isEmpty()) {
                  TLRPC.Chat var10 = (TLRPC.Chat)var6.chats.get(0);
                  var10.left = false;
                  var10.kicked = false;
                  MessagesController.getInstance(var4).putUsers(var6.users, false);
                  MessagesController.getInstance(var4).putChats(var6.chats, false);
                  Bundle var7 = new Bundle();
                  var7.putInt("chat_id", var10.id);
                  if (!mainFragmentsStack.isEmpty()) {
                     MessagesController var11 = MessagesController.getInstance(var4);
                     ArrayList var12 = mainFragmentsStack;
                     if (!var11.checkCanOpenChat(var7, (BaseFragment)var12.get(var12.size() - 1))) {
                        return;
                     }
                  }

                  ChatActivity var8 = new ChatActivity(var7);
                  NotificationCenter.getInstance(var4).postNotificationName(NotificationCenter.closeChats);
                  this.actionBarLayout.presentFragment(var8, false, true, true, false);
               }
            }
         } else {
            AlertDialog.Builder var9 = new AlertDialog.Builder(this);
            var9.setTitle(LocaleController.getString("AppName", 2131558635));
            if (var2.text.startsWith("FLOOD_WAIT")) {
               var9.setMessage(LocaleController.getString("FloodWait", 2131559495));
            } else if (var2.text.equals("USERS_TOO_MUCH")) {
               var9.setMessage(LocaleController.getString("JoinToGroupErrorFull", 2131559704));
            } else {
               var9.setMessage(LocaleController.getString("JoinToGroupErrorNotExist", 2131559705));
            }

            var9.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            this.showAlertDialog(var9);
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$19$LaunchActivity(AlertDialog var1, TLObject var2, int var3, TLRPC.TL_account_authorizationForm var4, TLRPC.TL_account_getAuthorizationForm var5, String var6, String var7, String var8) {
      try {
         var1.dismiss();
      } catch (Exception var9) {
         FileLog.e((Throwable)var9);
      }

      if (var2 != null) {
         TLRPC.TL_account_password var10 = (TLRPC.TL_account_password)var2;
         MessagesController.getInstance(var3).putUsers(var4.users, false);
         this.presentFragment(new PassportActivity(5, var5.bot_id, var5.scope, var5.public_key, var6, var7, var8, var4, var10));
      }

   }

   // $FF: synthetic method
   public void lambda$null$20$LaunchActivity(AlertDialog var1, int var2, TLRPC.TL_account_authorizationForm var3, TLRPC.TL_account_getAuthorizationForm var4, String var5, String var6, String var7, TLObject var8, TLRPC.TL_error var9) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$PDILZQFsnUj7QGqTcIzCivtaxlA(this, var1, var8, var2, var3, var4, var5, var6, var7));
   }

   // $FF: synthetic method
   public void lambda$null$21$LaunchActivity(AlertDialog var1, TLRPC.TL_error var2) {
      try {
         var1.dismiss();
         if ("APP_VERSION_OUTDATED".equals(var2.text)) {
            AlertsCreator.showUpdateAppAlert(this, LocaleController.getString("UpdateAppAlert", 2131560951), true);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append(LocaleController.getString("ErrorOccurred", 2131559375));
            var4.append("\n");
            var4.append(var2.text);
            this.showAlertDialog(AlertsCreator.createSimpleAlert(this, var4.toString()));
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$23$LaunchActivity(AlertDialog var1, TLObject var2) {
      try {
         var1.dismiss();
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      if (var2 instanceof TLRPC.TL_help_deepLinkInfo) {
         TLRPC.TL_help_deepLinkInfo var4 = (TLRPC.TL_help_deepLinkInfo)var2;
         AlertsCreator.showUpdateAppAlert(this, var4.message, var4.update_app);
      }

   }

   // $FF: synthetic method
   public void lambda$null$25$LaunchActivity(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      try {
         var1.dismiss();
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      if (var2 instanceof TLRPC.TL_langPackLanguage) {
         this.showAlertDialog(AlertsCreator.createLanguageAlert(this, (TLRPC.TL_langPackLanguage)var2));
      } else if (var3 != null) {
         if ("LANG_CODE_NOT_SUPPORTED".equals(var3.text)) {
            this.showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LanguageUnsupportedError", 2131559727)));
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append(LocaleController.getString("ErrorOccurred", 2131559375));
            var5.append("\n");
            var5.append(var3.text);
            this.showAlertDialog(AlertsCreator.createSimpleAlert(this, var5.toString()));
         }
      }

   }

   // $FF: synthetic method
   public void lambda$null$28$LaunchActivity(AlertDialog var1, TLObject var2, TLRPC.TL_wallPaper var3, TLRPC.TL_error var4) {
      try {
         var1.dismiss();
      } catch (Exception var5) {
         FileLog.e((Throwable)var5);
      }

      if (var2 instanceof TLRPC.TL_wallPaper) {
         Object var6 = (TLRPC.TL_wallPaper)var2;
         TLRPC.TL_wallPaperSettings var7;
         if (((TLRPC.TL_wallPaper)var6).pattern) {
            var7 = var3.settings;
            WallpapersListActivity.ColorWallpaper var10 = new WallpapersListActivity.ColorWallpaper(-1L, var7.background_color, ((TLRPC.TL_wallPaper)var6).id, (float)var7.intensity / 100.0F, var7.motion, (File)null);
            var10.pattern = (TLRPC.TL_wallPaper)var6;
            var6 = var10;
         }

         WallpaperActivity var8 = new WallpaperActivity(var6, (Bitmap)null);
         var7 = var3.settings;
         var8.setInitialModes(var7.blur, var7.motion);
         this.presentFragment(var8);
      } else {
         StringBuilder var9 = new StringBuilder();
         var9.append(LocaleController.getString("ErrorOccurred", 2131559375));
         var9.append("\n");
         var9.append(var4.text);
         this.showAlertDialog(AlertsCreator.createSimpleAlert(this, var9.toString()));
      }

   }

   // $FF: synthetic method
   public void lambda$null$30$LaunchActivity(AlertDialog var1, TLObject var2, BaseFragment var3, int var4, Bundle var5) {
      try {
         var1.dismiss();
      } catch (Exception var8) {
         FileLog.e((Throwable)var8);
      }

      boolean var6 = true;
      boolean var7 = var6;
      if (var2 instanceof TLRPC.TL_messages_chats) {
         TLRPC.TL_messages_chats var9 = (TLRPC.TL_messages_chats)var2;
         var7 = var6;
         if (!var9.chats.isEmpty()) {
            MessagesController.getInstance(this.currentAccount).putChats(var9.chats, false);
            TLRPC.Chat var10 = (TLRPC.Chat)var9.chats.get(0);
            if (var3 == null || MessagesController.getInstance(var4).checkCanOpenChat(var5, var3)) {
               this.actionBarLayout.presentFragment(new ChatActivity(var5));
            }

            var7 = false;
         }
      }

      if (var7) {
         this.showAlertDialog(AlertsCreator.createSimpleAlert(this, LocaleController.getString("LinkNotFound", 2131559761)));
      }

   }

   // $FF: synthetic method
   public void lambda$null$31$LaunchActivity(AlertDialog var1, BaseFragment var2, int var3, Bundle var4, TLObject var5, TLRPC.TL_error var6) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$z_gJkNbvZ8jzLDA3H5Zgz1rUffg(this, var1, var5, var2, var3, var4));
   }

   // $FF: synthetic method
   public void lambda$null$34$LaunchActivity(TLRPC.TL_help_appUpdate var1, int var2) {
      if (BuildVars.DEBUG_PRIVATE_VERSION) {
         var1.popup = Utilities.random.nextBoolean();
      }

      if (!var1.popup) {
         (new UpdateAppAlertDialog(this, var1, var2)).show();
      } else {
         UserConfig.getInstance(0).pendingAppUpdate = var1;
         UserConfig.getInstance(0).pendingAppUpdateBuildVersion = BuildVars.BUILD_VERSION;

         try {
            PackageInfo var3 = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            UserConfig.getInstance(0).pendingAppUpdateInstallTime = Math.max(var3.lastUpdateTime, var3.firstInstallTime);
         } catch (Exception var4) {
            FileLog.e((Throwable)var4);
            UserConfig.getInstance(0).pendingAppUpdateInstallTime = 0L;
         }

         UserConfig.getInstance(0).saveConfig(false);
         this.showUpdateActivity(var2, var1);
      }

   }

   // $FF: synthetic method
   public void lambda$null$47$LaunchActivity() {
      try {
         AlertsCreator.createFreeSpaceDialog(this).show();
      } catch (Throwable var2) {
      }

   }

   // $FF: synthetic method
   public void lambda$null$52$LaunchActivity(HashMap var1, LocaleController.LocaleInfo[] var2, String var3) {
      this.systemLocaleStrings = var1;
      if (this.englishLocaleStrings != null && this.systemLocaleStrings != null) {
         this.showLanguageAlertInternal(var2[1], var2[0], var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$54$LaunchActivity(HashMap var1, LocaleController.LocaleInfo[] var2, String var3) {
      this.englishLocaleStrings = var1;
      if (this.englishLocaleStrings != null && this.systemLocaleStrings != null) {
         this.showLanguageAlertInternal(var2[1], var2[0], var3);
      }

   }

   // $FF: synthetic method
   public void lambda$null$9$LaunchActivity(String var1, int var2, TLRPC.TL_contacts_resolvedPeer var3, DialogsActivity var4, ArrayList var5, CharSequence var6, boolean var7) {
      long var8 = (Long)var5.get(0);
      TLRPC.TL_inputMediaGame var15 = new TLRPC.TL_inputMediaGame();
      var15.id = new TLRPC.TL_inputGameShortName();
      TLRPC.InputGame var16 = var15.id;
      var16.short_name = var1;
      var16.bot_id = MessagesController.getInstance(var2).getInputUser((TLRPC.User)var3.users.get(0));
      SendMessagesHelper var12 = SendMessagesHelper.getInstance(var2);
      MessagesController var14 = MessagesController.getInstance(var2);
      int var10 = (int)var8;
      var12.sendGame(var14.getInputPeer(var10), var15, 0L, 0L);
      Bundle var13 = new Bundle();
      var13.putBoolean("scrollToTopOnResume", true);
      int var11 = (int)(var8 >> 32);
      if (var10 != 0) {
         if (var11 == 1) {
            var13.putInt("chat_id", var10);
         } else if (var10 > 0) {
            var13.putInt("user_id", var10);
         } else if (var10 < 0) {
            var13.putInt("chat_id", -var10);
         }
      } else {
         var13.putInt("enc_id", var11);
      }

      if (MessagesController.getInstance(var2).checkCanOpenChat(var13, var4)) {
         NotificationCenter.getInstance(var2).postNotificationName(NotificationCenter.closeChats);
         this.actionBarLayout.presentFragment(new ChatActivity(var13), true, false, true, false);
      }

   }

   // $FF: synthetic method
   public boolean lambda$onCreate$0$LaunchActivity(View var1, MotionEvent var2) {
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
   public void lambda$onCreate$2$LaunchActivity(View var1, int var2) {
      if (var2 == 0) {
         DrawerLayoutAdapter var5 = this.drawerLayoutAdapter;
         var5.setAccountsShowed(var5.isAccountsShowed() ^ true, true);
      } else if (var1 instanceof DrawerUserCell) {
         this.switchToAccount(((DrawerUserCell)var1).getAccountNumber(), true);
         this.drawerLayoutContainer.closeDrawer(false);
      } else if (var1 instanceof DrawerAddCell) {
         byte var3 = -1;
         var2 = 0;

         int var4;
         while(true) {
            var4 = var3;
            if (var2 >= 3) {
               break;
            }

            if (!UserConfig.getInstance(var2).isClientActivated()) {
               var4 = var2;
               break;
            }

            ++var2;
         }

         if (var4 >= 0) {
            this.presentFragment(new LoginActivity(var4));
         }

         this.drawerLayoutContainer.closeDrawer(false);
      } else {
         var2 = this.drawerLayoutAdapter.getId(var2);
         if (var2 == 2) {
            this.presentFragment(new GroupCreateActivity(new Bundle()));
            this.drawerLayoutContainer.closeDrawer(false);
         } else {
            Bundle var6;
            if (var2 == 3) {
               var6 = new Bundle();
               var6.putBoolean("onlyUsers", true);
               var6.putBoolean("destroyAfterSelect", true);
               var6.putBoolean("createSecretChat", true);
               var6.putBoolean("allowBots", false);
               this.presentFragment(new ContactsActivity(var6));
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 4) {
               SharedPreferences var7 = MessagesController.getGlobalMainSettings();
               if (!BuildVars.DEBUG_VERSION && var7.getBoolean("channel_intro", false)) {
                  var6 = new Bundle();
                  var6.putInt("step", 0);
                  this.presentFragment(new ChannelCreateActivity(var6));
               } else {
                  this.presentFragment(new ChannelIntroActivity());
                  var7.edit().putBoolean("channel_intro", true).commit();
               }

               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 6) {
               this.presentFragment(new ContactsActivity((Bundle)null));
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 7) {
               this.presentFragment(new InviteContactsActivity());
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 8) {
               this.presentFragment(new SettingsActivity());
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 9) {
               Browser.openUrl(this, (String)LocaleController.getString("TelegramFaqUrl", 2131560871));
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 10) {
               this.presentFragment(new CallLogActivity());
               this.drawerLayoutContainer.closeDrawer(false);
            } else if (var2 == 11) {
               var6 = new Bundle();
               var6.putInt("user_id", UserConfig.getInstance(this.currentAccount).getClientUserId());
               this.presentFragment(new ChatActivity(var6));
               this.drawerLayoutContainer.closeDrawer(false);
            }
         }
      }

   }

   // $FF: synthetic method
   public void lambda$onRequestPermissionsResult$37$LaunchActivity(DialogInterface var1, int var2) {
      try {
         Intent var5 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
         StringBuilder var3 = new StringBuilder();
         var3.append("package:");
         var3.append(ApplicationLoader.applicationContext.getPackageName());
         var5.setData(Uri.parse(var3.toString()));
         this.startActivity(var5);
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$12$LaunchActivity(AlertDialog var1, String var2, int var3, String var4, String var5, Integer var6, TLObject var7, TLRPC.TL_error var8) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$iffzHHAMAuBNKcUpOYSEoUIrEeY(this, var1, var7, var8, var2, var3, var4, var5, var6));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$15$LaunchActivity(AlertDialog var1, int var2, String var3, String var4, String var5, String var6, String var7, String var8, boolean var9, Integer var10, Integer var11, String var12, HashMap var13, String var14, String var15, String var16, TLRPC.TL_wallPaper var17, TLObject var18, TLRPC.TL_error var19) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$XxlJBaFgsBRiIBlnYo_htRWRAf0(this, var1, var19, var18, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$17$LaunchActivity(int var1, AlertDialog var2, TLObject var3, TLRPC.TL_error var4) {
      if (var4 == null) {
         TLRPC.Updates var5 = (TLRPC.Updates)var3;
         MessagesController.getInstance(var1).processUpdates(var5, false);
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk(this, var2, var4, var3, var1));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$18$LaunchActivity(boolean var1, int var2, String var3, DialogsActivity var4, ArrayList var5, CharSequence var6, boolean var7) {
      long var8 = (Long)var5.get(0);
      Bundle var12 = new Bundle();
      var12.putBoolean("scrollToTopOnResume", true);
      var12.putBoolean("hasUrl", var1);
      int var10 = (int)var8;
      int var11 = (int)(var8 >> 32);
      if (var10 != 0) {
         if (var11 == 1) {
            var12.putInt("chat_id", var10);
         } else if (var10 > 0) {
            var12.putInt("user_id", var10);
         } else if (var10 < 0) {
            var12.putInt("chat_id", -var10);
         }
      } else {
         var12.putInt("enc_id", var11);
      }

      if (MessagesController.getInstance(var2).checkCanOpenChat(var12, var4)) {
         NotificationCenter.getInstance(var2).postNotificationName(NotificationCenter.closeChats);
         DataQuery.getInstance(var2).saveDraft(var8, var3, (ArrayList)null, (TLRPC.Message)null, false);
         this.actionBarLayout.presentFragment(new ChatActivity(var12), true, false, true, false);
      }

   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$22$LaunchActivity(int[] var1, int var2, AlertDialog var3, TLRPC.TL_account_getAuthorizationForm var4, String var5, String var6, String var7, TLObject var8, TLRPC.TL_error var9) {
      TLRPC.TL_account_authorizationForm var10 = (TLRPC.TL_account_authorizationForm)var8;
      if (var10 != null) {
         TLRPC.TL_account_getPassword var11 = new TLRPC.TL_account_getPassword();
         var1[0] = ConnectionsManager.getInstance(var2).sendRequest(var11, new _$$Lambda$LaunchActivity$GofL0vsgJimoLHa3SGxufnpInEA(this, var3, var2, var10, var4, var5, var6, var7));
      } else {
         AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$YQzhQ7yG96OboNlBWD_9IQ2DM9I(this, var3, var9));
      }

   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$24$LaunchActivity(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$I0_icmFxixS_c3kxY_ZR1JLcHdI(this, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$26$LaunchActivity(AlertDialog var1, TLObject var2, TLRPC.TL_error var3) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$3LcmzAiodHFy1RroGRficYduxV8(this, var1, var2, var3));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$27$LaunchActivity(WallpaperActivity var1) {
      this.presentFragment(var1);
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$29$LaunchActivity(AlertDialog var1, TLRPC.TL_wallPaper var2, TLObject var3, TLRPC.TL_error var4) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$nSOp0Z8yCajsdR_ViEON2LJngGI(this, var1, var3, var2, var4));
   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$32$LaunchActivity(Bundle var1, Integer var2, int[] var3, AlertDialog var4, BaseFragment var5, int var6) {
      if (!this.actionBarLayout.presentFragment(new ChatActivity(var1))) {
         TLRPC.TL_channels_getChannels var7 = new TLRPC.TL_channels_getChannels();
         TLRPC.TL_inputChannel var8 = new TLRPC.TL_inputChannel();
         var8.channel_id = var2;
         var7.id.add(var8);
         var3[0] = ConnectionsManager.getInstance(this.currentAccount).sendRequest(var7, new _$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU(this, var4, var5, var6, var1));
      }

   }

   // $FF: synthetic method
   public void lambda$runLinkRequest$8$LaunchActivity(int var1, String var2, String var3, String var4, String var5, String var6, String var7, boolean var8, Integer var9, Integer var10, String var11, HashMap var12, String var13, String var14, String var15, TLRPC.TL_wallPaper var16, int var17) {
      if (var17 != var1) {
         this.switchToAccount(var17, true);
      }

      this.runLinkRequest(var17, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, 1);
   }

   // $FF: synthetic method
   public void lambda$showLanguageAlert$53$LaunchActivity(LocaleController.LocaleInfo[] var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      HashMap var8 = new HashMap();
      if (var3 != null) {
         TLRPC.Vector var7 = (TLRPC.Vector)var3;

         for(int var5 = 0; var5 < var7.objects.size(); ++var5) {
            TLRPC.LangPackString var6 = (TLRPC.LangPackString)var7.objects.get(var5);
            var8.put(var6.key, var6.value);
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$_sPBUeq_K9hirrefOHaOkEfYGN8(this, var8, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$showLanguageAlert$55$LaunchActivity(LocaleController.LocaleInfo[] var1, String var2, TLObject var3, TLRPC.TL_error var4) {
      HashMap var8 = new HashMap();
      if (var3 != null) {
         TLRPC.Vector var7 = (TLRPC.Vector)var3;

         for(int var5 = 0; var5 < var7.objects.size(); ++var5) {
            TLRPC.LangPackString var6 = (TLRPC.LangPackString)var7.objects.get(var5);
            var8.put(var6.key, var6.value);
         }
      }

      AndroidUtilities.runOnUIThread(new _$$Lambda$LaunchActivity$mWoCWWLsHIOPRrn56P68AHjatJk(this, var8, var1, var2));
   }

   // $FF: synthetic method
   public void lambda$showLanguageAlertInternal$50$LaunchActivity(View var1) {
      this.localeDialog = null;
      this.drawerLayoutContainer.closeDrawer(true);
      this.presentFragment(new LanguageSelectActivity());
      AlertDialog var2 = this.visibleDialog;
      if (var2 != null) {
         var2.dismiss();
         this.visibleDialog = null;
      }

   }

   // $FF: synthetic method
   public void lambda$showLanguageAlertInternal$51$LaunchActivity(LocaleController.LocaleInfo[] var1, DialogInterface var2, int var3) {
      LocaleController.getInstance().applyLanguage(var1[0], true, false, this.currentAccount);
      this.rebuildAllFragments(true);
   }

   // $FF: synthetic method
   public void lambda$showPasscodeActivity$4$LaunchActivity() {
      SharedConfig.isWaitingForPasscodeEnter = false;
      Intent var1 = this.passcodeSaveIntent;
      if (var1 != null) {
         this.handleIntent(var1, this.passcodeSaveIntentIsNew, this.passcodeSaveIntentIsRestore, true);
         this.passcodeSaveIntent = null;
      }

      this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
      this.actionBarLayout.setVisibility(0);
      this.actionBarLayout.showLastFragment();
      if (AndroidUtilities.isTablet()) {
         this.layersActionBarLayout.showLastFragment();
         this.rightActionBarLayout.showLastFragment();
         if (this.layersActionBarLayout.getVisibility() == 4) {
            this.layersActionBarLayout.setVisibility(0);
         }

         this.rightActionBarLayout.setVisibility(0);
      }

   }

   // $FF: synthetic method
   public void lambda$updateCurrentConnectionState$56$LaunchActivity() {
      BaseFragment var2;
      label22: {
         ArrayList var1;
         if (AndroidUtilities.isTablet()) {
            if (!layerFragmentsStack.isEmpty()) {
               var1 = layerFragmentsStack;
               var2 = (BaseFragment)var1.get(var1.size() - 1);
               break label22;
            }
         } else if (!mainFragmentsStack.isEmpty()) {
            var1 = mainFragmentsStack;
            var2 = (BaseFragment)var1.get(var1.size() - 1);
            break label22;
         }

         var2 = null;
      }

      if (!(var2 instanceof ProxyListActivity) && !(var2 instanceof ProxySettingsActivity)) {
         this.presentFragment(new ProxyListActivity());
      }

   }

   public boolean needAddFragmentToStack(BaseFragment var1, ActionBarLayout var2) {
      boolean var5;
      if (AndroidUtilities.isTablet()) {
         DrawerLayoutContainer var3 = this.drawerLayoutContainer;
         boolean var4 = var1 instanceof LoginActivity;
         if (!var4 && !(var1 instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         var3.setAllowOpenDrawer(var5, true);
         ActionBarLayout var7;
         if (var1 instanceof DialogsActivity) {
            if (((DialogsActivity)var1).isMainDialogList()) {
               var7 = this.actionBarLayout;
               if (var2 != var7) {
                  var7.removeAllFragments();
                  this.actionBarLayout.addFragmentToStack(var1);
                  this.layersActionBarLayout.removeAllFragments();
                  this.layersActionBarLayout.setVisibility(8);
                  this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
                  if (!this.tabletFullSize) {
                     this.shadowTabletSide.setVisibility(0);
                     if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                        this.backgroundTablet.setVisibility(0);
                     }
                  }

                  return false;
               }
            }
         } else if (var1 instanceof ChatActivity) {
            ActionBarLayout var6;
            if (!this.tabletFullSize) {
               var7 = this.rightActionBarLayout;
               if (var2 != var7) {
                  var7.setVisibility(0);
                  this.backgroundTablet.setVisibility(8);
                  this.rightActionBarLayout.removeAllFragments();
                  this.rightActionBarLayout.addFragmentToStack(var1);
                  if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                     while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        var6 = this.layersActionBarLayout;
                        var6.removeFragmentFromStack((BaseFragment)var6.fragmentsStack.get(0));
                     }

                     this.layersActionBarLayout.closeLastFragment(true);
                  }

                  return false;
               }
            }

            if (this.tabletFullSize) {
               var7 = this.actionBarLayout;
               if (var2 != var7) {
                  var7.addFragmentToStack(var1);
                  if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                     while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        var6 = this.layersActionBarLayout;
                        var6.removeFragmentFromStack((BaseFragment)var6.fragmentsStack.get(0));
                     }

                     this.layersActionBarLayout.closeLastFragment(true);
                  }

                  return false;
               }
            }
         } else {
            var7 = this.layersActionBarLayout;
            if (var2 != var7) {
               var7.setVisibility(0);
               this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
               if (var4) {
                  this.backgroundTablet.setVisibility(0);
                  this.shadowTabletSide.setVisibility(8);
                  this.shadowTablet.setBackgroundColor(0);
               } else {
                  this.shadowTablet.setBackgroundColor(2130706432);
               }

               this.layersActionBarLayout.addFragmentToStack(var1);
               return false;
            }
         }

         return true;
      } else {
         label94: {
            label93: {
               if (var1 instanceof LoginActivity) {
                  if (mainFragmentsStack.size() == 0) {
                     break label93;
                  }
               } else if (var1 instanceof CountrySelectActivity && mainFragmentsStack.size() == 1) {
                  break label93;
               }

               var5 = true;
               break label94;
            }

            var5 = false;
         }

         this.drawerLayoutContainer.setAllowOpenDrawer(var5, false);
         return true;
      }
   }

   public boolean needCloseLastFragment(ActionBarLayout var1) {
      if (AndroidUtilities.isTablet()) {
         if (var1 == this.actionBarLayout && var1.fragmentsStack.size() <= 1) {
            this.onFinish();
            this.finish();
            return false;
         }

         if (var1 == this.rightActionBarLayout) {
            if (!this.tabletFullSize) {
               this.backgroundTablet.setVisibility(0);
            }
         } else if (var1 == this.layersActionBarLayout && this.actionBarLayout.fragmentsStack.isEmpty() && this.layersActionBarLayout.fragmentsStack.size() == 1) {
            this.onFinish();
            this.finish();
            return false;
         }
      } else {
         if (var1.fragmentsStack.size() <= 1) {
            this.onFinish();
            this.finish();
            return false;
         }

         if (var1.fragmentsStack.size() >= 2 && !(var1.fragmentsStack.get(0) instanceof LoginActivity)) {
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
         }
      }

      return true;
   }

   public boolean needPresentFragment(BaseFragment var1, boolean var2, boolean var3, ActionBarLayout var4) {
      if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
         ArticleViewer.getInstance().close(false, true);
      }

      if (AndroidUtilities.isTablet()) {
         DrawerLayoutContainer var5 = this.drawerLayoutContainer;
         boolean var6 = var1 instanceof LoginActivity;
         boolean var7;
         if (!var6 && !(var1 instanceof CountrySelectActivity) && this.layersActionBarLayout.getVisibility() != 0) {
            var7 = true;
         } else {
            var7 = false;
         }

         var5.setAllowOpenDrawer(var7, true);
         ActionBarLayout var9;
         if (var1 instanceof DialogsActivity && ((DialogsActivity)var1).isMainDialogList()) {
            var9 = this.actionBarLayout;
            if (var4 != var9) {
               var9.removeAllFragments();
               this.actionBarLayout.presentFragment(var1, var2, var3, false, false);
               this.layersActionBarLayout.removeAllFragments();
               this.layersActionBarLayout.setVisibility(8);
               this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
               if (!this.tabletFullSize) {
                  this.shadowTabletSide.setVisibility(0);
                  if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                     this.backgroundTablet.setVisibility(0);
                  }
               }

               return false;
            }
         }

         if (!(var1 instanceof ChatActivity)) {
            var9 = this.layersActionBarLayout;
            if (var4 != var9) {
               var9.setVisibility(0);
               this.drawerLayoutContainer.setAllowOpenDrawer(false, true);
               if (var6) {
                  this.backgroundTablet.setVisibility(0);
                  this.shadowTabletSide.setVisibility(8);
                  this.shadowTablet.setBackgroundColor(0);
               } else {
                  this.shadowTablet.setBackgroundColor(2130706432);
               }

               this.layersActionBarLayout.presentFragment(var1, var2, var3, false, false);
               return false;
            } else {
               return true;
            }
         } else if ((this.tabletFullSize || var4 != this.rightActionBarLayout) && (!this.tabletFullSize || var4 != this.actionBarLayout)) {
            ActionBarLayout var8;
            if (!this.tabletFullSize) {
               var9 = this.rightActionBarLayout;
               if (var4 != var9) {
                  var9.setVisibility(0);
                  this.backgroundTablet.setVisibility(8);
                  this.rightActionBarLayout.removeAllFragments();
                  this.rightActionBarLayout.presentFragment(var1, var2, true, false, false);
                  if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                     while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        var8 = this.layersActionBarLayout;
                        var8.removeFragmentFromStack((BaseFragment)var8.fragmentsStack.get(0));
                     }

                     this.layersActionBarLayout.closeLastFragment(var3 ^ true);
                  }

                  return false;
               }
            }

            if (this.tabletFullSize) {
               var9 = this.actionBarLayout;
               if (var4 != var9) {
                  if (var9.fragmentsStack.size() > 1) {
                     var2 = true;
                  } else {
                     var2 = false;
                  }

                  var9.presentFragment(var1, var2, var3, false, false);
                  if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                     while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                        var8 = this.layersActionBarLayout;
                        var8.removeFragmentFromStack((BaseFragment)var8.fragmentsStack.get(0));
                     }

                     this.layersActionBarLayout.closeLastFragment(var3 ^ true);
                  }

                  return false;
               }
            }

            if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
               while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                  var4 = this.layersActionBarLayout;
                  var4.removeFragmentFromStack((BaseFragment)var4.fragmentsStack.get(0));
               }

               this.layersActionBarLayout.closeLastFragment(var3 ^ true);
            }

            var4 = this.actionBarLayout;
            if (var4.fragmentsStack.size() > 1) {
               var2 = true;
            } else {
               var2 = false;
            }

            var4.presentFragment(var1, var2, var3, false, false);
            return false;
         } else {
            label107: {
               if (this.tabletFullSize) {
                  var9 = this.actionBarLayout;
                  if (var4 == var9 && var9.fragmentsStack.size() == 1) {
                     var2 = false;
                     break label107;
                  }
               }

               var2 = true;
            }

            if (!this.layersActionBarLayout.fragmentsStack.isEmpty()) {
               while(this.layersActionBarLayout.fragmentsStack.size() - 1 > 0) {
                  var4 = this.layersActionBarLayout;
                  var4.removeFragmentFromStack((BaseFragment)var4.fragmentsStack.get(0));
               }

               this.layersActionBarLayout.closeLastFragment(var3 ^ true);
            }

            if (!var2) {
               this.actionBarLayout.presentFragment(var1, false, var3, false, false);
            }

            return var2;
         }
      } else {
         label170: {
            label169: {
               if (var1 instanceof LoginActivity) {
                  if (mainFragmentsStack.size() == 0) {
                     break label169;
                  }
               } else if (var1 instanceof CountrySelectActivity && mainFragmentsStack.size() == 1) {
                  break label169;
               }

               var2 = true;
               break label170;
            }

            var2 = false;
         }

         this.drawerLayoutContainer.setAllowOpenDrawer(var2, false);
         return true;
      }
   }

   public void onActionModeFinished(ActionMode var1) {
      super.onActionModeFinished(var1);
      if (this.visibleActionMode == var1) {
         this.visibleActionMode = null;
      }

      if (VERSION.SDK_INT < 23 || var1.getType() != 1) {
         this.actionBarLayout.onActionModeFinished(var1);
         if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeFinished(var1);
            this.layersActionBarLayout.onActionModeFinished(var1);
         }

      }
   }

   public void onActionModeStarted(ActionMode var1) {
      super.onActionModeStarted(var1);
      this.visibleActionMode = var1;

      label40: {
         Exception var10000;
         label44: {
            boolean var10001;
            Menu var2;
            try {
               var2 = var1.getMenu();
            } catch (Exception var4) {
               var10000 = var4;
               var10001 = false;
               break label44;
            }

            if (var2 == null) {
               break label40;
            }

            try {
               if (!this.actionBarLayout.extendActionMode(var2) && AndroidUtilities.isTablet() && !this.rightActionBarLayout.extendActionMode(var2)) {
                  this.layersActionBarLayout.extendActionMode(var2);
               }
               break label40;
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
            }
         }

         Exception var5 = var10000;
         FileLog.e((Throwable)var5);
      }

      if (VERSION.SDK_INT < 23 || var1.getType() != 1) {
         this.actionBarLayout.onActionModeStarted(var1);
         if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onActionModeStarted(var1);
            this.layersActionBarLayout.onActionModeStarted(var1);
         }

      }
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.lastPauseTime != 0) {
         SharedConfig.lastPauseTime = 0;
         UserConfig.getInstance(this.currentAccount).saveConfig(false);
      }

      super.onActivityResult(var1, var2, var3);
      ThemeEditorView var4 = ThemeEditorView.getInstance();
      if (var4 != null) {
         var4.onActivityResult(var1, var2, var3);
      }

      ArrayList var5;
      if (this.actionBarLayout.fragmentsStack.size() != 0) {
         var5 = this.actionBarLayout.fragmentsStack;
         ((BaseFragment)var5.get(var5.size() - 1)).onActivityResultFragment(var1, var2, var3);
      }

      if (AndroidUtilities.isTablet()) {
         if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
            var5 = this.rightActionBarLayout.fragmentsStack;
            ((BaseFragment)var5.get(var5.size() - 1)).onActivityResultFragment(var1, var2, var3);
         }

         if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
            var5 = this.layersActionBarLayout.fragmentsStack;
            ((BaseFragment)var5.get(var5.size() - 1)).onActivityResultFragment(var1, var2, var3);
         }
      }

   }

   public void onBackPressed() {
      if (this.passcodeView.getVisibility() == 0) {
         this.finish();
      } else {
         boolean var1 = SecretMediaViewer.hasInstance();
         boolean var2 = false;
         if (var1 && SecretMediaViewer.getInstance().isVisible()) {
            SecretMediaViewer.getInstance().closePhoto(true, false);
         } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            PhotoViewer.getInstance().closePhoto(true, false);
         } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            ArticleViewer.getInstance().close(true, false);
         } else if (this.drawerLayoutContainer.isDrawerOpened()) {
            this.drawerLayoutContainer.closeDrawer(false);
         } else if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0) {
               this.layersActionBarLayout.onBackPressed();
            } else {
               boolean var3 = var2;
               if (this.rightActionBarLayout.getVisibility() == 0) {
                  var3 = var2;
                  if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                     ArrayList var4 = this.rightActionBarLayout.fragmentsStack;
                     var3 = ((BaseFragment)var4.get(var4.size() - 1)).onBackPressed() ^ true;
                  }
               }

               if (!var3) {
                  this.actionBarLayout.onBackPressed();
               }
            }
         } else {
            this.actionBarLayout.onBackPressed();
         }

      }
   }

   public void onConfigurationChanged(Configuration var1) {
      AndroidUtilities.checkDisplaySize(this, var1);
      super.onConfigurationChanged(var1);
      this.checkLayout();
      PipRoundVideoView var2 = PipRoundVideoView.getInstance();
      if (var2 != null) {
         var2.onConfigurationChanged();
      }

      EmbedBottomSheet var4 = EmbedBottomSheet.getInstance();
      if (var4 != null) {
         var4.onConfigurationChanged(var1);
      }

      PhotoViewer var5 = PhotoViewer.getPipInstance();
      if (var5 != null) {
         var5.onConfigurationChanged(var1);
      }

      ThemeEditorView var3 = ThemeEditorView.getInstance();
      if (var3 != null) {
         var3.onConfigurationChanged();
      }

   }

   protected void onCreate(Bundle var1) {
      ApplicationLoader.postInitApplication();
      AndroidUtilities.checkDisplaySize(this, this.getResources().getConfiguration());
      this.currentAccount = UserConfig.selectedAccount;
      Intent var2;
      boolean var7;
      String var38;
      if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
         boolean var4;
         label374: {
            var2 = this.getIntent();
            if (var2 != null && var2.getAction() != null) {
               if ("android.intent.action.SEND".equals(var2.getAction()) || "android.intent.action.SEND_MULTIPLE".equals(var2.getAction())) {
                  super.onCreate(var1);
                  this.finish();
                  return;
               }

               if ("android.intent.action.VIEW".equals(var2.getAction())) {
                  Uri var3 = var2.getData();
                  if (var3 != null) {
                     var38 = var3.toString().toLowerCase();
                     if (var38.startsWith("tg:proxy") || var38.startsWith("tg://proxy") || var38.startsWith("tg:socks") || var38.startsWith("tg://socks")) {
                        var4 = true;
                        break label374;
                     }
                  }
               }
            }

            var4 = false;
         }

         SharedPreferences var41 = MessagesController.getGlobalMainSettings();
         long var5 = var41.getLong("intro_crashed_time", 0L);
         var7 = var2.getBooleanExtra("fromIntro", false);
         if (var7) {
            var41.edit().putLong("intro_crashed_time", 0L).commit();
         }

         if (!var4 && Math.abs(var5 - System.currentTimeMillis()) >= 120000L && var2 != null && !var7 && ApplicationLoader.applicationContext.getSharedPreferences("logininfo2", 0).getAll().isEmpty()) {
            Intent var62 = new Intent(this, IntroActivity.class);
            var62.setData(var2.getData());
            this.startActivity(var62);
            super.onCreate(var1);
            this.finish();
            return;
         }
      }

      this.requestWindowFeature(1);
      this.setTheme(2131624206);
      if (VERSION.SDK_INT >= 21) {
         try {
            TaskDescription var34 = new TaskDescription((String)null, (Bitmap)null, Theme.getColor("actionBarDefault") | -16777216);
            this.setTaskDescription(var34);
         } catch (Exception var13) {
         }

         try {
            this.getWindow().setNavigationBarColor(-16777216);
         } catch (Exception var12) {
         }
      }

      this.getWindow().setBackgroundDrawableResource(2131165891);
      if (SharedConfig.passcodeHash.length() > 0 && !SharedConfig.allowScreenCapture) {
         try {
            this.getWindow().setFlags(8192, 8192);
         } catch (Exception var11) {
            FileLog.e((Throwable)var11);
         }
      }

      super.onCreate(var1);
      if (VERSION.SDK_INT >= 24) {
         AndroidUtilities.isInMultiwindow = this.isInMultiWindowMode();
      }

      Theme.createChatResources(this, false);
      if (SharedConfig.passcodeHash.length() != 0 && SharedConfig.appLocked) {
         SharedConfig.lastPauseTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
      }

      int var49 = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
      if (var49 > 0) {
         AndroidUtilities.statusBarHeight = this.getResources().getDimensionPixelSize(var49);
      }

      this.actionBarLayout = new ActionBarLayout(this);
      this.drawerLayoutContainer = new DrawerLayoutContainer(this);
      this.drawerLayoutContainer.setBehindKeyboardColor(Theme.getColor("windowBackgroundWhite"));
      this.setContentView(this.drawerLayoutContainer, new LayoutParams(-1, -1));
      if (AndroidUtilities.isTablet()) {
         this.getWindow().setSoftInputMode(16);
         RelativeLayout var35 = new RelativeLayout(this) {
            private boolean inLayout;

            protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
               int var6 = var4 - var2;
               if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
                  var4 = var6 / 100 * 35;
                  var2 = var4;
                  if (var4 < AndroidUtilities.dp(320.0F)) {
                     var2 = AndroidUtilities.dp(320.0F);
                  }

                  LaunchActivity.this.shadowTabletSide.layout(var2, 0, LaunchActivity.this.shadowTabletSide.getMeasuredWidth() + var2, LaunchActivity.this.shadowTabletSide.getMeasuredHeight());
                  LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
                  LaunchActivity.this.rightActionBarLayout.layout(var2, 0, LaunchActivity.this.rightActionBarLayout.getMeasuredWidth() + var2, LaunchActivity.this.rightActionBarLayout.getMeasuredHeight());
               } else {
                  LaunchActivity.this.actionBarLayout.layout(0, 0, LaunchActivity.this.actionBarLayout.getMeasuredWidth(), LaunchActivity.this.actionBarLayout.getMeasuredHeight());
               }

               var2 = (var6 - LaunchActivity.this.layersActionBarLayout.getMeasuredWidth()) / 2;
               var3 = (var5 - var3 - LaunchActivity.this.layersActionBarLayout.getMeasuredHeight()) / 2;
               LaunchActivity.this.layersActionBarLayout.layout(var2, var3, LaunchActivity.this.layersActionBarLayout.getMeasuredWidth() + var2, LaunchActivity.this.layersActionBarLayout.getMeasuredHeight() + var3);
               LaunchActivity.this.backgroundTablet.layout(0, 0, LaunchActivity.this.backgroundTablet.getMeasuredWidth(), LaunchActivity.this.backgroundTablet.getMeasuredHeight());
               LaunchActivity.this.shadowTablet.layout(0, 0, LaunchActivity.this.shadowTablet.getMeasuredWidth(), LaunchActivity.this.shadowTablet.getMeasuredHeight());
            }

            protected void onMeasure(int var1, int var2) {
               this.inLayout = true;
               int var3 = MeasureSpec.getSize(var1);
               int var4 = MeasureSpec.getSize(var2);
               this.setMeasuredDimension(var3, var4);
               if (!AndroidUtilities.isInMultiwindow && (!AndroidUtilities.isSmallTablet() || this.getResources().getConfiguration().orientation == 2)) {
                  LaunchActivity.this.tabletFullSize = false;
                  var2 = var3 / 100 * 35;
                  var1 = var2;
                  if (var2 < AndroidUtilities.dp(320.0F)) {
                     var1 = AndroidUtilities.dp(320.0F);
                  }

                  LaunchActivity.this.actionBarLayout.measure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
                  LaunchActivity.this.shadowTabletSide.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(1.0F), 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
                  LaunchActivity.this.rightActionBarLayout.measure(MeasureSpec.makeMeasureSpec(var3 - var1, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
               } else {
                  LaunchActivity.this.tabletFullSize = true;
                  LaunchActivity.this.actionBarLayout.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
               }

               LaunchActivity.this.backgroundTablet.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
               LaunchActivity.this.shadowTablet.measure(MeasureSpec.makeMeasureSpec(var3, 1073741824), MeasureSpec.makeMeasureSpec(var4, 1073741824));
               LaunchActivity.this.layersActionBarLayout.measure(MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(530.0F), var3), 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(528.0F), var4), 1073741824));
               this.inLayout = false;
            }

            public void requestLayout() {
               if (!this.inLayout) {
                  super.requestLayout();
               }
            }
         };
         this.drawerLayoutContainer.addView(var35, LayoutHelper.createFrame(-1, -1.0F));
         this.backgroundTablet = new View(this);
         BitmapDrawable var43 = (BitmapDrawable)this.getResources().getDrawable(2131165338);
         TileMode var8 = TileMode.REPEAT;
         var43.setTileModeXY(var8, var8);
         this.backgroundTablet.setBackgroundDrawable(var43);
         var35.addView(this.backgroundTablet, LayoutHelper.createRelative(-1, -1));
         var35.addView(this.actionBarLayout);
         this.rightActionBarLayout = new ActionBarLayout(this);
         this.rightActionBarLayout.init(rightFragmentsStack);
         this.rightActionBarLayout.setDelegate(this);
         var35.addView(this.rightActionBarLayout);
         this.shadowTabletSide = new FrameLayout(this);
         this.shadowTabletSide.setBackgroundColor(1076449908);
         var35.addView(this.shadowTabletSide);
         this.shadowTablet = new FrameLayout(this);
         FrameLayout var45 = this.shadowTablet;
         var7 = layerFragmentsStack.isEmpty();
         byte var9 = 8;
         byte var53;
         if (var7) {
            var53 = 8;
         } else {
            var53 = 0;
         }

         var45.setVisibility(var53);
         this.shadowTablet.setBackgroundColor(2130706432);
         var35.addView(this.shadowTablet);
         this.shadowTablet.setOnTouchListener(new _$$Lambda$LaunchActivity$KFZR9bOIUYM1vrC9qoPrRupqDO4(this));
         this.shadowTablet.setOnClickListener(_$$Lambda$LaunchActivity$OJponKw8R53ezoQT8H7udVOmkKQ.INSTANCE);
         this.layersActionBarLayout = new ActionBarLayout(this);
         this.layersActionBarLayout.setRemoveActionBarExtraHeight(true);
         this.layersActionBarLayout.setBackgroundView(this.shadowTablet);
         this.layersActionBarLayout.setUseAlphaAnimations(true);
         this.layersActionBarLayout.setBackgroundResource(2131165322);
         this.layersActionBarLayout.init(layerFragmentsStack);
         this.layersActionBarLayout.setDelegate(this);
         this.layersActionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
         ActionBarLayout var47 = this.layersActionBarLayout;
         if (layerFragmentsStack.isEmpty()) {
            var53 = var9;
         } else {
            var53 = 0;
         }

         var47.setVisibility(var53);
         var35.addView(this.layersActionBarLayout);
      } else {
         this.drawerLayoutContainer.addView(this.actionBarLayout, new LayoutParams(-1, -1));
      }

      this.sideMenu = new RecyclerListView(this);
      ((DefaultItemAnimator)this.sideMenu.getItemAnimator()).setDelayAnimations(false);
      this.sideMenu.setBackgroundColor(Theme.getColor("chats_menuBackground"));
      this.sideMenu.setLayoutManager(new LinearLayoutManager(this, 1, false));
      RecyclerListView var50 = this.sideMenu;
      DrawerLayoutAdapter var36 = new DrawerLayoutAdapter(this);
      this.drawerLayoutAdapter = var36;
      var50.setAdapter(var36);
      this.drawerLayoutContainer.setDrawerLayout(this.sideMenu);
      android.widget.FrameLayout.LayoutParams var39 = (android.widget.FrameLayout.LayoutParams)this.sideMenu.getLayoutParams();
      Point var52 = AndroidUtilities.getRealScreenSize();
      if (AndroidUtilities.isTablet()) {
         var49 = AndroidUtilities.dp(320.0F);
      } else {
         var49 = Math.min(AndroidUtilities.dp(320.0F), Math.min(var52.x, var52.y) - AndroidUtilities.dp(56.0F));
      }

      var39.width = var49;
      var39.height = -1;
      this.sideMenu.setLayoutParams(var39);
      this.sideMenu.setOnItemClickListener((RecyclerListView.OnItemClickListener)(new _$$Lambda$LaunchActivity$dOtJNBBcNQv2FwIcA_NKr5dzUI0(this)));
      this.drawerLayoutContainer.setParentActionBarLayout(this.actionBarLayout);
      this.actionBarLayout.setDrawerLayoutContainer(this.drawerLayoutContainer);
      this.actionBarLayout.init(mainFragmentsStack);
      this.actionBarLayout.setDelegate(this);
      Theme.loadWallpaper();
      this.passcodeView = new PasscodeView(this);
      this.drawerLayoutContainer.addView(this.passcodeView, LayoutHelper.createFrame(-1, -1.0F));
      this.checkCurrentAccount();
      NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeOtherAppActivities, this);
      this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needShowAlert);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.reloadInterface);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.suggestedLangpack);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewTheme);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.needSetDayNightTheme);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.closeOtherAppActivities);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetPasscode);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
      NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.notificationsCountUpdated);
      Exception var10000;
      boolean var10001;
      if (this.actionBarLayout.fragmentsStack.isEmpty()) {
         if (!UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            this.actionBarLayout.addFragmentToStack(new LoginActivity());
            this.drawerLayoutContainer.setAllowOpenDrawer(false, false);
         } else {
            DialogsActivity var42 = new DialogsActivity((Bundle)null);
            var42.setSideMenu(this.sideMenu);
            this.actionBarLayout.addFragmentToStack(var42);
            this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
         }

         if (var1 != null) {
            label339: {
               label385: {
                  try {
                     var38 = var1.getString("fragment");
                  } catch (Exception var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label385;
                  }

                  if (var38 == null) {
                     break label339;
                  }

                  Bundle var44;
                  byte var63;
                  label333: {
                     label332: {
                        label331: {
                           label330: {
                              label329: {
                                 label328: {
                                    label386: {
                                       label326: {
                                          label325: {
                                             label324: {
                                                label323: {
                                                   label322: {
                                                      try {
                                                         var44 = var1.getBundle("args");
                                                         switch(var38.hashCode()) {
                                                         case -1529105743:
                                                            break label323;
                                                         case -1349522494:
                                                            break label324;
                                                         case 3052376:
                                                            break label325;
                                                         case 98629247:
                                                            break;
                                                         case 738950403:
                                                            break label322;
                                                         case 1434631203:
                                                            break label326;
                                                         default:
                                                            break label386;
                                                         }
                                                      } catch (Exception var31) {
                                                         var10000 = var31;
                                                         var10001 = false;
                                                         break label385;
                                                      }

                                                      try {
                                                         if (var38.equals("group")) {
                                                            break label331;
                                                         }
                                                         break label386;
                                                      } catch (Exception var26) {
                                                         var10000 = var26;
                                                         var10001 = false;
                                                         break label385;
                                                      }
                                                   }

                                                   try {
                                                      if (var38.equals("channel")) {
                                                         break label332;
                                                      }
                                                      break label386;
                                                   } catch (Exception var25) {
                                                      var10000 = var25;
                                                      var10001 = false;
                                                      break label385;
                                                   }
                                                }

                                                try {
                                                   if (var38.equals("wallpapers")) {
                                                      break label328;
                                                   }
                                                   break label386;
                                                } catch (Exception var29) {
                                                   var10000 = var29;
                                                   var10001 = false;
                                                   break label385;
                                                }
                                             }

                                             try {
                                                if (var38.equals("chat_profile")) {
                                                   break label329;
                                                }
                                                break label386;
                                             } catch (Exception var28) {
                                                var10000 = var28;
                                                var10001 = false;
                                                break label385;
                                             }
                                          }

                                          try {
                                             if (var38.equals("chat")) {
                                                break label330;
                                             }
                                             break label386;
                                          } catch (Exception var27) {
                                             var10000 = var27;
                                             var10001 = false;
                                             break label385;
                                          }
                                       }

                                       try {
                                          if (!var38.equals("settings")) {
                                             break label386;
                                          }
                                       } catch (Exception var30) {
                                          var10000 = var30;
                                          var10001 = false;
                                          break label385;
                                       }

                                       var63 = 1;
                                       break label333;
                                    }

                                    var63 = -1;
                                    break label333;
                                 }

                                 var63 = 5;
                                 break label333;
                              }

                              var63 = 4;
                              break label333;
                           }

                           var63 = 0;
                           break label333;
                        }

                        var63 = 2;
                        break label333;
                     }

                     var63 = 3;
                  }

                  if (var63 != 0) {
                     if (var63 != 1) {
                        if (var63 != 2) {
                           if (var63 != 3) {
                              if (var63 != 4) {
                                 if (var63 != 5) {
                                    break label339;
                                 }

                                 try {
                                    WallpapersListActivity var46 = new WallpapersListActivity(0);
                                    this.actionBarLayout.addFragmentToStack(var46);
                                    var46.restoreSelfArgs(var1);
                                    break label339;
                                 } catch (Exception var19) {
                                    var10000 = var19;
                                    var10001 = false;
                                 }
                              } else {
                                 if (var44 == null) {
                                    break label339;
                                 }

                                 try {
                                    ProfileActivity var56 = new ProfileActivity(var44);
                                    if (this.actionBarLayout.addFragmentToStack(var56)) {
                                       var56.restoreSelfArgs(var1);
                                    }
                                    break label339;
                                 } catch (Exception var20) {
                                    var10000 = var20;
                                    var10001 = false;
                                 }
                              }
                           } else {
                              if (var44 == null) {
                                 break label339;
                              }

                              try {
                                 ChannelCreateActivity var57 = new ChannelCreateActivity(var44);
                                 if (this.actionBarLayout.addFragmentToStack(var57)) {
                                    var57.restoreSelfArgs(var1);
                                 }
                                 break label339;
                              } catch (Exception var21) {
                                 var10000 = var21;
                                 var10001 = false;
                              }
                           }
                        } else {
                           if (var44 == null) {
                              break label339;
                           }

                           try {
                              GroupCreateFinalActivity var58 = new GroupCreateFinalActivity(var44);
                              if (this.actionBarLayout.addFragmentToStack(var58)) {
                                 var58.restoreSelfArgs(var1);
                              }
                              break label339;
                           } catch (Exception var22) {
                              var10000 = var22;
                              var10001 = false;
                           }
                        }
                     } else {
                        try {
                           SettingsActivity var48 = new SettingsActivity();
                           this.actionBarLayout.addFragmentToStack(var48);
                           var48.restoreSelfArgs(var1);
                           break label339;
                        } catch (Exception var23) {
                           var10000 = var23;
                           var10001 = false;
                        }
                     }
                  } else {
                     if (var44 == null) {
                        break label339;
                     }

                     try {
                        ChatActivity var59 = new ChatActivity(var44);
                        if (this.actionBarLayout.addFragmentToStack(var59)) {
                           var59.restoreSelfArgs(var1);
                        }
                        break label339;
                     } catch (Exception var24) {
                        var10000 = var24;
                        var10001 = false;
                     }
                  }
               }

               Exception var51 = var10000;
               FileLog.e((Throwable)var51);
            }
         }
      } else {
         BaseFragment var54 = (BaseFragment)this.actionBarLayout.fragmentsStack.get(0);
         if (var54 instanceof DialogsActivity) {
            ((DialogsActivity)var54).setSideMenu(this.sideMenu);
         }

         boolean var10;
         if (!AndroidUtilities.isTablet()) {
            var7 = true;
         } else {
            if (this.actionBarLayout.fragmentsStack.size() <= 1 && this.layersActionBarLayout.fragmentsStack.isEmpty()) {
               var10 = true;
            } else {
               var10 = false;
            }

            var7 = var10;
            if (this.layersActionBarLayout.fragmentsStack.size() == 1) {
               var7 = var10;
               if (this.layersActionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) {
                  var7 = false;
               }
            }
         }

         var10 = var7;
         if (this.actionBarLayout.fragmentsStack.size() == 1) {
            var10 = var7;
            if (this.actionBarLayout.fragmentsStack.get(0) instanceof LoginActivity) {
               var10 = false;
            }
         }

         this.drawerLayoutContainer.setAllowOpenDrawer(var10, false);
      }

      this.checkLayout();
      var2 = this.getIntent();
      if (var1 != null) {
         var7 = true;
      } else {
         var7 = false;
      }

      this.handleIntent(var2, false, var7, false);

      label237: {
         label389: {
            String var33;
            try {
               var33 = Build.DISPLAY;
               var38 = Build.USER;
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label389;
            }

            String var55 = "";
            if (var33 != null) {
               try {
                  var33 = var33.toLowerCase();
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label389;
               }
            } else {
               var33 = "";
            }

            if (var38 != null) {
               try {
                  var55 = var33.toLowerCase();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label389;
               }
            }

            try {
               if (!var33.contains("flyme") && !var55.contains("flyme")) {
                  break label237;
               }
            } catch (Exception var15) {
               var10000 = var15;
               var10001 = false;
               break label389;
            }

            try {
               AndroidUtilities.incorrectDisplaySizeFix = true;
               View var61 = this.getWindow().getDecorView().getRootView();
               ViewTreeObserver var60 = var61.getViewTreeObserver();
               _$$Lambda$LaunchActivity$KOGX7F1UQC0GXDka6tTbIrU0Wk0 var40 = new _$$Lambda$LaunchActivity$KOGX7F1UQC0GXDka6tTbIrU0Wk0(var61);
               this.onGlobalLayoutListener = var40;
               var60.addOnGlobalLayoutListener(var40);
               break label237;
            } catch (Exception var14) {
               var10000 = var14;
               var10001 = false;
            }
         }

         Exception var37 = var10000;
         FileLog.e((Throwable)var37);
      }

      MediaController.getInstance().setBaseActivity(this, true);
   }

   protected void onDestroy() {
      if (PhotoViewer.getPipInstance() != null) {
         PhotoViewer.getPipInstance().destroyPhotoViewer();
      }

      if (PhotoViewer.hasInstance()) {
         PhotoViewer.getInstance().destroyPhotoViewer();
      }

      if (SecretMediaViewer.hasInstance()) {
         SecretMediaViewer.getInstance().destroyPhotoViewer();
      }

      if (ArticleViewer.hasInstance()) {
         ArticleViewer.getInstance().destroyArticleViewer();
      }

      if (ContentPreviewViewer.hasInstance()) {
         ContentPreviewViewer.getInstance().destroy();
      }

      PipRoundVideoView var1 = PipRoundVideoView.getInstance();
      MediaController.getInstance().setBaseActivity(this, false);
      MediaController.getInstance().setFeedbackView(this.actionBarLayout, false);
      if (var1 != null) {
         var1.close(false);
      }

      Theme.destroyResources();
      EmbedBottomSheet var4 = EmbedBottomSheet.getInstance();
      if (var4 != null) {
         var4.destroy();
      }

      ThemeEditorView var5 = ThemeEditorView.getInstance();
      if (var5 != null) {
         var5.destroy();
      }

      try {
         if (this.visibleDialog != null) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
      }

      try {
         if (this.onGlobalLayoutListener != null) {
            this.getWindow().getDecorView().getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(this.onGlobalLayoutListener);
         }
      } catch (Exception var2) {
         FileLog.e((Throwable)var2);
      }

      super.onDestroy();
      this.onFinish();
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      if (var1 == 82 && !SharedConfig.isWaitingForPasscodeEnter) {
         if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
            return super.onKeyUp(var1, var2);
         }

         if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
            return super.onKeyUp(var1, var2);
         }

         if (AndroidUtilities.isTablet()) {
            if (this.layersActionBarLayout.getVisibility() == 0 && !this.layersActionBarLayout.fragmentsStack.isEmpty()) {
               this.layersActionBarLayout.onKeyUp(var1, var2);
            } else if (this.rightActionBarLayout.getVisibility() == 0 && !this.rightActionBarLayout.fragmentsStack.isEmpty()) {
               this.rightActionBarLayout.onKeyUp(var1, var2);
            } else {
               this.actionBarLayout.onKeyUp(var1, var2);
            }
         } else if (this.actionBarLayout.fragmentsStack.size() == 1) {
            if (!this.drawerLayoutContainer.isDrawerOpened()) {
               if (this.getCurrentFocus() != null) {
                  AndroidUtilities.hideKeyboard(this.getCurrentFocus());
               }

               this.drawerLayoutContainer.openDrawer(false);
            } else {
               this.drawerLayoutContainer.closeDrawer(false);
            }
         } else {
            this.actionBarLayout.onKeyUp(var1, var2);
         }
      }

      return super.onKeyUp(var1, var2);
   }

   public void onLowMemory() {
      super.onLowMemory();
      this.actionBarLayout.onLowMemory();
      if (AndroidUtilities.isTablet()) {
         this.rightActionBarLayout.onLowMemory();
         this.layersActionBarLayout.onLowMemory();
      }

   }

   public void onMultiWindowModeChanged(boolean var1) {
      AndroidUtilities.isInMultiwindow = var1;
      this.checkLayout();
   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      this.handleIntent(var1, true, false, false);
   }

   protected void onPause() {
      super.onPause();
      SharedConfig.lastAppPauseTime = System.currentTimeMillis();
      ApplicationLoader.mainInterfacePaused = true;
      Utilities.stageQueue.postRunnable(_$$Lambda$LaunchActivity$uQqSZiudecpXZp8TwXxpaZLYG6E.INSTANCE);
      this.onPasscodePause();
      this.actionBarLayout.onPause();
      if (AndroidUtilities.isTablet()) {
         this.rightActionBarLayout.onPause();
         this.layersActionBarLayout.onPause();
      }

      PasscodeView var1 = this.passcodeView;
      if (var1 != null) {
         var1.onPause();
      }

      ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
      AndroidUtilities.unregisterUpdates();
      if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
         PhotoViewer.getInstance().onPause();
      }

   }

   public boolean onPreIme() {
      if (SecretMediaViewer.hasInstance() && SecretMediaViewer.getInstance().isVisible()) {
         SecretMediaViewer.getInstance().closePhoto(true, false);
         return true;
      } else if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
         PhotoViewer.getInstance().closePhoto(true, false);
         return true;
      } else if (ArticleViewer.hasInstance() && ArticleViewer.getInstance().isVisible()) {
         ArticleViewer.getInstance().close(true, false);
         return true;
      } else {
         return false;
      }
   }

   public void onRebuildAllFragments(ActionBarLayout var1, boolean var2) {
      if (AndroidUtilities.isTablet() && var1 == this.layersActionBarLayout) {
         this.rightActionBarLayout.rebuildAllFragmentViews(var2, var2);
         this.actionBarLayout.rebuildAllFragmentViews(var2, var2);
      }

      this.drawerLayoutAdapter.notifyDataSetChanged();
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      super.onRequestPermissionsResult(var1, var2, var3);
      boolean var4 = false;
      if (var1 != 3 && var1 != 4 && var1 != 5 && var1 != 19 && var1 != 20 && var1 != 22) {
         if (var1 == 2 && var3.length > 0 && var3[0] == 0) {
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.locationPermissionGranted);
         }
      } else {
         boolean var5;
         label95: {
            if (var3.length > 0 && var3[0] == 0) {
               if (var1 == 4) {
                  ImageLoader.getInstance().checkMediaPaths();
                  return;
               }

               if (var1 == 5) {
                  ContactsController.getInstance(this.currentAccount).forceImportContacts();
                  return;
               }

               if (var1 == 3) {
                  if (SharedConfig.inappCamera) {
                     CameraController.getInstance().initCamera((Runnable)null);
                  }

                  return;
               }

               var5 = var4;
               if (var1 == 19) {
                  break label95;
               }

               var5 = var4;
               if (var1 == 20) {
                  break label95;
               }

               if (var1 == 22) {
                  var5 = var4;
                  break label95;
               }
            }

            var5 = true;
         }

         if (var5) {
            AlertDialog.Builder var7 = new AlertDialog.Builder(this);
            var7.setTitle(LocaleController.getString("AppName", 2131558635));
            if (var1 == 3) {
               var7.setMessage(LocaleController.getString("PermissionNoAudio", 2131560414));
            } else if (var1 == 4) {
               var7.setMessage(LocaleController.getString("PermissionStorage", 2131560420));
            } else if (var1 == 5) {
               var7.setMessage(LocaleController.getString("PermissionContacts", 2131560412));
            } else if (var1 == 19 || var1 == 20 || var1 == 22) {
               var7.setMessage(LocaleController.getString("PermissionNoCamera", 2131560416));
            }

            var7.setNegativeButton(LocaleController.getString("PermissionOpenSettings", 2131560419), new _$$Lambda$LaunchActivity$4W20lwtGVi8T2FP_31dzdYvI9yo(this));
            var7.setPositiveButton(LocaleController.getString("OK", 2131560097), (OnClickListener)null);
            var7.show();
            return;
         }
      }

      ArrayList var6;
      if (this.actionBarLayout.fragmentsStack.size() != 0) {
         var6 = this.actionBarLayout.fragmentsStack;
         ((BaseFragment)var6.get(var6.size() - 1)).onRequestPermissionsResultFragment(var1, var2, var3);
      }

      if (AndroidUtilities.isTablet()) {
         if (this.rightActionBarLayout.fragmentsStack.size() != 0) {
            var6 = this.rightActionBarLayout.fragmentsStack;
            ((BaseFragment)var6.get(var6.size() - 1)).onRequestPermissionsResultFragment(var1, var2, var3);
         }

         if (this.layersActionBarLayout.fragmentsStack.size() != 0) {
            var6 = this.layersActionBarLayout.fragmentsStack;
            ((BaseFragment)var6.get(var6.size() - 1)).onRequestPermissionsResultFragment(var1, var2, var3);
         }
      }

   }

   protected void onResume() {
      super.onResume();
      MediaController.getInstance().setFeedbackView(this.actionBarLayout, true);
      ApplicationLoader.mainInterfacePaused = false;
      this.showLanguageAlert(false);
      Utilities.stageQueue.postRunnable(_$$Lambda$LaunchActivity$ZNfXQPqW9KpGIQ6C_so6U6aBbbU.INSTANCE);
      this.checkFreeDiscSpace();
      MediaController.checkGallery();
      this.onPasscodeResume();
      if (this.passcodeView.getVisibility() != 0) {
         this.actionBarLayout.onResume();
         if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.onResume();
            this.layersActionBarLayout.onResume();
         }
      } else {
         this.actionBarLayout.dismissDialogs();
         if (AndroidUtilities.isTablet()) {
            this.rightActionBarLayout.dismissDialogs();
            this.layersActionBarLayout.dismissDialogs();
         }

         this.passcodeView.onResume();
      }

      AndroidUtilities.checkForCrashes(this);
      AndroidUtilities.checkForUpdates(this);
      ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
      this.updateCurrentConnectionState(this.currentAccount);
      if (PhotoViewer.hasInstance() && PhotoViewer.getInstance().isVisible()) {
         PhotoViewer.getInstance().onResume();
      }

      if (PipRoundVideoView.getInstance() != null && MediaController.getInstance().isMessagePaused()) {
         MessageObject var1 = MediaController.getInstance().getPlayingMessageObject();
         if (var1 != null) {
            MediaController.getInstance().seekToProgress(var1, var1.audioProgress);
         }
      }

      if (UserConfig.getInstance(UserConfig.selectedAccount).unacceptedTermsOfService != null) {
         int var2 = UserConfig.selectedAccount;
         this.showTosActivity(var2, UserConfig.getInstance(var2).unacceptedTermsOfService);
      } else if (UserConfig.getInstance(0).pendingAppUpdate != null) {
         this.showUpdateActivity(UserConfig.selectedAccount, UserConfig.getInstance(0).pendingAppUpdate);
      }

      this.checkAppUpdate(false);
   }

   protected void onSaveInstanceState(Bundle var1) {
      Exception var10000;
      label174: {
         boolean var10001;
         try {
            super.onSaveInstanceState(var1);
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label174;
         }

         BaseFragment var2 = null;

         label167: {
            label157: {
               try {
                  if (AndroidUtilities.isTablet()) {
                     if (this.layersActionBarLayout.fragmentsStack.isEmpty()) {
                        break label157;
                     }

                     var2 = (BaseFragment)this.layersActionBarLayout.fragmentsStack.get(this.layersActionBarLayout.fragmentsStack.size() - 1);
                     break label167;
                  }
               } catch (Exception var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label174;
               }

               try {
                  if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                     var2 = (BaseFragment)this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
                  }
                  break label167;
               } catch (Exception var17) {
                  var10000 = var17;
                  var10001 = false;
                  break label174;
               }
            }

            try {
               if (!this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                  var2 = (BaseFragment)this.rightActionBarLayout.fragmentsStack.get(this.rightActionBarLayout.fragmentsStack.size() - 1);
                  break label167;
               }
            } catch (Exception var18) {
               var10000 = var18;
               var10001 = false;
               break label174;
            }

            try {
               if (!this.actionBarLayout.fragmentsStack.isEmpty()) {
                  var2 = (BaseFragment)this.actionBarLayout.fragmentsStack.get(this.actionBarLayout.fragmentsStack.size() - 1);
               }
            } catch (Exception var16) {
               var10000 = var16;
               var10001 = false;
               break label174;
            }
         }

         if (var2 == null) {
            return;
         }

         Bundle var3;
         boolean var4;
         try {
            var3 = var2.getArguments();
            var4 = var2 instanceof ChatActivity;
         } catch (Exception var15) {
            var10000 = var15;
            var10001 = false;
            break label174;
         }

         if (var4 && var3 != null) {
            try {
               var1.putBundle("args", var3);
               var1.putString("fragment", "chat");
            } catch (Exception var9) {
               var10000 = var9;
               var10001 = false;
               break label174;
            }
         } else {
            label175: {
               try {
                  if (var2 instanceof SettingsActivity) {
                     var1.putString("fragment", "settings");
                     break label175;
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label174;
               }

               label121: {
                  try {
                     if (!(var2 instanceof GroupCreateFinalActivity)) {
                        break label121;
                     }
                  } catch (Exception var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label174;
                  }

                  if (var3 != null) {
                     try {
                        var1.putBundle("args", var3);
                        var1.putString("fragment", "group");
                        break label175;
                     } catch (Exception var8) {
                        var10000 = var8;
                        var10001 = false;
                        break label174;
                     }
                  }
               }

               try {
                  if (var2 instanceof WallpapersListActivity) {
                     var1.putString("fragment", "wallpapers");
                     break label175;
                  }
               } catch (Exception var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label174;
               }

               label107: {
                  try {
                     if (!(var2 instanceof ProfileActivity) || !((ProfileActivity)var2).isChat()) {
                        break label107;
                     }
                  } catch (Exception var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label174;
                  }

                  if (var3 != null) {
                     try {
                        var1.putBundle("args", var3);
                        var1.putString("fragment", "chat_profile");
                        break label175;
                     } catch (Exception var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label174;
                     }
                  }
               }

               try {
                  if (!(var2 instanceof ChannelCreateActivity)) {
                     break label175;
                  }
               } catch (Exception var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label174;
               }

               if (var3 != null) {
                  try {
                     if (var3.getInt("step") == 0) {
                        var1.putBundle("args", var3);
                        var1.putString("fragment", "channel");
                     }
                  } catch (Exception var6) {
                     var10000 = var6;
                     var10001 = false;
                     break label174;
                  }
               }
            }
         }

         try {
            var2.saveSelfArgs(var1);
            return;
         } catch (Exception var5) {
            var10000 = var5;
            var10001 = false;
         }
      }

      Exception var21 = var10000;
      FileLog.e((Throwable)var21);
   }

   protected void onStart() {
      super.onStart();
      Browser.bindCustomTabsService(this);
   }

   protected void onStop() {
      super.onStop();
      Browser.unbindCustomTabsService(this);
   }

   public void presentFragment(BaseFragment var1) {
      this.actionBarLayout.presentFragment(var1);
   }

   public boolean presentFragment(BaseFragment var1, boolean var2, boolean var3) {
      return this.actionBarLayout.presentFragment(var1, var2, var3, true, false);
   }

   public void rebuildAllFragments(boolean var1) {
      ActionBarLayout var2 = this.layersActionBarLayout;
      if (var2 != null) {
         var2.rebuildAllFragmentViews(var1, var1);
      } else {
         this.actionBarLayout.rebuildAllFragmentViews(var1, var1);
      }

   }

   public AlertDialog showAlertDialog(AlertDialog.Builder var1) {
      try {
         if (this.visibleDialog != null) {
            this.visibleDialog.dismiss();
            this.visibleDialog = null;
         }
      } catch (Exception var4) {
         FileLog.e((Throwable)var4);
      }

      try {
         this.visibleDialog = var1.show();
         this.visibleDialog.setCanceledOnTouchOutside(true);
         AlertDialog var2 = this.visibleDialog;
         OnDismissListener var5 = new OnDismissListener() {
            public void onDismiss(DialogInterface var1) {
               if (LaunchActivity.this.visibleDialog != null) {
                  if (LaunchActivity.this.visibleDialog == LaunchActivity.this.localeDialog) {
                     label39: {
                        Exception var10000;
                        label44: {
                           boolean var10001;
                           LaunchActivity var2;
                           LaunchActivity var3;
                           HashMap var8;
                           label36: {
                              try {
                                 String var7 = LocaleController.getInstance().getCurrentLocaleInfo().shortName;
                                 var2 = LaunchActivity.this;
                                 var3 = LaunchActivity.this;
                                 if (var7.equals("en")) {
                                    var8 = LaunchActivity.this.englishLocaleStrings;
                                    break label36;
                                 }
                              } catch (Exception var6) {
                                 var10000 = var6;
                                 var10001 = false;
                                 break label44;
                              }

                              try {
                                 var8 = LaunchActivity.this.systemLocaleStrings;
                              } catch (Exception var5) {
                                 var10000 = var5;
                                 var10001 = false;
                                 break label44;
                              }
                           }

                           try {
                              Toast.makeText(var2, var3.getStringForLanguageAlert(var8, "ChangeLanguageLater", 2131558906), 1).show();
                              break label39;
                           } catch (Exception var4) {
                              var10000 = var4;
                              var10001 = false;
                           }
                        }

                        Exception var9 = var10000;
                        FileLog.e((Throwable)var9);
                     }

                     LaunchActivity.this.localeDialog = null;
                  } else if (LaunchActivity.this.visibleDialog == LaunchActivity.this.proxyErrorDialog) {
                     MessagesController.getGlobalMainSettings();
                     Editor var10 = MessagesController.getGlobalMainSettings().edit();
                     var10.putBoolean("proxy_enabled", false);
                     var10.putBoolean("proxy_enabled_calls", false);
                     var10.commit();
                     NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.proxySettingsChanged);
                     ConnectionsManager.setProxySettings(false, "", 1080, "", "", "");
                     NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.proxySettingsChanged);
                     LaunchActivity.this.proxyErrorDialog = null;
                  }
               }

               LaunchActivity.this.visibleDialog = null;
            }
         };
         var2.setOnDismissListener(var5);
         AlertDialog var6 = this.visibleDialog;
         return var6;
      } catch (Exception var3) {
         FileLog.e((Throwable)var3);
         return null;
      }
   }

   public void switchToAccount(int var1, boolean var2) {
      if (var1 != UserConfig.selectedAccount) {
         ConnectionsManager.getInstance(this.currentAccount).setAppPaused(true, false);
         UserConfig.selectedAccount = var1;
         UserConfig.getInstance(0).saveConfig(false);
         this.checkCurrentAccount();
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.removeAllFragments();
            this.rightActionBarLayout.removeAllFragments();
            if (!this.tabletFullSize) {
               this.shadowTabletSide.setVisibility(0);
               if (this.rightActionBarLayout.fragmentsStack.isEmpty()) {
                  this.backgroundTablet.setVisibility(0);
               }

               this.rightActionBarLayout.setVisibility(8);
            }

            this.layersActionBarLayout.setVisibility(8);
         }

         if (var2) {
            this.actionBarLayout.removeAllFragments();
         } else {
            this.actionBarLayout.removeFragmentFromStack(0);
         }

         DialogsActivity var3 = new DialogsActivity((Bundle)null);
         var3.setSideMenu(this.sideMenu);
         this.actionBarLayout.addFragmentToStack(var3, 0);
         this.drawerLayoutContainer.setAllowOpenDrawer(true, false);
         this.actionBarLayout.showLastFragment();
         if (AndroidUtilities.isTablet()) {
            this.layersActionBarLayout.showLastFragment();
            this.rightActionBarLayout.showLastFragment();
         }

         if (!ApplicationLoader.mainInterfacePaused) {
            ConnectionsManager.getInstance(this.currentAccount).setAppPaused(false, false);
         }

         if (UserConfig.getInstance(var1).unacceptedTermsOfService != null) {
            this.showTosActivity(var1, UserConfig.getInstance(var1).unacceptedTermsOfService);
         }

      }
   }
}
