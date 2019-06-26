package org.telegram.messenger;

import android.content.SharedPreferences.Editor;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$h5vCC_HpjKgEFkNX9o79KFdTfVk implements RequestDelegate {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final Editor f$1;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$h5vCC_HpjKgEFkNX9o79KFdTfVk(ContactsController var1, Editor var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$reloadContactsStatuses$55$ContactsController(this.f$1, var1, var2);
   }
}
