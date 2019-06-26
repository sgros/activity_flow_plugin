package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$9hiJJNQirqjcrHEWZID3qqycfH0 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.TL_chatBannedRights f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$9hiJJNQirqjcrHEWZID3qqycfH0(MessagesStorage var1, int var2, int var3, TLRPC.TL_chatBannedRights var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$updateChatDefaultBannedRights$112$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
