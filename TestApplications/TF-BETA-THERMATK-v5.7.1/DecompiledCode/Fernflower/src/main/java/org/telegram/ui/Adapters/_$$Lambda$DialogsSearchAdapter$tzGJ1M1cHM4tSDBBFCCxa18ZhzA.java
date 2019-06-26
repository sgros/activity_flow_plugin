package org.telegram.ui.Adapters;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DialogsSearchAdapter$tzGJ1M1cHM4tSDBBFCCxa18ZhzA implements Runnable {
   // $FF: synthetic field
   private final DialogsSearchAdapter f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final TLRPC.TL_messages_searchGlobal f$4;

   // $FF: synthetic method
   public _$$Lambda$DialogsSearchAdapter$tzGJ1M1cHM4tSDBBFCCxa18ZhzA(DialogsSearchAdapter var1, int var2, TLRPC.TL_error var3, TLObject var4, TLRPC.TL_messages_searchGlobal var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$0$DialogsSearchAdapter(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
