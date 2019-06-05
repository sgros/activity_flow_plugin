package org.mozilla.focus.navigation;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.DefaultLifecycleObserver.C0006-CC;
import android.arch.lifecycle.LifecycleOwner;
import android.support.p001v4.app.Fragment;
import android.support.p001v4.app.FragmentManager;
import android.support.p001v4.app.FragmentManager.BackStackEntry;
import android.support.p001v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.p001v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import org.mozilla.focus.C0427R;
import org.mozilla.focus.navigation.ScreenNavigator.BrowserScreen;
import org.mozilla.focus.navigation.ScreenNavigator.HomeScreen;
import org.mozilla.focus.navigation.ScreenNavigator.HostActivity;
import org.mozilla.focus.navigation.ScreenNavigator.Screen;
import org.mozilla.focus.navigation.ScreenNavigator.UrlInputScreen;
import org.mozilla.rocket.C0769R;

class TransactionHelper implements DefaultLifecycleObserver {
    private final HostActivity activity;
    private BackStackListener backStackListener = new BackStackListener(this);

    private static class BackStackListener implements OnBackStackChangedListener {
        private TransactionHelper helper;
        private Runnable stateRunnable;

        /* renamed from: org.mozilla.focus.navigation.TransactionHelper$BackStackListener$1 */
        class C04991 implements AnimationListener {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            C04991() {
            }

            public void onAnimationEnd(Animation animation) {
                BackStackListener.this.executeStateRunnable();
            }
        }

        BackStackListener(TransactionHelper transactionHelper) {
            this.helper = transactionHelper;
        }

        public void onBackStackChanged() {
            if (this.helper != null && (this.helper.activity.getSupportFragmentManager().findFragmentById(C0427R.C0426id.browser) instanceof BrowserScreen)) {
                setBrowserState(shouldKeepBrowserRunning(this.helper), this.helper);
            }
        }

        private void onStop() {
            this.helper = null;
            this.stateRunnable = null;
        }

        private boolean shouldKeepBrowserRunning(TransactionHelper transactionHelper) {
            FragmentManager supportFragmentManager = transactionHelper.activity.getSupportFragmentManager();
            for (int backStackEntryCount = supportFragmentManager.getBackStackEntryCount() - 1; backStackEntryCount >= 0; backStackEntryCount--) {
                if (transactionHelper.getEntryType(supportFragmentManager.getBackStackEntryAt(backStackEntryCount)) != 2) {
                    return false;
                }
            }
            return true;
        }

        private void setBrowserState(boolean z, TransactionHelper transactionHelper) {
            this.stateRunnable = new C0498x94f5e1b2(this, z);
            FragmentAnimationAccessor topAnimationAccessibleFragment = getTopAnimationAccessibleFragment(transactionHelper);
            if (topAnimationAccessibleFragment != null) {
                Animation customEnterTransition = topAnimationAccessibleFragment.getCustomEnterTransition();
                if (!(customEnterTransition == null || customEnterTransition.hasEnded())) {
                    customEnterTransition.setAnimationListener(new C04991());
                    return;
                }
            }
            executeStateRunnable();
        }

        private void setBrowserForegroundState(boolean z) {
            if (this.helper != null) {
                BrowserScreen browserScreen = (BrowserScreen) this.helper.activity.getSupportFragmentManager().findFragmentById(C0427R.C0426id.browser);
                if (z) {
                    browserScreen.goForeground();
                } else {
                    browserScreen.goBackground();
                }
            }
        }

        private void executeStateRunnable() {
            if (this.stateRunnable != null) {
                this.stateRunnable.run();
                this.stateRunnable = null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public FragmentAnimationAccessor getTopAnimationAccessibleFragment(TransactionHelper transactionHelper) {
            Fragment latestCommitFragment = transactionHelper.getLatestCommitFragment();
            return (latestCommitFragment == null || !(latestCommitFragment instanceof FragmentAnimationAccessor)) ? null : (FragmentAnimationAccessor) latestCommitFragment;
        }
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

    TransactionHelper(HostActivity hostActivity) {
        this.activity = hostActivity;
    }

    public void onStart(LifecycleOwner lifecycleOwner) {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        BackStackListener backStackListener = new BackStackListener(this);
        this.backStackListener = backStackListener;
        supportFragmentManager.addOnBackStackChangedListener(backStackListener);
    }

    public void onStop(LifecycleOwner lifecycleOwner) {
        this.activity.getSupportFragmentManager().removeOnBackStackChangedListener(this.backStackListener);
        this.backStackListener.onStop();
    }

    /* Access modifiers changed, original: 0000 */
    public void showHomeScreen(boolean z, int i) {
        if (!isStateSaved()) {
            prepareHomeScreen(z, i).commit();
            this.activity.getSupportFragmentManager().executePendingTransactions();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void showFirstRun() {
        if (!isStateSaved()) {
            prepareFirstRun().commit();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void showUrlInput(String str, String str2) {
        if (!isStateSaved()) {
            Fragment findFragmentByTag = this.activity.getSupportFragmentManager().findFragmentByTag("url_input_sceen");
            if (findFragmentByTag == null || !findFragmentByTag.isAdded() || findFragmentByTag.isRemoving()) {
                prepareUrlInput(str, str2).addToBackStack(makeEntryTag("url_input_sceen", 2)).commit();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dismissUrlInput() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        if (!supportFragmentManager.isStateSaved()) {
            supportFragmentManager.popBackStack();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldFinish() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        boolean z = true;
        if (backStackEntryCount == 0) {
            return true;
        }
        if (getEntryType(supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1)) != 0) {
            z = false;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public void popAllScreens() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        for (int backStackEntryCount = supportFragmentManager.getBackStackEntryCount(); backStackEntryCount > 0; backStackEntryCount--) {
            supportFragmentManager.popBackStack();
        }
        supportFragmentManager.executePendingTransactions();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean popScreensUntil(String str, int i) {
        boolean z = false;
        Object obj = str == null ? 1 : null;
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        for (int backStackEntryCount = supportFragmentManager.getBackStackEntryCount(); backStackEntryCount > 0; backStackEntryCount--) {
            BackStackEntry backStackEntryAt = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 1);
            if (obj == null && TextUtils.equals(str, getEntryTag(backStackEntryAt)) && i == getEntryType(backStackEntryAt)) {
                z = true;
                break;
            }
            supportFragmentManager.popBackStack();
        }
        supportFragmentManager.executePendingTransactions();
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public Fragment getLatestCommitFragment() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            return null;
        }
        return supportFragmentManager.findFragmentByTag(getFragmentTag(backStackEntryCount - 1));
    }

    private FragmentTransaction prepareFirstRun() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        Screen createFirstRunScreen = this.activity.createFirstRunScreen();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        if (supportFragmentManager.findFragmentByTag("first_run") == null) {
            beginTransaction.replace(2131296374, createFirstRunScreen.getFragment(), "first_run").addToBackStack(makeEntryTag("first_run", 0));
        }
        return beginTransaction;
    }

    private FragmentTransaction prepareHomeScreen(boolean z, int i) {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        HomeScreen createHomeScreen = this.activity.createHomeScreen();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        beginTransaction.setCustomAnimations(z ? C0769R.anim.tab_transition_fade_in : 0, 0, 0, i == 0 ? 0 : C0769R.anim.tab_transition_fade_out);
        beginTransaction.add(2131296374, createHomeScreen.getFragment(), "home_screen");
        beginTransaction.addToBackStack(makeEntryTag("home_screen", i));
        return beginTransaction;
    }

    private FragmentTransaction prepareUrlInput(String str, String str2) {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        UrlInputScreen createUrlInputScreen = this.activity.createUrlInputScreen(str, str2);
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        beginTransaction.add(2131296374, createUrlInputScreen.getFragment(), "url_input_sceen");
        return beginTransaction;
    }

    /* Access modifiers changed, original: 0000 */
    public void onUrlInputScreenVisible(boolean z) {
        Screen screen = (Screen) this.activity.getSupportFragmentManager().findFragmentByTag("home_screen");
        if (screen != null && screen.getFragment().isVisible() && (screen instanceof HomeScreen)) {
            ((HomeScreen) screen).onUrlInputScreenVisible(z);
        }
    }

    private String getFragmentTag(int i) {
        return getEntryTag(this.activity.getSupportFragmentManager().getBackStackEntryAt(i));
    }

    private String getEntryTag(BackStackEntry backStackEntry) {
        return backStackEntry.getName().split("#")[0];
    }

    private int getEntryType(BackStackEntry backStackEntry) {
        return Integer.parseInt(backStackEntry.getName().split("#")[1]);
    }

    private String makeEntryTag(String str, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("#");
        stringBuilder.append(i);
        return stringBuilder.toString();
    }

    private boolean isStateSaved() {
        FragmentManager supportFragmentManager = this.activity.getSupportFragmentManager();
        return supportFragmentManager == null || supportFragmentManager.isStateSaved();
    }
}
