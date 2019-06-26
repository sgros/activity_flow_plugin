package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final TLRPC.TL_messages_search f$3;
   // $FF: synthetic field
   private final long f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final TLRPC.User f$7;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$eJFDqCNJQlVs4Vxl5xT4VYdEsgw(DataQuery var1, long var2, TLObject var4, TLRPC.TL_messages_search var5, long var6, int var8, int var9, TLRPC.User var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var8;
      this.f$6 = var9;
      this.f$7 = var10;
   }

   public final void run() {
      this.f$0.lambda$null$47$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
