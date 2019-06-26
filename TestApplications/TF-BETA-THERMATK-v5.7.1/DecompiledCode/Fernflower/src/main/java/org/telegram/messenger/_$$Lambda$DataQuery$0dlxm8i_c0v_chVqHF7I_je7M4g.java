package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$0dlxm8i_c0v_chVqHF7I_je7M4g implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Messages f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final boolean f$7;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$0dlxm8i_c0v_chVqHF7I_je7M4g(DataQuery var1, TLRPC.messages_Messages var2, int var3, long var4, ArrayList var6, int var7, int var8, boolean var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
   }

   public final void run() {
      this.f$0.lambda$processLoadedMedia$59$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
