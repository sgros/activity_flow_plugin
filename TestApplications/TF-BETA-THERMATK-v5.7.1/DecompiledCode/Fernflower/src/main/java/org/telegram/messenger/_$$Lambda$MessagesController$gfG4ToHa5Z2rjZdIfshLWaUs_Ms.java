package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$gfG4ToHa5Z2rjZdIfshLWaUs_Ms implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$gfG4ToHa5Z2rjZdIfshLWaUs_Ms(MessagesController var1, long var2, TLRPC.Chat var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadUnknownChannel$195$MessagesController(this.f$1, this.f$2, var1, var2);
   }
}
