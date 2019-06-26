package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$GroupCreateFinalActivity$NmRkjRZ6_MrsxxvtnDOrMWsjyWE implements Runnable {
   // $FF: synthetic field
   private final GroupCreateFinalActivity f$0;
   // $FF: synthetic field
   private final TLRPC.InputFile f$1;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$2;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$3;

   // $FF: synthetic method
   public _$$Lambda$GroupCreateFinalActivity$NmRkjRZ6_MrsxxvtnDOrMWsjyWE(GroupCreateFinalActivity var1, TLRPC.InputFile var2, TLRPC.PhotoSize var3, TLRPC.PhotoSize var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$didUploadPhoto$5$GroupCreateFinalActivity(this.f$1, this.f$2, this.f$3);
   }
}
