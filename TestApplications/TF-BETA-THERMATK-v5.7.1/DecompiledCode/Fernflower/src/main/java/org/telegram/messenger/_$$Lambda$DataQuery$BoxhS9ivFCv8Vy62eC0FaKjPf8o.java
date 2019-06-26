package org.telegram.messenger;

import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$BoxhS9ivFCv8Vy62eC0FaKjPf8o implements Runnable {
   // $FF: synthetic field
   private final DataQuery f$0;
   // $FF: synthetic field
   private final StringBuilder f$1;
   // $FF: synthetic field
   private final long f$2;
   // $FF: synthetic field
   private final ArrayList f$3;
   // $FF: synthetic field
   private final SparseArray f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$BoxhS9ivFCv8Vy62eC0FaKjPf8o(DataQuery var1, StringBuilder var2, long var3, ArrayList var5, SparseArray var6, int var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
   }

   public final void run() {
      this.f$0.lambda$loadReplyMessagesForMessages$94$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
