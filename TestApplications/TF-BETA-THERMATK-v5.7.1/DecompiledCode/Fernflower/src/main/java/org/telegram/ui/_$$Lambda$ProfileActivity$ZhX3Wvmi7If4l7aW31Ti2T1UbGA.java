package org.telegram.ui;

import android.view.View;
import android.view.View.OnClickListener;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA implements OnClickListener {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final TLRPC.Chat f$1;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$ZhX3Wvmi7If4l7aW31Ti2T1UbGA(ProfileActivity var1, TLRPC.Chat var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void onClick(View var1) {
      this.f$0.lambda$createView$10$ProfileActivity(this.f$1, var1);
   }
}
