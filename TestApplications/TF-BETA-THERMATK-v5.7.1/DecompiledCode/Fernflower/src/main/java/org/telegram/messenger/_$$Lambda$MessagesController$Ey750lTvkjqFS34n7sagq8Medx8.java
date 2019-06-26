package org.telegram.messenger;

import android.util.SparseArray;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$Ey750lTvkjqFS34n7sagq8Medx8 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_Difference f$1;
   // $FF: synthetic field
   private final SparseArray f$2;
   // $FF: synthetic field
   private final SparseArray f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$Ey750lTvkjqFS34n7sagq8Medx8(MessagesController var1, TLRPC.updates_Difference var2, SparseArray var3, SparseArray var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$212$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
