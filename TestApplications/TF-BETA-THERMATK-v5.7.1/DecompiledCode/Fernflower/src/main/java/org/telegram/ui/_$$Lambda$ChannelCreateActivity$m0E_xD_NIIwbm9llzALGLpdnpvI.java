package org.telegram.ui;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ChannelCreateActivity$m0E_xD_NIIwbm9llzALGLpdnpvI implements Runnable {
   // $FF: synthetic field
   private final ChannelCreateActivity f$0;
   // $FF: synthetic field
   private final TLRPC.InputFile f$1;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$2;
   // $FF: synthetic field
   private final TLRPC.PhotoSize f$3;

   // $FF: synthetic method
   public _$$Lambda$ChannelCreateActivity$m0E_xD_NIIwbm9llzALGLpdnpvI(ChannelCreateActivity var1, TLRPC.InputFile var2, TLRPC.PhotoSize var3, TLRPC.PhotoSize var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$didUploadPhoto$11$ChannelCreateActivity(this.f$1, this.f$2, this.f$3);
   }
}
