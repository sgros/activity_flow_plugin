package org.mozilla.rocket.privately;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import kotlin.NotImplementedError;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.support.base.observer.Observable;
import org.mozilla.focus.activity.BaseActivity;
import org.mozilla.focus.activity.MainActivity;
import org.mozilla.focus.download.DownloadInfoManager;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.urlinput.UrlInputFragment;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.rocket.component.PrivateSessionNotificationService;
import org.mozilla.rocket.privately.browse.BrowserFragment;
import org.mozilla.rocket.privately.home.PrivateHomeFragment;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabViewProvider;
import org.mozilla.rocket.tabs.TabsSessionProvider;

public final class PrivateModeActivity extends BaseActivity implements ScreenNavigator.HostActivity, ScreenNavigator.Provider, FragmentListener, TabsSessionProvider.SessionHost {
   private final String LOG_TAG = "PrivateModeActivity";
   private ScreenNavigator screenNavigator;
   private SessionManager sessionManager;
   private SharedViewModel sharedViewModel;
   private View snackBarContainer;
   private PrivateTabViewProvider tabViewProvider;
   private BroadcastReceiver uiMessageReceiver;

   // $FF: synthetic method
   public static final View access$getSnackBarContainer$p(PrivateModeActivity var0) {
      View var1 = var0.snackBarContainer;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("snackBarContainer");
      }

      return var1;
   }

   private final void dismissUrlInput() {
      ScreenNavigator var1 = this.screenNavigator;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
      }

      var1.popUrlScreen();
      SharedViewModel var2 = this.sharedViewModel;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
      }

      var2.urlInputState().setValue(false);
   }

   private final void dropBrowserFragment() {
      this.stopPrivateMode();
      Toast.makeText((Context)this, 2131755359, 1).show();
   }

   private final boolean handleIntent(Intent var1) {
      String var2;
      if (var1 != null) {
         var2 = var1.getAction();
      } else {
         var2 = null;
      }

      if (Intrinsics.areEqual(var2, "intent_extra_sanitize")) {
         TelemetryWrapper.erasePrivateModeNotification();
         this.stopPrivateMode();
         Toast.makeText((Context)this, 2131755359, 1).show();
         this.finishAndRemoveTask();
         return true;
      } else {
         return false;
      }
   }

   private final void initBroadcastReceivers() {
      this.uiMessageReceiver = (BroadcastReceiver)(new BroadcastReceiver() {
         public void onReceive(Context var1, Intent var2) {
            Intrinsics.checkParameterIsNotNull(var1, "context");
            Intrinsics.checkParameterIsNotNull(var2, "intent");
            if (Intrinsics.areEqual(var2.getAction(), "org.mozilla.action.RELOCATE_FINISH")) {
               DownloadInfoManager.getInstance().showOpenDownloadSnackBar(var2.getLongExtra("org.mozilla.extra.row_id", -1L), PrivateModeActivity.access$getSnackBarContainer$p(PrivateModeActivity.this), PrivateModeActivity.this.LOG_TAG);
            }

         }
      });
   }

   private final void initViewModel() {
      ViewModel var1 = ViewModelProviders.of((FragmentActivity)this).get(SharedViewModel.class);
      Intrinsics.checkExpressionValueIsNotNull(var1, "ViewModelProviders.of(th…redViewModel::class.java)");
      this.sharedViewModel = (SharedViewModel)var1;
      SharedViewModel var2 = this.sharedViewModel;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
      }

      var2.urlInputState().setValue(false);
   }

   private final void makeStatusBarTransparent() {
      Window var1 = this.getWindow();
      Intrinsics.checkExpressionValueIsNotNull(var1, "window");
      View var3 = var1.getDecorView();
      Intrinsics.checkExpressionValueIsNotNull(var3, "window.decorView");
      int var2 = var3.getSystemUiVisibility();
      var1 = this.getWindow();
      Intrinsics.checkExpressionValueIsNotNull(var1, "window");
      var3 = var1.getDecorView();
      Intrinsics.checkExpressionValueIsNotNull(var3, "window.decorView");
      var3.setSystemUiVisibility(var2 | 1280);
   }

   private final void openUrl(Object var1) {
      String var2;
      label12: {
         if (var1 != null) {
            var2 = var1.toString();
            if (var2 != null) {
               break label12;
            }
         }

         var2 = "";
      }

      ((SharedViewModel)ViewModelProviders.of((FragmentActivity)this).get(SharedViewModel.class)).setUrl(var2);
      this.dismissUrlInput();
      this.startPrivateMode();
      ScreenNavigator.get((Context)this).showBrowserScreen(var2, false, false);
   }

   private final void pushToBack() {
      Intent var1 = new Intent((Context)this, MainActivity.class);
      var1.addFlags(16384);
      this.startActivity(var1);
      this.overridePendingTransition(0, 2130771988);
   }

   private final void showUrlInput(Object var1) {
      String var3;
      label20: {
         if (var1 != null) {
            var3 = var1.toString();
            if (var3 != null) {
               break label20;
            }
         }

         var3 = "";
      }

      ScreenNavigator var2 = this.screenNavigator;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
      }

      var2.addUrlScreen(var3);
      SharedViewModel var4 = this.sharedViewModel;
      if (var4 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sharedViewModel");
      }

      var4.urlInputState().setValue(true);
   }

   private final void startPrivateMode() {
      PrivateSessionNotificationService.Companion.start((Context)this);
   }

   private final void stopPrivateMode() {
      PrivateSessionNotificationService.Companion var1 = PrivateSessionNotificationService.Companion;
      Context var2 = (Context)this;
      var1.stop$app_focusWebkitRelease(var2);
      PrivateMode.Companion var4 = PrivateMode.Companion;
      Context var3 = this.getApplicationContext();
      Intrinsics.checkExpressionValueIsNotNull(var3, "this.applicationContext");
      var4.sanitize(var3);
      TabViewProvider.purify(var2);
   }

   public void applyLocale() {
   }

   public ScreenNavigator.Screen createFirstRunScreen() {
      StringBuilder var1 = new StringBuilder();
      var1.append("An operation is not implemented: ");
      var1.append("PrivateModeActivity should never show first-run");
      throw (Throwable)(new NotImplementedError(var1.toString()));
   }

   public ScreenNavigator.HomeScreen createHomeScreen() {
      return (ScreenNavigator.HomeScreen)PrivateHomeFragment.Companion.create();
   }

   public ScreenNavigator.UrlInputScreen createUrlInputScreen(String var1, String var2) {
      return (ScreenNavigator.UrlInputScreen)UrlInputFragment.Companion.create(var1, (String)null, false);
   }

   public ScreenNavigator.BrowserScreen getBrowserScreen() {
      Fragment var1 = this.getSupportFragmentManager().findFragmentById(2131296331);
      if (var1 != null) {
         return (ScreenNavigator.BrowserScreen)((BrowserFragment)var1);
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.privately.browse.BrowserFragment");
      }
   }

   public ScreenNavigator getScreenNavigator() {
      ScreenNavigator var1 = this.screenNavigator;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
      }

      return var1;
   }

   public SessionManager getSessionManager() {
      if (this.sessionManager == null) {
         PrivateTabViewProvider var1 = this.tabViewProvider;
         if (var1 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tabViewProvider");
         }

         this.sessionManager = new SessionManager((TabViewProvider)var1, (Observable)null, 2, (DefaultConstructorMarker)null);
      }

      SessionManager var2 = this.sessionManager;
      if (var2 == null) {
         Intrinsics.throwNpe();
      }

      return var2;
   }

   public void onBackPressed() {
      FragmentManager var1 = this.getSupportFragmentManager();
      Intrinsics.checkExpressionValueIsNotNull(var1, "supportFragmentManager");
      if (!var1.isStateSaved()) {
         ScreenNavigator var3 = this.screenNavigator;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
         }

         ScreenNavigator.BrowserScreen var4 = var3.getVisibleBrowserScreen();
         boolean var2;
         if (var4 != null) {
            var2 = var4.onBackPressed();
         } else {
            var2 = false;
         }

         if (!var2) {
            var3 = this.screenNavigator;
            if (var3 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
            }

            if (!var3.canGoBack()) {
               this.finish();
            } else {
               super.onBackPressed();
            }
         }
      }
   }

   protected void onCreate(Bundle var1) {
      super.onCreate((Bundle)null);
      this.tabViewProvider = new PrivateTabViewProvider((Activity)this);
      this.screenNavigator = new ScreenNavigator((ScreenNavigator.HostActivity)this);
      if (this.handleIntent(this.getIntent())) {
         this.pushToBack();
      } else {
         this.setContentView(2131492895);
         View var2 = this.findViewById(2131296374);
         Intrinsics.checkExpressionValueIsNotNull(var2, "findViewById(R.id.container)");
         this.snackBarContainer = var2;
         this.makeStatusBarTransparent();
         this.initViewModel();
         this.initBroadcastReceivers();
         ScreenNavigator var3 = this.screenNavigator;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("screenNavigator");
         }

         var3.popToHomeScreen(false);
      }
   }

   protected void onDestroy() {
      super.onDestroy();
      this.stopPrivateMode();
      SessionManager var1 = this.sessionManager;
      if (var1 != null) {
         var1.destroy();
      }

   }

   protected void onNewIntent(Intent var1) {
      super.onNewIntent(var1);
      if (!this.handleIntent(var1)) {
         ;
      }
   }

   public void onNotified(Fragment var1, FragmentListener.TYPE var2, Object var3) {
      // $FF: Couldn't be decompiled
   }

   protected void onPause() {
      super.onPause();
      LocalBroadcastManager var1 = LocalBroadcastManager.getInstance((Context)this);
      BroadcastReceiver var2 = this.uiMessageReceiver;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
      }

      var1.unregisterReceiver(var2);
   }

   protected void onResume() {
      super.onResume();
      IntentFilter var1 = new IntentFilter();
      var1.addCategory("org.mozilla.category.FILE_OPERATION");
      var1.addAction("org.mozilla.action.RELOCATE_FINISH");
      LocalBroadcastManager var2 = LocalBroadcastManager.getInstance((Context)this);
      BroadcastReceiver var3 = this.uiMessageReceiver;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("uiMessageReceiver");
      }

      var2.registerReceiver(var3, var1);
   }
}
