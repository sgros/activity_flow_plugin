package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final TLRPC.InputPeer f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$GgsMvhuFaAXbZ2H0PLOXM_0uOo4(MessagesController var1, long var2, long var4, int var6, int var7, boolean var8, TLRPC.InputPeer var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var6;
      this.f$4 = var7;
      this.f$5 = var8;
      this.f$6 = var9;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$deleteDialog$70$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}
