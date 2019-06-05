package org.mozilla.rocket.tabs;

import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.concept.engine.EngineSession;
import mozilla.components.support.base.observer.Observable;
import mozilla.components.support.base.observer.ObserverRegistry;
import org.mozilla.rocket.tabs.web.Download;

public final class TabViewEngineSession implements Observable {
   private final Observable delegate;
   private TabViewEngineSession.Client engineSessionClient;
   private TabView engineView;
   private Bundle webViewState;
   private TabViewEngineSession.WindowClient windowClient;

   public TabViewEngineSession() {
      this((Observable)null, 1, (DefaultConstructorMarker)null);
   }

   public TabViewEngineSession(Observable var1) {
      Intrinsics.checkParameterIsNotNull(var1, "delegate");
      super();
      this.delegate = var1;
   }

   // $FF: synthetic method
   public TabViewEngineSession(Observable var1, int var2, DefaultConstructorMarker var3) {
      if ((var2 & 1) != 0) {
         var1 = (Observable)(new ObserverRegistry());
      }

      this(var1);
   }

   public final void destroy$feature_tabs_release() {
      this.unregisterObservers();
      this.detach();
      TabView var1 = this.getTabView();
      if (var1 != null) {
         var1.destroy();
      }

   }

   public final void detach() {
      TabView var1 = this.getTabView();
      if (var1 != null) {
         View var2 = var1.getView();
         if (var2 != null) {
            ViewParent var3 = var2.getParent();
            if (var3 != null) {
               if (var3 == null) {
                  throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup");
               }

               ((ViewGroup)var3).removeView(var2);
            }
         }
      }

   }

   public final TabViewEngineSession.Client getEngineSessionClient() {
      return this.engineSessionClient;
   }

   public final TabView getTabView() {
      return this.engineView;
   }

   public final Bundle getWebViewState() {
      return this.webViewState;
   }

   public final TabViewEngineSession.WindowClient getWindowClient() {
      return this.windowClient;
   }

   public final Unit goBack() {
      TabView var1 = this.getTabView();
      Unit var2;
      if (var1 != null) {
         var1.goBack();
         var2 = Unit.INSTANCE;
      } else {
         var2 = null;
      }

      return var2;
   }

   public final Unit goForward() {
      TabView var1 = this.getTabView();
      Unit var2;
      if (var1 != null) {
         var1.goForward();
         var2 = Unit.INSTANCE;
      } else {
         var2 = null;
      }

      return var2;
   }

   public void notifyObservers(Function1 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      this.delegate.notifyObservers(var1);
   }

   public void register(TabViewEngineSession.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.delegate.register(var1);
   }

   public void register(TabViewEngineSession.Observer var1, LifecycleOwner var2, boolean var3) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      Intrinsics.checkParameterIsNotNull(var2, "owner");
      this.delegate.register(var1, var2, var3);
   }

   public final Unit reload() {
      TabView var1 = this.getTabView();
      Unit var2;
      if (var1 != null) {
         var1.reload();
         var2 = Unit.INSTANCE;
      } else {
         var2 = null;
      }

      return var2;
   }

   public final void saveState() {
      if (this.webViewState == null) {
         this.webViewState = new Bundle();
      }

      TabView var1 = this.getTabView();
      if (var1 != null) {
         var1.saveViewState(this.webViewState);
      }

   }

   public final void setEngineSessionClient(TabViewEngineSession.Client var1) {
      this.engineSessionClient = var1;
   }

   public final void setTabView(TabView var1) {
      if (var1 != null) {
         var1.setViewClient((TabViewClient)(new TabViewEngineSession.ViewClient(this)));
      }

      if (var1 != null) {
         var1.setChromeClient((TabChromeClient)(new TabViewEngineSession.ChromeClient(this)));
      }

      if (var1 != null) {
         var1.setFindListener((TabView.FindListener)(new TabViewEngineSession.FindListener(this)));
      }

      if (var1 != null) {
         var1.setDownloadCallback((org.mozilla.rocket.tabs.web.DownloadCallback)(new TabViewEngineSession.DownloadCallback(this)));
      }

      TabView var2 = this.engineView;
      if (var2 != null) {
         var2.setViewClient((TabViewClient)null);
      }

      var2 = this.engineView;
      if (var2 != null) {
         var2.setChromeClient((TabChromeClient)null);
      }

      var2 = this.engineView;
      if (var2 != null) {
         var2.setFindListener((TabView.FindListener)null);
      }

      var2 = this.engineView;
      if (var2 != null) {
         var2.setDownloadCallback((org.mozilla.rocket.tabs.web.DownloadCallback)null);
      }

      this.engineView = var1;
   }

   public final void setWebViewState(Bundle var1) {
      this.webViewState = var1;
   }

   public final void setWindowClient(TabViewEngineSession.WindowClient var1) {
      this.windowClient = var1;
   }

   public final Unit stopLoading() {
      TabView var1 = this.getTabView();
      Unit var2;
      if (var1 != null) {
         var1.stopLoading();
         var2 = Unit.INSTANCE;
      } else {
         var2 = null;
      }

      return var2;
   }

   public void unregister(TabViewEngineSession.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      this.delegate.unregister(var1);
   }

   public void unregisterObservers() {
      this.delegate.unregisterObservers();
   }

   public List wrapConsumers(Function2 var1) {
      Intrinsics.checkParameterIsNotNull(var1, "block");
      return this.delegate.wrapConsumers(var1);
   }

   public static final class ChromeClient extends TabChromeClient {
      private final TabViewEngineSession es;

      public ChromeClient(TabViewEngineSession var1) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         super();
         this.es = var1;
      }

      public void onCloseWindow(TabView var1) {
         TabViewEngineSession.WindowClient var2 = this.es.getWindowClient();
         if (var2 != null) {
            var2.onCloseWindow(this.es);
         }

      }

      public boolean onCreateWindow(boolean var1, boolean var2, Message var3) {
         TabViewEngineSession.WindowClient var4 = this.es.getWindowClient();
         if (var4 != null) {
            var1 = var4.onCreateWindow(var1, var2, var3);
         } else {
            var1 = false;
         }

         return var1;
      }

      public void onEnterFullScreen(final TabView.FullscreenCallback var1, final View var2) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onEnterFullScreen(var1, var2);
            }
         }));
      }

      public void onExitFullScreen() {
         this.es.notifyObservers((Function1)null.INSTANCE);
      }

      public void onGeolocationPermissionsShowPrompt(final String var1, final Callback var2) {
         Intrinsics.checkParameterIsNotNull(var1, "origin");
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onGeolocationPermissionsShowPrompt(var1, var2);
            }
         }));
      }

      public void onLongPress(final TabView.HitTarget var1) {
         Intrinsics.checkParameterIsNotNull(var1, "hitTarget");
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onLongPress(var1);
            }
         }));
      }

      public void onProgressChanged(final int var1) {
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onProgress(var1);
            }
         }));
      }

      public void onReceivedIcon(TabView var1, final Bitmap var2) {
         Intrinsics.checkParameterIsNotNull(var1, "view");
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1) {
               Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
               var1.onReceivedIcon(var2);
            }
         }));
      }

      public void onReceivedTitle(final TabView var1, final String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "view");
         if (var2 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onTitleChange(var2);
               }
            }));
         }

         var2 = var1.getUrl();
         if (var2 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onLocationChange(var2);
               }
            }));
         }

         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onNavigationStateChange(var1.canGoBack(), var1.canGoForward());
            }
         }));
      }

      public boolean onShowFileChooser(TabView var1, ValueCallback var2, FileChooserParams var3) {
         Intrinsics.checkParameterIsNotNull(var1, "tabView");
         TabViewEngineSession.Client var5 = this.es.getEngineSessionClient();
         boolean var4;
         if (var5 != null) {
            var4 = var5.onShowFileChooser(this.es, var2, var3);
         } else {
            var4 = false;
         }

         return var4;
      }
   }

   public interface Client {
      boolean handleExternalUrl(String var1);

      void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3);

      boolean onShowFileChooser(TabViewEngineSession var1, ValueCallback var2, FileChooserParams var3);

      void updateFailingUrl(String var1, boolean var2);
   }

   public static final class DownloadCallback implements org.mozilla.rocket.tabs.web.DownloadCallback {
      private final TabViewEngineSession es;

      public DownloadCallback(TabViewEngineSession var1) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         super();
         this.es = var1;
      }

      public void onDownloadStart(final Download var1) {
         Intrinsics.checkParameterIsNotNull(var1, "download");
         final String var2 = CookieManager.getInstance().getCookie(var1.getUrl());
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               String var2x = var1.getUrl();
               Intrinsics.checkExpressionValueIsNotNull(var2x, "download.url");
               var1x.onExternalResource(var2x, var1.getName(), var1.getContentLength(), var1.getMimeType(), var2, var1.getUserAgent());
            }
         }));
      }
   }

   public static final class FindListener implements TabView.FindListener {
      private final TabViewEngineSession es;

      public FindListener(TabViewEngineSession var1) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         super();
         this.es = var1;
      }

      public void onFindResultReceived(final int var1, final int var2, final boolean var3) {
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               var1x.onFindResult(var1, var2, var3);
            }
         }));
      }
   }

   public interface Observer extends EngineSession.Observer {
      void onEnterFullScreen(TabView.FullscreenCallback var1, View var2);

      void onExitFullScreen();

      void onGeolocationPermissionsShowPrompt(String var1, Callback var2);

      void onLongPress(TabView.HitTarget var1);

      void onReceivedIcon(Bitmap var1);
   }

   public static final class ViewClient extends TabViewClient {
      private final TabViewEngineSession es;

      public ViewClient(TabViewEngineSession var1) {
         Intrinsics.checkParameterIsNotNull(var1, "es");
         super();
         this.es = var1;
      }

      public boolean handleExternalUrl(String var1) {
         TabViewEngineSession.Client var2 = this.es.getEngineSessionClient();
         boolean var3;
         if (var2 != null) {
            var3 = var2.handleExternalUrl(var1);
         } else {
            var3 = false;
         }

         return var3;
      }

      public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1, String var2, String var3) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         TabViewEngineSession.Client var4 = this.es.getEngineSessionClient();
         if (var4 != null) {
            var4.onHttpAuthRequest(var1, var2, var3);
         }

      }

      public void onPageFinished(final boolean var1) {
         this.es.notifyObservers((Function1)null.INSTANCE);
         this.es.notifyObservers((Function1)(new Function1() {
            public final void invoke(TabViewEngineSession.Observer var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
               EngineSession.Observer.DefaultImpls.onSecurityChange$default(var1x, var1, (String)null, (String)null, 6, (Object)null);
            }
         }));
         final TabView var2 = this.es.getTabView();
         if (var2 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onNavigationStateChange(var2.canGoBack(), var2.canGoForward());
               }
            }));
         }

      }

      public void onPageStarted(final String var1) {
         this.es.notifyObservers((Function1)null.INSTANCE);
         if (var1 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1x) {
                  Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
                  var1x.onLocationChange(var1);
               }
            }));
         }

         final TabView var2 = this.es.getTabView();
         if (var2 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1) {
                  Intrinsics.checkParameterIsNotNull(var1, "receiver$0");
                  var1.onNavigationStateChange(var2.canGoBack(), var2.canGoForward());
               }
            }));
         }

      }

      public void onURLChanged(final String var1) {
         if (var1 != null) {
            this.es.notifyObservers((Function1)(new Function1() {
               public final void invoke(TabViewEngineSession.Observer var1x) {
                  Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
                  var1x.onLocationChange(var1);
               }
            }));
         }

      }

      public void updateFailingUrl(String var1, boolean var2) {
         TabViewEngineSession.Client var3 = this.es.getEngineSessionClient();
         if (var3 != null) {
            var3.updateFailingUrl(var1, var2);
         }

      }
   }

   public interface WindowClient {
      void onCloseWindow(TabViewEngineSession var1);

      boolean onCreateWindow(boolean var1, boolean var2, Message var3);
   }
}
