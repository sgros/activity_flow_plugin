package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$kXyPCeVLRJzYiyabGiQYTza0QCE implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.Message f$1;
   // $FF: synthetic field
   private final TLRPC.messages_SentEncryptedMessage f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$kXyPCeVLRJzYiyabGiQYTza0QCE(SecretChatHelper var1, TLRPC.Message var2, TLRPC.messages_SentEncryptedMessage var3, int var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$4$SecretChatHelper(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
