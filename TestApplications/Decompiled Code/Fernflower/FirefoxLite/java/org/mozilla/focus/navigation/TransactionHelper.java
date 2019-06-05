package org.mozilla.focus.navigation;

import android.arch.lifecycle.DefaultLifecycleObserver;
import android.arch.lifecycle.DefaultLifecycleObserver$_CC;
import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

class TransactionHelper implements DefaultLifecycleObserver {
   private final ScreenNavigator.HostActivity activity;
   private TransactionHelper.BackStackListener backStackListener;

   TransactionHelper(ScreenNavigator.HostActivity var1) {
      this.activity = var1;
      this.backStackListener = new TransactionHelper.BackStackListener(this);
   }

   private String getEntryTag(FragmentManager.BackStackEntry var1) {
      return var1.getName().split("#")[0];
   }

   private int getEntryType(FragmentManager.BackStackEntry var1) {
      return Integer.parseInt(var1.getName().split("#")[1]);
   }

   private String getFragmentTag(int var1) {
      return this.getEntryTag(this.activity.getSupportFragmentManager().getBackStackEntryAt(var1));
   }

   private boolean isStateSaved() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();
      boolean var2;
      if (var1 != null && !var1.isStateSaved()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private String makeEntryTag(String var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var1);
      var3.append("#");
      var3.append(var2);
      return var3.toString();
   }

   private FragmentTransaction prepareFirstRun() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();
      ScreenNavigator.Screen var2 = this.activity.createFirstRunScreen();
      FragmentTransaction var3 = var1.beginTransaction();
      if (var1.findFragmentByTag("first_run") == null) {
         var3.replace(2131296374, var2.getFragment(), "first_run").addToBackStack(this.makeEntryTag("first_run", 0));
      }

      return var3;
   }

   private FragmentTransaction prepareHomeScreen(boolean var1, int var2) {
      FragmentManager var3 = this.activity.getSupportFragmentManager();
      ScreenNavigator.HomeScreen var4 = this.activity.createHomeScreen();
      FragmentTransaction var7 = var3.beginTransaction();
      int var5;
      if (var1) {
         var5 = 2130771989;
      } else {
         var5 = 0;
      }

      int var6;
      if (var2 == 0) {
         var6 = 0;
      } else {
         var6 = 2130771990;
      }

      var7.setCustomAnimations(var5, 0, 0, var6);
      var7.add(2131296374, var4.getFragment(), "home_screen");
      var7.addToBackStack(this.makeEntryTag("home_screen", var2));
      return var7;
   }

   private FragmentTransaction prepareUrlInput(String var1, String var2) {
      FragmentManager var3 = this.activity.getSupportFragmentManager();
      ScreenNavigator.UrlInputScreen var4 = this.activity.createUrlInputScreen(var1, var2);
      FragmentTransaction var5 = var3.beginTransaction();
      var5.add(2131296374, var4.getFragment(), "url_input_sceen");
      return var5;
   }

   void dismissUrlInput() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();
      if (!var1.isStateSaved()) {
         var1.popBackStack();
      }
   }

   Fragment getLatestCommitFragment() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();
      int var2 = var1.getBackStackEntryCount();
      return var2 == 0 ? null : var1.findFragmentByTag(this.getFragmentTag(var2 - 1));
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
      FragmentManager var3 = this.activity.getSupportFragmentManager();
      TransactionHelper.BackStackListener var2 = new TransactionHelper.BackStackListener(this);
      this.backStackListener = var2;
      var3.addOnBackStackChangedListener(var2);
   }

   public void onStop(LifecycleOwner var1) {
      this.activity.getSupportFragmentManager().removeOnBackStackChangedListener(this.backStackListener);
      this.backStackListener.onStop();
   }

   void onUrlInputScreenVisible(boolean var1) {
      ScreenNavigator.Screen var2 = (ScreenNavigator.Screen)this.activity.getSupportFragmentManager().findFragmentByTag("home_screen");
      if (var2 != null && var2.getFragment().isVisible() && var2 instanceof ScreenNavigator.HomeScreen) {
         ((ScreenNavigator.HomeScreen)var2).onUrlInputScreenVisible(var1);
      }

   }

   void popAllScreens() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();

      for(int var2 = var1.getBackStackEntryCount(); var2 > 0; --var2) {
         var1.popBackStack();
      }

      var1.executePendingTransactions();
   }

   boolean popScreensUntil(String var1, int var2) {
      boolean var3 = false;
      boolean var4;
      if (var1 == null) {
         var4 = true;
      } else {
         var4 = false;
      }

      FragmentManager var5 = this.activity.getSupportFragmentManager();
      int var6 = var5.getBackStackEntryCount();

      boolean var7;
      while(true) {
         var7 = var3;
         if (var6 <= 0) {
            break;
         }

         FragmentManager.BackStackEntry var8 = var5.getBackStackEntryAt(var6 - 1);
         if (!var4 && TextUtils.equals(var1, this.getEntryTag(var8)) && var2 == this.getEntryType(var8)) {
            var7 = true;
            break;
         }

         var5.popBackStack();
         --var6;
      }

      var5.executePendingTransactions();
      return var7;
   }

   boolean shouldFinish() {
      FragmentManager var1 = this.activity.getSupportFragmentManager();
      int var2 = var1.getBackStackEntryCount();
      boolean var3 = true;
      if (var2 == 0) {
         return true;
      } else {
         if (this.getEntryType(var1.getBackStackEntryAt(var2 - 1)) != 0) {
            var3 = false;
         }

         return var3;
      }
   }

   void showFirstRun() {
      if (!this.isStateSaved()) {
         this.prepareFirstRun().commit();
      }
   }

   void showHomeScreen(boolean var1, int var2) {
      if (!this.isStateSaved()) {
         this.prepareHomeScreen(var1, var2).commit();
         this.activity.getSupportFragmentManager().executePendingTransactions();
      }
   }

   void showUrlInput(String var1, String var2) {
      if (!this.isStateSaved()) {
         Fragment var3 = this.activity.getSupportFragmentManager().findFragmentByTag("url_input_sceen");
         if (var3 == null || !var3.isAdded() || var3.isRemoving()) {
            this.prepareUrlInput(var1, var2).addToBackStack(this.makeEntryTag("url_input_sceen", 2)).commit();
         }
      }
   }

   private static class BackStackListener implements FragmentManager.OnBackStackChangedListener {
      private TransactionHelper helper;
      private Runnable stateRunnable;

      BackStackListener(TransactionHelper var1) {
         this.helper = var1;
      }

      private void executeStateRunnable() {
         if (this.stateRunnable != null) {
            this.stateRunnable.run();
            this.stateRunnable = null;
         }

      }

      // $FF: synthetic method
      public static void lambda$setBrowserState$0(TransactionHelper.BackStackListener var0, boolean var1) {
         var0.setBrowserForegroundState(var1);
      }

      private void onStop() {
         this.helper = null;
         this.stateRunnable = null;
      }

      private void setBrowserForegroundState(boolean var1) {
         if (this.helper != null) {
            ScreenNavigator.BrowserScreen var2 = (ScreenNavigator.BrowserScreen)this.helper.activity.getSupportFragmentManager().findFragmentById(2131296331);
            if (var1) {
               var2.goForeground();
            } else {
               var2.goBackground();
            }

         }
      }

      private void setBrowserState(boolean var1, TransactionHelper var2) {
         this.stateRunnable = new _$$Lambda$TransactionHelper$BackStackListener$KqWYXvDV_PCHcqgGsMvvJBFNPvE(this, var1);
         FragmentAnimationAccessor var3 = this.getTopAnimationAccessibleFragment(var2);
         if (var3 != null) {
            Animation var4 = var3.getCustomEnterTransition();
            if (var4 != null && !var4.hasEnded()) {
               var4.setAnimationListener(new AnimationListener() {
                  public void onAnimationEnd(Animation var1) {
                     BackStackListener.this.executeStateRunnable();
                  }

                  public void onAnimationRepeat(Animation var1) {
                  }

                  public void onAnimationStart(Animation var1) {
                  }
               });
               return;
            }
         }

         this.executeStateRunnable();
      }

      private boolean shouldKeepBrowserRunning(TransactionHelper var1) {
         FragmentManager var2 = var1.activity.getSupportFragmentManager();

         for(int var3 = var2.getBackStackEntryCount() - 1; var3 >= 0; --var3) {
            if (var1.getEntryType(var2.getBackStackEntryAt(var3)) != 2) {
               return false;
            }
         }

         return true;
      }

      FragmentAnimationAccessor getTopAnimationAccessibleFragment(TransactionHelper var1) {
         Fragment var2 = var1.getLatestCommitFragment();
         return var2 != null && var2 instanceof FragmentAnimationAccessor ? (FragmentAnimationAccessor)var2 : null;
      }

      public void onBackStackChanged() {
         if (this.helper != null) {
            if (this.helper.activity.getSupportFragmentManager().findFragmentById(2131296331) instanceof ScreenNavigator.BrowserScreen) {
               this.setBrowserState(this.shouldKeepBrowserRunning(this.helper), this.helper);
            }

         }
      }
   }
}
