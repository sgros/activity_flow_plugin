package org.telegram.messenger;

import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$l3XEyiXk02DazId_mQdpRpST3Co implements Comparator {
   // $FF: synthetic field
   private final ContactsController f$0;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$l3XEyiXk02DazId_mQdpRpST3Co(ContactsController var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return this.f$0.lambda$buildContactsSectionsArrays$41$ContactsController((TLRPC.TL_contact)var1, (TLRPC.TL_contact)var2);
   }
}
