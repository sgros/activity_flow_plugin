package org.telegram.messenger;

import org.telegram.tgnet.TLObject;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$7VZ_XJgDXdzbLiASsmURuo_C9GM implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final String f$2;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$7VZ_XJgDXdzbLiASsmURuo_C9GM(MessagesStorage var1, TLObject var2, String var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$saveBotCache$71$MessagesStorage(this.f$1, this.f$2);
   }
}
