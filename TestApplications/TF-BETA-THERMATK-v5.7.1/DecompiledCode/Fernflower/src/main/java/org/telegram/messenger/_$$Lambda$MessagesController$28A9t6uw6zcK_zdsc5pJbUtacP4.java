package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$28A9t6uw6zcK_zdsc5pJbUtacP4 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_ChannelDifference f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$28A9t6uw6zcK_zdsc5pJbUtacP4(MessagesController var1, TLRPC.updates_ChannelDifference var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$197$MessagesController(this.f$1);
   }
}
