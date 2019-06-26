package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$earKZXcnSlbNSw2ZGT5Q1z7WPUw implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$earKZXcnSlbNSw2ZGT5Q1z7WPUw(MessagesController var1, long var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadPeerSettings$27$MessagesController(this.f$1, var1, var2);
   }
}
