package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$qjcWF_Rlu499xbvvlZfzwoJxsQI implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final BaseFragment f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$qjcWF_Rlu499xbvvlZfzwoJxsQI(DataQuery var1, int var2, int var3, BaseFragment var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$removeStickersSet$44$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
