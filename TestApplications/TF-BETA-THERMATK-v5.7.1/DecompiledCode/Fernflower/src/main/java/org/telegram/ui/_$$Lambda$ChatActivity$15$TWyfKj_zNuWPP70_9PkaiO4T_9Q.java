package org.telegram.ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$15$TWyfKj_zNuWPP70_9PkaiO4T_9Q implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final MessagesStorage f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$15$TWyfKj_zNuWPP70_9PkaiO4T_9Q(Object var1, MessagesStorage var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadLastUnreadMention$2$ChatActivity$15(this.f$1, var1, var2);
   }
}
