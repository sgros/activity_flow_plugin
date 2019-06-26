package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$15$6A8CAzbPtjUbeALHm7vOtlzkUV4 implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final MessagesStorage f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$15$6A8CAzbPtjUbeALHm7vOtlzkUV4(Object var1, TLObject var2, TLRPC.TL_error var3, MessagesStorage var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$1$ChatActivity$15(this.f$1, this.f$2, this.f$3);
   }
}
