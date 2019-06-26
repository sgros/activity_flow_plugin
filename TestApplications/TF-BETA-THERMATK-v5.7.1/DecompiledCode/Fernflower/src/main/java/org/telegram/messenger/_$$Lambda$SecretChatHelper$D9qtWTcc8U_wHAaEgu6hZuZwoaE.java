package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_encryptedChatDiscarded f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$D9qtWTcc8U_wHAaEgu6hZuZwoaE(SecretChatHelper var1, TLRPC.TL_encryptedChatDiscarded var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processAcceptedSecretChat$18$SecretChatHelper(this.f$1);
   }
}
