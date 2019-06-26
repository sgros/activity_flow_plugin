package org.telegram.messenger;

import org.telegram.tgnet.NativeByteBuffer;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3__p_EFkrwY3ws implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final NativeByteBuffer f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$TxzHvDLT8O_Bs3__p_EFkrwY3ws(MessagesStorage var1, long var2, NativeByteBuffer var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
   }

   public final void run() {
      this.f$0.lambda$createPendingTask$6$MessagesStorage(this.f$1, this.f$2);
   }
}
