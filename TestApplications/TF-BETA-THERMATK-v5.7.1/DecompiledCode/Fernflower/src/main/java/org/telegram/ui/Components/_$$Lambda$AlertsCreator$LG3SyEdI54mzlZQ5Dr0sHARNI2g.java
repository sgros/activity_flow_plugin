package org.telegram.ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g implements OnClickListener {
   // $FF: synthetic field
   private final TLRPC.EncryptedChat f$0;
   // $FF: synthetic field
   private final NumberPicker f$1;

   // $FF: synthetic method
   public _$$Lambda$AlertsCreator$LG3SyEdI54mzlZQ5Dr0sHARNI2g(TLRPC.EncryptedChat var1, NumberPicker var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      AlertsCreator.lambda$createTTLAlert$39(this.f$0, this.f$1, var1, var2);
   }
}
