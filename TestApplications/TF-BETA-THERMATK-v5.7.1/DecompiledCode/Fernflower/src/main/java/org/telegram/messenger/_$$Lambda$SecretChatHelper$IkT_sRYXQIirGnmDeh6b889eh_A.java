package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$IkT_sRYXQIirGnmDeh6b889eh_A implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Dialog f$1;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$IkT_sRYXQIirGnmDeh6b889eh_A(SecretChatHelper var1, TLRPC.Dialog var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$processUpdateEncryption$1$SecretChatHelper(this.f$1);
   }
}
