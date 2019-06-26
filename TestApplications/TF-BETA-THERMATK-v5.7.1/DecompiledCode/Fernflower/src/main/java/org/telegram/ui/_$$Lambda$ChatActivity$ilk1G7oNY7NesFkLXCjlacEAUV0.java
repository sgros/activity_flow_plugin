package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$ilk1G7oNY7NesFkLXCjlacEAUV0 implements RequestDelegate {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$ilk1G7oNY7NesFkLXCjlacEAUV0(ChatActivity var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$87$ChatActivity(this.f$1, var1, var2);
   }
}
