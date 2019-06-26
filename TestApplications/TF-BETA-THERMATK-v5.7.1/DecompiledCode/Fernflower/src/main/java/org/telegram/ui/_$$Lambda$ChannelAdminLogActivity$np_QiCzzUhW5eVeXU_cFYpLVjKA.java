package org.telegram.ui;

import android.util.SparseArray;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.Components.AdminLogFilterAlert;

// $FF: synthetic class
public final class _$$Lambda$ChannelAdminLogActivity$np_QiCzzUhW5eVeXU_cFYpLVjKA implements AdminLogFilterAlert.AdminLogFilterAlertDelegate {
   // $FF: synthetic field
   private final ChannelAdminLogActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChannelAdminLogActivity$np_QiCzzUhW5eVeXU_cFYpLVjKA(ChannelAdminLogActivity var1) {
      this.f$0 = var1;
   }

   public final void didSelectRights(TLRPC.TL_channelAdminLogEventsFilter var1, SparseArray var2) {
      this.f$0.lambda$null$4$ChannelAdminLogActivity(var1, var2);
   }
}
