package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.Dialog f$2;
   // $FF: synthetic field
   private final long f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$LzTCLk4PcU0AIVN_3fmrcJA4k9I(MessagesController var1, int var2, TLRPC.Dialog var3, long var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$checkLastDialogMessage$136$MessagesController(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
