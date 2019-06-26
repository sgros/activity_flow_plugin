package org.telegram.messenger;

import android.graphics.Bitmap;
import java.util.HashMap;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$M7CzdagvPTvoV__4PfcXK58t2Pc implements Runnable {
   // $FF: synthetic field
   private final Bitmap[] f$0;
   // $FF: synthetic field
   private final String[] f$1;
   // $FF: synthetic field
   private final MessageObject f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final TLRPC.TL_photo f$4;
   // $FF: synthetic field
   private final HashMap f$5;
   // $FF: synthetic field
   private final String f$6;
   // $FF: synthetic field
   private final long f$7;
   // $FF: synthetic field
   private final MessageObject f$8;
   // $FF: synthetic field
   private final SendMessagesHelper.SendingMediaInfo f$9;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$M7CzdagvPTvoV__4PfcXK58t2Pc(Bitmap[] var1, String[] var2, MessageObject var3, int var4, TLRPC.TL_photo var5, HashMap var6, String var7, long var8, MessageObject var10, SendMessagesHelper.SendingMediaInfo var11) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var10;
      this.f$9 = var11;
   }

   public final void run() {
      SendMessagesHelper.lambda$null$61(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
   }
}
