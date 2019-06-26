package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Messages f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final boolean f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$6OdG59RQk4gtHeSIVwqv0igXK90(MessagesStorage var1, TLRPC.messages_Messages var2, int var3, long var4, int var6, boolean var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
      this.f$5 = var7;
   }

   public final void run() {
      this.f$0.lambda$putMessages$138$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
