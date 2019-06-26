package org.telegram.messenger;

import android.util.LongSparseArray;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$yD6NgtKkjxhJizLM4eoCQwHgn60 implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$4;
   // $FF: synthetic field
   private final LongSparseArray f$5;
   // $FF: synthetic field
   private final LongSparseArray f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$yD6NgtKkjxhJizLM4eoCQwHgn60(MessagesController var1, int var2, int var3, int var4, TLRPC.messages_Dialogs var5, LongSparseArray var6, LongSparseArray var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$completeDialogsReset$122$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
