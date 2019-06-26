package org.telegram.messenger;

import android.util.SparseArray;
import android.util.SparseIntArray;
import java.util.ArrayList;
import org.telegram.messenger.support.SparseLongArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ZCTDymAyINyEW9FoTNxhmaMSvzk implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final SparseLongArray f$1;
   // $FF: synthetic field
   private final SparseLongArray f$2;
   // $FF: synthetic field
   private final SparseIntArray f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final SparseArray f$5;
   // $FF: synthetic field
   private final SparseIntArray f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ZCTDymAyINyEW9FoTNxhmaMSvzk(MessagesController var1, SparseLongArray var2, SparseLongArray var3, SparseIntArray var4, ArrayList var5, SparseArray var6, SparseIntArray var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$processUpdateArray$252$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
