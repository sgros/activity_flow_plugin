package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import java.net.URLEncoder;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.ShareAlert;

public class WebviewActivity extends BaseFragment {
   private static final int TYPE_GAME = 0;
   private static final int TYPE_STAT = 1;
   private static final int open_in = 2;
   private static final int share = 1;
   private String currentBot;
   private long currentDialogId;
   private String currentGame;
   private MessageObject currentMessageObject;
   private String currentUrl;
   private String linkToCopy;
   private boolean loadStats;
   private ActionBarMenuItem progressItem;
   private ContextProgressView progressView;
   private String short_param;
   private int type;
   public Runnable typingRunnable = new Runnable() {
      public void run() {
         if (WebviewActivity.this.currentMessageObject != null && WebviewActivity.this.getParentActivity() != null) {
            WebviewActivity var1 = WebviewActivity.this;
            if (var1.typingRunnable != null) {
               MessagesController.getInstance(WebviewActivity.access$200(var1)).sendTyping(WebviewActivity.this.currentMessageObject.getDialogId(), 6, 0);
               AndroidUtilities.runOnUIThread(WebviewActivity.this.typingRunnable, 25000L);
            }
         }

      }
   };
   private WebView webView;

   public WebviewActivity(String var1, long var2) {
      this.currentUrl = var1;
      this.currentDialogId = var2;
      this.type = 1;
   }

   public WebviewActivity(String var1, String var2, String var3, String var4, MessageObject var5) {
      this.currentUrl = var1;
      this.currentBot = var2;
      this.currentGame = var3;
      this.currentMessageObject = var5;
      this.short_param = var4;
      StringBuilder var7 = new StringBuilder();
      var7.append("https://");
      var7.append(MessagesController.getInstance(super.currentAccount).linkPrefix);
      var7.append("/");
      var7.append(this.currentBot);
      if (TextUtils.isEmpty(var4)) {
         var1 = "";
      } else {
         StringBuilder var6 = new StringBuilder();
         var6.append("?game=");
         var6.append(var4);
         var1 = var6.toString();
      }

      var7.append(var1);
      this.linkToCopy = var7.toString();
      this.type = 0;
   }

   // $FF: synthetic method
   static int access$200(WebviewActivity var0) {
      return var0.currentAccount;
   }

   public static void openGameInBrowser(String var0, MessageObject var1, Activity var2, String var3, String var4) {
      String var5 = "";

      Exception var10000;
      label128: {
         SharedPreferences var6;
         StringBuilder var7;
         String var8;
         StringBuilder var9;
         boolean var10001;
         try {
            var6 = ApplicationLoader.applicationContext.getSharedPreferences("botshare", 0);
            var7 = new StringBuilder();
            var7.append("");
            var7.append(var1.getId());
            var8 = var6.getString(var7.toString(), (String)null);
            var9 = new StringBuilder;
         } catch (Exception var24) {
            var10000 = var24;
            var10001 = false;
            break label128;
         }

         String var29;
         if (var8 != null) {
            var29 = var8;
         } else {
            var29 = "";
         }

         try {
            var9.<init>(var29);
            StringBuilder var10 = new StringBuilder();
            var10.append("tgShareScoreUrl=");
            var10.append(URLEncoder.encode("tgb://share_game_score?hash=", "UTF-8"));
            var7 = new StringBuilder(var10.toString());
         } catch (Exception var23) {
            var10000 = var23;
            var10001 = false;
            break label128;
         }

         int var11;
         if (var8 == null) {
            char[] var30;
            try {
               var30 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            } catch (Exception var22) {
               var10000 = var22;
               var10001 = false;
               break label128;
            }

            for(var11 = 0; var11 < 20; ++var11) {
               try {
                  var9.append(var30[Utilities.random.nextInt(var30.length)]);
               } catch (Exception var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label128;
               }
            }
         }

         try {
            var7.append(var9);
            var11 = var0.indexOf(35);
         } catch (Exception var20) {
            var10000 = var20;
            var10001 = false;
            break label128;
         }

         StringBuilder var31;
         if (var11 < 0) {
            try {
               var31 = new StringBuilder();
               var31.append(var0);
               var31.append("#");
               var31.append(var7);
               var0 = var31.toString();
            } catch (Exception var17) {
               var10000 = var17;
               var10001 = false;
               break label128;
            }
         } else {
            label134: {
               label133: {
                  try {
                     var8 = var0.substring(var11 + 1);
                     if (var8.indexOf(61) < 0 && var8.indexOf(63) < 0) {
                        break label133;
                     }
                  } catch (Exception var19) {
                     var10000 = var19;
                     var10001 = false;
                     break label128;
                  }

                  try {
                     var31 = new StringBuilder();
                     var31.append(var0);
                     var31.append("&");
                     var31.append(var7);
                     var0 = var31.toString();
                     break label134;
                  } catch (Exception var15) {
                     var10000 = var15;
                     var10001 = false;
                     break label128;
                  }
               }

               try {
                  if (var8.length() > 0) {
                     var31 = new StringBuilder();
                     var31.append(var0);
                     var31.append("?");
                     var31.append(var7);
                     var0 = var31.toString();
                     break label134;
                  }
               } catch (Exception var18) {
                  var10000 = var18;
                  var10001 = false;
                  break label128;
               }

               try {
                  var31 = new StringBuilder();
                  var31.append(var0);
                  var31.append(var7);
                  var0 = var31.toString();
               } catch (Exception var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label128;
               }
            }
         }

         String var25;
         StringBuilder var28;
         Editor var32;
         String var33;
         SerializedData var34;
         label74: {
            label73: {
               try {
                  var32 = var6.edit();
                  var31 = new StringBuilder();
                  var31.append(var9);
                  var31.append("_date");
                  var32.putInt(var31.toString(), (int)(System.currentTimeMillis() / 1000L));
                  var34 = new SerializedData(var1.messageOwner.getObjectSize());
                  var1.messageOwner.serializeToStream(var34);
                  var28 = new StringBuilder();
                  var28.append(var9);
                  var28.append("_m");
                  var32.putString(var28.toString(), Utilities.bytesToHex(var34.toByteArray()));
                  var28 = new StringBuilder();
                  var28.append(var9);
                  var28.append("_link");
                  var33 = var28.toString();
                  var28 = new StringBuilder();
                  var28.append("https://");
                  var28.append(MessagesController.getInstance(var1.currentAccount).linkPrefix);
                  var28.append("/");
                  var28.append(var4);
                  if (!TextUtils.isEmpty(var3)) {
                     break label73;
                  }
               } catch (Exception var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label128;
               }

               var25 = var5;
               break label74;
            }

            try {
               StringBuilder var27 = new StringBuilder();
               var27.append("?game=");
               var27.append(var3);
               var25 = var27.toString();
            } catch (Exception var13) {
               var10000 = var13;
               var10001 = false;
               break label128;
            }
         }

         try {
            var28.append(var25);
            var32.putString(var33, var28.toString());
            var32.commit();
            Browser.openUrl(var2, (String)var0, false);
            var34.cleanup();
            return;
         } catch (Exception var12) {
            var10000 = var12;
            var10001 = false;
         }
      }

      Exception var26 = var10000;
      FileLog.e((Throwable)var26);
   }

   private void reloadStats(String var1) {
      if (!this.loadStats) {
         this.loadStats = true;
         TLRPC.TL_messages_getStatsURL var2 = new TLRPC.TL_messages_getStatsURL();
         var2.peer = MessagesController.getInstance(super.currentAccount).getInputPeer((int)this.currentDialogId);
         if (var1 == null) {
            var1 = "";
         }

         var2.params = var1;
         var2.dark = Theme.getCurrentTheme().isDark();
         ConnectionsManager.getInstance(super.currentAccount).sendRequest(var2, new _$$Lambda$WebviewActivity$O62eLYvTahA2PRe6UItNx5NI6ao(this));
      }
   }

   public static boolean supportWebview() {
      String var0 = Build.MANUFACTURER;
      String var1 = Build.MODEL;
      return !"samsung".equals(var0) || !"GT-I9500".equals(var1);
   }

   @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
   public View createView(Context var1) {
      super.swipeBackEnabled = false;
      super.actionBar.setBackButtonImage(2131165409);
      super.actionBar.setAllowOverlayTitle(true);
      super.actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
         public void onItemClick(int var1) {
            if (var1 == -1) {
               WebviewActivity.this.finishFragment();
            } else if (var1 == 1) {
               if (WebviewActivity.this.currentMessageObject != null) {
                  WebviewActivity.this.currentMessageObject.messageOwner.with_my_score = false;
                  WebviewActivity var2 = WebviewActivity.this;
                  var2.showDialog(ShareAlert.createShareAlert(var2.getParentActivity(), WebviewActivity.this.currentMessageObject, (String)null, false, WebviewActivity.this.linkToCopy, false));
               }
            } else if (var1 == 2) {
               WebviewActivity.openGameInBrowser(WebviewActivity.this.currentUrl, WebviewActivity.this.currentMessageObject, WebviewActivity.this.getParentActivity(), WebviewActivity.this.short_param, WebviewActivity.this.currentBot);
            }

         }
      });
      ActionBarMenu var2 = super.actionBar.createMenu();
      this.progressItem = var2.addItemWithWidth(1, 2131165818, AndroidUtilities.dp(54.0F));
      int var3 = this.type;
      if (var3 == 0) {
         var2.addItem(0, 2131165416).addSubItem(2, 2131165649, LocaleController.getString("OpenInExternalApp", 2131560116));
         super.actionBar.setTitle(this.currentGame);
         ActionBar var4 = super.actionBar;
         StringBuilder var6 = new StringBuilder();
         var6.append("@");
         var6.append(this.currentBot);
         var4.setSubtitle(var6.toString());
         this.progressView = new ContextProgressView(var1, 1);
         this.progressItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
         this.progressView.setAlpha(0.0F);
         this.progressView.setScaleX(0.1F);
         this.progressView.setScaleY(0.1F);
         this.progressView.setVisibility(4);
      } else if (var3 == 1) {
         super.actionBar.setBackgroundColor(Theme.getColor("player_actionBar"));
         super.actionBar.setItemsColor(Theme.getColor("player_actionBarItems"), false);
         super.actionBar.setItemsBackgroundColor(Theme.getColor("player_actionBarSelector"), false);
         super.actionBar.setTitleColor(Theme.getColor("player_actionBarTitle"));
         super.actionBar.setSubtitleColor(Theme.getColor("player_actionBarSubtitle"));
         super.actionBar.setTitle(LocaleController.getString("Statistics", 2131560806));
         this.progressView = new ContextProgressView(var1, 3);
         this.progressItem.addView(this.progressView, LayoutHelper.createFrame(-1, -1.0F));
         this.progressView.setAlpha(1.0F);
         this.progressView.setScaleX(1.0F);
         this.progressView.setScaleY(1.0F);
         this.progressView.setVisibility(0);
         this.progressItem.getImageView().setVisibility(8);
         this.progressItem.setEnabled(false);
      }

      this.webView = new WebView(var1);
      this.webView.getSettings().setJavaScriptEnabled(true);
      this.webView.getSettings().setDomStorageEnabled(true);
      super.fragmentView = new FrameLayout(var1);
      FrameLayout var5 = (FrameLayout)super.fragmentView;
      if (VERSION.SDK_INT >= 19) {
         this.webView.setLayerType(2, (Paint)null);
      }

      if (VERSION.SDK_INT >= 21) {
         this.webView.getSettings().setMixedContentMode(0);
         CookieManager.getInstance().setAcceptThirdPartyCookies(this.webView, true);
         if (this.type == 0) {
            this.webView.addJavascriptInterface(new WebviewActivity.TelegramWebviewProxy(), "TelegramWebviewProxy");
         }
      }

      this.webView.setWebViewClient(new WebViewClient() {
         private boolean isInternalUrl(String var1) {
            if (TextUtils.isEmpty(var1)) {
               return false;
            } else {
               Uri var2 = Uri.parse(var1);
               if ("tg".equals(var2.getScheme())) {
                  if (WebviewActivity.this.type == 1) {
                     try {
                        Uri var5 = Uri.parse(var1.replace("tg:statsrefresh", "tg://telegram.org"));
                        WebviewActivity.this.reloadStats(var5.getQueryParameter("params"));
                     } catch (Throwable var4) {
                        FileLog.e(var4);
                     }
                  } else {
                     WebviewActivity.this.finishFragment(false);

                     try {
                        Intent var6 = new Intent("android.intent.action.VIEW", var2);
                        ComponentName var7 = new ComponentName(ApplicationLoader.applicationContext.getPackageName(), LaunchActivity.class.getName());
                        var6.setComponent(var7);
                        var6.putExtra("com.android.browser.application_id", ApplicationLoader.applicationContext.getPackageName());
                        ApplicationLoader.applicationContext.startActivity(var6);
                     } catch (Exception var3) {
                        FileLog.e((Throwable)var3);
                     }
                  }

                  return true;
               } else {
                  return false;
               }
            }
         }

         public void onLoadResource(WebView var1, String var2) {
            if (!this.isInternalUrl(var2)) {
               super.onLoadResource(var1, var2);
            }
         }

         public void onPageFinished(WebView var1, String var2) {
            super.onPageFinished(var1, var2);
            if (WebviewActivity.this.progressView != null && WebviewActivity.this.progressView.getVisibility() == 0) {
               AnimatorSet var3 = new AnimatorSet();
               if (WebviewActivity.this.type == 0) {
                  WebviewActivity.this.progressItem.getImageView().setVisibility(0);
                  WebviewActivity.this.progressItem.setEnabled(true);
                  var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "scaleX", new float[]{1.0F, 0.1F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "scaleY", new float[]{1.0F, 0.1F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "alpha", new float[]{1.0F, 0.0F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressItem.getImageView(), "scaleX", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressItem.getImageView(), "scaleY", new float[]{0.0F, 1.0F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressItem.getImageView(), "alpha", new float[]{0.0F, 1.0F})});
               } else {
                  var3.playTogether(new Animator[]{ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "scaleX", new float[]{1.0F, 0.1F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "scaleY", new float[]{1.0F, 0.1F}), ObjectAnimator.ofFloat(WebviewActivity.this.progressView, "alpha", new float[]{1.0F, 0.0F})});
               }

               var3.addListener(new AnimatorListenerAdapter() {
                  public void onAnimationEnd(Animator var1) {
                     if (WebviewActivity.this.type == 1) {
                        WebviewActivity.this.progressItem.setVisibility(8);
                     } else {
                        WebviewActivity.this.progressView.setVisibility(4);
                     }

                  }
               });
               var3.setDuration(150L);
               var3.start();
            }

         }

         public boolean shouldOverrideUrlLoading(WebView var1, String var2) {
            boolean var3;
            if (!this.isInternalUrl(var2) && !super.shouldOverrideUrlLoading(var1, var2)) {
               var3 = false;
            } else {
               var3 = true;
            }

            return var3;
         }
      });
      var5.addView(this.webView, LayoutHelper.createFrame(-1, -1.0F));
      return super.fragmentView;
   }

   public ThemeDescription[] getThemeDescriptions() {
      return this.type == 0 ? new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefault"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultIcon"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSelector"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner2"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter2")} : new ThemeDescription[]{new ThemeDescription(super.fragmentView, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "windowBackgroundWhite"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_BACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBar"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarItems"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBTITLECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarTitle"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "player_actionBarSelector"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuBackground"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItem"), new ThemeDescription(super.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM | ThemeDescription.FLAG_IMAGECOLOR, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "actionBarDefaultSubmenuItemIcon"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressInner4"), new ThemeDescription(this.progressView, 0, (Class[])null, (Paint)null, (Drawable[])null, (ThemeDescription.ThemeDescriptionDelegate)null, "contextProgressOuter4")};
   }

   // $FF: synthetic method
   public void lambda$null$0$WebviewActivity(TLObject var1) {
      this.loadStats = false;
      if (var1 != null) {
         TLRPC.TL_statsURL var2 = (TLRPC.TL_statsURL)var1;
         WebView var3 = this.webView;
         String var4 = var2.url;
         this.currentUrl = var4;
         var3.loadUrl(var4);
      }

   }

   // $FF: synthetic method
   public void lambda$reloadStats$1$WebviewActivity(TLObject var1, TLRPC.TL_error var2) {
      AndroidUtilities.runOnUIThread(new _$$Lambda$WebviewActivity$sRTqxzi1H4zOl0Tg9DGFjQm9YEQ(this, var1));
   }

   public void onFragmentDestroy() {
      super.onFragmentDestroy();
      AndroidUtilities.cancelRunOnUIThread(this.typingRunnable);
      this.webView.setLayerType(0, (Paint)null);
      this.typingRunnable = null;

      Exception var10000;
      label33: {
         ViewParent var1;
         boolean var10001;
         try {
            var1 = this.webView.getParent();
         } catch (Exception var4) {
            var10000 = var4;
            var10001 = false;
            break label33;
         }

         if (var1 != null) {
            try {
               ((FrameLayout)var1).removeView(this.webView);
            } catch (Exception var3) {
               var10000 = var3;
               var10001 = false;
               break label33;
            }
         }

         try {
            this.webView.stopLoading();
            this.webView.loadUrl("about:blank");
            this.webView.destroy();
            this.webView = null;
            return;
         } catch (Exception var2) {
            var10000 = var2;
            var10001 = false;
         }
      }

      Exception var5 = var10000;
      FileLog.e((Throwable)var5);
   }

   public void onResume() {
      super.onResume();
      AndroidUtilities.cancelRunOnUIThread(this.typingRunnable);
      this.typingRunnable.run();
   }

   protected void onTransitionAnimationEnd(boolean var1, boolean var2) {
      if (var1 && !var2) {
         WebView var3 = this.webView;
         if (var3 != null) {
            var3.loadUrl(this.currentUrl);
         }
      }

   }

   private class TelegramWebviewProxy {
      private TelegramWebviewProxy() {
      }

      // $FF: synthetic method
      TelegramWebviewProxy(Object var2) {
         this();
      }

      // $FF: synthetic method
      public void lambda$postEvent$0$WebviewActivity$TelegramWebviewProxy(String var1) {
         if (WebviewActivity.this.getParentActivity() != null) {
            if (BuildVars.LOGS_ENABLED) {
               FileLog.d(var1);
            }

            byte var2 = -1;
            int var3 = var1.hashCode();
            if (var3 != -1788360622) {
               if (var3 == 406539826 && var1.equals("share_score")) {
                  var2 = 1;
               }
            } else if (var1.equals("share_game")) {
               var2 = 0;
            }

            if (var2 != 0) {
               if (var2 == 1) {
                  WebviewActivity.this.currentMessageObject.messageOwner.with_my_score = true;
               }
            } else {
               WebviewActivity.this.currentMessageObject.messageOwner.with_my_score = false;
            }

            WebviewActivity var4 = WebviewActivity.this;
            var4.showDialog(ShareAlert.createShareAlert(var4.getParentActivity(), WebviewActivity.this.currentMessageObject, (String)null, false, WebviewActivity.this.linkToCopy, false));
         }
      }

      @JavascriptInterface
      public void postEvent(String var1, String var2) {
         AndroidUtilities.runOnUIThread(new _$$Lambda$WebviewActivity$TelegramWebviewProxy$4b6v9uSCLRENGHQEEzvYSfqVMkw(this, var1));
      }
   }
}
