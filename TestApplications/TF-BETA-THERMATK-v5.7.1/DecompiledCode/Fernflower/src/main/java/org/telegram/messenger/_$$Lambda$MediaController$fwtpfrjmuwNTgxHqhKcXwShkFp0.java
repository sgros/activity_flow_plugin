package org.telegram.messenger;

import java.io.File;

// $FF: synthetic class
public final class _$$Lambda$MediaController$fwtpfrjmuwNTgxHqhKcXwShkFp0 implements Runnable {
   // $FF: synthetic field
   private final MediaController f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final File f$4;
   // $FF: synthetic field
   private final boolean f$5;
   // $FF: synthetic field
   private final long f$6;

   // $FF: synthetic method
   public _$$Lambda$MediaController$fwtpfrjmuwNTgxHqhKcXwShkFp0(MediaController var1, boolean var2, boolean var3, MessageObject var4, File var5, boolean var6, long var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
   }

   public final void run() {
      this.f$0.lambda$didWriteData$30$MediaController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
   }
}
