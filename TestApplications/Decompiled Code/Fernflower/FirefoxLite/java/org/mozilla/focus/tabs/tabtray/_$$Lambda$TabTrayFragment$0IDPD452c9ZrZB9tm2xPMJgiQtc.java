package org.mozilla.focus.tabs.tabtray;

import org.mozilla.rocket.tabs.Session;

// $FF: synthetic class
public final class _$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc implements Runnable {
   // $FF: synthetic field
   private final TabTrayFragment f$0;
   // $FF: synthetic field
   private final Session f$1;

   // $FF: synthetic method
   public _$$Lambda$TabTrayFragment$0IDPD452c9ZrZB9tm2xPMJgiQtc(TabTrayFragment var1, Session var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      TabTrayFragment.lambda$refreshData$1(this.f$0, this.f$1);
   }
}
