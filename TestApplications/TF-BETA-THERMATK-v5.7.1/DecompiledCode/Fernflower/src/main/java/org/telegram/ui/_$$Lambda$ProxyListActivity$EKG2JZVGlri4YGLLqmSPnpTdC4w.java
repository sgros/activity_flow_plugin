package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.SharedConfig;

// $FF: synthetic class
public final class _$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w implements OnClickListener {
   // $FF: synthetic field
   private final ProxyListActivity f$0;
   // $FF: synthetic field
   private final SharedConfig.ProxyInfo f$1;

   // $FF: synthetic method
   public _$$Lambda$ProxyListActivity$EKG2JZVGlri4YGLLqmSPnpTdC4w(ProxyListActivity var1, SharedConfig.ProxyInfo var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$1$ProxyListActivity(this.f$1, var1, var2);
   }
}
