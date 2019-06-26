package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$PassportActivity$hLNmBmiskobIF7R8TJhAQSbqU04 implements OnClickListener {
   // $FF: synthetic field
   private final PassportActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_auth_passwordRecovery f$1;

   // $FF: synthetic method
   public _$$Lambda$PassportActivity$hLNmBmiskobIF7R8TJhAQSbqU04(PassportActivity var1, TLRPC.TL_auth_passwordRecovery var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$8$PassportActivity(this.f$1, var1, var2);
   }
}
