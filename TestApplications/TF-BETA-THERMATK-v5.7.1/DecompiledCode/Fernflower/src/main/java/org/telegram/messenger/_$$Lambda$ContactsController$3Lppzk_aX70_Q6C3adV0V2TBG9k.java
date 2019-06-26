package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$3Lppzk_aX70_Q6C3adV0V2TBG9k implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_importedContacts f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$3Lppzk_aX70_Q6C3adV0V2TBG9k(ContactsController var1, TLRPC.TL_contacts_importedContacts var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$null$49$ContactsController(this.f$1);
   }
}
