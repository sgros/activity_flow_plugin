package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$gZU7uqq34_u60ckwe65AH41ydVA implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final TLRPC.InputPeer f$6;
   // $FF: synthetic field
   private final long f$7;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$gZU7uqq34_u60ckwe65AH41ydVA(MessagesStorage var1, long var2, boolean var4, int var5, int var6, boolean var7, TLRPC.InputPeer var8, long var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
   }

   public final void run() {
      this.f$0.lambda$null$17$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
