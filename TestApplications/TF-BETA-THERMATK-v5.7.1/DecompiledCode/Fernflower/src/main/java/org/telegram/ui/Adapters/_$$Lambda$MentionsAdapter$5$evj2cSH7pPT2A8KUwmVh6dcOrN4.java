package org.telegram.ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4 implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final SparseArray f$3;
   // $FF: synthetic field
   private final TLRPC.TL_error f$4;
   // $FF: synthetic field
   private final TLObject f$5;
   // $FF: synthetic field
   private final MessagesController f$6;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$5$evj2cSH7pPT2A8KUwmVh6dcOrN4(Object var1, int var2, ArrayList var3, SparseArray var4, TLRPC.TL_error var5, TLObject var6, MessagesController var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$0$MentionsAdapter$5(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
