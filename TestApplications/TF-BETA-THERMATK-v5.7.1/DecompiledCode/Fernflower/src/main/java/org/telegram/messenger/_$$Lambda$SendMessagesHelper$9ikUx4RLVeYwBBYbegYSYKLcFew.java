package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$9ikUx4RLVeYwBBYbegYSYKLcFew implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.InputMedia f$1;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$2;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$9ikUx4RLVeYwBBYbegYSYKLcFew(SendMessagesHelper var1, TLRPC.InputMedia var2, SendMessagesHelper.DelayedMessage var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$uploadMultiMedia$20$SendMessagesHelper(this.f$1, this.f$2, var1, var2);
   }
}
