package org.telegram.messenger;

import android.util.SparseIntArray;
import org.telegram.messenger.support.SparseLongArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$7M1fsQyOLqfQ09O_qH6xF5Wf9zk implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final SparseLongArray f$1;
   // $FF: synthetic field
   private final SparseLongArray f$2;
   // $FF: synthetic field
   private final SparseIntArray f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$7M1fsQyOLqfQ09O_qH6xF5Wf9zk(MessagesStorage var1, SparseLongArray var2, SparseLongArray var3, SparseIntArray var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$markMessagesAsRead$130$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
