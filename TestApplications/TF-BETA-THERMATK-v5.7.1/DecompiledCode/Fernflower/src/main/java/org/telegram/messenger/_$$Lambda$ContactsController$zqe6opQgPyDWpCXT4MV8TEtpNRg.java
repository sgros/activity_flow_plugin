package org.telegram.messenger;

import java.util.HashMap;

// $FF: synthetic class
public final class _$$Lambda$ContactsController$zqe6opQgPyDWpCXT4MV8TEtpNRg implements Runnable {
   // $FF: synthetic field
   private final ContactsController f$0;
   // $FF: synthetic field
   private final HashMap f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final boolean f$4;

   // $FF: synthetic method
   public _$$Lambda$ContactsController$zqe6opQgPyDWpCXT4MV8TEtpNRg(ContactsController var1, HashMap var2, boolean var3, boolean var4, boolean var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$syncPhoneBookByAlert$6$ContactsController(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
