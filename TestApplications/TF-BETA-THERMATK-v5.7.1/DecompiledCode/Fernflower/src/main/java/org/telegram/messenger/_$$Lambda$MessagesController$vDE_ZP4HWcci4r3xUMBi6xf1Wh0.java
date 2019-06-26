package org.telegram.messenger;

import android.util.SparseArray;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$vDE_ZP4HWcci4r3xUMBi6xf1Wh0 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_ChannelDifference f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.Chat f$3;
   // $FF: synthetic field
   private final SparseArray f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final long f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$vDE_ZP4HWcci4r3xUMBi6xf1Wh0(MessagesController var1, TLRPC.updates_ChannelDifference var2, int var3, TLRPC.Chat var4, SparseArray var5, int var6, long var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$202$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
