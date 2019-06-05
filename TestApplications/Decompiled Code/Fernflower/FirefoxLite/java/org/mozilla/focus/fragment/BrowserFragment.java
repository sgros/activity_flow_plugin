package org.mozilla.focus.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;
import com.airbnb.lottie.LottieAnimationView;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.WeakHashMap;
import org.mozilla.focus.Inject;
import org.mozilla.focus.download.EnqueueDownloadTask;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.screenshot.CaptureRunnable;
import org.mozilla.focus.tabs.TabCounter;
import org.mozilla.focus.tabs.tabtray.TabTray;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.AppConstants;
import org.mozilla.focus.utils.FileChooseAction;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.GeoPermissionCache;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.widget.FindInPage;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener$_CC;
import org.mozilla.focus.widget.TabRestoreMonitor;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.content.HomeFragmentViewState;
import org.mozilla.rocket.download.DownloadIndicatorIntroViewHelper;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.rocket.nightmode.themed.ThemedImageButton;
import org.mozilla.rocket.nightmode.themed.ThemedImageView;
import org.mozilla.rocket.nightmode.themed.ThemedLinearLayout;
import org.mozilla.rocket.nightmode.themed.ThemedRelativeLayout;
import org.mozilla.rocket.nightmode.themed.ThemedTextView;
import org.mozilla.rocket.nightmode.themed.ThemedView;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

public class BrowserFragment extends LocaleAwareFragment implements LifecycleOwner, OnClickListener, OnLongClickListener, ScreenNavigator.BrowserScreen, BackKeyHandleable {
   private static final Handler HANDLER = new Handler();
   private TransitionDrawable backgroundTransition;
   private ThemedRelativeLayout backgroundView;
   private ThemedView bottomMenuDivider;
   private ThemedLinearLayout browserContainer;
   private ThemedImageButton captureBtn;
   private CaptureRunnable.CaptureStateListener captureStateListener;
   private BrowserFragment.DownloadCallback downloadCallback = new BrowserFragment.DownloadCallback();
   private ImageView downloadIndicator;
   private View downloadIndicatorIntro;
   private LottieAnimationView downloadingIndicator;
   private FileChooseAction fileChooseAction;
   private FindInPage findInPage;
   private TabView.FullscreenCallback fullscreenCallback;
   private AlertDialog geoDialog;
   private Callback geolocationCallback;
   private String geolocationOrigin;
   private boolean hasPendingScreenCaptureTask = false;
   private boolean isLoading = false;
   private WeakReference loadStateListenerWeakReference = new WeakReference((Object)null);
   final SessionManager.Observer managerObserver;
   private ThemedImageButton menuBtn;
   private ThemedImageButton newTabBtn;
   private PermissionHandler permissionHandler;
   private AnimatedProgressBar progressView;
   private ThemedImageButton searchBtn;
   private SessionManager sessionManager;
   private BrowserFragment.SessionObserver sessionObserver = new BrowserFragment.SessionObserver();
   private ThemedImageView siteIdentity;
   private int systemVisibility = -1;
   private TabCounter tabCounter;
   private ThemedLinearLayout toolbarRoot;
   private ThemedView urlBarDivider;
   private ThemedTextView urlView;
   private ViewGroup videoContainer;
   private Dialog webContextMenu;
   private ViewGroup webViewSlot;

   public BrowserFragment() {
      this.managerObserver = new BrowserFragment.SessionManagerObserver(this.sessionObserver);
   }

   private void acceptGeoRequest(boolean var1) {
      if (this.geolocationCallback != null) {
         if (var1) {
            GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.TRUE);
         }

         this.geolocationCallback.invoke(this.geolocationOrigin, true, false);
         this.geolocationOrigin = "";
         this.geolocationCallback = null;
      }
   }

   private void dismissDownloadIndicatorIntroView() {
      if (this.downloadIndicatorIntro != null) {
         this.downloadIndicatorIntro.setVisibility(8);
      }

   }

   private Bitmap getPageBitmap(WebView var1) {
      DisplayMetrics var2 = new DisplayMetrics();
      this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(var2);

      try {
         Bitmap var5 = Bitmap.createBitmap(var1.getWidth(), (int)((float)var1.getContentHeight() * var2.density), Config.RGB_565);
         Canvas var3 = new Canvas(var5);
         var1.draw(var3);
         return var5;
      } catch (OutOfMemoryError | Exception var4) {
         return null;
      }
   }

   private void hideFindInPage() {
      this.findInPage.hide();
   }

   private void initialiseNormalBrowserUi() {
      this.urlView.setOnClickListener(this);
   }

   private boolean isPopupWindowAllowed() {
      boolean var1;
      if (ScreenNavigator.get(this.getContext()).isBrowserInForeground() && !TabTray.isShowing(this.getFragmentManager())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   private boolean isStartedFromExternalApp() {
      FragmentActivity var1 = this.getActivity();
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         Intent var5 = var1.getIntent();
         boolean var3;
         if (var5 != null && var5.getBooleanExtra("is_internal_request", false)) {
            var3 = true;
         } else {
            var3 = false;
         }

         boolean var4 = var2;
         if (var5 != null) {
            var4 = var2;
            if ("android.intent.action.VIEW".equals(var5.getAction())) {
               var4 = var2;
               if (!var3) {
                  var4 = true;
               }
            }
         }

         return var4;
      }
   }

   private boolean isTabRestoredComplete() {
      if (!(this.getActivity() instanceof TabRestoreMonitor)) {
         if (!AppConstants.isDevBuild()) {
            return true;
         } else {
            throw new RuntimeException("Base activity needs to implement TabRestoreMonitor");
         }
      } else {
         return ((TabRestoreMonitor)this.getActivity()).isTabRestoredComplete();
      }
   }

   // $FF: synthetic method
   static void lambda$buildGeoPromptDialog$3(CheckedTextView var0, View var1) {
      var0.toggle();
   }

   // $FF: synthetic method
   public static void lambda$null$1(BrowserFragment var0, View var1) {
      var0.downloadIndicatorIntro = var1;
   }

   // $FF: synthetic method
   static WindowInsets lambda$onCreateView$0(View var0, WindowInsets var1) {
      ((LayoutParams)var0.getLayoutParams()).topMargin = var1.getSystemWindowInsetTop();
      return var1;
   }

   // $FF: synthetic method
   public static void lambda$onCreateView$2(BrowserFragment var0, ViewGroup var1, DownloadIndicatorViewModel.Status var2) {
      if (var2 == DownloadIndicatorViewModel.Status.DOWNLOADING) {
         var0.downloadIndicator.setVisibility(8);
         var0.downloadingIndicator.setVisibility(0);
         if (!var0.downloadingIndicator.isAnimating()) {
            var0.downloadingIndicator.playAnimation();
         }
      } else if (var2 == DownloadIndicatorViewModel.Status.UNREAD) {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(0);
         var0.downloadIndicator.setImageResource(2131230948);
      } else if (var2 == DownloadIndicatorViewModel.Status.WARNING) {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(0);
         var0.downloadIndicator.setImageResource(2131230949);
      } else {
         var0.downloadingIndicator.setVisibility(8);
         var0.downloadIndicator.setVisibility(8);
      }

      Settings.EventHistory var3 = Settings.getInstance(var0.getActivity()).getEventHistory();
      if (!var3.contains("dl_indicator_intro") && var2 != DownloadIndicatorViewModel.Status.DEFAULT) {
         var3.add("dl_indicator_intro");
         DownloadIndicatorIntroViewHelper.INSTANCE.initDownloadIndicatorIntroView(var0, var0.menuBtn, var1, new _$$Lambda$BrowserFragment$4s_5qYgCoiDUfvrmBOxvJDEcYeU(var0));
      }

   }

   private void queueDownload(Download var1) {
      if (this.getActivity() != null && var1 != null) {
         (new EnqueueDownloadTask(this.getActivity(), var1, this.getUrl())).execute(new Void[0]);
      }
   }

   private void rejectGeoRequest(boolean var1) {
      if (this.geolocationCallback != null) {
         if (var1) {
            GeoPermissionCache.putAllowed(this.geolocationOrigin, Boolean.FALSE);
         }

         this.geolocationCallback.invoke(this.geolocationOrigin, false, false);
         this.geolocationOrigin = "";
         this.geolocationCallback = null;
      }
   }

   private void showGeolocationPermissionPrompt() {
      if (this.isPopupWindowAllowed()) {
         if (this.geolocationCallback != null) {
            if (this.geoDialog == null || !this.geoDialog.isShowing()) {
               Boolean var1 = GeoPermissionCache.getAllowed(this.geolocationOrigin);
               if (var1 != null) {
                  this.geolocationCallback.invoke(this.geolocationOrigin, var1, false);
               } else {
                  this.geoDialog = this.buildGeoPromptDialog();
                  this.geoDialog.show();
               }

            }
         }
      }
   }

   private void showLoadingAndCapture() {
      if (this.isResumed()) {
         this.hasPendingScreenCaptureTask = false;
         ScreenCaptureDialogFragment var1 = ScreenCaptureDialogFragment.newInstance();
         var1.show(this.getChildFragmentManager(), "capturingFragment");
         HANDLER.postDelayed(new CaptureRunnable(this.getContext(), this, var1, this.getActivity().findViewById(2131296374)), 150L);
      }
   }

   private void updateIsLoading(boolean var1) {
      this.isLoading = var1;
      BrowserFragment.LoadStateListener var2 = (BrowserFragment.LoadStateListener)this.loadStateListenerWeakReference.get();
      if (var2 != null) {
         var2.isLoadingChanged(var1);
      }

   }

   private void updateURL(String var1) {
      if (!UrlUtils.isInternalErrorURL(var1)) {
         this.urlView.setText(UrlUtils.stripUserInfo(var1));
      }
   }

   public void applyLocale() {
      (new WebView(this.getContext())).destroy();
   }

   public AlertDialog buildGeoPromptDialog() {
      View var1 = LayoutInflater.from(this.getContext()).inflate(2131492943, (ViewGroup)null);
      final CheckedTextView var2 = (CheckedTextView)var1.findViewById(2131296360);
      var2.setText(this.getString(2131755223, new Object[]{this.getString(2131755062)}));
      var2.setOnClickListener(new _$$Lambda$BrowserFragment$qv_dY4EfQ8BL4okjukxNbzZQYjI(var2));
      AlertDialog.Builder var3 = new AlertDialog.Builder(this.getContext());
      var3.setView(var1).setMessage(this.getString(2131755222, new Object[]{this.geolocationOrigin})).setCancelable(true).setPositiveButton(this.getString(2131755220), new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1, int var2x) {
            BrowserFragment.this.acceptGeoRequest(var2.isChecked());
         }
      }).setNegativeButton(this.getString(2131755221), new android.content.DialogInterface.OnClickListener() {
         public void onClick(DialogInterface var1, int var2x) {
            BrowserFragment.this.rejectGeoRequest(var2.isChecked());
         }
      }).setOnDismissListener(new OnDismissListener() {
         public void onDismiss(DialogInterface var1) {
            BrowserFragment.this.rejectGeoRequest(false);
         }
      }).setOnCancelListener(new OnCancelListener() {
         public void onCancel(DialogInterface var1) {
            BrowserFragment.this.rejectGeoRequest(false);
         }
      });
      return var3.create();
   }

   public boolean canGoBack() {
      boolean var1;
      if (this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoBack()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean canGoForward() {
      boolean var1;
      if (this.sessionManager.getFocusSession() != null && this.sessionManager.getFocusSession().getCanGoForward()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean capturePage(BrowserFragment.ScreenshotCallback var1) {
      Session var2 = this.sessionManager.getFocusSession();
      if (var2 == null) {
         return false;
      } else {
         TabView var3 = var2.getEngineSession().getTabView();
         if (var3 != null && var3 instanceof WebView) {
            Bitmap var4 = this.getPageBitmap((WebView)var3);
            if (var4 == null) {
               return false;
            } else {
               var1.onCaptureComplete(var3.getTitle(), var3.getUrl(), var4);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void dismissGeoDialog() {
      if (this.geoDialog != null) {
         this.geoDialog.dismiss();
         this.geoDialog = null;
      }

   }

   public void dismissWebContextMenu() {
      if (this.webContextMenu != null) {
         this.webContextMenu.dismiss();
         this.webContextMenu = null;
      }

   }

   public CaptureRunnable.CaptureStateListener getCaptureStateListener() {
      return this.captureStateListener;
   }

   public Fragment getFragment() {
      return this;
   }

   public String getUrl() {
      return this.urlView.getText().toString();
   }

   public void goBack() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         TabView var3 = var1.getEngineSession().getTabView();
         if (var3 == null || !var3.canGoBack()) {
            return;
         }

         WebBackForwardList var2 = ((WebView)var3).copyBackForwardList();
         this.updateURL(var2.getItemAtIndex(var2.getCurrentIndex() - 1).getUrl());
         var3.goBack();
      }

   }

   public void goBackground() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         TabViewEngineSession var2 = var1.getEngineSession();
         if (var2 != null) {
            var2.detach();
            TabView var3 = var2.getTabView();
            if (var3 != null) {
               this.webViewSlot.removeView(var3.getView());
            }
         }
      }

   }

   public void goForeground() {
      Session var1 = this.sessionManager.getFocusSession();
      if (this.webViewSlot.getChildCount() == 0 && var1 != null) {
         TabViewEngineSession var2 = var1.getEngineSession();
         if (var2 != null) {
            TabView var3 = var2.getTabView();
            if (var3 != null) {
               this.webViewSlot.addView(var3.getView());
            }
         }
      }

      this.setNightModeEnabled(Settings.getInstance(this.getActivity()).isNightModeEnable());
   }

   public void goForward() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         TabView var3 = var1.getEngineSession().getTabView();
         if (var3 == null) {
            return;
         }

         WebBackForwardList var2 = ((WebView)var3).copyBackForwardList();
         this.updateURL(var2.getItemAtIndex(var2.getCurrentIndex() + 1).getUrl());
         var3.goForward();
      }

   }

   public boolean isLoading() {
      return this.isLoading;
   }

   public void loadUrl(String var1, boolean var2, boolean var3, Runnable var4) {
      this.updateURL(var1);
      if (SupportUtils.isUrl(var1)) {
         if (var2) {
            this.sessionManager.addTab(var1, TabUtil.argument((String)null, var3, true));
            this.dismissDownloadIndicatorIntroView();
            ThreadUtils.postToMainThread(var4);
         } else {
            Session var5 = this.sessionManager.getFocusSession();
            if (var5 != null && var5.getEngineSession().getTabView() != null) {
               var5.getEngineSession().getTabView().loadUrl(var1);
               var4.run();
            } else {
               this.sessionManager.addTab(var1, TabUtil.argument((String)null, var3, true));
               ThreadUtils.postToMainThread(var4);
            }
         }
      } else if (AppConstants.isDevBuild()) {
         StringBuilder var6 = new StringBuilder();
         var6.append("trying to open a invalid url: ");
         var6.append(var1);
         throw new RuntimeException(var6.toString());
      }

   }

   public void onActivityResult(int var1, int var2, Intent var3) {
      this.permissionHandler.onActivityResult(this.getActivity(), var1, var2, var3);
      if (var1 == 103) {
         boolean var4;
         if (this.fileChooseAction != null && !this.fileChooseAction.onFileChose(var2, var3)) {
            var4 = false;
         } else {
            var4 = true;
         }

         if (var4) {
            this.fileChooseAction = null;
         }
      }

   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      this.permissionHandler = new PermissionHandler(new PermissionHandle() {
         private void actionCaptureGranted() {
            BrowserFragment.this.hasPendingScreenCaptureTask = true;
         }

         private void actionDownloadGranted(Parcelable var1) {
            Download var2 = (Download)var1;
            BrowserFragment.this.queueDownload(var2);
         }

         private void actionPickFileGranted() {
            if (BrowserFragment.this.fileChooseAction != null) {
               BrowserFragment.this.fileChooseAction.startChooserActivity();
            }

         }

         private void doActionGrantedOrSetting(String var1, int var2, Parcelable var3) {
            switch(var2) {
            case 0:
               this.actionDownloadGranted(var3);
               break;
            case 1:
               this.actionPickFileGranted();
               break;
            case 2:
               BrowserFragment.this.showGeolocationPermissionPrompt();
               break;
            case 3:
               this.actionCaptureGranted();
               break;
            default:
               throw new IllegalArgumentException("Unknown actionId");
            }

         }

         private int getAskAgainSnackBarString(int var1) {
            if (var1 != 0 && var1 != 1 && var1 != 3) {
               if (var1 == 2) {
                  return 2131755289;
               } else {
                  throw new IllegalArgumentException("Unknown Action");
               }
            } else {
               return 2131755291;
            }
         }

         private int getPermissionDeniedToastString(int var1) {
            if (var1 != 0 && var1 != 1 && var1 != 3) {
               if (var1 == 2) {
                  return 2131755290;
               } else {
                  throw new IllegalArgumentException("Unknown Action");
               }
            } else {
               return 2131755292;
            }
         }

         // $FF: synthetic method
         public static void lambda$doActionNoPermission$0(Object var0) {
            BrowserFragment.this.rejectGeoRequest(false);
         }

         public void doActionDirect(String var1, int var2, Parcelable var3) {
            switch(var2) {
            case 0:
               if (BrowserFragment.this.getContext() == null) {
                  Log.w("browser_screen", "No context to use, abort callback onDownloadStart");
                  return;
               }

               Download var4 = (Download)var3;
               if (ContextCompat.checkSelfPermission(BrowserFragment.this.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                  BrowserFragment.this.queueDownload(var4);
               }
               break;
            case 1:
               BrowserFragment.this.fileChooseAction.startChooserActivity();
               break;
            case 2:
               BrowserFragment.this.showGeolocationPermissionPrompt();
               break;
            case 3:
               BrowserFragment.this.showLoadingAndCapture();
               break;
            default:
               throw new IllegalArgumentException("Unknown actionId");
            }

         }

         public void doActionGranted(String var1, int var2, Parcelable var3) {
            this.doActionGrantedOrSetting(var1, var2, var3);
         }

         public void doActionNoPermission(String var1, int var2, Parcelable var3) {
            switch(var2) {
            case 0:
            case 3:
               break;
            case 1:
               if (BrowserFragment.this.fileChooseAction != null) {
                  BrowserFragment.this.fileChooseAction.cancel();
                  BrowserFragment.this.fileChooseAction = null;
               }
               break;
            case 2:
               if (BrowserFragment.this.geolocationCallback != null) {
                  ThreadUtils.postToMainThread(new _$$Lambda$BrowserFragment$1$O1zuVOux1ZaId_gKigNygb5Rzpc(this));
               }
               break;
            default:
               throw new IllegalArgumentException("Unknown actionId");
            }

         }

         public void doActionSetting(String var1, int var2, Parcelable var3) {
            this.doActionGrantedOrSetting(var1, var2, var3);
         }

         public Snackbar makeAskAgainSnackBar(int var1) {
            return PermissionHandler.makeAskAgainSnackBar((Fragment)BrowserFragment.this, BrowserFragment.this.getActivity().findViewById(2131296374), this.getAskAgainSnackBarString(var1));
         }

         public void permissionDeniedToast(int var1) {
            Toast.makeText(BrowserFragment.this.getContext(), this.getPermissionDeniedToastString(var1), 1).show();
         }

         public void requestPermissions(int var1) {
            switch(var1) {
            case 0:
            case 3:
               BrowserFragment.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, var1);
               break;
            case 1:
               BrowserFragment.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, var1);
               break;
            case 2:
               BrowserFragment.this.requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, var1);
               break;
            default:
               throw new IllegalArgumentException("Unknown Action");
            }

         }
      });
   }

   public boolean onBackPressed() {
      if (this.findInPage.onBackPressed()) {
         return true;
      } else {
         if (this.canGoBack()) {
            this.goBack();
         } else {
            Session var1 = this.sessionManager.getFocusSession();
            if (var1 == null) {
               return false;
            }

            if (!var1.isFromExternal() && !var1.hasParentTab()) {
               ScreenNavigator.get(this.getContext()).popToHomeScreen(true);
            } else {
               this.sessionManager.closeTab(var1.getId());
            }
         }

         return true;
      }
   }

   public void onCaptureClicked() {
      this.permissionHandler.tryAction((Fragment)this, "android.permission.WRITE_EXTERNAL_STORAGE", 3, (Parcelable)null);
   }

   public void onClick(View var1) {
      switch(var1.getId()) {
      case 2131296345:
         this.onCaptureClicked();
         break;
      case 2131296349:
         FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_MENU, (Object)null);
         TelemetryWrapper.showMenuToolbar();
         break;
      case 2131296354:
         HomeFragmentViewState.reset();
         ScreenNavigator.get(this.getContext()).addHomeScreen(true);
         TelemetryWrapper.clickAddTabToolbar();
         break;
      case 2131296356:
         FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_URL_INPUT, this.getUrl());
         TelemetryWrapper.clickToolbarSearch();
         break;
      case 2131296357:
         FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_TAB_TRAY, (Object)null);
         TelemetryWrapper.showTabTrayToolbar();
         break;
      case 2131296411:
         FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_URL_INPUT, this.getUrl());
         TelemetryWrapper.clickUrlbar();
         break;
      default:
         throw new IllegalArgumentException("Unhandled menu item in BrowserFragment");
      }

   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      View var4 = var1.inflate(2131492954, var2, false);
      this.videoContainer = (ViewGroup)var4.findViewById(2131296717);
      this.browserContainer = (ThemedLinearLayout)var4.findViewById(2131296337);
      this.urlView = (ThemedTextView)var4.findViewById(2131296411);
      this.backgroundView = (ThemedRelativeLayout)var4.findViewById(2131296301);
      var4.findViewById(2131296295).setOnApplyWindowInsetsListener(_$$Lambda$BrowserFragment$E4DFpfvxoIczJjyXxreM27CrkUE.INSTANCE);
      this.backgroundTransition = (TransitionDrawable)this.backgroundView.getBackground();
      this.tabCounter = (TabCounter)var4.findViewById(2131296357);
      this.newTabBtn = (ThemedImageButton)var4.findViewById(2131296354);
      this.searchBtn = (ThemedImageButton)var4.findViewById(2131296356);
      this.captureBtn = (ThemedImageButton)var4.findViewById(2131296345);
      this.menuBtn = (ThemedImageButton)var4.findViewById(2131296349);
      this.toolbarRoot = (ThemedLinearLayout)var4.findViewById(2131296701);
      this.bottomMenuDivider = (ThemedView)var4.findViewById(2131296327);
      this.urlBarDivider = (ThemedView)var4.findViewById(2131296713);
      if (this.tabCounter != null) {
         this.tabCounter.setOnClickListener(this);
      }

      if (this.newTabBtn != null) {
         this.newTabBtn.setOnClickListener(this);
      }

      if (this.searchBtn != null) {
         this.searchBtn.setOnClickListener(this);
      }

      if (this.captureBtn != null) {
         this.captureBtn.setOnClickListener(this);
      }

      if (this.menuBtn != null) {
         this.menuBtn.setOnClickListener(this);
         this.menuBtn.setOnLongClickListener(this);
      }

      this.siteIdentity = (ThemedImageView)var4.findViewById(2131296648);
      this.findInPage = new FindInPage(var4);
      this.progressView = (AnimatedProgressBar)var4.findViewById(2131296576);
      this.initialiseNormalBrowserUi();
      this.webViewSlot = (ViewGroup)var4.findViewById(2131296723);
      this.sessionManager = TabsSessionProvider.getOrThrow(this.getActivity());
      this.sessionManager.register((SessionManager.Observer)this.managerObserver, this, false);
      if (this.tabCounter != null && this.isTabRestoredComplete()) {
         this.tabCounter.setCount(this.sessionManager.getTabsCount());
      }

      this.setNightModeEnabled(Settings.getInstance(this.getActivity()).isNightModeEnable());
      this.downloadingIndicator = (LottieAnimationView)var4.findViewById(2131296418);
      this.downloadIndicator = (ImageView)var4.findViewById(2131296417);
      var2 = (ViewGroup)var4.findViewById(2131296338);
      Inject.obtainDownloadIndicatorViewModel(this.getActivity()).getDownloadIndicatorObservable().observe(this.getViewLifecycleOwner(), new _$$Lambda$BrowserFragment$l94_q85PEWz_WW25q4mSUZ5WGEE(this, var2));
      return var4;
   }

   public void onDestroyView() {
      this.sessionManager.unregister(this.managerObserver);
      super.onDestroyView();
   }

   public boolean onLongClick(View var1) {
      if (var1.getId() == 2131296349) {
         FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_DOWNLOAD_PANEL, (Object)null);
         TelemetryWrapper.longPressDownloadIndicator();
         return false;
      } else {
         throw new IllegalArgumentException("Unhandled long click menu item in BrowserFragment");
      }
   }

   public void onPause() {
      this.sessionManager.pause();
      super.onPause();
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      this.permissionHandler.onRequestPermissionsResult(this.getContext(), var1, var2, var3);
   }

   public void onResume() {
      this.sessionManager.resume();
      super.onResume();
      if (this.hasPendingScreenCaptureTask) {
         this.showLoadingAndCapture();
         this.hasPendingScreenCaptureTask = false;
      }

   }

   public void onSaveInstanceState(Bundle var1) {
      this.permissionHandler.onSaveInstanceState(var1);
      if (this.sessionManager.getFocusSession() != null) {
         TabViewEngineSession var2 = this.sessionManager.getFocusSession().getEngineSession();
         if (var2 != null && var2.getTabView() != null) {
            var2.getTabView().saveViewState(var1);
         }
      }

      if (var1.containsKey("WEBVIEW_CHROMIUM_STATE") && var1.getByteArray("WEBVIEW_CHROMIUM_STATE").length > 300000) {
         var1.remove("WEBVIEW_CHROMIUM_STATE");
      }

      super.onSaveInstanceState(var1);
   }

   public void onStop() {
      if (this.systemVisibility != -1) {
         Session var1 = this.sessionManager.getFocusSession();
         if (var1 != null) {
            TabView var2 = var1.getEngineSession().getTabView();
            if (var2 != null) {
               var2.performExitFullScreen();
            }
         }
      }

      this.dismissGeoDialog();
      super.onStop();
   }

   public void onViewCreated(View var1, Bundle var2) {
      super.onViewCreated(var1, var2);
      if (var2 != null) {
         Session var3 = this.sessionManager.getFocusSession();
         if (var3 != null) {
            TabView var4 = var3.getEngineSession().getTabView();
            if (var4 != null) {
               var4.restoreViewState(var2);
            } else {
               this.sessionManager.switchToTab(var3.getId());
            }
         }
      }

   }

   public void onViewStateRestored(Bundle var1) {
      super.onViewStateRestored(var1);
      if (var1 != null) {
         this.permissionHandler.onRestoreInstanceState(var1);
      }

   }

   public void reload() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         TabView var2 = var1.getEngineSession().getTabView();
         if (var2 == null) {
            return;
         }

         var2.reload();
      }

   }

   public void setContentBlockingEnabled(boolean var1) {
      Iterator var2 = this.sessionManager.getTabs().iterator();

      while(var2.hasNext()) {
         TabViewEngineSession var3 = ((Session)var2.next()).getEngineSession();
         if (var3 != null && var3.getTabView() != null) {
            var3.getTabView().setContentBlockingEnabled(var1);
         }
      }

   }

   public void setImageBlockingEnabled(boolean var1) {
      Iterator var2 = this.sessionManager.getTabs().iterator();

      while(var2.hasNext()) {
         TabViewEngineSession var3 = ((Session)var2.next()).getEngineSession();
         if (var3 != null && var3.getTabView() != null) {
            var3.getTabView().setImageBlockingEnabled(var1);
         }
      }

   }

   public void setNightModeEnabled(boolean var1) {
      this.browserContainer.setNightMode(var1);
      this.toolbarRoot.setNightMode(var1);
      this.urlView.setNightMode(var1);
      this.siteIdentity.setNightMode(var1);
      this.newTabBtn.setNightMode(var1);
      this.searchBtn.setNightMode(var1);
      this.captureBtn.setNightMode(var1);
      this.menuBtn.setNightMode(var1);
      this.tabCounter.setNightMode(var1);
      this.bottomMenuDivider.setNightMode(var1);
      this.backgroundView.setNightMode(var1);
      this.urlBarDivider.setNightMode(var1);
      ViewUtils.updateStatusBarStyle(var1 ^ true, this.getActivity().getWindow());
   }

   public void showFindInPage() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         this.findInPage.show(var1);
         TelemetryWrapper.findInPage(TelemetryWrapper.FIND_IN_PAGE.OPEN_BY_MENU);
      }

   }

   public void showMyShotOnBoarding() {
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         Settings.EventHistory var2 = Settings.getInstance(var1).getEventHistory();
         if (!var2.contains("show_my_shot_on_boarding_dialog")) {
            var2.add("show_my_shot_on_boarding_dialog");
            FragmentListener$_CC.notifyParent(this, FragmentListener.TYPE.SHOW_MY_SHOT_ON_BOARDING, (Object)null);
         }

      }
   }

   public void stop() {
      Session var1 = this.sessionManager.getFocusSession();
      if (var1 != null) {
         TabView var2 = var1.getEngineSession().getTabView();
         if (var2 == null) {
            return;
         }

         var2.stopLoading();
      }

   }

   public void switchToTab(String var1) {
      if (!TextUtils.isEmpty(var1)) {
         this.sessionManager.switchToTab(var1);
      }

   }

   class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback {
      public void onDownloadStart(Download var1) {
         FragmentActivity var2 = BrowserFragment.this.getActivity();
         if (var2 != null && var2.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            BrowserFragment.this.permissionHandler.tryAction((Fragment)BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, var1);
         }
      }
   }

   private final class HistoryInserter {
      private WeakHashMap failingUrls;
      private WeakHashMap lastInsertedUrls;

      private HistoryInserter() {
         this.failingUrls = new WeakHashMap();
         this.lastInsertedUrls = new WeakHashMap();
      }

      // $FF: synthetic method
      HistoryInserter(Object var2) {
         this();
      }

      private String getFailingUrl(Session var1) {
         String var2 = (String)this.failingUrls.get(var1);
         String var3 = var2;
         if (TextUtils.isEmpty(var2)) {
            var3 = "";
         }

         return var3;
      }

      private String getLastInsertedUrl(Session var1) {
         String var2 = (String)this.lastInsertedUrls.get(var1);
         String var3 = var2;
         if (TextUtils.isEmpty(var2)) {
            var3 = "";
         }

         return var3;
      }

      private void insertBrowsingHistory(Session var1) {
         String var2 = BrowserFragment.this.getUrl();
         String var3 = this.getLastInsertedUrl(var1);
         if (!TextUtils.isEmpty(var2)) {
            if (!var2.equals(this.getFailingUrl(var1))) {
               if (!var2.equals(var3)) {
                  TabView var4 = var1.getEngineSession().getTabView();
                  if (var4 != null) {
                     var4.insertBrowsingHistory();
                  }

                  this.lastInsertedUrls.put(var1, var2);
               }
            }
         }
      }

      void onTabFinished(Session var1) {
         this.insertBrowsingHistory(var1);
      }

      void onTabStarted(Session var1) {
         this.lastInsertedUrls.remove(var1);
      }

      void updateFailingUrl(Session var1, String var2, boolean var3) {
         String var4 = (String)this.failingUrls.get(var1);
         if (!var3 && !var2.equals(var4)) {
            this.failingUrls.remove(var1);
         } else {
            this.failingUrls.put(var1, var2);
         }

      }
   }

   public interface LoadStateListener {
      void isLoadingChanged(boolean var1);
   }

   public interface ScreenshotCallback {
      void onCaptureComplete(String var1, String var2, Bitmap var3);
   }

   class SessionManagerObserver implements SessionManager.Observer {
      private BrowserFragment.SessionObserver sessionObserver;
      private ValueAnimator tabTransitionAnimator;

      SessionManagerObserver(BrowserFragment.SessionObserver var2) {
         this.sessionObserver = var2;
      }

      private View findExistingTabView(ViewGroup var1) {
         int var2 = var1.getChildCount();

         for(int var3 = 0; var3 < var2; ++var3) {
            View var4 = var1.getChildAt(var3);
            if (var4 instanceof TabView) {
               return ((TabView)var4).getView();
            }
         }

         return null;
      }

      private void onTabAddedByContextMenu(final Session var1, Bundle var2) {
         if (!TabUtil.toFocus(var2)) {
            Snackbar.make(BrowserFragment.this.webViewSlot, 2131755272, 0).setAction(2131755273, new OnClickListener() {
               public void onClick(View var1x) {
                  BrowserFragment.this.sessionManager.switchToTab(var1.getId());
               }
            }).show();
         }

      }

      private void refreshChrome(Session var1) {
         BrowserFragment.this.geolocationOrigin = "";
         BrowserFragment.this.geolocationCallback = null;
         BrowserFragment.this.dismissGeoDialog();
         BrowserFragment.this.updateURL(var1.getUrl());
         BrowserFragment.this.progressView.setProgress(0);
         byte var2 = var1.getSecurityInfo().getSecure();
         BrowserFragment.this.siteIdentity.setImageLevel(var2);
         BrowserFragment.this.hideFindInPage();
      }

      private void startTransitionAnimation(final View var1, final View var2, final Runnable var3) {
         this.stopTabTransition();
         var2.setAlpha(0.0F);
         if (var1 != null) {
            var1.setAlpha(1.0F);
         }

         int var4 = var2.getResources().getInteger(2131361813);
         this.tabTransitionAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F}).setDuration((long)var4);
         this.tabTransitionAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator var1x) {
               float var2x = (Float)var1x.getAnimatedValue();
               if (var1 != null) {
                  var1.setAlpha(1.0F - var2x);
               }

               var2.setAlpha(var2x);
            }
         });
         this.tabTransitionAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator var1x) {
               if (var3 != null) {
                  var3.run();
               }

               var2.setAlpha(1.0F);
               if (var1 != null) {
                  var1.setAlpha(1.0F);
               }

            }
         });
         this.tabTransitionAnimator.start();
      }

      private void stopTabTransition() {
         if (this.tabTransitionAnimator != null && this.tabTransitionAnimator.isRunning()) {
            this.tabTransitionAnimator.end();
         }

      }

      private void transitToTab(Session var1) {
         TabView var2 = var1.getEngineSession().getTabView();
         if (var2 != null) {
            if (var1.getEngineSession() != null) {
               var1.getEngineSession().detach();
            }

            View var3 = this.findExistingTabView(BrowserFragment.this.webViewSlot);
            BrowserFragment.this.webViewSlot.removeView(var3);
            var3 = var2.getView();
            BrowserFragment.this.webViewSlot.addView(var3);
            this.sessionObserver.changeSession(var1);
            this.startTransitionAnimation((View)null, var3, (Runnable)null);
         } else {
            throw new RuntimeException("Tabview should be created at this moment and never be null");
         }
      }

      public boolean handleExternalUrl(String var1) {
         return this.sessionObserver.handleExternalUrl(var1);
      }

      public void onFocusChanged(Session var1, SessionManager.Factor var2) {
         if (var1 == null) {
            if (var2 == SessionManager.Factor.FACTOR_NO_FOCUS && !BrowserFragment.this.isStartedFromExternalApp()) {
               ScreenNavigator.get(BrowserFragment.this.getContext()).popToHomeScreen(true);
            } else {
               BrowserFragment.this.getActivity().finish();
            }
         } else {
            this.transitToTab(var1);
            this.refreshChrome(var1);
         }

      }

      public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3) {
         this.sessionObserver.onHttpAuthRequest(var1, var2, var3);
      }

      public void onSessionAdded(Session var1, Bundle var2) {
         if (var2 != null) {
            if (var2.getInt("extra_bkg_tab_src", -1) == 0) {
               this.onTabAddedByContextMenu(var1, var2);
            }

         }
      }

      public void onSessionCountChanged(int var1) {
         if (BrowserFragment.this.isTabRestoredComplete()) {
            BrowserFragment.this.tabCounter.setCountWithAnimation(var1);
         }

      }

      public boolean onShowFileChooser(TabViewEngineSession var1, ValueCallback var2, FileChooserParams var3) {
         return this.sessionObserver.onShowFileChooser(var1, var2, var3);
      }

      public void updateFailingUrl(String var1, boolean var2) {
         this.sessionObserver.updateFailingUrl(var1, var2);
      }
   }

   class SessionObserver implements Session.Observer, TabViewEngineSession.Client {
      private BrowserFragment.HistoryInserter historyInserter = BrowserFragment.this.new HistoryInserter();
      private String loadedUrl = null;
      private Session session;

      private boolean isForegroundSession(Session var1) {
         boolean var2;
         if (BrowserFragment.this.sessionManager.getFocusSession() == var1) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      // $FF: synthetic method
      static void lambda$onHttpAuthRequest$0(TabViewClient.HttpAuthCallback var0, String var1, String var2, String var3, String var4) {
         var0.proceed(var3, var4);
      }

      private void updateUrlFromWebView(Session var1) {
         if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
            this.onUrlChanged(var1, BrowserFragment.this.sessionManager.getFocusSession().getUrl());
         }

      }

      void changeSession(Session var1) {
         if (this.session != null) {
            this.session.unregister((Session.Observer)this);
         }

         this.session = var1;
         if (this.session != null) {
            this.session.register((Session.Observer)this);
         }

      }

      public boolean handleExternalUrl(String var1) {
         if (BrowserFragment.this.getContext() == null) {
            Log.w("browser_screen", "No context to use, abort callback handleExternalUrl");
            return false;
         } else {
            return IntentUtils.handleExternalUri(BrowserFragment.this.getContext(), var1);
         }
      }

      public boolean onDownload(Session var1, mozilla.components.browser.session.Download var2) {
         FragmentActivity var3 = BrowserFragment.this.getActivity();
         if (var3 != null && var3.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            Download var4 = new Download(var2.getUrl(), var2.getFileName(), var2.getUserAgent(), "", var2.getContentType(), var2.getContentLength(), false);
            BrowserFragment.this.permissionHandler.tryAction((Fragment)BrowserFragment.this, "android.permission.WRITE_EXTERNAL_STORAGE", 0, var4);
            return true;
         } else {
            return false;
         }
      }

      public void onEnterFullScreen(TabView.FullscreenCallback var1, View var2) {
         if (this.session != null) {
            if (!this.isForegroundSession(this.session)) {
               var1.fullScreenExited();
            } else {
               BrowserFragment.this.fullscreenCallback = var1;
               if (this.session.getEngineSession().getTabView() != null && var2 != null) {
                  BrowserFragment.this.browserContainer.setVisibility(4);
                  android.widget.FrameLayout.LayoutParams var3 = new android.widget.FrameLayout.LayoutParams(-1, -1);
                  BrowserFragment.this.videoContainer.addView(var2, var3);
                  BrowserFragment.this.videoContainer.setVisibility(0);
                  BrowserFragment.this.systemVisibility = ViewUtils.switchToImmersiveMode(BrowserFragment.this.getActivity());
               }

            }
         }
      }

      public void onExitFullScreen() {
         if (this.session != null) {
            BrowserFragment.this.videoContainer.removeAllViews();
            BrowserFragment.this.videoContainer.setVisibility(8);
            BrowserFragment.this.browserContainer.setVisibility(0);
            if (BrowserFragment.this.systemVisibility != -1) {
               ViewUtils.exitImmersiveMode(BrowserFragment.this.systemVisibility, BrowserFragment.this.getActivity());
            }

            if (BrowserFragment.this.fullscreenCallback != null) {
               BrowserFragment.this.fullscreenCallback.fullScreenExited();
               BrowserFragment.this.fullscreenCallback = null;
            }

            if (this.session.getEngineSession().getTabView() instanceof WebView) {
               ((WebView)this.session.getEngineSession().getTabView()).clearFocus();
            }

         }
      }

      public void onFindResult(Session var1, mozilla.components.browser.session.Session.FindResult var2) {
         BrowserFragment.this.findInPage.onFindResultReceived(var2);
      }

      public void onGeolocationPermissionsShowPrompt(String var1, Callback var2) {
         if (this.session != null) {
            if (this.isForegroundSession(this.session) && BrowserFragment.this.isPopupWindowAllowed()) {
               BrowserFragment.this.geolocationOrigin = var1;
               BrowserFragment.this.geolocationCallback = var2;
               BrowserFragment.this.permissionHandler.tryAction((Fragment)BrowserFragment.this, "android.permission.ACCESS_FINE_LOCATION", 2, (Parcelable)null);
            }
         }
      }

      public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3) {
         HttpAuthenticationDialogBuilder.Builder var5 = (new HttpAuthenticationDialogBuilder.Builder(BrowserFragment.this.getActivity(), var2, var3)).setOkListener(new _$$Lambda$BrowserFragment$SessionObserver$3ofKWXqzOJQXPDqF9Nkl_b0cv_s(var1));
         var1.getClass();
         HttpAuthenticationDialogBuilder var4 = var5.setCancelListener(new _$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI(var1)).build();
         var4.createDialog();
         var4.show();
      }

      public void onLoadingStateChanged(Session var1, boolean var2) {
         BrowserFragment.this.isLoading = var2;
         if (var2) {
            this.historyInserter.onTabStarted(var1);
         } else {
            this.historyInserter.onTabFinished(var1);
         }

         if (this.isForegroundSession(var1)) {
            if (var2) {
               this.loadedUrl = null;
               BrowserFragment.this.updateIsLoading(true);
               BrowserFragment.this.updateURL(var1.getUrl());
               BrowserFragment.this.backgroundTransition.resetTransition();
            } else {
               this.updateUrlFromWebView(var1);
               BrowserFragment.this.updateIsLoading(false);
               FragmentListener$_CC.notifyParent(BrowserFragment.this, FragmentListener.TYPE.UPDATE_MENU, (Object)null);
               BrowserFragment.this.backgroundTransition.startTransition(300);
            }

         }
      }

      public void onLongPress(Session var1, TabView.HitTarget var2) {
         if (BrowserFragment.this.getActivity() == null) {
            Log.w("browser_screen", "No context to use, abort callback onLongPress");
         } else {
            BrowserFragment.this.webContextMenu = WebContextMenu.show(false, BrowserFragment.this.getActivity(), BrowserFragment.this.downloadCallback, var2);
         }
      }

      public void onNavigationStateChanged(Session var1, boolean var2, boolean var3) {
      }

      public void onProgress(Session var1, int var2) {
         if (this.isForegroundSession(var1)) {
            BrowserFragment.this.hideFindInPage();
            if (BrowserFragment.this.sessionManager.getFocusSession() != null) {
               String var5 = BrowserFragment.this.sessionManager.getFocusSession().getUrl();
               boolean var3 = TextUtils.equals(var5, this.loadedUrl);
               boolean var4;
               if (BrowserFragment.this.progressView.getMax() != BrowserFragment.this.progressView.getProgress() && var2 == BrowserFragment.this.progressView.getMax()) {
                  var4 = true;
               } else {
                  var4 = false;
               }

               if (var4) {
                  this.loadedUrl = var5;
               }

               if (var3) {
                  return;
               }
            }

            BrowserFragment.this.progressView.setProgress(var2);
         }
      }

      public void onReceivedIcon(Bitmap var1) {
      }

      public void onSecurityChanged(Session var1, boolean var2) {
         BrowserFragment.this.siteIdentity.setImageLevel(var2);
      }

      public boolean onShowFileChooser(TabViewEngineSession var1, ValueCallback var2, FileChooserParams var3) {
         if (!this.isForegroundSession(this.session)) {
            return false;
         } else {
            TelemetryWrapper.browseFilePermissionEvent();

            try {
               BrowserFragment var4 = BrowserFragment.this;
               FileChooseAction var6 = new FileChooseAction(BrowserFragment.this, var2, var3);
               var4.fileChooseAction = var6;
               BrowserFragment.this.permissionHandler.tryAction((Fragment)BrowserFragment.this, "android.permission.READ_EXTERNAL_STORAGE", 1, (Parcelable)null);
               return true;
            } catch (Exception var5) {
               var5.printStackTrace();
               return false;
            }
         }
      }

      public void onTitleChanged(Session var1, String var2) {
         if (var1 != null) {
            if (this.isForegroundSession(var1)) {
               if (!BrowserFragment.this.getUrl().equals(var1.getUrl())) {
                  BrowserFragment.this.updateURL(var1.getUrl());
               }

            }
         }
      }

      public void onUrlChanged(Session var1, String var2) {
         if (this.isForegroundSession(var1)) {
            BrowserFragment.this.updateURL(var2);
         }
      }

      public void updateFailingUrl(String var1, boolean var2) {
         if (this.session != null) {
            this.historyInserter.updateFailingUrl(this.session, var1, var2);
         }
      }
   }
}
