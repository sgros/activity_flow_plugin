package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final int[] f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$vf0a3gbyExIJKu8Fv0SfXfbDaBc(MessagesController var1, TLRPC.messages_Dialogs var2, int var3, boolean var4, int[] var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$127$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
