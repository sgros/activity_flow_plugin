package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$wWMmlnBYX7ztuC_F9UPquAEbR_s implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;
   // $FF: synthetic field
   private final TLRPC.Dialog f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$wWMmlnBYX7ztuC_F9UPquAEbR_s(MessagesStorage var1, TLRPC.EncryptedChat var2, TLRPC.User var3, TLRPC.Dialog var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$putEncryptedChat$111$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
