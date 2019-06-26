package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ChatActivity;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final TLRPC.KeyboardButton f$4;
   // $FF: synthetic field
   private final ChatActivity f$5;
   // $FF: synthetic field
   private final TLObject[] f$6;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY(SendMessagesHelper var1, String var2, boolean var3, MessageObject var4, TLRPC.KeyboardButton var5, ChatActivity var6, TLObject[] var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$sendCallback$17$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}
