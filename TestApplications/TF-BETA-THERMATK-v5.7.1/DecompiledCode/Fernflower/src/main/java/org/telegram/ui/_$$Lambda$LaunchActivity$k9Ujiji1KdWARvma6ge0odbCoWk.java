package org.telegram.ui;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk implements Runnable {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final int f$4;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$k9Ujiji1KdWARvma6ge0odbCoWk(LaunchActivity var1, AlertDialog var2, TLRPC.TL_error var3, TLObject var4, int var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$16$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}