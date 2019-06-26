package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$NAGNwZznFWBfaOyQvFbS7VcD9Ac implements Runnable {
   // $FF: synthetic field
   private final TLRPC.TL_document f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final long f$3;
   // $FF: synthetic field
   private final MessageObject f$4;
   // $FF: synthetic field
   private final TLRPC.BotInlineResult f$5;
   // $FF: synthetic field
   private final HashMap f$6;
   // $FF: synthetic field
   private final TLRPC.TL_photo f$7;
   // $FF: synthetic field
   private final TLRPC.TL_game f$8;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$NAGNwZznFWBfaOyQvFbS7VcD9Ac(TLRPC.TL_document var1, int var2, String var3, long var4, MessageObject var6, TLRPC.BotInlineResult var7, HashMap var8, TLRPC.TL_photo var9, TLRPC.TL_game var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
      this.f$8 = var10;
   }

   public final void run() {
      SendMessagesHelper.lambda$null$52(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
   }
}
