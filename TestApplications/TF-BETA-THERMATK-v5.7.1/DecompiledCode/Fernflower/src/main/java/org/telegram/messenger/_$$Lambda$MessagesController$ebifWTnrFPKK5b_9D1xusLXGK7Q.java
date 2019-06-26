package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ebifWTnrFPKK5b_9D1xusLXGK7Q implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ebifWTnrFPKK5b_9D1xusLXGK7Q(MessagesController var1, TLRPC.Chat var2, long var3, int var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadFullChat$17$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
