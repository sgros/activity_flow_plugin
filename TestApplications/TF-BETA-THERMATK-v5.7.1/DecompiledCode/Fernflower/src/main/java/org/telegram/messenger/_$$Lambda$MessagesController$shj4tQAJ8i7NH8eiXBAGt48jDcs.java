package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$shj4tQAJ8i7NH8eiXBAGt48jDcs implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$3;
   // $FF: synthetic field
   private final boolean f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final ArrayList f$6;
   // $FF: synthetic field
   private final int f$7;
   // $FF: synthetic field
   private final boolean f$8;
   // $FF: synthetic field
   private final boolean f$9;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$shj4tQAJ8i7NH8eiXBAGt48jDcs(MessagesController var1, int var2, int var3, TLRPC.messages_Dialogs var4, boolean var5, int var6, ArrayList var7, int var8, boolean var9, boolean var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
   }

   public final void run() {
      this.f$0.lambda$processLoadedDialogs$129$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
   }
}
