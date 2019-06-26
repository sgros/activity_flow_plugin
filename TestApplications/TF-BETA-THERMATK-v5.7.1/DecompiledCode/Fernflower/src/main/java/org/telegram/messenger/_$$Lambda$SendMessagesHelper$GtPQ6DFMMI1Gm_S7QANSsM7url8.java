package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$GtPQ6DFMMI1Gm_S7QANSsM7url8 implements RequestDelegate {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final LongSparseArray f$4;
   // $FF: synthetic field
   private final ArrayList f$5;
   // $FF: synthetic field
   private final ArrayList f$6;
   // $FF: synthetic field
   private final TLRPC.Peer f$7;
   // $FF: synthetic field
   private final TLRPC.TL_messages_forwardMessages f$8;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$GtPQ6DFMMI1Gm_S7QANSsM7url8(SendMessagesHelper var1, long var2, boolean var4, boolean var5, LongSparseArray var6, ArrayList var7, ArrayList var8, TLRPC.Peer var9, TLRPC.TL_messages_forwardMessages var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
      this.f$8 = var10;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$sendMessage$9$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, var1, var2);
   }
}
