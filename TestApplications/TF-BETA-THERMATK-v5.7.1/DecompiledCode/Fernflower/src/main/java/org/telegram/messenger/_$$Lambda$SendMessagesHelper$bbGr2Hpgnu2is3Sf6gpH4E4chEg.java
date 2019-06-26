package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg implements Runnable {
   // $FF: synthetic field
   private final MessageObject f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_photo f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final SendMessagesHelper.SendingMediaInfo f$4;
   // $FF: synthetic field
   private final HashMap f$5;
   // $FF: synthetic field
   private final String f$6;
   // $FF: synthetic field
   private final long f$7;
   // $FF: synthetic field
   private final MessageObject f$8;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg(MessageObject var1, int var2, TLRPC.TL_photo var3, boolean var4, SendMessagesHelper.SendingMediaInfo var5, HashMap var6, String var7, long var8, MessageObject var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var10;
   }

   public final void run() {
      SendMessagesHelper.lambda$null$59(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
   }
}
