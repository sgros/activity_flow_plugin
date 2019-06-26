package org.telegram.messenger;

import android.util.SparseArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$bHd9VuzZp__o4GfCjtn5be2_y4I implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final SparseArray f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$bHd9VuzZp__o4GfCjtn5be2_y4I(MessagesController var1, SparseArray var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$didAddedNewTask$31$MessagesController(this.f$1);
   }
}
