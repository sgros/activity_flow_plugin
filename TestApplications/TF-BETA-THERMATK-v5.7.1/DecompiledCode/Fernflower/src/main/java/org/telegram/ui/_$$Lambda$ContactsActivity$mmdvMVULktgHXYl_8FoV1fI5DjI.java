package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ContactsActivity$mmdvMVULktgHXYl_8FoV1fI5DjI implements OnClickListener {
   // $FF: synthetic field
   private final ContactsActivity f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$ContactsActivity$mmdvMVULktgHXYl_8FoV1fI5DjI(ContactsActivity var1, TLRPC.User var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$didSelectResult$3$ContactsActivity(this.f$1, this.f$2, var1, var2);
   }
}
