package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$EZvJWephnINCisVyT9aioUlUdEQ implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$EZvJWephnINCisVyT9aioUlUdEQ(ContactsController var1, ArrayList var2, int var3, ArrayList var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$processLoadedContacts$35$ContactsController(this.f$1, this.f$2, this.f$3);
   }
}
