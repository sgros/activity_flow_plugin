package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final TLRPC.InputMedia f$2;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$3;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$TtlUVA9H7pRWgwj4hEtQKuSUaGg(SendMessagesHelper var1, TLObject var2, TLRPC.InputMedia var3, SendMessagesHelper.DelayedMessage var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$19$SendMessagesHelper(this.f$1, this.f$2, this.f$3);
   }
}
