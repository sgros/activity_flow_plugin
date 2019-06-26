package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoadOperation$AloCOvGHlndklEjA6lccwgvlez8 implements RequestDelegate {
   // $FF: synthetic field
   private final FileLoadOperation f$0;

   // $FF: synthetic method
   public _$$Lambda$FileLoadOperation$AloCOvGHlndklEjA6lccwgvlez8(FileLoadOperation var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$requestFileOffsets$8$FileLoadOperation(var1, var2);
   }
}
