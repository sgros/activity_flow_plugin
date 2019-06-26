package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$sHevIZGEf9jGeQ0PmpgZfE_2SOQ implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$sHevIZGEf9jGeQ0PmpgZfE_2SOQ(MessagesController var1, TLRPC.User var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadFullUser$20$MessagesController(this.f$1, this.f$2, var1, var2);
   }
}
