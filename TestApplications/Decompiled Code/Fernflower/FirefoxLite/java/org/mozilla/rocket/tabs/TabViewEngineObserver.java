package org.mozilla.rocket.tabs;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.webkit.GeolocationPermissions.Callback;
import java.util.Collection;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import mozilla.components.browser.session.Download;
import mozilla.components.support.base.observer.Consumable;

public final class TabViewEngineObserver implements TabViewEngineSession.Observer {
   private final Session session;

   public TabViewEngineObserver(Session var1) {
      Intrinsics.checkParameterIsNotNull(var1, "session");
      super();
      this.session = var1;
   }

   public final Session getSession() {
      return this.session;
   }

   public void onEnterFullScreen(final TabView.FullscreenCallback var1, final View var2) {
      Intrinsics.checkParameterIsNotNull(var1, "callback");
      this.session.notifyObservers((Function1)(new Function1() {
         public final void invoke(Session.Observer var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
            var1x.onEnterFullScreen(var1, var2);
         }
      }));
   }

   public void onExitFullScreen() {
      this.session.notifyObservers((Function1)null.INSTANCE);
   }

   public void onExternalResource(String var1, String var2, Long var3, String var4, String var5, String var6) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      var5 = Environment.DIRECTORY_DOWNLOADS;
      Intrinsics.checkExpressionValueIsNotNull(var5, "Environment.DIRECTORY_DOWNLOADS");
      Download var7 = new Download(var1, var2, var4, var3, var6, var5);
      this.session.setDownload(Consumable.Companion.from(var7));
   }

   public void onFindResult(int var1, int var2, boolean var3) {
      Session var4 = this.session;
      var4.setFindResults(CollectionsKt.plus((Collection)var4.getFindResults(), new mozilla.components.browser.session.Session.FindResult(var1, var2, var3)));
   }

   public void onGeolocationPermissionsShowPrompt(final String var1, final Callback var2) {
      Intrinsics.checkParameterIsNotNull(var1, "origin");
      this.session.notifyObservers((Function1)(new Function1() {
         public final void invoke(Session.Observer var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
            var1x.onGeolocationPermissionsShowPrompt(var1, var2);
         }
      }));
   }

   public void onLoadingStateChange(boolean var1) {
      this.session.setLoading(var1);
   }

   public void onLocationChange(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "url");
      this.session.setUrl(var1);
   }

   public void onLongPress(final TabView.HitTarget var1) {
      Intrinsics.checkParameterIsNotNull(var1, "hitTarget");
      this.session.notifyObservers((Function1)(new Function1() {
         public final void invoke(Session.Observer var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
            var1x.onLongPress(TabViewEngineObserver.this.getSession(), var1);
         }
      }));
   }

   public void onNavigationStateChange(Boolean var1, Boolean var2) {
      if (var1 != null) {
         var1;
         this.session.setCanGoBack(var1);
      }

      if (var2 != null) {
         var2;
         this.session.setCanGoForward(var2);
      }

   }

   public void onProgress(int var1) {
      this.session.setProgress(var1);
   }

   public void onReceivedIcon(final Bitmap var1) {
      this.session.setFavicon(var1);
      this.session.notifyObservers((Function1)(new Function1() {
         public final void invoke(Session.Observer var1x) {
            Intrinsics.checkParameterIsNotNull(var1x, "receiver$0");
            var1x.onReceivedIcon(var1);
         }
      }));
   }

   public void onSecurityChange(boolean var1, String var2, String var3) {
      Session var4 = this.session;
      if (var2 == null) {
         var2 = "";
      }

      if (var3 == null) {
         var3 = "";
      }

      var4.setSecurityInfo(new mozilla.components.browser.session.Session.SecurityInfo(var1, var2, var3));
   }

   public void onTitleChange(String var1) {
      Intrinsics.checkParameterIsNotNull(var1, "title");
      this.session.setTitle(var1);
   }
}
