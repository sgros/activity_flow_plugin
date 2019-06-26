package org.telegram.ui;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ProfileActivity$3$P_pKjyT3akZRyLfSf9io1zoClmw implements DialogsActivity.DialogsActivityDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.User f$1;

   // $FF: synthetic method
   public _$$Lambda$ProfileActivity$3$P_pKjyT3akZRyLfSf9io1zoClmw(Object var1, TLRPC.User var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void didSelectDialogs(DialogsActivity var1, ArrayList var2, CharSequence var3, boolean var4) {
      this.f$0.lambda$onItemClick$2$ProfileActivity$3(this.f$1, var1, var2, var3, var4);
   }
}
