package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$rxB6ZCv_wieivWsHR6TFAEUSbcc implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.Chat f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$rxB6ZCv_wieivWsHR6TFAEUSbcc(MessagesController var1, long var2, TLRPC.Chat var4, ArrayList var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$reloadMessages$22$MessagesController(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
