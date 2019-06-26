package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$NewContactActivity$1$WRq0Ss_PBCngsAibqDEMoSm52R4 implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_inputPhoneContact f$1;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_importContacts f$2;

   // $FF: synthetic method
   public _$$Lambda$NewContactActivity$1$WRq0Ss_PBCngsAibqDEMoSm52R4(Object var1, TLRPC.TL_inputPhoneContact var2, TLRPC.TL_contacts_importContacts var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onItemClick$2$NewContactActivity$1(this.f$1, this.f$2, var1, var2);
   }
}
