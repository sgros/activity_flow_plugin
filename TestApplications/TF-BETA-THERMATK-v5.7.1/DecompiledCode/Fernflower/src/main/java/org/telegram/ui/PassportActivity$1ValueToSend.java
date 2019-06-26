package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

class PassportActivity$1ValueToSend {
   boolean selfie_required;
   // $FF: synthetic field
   final PassportActivity this$0;
   boolean translation_required;
   TLRPC.TL_secureValue value;

   public PassportActivity$1ValueToSend(PassportActivity var1, TLRPC.TL_secureValue var2, boolean var3, boolean var4) {
      this.this$0 = var1;
      this.value = var2;
      this.selfie_required = var3;
      this.translation_required = var4;
   }
}
