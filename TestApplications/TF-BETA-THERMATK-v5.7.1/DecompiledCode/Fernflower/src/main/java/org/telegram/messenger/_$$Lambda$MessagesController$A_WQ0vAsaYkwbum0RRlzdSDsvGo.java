package org.telegram.messenger;

import org.telegram.messenger.-..Lambda.MessagesController.A-WQ0vAsaYkwbum0RRlzdSDsvGo;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$A_WQ0vAsaYkwbum0RRlzdSDsvGo implements RequestDelegate {
   // $FF: synthetic field
   public static final A-WQ0vAsaYkwbum0RRlzdSDsvGo INSTANCE = new _$$Lambda$MessagesController$A_WQ0vAsaYkwbum0RRlzdSDsvGo();

   // $FF: synthetic method
   private _$$Lambda$MessagesController$A_WQ0vAsaYkwbum0RRlzdSDsvGo() {
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      MessagesController.lambda$reportSpam$25(var1, var2);
   }
}
