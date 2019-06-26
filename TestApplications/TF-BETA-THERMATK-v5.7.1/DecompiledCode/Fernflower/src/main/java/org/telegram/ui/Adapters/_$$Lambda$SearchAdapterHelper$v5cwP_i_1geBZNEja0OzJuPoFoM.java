package org.telegram.ui.Adapters;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SearchAdapterHelper$v5cwP_i_1geBZNEja0OzJuPoFoM implements Runnable {
   // $FF: synthetic field
   private final SearchAdapterHelper f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final String f$4;
   // $FF: synthetic field
   private final boolean f$5;

   // $FF: synthetic method
   public _$$Lambda$SearchAdapterHelper$v5cwP_i_1geBZNEja0OzJuPoFoM(SearchAdapterHelper var1, int var2, TLRPC.TL_error var3, TLObject var4, String var5, boolean var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$0$SearchAdapterHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
