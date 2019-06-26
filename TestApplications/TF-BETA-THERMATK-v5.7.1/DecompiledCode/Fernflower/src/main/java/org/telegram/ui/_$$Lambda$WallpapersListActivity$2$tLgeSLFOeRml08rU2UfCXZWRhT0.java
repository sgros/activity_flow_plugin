package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0 implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final int[] f$1;

   // $FF: synthetic method
   public _$$Lambda$WallpapersListActivity$2$tLgeSLFOeRml08rU2UfCXZWRhT0(Object var1, int[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$1$WallpapersListActivity$2(this.f$1, var1, var2);
   }
}
