package org.telegram.messenger;

import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$oWtR0AbMdP04znHWUTItf_tR4Wc implements Runnable {
   // $FF: synthetic field
   private final MessageObject f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final TLRPC.TL_document f$2;
   // $FF: synthetic field
   private final String f$3;
   // $FF: synthetic field
   private final HashMap f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final long f$6;
   // $FF: synthetic field
   private final MessageObject f$7;
   // $FF: synthetic field
   private final String f$8;
   // $FF: synthetic field
   private final ArrayList f$9;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$oWtR0AbMdP04znHWUTItf_tR4Wc(MessageObject var1, int var2, TLRPC.TL_document var3, String var4, HashMap var5, String var6, long var7, MessageObject var9, String var10, ArrayList var11) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var9;
      this.f$8 = var10;
      this.f$9 = var11;
   }

   public final void run() {
      SendMessagesHelper.lambda$prepareSendingDocumentInternal$44(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
   }
}
