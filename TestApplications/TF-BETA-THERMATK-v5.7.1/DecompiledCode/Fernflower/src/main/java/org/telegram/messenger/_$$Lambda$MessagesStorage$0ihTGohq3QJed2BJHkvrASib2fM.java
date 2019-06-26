package org.telegram.messenger;

import org.telegram.tgnet.TLObject;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$0ihTGohq3QJed2BJHkvrASib2fM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLObject f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final String f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$0ihTGohq3QJed2BJHkvrASib2fM(MessagesStorage var1, String var2, TLObject var3, int var4, String var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$putSentFile$103$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
