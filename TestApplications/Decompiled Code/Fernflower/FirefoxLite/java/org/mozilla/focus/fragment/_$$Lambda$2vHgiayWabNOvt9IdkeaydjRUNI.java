package org.mozilla.focus.fragment;

import org.mozilla.focus.web.HttpAuthenticationDialogBuilder;
import org.mozilla.rocket.tabs.TabViewClient;

// $FF: synthetic class
public final class _$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI implements HttpAuthenticationDialogBuilder.CancelListener {
   // $FF: synthetic field
   private final TabViewClient.HttpAuthCallback f$0;

   // $FF: synthetic method
   public _$$Lambda$2vHgiayWabNOvt9IdkeaydjRUNI(TabViewClient.HttpAuthCallback var1) {
      this.f$0 = var1;
   }

   public final void onCancel() {
      this.f$0.cancel();
   }
}
