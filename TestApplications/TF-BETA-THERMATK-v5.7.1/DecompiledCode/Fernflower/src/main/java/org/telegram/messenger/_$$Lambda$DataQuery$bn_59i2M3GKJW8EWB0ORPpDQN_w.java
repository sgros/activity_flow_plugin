package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$bn_59i2M3GKJW8EWB0ORPpDQN_w implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$bn_59i2M3GKJW8EWB0ORPpDQN_w(DataQuery var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPinnedMessageInternal$87$DataQuery(this.f$1, var1, var2);
   }
}