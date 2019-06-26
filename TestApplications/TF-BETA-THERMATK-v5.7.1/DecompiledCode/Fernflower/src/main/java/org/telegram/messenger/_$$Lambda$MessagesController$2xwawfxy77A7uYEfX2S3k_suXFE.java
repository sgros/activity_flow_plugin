package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$2xwawfxy77A7uYEfX2S3k_suXFE implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final TLRPC.TL_messages_peerDialogs f$4;
   // $FF: synthetic field
   private final LongSparseArray f$5;
   // $FF: synthetic field
   private final TLRPC.TL_messages_dialogs f$6;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$2xwawfxy77A7uYEfX2S3k_suXFE(MessagesController var1, int var2, ArrayList var3, boolean var4, TLRPC.TL_messages_peerDialogs var5, LongSparseArray var6, TLRPC.TL_messages_dialogs var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$null$221$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
