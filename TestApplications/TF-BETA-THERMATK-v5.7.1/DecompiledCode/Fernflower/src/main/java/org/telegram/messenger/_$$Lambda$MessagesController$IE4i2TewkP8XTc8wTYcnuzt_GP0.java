package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$IE4i2TewkP8XTc8wTYcnuzt_GP0 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final TLRPC.InputUser f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$IE4i2TewkP8XTc8wTYcnuzt_GP0(MessagesController var1, boolean var2, TLRPC.InputUser var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteUserFromChat$182$MessagesController(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
