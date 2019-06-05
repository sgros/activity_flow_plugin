package org.mozilla.focus.fragment;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.rocket.tabs.TabViewClient;

// $FF: synthetic class
public final class _$$Lambda$BrowserFragment$SessionObserver$3ofKWXqzOJQXPDqF9Nkl_b0cv_s implements HttpAuthenticationDialogBuilder.OkListener {
   // $FF: synthetic field
   private final TabViewClient.HttpAuthCallback f$0;

   // $FF: synthetic method
   public _$$Lambda$BrowserFragment$SessionObserver$3ofKWXqzOJQXPDqF9Nkl_b0cv_s(TabViewClient.HttpAuthCallback var1) {
      this.f$0 = var1;
   }

   public final void onOk(String var1, String var2, String var3, String var4) {
      BrowserFragment.SessionObserver.lambda$onHttpAuthRequest$0(this.f$0, var1, var2, var3, var4);
   }
}
