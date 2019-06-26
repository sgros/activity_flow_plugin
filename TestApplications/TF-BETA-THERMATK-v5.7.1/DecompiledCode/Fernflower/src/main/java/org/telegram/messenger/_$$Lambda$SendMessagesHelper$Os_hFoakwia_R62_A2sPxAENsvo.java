package org.telegram.messenger;

import android.graphics.Bitmap;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$Os_hFoakwia_R62_A2sPxAENsvo implements Runnable {
   // $FF: synthetic field
   private final Bitmap f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final MessageObject f$10;
   // $FF: synthetic field
   private final SendMessagesHelper.SendingMediaInfo f$11;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final VideoEditedInfo f$4;
   // $FF: synthetic field
   private final TLRPC.TL_document f$5;
   // $FF: synthetic field
   private final String f$6;
   // $FF: synthetic field
   private final HashMap f$7;
   // $FF: synthetic field
   private final String f$8;
   // $FF: synthetic field
   private final long f$9;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$Os_hFoakwia_R62_A2sPxAENsvo(Bitmap var1, String var2, MessageObject var3, int var4, VideoEditedInfo var5, TLRPC.TL_document var6, String var7, HashMap var8, String var9, long var10, MessageObject var12, SendMessagesHelper.SendingMediaInfo var13) {
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
      this.f$10 = var12;
      this.f$11 = var13;
   }

   public final void run() {
      SendMessagesHelper.lambda$null$60(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11);
   }
}
