package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatUsersActivity$VPgAUAFJXqsKcGEM9EFJ9hJ7L7M implements RequestDelegate {
   // $FF: synthetic field
   private final ChatUsersActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_channels_getParticipants f$1;

   // $FF: synthetic method
   public _$$Lambda$ChatUsersActivity$VPgAUAFJXqsKcGEM9EFJ9hJ7L7M(ChatUsersActivity var1, TLRPC.TL_channels_getParticipants var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadChatParticipants$21$ChatUsersActivity(this.f$1, var1, var2);
   }
}
