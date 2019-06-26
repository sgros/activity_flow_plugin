package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$s0GgcDgh5GZ9Riv31NjWgQrzb_w implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final TLRPC.Updates f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$s0GgcDgh5GZ9Riv31NjWgQrzb_w(MessagesController var1, boolean var2, TLRPC.Updates var3, ArrayList var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$processUpdates$233$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
