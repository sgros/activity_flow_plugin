package org.telegram.messenger;

import android.util.SparseArray;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$hjm8gMZUsX8pFMsciXhWnMP28rs implements Comparator {
   // $FF: synthetic field
   private final SparseArray f$0;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$hjm8gMZUsX8pFMsciXhWnMP28rs(SparseArray var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return ContactsController.lambda$null$28(this.f$0, (TLRPC.TL_contact)var1, (TLRPC.TL_contact)var2);
   }
}
