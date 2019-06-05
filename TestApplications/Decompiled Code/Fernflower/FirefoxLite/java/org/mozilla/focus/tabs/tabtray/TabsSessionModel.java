package org.mozilla.focus.tabs.tabtray;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.WebChromeClient.FileChooserParams;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.session.Download;
import org.mozilla.rocket.tabs.Session;
import org.mozilla.rocket.tabs.SessionManager;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewClient;
import org.mozilla.rocket.tabs.TabViewEngineSession;

public final class TabsSessionModel implements TabTrayContract.Model {
   private TabsSessionModel.SessionModelObserver modelObserver;
   private final SessionManager sessionManager;
   private final ArrayList tabs;

   public TabsSessionModel(SessionManager var1) {
      Intrinsics.checkParameterIsNotNull(var1, "sessionManager");
      super();
      this.sessionManager = var1;
      this.tabs = new ArrayList();
   }

   public void clearTabs() {
      Iterator var1 = this.sessionManager.getTabs().iterator();

      while(var1.hasNext()) {
         Session var2 = (Session)var1.next();
         this.sessionManager.dropTab(var2.getId());
      }

   }

   public Session getFocusedTab() {
      return this.sessionManager.getFocusSession();
   }

   public List getTabs() {
      return (List)this.tabs;
   }

   public void loadTabs(TabTrayContract.Model.OnLoadCompleteListener var1) {
      this.tabs.clear();
      this.tabs.addAll((Collection)this.sessionManager.getTabs());
      if (var1 != null) {
         var1.onLoadComplete();
      }

   }

   public void removeTab(int var1) {
      if (var1 >= 0 && var1 < this.tabs.size()) {
         this.sessionManager.dropTab(((Session)this.tabs.get(var1)).getId());
      }

   }

   public void subscribe(final TabTrayContract.Model.Observer var1) {
      Intrinsics.checkParameterIsNotNull(var1, "observer");
      if (this.modelObserver == null) {
         this.modelObserver = (TabsSessionModel.SessionModelObserver)(new TabsSessionModel.SessionModelObserver() {
            public boolean handleExternalUrl(String var1x) {
               return false;
            }

            public void onHttpAuthRequest(TabViewClient.HttpAuthCallback var1x, String var2, String var3) {
               Intrinsics.checkParameterIsNotNull(var1x, "callback");
            }

            public void onSessionCountChanged(int var1x) {
               var1.onUpdate(TabsSessionModel.this.sessionManager.getTabs());
            }

            public boolean onShowFileChooser(TabViewEngineSession var1x, ValueCallback var2, FileChooserParams var3) {
               Intrinsics.checkParameterIsNotNull(var1x, "es");
               return false;
            }

            public void onTabModelChanged$app_focusWebkitRelease(Session var1x) {
               Intrinsics.checkParameterIsNotNull(var1x, "session");
               var1.onTabUpdate(var1x);
            }

            public void updateFailingUrl(String var1x, boolean var2) {
            }
         });
      }

      SessionManager var3 = this.sessionManager;
      TabsSessionModel.SessionModelObserver var2 = this.modelObserver;
      if (var2 != null) {
         var3.register((SessionManager.Observer)var2);
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
      }
   }

   public void switchTab(int var1) {
      if (var1 >= 0 && var1 < this.tabs.size()) {
         Object var2 = this.tabs.get(var1);
         Intrinsics.checkExpressionValueIsNotNull(var2, "tabs[tabPosition]");
         Session var4 = (Session)var2;
         boolean var3;
         if (this.sessionManager.getTabs().indexOf(var4) != -1) {
            var3 = true;
         } else {
            var3 = false;
         }

         if (var3) {
            this.sessionManager.switchToTab(var4.getId());
         }
      }

   }

   public void unsubscribe() {
      if (this.modelObserver != null) {
         SessionManager var1 = this.sessionManager;
         TabsSessionModel.SessionModelObserver var2 = this.modelObserver;
         if (var2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.SessionManager.Observer");
         }

         var1.unregister((SessionManager.Observer)var2);
         this.modelObserver = (TabsSessionModel.SessionModelObserver)null;
      }

   }

   private abstract static class SessionModelObserver implements Session.Observer, SessionManager.Observer {
      private Session session;

      public SessionModelObserver() {
      }

      public boolean onDownload(Session var1, Download var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "download");
         return Session.Observer.DefaultImpls.onDownload(this, var1, var2);
      }

      public void onEnterFullScreen(TabView.FullscreenCallback var1, View var2) {
         Intrinsics.checkParameterIsNotNull(var1, "callback");
         Session.Observer.DefaultImpls.onEnterFullScreen(this, var1, var2);
      }

      public void onExitFullScreen() {
         Session.Observer.DefaultImpls.onExitFullScreen(this);
      }

      public void onFindResult(Session var1, mozilla.components.browser.session.Session.FindResult var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "result");
         Session.Observer.DefaultImpls.onFindResult(this, var1, var2);
      }

      public void onFocusChanged(Session var1, SessionManager.Factor var2) {
         Intrinsics.checkParameterIsNotNull(var2, "factor");
         Session var3 = this.session;
         if (var3 != null) {
            var3.unregister((Session.Observer)this);
         }

         this.session = var1;
         var1 = this.session;
         if (var1 != null) {
            var1.register((Session.Observer)this);
         }

      }

      public void onGeolocationPermissionsShowPrompt(String var1, Callback var2) {
         Intrinsics.checkParameterIsNotNull(var1, "origin");
         Session.Observer.DefaultImpls.onGeolocationPermissionsShowPrompt(this, var1, var2);
      }

      public void onLoadingStateChanged(Session var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Session.Observer.DefaultImpls.onLoadingStateChanged(this, var1, var2);
      }

      public void onLongPress(Session var1, TabView.HitTarget var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Intrinsics.checkParameterIsNotNull(var2, "hitTarget");
         Session.Observer.DefaultImpls.onLongPress(this, var1, var2);
      }

      public void onNavigationStateChanged(Session var1, boolean var2, boolean var3) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Session.Observer.DefaultImpls.onNavigationStateChanged(this, var1, var2, var3);
      }

      public void onProgress(Session var1, int var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Session.Observer.DefaultImpls.onProgress(this, var1, var2);
      }

      public void onReceivedIcon(Bitmap var1) {
         Session var2 = this.session;
         if (var2 != null) {
            this.onTabModelChanged$app_focusWebkitRelease(var2);
         }

      }

      public void onSecurityChanged(Session var1, boolean var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         Session.Observer.DefaultImpls.onSecurityChanged(this, var1, var2);
      }

      public void onSessionAdded(Session var1, Bundle var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         SessionManager.Observer.DefaultImpls.onSessionAdded(this, var1, var2);
      }

      public void onSessionCountChanged(int var1) {
         SessionManager.Observer.DefaultImpls.onSessionCountChanged(this, var1);
      }

      public abstract void onTabModelChanged$app_focusWebkitRelease(Session var1);

      public void onTitleChanged(Session var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         this.onTabModelChanged$app_focusWebkitRelease(var1);
      }

      public void onUrlChanged(Session var1, String var2) {
         Intrinsics.checkParameterIsNotNull(var1, "session");
         this.onTabModelChanged$app_focusWebkitRelease(var1);
      }
   }
}
