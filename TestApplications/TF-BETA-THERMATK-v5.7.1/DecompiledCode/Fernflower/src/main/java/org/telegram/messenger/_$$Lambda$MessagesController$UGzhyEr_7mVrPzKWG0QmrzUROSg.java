package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg implements MessagesStorage.IntCallback {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.Dialog f$1;
   // $FF: synthetic field
   private final long f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$UGzhyEr_7mVrPzKWG0QmrzUROSg(MessagesController var1, TLRPC.Dialog var2, long var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(int var1) {
      this.f$0.lambda$updateInterfaceWithMessages$255$MessagesController(this.f$1, this.f$2, var1);
   }
}
