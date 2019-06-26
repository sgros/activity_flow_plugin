package org.telegram.messenger;

import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$OMZI4TzcbS75iT0wAGlu_LDNynw implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final boolean f$10;
   // $FF: synthetic field
   private final int f$11;
   // $FF: synthetic field
   private final ArrayList f$12;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final LongSparseArray f$6;
   // $FF: synthetic field
   private final LongSparseArray f$7;
   // $FF: synthetic field
   private final SparseArray f$8;
   // $FF: synthetic field
   private final int f$9;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$OMZI4TzcbS75iT0wAGlu_LDNynw(MessagesController var1, int var2, TLRPC.messages_Dialogs var3, ArrayList var4, boolean var5, int var6, LongSparseArray var7, LongSparseArray var8, SparseArray var9, int var10, boolean var11, int var12, ArrayList var13) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
      this.f$10 = var11;
      this.f$11 = var12;
      this.f$12 = var13;
   }

   public final void run() {
      this.f$0.lambda$null$128$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12);
   }
}
