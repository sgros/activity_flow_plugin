package org.telegram.messenger;

import org.telegram.messenger.-..Lambda.MessagesController.APiRxvKDu_B4Za4CATjRgQ8eiYM;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM implements RequestDelegate {
   // $FF: synthetic field
   public static final APiRxvKDu_B4Za4CATjRgQ8eiYM INSTANCE = new _$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM();

   // $FF: synthetic method
   private _$$Lambda$MessagesController$APiRxvKDu_B4Za4CATjRgQ8eiYM() {
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      MessagesController.lambda$markMentionMessageAsRead$142(var1, var2);
   }
}
