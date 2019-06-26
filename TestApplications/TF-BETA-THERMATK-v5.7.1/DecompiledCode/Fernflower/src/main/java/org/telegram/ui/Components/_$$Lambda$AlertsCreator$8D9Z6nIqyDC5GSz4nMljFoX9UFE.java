package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE implements OnClickListener {
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$0;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$8D9Z6nIqyDC5GSz4nMljFoX9UFE(MessagesStorage.IntCallback var1) {
      this.f$0 = var1;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createContactsPermissionDialog$31(this.f$0, var1, var2);
   }
}
