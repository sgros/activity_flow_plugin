package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$jdY_gZP_eY5WAh_nSc2UhUqpq4M implements Comparator {
   // $FF: synthetic field
   private final MessagesController f$0;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$jdY_gZP_eY5WAh_nSc2UhUqpq4M(MessagesController var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return this.f$0.lambda$new$0$MessagesController((TLRPC.Update)var1, (TLRPC.Update)var2);
   }
}
