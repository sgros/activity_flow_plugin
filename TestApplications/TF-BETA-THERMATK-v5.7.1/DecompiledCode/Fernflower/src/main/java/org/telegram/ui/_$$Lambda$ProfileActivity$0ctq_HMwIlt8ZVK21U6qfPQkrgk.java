package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$0ctq_HMwIlt8ZVK21U6qfPQkrgk implements RequestDelegate {
   // $FF: synthetic field
   private final ProfileActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_channels_getParticipants f$1;
   // $FF: synthetic field
   private final int f$2;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$0ctq_HMwIlt8ZVK21U6qfPQkrgk(ProfileActivity var1, TLRPC.TL_channels_getParticipants var2, int var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$getChannelParticipants$19$ProfileActivity(this.f$1, this.f$2, var1, var2);
   }
}
