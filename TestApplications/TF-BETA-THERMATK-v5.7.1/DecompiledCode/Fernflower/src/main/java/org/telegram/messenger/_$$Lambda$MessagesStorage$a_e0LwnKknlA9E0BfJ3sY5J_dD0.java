package org.telegram.messenger;

import android.util.SparseIntArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$a_e0LwnKknlA9E0BfJ3sY5J_dD0 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final SparseIntArray f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$a_e0LwnKknlA9E0BfJ3sY5J_dD0(MessagesStorage var1, boolean var2, SparseIntArray var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putBlockedUsers$40$MessagesStorage(this.f$1, this.f$2);
   }
}
