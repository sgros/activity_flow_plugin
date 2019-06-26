package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$5IM4wVI_k9G6IDitB8PxuFm_zIw implements OnClickListener {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final TLRPC.User f$2;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$5IM4wVI_k9G6IDitB8PxuFm_zIw(ProfileActivity var1, ArrayList var2, TLRPC.User var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$processOnClickOrPress$15$ProfileActivity(this.f$1, this.f$2, var1, var2);
   }
}
