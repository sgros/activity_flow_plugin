package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$8jHuChSQV9WMksUcSKrM56MxPqE(ContactsController var1, ArrayList var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$applyContactsUpdates$46$ContactsController(this.f$1, this.f$2);
   }
}
