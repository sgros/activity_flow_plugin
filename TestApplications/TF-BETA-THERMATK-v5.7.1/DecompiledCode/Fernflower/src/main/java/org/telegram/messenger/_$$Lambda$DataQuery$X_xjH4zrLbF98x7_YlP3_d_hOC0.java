package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$X_xjH4zrLbF98x7_YlP3_d_hOC0 implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_search f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final TLRPC.User f$6;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$X_xjH4zrLbF98x7_YlP3_d_hOC0(DataQuery var1, long var2, TLRPC.TL_messages_search var4, long var5, int var7, int var8, TLRPC.User var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var7;
      this.f$5 = var8;
      this.f$6 = var9;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchMessagesInChat$48$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, var1, var2);
   }
}
