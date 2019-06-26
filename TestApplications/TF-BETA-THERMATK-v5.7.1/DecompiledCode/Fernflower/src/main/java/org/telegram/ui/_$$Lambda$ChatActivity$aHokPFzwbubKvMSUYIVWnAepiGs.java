package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$aHokPFzwbubKvMSUYIVWnAepiGs implements RequestDelegate {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_messages_getWebPagePreview f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$aHokPFzwbubKvMSUYIVWnAepiGs(ChatActivity var1, TLRPC.TL_messages_getWebPagePreview var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$50$ChatActivity(this.f$1, var1, var2);
   }
}
