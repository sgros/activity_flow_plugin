package org.telegram.messenger;

import org.telegram.messenger.-..Lambda.MessagesController.GZZLBgVSubugJqEDeBHWQk3eSTE;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE implements RequestDelegate {
   // $FF: synthetic field
   public static final GZZLBgVSubugJqEDeBHWQk3eSTE INSTANCE = new _$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE();

   // $FF: synthetic method
   private _$$Lambda$MessagesController$GZZLBgVSubugJqEDeBHWQk3eSTE() {
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      MessagesController.lambda$markMessageContentAsRead$140(var1, var2);
   }
}
