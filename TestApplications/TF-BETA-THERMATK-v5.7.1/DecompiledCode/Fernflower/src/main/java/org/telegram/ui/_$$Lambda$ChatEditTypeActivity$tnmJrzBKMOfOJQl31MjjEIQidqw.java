package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$tnmJrzBKMOfOJQl31MjjEIQidqw implements RequestDelegate {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$tnmJrzBKMOfOJQl31MjjEIQidqw(ChatEditTypeActivity var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$18$ChatEditTypeActivity(this.f$1, var1, var2);
   }
}
