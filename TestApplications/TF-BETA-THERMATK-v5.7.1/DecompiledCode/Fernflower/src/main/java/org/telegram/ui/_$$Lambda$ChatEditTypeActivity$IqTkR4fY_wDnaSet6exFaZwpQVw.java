package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatEditTypeActivity$IqTkR4fY_wDnaSet6exFaZwpQVw implements Runnable {
   // $FF: synthetic field
   private final ChatEditTypeActivity f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;

   // $FF: synthetic method
   public _$$Lambda$ChatEditTypeActivity$IqTkR4fY_wDnaSet6exFaZwpQVw(ChatEditTypeActivity var1, String var2, TLRPC.TL_error var3, TLObject var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$17$ChatEditTypeActivity(this.f$1, this.f$2, this.f$3);
   }
}
