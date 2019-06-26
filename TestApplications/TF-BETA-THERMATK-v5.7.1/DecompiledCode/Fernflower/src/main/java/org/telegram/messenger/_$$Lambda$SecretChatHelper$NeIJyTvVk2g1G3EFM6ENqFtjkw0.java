package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SecretChatHelper$NeIJyTvVk2g1G3EFM6ENqFtjkw0 implements RequestDelegate {
   // $FF: synthetic field
   private final SecretChatHelper f$0;
   // $FF: synthetic field
   private final TLRPC.DecryptedMessage f$1;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$2;
   // $FF: synthetic field
   private final TLRPC.Message f$3;
   // $FF: synthetic field
   private final MessageObject f$4;
   // $FF: synthetic field
   private final String f$5;

   // $FF: synthetic method
   public _$$Lambda$SecretChatHelper$NeIJyTvVk2g1G3EFM6ENqFtjkw0(SecretChatHelper var1, TLRPC.DecryptedMessage var2, TLRPC.EncryptedChat var3, TLRPC.Message var4, MessageObject var5, String var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$6$SecretChatHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, var1, var2);
   }
}
