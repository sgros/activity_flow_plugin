package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI implements RequestDelegate {
   // $FF: synthetic field
   private final CallLogActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$CallLogActivity$YB5WYkkG7xwD7BaORAcGJNp4ptI(CallLogActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$getCalls$6$CallLogActivity(var1, var2);
   }
}
