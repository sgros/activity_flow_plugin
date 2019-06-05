package org.mozilla.focus.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
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
import android.preference.PreferenceManager;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.p001v4.app.DialogFragment;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.NotificationCompat.BigTextStyle;
import android.support.p001v4.app.NotificationManagerCompat;
import android.support.p001v4.content.LocalBroadcastManager;
import android.support.p001v4.content.p002pm.ShortcutManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import com.adjust.sdk.Constants;
import java.util.List;
import java.util.Locale;
import org.mozilla.fileutils.FileUtils;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.Inject;
import org.mozilla.focus.fragment.BrowserFragment;
import org.mozilla.focus.fragment.FirstrunFragment;
import org.mozilla.focus.fragment.ListPanelDialog;
import org.mozilla.focus.home.HomeFragment;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.navigation.ScreenNavigator.BrowserScreen;
import org.mozilla.focus.navigation.ScreenNavigator.HostActivity;
import org.mozilla.focus.navigation.ScreenNavigator.Provider;
import org.mozilla.focus.notification.NotificationUtil;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.focus.persistence.TabModelStore;
import org.mozilla.focus.persistence.TabModelStore.AsyncQueryListener;
import org.mozilla.focus.provider.DownloadContract.Download;
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
import org.mozilla.focus.viewmodel.BookmarkViewModel.Factory;
import org.mozilla.focus.web.GeoPermissionCache;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.focus.widget.TabRestoreMonitor;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.component.LaunchIntentDispatcher.LaunchMethod;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog.Intents;
import org.mozilla.rocket.privately.PrivateMode;
import org.mozilla.rocket.privately.PrivateModeActivity;
import org.mozilla.rocket.promotion.PromotionModel;
import org.mozilla.rocket.promotion.PromotionPresenter;
import org.mozilla.rocket.promotion.PromotionViewContract;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.SessionManager.SessionWithState;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.rocket.tabs.TabsSessionProvider.SessionHost;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.rocket.theme.ThemeManager.ThemeHost;
import org.mozilla.urlutils.UrlUtils;

public class MainActivity extends BaseActivity implements OnSharedPreferenceChangeListener, HostActivity, Provider, AsyncQueryListener, FragmentListener, TabRestoreMonitor, PromotionViewContract, SessionHost, ThemeHost {
    private View blockImageButton;
    private View bookmarkIcon;
    private BookmarkViewModel bookmarkViewModel;
    private DownloadIndicatorViewModel downloadIndicatorViewModel;
    private ContentObserver downloadObserver = new ContentObserver(null) {
        public void onChange(boolean z) {
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
    OnLongClickListener onLongClickListener = new C04362();
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

    /* renamed from: org.mozilla.focus.activity.MainActivity$1 */
    class C04351 extends BroadcastReceiver {
        C04351() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:16:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
        /* JADX WARNING: Removed duplicated region for block: B:16:? A:{SYNTHETIC, RETURN} */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0048  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x002c  */
        public void onReceive(android.content.Context r4, android.content.Intent r5) {
            /*
            r3 = this;
            r4 = r5.getAction();
            r0 = r4.hashCode();
            r1 = 480525853; // 0x1ca43e1d float:1.0868666E-21 double:2.37411316E-315;
            if (r0 == r1) goto L_0x001d;
        L_0x000d:
            r1 = 1438864026; // 0x55c3529a float:2.68449423E13 double:7.108932843E-315;
            if (r0 == r1) goto L_0x0013;
        L_0x0012:
            goto L_0x0027;
        L_0x0013:
            r0 = "org.mozilla.action.NOTIFY_UI";
            r4 = r4.equals(r0);
            if (r4 == 0) goto L_0x0027;
        L_0x001b:
            r4 = 0;
            goto L_0x0028;
        L_0x001d:
            r0 = "org.mozilla.action.RELOCATE_FINISH";
            r4 = r4.equals(r0);
            if (r4 == 0) goto L_0x0027;
        L_0x0025:
            r4 = 1;
            goto L_0x0028;
        L_0x0027:
            r4 = -1;
        L_0x0028:
            switch(r4) {
                case 0: goto L_0x0048;
                case 1: goto L_0x002c;
                default: goto L_0x002b;
            };
        L_0x002b:
            goto L_0x0053;
        L_0x002c:
            r4 = org.mozilla.focus.download.DownloadInfoManager.getInstance();
            r0 = "org.mozilla.extra.row_id";
            r1 = -1;
            r0 = r5.getLongExtra(r0, r1);
            r5 = java.lang.Long.valueOf(r0);
            r0 = org.mozilla.focus.activity.MainActivity.this;
            r0 = r0.snackBarContainer;
            r1 = "MainActivity";
            r4.showOpenDownloadSnackBar(r5, r0, r1);
            goto L_0x0053;
        L_0x0048:
            r4 = "org.mozilla.extra.message";
            r4 = r5.getCharSequenceExtra(r4);
            r5 = org.mozilla.focus.activity.MainActivity.this;
            r5.showMessage(r4);
        L_0x0053:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.mozilla.focus.activity.MainActivity$C04351.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    /* renamed from: org.mozilla.focus.activity.MainActivity$2 */
    class C04362 implements OnLongClickListener {
        C04362() {
        }

        public boolean onLongClick(View view) {
            MainActivity.this.menu.cancel();
            if (view.getId() == C0427R.C0426id.menu_night_mode) {
                MainActivity.this.setNightModeEnabled(Settings.getInstance(MainActivity.this.getApplicationContext()), true);
                MainActivity.this.showAdjustBrightness();
                return true;
            }
            throw new RuntimeException("Unknown id in menu, OnLongClickListener() is only for known ids");
        }
    }

    /* renamed from: org.mozilla.focus.activity.MainActivity$3 */
    class C04373 implements Runnable {
        C04373() {
        }

        public void run() {
            MainActivity.this.asyncCheckStorage();
        }
    }

    private static class MainTabViewProvider extends TabViewProvider {
        private Activity activity;

        MainTabViewProvider(Activity activity) {
            this.activity = activity;
        }

        public TabView create() {
            return (TabView) WebViewProvider.create(this.activity, null);
        }
    }

    static /* synthetic */ void lambda$null$3(DialogInterface dialogInterface) {
    }

    public ThemeManager getThemeManager() {
        return this.themeManager;
    }

    public Theme getTheme() {
        Theme theme = super.getTheme();
        if (this.themeManager != null) {
            this.themeManager.applyCurrentTheme(theme);
        }
        return theme;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        this.themeManager = new ThemeManager(this);
        super.onCreate(bundle);
        asyncInitialize();
        setContentView((int) C0769R.layout.activity_main);
        initViews();
        initBroadcastReceivers();
        this.screenNavigator = new ScreenNavigator(this);
        SafeIntent safeIntent = new SafeIntent(getIntent());
        if (bundle == null) {
            if ("android.intent.action.VIEW".equals(safeIntent.getAction())) {
                String dataString = safeIntent.getDataString();
                if (Settings.getInstance(this).shouldShowFirstrun()) {
                    this.pendingUrl = dataString;
                    this.screenNavigator.addFirstRunScreen();
                } else {
                    this.screenNavigator.showBrowserScreen(dataString, safeIntent.getBooleanExtra("open_new_tab", false), true);
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
        restoreTabsFromPersistence();
        WebViewProvider.preload(this);
        this.promotionModel = new PromotionModel(this, safeIntent);
        if (Inject.getActivityNewlyCreatedFlag()) {
            Inject.setActivityNewlyCreatedFlag();
            PromotionPresenter.runPromotion(this, this.promotionModel);
        }
        this.bookmarkViewModel = (BookmarkViewModel) ViewModelProviders.m3of((FragmentActivity) this, new Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance(this)))).get(BookmarkViewModel.class);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        this.downloadIndicatorViewModel = Inject.obtainDownloadIndicatorViewModel(this);
    }

    private void initBroadcastReceivers() {
        this.uiMessageReceiver = new C04351();
    }

    public void applyLocale() {
        setUpMenu();
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        super.onStart();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        TelemetryWrapper.startSession();
        IntentFilter intentFilter = new IntentFilter("org.mozilla.action.NOTIFY_UI");
        intentFilter.addCategory("org.mozilla.category.FILE_OPERATION");
        intentFilter.addAction("org.mozilla.action.RELOCATE_FINISH");
        LocalBroadcastManager.getInstance(this).registerReceiver(this.uiMessageReceiver, intentFilter);
        getContentResolver().registerContentObserver(Download.CONTENT_URI, true, this.downloadObserver);
        this.downloadIndicatorViewModel.updateIndicator();
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.uiMessageReceiver);
        getContentResolver().unregisterContentObserver(this.downloadObserver);
        TelemetryWrapper.stopSession();
        saveTabsToPersistence();
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        super.onStop();
        TelemetryWrapper.stopMainActivity();
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
        SafeIntent safeIntent = new SafeIntent(intent);
        if (this.promotionModel != null) {
            this.promotionModel.parseIntent(safeIntent);
            if (PromotionPresenter.runPromotionFromIntent(this, this.promotionModel)) {
                return;
            }
        }
        if ("android.intent.action.VIEW".equals(safeIntent.getAction())) {
            this.pendingUrl = safeIntent.getDataString();
            dismissAllMenus();
            TabTray.dismiss(getSupportFragmentManager());
        }
        setIntent(intent);
    }

    /* Access modifiers changed, original: protected */
    public void onResumeFragments() {
        super.onResumeFragments();
        if (this.pendingUrl != null) {
            this.screenNavigator.showBrowserScreen(this.pendingUrl, new SafeIntent(getIntent()).getBooleanExtra("open_new_tab", true), true);
            this.pendingUrl = null;
        }
    }

    private void initViews() {
        getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() | 1280);
        this.snackBarContainer = findViewById(2131296374);
        setUpMenu();
    }

    public void postSurveyNotification() {
        NotificationUtil.sendNotification(this, Constants.ONE_SECOND, NotificationUtil.importantBuilder(this).setContentTitle(getString(C0769R.string.survey_notification_title, new Object[]{"🙌"})).setContentText(getString(C0769R.string.survey_notification_description)).setStyle(new BigTextStyle().bigText(getString(C0769R.string.survey_notification_description))).setContentIntent(PendingIntent.getActivity(this, 0, IntentUtils.createInternalOpenUrlIntent(this, getSurveyUrl(), true), 1073741824)));
    }

    private String getSurveyUrl() {
        Object[] objArr = new Object[1];
        objArr[0] = Locale.getDefault().getLanguage().equalsIgnoreCase(new Locale("id").getLanguage()) ? "id" : "en";
        return getString(C0769R.string.survey_notification_url, objArr);
    }

    private void setUpMenu() {
        View inflate = getLayoutInflater().inflate(C0769R.layout.bottom_sheet_main_menu, (ViewGroup) null);
        this.menu = new BottomSheetDialog(this, C0769R.style.BottomSheetTheme);
        this.menu.setContentView(inflate);
        this.menu.setCanceledOnTouchOutside(true);
        this.myshotIndicator = this.menu.findViewById(C0427R.C0426id.menu_my_shot_unread);
        this.nextButton = this.menu.findViewById(C0427R.C0426id.action_next);
        this.loadingButton = this.menu.findViewById(C0427R.C0426id.action_loading);
        this.privateModeButton = this.menu.findViewById(C0427R.C0426id.btn_private_browsing);
        this.privateModeIndicator = this.menu.findViewById(C0427R.C0426id.menu_private_mode_indicator);
        this.shareButton = this.menu.findViewById(C0427R.C0426id.action_share);
        this.bookmarkIcon = this.menu.findViewById(C0427R.C0426id.action_bookmark);
        this.refreshIcon = this.menu.findViewById(C0427R.C0426id.action_refresh);
        this.stopIcon = this.menu.findViewById(C0427R.C0426id.action_stop);
        this.pinShortcut = this.menu.findViewById(C0427R.C0426id.action_pin_shortcut);
        this.myshotButton = this.menu.findViewById(C0427R.C0426id.menu_screenshots);
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported(this)) {
            this.pinShortcut.setVisibility(8);
        }
        this.turboModeButton = this.menu.findViewById(C0427R.C0426id.menu_turbomode);
        this.turboModeButton.setSelected(isTurboEnabled());
        this.blockImageButton = this.menu.findViewById(C0427R.C0426id.menu_blockimg);
        this.blockImageButton.setSelected(isBlockingImages());
        this.nightModeButton = this.menu.findViewById(C0427R.C0426id.menu_night_mode);
        this.nightModeButton.setOnLongClickListener(this.onLongClickListener);
        this.nightModeButton.setSelected(isNightModeEnabled(Settings.getInstance(getApplicationContext())));
    }

    public BrowserFragment getVisibleBrowserFragment() {
        return this.screenNavigator.isBrowserInForeground() ? getBrowserFragment() : null;
    }

    private void openUrl(boolean z, Object obj) {
        ScreenNavigator.get(this).showBrowserScreen(obj != null ? obj.toString() : null, z, false);
    }

    private void showMenu() {
        updateMenu();
        this.menu.show();
    }

    private void updateMenu() {
        this.turboModeButton.setSelected(isTurboEnabled());
        this.blockImageButton.setSelected(isBlockingImages());
        boolean z = true;
        Object obj = (AppConfigWrapper.getMyshotUnreadEnabled(this) && Settings.getInstance(this).hasUnreadMyShot()) ? 1 : null;
        boolean hasPrivateSession = PrivateMode.hasPrivateSession(this);
        Settings instance = Settings.getInstance(getApplicationContext());
        int i = 8;
        this.myshotIndicator.setVisibility(obj != null ? 0 : 8);
        View view = this.privateModeIndicator;
        if (hasPrivateSession) {
            i = 0;
        }
        view.setVisibility(i);
        if (this.pendingMyShotOnBoarding) {
            this.pendingMyShotOnBoarding = false;
            setShowNightModeSpotlight(instance, false);
            this.myshotButton.post(new C0432-$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy-P_T4VgUvo(this));
        }
        this.nightModeButton.setSelected(isNightModeEnabled(instance));
        if (shouldShowNightModeSpotlight(instance)) {
            setShowNightModeSpotlight(instance, false);
            this.nightModeButton.post(new C0431-$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU(this));
        }
        BrowserFragment visibleBrowserFragment = getVisibleBrowserFragment();
        hasPrivateSession = visibleBrowserFragment != null && visibleBrowserFragment.canGoForward();
        setEnable(this.nextButton, hasPrivateSession);
        setLoadingButton(visibleBrowserFragment);
        setEnable(this.bookmarkIcon, visibleBrowserFragment != null);
        setEnable(this.shareButton, visibleBrowserFragment != null);
        View view2 = this.pinShortcut;
        if (visibleBrowserFragment == null) {
            z = false;
        }
        setEnable(view2, z);
        Session focusSession = getSessionManager().getFocusSession();
        if (focusSession != null) {
            this.bookmarkViewModel.getBookmarksByUrl(focusSession.getUrl()).observe(this, new C0681-$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o(this));
        }
    }

    public static /* synthetic */ void lambda$null$1(MainActivity mainActivity, View view) {
        mainActivity.screenNavigator.showBrowserScreen(SupportUtils.getSumoURLForTopic(mainActivity, "screenshot-telemetry"), true, false);
        mainActivity.dismissAllMenus();
    }

    public static /* synthetic */ void lambda$updateMenu$5(MainActivity mainActivity, List list) {
        boolean z = list != null && list.size() > 0;
        mainActivity.bookmarkIcon.setActivated(z);
    }

    private boolean isTurboEnabled() {
        return Settings.getInstance(this).shouldUseTurboMode();
    }

    private boolean isBlockingImages() {
        return Settings.getInstance(this).shouldBlockImages();
    }

    private void showListPanel(int i) {
        ListPanelDialog newInstance = ListPanelDialog.newInstance(i);
        newInstance.setCancelable(true);
        newInstance.show(getSupportFragmentManager(), "");
        this.mDialogFragment = newInstance;
    }

    private void dismissAllMenus() {
        if (this.menu != null) {
            this.menu.dismiss();
        }
        BrowserFragment visibleBrowserFragment = getVisibleBrowserFragment();
        if (visibleBrowserFragment != null) {
            visibleBrowserFragment.dismissWebContextMenu();
            visibleBrowserFragment.dismissGeoDialog();
        }
        if (this.mDialogFragment != null) {
            this.mDialogFragment.dismissAllowingStateLoss();
        }
        if (this.myshotOnBoardingDialog != null) {
            this.myshotOnBoardingDialog.dismiss();
            this.myshotOnBoardingDialog = null;
        }
    }

    public void onMenuItemClicked(View view) {
        if (view.isEnabled()) {
            this.menu.cancel();
            int isBlockingImages;
            switch (view.getId()) {
                case C0427R.C0426id.action_bookmark /*2131296271*/:
                case C0427R.C0426id.action_loading /*2131296276*/:
                case C0427R.C0426id.action_next /*2131296282*/:
                case C0427R.C0426id.action_pin_shortcut /*2131296283*/:
                case C0427R.C0426id.action_share /*2131296285*/:
                    onMenuBrowsingItemClicked(view);
                    break;
                case C0427R.C0426id.btn_private_browsing /*2131296355*/:
                    startActivity(new Intent(this, PrivateModeActivity.class));
                    overridePendingTransition(C0769R.anim.tab_transition_fade_in, C0769R.anim.tab_transition_fade_out);
                    TelemetryWrapper.togglePrivateMode(true);
                    break;
                case C0427R.C0426id.menu_blockimg /*2131296505*/:
                    isBlockingImages = isBlockingImages() ^ 1;
                    Settings.getInstance(this).setBlockImages(isBlockingImages);
                    view.setSelected(isBlockingImages);
                    Toast.makeText(this, isBlockingImages != 0 ? C0769R.string.message_enable_block_image : C0769R.string.message_disable_block_image, 0).show();
                    TelemetryWrapper.menuBlockImageChangeTo(isBlockingImages);
                    break;
                case C0427R.C0426id.menu_bookmark /*2131296506*/:
                    onBookmarksClicked();
                    TelemetryWrapper.clickMenuBookmark();
                    break;
                case C0427R.C0426id.menu_delete /*2131296507*/:
                    onDeleteClicked();
                    TelemetryWrapper.clickMenuClearCache();
                    break;
                case C0427R.C0426id.menu_download /*2131296508*/:
                    onDownloadClicked();
                    TelemetryWrapper.clickMenuDownload();
                    break;
                case C0427R.C0426id.menu_exit /*2131296509*/:
                    onExitClicked();
                    TelemetryWrapper.clickMenuExit();
                    break;
                case C0427R.C0426id.menu_find_in_page /*2131296510*/:
                    onFindInPageClicked();
                    break;
                case C0427R.C0426id.menu_history /*2131296511*/:
                    onHistoryClicked();
                    TelemetryWrapper.clickMenuHistory();
                    break;
                case C0427R.C0426id.menu_night_mode /*2131296520*/:
                    Settings instance = Settings.getInstance(this);
                    int isNightModeEnabled = isNightModeEnabled(instance) ^ 1;
                    view.setSelected(isNightModeEnabled);
                    setNightModeEnabled(instance, isNightModeEnabled);
                    showAdjustBrightnessIfNeeded(instance);
                    TelemetryWrapper.menuNightModeChangeTo(isNightModeEnabled);
                    break;
                case C0427R.C0426id.menu_preferences /*2131296521*/:
                    driveDefaultBrowser();
                    onPreferenceClicked();
                    TelemetryWrapper.clickMenuSettings();
                    break;
                case C0427R.C0426id.menu_screenshots /*2131296523*/:
                    onScreenshotsClicked();
                    TelemetryWrapper.clickMenuCapture();
                    break;
                case C0427R.C0426id.menu_turbomode /*2131296524*/:
                    isBlockingImages = isTurboEnabled() ^ 1;
                    Settings.getInstance(this).setTurboMode(isBlockingImages);
                    view.setSelected(isBlockingImages);
                    Toast.makeText(this, isBlockingImages != 0 ? C0769R.string.message_enable_turbo_mode : C0769R.string.message_disable_turbo_mode, 0).show();
                    TelemetryWrapper.menuTurboChangeTo(isBlockingImages);
                    break;
                default:
                    throw new RuntimeException("Unknown id in menu, onMenuItemClicked() is only for known ids");
            }
        }
    }

    private void driveDefaultBrowser() {
        Settings instance = Settings.getInstance(this);
        if (!instance.isDefaultBrowserSettingDidShow()) {
            instance.addMenuPreferenceClickCount();
            if (instance.getMenuPreferenceClickCount() == AppConfigWrapper.getDriveDefaultBrowserFromMenuSettingThreshold() && !Browsers.isDefaultBrowser(this)) {
                DialogUtils.showDefaultSettingNotification(this);
                TelemetryWrapper.showDefaultSettingNotification();
            }
        }
    }

    private void setEnable(View view, boolean z) {
        view.setEnabled(z);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setEnable(viewGroup.getChildAt(i), z);
            }
        }
    }

    private void setLoadingButton(BrowserFragment browserFragment) {
        int i = 8;
        if (browserFragment == null) {
            setEnable(this.loadingButton, false);
            this.refreshIcon.setVisibility(0);
            this.stopIcon.setVisibility(8);
            this.loadingButton.setTag(Boolean.valueOf(false));
            return;
        }
        setEnable(this.loadingButton, true);
        boolean isLoading = browserFragment.isLoading();
        this.refreshIcon.setVisibility(isLoading ? 8 : 0);
        View view = this.stopIcon;
        if (isLoading) {
            i = 0;
        }
        view.setVisibility(i);
        this.loadingButton.setTag(Boolean.valueOf(isLoading));
    }

    public void onMenuBrowsingItemClicked(View view) {
        BrowserFragment visibleBrowserFragment = getVisibleBrowserFragment();
        if (visibleBrowserFragment != null) {
            switch (view.getId()) {
                case C0427R.C0426id.action_bookmark /*2131296271*/:
                    onBookMarkClicked();
                    break;
                case C0427R.C0426id.action_loading /*2131296276*/:
                    if (((Boolean) view.getTag()).booleanValue()) {
                        onStopClicked(visibleBrowserFragment);
                    } else {
                        onRefreshClicked(visibleBrowserFragment);
                    }
                    TelemetryWrapper.clickToolbarReload();
                    break;
                case C0427R.C0426id.action_next /*2131296282*/:
                    onNextClicked(visibleBrowserFragment);
                    TelemetryWrapper.clickToolbarForward();
                    break;
                case C0427R.C0426id.action_pin_shortcut /*2131296283*/:
                    onAddToHomeClicked();
                    TelemetryWrapper.clickAddToHome();
                    break;
                case C0427R.C0426id.action_share /*2131296285*/:
                    onShraeClicked(visibleBrowserFragment);
                    TelemetryWrapper.clickToolbarShare();
                    break;
                default:
                    throw new RuntimeException("Unknown id in menu, onMenuBrowsingItemClicked() is only for known ids");
            }
        }
    }

    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        this.sessionManager.destroy();
        super.onDestroy();
    }

    private void onPreferenceClicked() {
        openPreferences();
    }

    private void onExitClicked() {
        GeoPermissionCache.clear();
        if (PrivateMode.hasPrivateSession(this)) {
            startActivity(PrivateSessionNotificationService.buildIntent(getApplicationContext(), true));
        }
        finish();
    }

    private void onBookmarksClicked() {
        showListPanel(4);
    }

    private void onDownloadClicked() {
        showListPanel(1);
    }

    private void onHistoryClicked() {
        showListPanel(2);
    }

    private void onScreenshotsClicked() {
        Settings.getInstance(this).setHasUnreadMyShot(false);
        showListPanel(3);
    }

    private void onFindInPageClicked() {
        BrowserFragment visibleBrowserFragment = getVisibleBrowserFragment();
        if (visibleBrowserFragment != null) {
            visibleBrowserFragment.showFindInPage();
        }
    }

    private void onDeleteClicked() {
        Toast.makeText(this, getString(FileUtils.clearCache(this) < 0 ? C0769R.string.message_clear_cache_fail : C0769R.string.message_cleared_cached, new Object[]{FormatUtils.getReadableStringFromFileSize(FileUtils.clearCache(this))}), 0).show();
    }

    private boolean shouldShowNightModeSpotlight(Settings settings) {
        return settings.showNightModeSpotlight();
    }

    private void setShowNightModeSpotlight(Settings settings, boolean z) {
        settings.setNightModeSpotlight(z);
    }

    private void showAdjustBrightness() {
        startActivity(Intents.INSTANCE.getStartIntentFromMenu(this));
    }

    private void showAdjustBrightnessIfNeeded(Settings settings) {
        if (settings.getNightModeBrightnessValue() == -1.0f) {
            settings.setNightModeBrightnessValue(0.125f);
            showAdjustBrightness();
            setShowNightModeSpotlight(settings, true);
        }
    }

    private void applyNightModeBrightness(boolean z, Settings settings, Window window) {
        LayoutParams attributes = window.getAttributes();
        attributes.screenBrightness = z ? settings.getNightModeBrightnessValue() : -1.0f;
        window.setAttributes(attributes);
    }

    private void setNightModeEnabled(Settings settings, boolean z) {
        settings.setNightMode(z);
        applyNightModeBrightness(z, settings, getWindow());
        Fragment topFragment = this.screenNavigator.getTopFragment();
        if (topFragment instanceof BrowserFragment) {
            ((BrowserFragment) topFragment).setNightModeEnabled(z);
        } else if (topFragment instanceof HomeFragment) {
            ((HomeFragment) topFragment).setNightModeEnabled(z);
        }
    }

    private boolean isNightModeEnabled(Settings settings) {
        return settings.isNightModeEnable();
    }

    public BrowserFragment getBrowserFragment() {
        return (BrowserFragment) getSupportFragmentManager().findFragmentById(C0427R.C0426id.browser);
    }

    private void onBookMarkClicked() {
        Session focusSession = getSessionManager().getFocusSession();
        if (focusSession != null) {
            boolean isActivated = this.bookmarkIcon.isActivated();
            TelemetryWrapper.clickToolbarBookmark(isActivated ^ 1);
            if (isActivated) {
                this.bookmarkViewModel.deleteBookmarksByUrl(focusSession.getUrl());
                Toast.makeText(this, C0769R.string.bookmark_removed, 1).show();
                this.bookmarkIcon.setActivated(false);
            } else if (!TextUtils.isEmpty(focusSession.getUrl())) {
                String title = focusSession.getTitle();
                if (TextUtils.isEmpty(title)) {
                    title = UrlUtils.stripCommonSubdomains(UrlUtils.stripHttp(focusSession.getUrl()));
                }
                String addBookmark = this.bookmarkViewModel.addBookmark(title, focusSession.getUrl());
                Snackbar make = Snackbar.make(this.snackBarContainer, (int) C0769R.string.bookmark_saved, 0);
                make.setAction((int) C0769R.string.bookmark_saved_edit, new C0429-$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8(this, addBookmark));
                make.show();
                this.bookmarkIcon.setActivated(true);
            }
        }
    }

    private void onNextClicked(BrowserFragment browserFragment) {
        browserFragment.goForward();
    }

    private void onRefreshClicked(BrowserFragment browserFragment) {
        browserFragment.reload();
    }

    private void onStopClicked(BrowserFragment browserFragment) {
        browserFragment.stop();
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        boolean isTurboEnabled;
        BrowserFragment browserFragment;
        if (getResources().getString(C0769R.string.pref_key_turbo_mode).equals(str)) {
            isTurboEnabled = isTurboEnabled();
            browserFragment = getBrowserFragment();
            if (browserFragment != null) {
                browserFragment.setContentBlockingEnabled(isTurboEnabled);
            }
            setMenuButtonSelected(C0427R.C0426id.menu_turbomode, isTurboEnabled);
        } else if (getResources().getString(C0769R.string.pref_key_performance_block_images).equals(str)) {
            isTurboEnabled = isBlockingImages();
            browserFragment = getBrowserFragment();
            if (browserFragment != null) {
                browserFragment.setImageBlockingEnabled(isTurboEnabled);
            }
            setMenuButtonSelected(C0427R.C0426id.menu_blockimg, isTurboEnabled);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setMenuButtonSelected(int i, boolean z) {
        if (this.menu != null) {
            View findViewById = this.menu.findViewById(i);
            if (findViewById != null) {
                findViewById.setSelected(z);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != Constants.ONE_SECOND) {
            return;
        }
        if (i2 == 100) {
            Toast.makeText(this, C0769R.string.message_deleted_screenshot, 0).show();
            if (this.mDialogFragment != null) {
                Fragment findFragmentById = this.mDialogFragment.getChildFragmentManager().findFragmentById(C0427R.C0426id.main_content);
                if ((findFragmentById instanceof ScreenshotGridFragment) && intent != null) {
                    ((ScreenshotGridFragment) findFragmentById).notifyItemDelete(intent.getLongExtra("extra_screenshot_item_id", -1));
                }
            }
        } else if (i2 == 101 && intent != null) {
            String stringExtra = intent.getStringExtra("extra_url");
            if (this.mDialogFragment != null) {
                this.mDialogFragment.dismissAllowingStateLoss();
            }
            this.screenNavigator.showBrowserScreen(stringExtra, true, false);
        }
    }

    private void onShraeClicked(BrowserFragment browserFragment) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", browserFragment.getUrl());
        startActivity(Intent.createChooser(intent, getString(C0769R.string.share_dialog_title)));
    }

    private void onAddToHomeClicked() {
        Session focusSession = getSessionManager().getFocusSession();
        if (focusSession != null) {
            String url = focusSession.getUrl();
            if (SupportUtils.isUrl(url)) {
                Bitmap favicon = focusSession.getFavicon();
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setClassName(this, "org.mozilla.rocket.activity.MainActivity");
                intent.setData(Uri.parse(url));
                intent.putExtra(LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), true);
                ShortcutUtils.requestPinShortcut(this, intent, focusSession.getTitle(), url, favicon);
            }
        }
    }

    public void onBackPressed() {
        if (!getSupportFragmentManager().isStateSaved()) {
            BrowserScreen visibleBrowserScreen = this.screenNavigator.getVisibleBrowserScreen();
            if ((visibleBrowserScreen != null && visibleBrowserScreen.onBackPressed()) || dismissContentPortal()) {
                return;
            }
            if (this.screenNavigator.canGoBack()) {
                super.onBackPressed();
            } else {
                finish();
            }
        }
    }

    private boolean dismissContentPortal() {
        Fragment topFragment = this.screenNavigator.getTopFragment();
        return topFragment instanceof HomeFragment ? ((HomeFragment) topFragment).hideContentPortal() : false;
    }

    public void firstrunFinished() {
        if (this.pendingUrl != null) {
            this.screenNavigator.showBrowserScreen(this.pendingUrl, true, true);
            this.pendingUrl = null;
            return;
        }
        this.screenNavigator.popToHomeScreen(false);
    }

    public void onNotified(Fragment fragment, TYPE type, Object obj) {
        switch (type) {
            case OPEN_PREFERENCE:
                openPreferences();
                break;
            case SHOW_MENU:
                showMenu();
                break;
            case UPDATE_MENU:
                updateMenu();
                break;
            case OPEN_URL_IN_CURRENT_TAB:
                openUrl(false, obj);
                break;
            case OPEN_URL_IN_NEW_TAB:
                openUrl(true, obj);
                break;
            case SHOW_URL_INPUT:
                if (!getSupportFragmentManager().isStateSaved()) {
                    this.screenNavigator.addUrlScreen(obj != null ? obj.toString() : null);
                    break;
                }
                return;
            case DISMISS_URL_INPUT:
                this.screenNavigator.popUrlScreen();
                break;
            case SHOW_TAB_TRAY:
                TabTray.show(getSupportFragmentManager());
                break;
            case REFRESH_TOP_SITE:
                fragment = this.screenNavigator.getTopFragment();
                if (fragment instanceof HomeFragment) {
                    ((HomeFragment) fragment).updateTopSitesData();
                    break;
                }
                break;
            case SHOW_MY_SHOT_ON_BOARDING:
                showMyShotOnBoarding();
                break;
            case SHOW_DOWNLOAD_PANEL:
                onDownloadClicked();
                break;
        }
    }

    public ScreenNavigator getScreenNavigator() {
        return this.screenNavigator;
    }

    public FirstrunFragment createFirstRunScreen() {
        return FirstrunFragment.create();
    }

    public BrowserFragment getBrowserScreen() {
        return (BrowserFragment) getSupportFragmentManager().findFragmentById(C0427R.C0426id.browser);
    }

    public UrlInputFragment createUrlInputScreen(String str, String str2) {
        return UrlInputFragment.create(str, str2, true);
    }

    public HomeFragment createHomeScreen() {
        return HomeFragment.create();
    }

    private void showMessage(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            Toast.makeText(this, charSequence, 0).show();
        }
    }

    private void asyncInitialize() {
        new Thread(new C04373()).start();
    }

    private void asyncCheckStorage() {
        boolean z = false;
        try {
            if (StorageUtils.getTargetDirOnRemovableStorageForDownloads(this, "*/*") != null) {
                z = true;
            }
        } catch (NoRemovableStorageException unused) {
        }
        Settings.getInstance(this).setRemovableStorageStateOnCreate(z);
    }

    public SessionManager getSessionManager() {
        if (this.sessionManager == null) {
            this.sessionManager = new SessionManager(new MainTabViewProvider(this));
        }
        return this.sessionManager;
    }

    public boolean isTabRestoredComplete() {
        return this.isTabRestoredComplete;
    }

    public void onQueryComplete(List<SessionWithState> list, String str) {
        this.isTabRestoredComplete = true;
        getSessionManager().restore(list, str);
        Session focusSession = getSessionManager().getFocusSession();
        if (!Settings.getInstance(this).shouldShowFirstrun() && focusSession != null && !getSupportFragmentManager().isStateSaved()) {
            this.screenNavigator.restoreBrowserScreen(focusSession.getId());
        }
    }

    private void restoreTabsFromPersistence() {
        this.isTabRestoredComplete = false;
        TabModelStore.getInstance(this).getSavedTabs(this, this);
    }

    private void saveTabsToPersistence() {
        if (this.isTabRestoredComplete) {
            List<Session> tabs = getSessionManager().getTabs();
            for (Session session : tabs) {
                if (session.getEngineSession() != null) {
                    session.getEngineSession().saveState();
                }
            }
            TabModelStore.getInstance(this).saveTabs(this, tabs, getSessionManager().getFocusSession() != null ? getSessionManager().getFocusSession().getId() : null, null);
        }
    }

    public void onPointerCaptureChanged(boolean z) {
        Settings.getInstance(this).getEventHistory().add("post_survey_notification");
    }

    public void showRateAppDialog() {
        DialogUtils.showRateAppDialog(this);
        TelemetryWrapper.showRateApp(false);
    }

    public void showRateAppNotification() {
        DialogUtils.showRateAppNotification(this);
        TelemetryWrapper.showRateApp(true);
    }

    public void showShareAppDialog() {
        DialogUtils.showShareAppDialog(this);
        TelemetryWrapper.showPromoteShareDialog();
    }

    public void showPrivacyPolicyUpdateNotification() {
        DialogUtils.showPrivacyPolicyUpdateNotification(this);
    }

    public void showRateAppDialogFromIntent() {
        DialogUtils.showRateAppDialog(this);
        TelemetryWrapper.showRateApp(false);
        NotificationManagerCompat.from(this).cancel(1001);
        if (getIntent().getExtras() != null) {
            getIntent().getExtras().putBoolean("show_rate_dialog", false);
        }
    }

    public void showMyShotOnBoarding() {
        this.pendingMyShotOnBoarding = true;
        showMenu();
    }
}
