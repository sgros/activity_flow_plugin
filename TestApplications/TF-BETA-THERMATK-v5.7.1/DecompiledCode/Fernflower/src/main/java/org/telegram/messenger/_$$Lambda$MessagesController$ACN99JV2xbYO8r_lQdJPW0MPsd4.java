package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ACN99JV2xbYO8r_lQdJPW0MPsd4 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.updates_Difference f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final SparseArray f$3;
   // $FF: synthetic field
   private final SparseArray f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ACN99JV2xbYO8r_lQdJPW0MPsd4(MessagesController var1, TLRPC.updates_Difference var2, ArrayList var3, SparseArray var4, SparseArray var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$213$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
