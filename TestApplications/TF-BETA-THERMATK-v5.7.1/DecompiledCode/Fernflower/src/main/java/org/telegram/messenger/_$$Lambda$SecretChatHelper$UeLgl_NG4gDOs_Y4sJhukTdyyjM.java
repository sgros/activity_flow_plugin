package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$UeLgl_NG4gDOs_Y4sJhukTdyyjM implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$UeLgl_NG4gDOs_Y4sJhukTdyyjM(SecretChatHelper var1, int var2, TLRPC.EncryptedChat var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$resendMessages$14$SecretChatHelper(this.f$1, this.f$2, this.f$3);
   }
}
