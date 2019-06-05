// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.navigation;

import org.mozilla.focus.widget.BackKeyHandleable;
import android.arch.lifecycle.DefaultLifecycleObserver$_CC;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.arch.lifecycle.LifecycleObserver;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.DefaultLifecycleObserver;

public class ScreenNavigator implements DefaultLifecycleObserver
{
    private HostActivity activity;
    private FragmentManager.FragmentLifecycleCallbacks lifecycleCallbacks;
    private TransactionHelper transactionHelper;
    
    public ScreenNavigator(final HostActivity activity) {
        this.lifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentStarted(final FragmentManager fragmentManager, final Fragment fragment) {
                super.onFragmentStarted(fragmentManager, fragment);
                if (fragment instanceof UrlInputScreen) {
                    ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(true);
                }
            }
            
            @Override
            public void onFragmentStopped(final FragmentManager fragmentManager, final Fragment fragment) {
                super.onFragmentStopped(fragmentManager, fragment);
                if (fragment instanceof UrlInputScreen) {
                    ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(false);
                }
            }
        };
        if (activity == null) {
            return;
        }
        this.activity = activity;
        this.transactionHelper = new TransactionHelper(activity);
        this.activity.getLifecycle().addObserver(this.transactionHelper);
        this.activity.getLifecycle().addObserver(this);
    }
    
    public static ScreenNavigator get(final Context context) {
        if (context instanceof Provider) {
            return ((Provider)context).getScreenNavigator();
        }
        return new NothingNavigated();
    }
    
    private BrowserScreen getBrowserScreen() {
        return this.activity.getBrowserScreen();
    }
    
    private void log(final String s) {
    }
    
    private void logMethod(final Object... array) {
    }
    
    public void addFirstRunScreen() {
        this.logMethod(new Object[0]);
        this.transactionHelper.showFirstRun();
    }
    
    public void addHomeScreen(final boolean b) {
        this.logMethod(new Object[0]);
        final boolean popScreensUntil = this.transactionHelper.popScreensUntil("home_screen", 1);
        final StringBuilder sb = new StringBuilder();
        sb.append("found exist home: ");
        sb.append(popScreensUntil);
        this.log(sb.toString());
        if (!popScreensUntil) {
            this.transactionHelper.showHomeScreen(b, 1);
        }
    }
    
    public void addUrlScreen(final String s) {
        this.logMethod(new Object[0]);
        final Fragment topFragment = this.getTopFragment();
        String s2 = "browser_screen";
        if (topFragment instanceof HomeScreen) {
            s2 = "home_screen";
        }
        else if (topFragment instanceof BrowserScreen) {
            s2 = "browser_screen";
        }
        this.transactionHelper.showUrlInput(s, s2);
    }
    
    public boolean canGoBack() {
        final boolean b = this.transactionHelper.shouldFinish() ^ true;
        final StringBuilder sb = new StringBuilder();
        sb.append("canGoBack: ");
        sb.append(b);
        this.log(sb.toString());
        return b;
    }
    
    public Fragment getTopFragment() {
        Fragment fragment;
        if ((fragment = this.transactionHelper.getLatestCommitFragment()) == null) {
            fragment = ((Screen)this.getBrowserScreen()).getFragment();
        }
        return fragment;
    }
    
    public BrowserScreen getVisibleBrowserScreen() {
        BrowserScreen browserScreen;
        if (this.isBrowserInForeground()) {
            browserScreen = this.getBrowserScreen();
        }
        else {
            browserScreen = null;
        }
        return browserScreen;
    }
    
    public boolean isBrowserInForeground() {
        final boolean b = this.activity.getSupportFragmentManager().getBackStackEntryCount() == 0;
        final StringBuilder sb = new StringBuilder();
        sb.append("isBrowserInForeground: ");
        sb.append(b);
        this.log(sb.toString());
        return b;
    }
    
    @Override
    public void onStart(final LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.lifecycleCallbacks, false);
    }
    
    @Override
    public void onStop(final LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.lifecycleCallbacks);
    }
    
    public void popToHomeScreen(final boolean b) {
        this.logMethod(new Object[0]);
        final boolean popScreensUntil = this.transactionHelper.popScreensUntil("home_screen", 0);
        final StringBuilder sb = new StringBuilder();
        sb.append("found exist home: ");
        sb.append(popScreensUntil);
        this.log(sb.toString());
        if (!popScreensUntil) {
            this.transactionHelper.showHomeScreen(b, 0);
        }
    }
    
    public void popUrlScreen() {
        this.logMethod(new Object[0]);
        if (this.getTopFragment() instanceof UrlInputScreen) {
            this.transactionHelper.dismissUrlInput();
        }
    }
    
    public void raiseBrowserScreen(final boolean b) {
        this.logMethod(new Object[0]);
        this.transactionHelper.popAllScreens();
    }
    
    public void restoreBrowserScreen(final String s) {
        this.logMethod(new Object[0]);
        this.getBrowserScreen().switchToTab(s);
        this.raiseBrowserScreen(false);
    }
    
    public void showBrowserScreen(final String s, final boolean b, final boolean b2) {
        this.logMethod(s, b);
        this.getBrowserScreen().loadUrl(s, b, b2, new _$$Lambda$ScreenNavigator$hVTTivt0CESyQHuzYEBXDzGQFqg(this));
    }
    
    public interface BrowserScreen extends Screen, BackKeyHandleable
    {
        void goBackground();
        
        void goForeground();
        
        void loadUrl(final String p0, final boolean p1, final boolean p2, final Runnable p3);
        
        void switchToTab(final String p0);
    }
    
    public interface HomeScreen extends Screen
    {
        void onUrlInputScreenVisible(final boolean p0);
    }
    
    public interface HostActivity extends LifecycleOwner
    {
        Screen createFirstRunScreen();
        
        HomeScreen createHomeScreen();
        
        UrlInputScreen createUrlInputScreen(final String p0, final String p1);
        
        BrowserScreen getBrowserScreen();
        
        FragmentManager getSupportFragmentManager();
    }
    
    private static class NothingNavigated extends ScreenNavigator
    {
        NothingNavigated() {
            super(null);
        }
        
        @Override
        public void addHomeScreen(final boolean b) {
        }
        
        @Override
        public void popToHomeScreen(final boolean b) {
        }
        
        @Override
        public void raiseBrowserScreen(final boolean b) {
        }
        
        @Override
        public void showBrowserScreen(final String s, final boolean b, final boolean b2) {
        }
    }
    
    public interface Provider
    {
        ScreenNavigator getScreenNavigator();
    }
    
    public interface Screen
    {
        Fragment getFragment();
    }
    
    public interface UrlInputScreen extends Screen
    {
    }
}
