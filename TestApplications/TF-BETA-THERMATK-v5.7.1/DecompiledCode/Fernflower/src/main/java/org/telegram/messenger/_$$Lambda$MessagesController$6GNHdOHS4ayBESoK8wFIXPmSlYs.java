package org.telegram.messenger;

import android.os.Bundle;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final AlertDialog f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final Bundle f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$6GNHdOHS4ayBESoK8wFIXPmSlYs(MessagesController var1, AlertDialog var2, BaseFragment var3, Bundle var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$checkCanOpenChat$257$MessagesController(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
