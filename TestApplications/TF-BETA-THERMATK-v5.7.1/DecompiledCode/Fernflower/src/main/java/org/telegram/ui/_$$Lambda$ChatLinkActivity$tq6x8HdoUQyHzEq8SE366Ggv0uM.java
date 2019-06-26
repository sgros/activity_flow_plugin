package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatLinkActivity$tq6x8HdoUQyHzEq8SE366Ggv0uM implements RequestDelegate {
   // $FF: synthetic field
   private final ChatLinkActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatLinkActivity$tq6x8HdoUQyHzEq8SE366Ggv0uM(ChatLinkActivity var1) {
      this.f$0 = var1;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadChats$15$ChatLinkActivity(var1, var2);
   }
}
