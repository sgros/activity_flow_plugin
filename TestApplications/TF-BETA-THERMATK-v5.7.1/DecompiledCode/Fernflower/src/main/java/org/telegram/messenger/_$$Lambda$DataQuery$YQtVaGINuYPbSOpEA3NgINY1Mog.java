package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog implements RequestDelegate {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_search f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final long f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final long f$6;
   // $FF: synthetic field
   private final TLRPC.User f$7;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog(DataQuery var1, int var2, TLRPC.TL_messages_search var3, long var4, long var6, int var8, long var9, TLRPC.User var11) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
      this.f$5 = var8;
      this.f$6 = var9;
      this.f$7 = var11;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$searchMessagesInChat$50$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, var1, var2);
   }
}
