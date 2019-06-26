package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final TLRPC.InputUser f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final BaseFragment f$4;
   // $FF: synthetic field
   private final TLObject f$5;
   // $FF: synthetic field
   private final boolean f$6;
   // $FF: synthetic field
   private final Runnable f$7;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4(MessagesController var1, boolean var2, TLRPC.InputUser var3, int var4, BaseFragment var5, TLObject var6, boolean var7, Runnable var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$addUserToChat$180$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, var1, var2);
   }
}
