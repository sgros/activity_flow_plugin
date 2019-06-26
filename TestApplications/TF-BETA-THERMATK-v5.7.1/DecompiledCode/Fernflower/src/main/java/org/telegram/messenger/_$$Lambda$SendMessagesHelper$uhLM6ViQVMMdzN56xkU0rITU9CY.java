package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final MessageObject f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final Runnable f$3;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$uhLM6ViQVMMdzN56xkU0rITU9CY(SendMessagesHelper var1, MessageObject var2, String var3, Runnable var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$sendVote$15$SendMessagesHelper(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
