package org.telegram.messenger;

import java.util.concurrent.CountDownLatch;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$DMcEynaB8xpg04Y8QygQNaKjBb4 implements Runnable {
   // $FF: synthetic field
   private final FileLoader f$0;
   // $FF: synthetic field
   private final FileLoadOperation[] f$1;
   // $FF: synthetic field
   private final TLRPC.Document f$2;
   // $FF: synthetic field
   private final Object f$3;
   // $FF: synthetic field
   private final FileLoadOperationStream f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final CountDownLatch f$6;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$DMcEynaB8xpg04Y8QygQNaKjBb4(FileLoader var1, FileLoadOperation[] var2, TLRPC.Document var3, Object var4, FileLoadOperationStream var5, int var6, CountDownLatch var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$loadStreamFile$8$FileLoader(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
