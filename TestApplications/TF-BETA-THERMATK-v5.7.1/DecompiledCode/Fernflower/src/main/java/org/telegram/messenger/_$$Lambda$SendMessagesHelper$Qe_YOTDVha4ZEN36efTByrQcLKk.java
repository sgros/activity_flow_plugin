package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final Object f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final String f$4;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$5;
   // $FF: synthetic field
   private final boolean f$6;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$7;
   // $FF: synthetic field
   private final TLRPC.Message f$8;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$Qe_YOTDVha4ZEN36efTByrQcLKk(SendMessagesHelper var1, TLObject var2, Object var3, MessageObject var4, String var5, SendMessagesHelper.DelayedMessage var6, boolean var7, SendMessagesHelper.DelayedMessage var8, TLRPC.Message var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$performSendMessageRequest$40$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, var1, var2);
   }
}
