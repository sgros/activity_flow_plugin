package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$4H8dDsDRj446uqFYh4DKm4UlDEU implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final MessageObject f$4;
   // $FF: synthetic field
   private final TLRPC.KeyboardButton f$5;
   // $FF: synthetic field
   private final ChatActivity f$6;
   // $FF: synthetic field
   private final TLObject[] f$7;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$4H8dDsDRj446uqFYh4DKm4UlDEU(SendMessagesHelper var1, String var2, boolean var3, TLObject var4, MessageObject var5, TLRPC.KeyboardButton var6, ChatActivity var7, TLObject[] var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run() {
      this.f$0.lambda$null$16$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
