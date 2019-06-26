package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$RwdHkk3sx1H7HCQZHIZAoujW4QQ implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.TL_wallPaper f$1;
   // $FF: synthetic field
   private final TLRPC.TL_wallPaperSettings f$2;
   // $FF: synthetic field
   private final File f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$RwdHkk3sx1H7HCQZHIZAoujW4QQ(MessagesController var1, TLRPC.TL_wallPaper var2, TLRPC.TL_wallPaperSettings var3, File var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$null$5$MessagesController(this.f$1, this.f$2, this.f$3);
   }
}
