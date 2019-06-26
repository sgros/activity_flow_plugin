package org.telegram.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$StickersActivity$ListAdapter$4oHDK9bhePlEa0rc89WtPkp_42Q implements OnClickListener {
   // $FF: synthetic field
   private final StickersActivity.ListAdapter f$0;
   // $FF: synthetic field
   private final int[] f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_stickerSet f$2;

   // $FF: synthetic method
   public _$$Lambda$StickersActivity$ListAdapter$4oHDK9bhePlEa0rc89WtPkp_42Q(StickersActivity.ListAdapter var1, int[] var2, TLRPC.TL_messages_stickerSet var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void onClick(DialogInterface var1, int var2) {
      this.f$0.lambda$null$0$StickersActivity$ListAdapter(this.f$1, this.f$2, var1, var2);
   }
}
