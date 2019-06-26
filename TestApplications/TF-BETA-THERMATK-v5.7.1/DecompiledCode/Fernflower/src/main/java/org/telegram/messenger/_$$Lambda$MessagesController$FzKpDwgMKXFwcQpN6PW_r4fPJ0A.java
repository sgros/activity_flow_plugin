package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW_r4fPJ0A implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final TLRPC.InputUser f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW_r4fPJ0A(MessagesController var1, TLRPC.TL_error var2, BaseFragment var3, TLObject var4, boolean var5, boolean var6, TLRPC.InputUser var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$178$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
