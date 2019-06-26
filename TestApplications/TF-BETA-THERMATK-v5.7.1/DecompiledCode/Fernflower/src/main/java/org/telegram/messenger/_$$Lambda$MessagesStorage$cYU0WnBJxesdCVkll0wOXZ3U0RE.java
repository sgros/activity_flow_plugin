package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$cYU0WnBJxesdCVkll0wOXZ3U0RE implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final RequestDelegate f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$cYU0WnBJxesdCVkll0wOXZ3U0RE(MessagesStorage var1, int var2, String var3, RequestDelegate var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$getBotCache$72$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
