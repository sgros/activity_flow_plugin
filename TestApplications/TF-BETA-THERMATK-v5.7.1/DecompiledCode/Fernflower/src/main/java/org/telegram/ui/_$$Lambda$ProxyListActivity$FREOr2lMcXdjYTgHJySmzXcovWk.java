package org.telegram.ui;

import org.telegram.messenger.SharedConfig;
import org.telegram.tgnet.RequestTimeDelegate;

// $FF: synthetic class
public final class _$$Lambda$ProxyListActivity$FREOr2lMcXdjYTgHJySmzXcovWk implements RequestTimeDelegate {
   // $FF: synthetic field
   private final SharedConfig.ProxyInfo f$0;

   // $FF: synthetic method
   public _$$Lambda$ProxyListActivity$FREOr2lMcXdjYTgHJySmzXcovWk(SharedConfig.ProxyInfo var1) {
      this.f$0 = var1;
   }

   public final void run(long var1) {
      ProxyListActivity.lambda$checkProxyList$4(this.f$0, var1);
   }
}
