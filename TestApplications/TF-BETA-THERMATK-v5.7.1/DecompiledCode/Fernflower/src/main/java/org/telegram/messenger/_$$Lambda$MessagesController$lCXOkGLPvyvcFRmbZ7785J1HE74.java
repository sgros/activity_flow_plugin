package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$lCXOkGLPvyvcFRmbZ7785J1HE74 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.updates_ChannelDifference f$3;
   // $FF: synthetic field
   private final TLRPC.Chat f$4;
   // $FF: synthetic field
   private final SparseArray f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final long f$7;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$lCXOkGLPvyvcFRmbZ7785J1HE74(MessagesController var1, ArrayList var2, int var3, TLRPC.updates_ChannelDifference var4, TLRPC.Chat var5, SparseArray var6, int var7, long var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run() {
      this.f$0.lambda$null$203$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
