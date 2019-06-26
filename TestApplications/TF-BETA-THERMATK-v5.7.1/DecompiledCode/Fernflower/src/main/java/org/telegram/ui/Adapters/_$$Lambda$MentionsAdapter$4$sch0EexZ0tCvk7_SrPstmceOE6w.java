package org.telegram.ui.Adapters;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7_SrPstmceOE6w implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final MessagesController f$2;
   // $FF: synthetic field
   private final MessagesStorage f$3;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7_SrPstmceOE6w(Object var1, String var2, MessagesController var3, MessagesStorage var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$1$MentionsAdapter$4(this.f$1, this.f$2, this.f$3, var1, var2);
   }
}
