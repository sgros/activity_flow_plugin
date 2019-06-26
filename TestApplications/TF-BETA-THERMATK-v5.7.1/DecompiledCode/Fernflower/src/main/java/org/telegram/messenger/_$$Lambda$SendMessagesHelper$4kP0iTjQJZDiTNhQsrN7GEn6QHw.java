package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw implements Runnable {
   // $FF: synthetic field
   private final ArrayList f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final MessageObject f$3;
   // $FF: synthetic field
   private final MessageObject f$4;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw(ArrayList var1, long var2, int var4, MessageObject var5, MessageObject var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
   }

   public final void run() {
      SendMessagesHelper.lambda$prepareSendingAudioDocuments$46(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
