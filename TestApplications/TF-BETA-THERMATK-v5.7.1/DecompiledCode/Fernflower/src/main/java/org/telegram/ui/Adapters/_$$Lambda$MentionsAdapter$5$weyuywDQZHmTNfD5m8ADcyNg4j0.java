package org.telegram.ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0 implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final SparseArray f$3;
   // $FF: synthetic field
   private final MessagesController f$4;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$5$weyuywDQZHmTNfD5m8ADcyNg4j0(Object var1, int var2, ArrayList var3, SparseArray var4, MessagesController var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$run$1$MentionsAdapter$5(this.f$1, this.f$2, this.f$3, this.f$4, var1, var2);
   }
}
