package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$G9V6FvkI_PnA0UumuoB_kxH2lOM implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$2;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$G9V6FvkI_PnA0UumuoB_kxH2lOM(SecretChatHelper var1, TLRPC.EncryptedChat var2, TLRPC.EncryptedChat var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$processUpdateEncryption$2$SecretChatHelper(this.f$1, this.f$2);
   }
}
