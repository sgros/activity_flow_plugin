package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$re3lVjnmONchziB0xgSqaVhxV5Q implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_editChatAdmin f$1;
   // $FF: synthetic field
   private final RequestDelegate f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$re3lVjnmONchziB0xgSqaVhxV5Q(MessagesController var1, TLRPC.TL_messages_editChatAdmin var2, RequestDelegate var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$setUserAdminRole$52$MessagesController(this.f$1, this.f$2);
   }
}
