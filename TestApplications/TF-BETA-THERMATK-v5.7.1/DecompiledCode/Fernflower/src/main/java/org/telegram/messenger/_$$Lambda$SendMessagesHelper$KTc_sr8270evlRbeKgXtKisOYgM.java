package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$KTc_sr8270evlRbeKgXtKisOYgM implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_sendMultiMedia f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$5;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$KTc_sr8270evlRbeKgXtKisOYgM(SendMessagesHelper var1, ArrayList var2, TLRPC.TL_messages_sendMultiMedia var3, ArrayList var4, ArrayList var5, SendMessagesHelper.DelayedMessage var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$performSendMessageRequestMulti$29$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1, var2);
   }
}
