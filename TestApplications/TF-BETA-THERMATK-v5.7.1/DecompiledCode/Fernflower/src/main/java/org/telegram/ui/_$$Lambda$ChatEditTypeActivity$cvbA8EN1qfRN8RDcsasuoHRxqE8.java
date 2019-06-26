package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$cvbA8EN1qfRN8RDcsasuoHRxqE8 implements RequestDelegate {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$cvbA8EN1qfRN8RDcsasuoHRxqE8(ChatEditTypeActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onFragmentCreate$2$ChatEditTypeActivity(var1, var2);
   }
}
