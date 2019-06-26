package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$48_HP_7TQqG7f2oWszU7R3aCenM implements RequestDelegate {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$48_HP_7TQqG7f2oWszU7R3aCenM(SecretChatHelper var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$21$SecretChatHelper(this.f$1, var1, var2);
   }
}
