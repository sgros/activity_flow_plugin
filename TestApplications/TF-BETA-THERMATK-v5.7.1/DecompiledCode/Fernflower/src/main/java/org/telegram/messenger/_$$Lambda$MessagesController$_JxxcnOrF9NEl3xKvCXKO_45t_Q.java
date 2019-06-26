package org.telegram.messenger;

import android.util.LongSparseArray;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$_JxxcnOrF9NEl3xKvCXKO_45t_Q implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$1;
   // $FF: synthetic field
   private final LongSparseArray f$2;
   // $FF: synthetic field
   private final LongSparseArray f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$_JxxcnOrF9NEl3xKvCXKO_45t_Q(MessagesController var1, TLRPC.messages_Dialogs var2, LongSparseArray var3, LongSparseArray var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$121$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
