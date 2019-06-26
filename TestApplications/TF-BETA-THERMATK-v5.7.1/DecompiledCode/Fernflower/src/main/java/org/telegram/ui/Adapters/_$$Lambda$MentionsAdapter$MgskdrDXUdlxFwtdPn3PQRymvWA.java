package org.telegram.ui.Adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA implements OnDismissListener {
   // $FF: synthetic field
   private final MentionsAdapter f$0;
   // $FF: synthetic field
   private final boolean[] f$1;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA(MentionsAdapter var1, boolean[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onDismiss(DialogInterface var1) {
      this.f$0.lambda$processFoundUser$2$MentionsAdapter(this.f$1, var1);
   }
}
