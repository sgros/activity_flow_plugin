package org.telegram.messenger;

import android.util.SparseArray;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final SparseArray f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$BUiF7Jzba7DA47bmyOp4BvMiTjQ(ContactsController var1, SparseArray var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$migratePhoneBookToV7$11$ContactsController(this.f$1);
   }
}
