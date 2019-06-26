package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$5K2sGZscq7bEKvLM0V2ywbk86iM implements Runnable {
   // $FF: synthetic field
   private final FileLoader f$0;
   // $FF: synthetic field
   private final TLRPC.Document f$1;
   // $FF: synthetic field
   private final boolean f$2;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$5K2sGZscq7bEKvLM0V2ywbk86iM(FileLoader var1, TLRPC.Document var2, boolean var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$setLoadingVideo$0$FileLoader(this.f$1, this.f$2);
   }
}
