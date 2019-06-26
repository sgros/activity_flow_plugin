package org.telegram.ui.Adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb_uXrprc implements OnClickListener {
   // $FF: synthetic field
   private final MentionsAdapter f$0;
   // $FF: synthetic field
   private final boolean[] f$1;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$CT1DE5mOMJY5cACE3GAb_uXrprc(MentionsAdapter var1, boolean[] var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$processFoundUser$1$MentionsAdapter(this.f$1, var1, var2);
   }
}
