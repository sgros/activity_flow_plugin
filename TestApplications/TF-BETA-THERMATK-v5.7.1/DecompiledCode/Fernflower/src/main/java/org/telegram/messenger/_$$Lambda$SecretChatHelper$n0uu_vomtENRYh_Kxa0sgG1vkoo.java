package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$n0uu_vomtENRYh_Kxa0sgG1vkoo(SecretChatHelper var1, TLRPC.EncryptedChat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$applyPeerLayer$8$SecretChatHelper(this.f$1);
   }
}
