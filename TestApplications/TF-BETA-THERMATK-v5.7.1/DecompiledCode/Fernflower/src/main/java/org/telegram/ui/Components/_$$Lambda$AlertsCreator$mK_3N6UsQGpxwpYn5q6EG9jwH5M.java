package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesStorage;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$mK_3N6UsQGpxwpYn5q6EG9jwH5M implements OnClickListener {
   // $FF: synthetic field
   private final MessagesStorage.IntCallback f$0;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$mK_3N6UsQGpxwpYn5q6EG9jwH5M(MessagesStorage.IntCallback var1) {
      this.f$0 = var1;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createContactsPermissionDialog$30(this.f$0, var1, var2);
   }
}
