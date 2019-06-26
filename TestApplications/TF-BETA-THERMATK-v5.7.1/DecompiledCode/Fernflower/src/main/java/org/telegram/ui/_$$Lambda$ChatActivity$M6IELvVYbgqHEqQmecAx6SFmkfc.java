package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChatActivity$M6IELvVYbgqHEqQmecAx6SFmkfc implements Runnable {
   // $FF: synthetic field
   private final ChatActivity f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final TLRPC.TL_messages_editMessage f$2;

   // $FF: synthetic method
   public _$$Lambda$ChatActivity$M6IELvVYbgqHEqQmecAx6SFmkfc(ChatActivity var1, TLRPC.TL_error var2, TLRPC.TL_messages_editMessage var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$null$77$ChatActivity(this.f$1, this.f$2);
   }
}
