// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.navigation;

import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.text.TextUtils;
import android.arch.lifecycle.DefaultLifecycleObserver$_CC;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.arch.lifecycle.DefaultLifecycleObserver;

class TransactionHelper implements DefaultLifecycleObserver
{
    private final ScreenNavigator.HostActivity activity;
    private BackStackListener backStackListener;
    
    TransactionHelper(final ScreenNavigator.HostActivity activity) {
        this.activity = activity;
        this.backStackListener = new BackStackListener(this);
    }
    
    private String getEntryTag(final FragmentManager.BackStackEntry backStackEntry) {
        return backStackEntry.getName().split("#")[0];
    }
    
    private int getEntryType(final FragmentManager.BackStackEntry backStackEntry) {
        return Integer.parseInt(backStackEntry.getName().split("#")[1]);
    }
    
    private String getFragmentTag(final int n) {
        return this.getEntryTag(this.activity.getSupportFragmentManager().getBackStackEntryAt(n));
    }
    
    private boolean isStateSaved() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        return supportFragmentManager == null || supportFragmentManager.isStateSaved();
    }
    
    private String makeEntryTag(final String str, final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("#");
        sb.append(i);
        return sb.toString();
    }
    
    private FragmentTransaction prepareFirstRun() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        final ScreenNavigator.Screen firstRunScreen = this.activity.createFirstRunScreen();
        final FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        if (supportFragmentManager.findFragmentByTag("first_run") == null) {
            beginTransaction.replace(2131296374, firstRunScreen.getFragment(), "first_run").addToBackStack(this.makeEntryTag("first_run", 0));
        }
        return beginTransaction;
    }
    
    private FragmentTransaction prepareHomeScreen(final boolean b, final int n) {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        final ScreenNavigator.HomeScreen homeScreen = this.activity.createHomeScreen();
        final FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        int n2;
        if (b) {
            n2 = 2130771989;
        }
        else {
            n2 = 0;
        }
        int n3;
        if (n == 0) {
            n3 = 0;
        }
        else {
            n3 = 2130771990;
        }
        beginTransaction.setCustomAnimations(n2, 0, 0, n3);
        beginTransaction.add(2131296374, ((ScreenNavigator.Screen)homeScreen).getFragment(), "home_screen");
        beginTransaction.addToBackStack(this.makeEntryTag("home_screen", n));
        return beginTransaction;
    }
    
    private FragmentTransaction prepareUrlInput(final String s, final String s2) {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        final ScreenNavigator.UrlInputScreen urlInputScreen = this.activity.createUrlInputScreen(s, s2);
        final FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        beginTransaction.add(2131296374, ((ScreenNavigator.Screen)urlInputScreen).getFragment(), "url_input_sceen");
        return beginTransaction;
    }
    
    void dismissUrlInput() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        if (supportFragmentManager.isStateSaved()) {
            return;
        }
        supportFragmentManager.popBackStack();
    }
    
    Fragment getLatestCommitFragment() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        final int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            return null;
        }
        return supportFragmentManager.findFragmentByTag(this.getFragmentTag(backStackEntryCount - 1));
    }
    
    @Override
    public void onStart(final LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().addOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)(this.backStackListener = new BackStackListener(this)));
    }
    
    @Override
    public void onStop(final LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().removeOnBackStackChangedListener((FragmentManager.OnBackStackChangedListener)this.backStackListener);
        this.backStackListener.onStop();
    }
    
    void onUrlInputScreenVisible(final boolean b) {
        final ScreenNavigator.Screen screen = (ScreenNavigator.Screen)this.activity.getSupportFragmentManager().findFragmentByTag("home_screen");
        if (screen != null && screen.getFragment().isVisible() && screen instanceof ScreenNavigator.HomeScreen) {
            ((ScreenNavigator.HomeScreen)screen).onUrlInputScreenVisible(b);
        }
    }
    
    void popAllScreens() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        for (int i = supportFragmentManager.getBackStackEntryCount(); i > 0; --i) {
            supportFragmentManager.popBackStack();
        }
        supportFragmentManager.executePendingTransactions();
    }
    
    boolean popScreensUntil(final String s, final int n) {
        final boolean b = false;
        final boolean b2 = s == null;
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        boolean b3;
        while (true) {
            b3 = b;
            if (backStackEntryCount <= 0) {
                break;
            }
            final FragmentManager.BackStackEntry backStackEntry = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            if (!b2 && TextUtils.equals((CharSequence)s, (CharSequence)this.getEntryTag(backStackEntry)) && n == this.getEntryType(backStackEntry)) {
                b3 = true;
                break;
            }
            supportFragmentManager.popBackStack();
            --backStackEntryCount;
        }
        supportFragmentManager.executePendingTransactions();
        return b3;
    }
    
    boolean shouldFinish() {
        final FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        final int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        boolean b = true;
        if (backStackEntryCount == 0) {
            return true;
        }
        if (this.getEntryType(supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1)) != 0) {
            b = false;
        }
        return b;
    }
    
    void showFirstRun() {
        if (this.isStateSaved()) {
            return;
        }
        this.prepareFirstRun().commit();
    }
    
    void showHomeScreen(final boolean b, final int n) {
        if (this.isStateSaved()) {
            return;
        }
        this.prepareHomeScreen(b, n).commit();
        this.activity.getSupportFragmentManager().executePendingTransactions();
    }
    
    void showUrlInput(final String s, final String s2) {
        if (this.isStateSaved()) {
            return;
        }
        final Fragment fragmentByTag = this.activity.getSupportFragmentManager().findFragmentByTag("url_input_sceen");
        if (fragmentByTag != null && fragmentByTag.isAdded() && !fragmentByTag.isRemoving()) {
            return;
        }
        this.prepareUrlInput(s, s2).addToBackStack(this.makeEntryTag("url_input_sceen", 2)).commit();
    }
    
    private static class BackStackListener implements OnBackStackChangedListener
    {
        private TransactionHelper helper;
        private Runnable stateRunnable;
        
        BackStackListener(final TransactionHelper helper) {
            this.helper = helper;
        }
        
        private void executeStateRunnable() {
            if (this.stateRunnable != null) {
                this.stateRunnable.run();
                this.stateRunnable = null;
            }
        }
        
        private void onStop() {
            this.helper = null;
            this.stateRunnable = null;
        }
        
        private void setBrowserForegroundState(final boolean b) {
            if (this.helper == null) {
                return;
            }
            final ScreenNavigator.BrowserScreen browserScreen = (ScreenNavigator.BrowserScreen)this.helper.activity.getSupportFragmentManager().findFragmentById(2131296331);
            if (b) {
                browserScreen.goForeground();
            }
            else {
                browserScreen.goBackground();
            }
        }
        
        private void setBrowserState(final boolean b, final TransactionHelper transactionHelper) {
            this.stateRunnable = new _$$Lambda$TransactionHelper$BackStackListener$KqWYXvDV_PCHcqgGsMvvJBFNPvE(this, b);
            final FragmentAnimationAccessor topAnimationAccessibleFragment = this.getTopAnimationAccessibleFragment(transactionHelper);
            if (topAnimationAccessibleFragment != null) {
                final Animation customEnterTransition = topAnimationAccessibleFragment.getCustomEnterTransition();
                if (customEnterTransition != null) {
                    if (!customEnterTransition.hasEnded()) {
                        customEnterTransition.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
                            public void onAnimationEnd(final Animation animation) {
                                BackStackListener.this.executeStateRunnable();
                            }
                            
                            public void onAnimationRepeat(final Animation animation) {
                            }
                            
                            public void onAnimationStart(final Animation animation) {
                            }
                        });
                        return;
                    }
                }
            }
            this.executeStateRunnable();
        }
        
        private boolean shouldKeepBrowserRunning(final TransactionHelper transactionHelper) {
            final FragmentManager supportFragmentManager = transactionHelper.activity.getSupportFragmentManager();
            for (int i = supportFragmentManager.getBackStackEntryCount() - 1; i >= 0; --i) {
                if (transactionHelper.getEntryType(supportFragmentManager.getBackStackEntryAt(i)) != 2) {
                    return false;
                }
            }
            return true;
        }
        
        FragmentAnimationAccessor getTopAnimationAccessibleFragment(final TransactionHelper transactionHelper) {
            final Fragment latestCommitFragment = transactionHelper.getLatestCommitFragment();
            if (latestCommitFragment != null && latestCommitFragment instanceof FragmentAnimationAccessor) {
                return (FragmentAnimationAccessor)latestCommitFragment;
            }
            return null;
        }
        
        @Override
        public void onBackStackChanged() {
            if (this.helper == null) {
                return;
            }
            if (this.helper.activity.getSupportFragmentManager().findFragmentById(2131296331) instanceof ScreenNavigator.BrowserScreen) {
                this.setBrowserState(this.shouldKeepBrowserRunning(this.helper), this.helper);
            }
        }
    }
}
