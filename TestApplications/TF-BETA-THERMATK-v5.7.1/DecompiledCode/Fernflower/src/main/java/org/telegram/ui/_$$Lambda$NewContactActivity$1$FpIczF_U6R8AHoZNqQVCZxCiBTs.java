package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_importedContacts f$1;
   // $FF: synthetic field
   private final TLRPC.TL_inputPhoneContact f$2;
   // $FF: synthetic field
   private final TLRPC.TL_error f$3;
   // $FF: synthetic field
   private final TLRPC.TL_contacts_importContacts f$4;

   // $FF: synthetic method
   public _$$Lambda$NewContactActivity$1$FpIczF_U6R8AHoZNqQVCZxCiBTs(Object var1, TLRPC.TL_contacts_importedContacts var2, TLRPC.TL_inputPhoneContact var3, TLRPC.TL_error var4, TLRPC.TL_contacts_importContacts var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$null$1$NewContactActivity$1(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
