package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$6WXJc4YH8LQTBuHben4e3iABxOY implements PollCreateActivity.PollCreateActivityDelegate {
   // $FF: synthetic field
   private final ChatActivity f$0;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$6WXJc4YH8LQTBuHben4e3iABxOY(ChatActivity var1) {
      this.f$0 = var1;
   }

   public final void sendPoll(TLRPC.TL_messageMediaPoll var1) {
      this.f$0.lambda$processSelectedAttach$44$ChatActivity(var1);
   }
}
