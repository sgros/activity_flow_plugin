package org.telegram.ui.Adapters;

import java.util.Comparator;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsAdapter$AjIuF4bNE_A90essgyL0wfJ8HaU implements Comparator {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsAdapter$AjIuF4bNE_A90essgyL0wfJ8HaU(MessagesController var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final int compare(Object var1, Object var2) {
      return ContactsAdapter.lambda$sortOnlineContacts$0(this.f$0, this.f$1, (TLRPC.TL_contact)var1, (TLRPC.TL_contact)var2);
   }
}
