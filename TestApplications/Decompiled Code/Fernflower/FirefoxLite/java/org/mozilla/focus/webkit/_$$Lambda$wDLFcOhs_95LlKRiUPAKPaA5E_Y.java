package org.mozilla.focus.webkit;

import android.webkit.WebView.FindListener;
import org.mozilla.rocket.tabs.TabView;

// $FF: synthetic class
public final class _$$Lambda$wDLFcOhs_95LlKRiUPAKPaA5E_Y implements FindListener {
   // $FF: synthetic field
   private final TabView.FindListener f$0;

   // $FF: synthetic method
   public _$$Lambda$wDLFcOhs_95LlKRiUPAKPaA5E_Y(TabView.FindListener var1) {
      this.f$0 = var1;
   }

   public final void onFindResultReceived(int var1, int var2, boolean var3) {
      this.f$0.onFindResultReceived(var1, var2, var3);
   }
}
