package org.telegram.ui.Adapters;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$4$ie2Kd71f_OJP1CoMCJ5ZnpnLA2g implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.TL_error f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final MessagesController f$4;
   // $FF: synthetic field
   private final MessagesStorage f$5;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$4$ie2Kd71f_OJP1CoMCJ5ZnpnLA2g(Object var1, String var2, TLRPC.TL_error var3, TLObject var4, MessagesController var5, MessagesStorage var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$0$MentionsAdapter$4(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
