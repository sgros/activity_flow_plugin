package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$tDKre2aQQBiVO0S8VAHIlXCNFCM(SecretChatHelper var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processAcceptedSecretChat$17$SecretChatHelper(this.f$1);
   }
}
