package org.telegram.messenger;

import android.util.LongSparseArray;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$Rsj_LL8Uz4Bh77DLk8Q7Jiw37h4 implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final TLRPC.messages_Dialogs f$1;
   // $FF: synthetic field
   private final LongSparseArray f$10;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final TLRPC.Message f$7;
   // $FF: synthetic field
   private final int f$8;
   // $FF: synthetic field
   private final LongSparseArray f$9;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$Rsj_LL8Uz4Bh77DLk8Q7Jiw37h4(MessagesStorage var1, TLRPC.messages_Dialogs var2, int var3, int var4, int var5, int var6, int var7, TLRPC.Message var8, int var9, LongSparseArray var10, LongSparseArray var11) {
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
      this.f$10 = var11;
   }

   public final void run() {
      this.f$0.lambda$resetDialogs$52$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10);
   }
}
