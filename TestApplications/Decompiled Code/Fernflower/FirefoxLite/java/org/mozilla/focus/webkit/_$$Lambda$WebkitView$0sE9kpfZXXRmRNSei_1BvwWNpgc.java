package org.mozilla.focus.webkit;

import android.webkit.ValueCallback;

// $FF: synthetic class
public final class _$$Lambda$WebkitView$0sE9kpfZXXRmRNSei_1BvwWNpgc implements ValueCallback {
   // $FF: synthetic field
   private final WebkitView f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$WebkitView$0sE9kpfZXXRmRNSei_1BvwWNpgc(WebkitView var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onReceiveValue(Object var1) {
      WebkitView.lambda$insertBrowsingHistory$0(this.f$0, this.f$1, (String)var1);
   }
}
