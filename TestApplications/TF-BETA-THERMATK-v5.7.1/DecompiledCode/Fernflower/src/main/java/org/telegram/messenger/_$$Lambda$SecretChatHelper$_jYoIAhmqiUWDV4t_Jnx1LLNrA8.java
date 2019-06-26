package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$_jYoIAhmqiUWDV4t_Jnx1LLNrA8 implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$_jYoIAhmqiUWDV4t_Jnx1LLNrA8(SecretChatHelper var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$20$SecretChatHelper(this.f$1);
   }
}
