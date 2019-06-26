package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.Comparator;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$k30A8i0EMAqCRpnk5v4RsWWhCoc implements Comparator {
   // $FF: synthetic field
   private final LongSparseArray f$0;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$k30A8i0EMAqCRpnk5v4RsWWhCoc(LongSparseArray var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return MessagesStorage.lambda$null$51(this.f$0, (Long)var1, (Long)var2);
   }
}
