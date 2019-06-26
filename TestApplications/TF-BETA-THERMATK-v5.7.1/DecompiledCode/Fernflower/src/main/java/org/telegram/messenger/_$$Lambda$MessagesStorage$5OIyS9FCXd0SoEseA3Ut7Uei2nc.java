package org.telegram.messenger;

import android.util.SparseArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final SparseArray f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$5OIyS9FCXd0SoEseA3Ut7Uei2nc(MessagesStorage var1, SparseArray var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$putChannelViews$122$MessagesStorage(this.f$1, this.f$2);
   }
}
