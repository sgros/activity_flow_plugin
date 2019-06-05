package org.mozilla.focus.navigation;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.DefaultLifecycleObserver.C0006-CC;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentManager.FragmentLifecycleCallbacks;
import org.mozilla.focus.widget.BackKeyHandleable;

public class ScreenNavigator implements DefaultLifecycleObserver {
    private HostActivity activity;
    private FragmentLifecycleCallbacks lifecycleCallbacks = new C07171();
    private TransactionHelper transactionHelper;

    public interface Provider {
        ScreenNavigator getScreenNavigator();
    }

    public interface Screen {
        Fragment getFragment();
    }

    /* renamed from: org.mozilla.focus.navigation.ScreenNavigator$1 */
    class C07171 extends FragmentLifecycleCallbacks {
        C07171() {
        }

        public void onFragmentStarted(FragmentManager fragmentManager, Fragment fragment) {
            super.onFragmentStarted(fragmentManager, fragment);
            if (fragment instanceof UrlInputScreen) {
                ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(true);
            }
        }

        public void onFragmentStopped(FragmentManager fragmentManager, Fragment fragment) {
            super.onFragmentStopped(fragmentManager, fragment);
            if (fragment instanceof UrlInputScreen) {
                ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(false);
            }
        }
    }

    public interface BrowserScreen extends Screen, BackKeyHandleable {
        void goBackground();

        void goForeground();

        void loadUrl(String str, boolean z, boolean z2, Runnable runnable);

        void switchToTab(String str);
    }

    public interface HomeScreen extends Screen {
        void onUrlInputScreenVisible(boolean z);
    }

    public interface HostActivity extends LifecycleOwner {
        Screen createFirstRunScreen();

        HomeScreen createHomeScreen();

        UrlInputScreen createUrlInputScreen(String str, String str2);

        BrowserScreen getBrowserScreen();

        FragmentManager getSupportFragmentManager();
    }

    public interface UrlInputScreen extends Screen {
    }

    private static class NothingNavigated extends ScreenNavigator {
        public void addHomeScreen(boolean z) {
        }

        public void popToHomeScreen(boolean z) {
        }

        public void raiseBrowserScreen(boolean z) {
        }

        public void showBrowserScreen(String str, boolean z, boolean z2) {
        }

        NothingNavigated() {
            super(null);
        }
    }

    private void log(String str) {
    }

    private void logMethod(Object... objArr) {
    }

    public /* synthetic */ void onCreate(LifecycleOwner lifecycleOwner) {
        C0006-CC.$default$onCreate(this, lifecycleOwner);
    }

    public /* synthetic */ void onDestroy(LifecycleOwner lifecycleOwner) {
        C0006-CC.$default$onDestroy(this, lifecycleOwner);
    }

    public /* synthetic */ void onPause(LifecycleOwner lifecycleOwner) {
        C0006-CC.$default$onPause(this, lifecycleOwner);
    }

    public /* synthetic */ void onResume(LifecycleOwner lifecycleOwner) {
        C0006-CC.$default$onResume(this, lifecycleOwner);
    }

    public static ScreenNavigator get(Context context) {
        if (context instanceof Provider) {
            return ((Provider) context).getScreenNavigator();
        }
        return new NothingNavigated();
    }

    public ScreenNavigator(HostActivity hostActivity) {
        if (hostActivity != null) {
            this.activity = hostActivity;
            this.transactionHelper = new TransactionHelper(hostActivity);
            this.activity.getLifecycle().addObserver(this.transactionHelper);
            this.activity.getLifecycle().addObserver(this);
        }
    }

    public void onStart(LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.lifecycleCallbacks, false);
    }

    public void onStop(LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.lifecycleCallbacks);
    }

    public void raiseBrowserScreen(boolean z) {
        logMethod(new Object[0]);
        this.transactionHelper.popAllScreens();
    }

    public void showBrowserScreen(String str, boolean z, boolean z2) {
        logMethod(str, Boolean.valueOf(z));
        getBrowserScreen().loadUrl(str, z, z2, new C0497-$$Lambda$ScreenNavigator$hVTTivt0CESyQHuzYEBXDzGQFqg(this));
    }

    public void restoreBrowserScreen(String str) {
        logMethod(new Object[0]);
        getBrowserScreen().switchToTab(str);
        raiseBrowserScreen(false);
    }

    public boolean isBrowserInForeground() {
        boolean z = this.activity.getSupportFragmentManager().getBackStackEntryCount() == 0;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("isBrowserInForeground: ");
        stringBuilder.append(z);
        log(stringBuilder.toString());
        return z;
    }

    public void addHomeScreen(boolean z) {
        logMethod(new Object[0]);
        boolean popScreensUntil = this.transactionHelper.popScreensUntil("home_screen", 1);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("found exist home: ");
        stringBuilder.append(popScreensUntil);
        log(stringBuilder.toString());
        if (!popScreensUntil) {
            this.transactionHelper.showHomeScreen(z, 1);
        }
    }

    public void popToHomeScreen(boolean z) {
        logMethod(new Object[0]);
        boolean popScreensUntil = this.transactionHelper.popScreensUntil("home_screen", 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("found exist home: ");
        stringBuilder.append(popScreensUntil);
        log(stringBuilder.toString());
        if (!popScreensUntil) {
            this.transactionHelper.showHomeScreen(z, 0);
        }
    }

    public void addFirstRunScreen() {
        logMethod(new Object[0]);
        this.transactionHelper.showFirstRun();
    }

    public void addUrlScreen(String str) {
        logMethod(new Object[0]);
        Fragment topFragment = getTopFragment();
        String str2 = "browser_screen";
        if (topFragment instanceof HomeScreen) {
            str2 = "home_screen";
        } else if (topFragment instanceof BrowserScreen) {
            str2 = "browser_screen";
        }
        this.transactionHelper.showUrlInput(str, str2);
    }

    public void popUrlScreen() {
        logMethod(new Object[0]);
        if (getTopFragment() instanceof UrlInputScreen) {
            this.transactionHelper.dismissUrlInput();
        }
    }

    public Fragment getTopFragment() {
        Fragment latestCommitFragment = this.transactionHelper.getLatestCommitFragment();
        return latestCommitFragment == null ? getBrowserScreen().getFragment() : latestCommitFragment;
    }

    public BrowserScreen getVisibleBrowserScreen() {
        return isBrowserInForeground() ? getBrowserScreen() : null;
    }

    private BrowserScreen getBrowserScreen() {
        return this.activity.getBrowserScreen();
    }

    public boolean canGoBack() {
        int shouldFinish = this.transactionHelper.shouldFinish() ^ 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("canGoBack: ");
        stringBuilder.append(shouldFinish);
        log(stringBuilder.toString());
        return shouldFinish;
    }
}
