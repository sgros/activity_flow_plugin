package org.mozilla.focus.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources.Theme;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.Inject;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.fragment.BrowserFragment;
import org.mozilla.focus.fragment.FirstrunFragment;
import org.mozilla.focus.fragment.ListPanelDialog;
import org.mozilla.focus.home.HomeFragment;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.persistence.TabModelStore;
import org.mozilla.focus.provider.DownloadContract;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.screenshot.ScreenshotGridFragment;
import org.mozilla.focus.tabs.tabtray.TabTray;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.urlinput.UrlInputFragment;
import org.mozilla.focus.utils.AppConfigWrapper;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.FormatUtils;
import org.mozilla.focus.utils.IntentUtils;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.NoRemovableStorageException;
import org.mozilla.focus.utils.SafeIntent;
import org.mozilla.focus.utils.Settings;
import org.mozilla.focus.utils.ShortcutUtils;
import org.mozilla.focus.utils.StorageUtils;
import org.mozilla.focus.utils.SupportUtils;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import org.mozilla.focus.web.GeoPermissionCache;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.TabRestoreMonitor;
import org.mozilla.rocket.component.LaunchIntentDispatcher;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog;
import org.mozilla.rocket.privately.PrivateMode;
import org.mozilla.rocket.privately.PrivateModeActivity;
import org.mozilla.rocket.promotion.PromotionModel;
import org.mozilla.rocket.promotion.PromotionPresenter;
import org.mozilla.rocket.promotion.PromotionViewContract;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.urlutils.UrlUtils;

public class MainActivity extends BaseActivity implements OnSharedPreferenceChangeListener, ScreenNavigator.HostActivity, ScreenNavigator.Provider, TabModelStore.AsyncQueryListener, FragmentListener, TabRestoreMonitor, PromotionViewContract, TabsSessionProvider.SessionHost, ThemeManager.ThemeHost {
   private View blockImageButton;
   private View bookmarkIcon;
   private BookmarkViewModel bookmarkViewModel;
   private DownloadIndicatorViewModel downloadIndicatorViewModel;
   private ContentObserver downloadObserver = new ContentObserver((Handler)null) {
      public void onChange(boolean var1) {
         MainActivity.this.downloadIndicatorViewModel.updateIndicator();
      }
   };
   private boolean isTabRestoredComplete = false;
   private View loadingButton;
   private DialogFragment mDialogFragment;
   private BottomSheetDialog menu;
   private View myshotButton;
   private View myshotIndicator;
   private Dialog myshotOnBoardingDialog;
   private View nextButton;
   private View nightModeButton;
   OnLongClickListener onLongClickListener = new OnLongClickListener() {
      public boolean onLongClick(View var1) {
         MainActivity.this.menu.cancel();
         if (var1.getId() == 2131296520) {
            Settings var2 = Settings.getInstance(MainActivity.this.getApplicationContext());
            MainActivity.this.setNightModeEnabled(var2, true);
            MainActivity.this.showAdjustBrightness();
            return true;
         } else {
            throw new RuntimeException("Unknown id in menu, OnLongClickListener() is only for known ids");
         }
      }
   };
   private boolean pendingMyShotOnBoarding;
   private String pendingUrl;
   private View pinShortcut;
   private View privateModeButton;
   private View privateModeIndicator;
   private PromotionModel promotionModel;
   private View refreshIcon;
   private ScreenNavigator screenNavigator;
   private SessionManager sessionManager;
   private View shareButton;
   private View snackBarContainer;
   private View stopIcon;
   private ThemeManager themeManager;
   private View turboModeButton;
   private BroadcastReceiver uiMessageReceiver;

   private void applyNightModeBrightness(boolean var1, Settings var2, Window var3) {
      LayoutParams var4 = var3.getAttributes();
      float var5;
      if (var1) {
         var5 = var2.getNightModeBrightnessValue();
      } else {
         var5 = -1.0F;
      }

      var4.screenBrightness = var5;
      var3.setAttributes(var4);
   }

   private void asyncCheckStorage() {
      boolean var1 = false;

      label15: {
         File var2;
         try {
            var2 = StorageUtils.getTargetDirOnRemovableStorageForDownloads(this, "*/*");
         } catch (NoRemovableStorageException var3) {
            break label15;
         }

         if (var2 != null) {
            var1 = true;
         }
      }

      Settings.getInstance(this).setRemovableStorageStateOnCreate(var1);
   }

   private void asyncInitialize() {
      (new Thread(new Runnable() {
         public void run() {
            MainActivity.this.asyncCheckStorage();
         }
      })).start();
   }

   private void dismissAllMenus() {
      if (this.menu != null) {
         this.menu.dismiss();
      }

      BrowserFragment var1 = this.getVisibleBrowserFragment();
      if (var1 != null) {
         var1.dismissWebContextMenu();
         var1.dismissGeoDialog();
      }

      if (this.mDialogFragment != null) {
         this.mDialogFragment.dismissAllowingStateLoss();
      }

      if (this.myshotOnBoardingDialog != null) {
         this.myshotOnBoardingDialog.dismiss();
         this.myshotOnBoardingDialog = null;
      }

   }

   private boolean dismissContentPortal() {
      Fragment var1 = this.screenNavigator.getTopFragment();
      return var1 instanceof HomeFragment ? ((HomeFragment)var1).hideContentPortal() : false;
   }

   private void driveDefaultBrowser() {
      Settings var1 = Settings.getInstance(this);
      if (!var1.isDefaultBrowserSettingDidShow()) {
         var1.addMenuPreferenceClickCount();
         if (var1.getMenuPreferenceClickCount() == AppConfigWrapper.getDriveDefaultBrowserFromMenuSettingThreshold() && !Browsers.isDefaultBrowser(this)) {
            DialogUtils.showDefaultSettingNotification(this);
            TelemetryWrapper.showDefaultSettingNotification();
         }

      }
   }

   private String getSurveyUrl() {
      String var1;
      if (Locale.getDefault().getLanguage().equalsIgnoreCase((new Locale("id")).getLanguage())) {
         var1 = "id";
      } else {
         var1 = "en";
      }

      return this.getString(2131755417, new Object[]{var1});
   }

   private void initBroadcastReceivers() {
      this.uiMessageReceiver = new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            byte var6;
            label26: {
               String var4 = var2.getAction();
               int var3 = var4.hashCode();
               if (var3 != 480525853) {
                  if (var3 == 1438864026 && var4.equals("org.mozilla.action.NOTIFY_UI")) {
                     var6 = 0;
                     break label26;
                  }
               } else if (var4.equals("org.mozilla.action.RELOCATE_FINISH")) {
                  var6 = 1;
                  break label26;
               }

               var6 = -1;
            }

            switch(var6) {
            case 0:
               CharSequence var5 = var2.getCharSequenceExtra("org.mozilla.extra.message");
               MainActivity.this.showMessage(var5);
               break;
            case 1:
               DownloadInfoManager.getInstance().showOpenDownloadSnackBar(var2.getLongExtra("org.mozilla.extra.row_id", -1L), MainActivity.this.snackBarContainer, "MainActivity");
            }

         }
      };
   }

   private void initViews() {
      int var1 = this.getWindow().getDecorView().getSystemUiVisibility();
      this.getWindow().getDecorView().setSystemUiVisibility(var1 | 1280);
      this.snackBarContainer = this.findViewById(2131296374);
      this.setUpMenu();
   }

   private boolean isBlockingImages() {
      return Settings.getInstance(this).shouldBlockImages();
   }

   private boolean isNightModeEnabled(Settings var1) {
      return var1.isNightModeEnable();
   }

   private boolean isTurboEnabled() {
      return Settings.getInstance(this).shouldUseTurboMode();
   }

   // $FF: synthetic method
   public static void lambda$null$0(MainActivity var0, DialogInterface var1) {
      var0.dismissAllMenus();
   }

   // $FF: synthetic method
   public static void lambda$null$1(MainActivity var0, View var1) {
      String var2 = SupportUtils.getSumoURLForTopic(var0, "screenshot-telemetry");
      var0.screenNavigator.showBrowserScreen(var2, true, false);
      var0.dismissAllMenus();
   }

   // $FF: synthetic method
   static void lambda$null$3(DialogInterface var0) {
   }

   // $FF: synthetic method
   public static void lambda$onBookMarkClicked$6(MainActivity var0, String var1, View var2) {
      var0.startActivity((new Intent(var0, EditBookmarkActivity.class)).putExtra("ITEM_UUID_KEY", var1));
   }

   // $FF: synthetic method
   public static void lambda$updateMenu$2(MainActivity var0) {
      var0.myshotOnBoardingDialog = DialogUtils.showMyShotOnBoarding(var0, var0.myshotButton, new _$$Lambda$MainActivity$pTM933lwu8pbNr7Y8C84tdVKTTs(var0), new _$$Lambda$MainActivity$1S8GdtQw7QrO0XD_WCw0iUqwwXI(var0));
   }

   // $FF: synthetic method
   public static void lambda$updateMenu$4(MainActivity var0) {
      DialogUtils.showSpotlight(var0, var0.nightModeButton, _$$Lambda$MainActivity$NCjHwftAzp9_j6nZuorYgzoqY2E.INSTANCE, 2131755277);
   }

   // $FF: synthetic method
   public static void lambda$updateMenu$5(MainActivity var0, List var1) {
      boolean var2;
      if (var1 != null && var1.size() > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      var0.bookmarkIcon.setActivated(var2);
   }

   private void onAddToHomeClicked() {
      Session var1 = this.getSessionManager().getFocusSession();
      if (var1 != null) {
         String var2 = var1.getUrl();
         if (SupportUtils.isUrl(var2)) {
            Bitmap var3 = var1.getFavicon();
            Intent var4 = new Intent("android.intent.action.VIEW");
            var4.setClassName(this, "org.mozilla.rocket.activity.MainActivity");
            var4.setData(Uri.parse(var2));
            var4.putExtra(LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), true);
            ShortcutUtils.requestPinShortcut(this, var4, var1.getTitle(), var2, var3);
         }
      }
   }

   private void onBookMarkClicked() {
      Session var1 = this.getSessionManager().getFocusSession();
      if (var1 != null) {
         boolean var2 = this.bookmarkIcon.isActivated();
         TelemetryWrapper.clickToolbarBookmark(var2 ^ true);
         if (var2) {
            this.bookmarkViewModel.deleteBookmarksByUrl(var1.getUrl());
            Toast.makeText(this, 2131755068, 1).show();
            this.bookmarkIcon.setActivated(false);
         } else {
            if (TextUtils.isEmpty(var1.getUrl())) {
               return;
            }

            String var3 = var1.getTitle();
            String var4 = var3;
            if (TextUtils.isEmpty(var3)) {
               var4 = UrlUtils.stripCommonSubdomains(UrlUtils.stripHttp(var1.getUrl()));
            }

            var3 = this.bookmarkViewModel.addBookmark(var4, var1.getUrl());
            Snackbar var5 = Snackbar.make(this.snackBarContainer, 2131755069, 0);
            var5.setAction(2131755070, new _$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8(this, var3));
            var5.show();
            this.bookmarkIcon.setActivated(true);
         }

      }
   }

   private void onBookmarksClicked() {
      this.showListPanel(4);
   }

   private void onDeleteClicked() {
      long var1 = FileUtils.clearCache(this);
      int var3;
      if (var1 < 0L) {
         var3 = 2131755255;
      } else {
         var3 = 2131755257;
      }

      Toast.makeText(this, this.getString(var3, new Object[]{FormatUtils.getReadableStringFromFileSize(var1)}), 0).show();
   }

   private void onDownloadClicked() {
      this.showListPanel(1);
   }

   private void onExitClicked() {
      GeoPermissionCache.clear();
      if (PrivateMode.hasPrivateSession(this)) {
         this.startActivity(PrivateSessionNotificationService.buildIntent(this.getApplicationContext(), true));
      }

      this.finish();
   }

   private void onFindInPageClicked() {
      BrowserFragment var1 = this.getVisibleBrowserFragment();
      if (var1 != null) {
         var1.showFindInPage();
      }

   }

   private void onHistoryClicked() {
      this.showListPanel(2);
   }

   private void onNextClicked(BrowserFragment var1) {
      var1.goForward();
   }

   private void onPreferenceClicked() {
      this.openPreferences();
   }

   private void onRefreshClicked(BrowserFragment var1) {
      var1.reload();
   }

   private void onScreenshotsClicked() {
      Settings.getInstance(this).setHasUnreadMyShot(false);
      this.showListPanel(3);
   }

   private void onShraeClicked(BrowserFragment var1) {
      Intent var2 = new Intent("android.intent.action.SEND");
      var2.setType("text/plain");
      var2.putExtra("android.intent.extra.TEXT", var1.getUrl());
      this.startActivity(Intent.createChooser(var2, this.getString(2131755412)));
   }

   private void onStopClicked(BrowserFragment var1) {
      var1.stop();
   }

   private void openUrl(boolean var1, Object var2) {
      String var3;
      if (var2 != null) {
         var3 = var2.toString();
      } else {
         var3 = null;
      }

      ScreenNavigator.get(this).showBrowserScreen(var3, var1, false);
   }

   private void restoreTabsFromPersistence() {
      this.isTabRestoredComplete = false;
      TabModelStore.getInstance(this).getSavedTabs(this, this);
   }

   private void saveTabsToPersistence() {
      if (this.isTabRestoredComplete) {
         List var1 = this.getSessionManager().getTabs();
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            Session var3 = (Session)var2.next();
            if (var3.getEngineSession() != null) {
               var3.getEngineSession().saveState();
            }
         }

         String var4;
         if (this.getSessionManager().getFocusSession() != null) {
            var4 = this.getSessionManager().getFocusSession().getId();
         } else {
            var4 = null;
         }

         TabModelStore.getInstance(this).saveTabs(this, var1, var4, (TabModelStore.AsyncSaveListener)null);
      }
   }

   private void setEnable(View var1, boolean var2) {
      var1.setEnabled(var2);
      if (var1 instanceof ViewGroup) {
         ViewGroup var4 = (ViewGroup)var1;

         for(int var3 = 0; var3 < var4.getChildCount(); ++var3) {
            this.setEnable(var4.getChildAt(var3), var2);
         }
      }

   }

   private void setLoadingButton(BrowserFragment var1) {
      byte var2 = 8;
      if (var1 == null) {
         this.setEnable(this.loadingButton, false);
         this.refreshIcon.setVisibility(0);
         this.stopIcon.setVisibility(8);
         this.loadingButton.setTag(false);
      } else {
         this.setEnable(this.loadingButton, true);
         boolean var3 = var1.isLoading();
         View var5 = this.refreshIcon;
         byte var4;
         if (var3) {
            var4 = 8;
         } else {
            var4 = 0;
         }

         var5.setVisibility(var4);
         var5 = this.stopIcon;
         var4 = var2;
         if (var3) {
            var4 = 0;
         }

         var5.setVisibility(var4);
         this.loadingButton.setTag(var3);
      }

   }

   private void setNightModeEnabled(Settings var1, boolean var2) {
      var1.setNightMode(var2);
      this.applyNightModeBrightness(var2, var1, this.getWindow());
      Fragment var3 = this.screenNavigator.getTopFragment();
      if (var3 instanceof BrowserFragment) {
         ((BrowserFragment)var3).setNightModeEnabled(var2);
      } else if (var3 instanceof HomeFragment) {
         ((HomeFragment)var3).setNightModeEnabled(var2);
      }

   }

   private void setShowNightModeSpotlight(Settings var1, boolean var2) {
      var1.setNightModeSpotlight(var2);
   }

   private void setUpMenu() {
      View var1 = this.getLayoutInflater().inflate(2131492902, (ViewGroup)null);
      this.menu = new BottomSheetDialog(this, 2131820747);
      this.menu.setContentView(var1);
      this.menu.setCanceledOnTouchOutside(true);
      this.myshotIndicator = this.menu.findViewById(2131296517);
      this.nextButton = this.menu.findViewById(2131296282);
      this.loadingButton = this.menu.findViewById(2131296276);
      this.privateModeButton = this.menu.findViewById(2131296355);
      this.privateModeIndicator = this.menu.findViewById(2131296522);
      this.shareButton = this.menu.findViewById(2131296285);
      this.bookmarkIcon = this.menu.findViewById(2131296271);
      this.refreshIcon = this.menu.findViewById(2131296284);
      this.stopIcon = this.menu.findViewById(2131296286);
      this.pinShortcut = this.menu.findViewById(2131296283);
      this.myshotButton = this.menu.findViewById(2131296523);
      if (!ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
         this.pinShortcut.setVisibility(8);
      }

      this.turboModeButton = this.menu.findViewById(2131296524);
      this.turboModeButton.setSelected(this.isTurboEnabled());
      this.blockImageButton = this.menu.findViewById(2131296505);
      this.blockImageButton.setSelected(this.isBlockingImages());
      this.nightModeButton = this.menu.findViewById(2131296520);
      this.nightModeButton.setOnLongClickListener(this.onLongClickListener);
      this.nightModeButton.setSelected(this.isNightModeEnabled(Settings.getInstance(this.getApplicationContext())));
   }

   private boolean shouldShowNightModeSpotlight(Settings var1) {
      return var1.showNightModeSpotlight();
   }

   private void showAdjustBrightness() {
      this.startActivity(AdjustBrightnessDialog.Intents.INSTANCE.getStartIntentFromMenu(this));
   }

   private void showAdjustBrightnessIfNeeded(Settings var1) {
      if (var1.getNightModeBrightnessValue() == -1.0F) {
         var1.setNightModeBrightnessValue(0.125F);
         this.showAdjustBrightness();
         this.setShowNightModeSpotlight(var1, true);
      }

   }

   private void showListPanel(int var1) {
      ListPanelDialog var2 = ListPanelDialog.newInstance(var1);
      var2.setCancelable(true);
      var2.show(this.getSupportFragmentManager(), "");
      this.mDialogFragment = var2;
   }

   private void showMenu() {
      this.updateMenu();
      this.menu.show();
   }

   private void showMessage(CharSequence var1) {
      if (!TextUtils.isEmpty(var1)) {
         Toast.makeText(this, var1, 0).show();
      }
   }

   private void updateMenu() {
      this.turboModeButton.setSelected(this.isTurboEnabled());
      this.blockImageButton.setSelected(this.isBlockingImages());
      boolean var1 = AppConfigWrapper.getMyshotUnreadEnabled(this);
      boolean var2 = true;
      boolean var3;
      if (var1 && Settings.getInstance(this).hasUnreadMyShot()) {
         var3 = true;
      } else {
         var3 = false;
      }

      var1 = PrivateMode.hasPrivateSession(this);
      Settings var4 = Settings.getInstance(this.getApplicationContext());
      View var5 = this.myshotIndicator;
      byte var6 = 8;
      byte var7;
      if (var3) {
         var7 = 0;
      } else {
         var7 = 8;
      }

      var5.setVisibility(var7);
      var5 = this.privateModeIndicator;
      var7 = var6;
      if (var1) {
         var7 = 0;
      }

      var5.setVisibility(var7);
      if (this.pendingMyShotOnBoarding) {
         this.pendingMyShotOnBoarding = false;
         this.setShowNightModeSpotlight(var4, false);
         this.myshotButton.post(new _$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy_P_T4VgUvo(this));
      }

      this.nightModeButton.setSelected(this.isNightModeEnabled(var4));
      if (this.shouldShowNightModeSpotlight(var4)) {
         this.setShowNightModeSpotlight(var4, false);
         this.nightModeButton.post(new _$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU(this));
      }

      BrowserFragment var8 = this.getVisibleBrowserFragment();
      if (var8 != null && var8.canGoForward()) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.setEnable(this.nextButton, var1);
      this.setLoadingButton(var8);
      var5 = this.bookmarkIcon;
      if (var8 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.setEnable(var5, var1);
      var5 = this.shareButton;
      if (var8 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.setEnable(var5, var1);
      var5 = this.pinShortcut;
      if (var8 != null) {
         var1 = var2;
      } else {
         var1 = false;
      }

      this.setEnable(var5, var1);
      Session var9 = this.getSessionManager().getFocusSession();
      if (var9 != null) {
         this.bookmarkViewModel.getBookmarksByUrl(var9.getUrl()).observe(this, new _$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o(this));
      }
   }

   public void applyLocale() {
      this.setUpMenu();
   }

   public FirstrunFragment createFirstRunScreen() {
      return FirstrunFragment.create();
   }

   public HomeFragment createHomeScreen() {
      return HomeFragment.create();
   }

   public UrlInputFragment createUrlInputScreen(String var1, String var2) {
      return UrlInputFragment.create(var1, var2, true);
   }

   public void firstrunFinished() {
      if (this.pendingUrl != null) {
         this.screenNavigator.showBrowserScreen(this.pendingUrl, true, true);
         this.pendingUrl = null;
      } else {
         this.screenNavigator.popToHomeScreen(false);
      }

   }

   public BrowserFragment getBrowserFragment() {
      return (BrowserFragment)this.getSupportFragmentManager().findFragmentById(2131296331);
   }

   public BrowserFragment getBrowserScreen() {
      return (BrowserFragment)this.getSupportFragmentManager().findFragmentById(2131296331);
   }

   public ScreenNavigator getScreenNavigator() {
      return this.screenNavigator;
   }

   public SessionManager getSessionManager() {
      if (this.sessionManager == null) {
         this.sessionManager = new SessionManager(new MainActivity.MainTabViewProvider(this));
      }

      return this.sessionManager;
   }

   public Theme getTheme() {
      Theme var1 = super.getTheme();
      if (this.themeManager != null) {
         this.themeManager.applyCurrentTheme(var1);
      }

      return var1;
   }

   public ThemeManager getThemeManager() {
      return this.themeManager;
   }

   public BrowserFragment getVisibleBrowserFragment() {
      BrowserFragment var1;
      if (this.screenNavigator.isBrowserInForeground()) {
         var1 = this.getBrowserFragment();
      } else {
         var1 = null;
      }

      return var1;
   }

   public boolean isTabRestoredComplete() {
      return this.isTabRestoredComplete;
   }

   protected void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      if (var1 == 1000) {
         if (var2 == 100) {
            Toast.makeText(this, 2131755258, 0).show();
            if (this.mDialogFragment != null) {
               Fragment var4 = this.mDialogFragment.getChildFragmentManager().findFragmentById(2131296499);
               if (var4 instanceof ScreenshotGridFragment && var3 != null) {
                  long var5 = var3.getLongExtra("extra_screenshot_item_id", -1L);
                  ((ScreenshotGridFragment)var4).notifyItemDelete(var5);
               }
            }
         } else if (var2 == 101 && var3 != null) {
            String var7 = var3.getStringExtra("extra_url");
            if (this.mDialogFragment != null) {
               this.mDialogFragment.dismissAllowingStateLoss();
            }

            this.screenNavigator.showBrowserScreen(var7, true, false);
         }
      }

   }

   public void onBackPressed() {
      if (!this.getSupportFragmentManager().isStateSaved()) {
         ScreenNavigator.BrowserScreen var1 = this.screenNavigator.getVisibleBrowserScreen();
         if (var1 == null || !var1.onBackPressed()) {
            if (!this.dismissContentPortal()) {
               if (!this.screenNavigator.canGoBack()) {
                  this.finish();
               } else {
                  super.onBackPressed();
               }
            }
         }
      }
   }

   protected void onCreate(Bundle var1) {
      this.themeManager = new ThemeManager(this);
      super.onCreate(var1);
      this.asyncInitialize();
      this.setContentView(2131492894);
      this.initViews();
      this.initBroadcastReceivers();
      this.screenNavigator = new ScreenNavigator(this);
      SafeIntent var2 = new SafeIntent(this.getIntent());
      if (var1 == null) {
         if ("android.intent.action.VIEW".equals(var2.getAction())) {
            String var4 = var2.getDataString();
            if (Settings.getInstance(this).shouldShowFirstrun()) {
               this.pendingUrl = var4;
               this.screenNavigator.addFirstRunScreen();
            } else {
               boolean var3 = var2.getBooleanExtra("open_new_tab", false);
               this.screenNavigator.showBrowserScreen(var4, var3, true);
            }
         } else if (Settings.getInstance(this).shouldShowFirstrun()) {
            this.screenNavigator.addFirstRunScreen();
         } else {
            this.screenNavigator.popToHomeScreen(false);
         }
      }

      if (NewFeatureNotice.getInstance(this).shouldShowLiteUpdate()) {
         this.themeManager.resetDefaultTheme();
      }

      this.restoreTabsFromPersistence();
      WebViewProvider.preload(this);
      this.promotionModel = new PromotionModel(this, var2);
      if (Inject.getActivityNewlyCreatedFlag()) {
         Inject.setActivityNewlyCreatedFlag();
         PromotionPresenter.runPromotion(this, this.promotionModel);
      }

      this.bookmarkViewModel = (BookmarkViewModel)ViewModelProviders.of((FragmentActivity)this, new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance(this)))).get(BookmarkViewModel.class);
      PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
      this.downloadIndicatorViewModel = Inject.obtainDownloadIndicatorViewModel(this);
   }

   public void onDestroy() {
      PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
      this.sessionManager.destroy();
      super.onDestroy();
   }

   public void onMenuBrowsingItemClicked(View var1) {
      BrowserFragment var2 = this.getVisibleBrowserFragment();
      if (var2 != null) {
         switch(var1.getId()) {
         case 2131296271:
            this.onBookMarkClicked();
            break;
         case 2131296276:
            if ((Boolean)var1.getTag()) {
               this.onStopClicked(var2);
            } else {
               this.onRefreshClicked(var2);
            }

            TelemetryWrapper.clickToolbarReload();
            break;
         case 2131296282:
            this.onNextClicked(var2);
            TelemetryWrapper.clickToolbarForward();
            break;
         case 2131296283:
            this.onAddToHomeClicked();
            TelemetryWrapper.clickAddToHome();
            break;
         case 2131296285:
            this.onShraeClicked(var2);
            TelemetryWrapper.clickToolbarShare();
            break;
         default:
            throw new RuntimeException("Unknown id in menu, onMenuBrowsingItemClicked() is only for known ids");
         }

      }
   }

   public void onMenuItemClicked(View var1) {
      if (var1.isEnabled()) {
         this.menu.cancel();
         boolean var2;
         int var3;
         switch(var1.getId()) {
         case 2131296271:
         case 2131296276:
         case 2131296282:
         case 2131296283:
         case 2131296285:
            this.onMenuBrowsingItemClicked(var1);
            break;
         case 2131296355:
            this.startActivity(new Intent(this, PrivateModeActivity.class));
            this.overridePendingTransition(2130771989, 2130771990);
            TelemetryWrapper.togglePrivateMode(true);
            break;
         case 2131296505:
            var2 = this.isBlockingImages() ^ true;
            Settings.getInstance(this).setBlockImages(var2);
            var1.setSelected(var2);
            if (var2) {
               var3 = 2131755261;
            } else {
               var3 = 2131755259;
            }

            Toast.makeText(this, var3, 0).show();
            TelemetryWrapper.menuBlockImageChangeTo(var2);
            break;
         case 2131296506:
            this.onBookmarksClicked();
            TelemetryWrapper.clickMenuBookmark();
            break;
         case 2131296507:
            this.onDeleteClicked();
            TelemetryWrapper.clickMenuClearCache();
            break;
         case 2131296508:
            this.onDownloadClicked();
            TelemetryWrapper.clickMenuDownload();
            break;
         case 2131296509:
            this.onExitClicked();
            TelemetryWrapper.clickMenuExit();
            break;
         case 2131296510:
            this.onFindInPageClicked();
            break;
         case 2131296511:
            this.onHistoryClicked();
            TelemetryWrapper.clickMenuHistory();
            break;
         case 2131296520:
            Settings var4 = Settings.getInstance(this);
            var2 = this.isNightModeEnabled(var4) ^ true;
            var1.setSelected(var2);
            this.setNightModeEnabled(var4, var2);
            this.showAdjustBrightnessIfNeeded(var4);
            TelemetryWrapper.menuNightModeChangeTo(var2);
            break;
         case 2131296521:
            this.driveDefaultBrowser();
            this.onPreferenceClicked();
            TelemetryWrapper.clickMenuSettings();
            break;
         case 2131296523:
            this.onScreenshotsClicked();
            TelemetryWrapper.clickMenuCapture();
            break;
         case 2131296524:
            var2 = this.isTurboEnabled() ^ true;
            Settings.getInstance(this).setTurboMode(var2);
            var1.setSelected(var2);
            if (var2) {
               var3 = 2131755262;
            } else {
               var3 = 2131755260;
            }

            Toast.makeText(this, var3, 0).show();
            TelemetryWrapper.menuTurboChangeTo(var2);
            break;
         default:
            throw new RuntimeException("Unknown id in menu, onMenuItemClicked() is only for known ids");
         }

      }
   }

   protected void onNewIntent(Intent var1) {
      SafeIntent var2 = new SafeIntent(var1);
      if (this.promotionModel != null) {
         this.promotionModel.parseIntent(var2);
         if (PromotionPresenter.runPromotionFromIntent(this, this.promotionModel)) {
            return;
         }
      }

      if ("android.intent.action.VIEW".equals(var2.getAction())) {
         this.pendingUrl = var2.getDataString();
         this.dismissAllMenus();
         TabTray.dismiss(this.getSupportFragmentManager());
      }

      this.setIntent(var1);
   }

   public void onNotified(Fragment var1, FragmentListener.TYPE var2, Object var3) {
      switch(var2) {
      case OPEN_PREFERENCE:
         this.openPreferences();
         break;
      case SHOW_MENU:
         this.showMenu();
         break;
      case UPDATE_MENU:
         this.updateMenu();
         break;
      case OPEN_URL_IN_CURRENT_TAB:
         this.openUrl(false, var3);
         break;
      case OPEN_URL_IN_NEW_TAB:
         this.openUrl(true, var3);
         break;
      case SHOW_URL_INPUT:
         if (this.getSupportFragmentManager().isStateSaved()) {
            return;
         }

         String var4;
         if (var3 != null) {
            var4 = var3.toString();
         } else {
            var4 = null;
         }

         this.screenNavigator.addUrlScreen(var4);
         break;
      case DISMISS_URL_INPUT:
         this.screenNavigator.popUrlScreen();
         break;
      case SHOW_TAB_TRAY:
         TabTray.show(this.getSupportFragmentManager());
         break;
      case REFRESH_TOP_SITE:
         var1 = this.screenNavigator.getTopFragment();
         if (var1 instanceof HomeFragment) {
            ((HomeFragment)var1).updateTopSitesData();
         }
         break;
      case SHOW_MY_SHOT_ON_BOARDING:
         this.showMyShotOnBoarding();
         break;
      case SHOW_DOWNLOAD_PANEL:
         this.onDownloadClicked();
      }

   }

   protected void onPause() {
      super.onPause();
      LocalBroadcastManager.getInstance(this).unregisterReceiver(this.uiMessageReceiver);
      this.getContentResolver().unregisterContentObserver(this.downloadObserver);
      TelemetryWrapper.stopSession();
      this.saveTabsToPersistence();
   }

   public void onPointerCaptureChanged(boolean var1) {
      Settings.getInstance(this).getEventHistory().add("post_survey_notification");
   }

   public void onQueryComplete(List var1, String var2) {
      this.isTabRestoredComplete = true;
      this.getSessionManager().restore(var1, var2);
      Session var3 = this.getSessionManager().getFocusSession();
      if (!Settings.getInstance(this).shouldShowFirstrun() && var3 != null && !this.getSupportFragmentManager().isStateSaved()) {
         this.screenNavigator.restoreBrowserScreen(var3.getId());
      }

   }

   protected void onResume() {
      super.onResume();
      TelemetryWrapper.startSession();
      IntentFilter var1 = new IntentFilter("org.mozilla.action.NOTIFY_UI");
      var1.addCategory("org.mozilla.category.FILE_OPERATION");
      var1.addAction("org.mozilla.action.RELOCATE_FINISH");
      LocalBroadcastManager.getInstance(this).registerReceiver(this.uiMessageReceiver, var1);
      this.getContentResolver().registerContentObserver(DownloadContract.Download.CONTENT_URI, true, this.downloadObserver);
      this.downloadIndicatorViewModel.updateIndicator();
   }

   protected void onResumeFragments() {
      super.onResumeFragments();
      if (this.pendingUrl != null) {
         boolean var1 = (new SafeIntent(this.getIntent())).getBooleanExtra("open_new_tab", true);
         this.screenNavigator.showBrowserScreen(this.pendingUrl, var1, true);
         this.pendingUrl = null;
      }

   }

   public void onSharedPreferenceChanged(SharedPreferences var1, String var2) {
      boolean var3;
      BrowserFragment var4;
      if (this.getResources().getString(2131755332).equals(var2)) {
         var3 = this.isTurboEnabled();
         var4 = this.getBrowserFragment();
         if (var4 != null) {
            var4.setContentBlockingEnabled(var3);
         }

         this.setMenuButtonSelected(2131296524, var3);
      } else if (this.getResources().getString(2131755316).equals(var2)) {
         var3 = this.isBlockingImages();
         var4 = this.getBrowserFragment();
         if (var4 != null) {
            var4.setImageBlockingEnabled(var3);
         }

         this.setMenuButtonSelected(2131296505, var3);
      }

   }

   protected void onStart() {
      super.onStart();
   }

   protected void onStop() {
      super.onStop();
      TelemetryWrapper.stopMainActivity();
   }

   public void postSurveyNotification() {
      PendingIntent var1 = PendingIntent.getActivity(this, 0, IntentUtils.createInternalOpenUrlIntent(this, this.getSurveyUrl(), true), 1073741824);
      NotificationUtil.sendNotification(this, 1000, NotificationUtil.importantBuilder(this).setContentTitle(this.getString(2131755416, new Object[]{"\ud83d\ude4c"})).setContentText(this.getString(2131755415)).setStyle((new NotificationCompat.BigTextStyle()).bigText(this.getString(2131755415))).setContentIntent(var1));
   }

   void setMenuButtonSelected(int var1, boolean var2) {
      if (this.menu != null) {
         View var3 = this.menu.findViewById(var1);
         if (var3 != null) {
            var3.setSelected(var2);
         }
      }
   }

   public void showMyShotOnBoarding() {
      this.pendingMyShotOnBoarding = true;
      this.showMenu();
   }

   public void showPrivacyPolicyUpdateNotification() {
      DialogUtils.showPrivacyPolicyUpdateNotification(this);
   }

   public void showRateAppDialog() {
      DialogUtils.showRateAppDialog(this);
      TelemetryWrapper.showRateApp(false);
   }

   public void showRateAppDialogFromIntent() {
      DialogUtils.showRateAppDialog(this);
      TelemetryWrapper.showRateApp(false);
      NotificationManagerCompat.from(this).cancel(1001);
      if (this.getIntent().getExtras() != null) {
         this.getIntent().getExtras().putBoolean("show_rate_dialog", false);
      }

   }

   public void showRateAppNotification() {
      DialogUtils.showRateAppNotification(this);
      TelemetryWrapper.showRateApp(true);
   }

   public void showShareAppDialog() {
      DialogUtils.showShareAppDialog(this);
      TelemetryWrapper.showPromoteShareDialog();
   }

   private static class MainTabViewProvider extends TabViewProvider {
      private Activity activity;

      MainTabViewProvider(Activity var1) {
         this.activity = var1;
      }

      public TabView create() {
         return (TabView)WebViewProvider.create(this.activity, (AttributeSet)null);
      }
   }
}
