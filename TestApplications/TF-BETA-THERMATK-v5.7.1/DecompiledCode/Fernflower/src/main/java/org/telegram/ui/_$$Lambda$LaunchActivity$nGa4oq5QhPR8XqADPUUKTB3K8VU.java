package org.telegram.ui;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU implements RequestDelegate {
   // $FF: synthetic field
   private final LaunchActivity f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final Bundle f$4;

   // $FF: synthetic method
   public _$$Lambda$LaunchActivity$nGa4oq5QhPR8XqADPUUKTB3K8VU(LaunchActivity var1, AlertDialog var2, BaseFragment var3, int var4, Bundle var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$null$31$LaunchActivity(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
