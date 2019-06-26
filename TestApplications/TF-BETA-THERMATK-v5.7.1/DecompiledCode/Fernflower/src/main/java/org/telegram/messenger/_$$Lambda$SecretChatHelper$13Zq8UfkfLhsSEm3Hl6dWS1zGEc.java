package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$13Zq8UfkfLhsSEm3Hl6dWS1zGEc implements RequestDelegate {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$13Zq8UfkfLhsSEm3Hl6dWS1zGEc(SecretChatHelper var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$acceptSecretChat$22$SecretChatHelper(this.f$1, var1, var2);
   }
}
