// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.privately;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import mozilla.components.support.base.observer.Observable;
import android.support.v4.app.Fragment;
import kotlin.TypeCastException;
import org.mozilla.rocket.privately.browse.BrowserFragment;
import org.mozilla.focus.urlinput.UrlInputFragment;
import org.mozilla.rocket.privately.home.PrivateHomeFragment;
import kotlin.NotImplementedError;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.focus.activity.MainActivity;
import android.view.Window;
import android.support.v4.app.FragmentActivity;
import android.arch.lifecycle.ViewModelProviders;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import android.content.Intent;
import android.widget.Toast;
import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
import android.content.BroadcastReceiver;
import android.view.View;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.activity.BaseActivity;

public final class PrivateModeActivity extends BaseActivity implements HostActivity, Provider, FragmentListener, SessionHost
{
    private final String LOG_TAG;
    private ScreenNavigator screenNavigator;
    private SessionManager sessionManager;
    private SharedViewModel sharedViewModel;
    private View snackBarContainer;
    private PrivateTabViewProvider tabViewProvider;
    private BroadcastReceiver uiMessageReceiver;
    
    public PrivateModeActivity() {
        this.LOG_TAG = "PrivateModeActivity";
    }
    
    private final void dismissUrlInput() {
        final ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        screenNavigator.popUrlScreen();
        final SharedViewModel sharedViewModel = this.sharedViewModel;
        if (sharedViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
        }
        sharedViewModel.urlInputState().setValue(false);
    }
    
    private final void dropBrowserFragment() {
        this.stopPrivateMode();
        Toast.makeText((Context)this, 2131755359, 1).show();
    }
    
    private final boolean handleIntent(final Intent intent) {
        String action;
        if (intent != null) {
            action = intent.getAction();
        }
        else {
            action = null;
        }
        if (Intrinsics.areEqual(action, "intent_extra_sanitize")) {
            TelemetryWrapper.erasePrivateModeNotification();
            this.stopPrivateMode();
            Toast.makeText((Context)this, 2131755359, 1).show();
            this.finishAndRemoveTask();
            return true;
        }
        return false;
    }
    
    private final void initBroadcastReceivers() {
        this.uiMessageReceiver = (BroadcastReceiver)new PrivateModeActivity$initBroadcastReceivers.PrivateModeActivity$initBroadcastReceivers$1(this);
    }
    
    private final void initViewModel() {
        final SharedViewModel value = ViewModelProviders.of(this).get(SharedViewModel.class);
        Intrinsics.checkExpressionValueIsNotNull(value, "ViewModelProviders.of(th\u2026redViewModel::class.java)");
        this.sharedViewModel = value;
        final SharedViewModel sharedViewModel = this.sharedViewModel;
        if (sharedViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
        }
        sharedViewModel.urlInputState().setValue(false);
    }
    
    private final void makeStatusBarTransparent() {
        final Window window = this.getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window, "window");
        final View decorView = window.getDecorView();
        Intrinsics.checkExpressionValueIsNotNull(decorView, "window.decorView");
        final int systemUiVisibility = decorView.getSystemUiVisibility();
        final Window window2 = this.getWindow();
        Intrinsics.checkExpressionValueIsNotNull(window2, "window");
        final View decorView2 = window2.getDecorView();
        Intrinsics.checkExpressionValueIsNotNull(decorView2, "window.decorView");
        decorView2.setSystemUiVisibility(systemUiVisibility | 0x500);
    }
    
    private final void openUrl(final Object o) {
        String string = null;
        Label_0019: {
            if (o != null) {
                string = o.toString();
                if (string != null) {
                    break Label_0019;
                }
            }
            string = "";
        }
        ViewModelProviders.of(this).get(SharedViewModel.class).setUrl(string);
        this.dismissUrlInput();
        this.startPrivateMode();
        ScreenNavigator.get((Context)this).showBrowserScreen(string, false, false);
    }
    
    private final void pushToBack() {
        final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
        intent.addFlags(16384);
        this.startActivity(intent);
        this.overridePendingTransition(0, 2130771988);
    }
    
    private final void showUrlInput(final Object o) {
        String string = null;
        Label_0019: {
            if (o != null) {
                string = o.toString();
                if (string != null) {
                    break Label_0019;
                }
            }
            string = "";
        }
        final ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        screenNavigator.addUrlScreen(string);
        final SharedViewModel sharedViewModel = this.sharedViewModel;
        if (sharedViewModel == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
        }
        sharedViewModel.urlInputState().setValue(true);
    }
    
    private final void startPrivateMode() {
        PrivateSessionNotificationService.Companion.start((Context)this);
    }
    
    private final void stopPrivateMode() {
        final PrivateSessionNotificationService.Companion companion = PrivateSessionNotificationService.Companion;
        final Context context = (Context)this;
        companion.stop$app_focusWebkitRelease(context);
        final PrivateMode.Companion companion2 = PrivateMode.Companion;
        final Context applicationContext = this.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "this.applicationContext");
        companion2.sanitize(applicationContext);
        TabViewProvider.purify(context);
    }
    
    @Override
    public void applyLocale() {
    }
    
    @Override
    public Screen createFirstRunScreen() {
        final StringBuilder sb = new StringBuilder();
        sb.append("An operation is not implemented: ");
        sb.append("PrivateModeActivity should never show first-run");
        throw new NotImplementedError(sb.toString());
    }
    
    @Override
    public HomeScreen createHomeScreen() {
        return PrivateHomeFragment.Companion.create();
    }
    
    @Override
    public UrlInputScreen createUrlInputScreen(final String s, final String s2) {
        return UrlInputFragment.Companion.create(s, null, false);
    }
    
    @Override
    public BrowserScreen getBrowserScreen() {
        final Fragment fragmentById = this.getSupportFragmentManager().findFragmentById(2131296331);
        if (fragmentById != null) {
            return (BrowserFragment)fragmentById;
        }
        throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.privately.browse.BrowserFragment");
    }
    
    @Override
    public ScreenNavigator getScreenNavigator() {
        final ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        return screenNavigator;
    }
    
    @Override
    public SessionManager getSessionManager() {
        if (this.sessionManager == null) {
            final PrivateTabViewProvider tabViewProvider = this.tabViewProvider;
            if (tabViewProvider == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tabViewProvider");
            }
            this.sessionManager = new SessionManager(tabViewProvider, null, 2, null);
        }
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager == null) {
            Intrinsics.throwNpe();
        }
        return sessionManager;
    }
    
    @Override
    public void onBackPressed() {
        final FragmentManager supportFragmentManager = this.getSupportFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(supportFragmentManager, "supportFragmentManager");
        if (supportFragmentManager.isStateSaved()) {
            return;
        }
        final ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        final ScreenNavigator.BrowserScreen visibleBrowserScreen = screenNavigator.getVisibleBrowserScreen();
        if (visibleBrowserScreen != null && visibleBrowserScreen.onBackPressed()) {
            return;
        }
        final ScreenNavigator screenNavigator2 = this.screenNavigator;
        if (screenNavigator2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        if (!screenNavigator2.canGoBack()) {
            this.finish();
            return;
        }
        super.onBackPressed();
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(null);
        this.tabViewProvider = new PrivateTabViewProvider(this);
        this.screenNavigator = new ScreenNavigator((ScreenNavigator.HostActivity)this);
        if (this.handleIntent(this.getIntent())) {
            this.pushToBack();
            return;
        }
        this.setContentView(2131492895);
        final View viewById = this.findViewById(2131296374);
        Intrinsics.checkExpressionValueIsNotNull(viewById, "findViewById(R.id.container)");
        this.snackBarContainer = viewById;
        this.makeStatusBarTransparent();
        this.initViewModel();
        this.initBroadcastReceivers();
        final ScreenNavigator screenNavigator = this.screenNavigator;
        if (screenNavigator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
        }
        screenNavigator.popToHomeScreen(false);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.stopPrivateMode();
        final SessionManager sessionManager = this.sessionManager;
        if (sessionManager != null) {
            sessionManager.destroy();
        }
    }
    
    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        if (this.handleIntent(intent)) {
            return;
        }
    }
    
    @Override
    public void onNotified(final Fragment fragment, final TYPE type, final Object o) {
        Intrinsics.checkParameterIsNotNull(fragment, "from");
        Intrinsics.checkParameterIsNotNull(type, "type");
        switch (PrivateModeActivity$WhenMappings.$EnumSwitchMapping$0[type.ordinal()]) {
            case 6: {
                this.dropBrowserFragment();
                break;
            }
            case 5: {
                this.openUrl(o);
                break;
            }
            case 4: {
                this.openUrl(o);
                break;
            }
            case 3: {
                this.dismissUrlInput();
                break;
            }
            case 2: {
                this.showUrlInput(o);
                break;
            }
            case 1: {
                this.pushToBack();
                break;
            }
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        final LocalBroadcastManager instance = LocalBroadcastManager.getInstance((Context)this);
        final BroadcastReceiver uiMessageReceiver = this.uiMessageReceiver;
        if (uiMessageReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
        }
        instance.unregisterReceiver(uiMessageReceiver);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addCategory("org.mozilla.category.FILE_OPERATION");
        intentFilter.addAction("org.mozilla.action.RELOCATE_FINISH");
        final LocalBroadcastManager instance = LocalBroadcastManager.getInstance((Context)this);
        final BroadcastReceiver uiMessageReceiver = this.uiMessageReceiver;
        if (uiMessageReceiver == null) {
            Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
        }
        instance.registerReceiver(uiMessageReceiver, intentFilter);
    }
}
