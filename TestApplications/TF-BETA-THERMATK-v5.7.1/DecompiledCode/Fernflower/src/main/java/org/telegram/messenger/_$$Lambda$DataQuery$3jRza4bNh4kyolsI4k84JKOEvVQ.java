package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$3jRza4bNh4kyolsI4k84JKOEvVQ implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final String f$3;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$3jRza4bNh4kyolsI4k84JKOEvVQ(DataQuery var1, int var2, String var3, String var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$115$DataQuery(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
