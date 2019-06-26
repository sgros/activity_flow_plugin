package org.telegram.ui.Adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$3hBy3ePtH3hOR9uoZtzoQOcheTs implements OnClickListener {
   // $FF: synthetic field
   private final MentionsAdapter f$0;
   // $FF: synthetic field
   private final boolean[] f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$3hBy3ePtH3hOR9uoZtzoQOcheTs(MentionsAdapter var1, boolean[] var2, TLRPC.User var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$processFoundUser$0$MentionsAdapter(this.f$1, this.f$2, var1, var2);
   }
}
