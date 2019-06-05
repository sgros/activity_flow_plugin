// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.activity;

import android.util.AttributeSet;
import org.mozilla.rocket.tabs.TabView;
import android.support.v4.app.NotificationManagerCompat;
import android.app.PendingIntent;
import org.mozilla.focus.utils.IntentUtils;
import android.support.v4.app.NotificationCompat;
import org.mozilla.focus.notification.NotificationUtil;
import android.content.SharedPreferences;
import org.mozilla.focus.provider.DownloadContract;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import org.mozilla.focus.tabs.tabtray.TabTray;
import org.mozilla.rocket.privately.PrivateModeActivity;
import android.preference.PreferenceManager;
import android.arch.lifecycle.ViewModelProvider;
import android.support.v4.app.FragmentActivity;
import android.arch.lifecycle.ViewModelProviders;
import org.mozilla.focus.repository.BookmarkRepository;
import org.mozilla.focus.persistence.BookmarksDatabase;
import org.mozilla.rocket.promotion.PromotionPresenter;
import org.mozilla.focus.Inject;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.focus.utils.NewFeatureNotice;
import org.mozilla.focus.utils.SafeIntent;
import android.os.Bundle;
import org.mozilla.focus.screenshot.ScreenshotGridFragment;
import android.content.res.Resources$Theme;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.focus.urlinput.UrlInputFragment;
import org.mozilla.focus.fragment.FirstrunFragment;
import org.mozilla.focus.persistence.BookmarkModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;
import org.mozilla.focus.fragment.ListPanelDialog;
import org.mozilla.rocket.nightmode.AdjustBrightnessDialog;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.view.ViewGroup;
import java.util.Iterator;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.privately.PrivateMode;
import org.mozilla.focus.web.GeoPermissionCache;
import org.mozilla.focus.utils.FormatUtils;
import org.mozilla.fileutils.FileUtils;
import android.support.design.widget.Snackbar;
import org.mozilla.urlutils.UrlUtils;
import android.text.TextUtils;
import android.widget.Toast;
import android.graphics.Bitmap;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.focus.utils.ShortcutUtils;
import org.mozilla.rocket.component.LaunchIntentDispatcher;
import android.net.Uri;
import java.util.List;
import android.view.View$OnClickListener;
import android.content.DialogInterface$OnCancelListener;
import android.app.Activity;
import org.mozilla.focus.utils.SupportUtils;
import android.content.DialogInterface;
import org.mozilla.focus.download.DownloadInfoManager;
import android.content.Intent;
import java.util.Locale;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.DialogUtils;
import org.mozilla.focus.utils.Browsers;
import org.mozilla.focus.utils.AppConfigWrapper;
import android.support.v4.app.Fragment;
import org.mozilla.focus.home.HomeFragment;
import org.mozilla.focus.fragment.BrowserFragment;
import org.mozilla.focus.utils.NoRemovableStorageException;
import android.content.Context;
import org.mozilla.focus.utils.StorageUtils;
import android.view.WindowManager$LayoutParams;
import android.view.Window;
import android.os.Handler;
import org.mozilla.focus.utils.Settings;
import android.content.BroadcastReceiver;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.promotion.PromotionModel;
import android.view.View$OnLongClickListener;
import android.app.Dialog;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.DialogFragment;
import android.database.ContentObserver;
import org.mozilla.rocket.download.DownloadIndicatorViewModel;
import org.mozilla.focus.viewmodel.BookmarkViewModel;
import android.view.View;
import org.mozilla.rocket.theme.ThemeManager;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.promotion.PromotionViewContract;
import org.mozilla.focus.widget.TabRestoreMonitor;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.persistence.TabModelStore;
import org.mozilla.focus.navigation.ScreenNavigator;
import android.content.SharedPreferences$OnSharedPreferenceChangeListener;

public class MainActivity extends BaseActivity implements SharedPreferences$OnSharedPreferenceChangeListener, HostActivity, Provider, AsyncQueryListener, FragmentListener, TabRestoreMonitor, PromotionViewContract, SessionHost, ThemeHost
{
    private View blockImageButton;
    private View bookmarkIcon;
    private BookmarkViewModel bookmarkViewModel;
    private DownloadIndicatorViewModel downloadIndicatorViewModel;
    private ContentObserver downloadObserver;
    private boolean isTabRestoredComplete;
    private View loadingButton;
    private DialogFragment mDialogFragment;
    private BottomSheetDialog menu;
    private View myshotButton;
    private View myshotIndicator;
    private Dialog myshotOnBoardingDialog;
    private View nextButton;
    private View nightModeButton;
    View$OnLongClickListener onLongClickListener;
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
    
    public MainActivity() {
        this.isTabRestoredComplete = false;
        this.onLongClickListener = (View$OnLongClickListener)new View$OnLongClickListener() {
            public boolean onLongClick(final View view) {
                MainActivity.this.menu.cancel();
                if (view.getId() == 2131296520) {
                    MainActivity.this.setNightModeEnabled(Settings.getInstance(((ThemeManager.ThemeHost)MainActivity.this).getApplicationContext()), true);
                    MainActivity.this.showAdjustBrightness();
                    return true;
                }
                throw new RuntimeException("Unknown id in menu, OnLongClickListener() is only for known ids");
            }
        };
        this.downloadObserver = new ContentObserver((Handler)null) {
            public void onChange(final boolean b) {
                MainActivity.this.downloadIndicatorViewModel.updateIndicator();
            }
        };
    }
    
    private void applyNightModeBrightness(final boolean b, final Settings settings, final Window window) {
        final WindowManager$LayoutParams attributes = window.getAttributes();
        float nightModeBrightnessValue;
        if (b) {
            nightModeBrightnessValue = settings.getNightModeBrightnessValue();
        }
        else {
            nightModeBrightnessValue = -1.0f;
        }
        attributes.screenBrightness = nightModeBrightnessValue;
        window.setAttributes(attributes);
    }
    
    private void asyncCheckStorage() {
        boolean removableStorageStateOnCreate = false;
        while (true) {
            try {
                if (StorageUtils.getTargetDirOnRemovableStorageForDownloads((Context)this, "*/*") != null) {
                    removableStorageStateOnCreate = true;
                }
                Settings.getInstance((Context)this).setRemovableStorageStateOnCreate(removableStorageStateOnCreate);
            }
            catch (NoRemovableStorageException ex) {
                continue;
            }
            break;
        }
    }
    
    private void asyncInitialize() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.this.asyncCheckStorage();
            }
        }).start();
    }
    
    private void dismissAllMenus() {
        if (this.menu != null) {
            this.menu.dismiss();
        }
        final BrowserFragment visibleBrowserFragment = this.getVisibleBrowserFragment();
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
    
    private boolean dismissContentPortal() {
        final Fragment topFragment = this.screenNavigator.getTopFragment();
        return topFragment instanceof HomeFragment && ((HomeFragment)topFragment).hideContentPortal();
    }
    
    private void driveDefaultBrowser() {
        final Settings instance = Settings.getInstance((Context)this);
        if (instance.isDefaultBrowserSettingDidShow()) {
            return;
        }
        instance.addMenuPreferenceClickCount();
        if (instance.getMenuPreferenceClickCount() == AppConfigWrapper.getDriveDefaultBrowserFromMenuSettingThreshold() && !Browsers.isDefaultBrowser((Context)this)) {
            DialogUtils.showDefaultSettingNotification((Context)this);
            TelemetryWrapper.showDefaultSettingNotification();
        }
    }
    
    private String getSurveyUrl() {
        String s;
        if (Locale.getDefault().getLanguage().equalsIgnoreCase(new Locale("id").getLanguage())) {
            s = "id";
        }
        else {
            s = "en";
        }
        return this.getString(2131755417, new Object[] { s });
    }
    
    private void initBroadcastReceivers() {
        this.uiMessageReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                final String action = intent.getAction();
                final int hashCode = action.hashCode();
                int n = 0;
                Label_0055: {
                    if (hashCode != 480525853) {
                        if (hashCode == 1438864026) {
                            if (action.equals("org.mozilla.action.NOTIFY_UI")) {
                                n = 0;
                                break Label_0055;
                            }
                        }
                    }
                    else if (action.equals("org.mozilla.action.RELOCATE_FINISH")) {
                        n = 1;
                        break Label_0055;
                    }
                    n = -1;
                }
                switch (n) {
                    case 1: {
                        DownloadInfoManager.getInstance().showOpenDownloadSnackBar(intent.getLongExtra("org.mozilla.extra.row_id", -1L), MainActivity.this.snackBarContainer, "MainActivity");
                        break;
                    }
                    case 0: {
                        MainActivity.this.showMessage(intent.getCharSequenceExtra("org.mozilla.extra.message"));
                        break;
                    }
                }
            }
        };
    }
    
    private void initViews() {
        this.getWindow().getDecorView().setSystemUiVisibility(this.getWindow().getDecorView().getSystemUiVisibility() | 0x500);
        this.snackBarContainer = this.findViewById(2131296374);
        this.setUpMenu();
    }
    
    private boolean isBlockingImages() {
        return Settings.getInstance((Context)this).shouldBlockImages();
    }
    
    private boolean isNightModeEnabled(final Settings settings) {
        return settings.isNightModeEnable();
    }
    
    private boolean isTurboEnabled() {
        return Settings.getInstance((Context)this).shouldUseTurboMode();
    }
    
    private void onAddToHomeClicked() {
        final Session focusSession = this.getSessionManager().getFocusSession();
        if (focusSession == null) {
            return;
        }
        final String url = focusSession.getUrl();
        if (!SupportUtils.isUrl(url)) {
            return;
        }
        final Bitmap favicon = focusSession.getFavicon();
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setClassName((Context)this, "org.mozilla.rocket.activity.MainActivity");
        intent.setData(Uri.parse(url));
        intent.putExtra(LaunchIntentDispatcher.LaunchMethod.EXTRA_BOOL_HOME_SCREEN_SHORTCUT.getValue(), true);
        ShortcutUtils.requestPinShortcut((Context)this, intent, focusSession.getTitle(), url, favicon);
    }
    
    private void onBookMarkClicked() {
        final Session focusSession = this.getSessionManager().getFocusSession();
        if (focusSession == null) {
            return;
        }
        final boolean activated = this.bookmarkIcon.isActivated();
        TelemetryWrapper.clickToolbarBookmark(activated ^ true);
        if (activated) {
            this.bookmarkViewModel.deleteBookmarksByUrl(focusSession.getUrl());
            Toast.makeText((Context)this, 2131755068, 1).show();
            this.bookmarkIcon.setActivated(false);
        }
        else {
            if (TextUtils.isEmpty((CharSequence)focusSession.getUrl())) {
                return;
            }
            String s;
            if (TextUtils.isEmpty((CharSequence)(s = focusSession.getTitle()))) {
                s = UrlUtils.stripCommonSubdomains(UrlUtils.stripHttp(focusSession.getUrl()));
            }
            final String addBookmark = this.bookmarkViewModel.addBookmark(s, focusSession.getUrl());
            final Snackbar make = Snackbar.make(this.snackBarContainer, 2131755069, 0);
            make.setAction(2131755070, (View$OnClickListener)new _$$Lambda$MainActivity$8YTaJwSpEv5fKJyTfPnaPvg8EA8(this, addBookmark));
            make.show();
            this.bookmarkIcon.setActivated(true);
        }
    }
    
    private void onBookmarksClicked() {
        this.showListPanel(4);
    }
    
    private void onDeleteClicked() {
        final long clearCache = FileUtils.clearCache((Context)this);
        int n;
        if (clearCache < 0L) {
            n = 2131755255;
        }
        else {
            n = 2131755257;
        }
        Toast.makeText((Context)this, (CharSequence)this.getString(n, new Object[] { FormatUtils.getReadableStringFromFileSize(clearCache) }), 0).show();
    }
    
    private void onDownloadClicked() {
        this.showListPanel(1);
    }
    
    private void onExitClicked() {
        GeoPermissionCache.clear();
        if (PrivateMode.hasPrivateSession((Context)this)) {
            this.startActivity(PrivateSessionNotificationService.buildIntent(((ThemeManager.ThemeHost)this).getApplicationContext(), true));
        }
        this.finish();
    }
    
    private void onFindInPageClicked() {
        final BrowserFragment visibleBrowserFragment = this.getVisibleBrowserFragment();
        if (visibleBrowserFragment != null) {
            visibleBrowserFragment.showFindInPage();
        }
    }
    
    private void onHistoryClicked() {
        this.showListPanel(2);
    }
    
    private void onNextClicked(final BrowserFragment browserFragment) {
        browserFragment.goForward();
    }
    
    private void onPreferenceClicked() {
        this.openPreferences();
    }
    
    private void onRefreshClicked(final BrowserFragment browserFragment) {
        browserFragment.reload();
    }
    
    private void onScreenshotsClicked() {
        Settings.getInstance((Context)this).setHasUnreadMyShot(false);
        this.showListPanel(3);
    }
    
    private void onShraeClicked(final BrowserFragment browserFragment) {
        final Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", browserFragment.getUrl());
        this.startActivity(Intent.createChooser(intent, (CharSequence)this.getString(2131755412)));
    }
    
    private void onStopClicked(final BrowserFragment browserFragment) {
        browserFragment.stop();
    }
    
    private void openUrl(final boolean b, final Object o) {
        String string;
        if (o != null) {
            string = o.toString();
        }
        else {
            string = null;
        }
        ScreenNavigator.get((Context)this).showBrowserScreen(string, b, false);
    }
    
    private void restoreTabsFromPersistence() {
        this.isTabRestoredComplete = false;
        TabModelStore.getInstance((Context)this).getSavedTabs((Context)this, (TabModelStore.AsyncQueryListener)this);
    }
    
    private void saveTabsToPersistence() {
        if (!this.isTabRestoredComplete) {
            return;
        }
        final List<Session> tabs = this.getSessionManager().getTabs();
        for (final Session session : tabs) {
            if (session.getEngineSession() != null) {
                session.getEngineSession().saveState();
            }
        }
        String id;
        if (this.getSessionManager().getFocusSession() != null) {
            id = this.getSessionManager().getFocusSession().getId();
        }
        else {
            id = null;
        }
        TabModelStore.getInstance((Context)this).saveTabs((Context)this, tabs, id, null);
    }
    
    private void setEnable(final View view, final boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                this.setEnable(viewGroup.getChildAt(i), enabled);
            }
        }
    }
    
    private void setLoadingButton(final BrowserFragment browserFragment) {
        final int n = 8;
        if (browserFragment == null) {
            this.setEnable(this.loadingButton, false);
            this.refreshIcon.setVisibility(0);
            this.stopIcon.setVisibility(8);
            this.loadingButton.setTag((Object)false);
        }
        else {
            this.setEnable(this.loadingButton, true);
            final boolean loading = browserFragment.isLoading();
            final View refreshIcon = this.refreshIcon;
            int visibility;
            if (loading) {
                visibility = 8;
            }
            else {
                visibility = 0;
            }
            refreshIcon.setVisibility(visibility);
            final View stopIcon = this.stopIcon;
            int visibility2 = n;
            if (loading) {
                visibility2 = 0;
            }
            stopIcon.setVisibility(visibility2);
            this.loadingButton.setTag((Object)loading);
        }
    }
    
    private void setNightModeEnabled(final Settings settings, final boolean nightModeEnabled) {
        settings.setNightMode(nightModeEnabled);
        this.applyNightModeBrightness(nightModeEnabled, settings, this.getWindow());
        final Fragment topFragment = this.screenNavigator.getTopFragment();
        if (topFragment instanceof BrowserFragment) {
            ((BrowserFragment)topFragment).setNightModeEnabled(nightModeEnabled);
        }
        else if (topFragment instanceof HomeFragment) {
            ((HomeFragment)topFragment).setNightModeEnabled(nightModeEnabled);
        }
    }
    
    private void setShowNightModeSpotlight(final Settings settings, final boolean nightModeSpotlight) {
        settings.setNightModeSpotlight(nightModeSpotlight);
    }
    
    private void setUpMenu() {
        (this.menu = new BottomSheetDialog((Context)this, 2131820747)).setContentView(this.getLayoutInflater().inflate(2131492902, (ViewGroup)null));
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
        if (!ShortcutManagerCompat.isRequestPinShortcutSupported((Context)this)) {
            this.pinShortcut.setVisibility(8);
        }
        (this.turboModeButton = this.menu.findViewById(2131296524)).setSelected(this.isTurboEnabled());
        (this.blockImageButton = this.menu.findViewById(2131296505)).setSelected(this.isBlockingImages());
        (this.nightModeButton = this.menu.findViewById(2131296520)).setOnLongClickListener(this.onLongClickListener);
        this.nightModeButton.setSelected(this.isNightModeEnabled(Settings.getInstance(((ThemeManager.ThemeHost)this).getApplicationContext())));
    }
    
    private boolean shouldShowNightModeSpotlight(final Settings settings) {
        return settings.showNightModeSpotlight();
    }
    
    private void showAdjustBrightness() {
        this.startActivity(AdjustBrightnessDialog.Intents.INSTANCE.getStartIntentFromMenu((Context)this));
    }
    
    private void showAdjustBrightnessIfNeeded(final Settings settings) {
        if (settings.getNightModeBrightnessValue() == -1.0f) {
            settings.setNightModeBrightnessValue(0.125f);
            this.showAdjustBrightness();
            this.setShowNightModeSpotlight(settings, true);
        }
    }
    
    private void showListPanel(final int n) {
        final ListPanelDialog instance = ListPanelDialog.newInstance(n);
        instance.setCancelable(true);
        instance.show(this.getSupportFragmentManager(), "");
        this.mDialogFragment = instance;
    }
    
    private void showMenu() {
        this.updateMenu();
        this.menu.show();
    }
    
    private void showMessage(final CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            return;
        }
        Toast.makeText((Context)this, charSequence, 0).show();
    }
    
    private void updateMenu() {
        this.turboModeButton.setSelected(this.isTurboEnabled());
        this.blockImageButton.setSelected(this.isBlockingImages());
        final boolean myshotUnreadEnabled = AppConfigWrapper.getMyshotUnreadEnabled((Context)this);
        final boolean b = true;
        final boolean b2 = myshotUnreadEnabled && Settings.getInstance((Context)this).hasUnreadMyShot();
        final boolean hasPrivateSession = PrivateMode.hasPrivateSession((Context)this);
        final Settings instance = Settings.getInstance(((ThemeManager.ThemeHost)this).getApplicationContext());
        final View myshotIndicator = this.myshotIndicator;
        final int n = 8;
        int visibility;
        if (b2) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        myshotIndicator.setVisibility(visibility);
        final View privateModeIndicator = this.privateModeIndicator;
        int visibility2 = n;
        if (hasPrivateSession) {
            visibility2 = 0;
        }
        privateModeIndicator.setVisibility(visibility2);
        if (this.pendingMyShotOnBoarding) {
            this.setShowNightModeSpotlight(instance, this.pendingMyShotOnBoarding = false);
            this.myshotButton.post((Runnable)new _$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy_P_T4VgUvo(this));
        }
        this.nightModeButton.setSelected(this.isNightModeEnabled(instance));
        if (this.shouldShowNightModeSpotlight(instance)) {
            this.setShowNightModeSpotlight(instance, false);
            this.nightModeButton.post((Runnable)new _$$Lambda$MainActivity$QszpWcFeD_6v9pC1ky_3bpKxZlU(this));
        }
        final BrowserFragment visibleBrowserFragment = this.getVisibleBrowserFragment();
        this.setEnable(this.nextButton, visibleBrowserFragment != null && visibleBrowserFragment.canGoForward());
        this.setLoadingButton(visibleBrowserFragment);
        this.setEnable(this.bookmarkIcon, visibleBrowserFragment != null);
        this.setEnable(this.shareButton, visibleBrowserFragment != null);
        this.setEnable(this.pinShortcut, visibleBrowserFragment != null && b);
        final Session focusSession = this.getSessionManager().getFocusSession();
        if (focusSession == null) {
            return;
        }
        this.bookmarkViewModel.getBookmarksByUrl(focusSession.getUrl()).observe(this, new _$$Lambda$MainActivity$d7XGEV0md3UPIIt73gfCZLP0q8o(this));
    }
    
    @Override
    public void applyLocale() {
        this.setUpMenu();
    }
    
    public FirstrunFragment createFirstRunScreen() {
        return FirstrunFragment.create();
    }
    
    public HomeFragment createHomeScreen() {
        return HomeFragment.create();
    }
    
    public UrlInputFragment createUrlInputScreen(final String s, final String s2) {
        return UrlInputFragment.create(s, s2, true);
    }
    
    public void firstrunFinished() {
        if (this.pendingUrl != null) {
            this.screenNavigator.showBrowserScreen(this.pendingUrl, true, true);
            this.pendingUrl = null;
        }
        else {
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
            this.sessionManager = new SessionManager(new MainTabViewProvider(this));
        }
        return this.sessionManager;
    }
    
    public Resources$Theme getTheme() {
        final Resources$Theme theme = super.getTheme();
        if (this.themeManager != null) {
            this.themeManager.applyCurrentTheme(theme);
        }
        return theme;
    }
    
    public ThemeManager getThemeManager() {
        return this.themeManager;
    }
    
    public BrowserFragment getVisibleBrowserFragment() {
        BrowserFragment browserFragment;
        if (this.screenNavigator.isBrowserInForeground()) {
            browserFragment = this.getBrowserFragment();
        }
        else {
            browserFragment = null;
        }
        return browserFragment;
    }
    
    public boolean isTabRestoredComplete() {
        return this.isTabRestoredComplete;
    }
    
    @Override
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (n == 1000) {
            if (n2 == 100) {
                Toast.makeText((Context)this, 2131755258, 0).show();
                if (this.mDialogFragment != null) {
                    final Fragment fragmentById = this.mDialogFragment.getChildFragmentManager().findFragmentById(2131296499);
                    if (fragmentById instanceof ScreenshotGridFragment && intent != null) {
                        ((ScreenshotGridFragment)fragmentById).notifyItemDelete(intent.getLongExtra("extra_screenshot_item_id", -1L));
                    }
                }
            }
            else if (n2 == 101 && intent != null) {
                final String stringExtra = intent.getStringExtra("extra_url");
                if (this.mDialogFragment != null) {
                    this.mDialogFragment.dismissAllowingStateLoss();
                }
                this.screenNavigator.showBrowserScreen(stringExtra, true, false);
            }
        }
    }
    
    public void onBackPressed() {
        if (this.getSupportFragmentManager().isStateSaved()) {
            return;
        }
        final ScreenNavigator.BrowserScreen visibleBrowserScreen = this.screenNavigator.getVisibleBrowserScreen();
        if (visibleBrowserScreen != null && visibleBrowserScreen.onBackPressed()) {
            return;
        }
        if (this.dismissContentPortal()) {
            return;
        }
        if (!this.screenNavigator.canGoBack()) {
            this.finish();
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        this.themeManager = new ThemeManager((ThemeManager.ThemeHost)this);
        super.onCreate(bundle);
        this.asyncInitialize();
        this.setContentView(2131492894);
        this.initViews();
        this.initBroadcastReceivers();
        this.screenNavigator = new ScreenNavigator((ScreenNavigator.HostActivity)this);
        final SafeIntent safeIntent = new SafeIntent(this.getIntent());
        if (bundle == null) {
            if ("android.intent.action.VIEW".equals(safeIntent.getAction())) {
                final String dataString = safeIntent.getDataString();
                if (Settings.getInstance((Context)this).shouldShowFirstrun()) {
                    this.pendingUrl = dataString;
                    this.screenNavigator.addFirstRunScreen();
                }
                else {
                    this.screenNavigator.showBrowserScreen(dataString, safeIntent.getBooleanExtra("open_new_tab", false), true);
                }
            }
            else if (Settings.getInstance((Context)this).shouldShowFirstrun()) {
                this.screenNavigator.addFirstRunScreen();
            }
            else {
                this.screenNavigator.popToHomeScreen(false);
            }
        }
        if (NewFeatureNotice.getInstance((Context)this).shouldShowLiteUpdate()) {
            this.themeManager.resetDefaultTheme();
        }
        this.restoreTabsFromPersistence();
        WebViewProvider.preload((Context)this);
        this.promotionModel = new PromotionModel((Context)this, safeIntent);
        if (Inject.getActivityNewlyCreatedFlag()) {
            Inject.setActivityNewlyCreatedFlag();
            PromotionPresenter.runPromotion(this, this.promotionModel);
        }
        this.bookmarkViewModel = ViewModelProviders.of(this, new BookmarkViewModel.Factory(BookmarkRepository.getInstance(BookmarksDatabase.getInstance((Context)this)))).get(BookmarkViewModel.class);
        PreferenceManager.getDefaultSharedPreferences((Context)this).registerOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        this.downloadIndicatorViewModel = Inject.obtainDownloadIndicatorViewModel(this);
    }
    
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences((Context)this).unregisterOnSharedPreferenceChangeListener((SharedPreferences$OnSharedPreferenceChangeListener)this);
        this.sessionManager.destroy();
        super.onDestroy();
    }
    
    public void onMenuBrowsingItemClicked(final View view) {
        final BrowserFragment visibleBrowserFragment = this.getVisibleBrowserFragment();
        if (visibleBrowserFragment == null) {
            return;
        }
        switch (view.getId()) {
            default: {
                throw new RuntimeException("Unknown id in menu, onMenuBrowsingItemClicked() is only for known ids");
            }
            case 2131296285: {
                this.onShraeClicked(visibleBrowserFragment);
                TelemetryWrapper.clickToolbarShare();
                break;
            }
            case 2131296283: {
                this.onAddToHomeClicked();
                TelemetryWrapper.clickAddToHome();
                break;
            }
            case 2131296282: {
                this.onNextClicked(visibleBrowserFragment);
                TelemetryWrapper.clickToolbarForward();
                break;
            }
            case 2131296276: {
                if (view.getTag()) {
                    this.onStopClicked(visibleBrowserFragment);
                }
                else {
                    this.onRefreshClicked(visibleBrowserFragment);
                }
                TelemetryWrapper.clickToolbarReload();
                break;
            }
            case 2131296271: {
                this.onBookMarkClicked();
                break;
            }
        }
    }
    
    public void onMenuItemClicked(final View view) {
        if (!view.isEnabled()) {
            return;
        }
        this.menu.cancel();
        switch (view.getId()) {
            default: {
                throw new RuntimeException("Unknown id in menu, onMenuItemClicked() is only for known ids");
            }
            case 2131296524: {
                final boolean b = this.isTurboEnabled() ^ true;
                Settings.getInstance((Context)this).setTurboMode(b);
                view.setSelected(b);
                int n;
                if (b) {
                    n = 2131755262;
                }
                else {
                    n = 2131755260;
                }
                Toast.makeText((Context)this, n, 0).show();
                TelemetryWrapper.menuTurboChangeTo(b);
                break;
            }
            case 2131296523: {
                this.onScreenshotsClicked();
                TelemetryWrapper.clickMenuCapture();
                break;
            }
            case 2131296521: {
                this.driveDefaultBrowser();
                this.onPreferenceClicked();
                TelemetryWrapper.clickMenuSettings();
                break;
            }
            case 2131296520: {
                final Settings instance = Settings.getInstance((Context)this);
                final boolean selected = this.isNightModeEnabled(instance) ^ true;
                view.setSelected(selected);
                this.setNightModeEnabled(instance, selected);
                this.showAdjustBrightnessIfNeeded(instance);
                TelemetryWrapper.menuNightModeChangeTo(selected);
                break;
            }
            case 2131296511: {
                this.onHistoryClicked();
                TelemetryWrapper.clickMenuHistory();
                break;
            }
            case 2131296510: {
                this.onFindInPageClicked();
                break;
            }
            case 2131296509: {
                this.onExitClicked();
                TelemetryWrapper.clickMenuExit();
                break;
            }
            case 2131296508: {
                this.onDownloadClicked();
                TelemetryWrapper.clickMenuDownload();
                break;
            }
            case 2131296507: {
                this.onDeleteClicked();
                TelemetryWrapper.clickMenuClearCache();
                break;
            }
            case 2131296506: {
                this.onBookmarksClicked();
                TelemetryWrapper.clickMenuBookmark();
                break;
            }
            case 2131296505: {
                final boolean b2 = this.isBlockingImages() ^ true;
                Settings.getInstance((Context)this).setBlockImages(b2);
                view.setSelected(b2);
                int n2;
                if (b2) {
                    n2 = 2131755261;
                }
                else {
                    n2 = 2131755259;
                }
                Toast.makeText((Context)this, n2, 0).show();
                TelemetryWrapper.menuBlockImageChangeTo(b2);
                break;
            }
            case 2131296355: {
                this.startActivity(new Intent((Context)this, (Class)PrivateModeActivity.class));
                this.overridePendingTransition(2130771989, 2130771990);
                TelemetryWrapper.togglePrivateMode(true);
                break;
            }
            case 2131296271:
            case 2131296276:
            case 2131296282:
            case 2131296283:
            case 2131296285: {
                this.onMenuBrowsingItemClicked(view);
                break;
            }
        }
    }
    
    protected void onNewIntent(final Intent intent) {
        final SafeIntent safeIntent = new SafeIntent(intent);
        if (this.promotionModel != null) {
            this.promotionModel.parseIntent(safeIntent);
            if (PromotionPresenter.runPromotionFromIntent(this, this.promotionModel)) {
                return;
            }
        }
        if ("android.intent.action.VIEW".equals(safeIntent.getAction())) {
            this.pendingUrl = safeIntent.getDataString();
            this.dismissAllMenus();
            TabTray.dismiss(this.getSupportFragmentManager());
        }
        this.setIntent(intent);
    }
    
    public void onNotified(Fragment topFragment, final TYPE type, final Object o) {
        switch (MainActivity$5.$SwitchMap$org$mozilla$focus$widget$FragmentListener$TYPE[type.ordinal()]) {
            case 11: {
                this.onDownloadClicked();
                break;
            }
            case 10: {
                this.showMyShotOnBoarding();
                break;
            }
            case 9: {
                topFragment = this.screenNavigator.getTopFragment();
                if (topFragment instanceof HomeFragment) {
                    ((HomeFragment)topFragment).updateTopSitesData();
                    break;
                }
                break;
            }
            case 8: {
                TabTray.show(this.getSupportFragmentManager());
                break;
            }
            case 7: {
                this.screenNavigator.popUrlScreen();
                break;
            }
            case 6: {
                if (this.getSupportFragmentManager().isStateSaved()) {
                    return;
                }
                String string;
                if (o != null) {
                    string = o.toString();
                }
                else {
                    string = null;
                }
                this.screenNavigator.addUrlScreen(string);
                break;
            }
            case 5: {
                this.openUrl(true, o);
                break;
            }
            case 4: {
                this.openUrl(false, o);
                break;
            }
            case 3: {
                this.updateMenu();
                break;
            }
            case 2: {
                this.showMenu();
                break;
            }
            case 1: {
                this.openPreferences();
                break;
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.uiMessageReceiver);
        this.getContentResolver().unregisterContentObserver(this.downloadObserver);
        TelemetryWrapper.stopSession();
        this.saveTabsToPersistence();
    }
    
    public void onPointerCaptureChanged(final boolean b) {
        Settings.getInstance((Context)this).getEventHistory().add("post_survey_notification");
    }
    
    public void onQueryComplete(final List<SessionManager.SessionWithState> list, final String s) {
        this.isTabRestoredComplete = true;
        this.getSessionManager().restore(list, s);
        final Session focusSession = this.getSessionManager().getFocusSession();
        if (!Settings.getInstance((Context)this).shouldShowFirstrun() && focusSession != null && !this.getSupportFragmentManager().isStateSaved()) {
            this.screenNavigator.restoreBrowserScreen(focusSession.getId());
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        TelemetryWrapper.startSession();
        final IntentFilter intentFilter = new IntentFilter("org.mozilla.action.NOTIFY_UI");
        intentFilter.addCategory("org.mozilla.category.FILE_OPERATION");
        intentFilter.addAction("org.mozilla.action.RELOCATE_FINISH");
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.uiMessageReceiver, intentFilter);
        this.getContentResolver().registerContentObserver(DownloadContract.Download.CONTENT_URI, true, this.downloadObserver);
        this.downloadIndicatorViewModel.updateIndicator();
    }
    
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (this.pendingUrl != null) {
            this.screenNavigator.showBrowserScreen(this.pendingUrl, new SafeIntent(this.getIntent()).getBooleanExtra("open_new_tab", true), true);
            this.pendingUrl = null;
        }
    }
    
    public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String s) {
        if (this.getResources().getString(2131755332).equals(s)) {
            final boolean turboEnabled = this.isTurboEnabled();
            final BrowserFragment browserFragment = this.getBrowserFragment();
            if (browserFragment != null) {
                browserFragment.setContentBlockingEnabled(turboEnabled);
            }
            this.setMenuButtonSelected(2131296524, turboEnabled);
        }
        else if (this.getResources().getString(2131755316).equals(s)) {
            final boolean blockingImages = this.isBlockingImages();
            final BrowserFragment browserFragment2 = this.getBrowserFragment();
            if (browserFragment2 != null) {
                browserFragment2.setImageBlockingEnabled(blockingImages);
            }
            this.setMenuButtonSelected(2131296505, blockingImages);
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
        NotificationUtil.sendNotification((Context)this, 1000, NotificationUtil.importantBuilder((Context)this).setContentTitle(this.getString(2131755416, new Object[] { "\ud83d\ude4c" })).setContentText(this.getString(2131755415)).setStyle(new NotificationCompat.BigTextStyle().bigText(this.getString(2131755415))).setContentIntent(PendingIntent.getActivity((Context)this, 0, IntentUtils.createInternalOpenUrlIntent((Context)this, this.getSurveyUrl(), true), 1073741824)));
    }
    
    void setMenuButtonSelected(final int n, final boolean selected) {
        if (this.menu == null) {
            return;
        }
        final View viewById = this.menu.findViewById(n);
        if (viewById == null) {
            return;
        }
        viewById.setSelected(selected);
    }
    
    public void showMyShotOnBoarding() {
        this.pendingMyShotOnBoarding = true;
        this.showMenu();
    }
    
    public void showPrivacyPolicyUpdateNotification() {
        DialogUtils.showPrivacyPolicyUpdateNotification((Context)this);
    }
    
    public void showRateAppDialog() {
        DialogUtils.showRateAppDialog((Context)this);
        TelemetryWrapper.showRateApp(false);
    }
    
    public void showRateAppDialogFromIntent() {
        DialogUtils.showRateAppDialog((Context)this);
        TelemetryWrapper.showRateApp(false);
        NotificationManagerCompat.from((Context)this).cancel(1001);
        if (this.getIntent().getExtras() != null) {
            this.getIntent().getExtras().putBoolean("show_rate_dialog", false);
        }
    }
    
    public void showRateAppNotification() {
        DialogUtils.showRateAppNotification((Context)this);
        TelemetryWrapper.showRateApp(true);
    }
    
    public void showShareAppDialog() {
        DialogUtils.showShareAppDialog((Context)this);
        TelemetryWrapper.showPromoteShareDialog();
    }
    
    private static class MainTabViewProvider extends TabViewProvider
    {
        private Activity activity;
        
        MainTabViewProvider(final Activity activity) {
            this.activity = activity;
        }
        
        @Override
        public TabView create() {
            return (TabView)WebViewProvider.create((Context)this.activity, null);
        }
    }
}
