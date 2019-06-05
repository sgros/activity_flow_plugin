package org.mozilla.focus.navigation;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.DefaultLifecycleObserver$_CC;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import org.mozilla.focus.widget.BackKeyHandleable;

public class ScreenNavigator implements DefaultLifecycleObserver {
   private ScreenNavigator.HostActivity activity;
   private FragmentManager.FragmentLifecycleCallbacks lifecycleCallbacks = new FragmentManager.FragmentLifecycleCallbacks() {
      public void onFragmentStarted(FragmentManager var1, Fragment var2) {
         super.onFragmentStarted(var1, var2);
         if (var2 instanceof ScreenNavigator.UrlInputScreen) {
            ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(true);
         }

      }

      public void onFragmentStopped(FragmentManager var1, Fragment var2) {
         super.onFragmentStopped(var1, var2);
         if (var2 instanceof ScreenNavigator.UrlInputScreen) {
            ScreenNavigator.this.transactionHelper.onUrlInputScreenVisible(false);
         }

      }
   };
   private TransactionHelper transactionHelper;

   public ScreenNavigator(ScreenNavigator.HostActivity var1) {
      if (var1 != null) {
         this.activity = var1;
         this.transactionHelper = new TransactionHelper(var1);
         this.activity.getLifecycle().addObserver(this.transactionHelper);
         this.activity.getLifecycle().addObserver(this);
      }
   }

   public static ScreenNavigator get(Context var0) {
      return (ScreenNavigator)(var0 instanceof ScreenNavigator.Provider ? ((ScreenNavigator.Provider)var0).getScreenNavigator() : new ScreenNavigator.NothingNavigated());
   }

   private ScreenNavigator.BrowserScreen getBrowserScreen() {
      return this.activity.getBrowserScreen();
   }

   // $FF: synthetic method
   public static void lambda$showBrowserScreen$0(ScreenNavigator var0) {
      var0.raiseBrowserScreen(true);
   }

   private void log(String var1) {
   }

   private void logMethod(Object... var1) {
   }

   public void addFirstRunScreen() {
      this.logMethod();
      this.transactionHelper.showFirstRun();
   }

   public void addHomeScreen(boolean var1) {
      this.logMethod();
      boolean var2 = this.transactionHelper.popScreensUntil("home_screen", 1);
      StringBuilder var3 = new StringBuilder();
      var3.append("found exist home: ");
      var3.append(var2);
      this.log(var3.toString());
      if (!var2) {
         this.transactionHelper.showHomeScreen(var1, 1);
      }

   }

   public void addUrlScreen(String var1) {
      this.logMethod();
      Fragment var2 = this.getTopFragment();
      String var3 = "browser_screen";
      if (var2 instanceof ScreenNavigator.HomeScreen) {
         var3 = "home_screen";
      } else if (var2 instanceof ScreenNavigator.BrowserScreen) {
         var3 = "browser_screen";
      }

      this.transactionHelper.showUrlInput(var1, var3);
   }

   public boolean canGoBack() {
      boolean var1 = this.transactionHelper.shouldFinish() ^ true;
      StringBuilder var2 = new StringBuilder();
      var2.append("canGoBack: ");
      var2.append(var1);
      this.log(var2.toString());
      return var1;
   }

   public Fragment getTopFragment() {
      Fragment var1 = this.transactionHelper.getLatestCommitFragment();
      Fragment var2 = var1;
      if (var1 == null) {
         var2 = this.getBrowserScreen().getFragment();
      }

      return var2;
   }

   public ScreenNavigator.BrowserScreen getVisibleBrowserScreen() {
      ScreenNavigator.BrowserScreen var1;
      if (this.isBrowserInForeground()) {
         var1 = this.getBrowserScreen();
      } else {
         var1 = null;
      }

      return var1;
   }

   public boolean isBrowserInForeground() {
      boolean var1;
      if (this.activity.getSupportFragmentManager().getBackStackEntryCount() == 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("isBrowserInForeground: ");
      var2.append(var1);
      this.log(var2.toString());
      return var1;
   }

   // $FF: synthetic method
   public void onCreate(LifecycleOwner var1) {
      DefaultLifecycleObserver$_CC.$default$onCreate(this, var1);
   }

   // $FF: synthetic method
   public void onDestroy(LifecycleOwner var1) {
      DefaultLifecycleObserver$_CC.$default$onDestroy(this, var1);
   }

   // $FF: synthetic method
   public void onPause(LifecycleOwner var1) {
      DefaultLifecycleObserver$_CC.$default$onPause(this, var1);
   }

   // $FF: synthetic method
   public void onResume(LifecycleOwner var1) {
      DefaultLifecycleObserver$_CC.$default$onResume(this, var1);
   }

   public void onStart(LifecycleOwner var1) {
      this.activity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.lifecycleCallbacks, false);
   }

   public void onStop(LifecycleOwner var1) {
      this.activity.getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.lifecycleCallbacks);
   }

   public void popToHomeScreen(boolean var1) {
      this.logMethod();
      boolean var2 = this.transactionHelper.popScreensUntil("home_screen", 0);
      StringBuilder var3 = new StringBuilder();
      var3.append("found exist home: ");
      var3.append(var2);
      this.log(var3.toString());
      if (!var2) {
         this.transactionHelper.showHomeScreen(var1, 0);
      }

   }

   public void popUrlScreen() {
      this.logMethod();
      if (this.getTopFragment() instanceof ScreenNavigator.UrlInputScreen) {
         this.transactionHelper.dismissUrlInput();
      }

   }

   public void raiseBrowserScreen(boolean var1) {
      this.logMethod();
      this.transactionHelper.popAllScreens();
   }

   public void restoreBrowserScreen(String var1) {
      this.logMethod();
      this.getBrowserScreen().switchToTab(var1);
      this.raiseBrowserScreen(false);
   }

   public void showBrowserScreen(String var1, boolean var2, boolean var3) {
      this.logMethod(var1, var2);
      this.getBrowserScreen().loadUrl(var1, var2, var3, new _$$Lambda$ScreenNavigator$hVTTivt0CESyQHuzYEBXDzGQFqg(this));
   }

   public interface BrowserScreen extends ScreenNavigator.Screen, BackKeyHandleable {
      void goBackground();

      void goForeground();

      void loadUrl(String var1, boolean var2, boolean var3, Runnable var4);

      void switchToTab(String var1);
   }

   public interface HomeScreen extends ScreenNavigator.Screen {
      void onUrlInputScreenVisible(boolean var1);
   }

   public interface HostActivity extends LifecycleOwner {
      ScreenNavigator.Screen createFirstRunScreen();

      ScreenNavigator.HomeScreen createHomeScreen();

      ScreenNavigator.UrlInputScreen createUrlInputScreen(String var1, String var2);

      ScreenNavigator.BrowserScreen getBrowserScreen();

      FragmentManager getSupportFragmentManager();
   }

   private static class NothingNavigated extends ScreenNavigator {
      NothingNavigated() {
         super((ScreenNavigator.HostActivity)null);
      }

      public void addHomeScreen(boolean var1) {
      }

      public void popToHomeScreen(boolean var1) {
      }

      public void raiseBrowserScreen(boolean var1) {
      }

      public void showBrowserScreen(String var1, boolean var2, boolean var3) {
      }
   }

   public interface Provider {
      ScreenNavigator getScreenNavigator();
   }

   public interface Screen {
      Fragment getFragment();
   }

   public interface UrlInputScreen extends ScreenNavigator.Screen {
   }
}
