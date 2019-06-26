package org.telegram.ui.Components;

import android.content.Context;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ShareAlert$_zt1k1mc1Pf5YI45UjblkHkDCiI implements RequestDelegate {
   // $FF: synthetic field
   private final ShareAlert f$0;
   // $FF: synthetic field
   private final Context f$1;

   // $FF: synthetic method
   public _$$Lambda$ShareAlert$_zt1k1mc1Pf5YI45UjblkHkDCiI(ShareAlert var1, Context var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$new$1$ShareAlert(this.f$1, var1, var2);
   }
}
