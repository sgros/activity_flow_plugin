package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$YgCXdrvaSdGA1hf5LiH9nJofnsg(MessagesStorage var1, TLRPC.User var2, boolean var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$loadUserInfo$73$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
