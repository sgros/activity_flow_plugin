package org.telegram.messenger;

import android.util.LongSparseArray;
import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$aIzxVN1fNX7ISomIdQom833zn_c implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final LongSparseArray f$3;
   // $FF: synthetic field
   private final LongSparseArray f$4;
   // $FF: synthetic field
   private final LongSparseArray f$5;
   // $FF: synthetic field
   private final boolean f$6;
   // $FF: synthetic field
   private final ArrayList f$7;
   // $FF: synthetic field
   private final ArrayList f$8;
   // $FF: synthetic field
   private final SparseArray f$9;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$aIzxVN1fNX7ISomIdQom833zn_c(MessagesController var1, int var2, ArrayList var3, LongSparseArray var4, LongSparseArray var5, LongSparseArray var6, boolean var7, ArrayList var8, ArrayList var9, SparseArray var10) {
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
   }

   public final void run() {
      this.f$0.lambda$processUpdateArray$250$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
   }
}
