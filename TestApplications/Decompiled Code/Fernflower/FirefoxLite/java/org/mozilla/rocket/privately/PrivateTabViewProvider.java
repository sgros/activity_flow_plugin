package org.mozilla.rocket.privately;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebSettings;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.mozilla.focus.web.WebViewProvider;
import org.mozilla.rocket.tabs.TabView;
import org.mozilla.rocket.tabs.TabViewProvider;

public final class PrivateTabViewProvider extends TabViewProvider {
   private final Activity host;

   public PrivateTabViewProvider(Activity var1) {
      Intrinsics.checkParameterIsNotNull(var1, "host");
      super();
      this.host = var1;
   }

   public TabView create() {
      View var1 = WebViewProvider.create((Context)this.host, (AttributeSet)null, (WebViewProvider.WebSettingsHook)PrivateTabViewProvider.WebViewSettingsHook.INSTANCE);
      if (var1 != null) {
         return (TabView)var1;
      } else {
         throw new TypeCastException("null cannot be cast to non-null type org.mozilla.rocket.tabs.TabView");
      }
   }

   public static final class WebViewSettingsHook implements WebViewProvider.WebSettingsHook {
      public static final PrivateTabViewProvider.WebViewSettingsHook INSTANCE = new PrivateTabViewProvider.WebViewSettingsHook();

      private WebViewSettingsHook() {
      }

      public void modify(WebSettings var1) {
         if (var1 != null) {
            var1.setSupportMultipleWindows(false);
         }
      }
   }
}
