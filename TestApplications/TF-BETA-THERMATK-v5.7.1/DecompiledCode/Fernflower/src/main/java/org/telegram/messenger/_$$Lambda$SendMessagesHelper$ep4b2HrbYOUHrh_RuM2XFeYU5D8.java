package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$ep4b2HrbYOUHrh_RuM2XFeYU5D8 implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.Message f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final TLObject f$4;
   // $FF: synthetic field
   private final MessageObject f$5;
   // $FF: synthetic field
   private final String f$6;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$ep4b2HrbYOUHrh_RuM2XFeYU5D8(SendMessagesHelper var1, TLRPC.TL_error var2, TLRPC.Message var3, TLObject var4, TLObject var5, MessageObject var6, String var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$39$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}