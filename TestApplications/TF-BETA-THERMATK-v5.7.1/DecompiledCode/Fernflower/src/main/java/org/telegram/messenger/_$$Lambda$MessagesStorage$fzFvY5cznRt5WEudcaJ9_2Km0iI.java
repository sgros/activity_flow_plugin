package org.telegram.messenger;

import android.util.LongSparseArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$fzFvY5cznRt5WEudcaJ9_2Km0iI implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final LongSparseArray f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$fzFvY5cznRt5WEudcaJ9_2Km0iI(MessagesStorage var1, LongSparseArray var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$putWebPages$119$MessagesStorage(this.f$1);
   }
}
