package org.telegram.ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.messenger.MessageObject;

// $FF: synthetic class
public final class _$$Lambda$PopupNotificationActivity$ox3mIPlvmBDmNDp_7DLxqyRSnLI implements OnClickListener {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final MessageObject f$1;

   // $FF: synthetic method
   public _$$Lambda$PopupNotificationActivity$ox3mIPlvmBDmNDp_7DLxqyRSnLI(int var1, MessageObject var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      PopupNotificationActivity.lambda$getButtonsViewForMessage$5(this.f$0, this.f$1, var1);
   }
}
