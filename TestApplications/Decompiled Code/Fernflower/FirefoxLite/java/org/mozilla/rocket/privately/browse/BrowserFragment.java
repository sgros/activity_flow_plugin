package org.mozilla.rocket.privately.browse;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.FileChooserParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Iterator;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import org.mozilla.focus.download.EnqueueDownloadTask;
import org.mozilla.focus.locale.LocaleAwareFragment;
import org.mozilla.focus.menu.WebContextMenu;
import org.mozilla.focus.navigation.ScreenNavigator;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.focus.utils.ViewUtils;
import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.focus.widget.AnimatedProgressBar;
import org.mozilla.focus.widget.BackKeyHandleable;
import org.mozilla.focus.widget.FragmentListener;
import org.mozilla.permissionhandler.PermissionHandle;
import org.mozilla.permissionhandler.PermissionHandler;
import org.mozilla.rocket.privately.SharedViewModel;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.TabViewEngineSession;
import org.mozilla.rocket.tabs.TabsSessionProvider;
import org.mozilla.rocket.tabs.utils.TabUtil;
import org.mozilla.rocket.tabs.web.Download;
import org.mozilla.rocket.tabs.web.DownloadCallback;
import org.mozilla.threadutils.ThreadUtils;
import org.mozilla.urlutils.UrlUtils;

public final class BrowserFragment extends LocaleAwareFragment implements ScreenNavigator.BrowserScreen, BackKeyHandleable {
   private HashMap _$_findViewCache;
   private ViewGroup browserContainer;
   private ImageButton btnLoad;
   private ImageButton btnNext;
   private TextView displayUrlView;
   private boolean isLoading;
   private FragmentListener listener;
   private BrowserFragment.Observer observer;
   private PermissionHandler permissionHandler;
   private AnimatedProgressBar progressView;
   private SessionManager sessionManager;
   private ImageView siteIdentity;
   private int systemVisibility = -1;
   private ViewGroup tabViewSlot;
   private ViewGroup videoContainer;

   // $FF: synthetic method
   public static final ViewGroup access$getBrowserContainer$p(BrowserFragment var0) {
      ViewGroup var1 = var0.browserContainer;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("browserContainer");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final ImageButton access$getBtnLoad$p(BrowserFragment var0) {
      ImageButton var1 = var0.btnLoad;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("btnLoad");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final ImageButton access$getBtnNext$p(BrowserFragment var0) {
      ImageButton var1 = var0.btnNext;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("btnNext");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final TextView access$getDisplayUrlView$p(BrowserFragment var0) {
      TextView var1 = var0.displayUrlView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final PermissionHandler access$getPermissionHandler$p(BrowserFragment var0) {
      PermissionHandler var1 = var0.permissionHandler;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final AnimatedProgressBar access$getProgressView$p(BrowserFragment var0) {
      AnimatedProgressBar var1 = var0.progressView;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("progressView");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final SessionManager access$getSessionManager$p(BrowserFragment var0) {
      SessionManager var1 = var0.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final ImageView access$getSiteIdentity$p(BrowserFragment var0) {
      ImageView var1 = var0.siteIdentity;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("siteIdentity");
      }

      return var1;
   }

   // $FF: synthetic method
   public static final ViewGroup access$getVideoContainer$p(BrowserFragment var0) {
      ViewGroup var1 = var0.videoContainer;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("videoContainer");
      }

      return var1;
   }

   private final Unit goBack() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var2 = var1.getFocusSession();
      Unit var4;
      if (var2 != null) {
         TabViewEngineSession var3 = var2.getEngineSession();
         if (var3 != null) {
            var4 = var3.goBack();
            return var4;
         }
      }

      var4 = null;
      return var4;
   }

   private final Unit goForward() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var2 = var1.getFocusSession();
      Unit var4;
      if (var2 != null) {
         TabViewEngineSession var3 = var2.getEngineSession();
         if (var3 != null) {
            var4 = var3.goForward();
            return var4;
         }
      }

      var4 = null;
      return var4;
   }

   private final void onDeleteClicked() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      SessionManager var3;
      Session var4;
      for(Iterator var2 = var1.getTabs().iterator(); var2.hasNext(); var3.dropTab(var4.getId())) {
         var4 = (Session)var2.next();
         var3 = this.sessionManager;
         if (var3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
         }
      }

      FragmentListener var5 = this.listener;
      if (var5 != null) {
         var5.onNotified((Fragment)this, FragmentListener.TYPE.DROP_BROWSING_PAGES, (Object)null);
      }

      ScreenNavigator.get((Context)this.getActivity()).popToHomeScreen(true);
   }

   private final void onLoadClicked() {
      if (this.isLoading) {
         this.stop();
      } else {
         this.reload();
      }

   }

   private final void onModeClicked() {
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         ((FragmentListener)var1).onNotified((Fragment)this, FragmentListener.TYPE.TOGGLE_PRIVATE_MODE, (Object)null);
         TelemetryWrapper.togglePrivateMode(false);
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
      }
   }

   private final void onNextClicked() {
      this.goForward();
   }

   private final void onSearchClicked() {
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         FragmentListener var2 = (FragmentListener)var1;
         Fragment var3 = (Fragment)this;
         FragmentListener.TYPE var4 = FragmentListener.TYPE.SHOW_URL_INPUT;
         TextView var5 = this.displayUrlView;
         if (var5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
         }

         var2.onNotified(var3, var4, var5.getText());
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.focus.widget.FragmentListener");
      }
   }

   private final void registerData(FragmentActivity var1) {
      ViewModel var2 = ViewModelProviders.of(var1).get(SharedViewModel.class);
      Intrinsics.checkExpressionValueIsNotNull(var2, "ViewModelProviders.of(ac…redViewModel::class.java)");
      ((SharedViewModel)var2).getUrl().observe((LifecycleOwner)this, (android.arch.lifecycle.Observer)(new android.arch.lifecycle.Observer() {
         public final void onChanged(String var1) {
            if (var1 != null) {
               BrowserFragment var2 = BrowserFragment.this;
               Intrinsics.checkExpressionValueIsNotNull(var1, "it");
               var2.loadUrl(var1, false, false, (Runnable)null);
            }

         }
      }));
   }

   private final Unit reload() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var2 = var1.getFocusSession();
      Unit var4;
      if (var2 != null) {
         TabViewEngineSession var3 = var2.getEngineSession();
         if (var3 != null) {
            var4 = var3.reload();
            return var4;
         }
      }

      var4 = null;
      return var4;
   }

   private final Unit stop() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var2 = var1.getFocusSession();
      Unit var4;
      if (var2 != null) {
         TabViewEngineSession var3 = var2.getEngineSession();
         if (var3 != null) {
            var4 = var3.stopLoading();
            return var4;
         }
      }

      var4 = null;
      return var4;
   }

   private final void unregisterData(FragmentActivity var1) {
      ViewModel var2 = ViewModelProviders.of(var1).get(SharedViewModel.class);
      Intrinsics.checkExpressionValueIsNotNull(var2, "ViewModelProviders.of(ac…redViewModel::class.java)");
      ((SharedViewModel)var2).getUrl().removeObservers((LifecycleOwner)this);
   }

   public void _$_clearFindViewByIdCache() {
      if (this._$_findViewCache != null) {
         this._$_findViewCache.clear();
      }

   }

   public void applyLocale() {
      (new WebView(this.getContext())).destroy();
   }

   public Fragment getFragment() {
      return (Fragment)this;
   }

   public void goBackground() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var2 = var1.getFocusSession();
      if (var2 != null) {
         TabViewEngineSession var3 = var2.getEngineSession();
         if (var3 != null) {
            TabView var4 = var3.getTabView();
            if (var4 != null) {
               TabViewEngineSession var5 = var2.getEngineSession();
               if (var5 != null) {
                  var5.detach();
               }

               ViewGroup var6 = this.tabViewSlot;
               if (var6 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
               }

               var6.removeView(var4.getView());
               return;
            }
         }

      }
   }

   public void goForeground() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var3 = var1.getFocusSession();
      if (var3 != null) {
         TabViewEngineSession var4 = var3.getEngineSession();
         if (var4 != null) {
            TabView var5 = var4.getTabView();
            if (var5 != null) {
               ViewGroup var2 = this.tabViewSlot;
               if (var2 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
               }

               if (var2.getChildCount() == 0) {
                  var2 = this.tabViewSlot;
                  if (var2 == null) {
                     Intrinsics.throwUninitializedPropertyAccessException("tabViewSlot");
                  }

                  var2.addView(var5.getView());
               }

               return;
            }
         }
      }

   }

   public void loadUrl(String var1, boolean var2, boolean var3, Runnable var4) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      CharSequence var5 = (CharSequence)var1;
      if (StringsKt.isBlank(var5) ^ true) {
         TextView var6 = this.displayUrlView;
         if (var6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
         }

         var6.setText(var5);
         SessionManager var7 = this.sessionManager;
         if (var7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
         }

         if (var7.getTabsCount() == 0) {
            var7 = this.sessionManager;
            if (var7 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }

            Bundle var9 = TabUtil.argument((String)null, false, true);
            Intrinsics.checkExpressionValueIsNotNull(var9, "TabUtil.argument(null, false, true)");
            var7.addTab(var1, var9);
         } else {
            var7 = this.sessionManager;
            if (var7 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
            }

            Session var8 = var7.getFocusSession();
            if (var8 == null) {
               Intrinsics.throwNpe();
            }

            TabViewEngineSession var10 = var8.getEngineSession();
            if (var10 != null) {
               TabView var11 = var10.getTabView();
               if (var11 != null) {
                  var11.loadUrl(var1);
               }
            }
         }

         ThreadUtils.postToMainThread(var4);
      }

   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      this.getActivity();
   }

   public void onAttach(Context var1) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      super.onAttach(var1);
      this.permissionHandler = new PermissionHandler((PermissionHandle)(new PermissionHandle() {
         private final void queueDownload(Download var1) {
            FragmentActivity var2 = BrowserFragment.this.getActivity();
            if (var2 != null && var1 != null) {
               (new EnqueueDownloadTask((Activity)var2, var1, BrowserFragment.access$getDisplayUrlView$p(BrowserFragment.this).getText().toString())).execute(new Void[0]);
            }

         }

         public final void actionDownloadGranted(Parcelable var1) {
            if (var1 != null) {
               this.queueDownload((Download)var1);
            } else {
               throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.web.Download");
            }
         }

         public void doActionDirect(String var1, int var2, Parcelable var3) {
            Context var4 = BrowserFragment.this.getContext();
            if (var4 != null) {
               if (var3 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.web.Download");
               }

               Download var6 = (Download)var3;
               if (ContextCompat.checkSelfPermission(var4, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                  this.queueDownload(var6);
               }

               if (var4 != null) {
                  return;
               }
            }

            <undefinedtype> var5 = (<undefinedtype>)this;
            Log.e("BrowserFragment.kt", "No context to use, abort callback onDownloadStart");
         }

         public void doActionGranted(String var1, int var2, Parcelable var3) {
            this.actionDownloadGranted(var3);
         }

         public void doActionNoPermission(String var1, int var2, Parcelable var3) {
         }

         public void doActionSetting(String var1, int var2, Parcelable var3) {
            this.actionDownloadGranted(var3);
         }

         public Snackbar makeAskAgainSnackBar(int var1) {
            FragmentActivity var2 = BrowserFragment.this.getActivity();
            if (var2 != null) {
               Snackbar var3 = PermissionHandler.makeAskAgainSnackBar((Fragment)BrowserFragment.this, var2.findViewById(2131296374), 2131755291);
               Intrinsics.checkExpressionValueIsNotNull(var3, "PermissionHandler.makeAs…age\n                    )");
               return var3;
            } else {
               throw (Throwable)(new IllegalStateException("No Activity to show Snackbar."));
            }
         }

         public void permissionDeniedToast(int var1) {
            Toast.makeText(BrowserFragment.this.getContext(), 2131755292, 1).show();
         }

         public void requestPermissions(int var1) {
            BrowserFragment.this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, var1);
         }
      }));
      if (var1 instanceof FragmentListener) {
         this.listener = (FragmentListener)var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append(var1.toString());
         var2.append(" must implement OnFragmentInteractionListener");
         throw (Throwable)(new RuntimeException(var2.toString()));
      }
   }

   public boolean onBackPressed() {
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var3 = var1.getFocusSession();
      if (var3 != null) {
         TabViewEngineSession var2 = var3.getEngineSession();
         if (var2 != null) {
            TabView var5 = var2.getTabView();
            if (var5 != null) {
               if (var5.canGoBack()) {
                  this.goBack();
                  return true;
               }

               SessionManager var6 = this.sessionManager;
               if (var6 == null) {
                  Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
               }

               var6.dropTab(var3.getId());
               ScreenNavigator.get((Context)this.getActivity()).popToHomeScreen(true);
               FragmentListener var4 = this.listener;
               if (var4 != null) {
                  var4.onNotified((Fragment)this, FragmentListener.TYPE.DROP_BROWSING_PAGES, (Object)null);
               }

               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
      Intrinsics.checkParameterIsNotNull(var1, "inflater");
      return var1.inflate(2131492969, var2, false);
   }

   public void onDestroyView() {
      super.onDestroyView();
      FragmentActivity var1 = this.getActivity();
      if (var1 != null) {
         Intrinsics.checkExpressionValueIsNotNull(var1, "it");
         this.unregisterData(var1);
      }

      SessionManager var3 = this.sessionManager;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      Session var4 = var3.getFocusSession();
      BrowserFragment.Observer var2;
      if (var4 != null) {
         var2 = this.observer;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("observer");
         }

         var4.unregister((Session.Observer)var2);
      }

      var3 = this.sessionManager;
      if (var3 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      var2 = this.observer;
      if (var2 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("observer");
      }

      var3.unregister((SessionManager.Observer)var2);
      this._$_clearFindViewByIdCache();
   }

   public void onDetach() {
      super.onDetach();
      this.listener = (FragmentListener)null;
   }

   public void onPause() {
      super.onPause();
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      var1.pause();
   }

   public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
      Intrinsics.checkParameterIsNotNull(var2, "permissions");
      Intrinsics.checkParameterIsNotNull(var3, "grantResults");
      PermissionHandler var4 = this.permissionHandler;
      if (var4 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("permissionHandler");
      }

      var4.onRequestPermissionsResult(this.getContext(), var1, var2, var3);
   }

   public void onResume() {
      super.onResume();
      SessionManager var1 = this.sessionManager;
      if (var1 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
      }

      var1.resume();
   }

   public void onViewCreated(View var1, Bundle var2) {
      Intrinsics.checkParameterIsNotNull(var1, "view");
      super.onViewCreated(var1, var2);
      View var6 = var1.findViewById(2131296411);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.display_url)");
      this.displayUrlView = (TextView)var6;
      TextView var8 = this.displayUrlView;
      if (var8 == null) {
         Intrinsics.throwUninitializedPropertyAccessException("displayUrlView");
      }

      var8.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            BrowserFragment.this.onSearchClicked();
         }
      }));
      var6 = var1.findViewById(2131296648);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.site_identity)");
      this.siteIdentity = (ImageView)var6;
      var6 = var1.findViewById(2131296337);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.browser_container)");
      this.browserContainer = (ViewGroup)var6;
      var6 = var1.findViewById(2131296717);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.video_container)");
      this.videoContainer = (ViewGroup)var6;
      var6 = var1.findViewById(2131296680);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.tab_view_slot)");
      this.tabViewSlot = (ViewGroup)var6;
      var6 = var1.findViewById(2131296576);
      Intrinsics.checkExpressionValueIsNotNull(var6, "view.findViewById(R.id.progress)");
      this.progressView = (AnimatedProgressBar)var6;
      var1.findViewById(2131296352).setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            BrowserFragment.this.onModeClicked();
         }
      }));
      var1.findViewById(2131296356).setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            BrowserFragment.this.onSearchClicked();
         }
      }));
      var1.findViewById(2131296347).setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            BrowserFragment.this.onDeleteClicked();
         }
      }));
      var6 = var1.findViewById(2131296348);
      ImageButton var3 = (ImageButton)var6;
      var3.setOnClickListener((OnClickListener)(new OnClickListener() {
         public final void onClick(View var1) {
            BrowserFragment.this.onLoadClicked();
         }
      }));
      Intrinsics.checkExpressionValueIsNotNull(var6, "(view.findViewById<Image…ner { onLoadClicked() } }");
      this.btnLoad = var3;
      var6 = var1.findViewById(2131296353);
      if (var6 != null) {
         ImageButton var10 = (ImageButton)var6;
         var10.setEnabled(false);
         var10.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View var1) {
               BrowserFragment.this.onNextClicked();
            }
         }));
         this.btnNext = var10;
         var1.findViewById(2131296295).setOnApplyWindowInsetsListener((OnApplyWindowInsetsListener)null.INSTANCE);
         SessionManager var4 = TabsSessionProvider.getOrThrow((Activity)this.getActivity());
         Intrinsics.checkExpressionValueIsNotNull(var4, "TabsSessionProvider.getOrThrow( activity)");
         this.sessionManager = var4;
         this.observer = new BrowserFragment.Observer(this);
         SessionManager var11 = this.sessionManager;
         if (var11 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
         }

         BrowserFragment.Observer var5 = this.observer;
         if (var5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("observer");
         }

         var11.register((SessionManager.Observer)var5);
         var4 = this.sessionManager;
         if (var4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
         }

         Session var7 = var4.getFocusSession();
         if (var7 != null) {
            BrowserFragment.Observer var12 = this.observer;
            if (var12 == null) {
               Intrinsics.throwUninitializedPropertyAccessException("observer");
            }

            var7.register((Session.Observer)var12);
         }

         FragmentActivity var9 = this.getActivity();
         if (var9 != null) {
            Intrinsics.checkExpressionValueIsNotNull(var9, "it");
            this.registerData(var9);
         }

      } else {
         throw new TypeCastException("null cannot be cast to non-null type android.widget.ImageButton");
      }
   }

   public void switchToTab(String var1) {
      if (!TextUtils.isEmpty((CharSequence)var1)) {
         SessionManager var2 = this.sessionManager;
         if (var2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("sessionManager");
         }

         if (var1 == null) {
            Intrinsics.throwNpe();
         }

         var2.switchToTab(var1);
      }

   }

   public static final class Observer implements Session.Observer, SessionManager.Observer {
      private TabView.FullscreenCallback callback;
      private final BrowserFragment fragment;
      private Session session;

      public Observer(BrowserFragment var1) {
         Intrinsics.checkParameterIsNotNull(var1, "fragment");
         super();
         this.fragment = var1;
      }

      public boolean handleExternalUrl(String var1) {
         return false;
      }

      public boolean onDownload(Session var1, mozilla.components.browser.session.Download var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "download");
         FragmentActivity var6 = this.fragment.getActivity();
         if (var6 != null) {
            Lifecycle var7 = var6.getLifecycle();
            Intrinsics.checkExpressionValueIsNotNull(var7, "activity.lifecycle");
            if (var7.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
               String var3 = var2.getUrl();
               String var4 = var2.getFileName();
               String var5 = var2.getUserAgent();
               if (var5 == null) {
                  Intrinsics.throwNpe();
               }

               String var8 = var2.getContentType();
               if (var8 == null) {
                  Intrinsics.throwNpe();
               }

               Long var9 = var2.getContentLength();
               if (var9 == null) {
                  Intrinsics.throwNpe();
               }

               Download var10 = new Download(var3, var4, var5, "", var8, var9, false);
               BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction((Fragment)((Fragment)this.fragment), "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)var10);
               return true;
            }
         }

         return false;
      }

      public void onEnterFullScreen(TabView.FullscreenCallback var1, View var2) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         BrowserFragment var3 = this.fragment;
         BrowserFragment.access$getBrowserContainer$p(var3).setVisibility(4);
         BrowserFragment.access$getVideoContainer$p(var3).setVisibility(0);
         BrowserFragment.access$getVideoContainer$p(var3).addView(var2);
         var3.systemVisibility = ViewUtils.switchToImmersiveMode((Activity)var3.getActivity());
      }

      public void onExitFullScreen() {
         BrowserFragment var1 = this.fragment;
         BrowserFragment.access$getBrowserContainer$p(var1).setVisibility(0);
         BrowserFragment.access$getVideoContainer$p(var1).setVisibility(4);
         BrowserFragment.access$getVideoContainer$p(var1).removeAllViews();
         if (var1.systemVisibility != -1) {
            ViewUtils.exitImmersiveMode(var1.systemVisibility, (Activity)var1.getActivity());
         }

         TabView.FullscreenCallback var2 = this.callback;
         if (var2 != null) {
            var2.fullScreenExited();
         }

         this.callback = (TabView.FullscreenCallback)null;
         Session var3 = this.session;
         if (var3 != null) {
            TabViewEngineSession var4 = var3.getEngineSession();
            if (var4 != null) {
               TabView var5 = var4.getTabView();
               if (var5 != null && var5 instanceof WebView) {
                  ((WebView)var5).clearFocus();
               }
            }
         }

      }

      public void onFindResult(Session var1, mozilla.components.browser.session.Session.FindResult var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "result");
         Session.Observer.DefaultImpls.onFindResult(this, var1, var2);
      }

      public void onFocusChanged(Session var1, SessionManager.Factor var2) {
         Intrinsics.checkParameterIsNotNull(var2, "factor");
         SessionManager.Observer.DefaultImpls.onFocusChanged(this, var1, var2);
      }

      public void onGeolocationPermissionsShowPrompt(String var1, Callback var2) {
         Intrinsics.checkParameterIsNotNull(var1, "origin");
         Session.Observer.DefaultImpls.onGeolocationPermissionsShowPrompt(this, var1, var2);
      }

      public void onHttpAuthRequest(final TabViewClient.HttpAuthCallback var1, String var2, String var3) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         HttpAuthenticationDialogBuilder var4 = (new HttpAuthenticationDialogBuilder.Builder((Context)this.fragment.getActivity(), var2, var3)).setOkListener((HttpAuthenticationDialogBuilder.OkListener)(new HttpAuthenticationDialogBuilder.OkListener() {
            public final void onOk(String var1x, String var2, String var3, String var4) {
               var1.proceed(var3, var4);
            }
         })).setCancelListener((HttpAuthenticationDialogBuilder.CancelListener)(new HttpAuthenticationDialogBuilder.CancelListener() {
            public final void onCancel() {
               var1.cancel();
            }
         })).build();
         var4.createDialog();
         var4.show();
      }

      public void onLoadingStateChanged(Session var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         this.fragment.isLoading = var2;
         if (var2) {
            BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230879);
         } else {
            var1 = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
            if (var1 != null) {
               TabViewEngineSession var3 = var1.getEngineSession();
               if (var3 != null) {
                  ImageButton var4 = BrowserFragment.access$getBtnNext$p(this.fragment);
                  TabView var5 = var3.getTabView();
                  if (var5 != null) {
                     var2 = var5.canGoForward();
                  } else {
                     var2 = false;
                  }

                  var4.setEnabled(var2);
                  BrowserFragment.access$getBtnLoad$p(this.fragment).setImageResource(2131230900);
                  return;
               }
            }

         }
      }

      public void onLongPress(Session var1, TabView.HitTarget var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "hitTarget");
         FragmentActivity var3 = this.fragment.getActivity();
         if (var3 != null) {
            WebContextMenu.show(true, (Activity)var3, (DownloadCallback)(new BrowserFragment.PrivateDownloadCallback(this.fragment, var1.getUrl())), var2);
         }

      }

      public void onNavigationStateChanged(Session var1, boolean var2, boolean var3) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Session.Observer.DefaultImpls.onNavigationStateChanged(this, var1, var2, var3);
      }

      public void onProgress(Session var1, int var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         BrowserFragment.access$getProgressView$p(this.fragment).setProgress(var2);
      }

      public void onReceivedIcon(Bitmap var1) {
         Session.Observer.DefaultImpls.onReceivedIcon(this, var1);
      }

      public void onSecurityChanged(Session var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         BrowserFragment.access$getSiteIdentity$p(this.fragment).setImageLevel(var2);
      }

      public void onSessionAdded(Session var1, Bundle var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
      }

      public void onSessionCountChanged(int var1) {
         Session var2;
         if (var1 == 0) {
            var2 = this.session;
            if (var2 != null) {
               var2.unregister((Session.Observer)this);
            }
         } else {
            this.session = BrowserFragment.access$getSessionManager$p(this.fragment).getFocusSession();
            var2 = this.session;
            if (var2 != null) {
               var2.register((Session.Observer)this);
            }
         }

      }

      public boolean onShowFileChooser(TabViewEngineSession var1, ValueCallback var2, FileChooserParams var3) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         return false;
      }

      public void onTitleChanged(Session var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         if (!BrowserFragment.access$getDisplayUrlView$p(this.fragment).getText().toString().equals(var1.getUrl())) {
            BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText((CharSequence)var1.getUrl());
         }

      }

      public void onUrlChanged(Session var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         if (!UrlUtils.isInternalErrorURL(var2)) {
            BrowserFragment.access$getDisplayUrlView$p(this.fragment).setText((CharSequence)var2);
         }

      }

      public void updateFailingUrl(String var1, boolean var2) {
      }
   }

   public static final class PrivateDownloadCallback implements DownloadCallback {
      private final BrowserFragment fragment;
      private final String refererUrl;

      public PrivateDownloadCallback(BrowserFragment var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "fragment");
         super();
         this.fragment = var1;
         this.refererUrl = var2;
      }

      public void onDownloadStart(Download var1) {
         Intrinsics.checkParameterIsNotNull(var1, "download");
         FragmentActivity var2 = this.fragment.getActivity();
         if (var2 != null) {
            Intrinsics.checkExpressionValueIsNotNull(var2, "it");
            Lifecycle var3 = var2.getLifecycle();
            Intrinsics.checkExpressionValueIsNotNull(var3, "it.lifecycle");
            if (!var3.getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
               return;
            }
         }

         BrowserFragment.access$getPermissionHandler$p(this.fragment).tryAction((Fragment)((Fragment)this.fragment), "android.permission.WRITE_EXTERNAL_STORAGE", 0, (Parcelable)var1);
      }
   }
}
