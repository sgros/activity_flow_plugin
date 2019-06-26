package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$ZJInZNgn5fe5N3COP4HchbUkZ4o implements Comparator {
   // $FF: synthetic field
   private final MessagesController f$0;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$ZJInZNgn5fe5N3COP4HchbUkZ4o(MessagesController var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return this.f$0.lambda$processUpdatesQueue$192$MessagesController((TLRPC.Updates)var1, (TLRPC.Updates)var2);
   }
}
