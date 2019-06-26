package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$go4ClJO8kMeuzFvRQpPn8_EmO40 implements Runnable {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;
   // $FF: synthetic field
   private final TLRPC.DecryptedMessage f$2;
   // $FF: synthetic field
   private final TLRPC.Message f$3;
   // $FF: synthetic field
   private final TLRPC.InputEncryptedFile f$4;
   // $FF: synthetic field
   private final MessageObject f$5;
   // $FF: synthetic field
   private final String f$6;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$go4ClJO8kMeuzFvRQpPn8_EmO40(SecretChatHelper var1, TLRPC.EncryptedChat var2, TLRPC.DecryptedMessage var3, TLRPC.Message var4, TLRPC.InputEncryptedFile var5, MessageObject var6, String var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$performSendEncryptedRequest$7$SecretChatHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
