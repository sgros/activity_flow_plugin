package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$dK1QMDFhAiUhe4t4z2CkJFVPwJo(MessagesStorage var1, int var2, ArrayList var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$updateChannelUsers$70$MessagesStorage(this.f$1, this.f$2);
   }
}