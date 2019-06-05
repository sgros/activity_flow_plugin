package org.mozilla.rocket.privately;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentActivity;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import kotlin.NotImplementedError;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.navigation.ScreenNavigator.BrowserScreen;
import org.mozilla.focus.navigation.ScreenNavigator.HomeScreen;
import org.mozilla.focus.navigation.ScreenNavigator.HostActivity;
import org.mozilla.focus.navigation.ScreenNavigator.Provider;
import org.mozilla.focus.navigation.ScreenNavigator.Screen;
import org.mozilla.focus.navigation.ScreenNavigator.UrlInputScreen;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.urlinput.UrlInputFragment;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.widget.FragmentListener.TYPE;
import org.mozilla.rocket.C0769R;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.privately.PrivateMode.Companion;
import org.mozilla.rocket.privately.browse.BrowserFragment;
import org.mozilla.rocket.privately.home.PrivateHomeFragment;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.rocket.tabs.TabsSessionProvider.SessionHost;

/* compiled from: PrivateModeActivity.kt */
public final class PrivateModeActivity extends BaseActivity implements HostActivity, Provider, FragmentListener, SessionHost {
    private final String LOG_TAG = "PrivateModeActivity";
    private ScreenNavigator screenNavigator;
    private SessionManager sessionManager;
    private SharedViewModel sharedViewModel;
    private View snackBarContainer;
    private PrivateTabViewProvider tabViewProvider;
    private BroadcastReceiver uiMessageReceiver;

    public void applyLocale() {
    }

    public static final /* synthetic */ View access$getSnackBarContainer$p(PrivateModeActivity privateModeActivity) {
        View view = privateModeActivity.snackBarContainer;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("snackBarContainer");
        }
        return view;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        this.tabViewProvider = new PrivateTabViewProvider(this);
        this.screenNavigator = new ScreenNavigator(this);
        if (handleIntent(getIntent())) {
            pushToBack();
            return;
        }
        setContentView((int) C0769R.layout.activity_private_mode);
        View findViewById = findViewById(2131296374);
        Intrinsics.checkExpressionValueIsNotNull(findViewById, "findViewById(R.id.container)");
        this.snackBarContainer = findViewById;
        makeStatusBarTransparent();
        initViewModel();
        initBroadcastReceivers();
        ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        screenNavigator.popToHomeScreen(false);
    }

    private final void initViewModel() {
        ViewModel viewModel = ViewModelProviders.m2of((FragmentActivity) this).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(viewModel, "ViewModelProviders.of(th…redViewModel::class.java)");
        this.sharedViewModel = (SharedViewModel) viewModel;
        SharedViewModel sharedViewModel = this.sharedViewModel;
        if (sharedViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
        }
        sharedViewModel.urlInputState().setValue(Boolean.valueOf(false));
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory("org.mozilla.category.FILE_OPERATION");
        intentFilter.addAction("org.mozilla.action.RELOCATE_FINISH");
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver broadcastReceiver = this.uiMessageReceiver;
        if (broadcastReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
        }
        instance.registerReceiver(broadcastReceiver, intentFilter);
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        BroadcastReceiver broadcastReceiver = this.uiMessageReceiver;
        if (broadcastReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
        }
        instance.unregisterReceiver(broadcastReceiver);
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        super.onDestroy();
        stopPrivateMode();
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }

    public void onNotified(Fragment fragment, TYPE type, Object obj) {
        Intrinsics.checkParameterIsNotNull(fragment, "from");
        Intrinsics.checkParameterIsNotNull(type, "type");
        switch (type) {
            case TOGGLE_PRIVATE_MODE:
                pushToBack();
                return;
            case SHOW_URL_INPUT:
                showUrlInput(obj);
                return;
            case DISMISS_URL_INPUT:
                dismissUrlInput();
                return;
            case OPEN_URL_IN_CURRENT_TAB:
                openUrl(obj);
                return;
            case OPEN_URL_IN_NEW_TAB:
                openUrl(obj);
                return;
            case DROP_BROWSING_PAGES:
                dropBrowserFragment();
                return;
            default:
                return;
        }
    }

    public void onBackPressed() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(supportFragmentManager, "supportFragmentManager");
        if (!supportFragmentManager.isStateSaved()) {
            ScreenNavigator screenNavigator = this.screenNavigator;
            if (screenNavigator == null) {
                Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
            }
            BrowserScreen visibleBrowserScreen = screenNavigator.getVisibleBrowserScreen();
            if (!(visibleBrowserScreen != null ? visibleBrowserScreen.onBackPressed() : false)) {
                screenNavigator = this.screenNavigator;
                if (screenNavigator == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
                }
                if (screenNavigator.canGoBack()) {
                    super.onBackPressed();
                } else {
                    finish();
                }
            }
        }
    }

    public SessionManager getSessionManager() {
        if (this.sessionManager == null) {
            PrivateTabViewProvider privateTabViewProvider = this.tabViewProvider;
            if (privateTabViewProvider == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tabViewProvider");
            }
            this.sessionManager = new SessionManager(privateTabViewProvider, null, 2, null);
        }
        SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwNpe();
        }
        return sessionManager;
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!handleIntent(intent)) {
        }
    }

    private final void dropBrowserFragment() {
        stopPrivateMode();
        Toast.makeText(this, C0769R.string.private_browsing_erase_done, 1).show();
    }

    public ScreenNavigator getScreenNavigator() {
        ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        return screenNavigator;
    }

    public BrowserScreen getBrowserScreen() {
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(C0427R.C0426id.browser);
        if (findFragmentById != null) {
            return (BrowserFragment) findFragmentById;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.privately.browse.BrowserFragment");
    }

    public Screen createFirstRunScreen() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("An operation is not implemented: ");
        stringBuilder.append("PrivateModeActivity should never show first-run");
        throw new NotImplementedError(stringBuilder.toString());
    }

    public HomeScreen createHomeScreen() {
        return PrivateHomeFragment.Companion.create();
    }

    public UrlInputScreen createUrlInputScreen(String str, String str2) {
        return UrlInputFragment.Companion.create(str, null, false);
    }

    private final void pushToBack() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(16384);
        startActivity(intent);
        overridePendingTransition(0, C0769R.anim.pb_exit);
    }

    /* JADX WARNING: Missing block: B:2:0x0006, code skipped:
            if (r3 != null) goto L_0x000b;
     */
    private final void showUrlInput(java.lang.Object r3) {
        /*
        r2 = this;
        if (r3 == 0) goto L_0x0009;
    L_0x0002:
        r3 = r3.toString();
        if (r3 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x000b;
    L_0x0009:
        r3 = "";
    L_0x000b:
        r0 = r2.screenNavigator;
        if (r0 != 0) goto L_0x0014;
    L_0x000f:
        r1 = "screenNavigator";
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1);
    L_0x0014:
        r0.addUrlScreen(r3);
        r3 = r2.sharedViewModel;
        if (r3 != 0) goto L_0x0020;
    L_0x001b:
        r0 = "sharedViewModel";
        kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r0);
    L_0x0020:
        r3 = r3.urlInputState();
        r0 = 1;
        r0 = java.lang.Boolean.valueOf(r0);
        r3.setValue(r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.privately.PrivateModeActivity.showUrlInput(java.lang.Object):void");
    }

    private final void dismissUrlInput() {
        ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        screenNavigator.popUrlScreen();
        SharedViewModel sharedViewModel = this.sharedViewModel;
        if (sharedViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
        }
        sharedViewModel.urlInputState().setValue(Boolean.valueOf(false));
    }

    /* JADX WARNING: Missing block: B:2:0x0006, code skipped:
            if (r3 != null) goto L_0x000b;
     */
    private final void openUrl(java.lang.Object r3) {
        /*
        r2 = this;
        if (r3 == 0) goto L_0x0009;
    L_0x0002:
        r3 = r3.toString();
        if (r3 == 0) goto L_0x0009;
    L_0x0008:
        goto L_0x000b;
    L_0x0009:
        r3 = "";
    L_0x000b:
        r0 = r2;
        r0 = (android.support.p001v4.app.FragmentActivity) r0;
        r0 = android.arch.lifecycle.ViewModelProviders.m2of(r0);
        r1 = org.mozilla.rocket.privately.SharedViewModel.class;
        r0 = r0.get(r1);
        r0 = (org.mozilla.rocket.privately.SharedViewModel) r0;
        r0.setUrl(r3);
        r2.dismissUrlInput();
        r2.startPrivateMode();
        r0 = r2;
        r0 = (android.content.Context) r0;
        r0 = org.mozilla.focus.navigation.ScreenNavigator.get(r0);
        r1 = 0;
        r0.showBrowserScreen(r3, r1, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mozilla.rocket.privately.PrivateModeActivity.openUrl(java.lang.Object):void");
    }

    private final void makeStatusBarTransparent() {
        Window window = getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "window");
        View decorView = window.getDecorView();
        Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
        int systemUiVisibility = decorView.getSystemUiVisibility() | 1280;
        Window window2 = getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window2, "window");
        View decorView2 = window2.getDecorView();
        Intrinsics.checkExpressionValueIsNotNull(decorView2, "window.decorView");
        decorView2.setSystemUiVisibility(systemUiVisibility);
    }

    private final void startPrivateMode() {
        PrivateSessionNotificationService.Companion.start(this);
    }

    private final void stopPrivateMode() {
        Context context = this;
        PrivateSessionNotificationService.Companion.stop$app_focusWebkitRelease(context);
        Companion companion = PrivateMode.Companion;
        Context applicationContext = getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
        companion.sanitize(applicationContext);
        TabViewProvider.purify(context);
    }

    private final boolean handleIntent(Intent intent) {
        if (!Intrinsics.areEqual(intent != null ? intent.getAction() : null, "intent_extra_sanitize")) {
            return false;
        }
        TelemetryWrapper.erasePrivateModeNotification();
        stopPrivateMode();
        Toast.makeText(this, C0769R.string.private_browsing_erase_done, 1).show();
        finishAndRemoveTask();
        return true;
    }

    private final void initBroadcastReceivers() {
        this.uiMessageReceiver = new PrivateModeActivity$initBroadcastReceivers$1(this);
    }
}
