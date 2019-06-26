package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoadOperation$_RANhbuGWnPnpbUnhksdq4cjW2c implements RequestDelegate {
   // $FF: synthetic field
   private final FileLoadOperation f$0;
   // $FF: synthetic field
   private final FileLoadOperation.RequestInfo f$1;

   // $FF: synthetic method
   public _$$Lambda$FileLoadOperation$_RANhbuGWnPnpbUnhksdq4cjW2c(FileLoadOperation var1, FileLoadOperation.RequestInfo var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$11$FileLoadOperation(this.f$1, var1, var2);
   }
}
