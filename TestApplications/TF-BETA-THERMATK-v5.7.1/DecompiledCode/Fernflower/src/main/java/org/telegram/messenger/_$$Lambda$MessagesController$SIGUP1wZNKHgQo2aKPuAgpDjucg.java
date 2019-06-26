package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$SIGUP1wZNKHgQo2aKPuAgpDjucg implements OnCancelListener {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$SIGUP1wZNKHgQo2aKPuAgpDjucg(MessagesController var1, int var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onCancel(DialogInterface var1) {
      this.f$0.lambda$convertToMegaGroup$165$MessagesController(this.f$1, var1);
   }
}
