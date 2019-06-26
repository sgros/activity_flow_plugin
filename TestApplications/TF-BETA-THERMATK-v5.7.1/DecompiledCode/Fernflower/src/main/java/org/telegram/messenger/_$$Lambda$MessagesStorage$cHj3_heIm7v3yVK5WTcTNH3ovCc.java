package org.telegram.messenger;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$cHj3_heIm7v3yVK5WTcTNH3ovCc implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final Integer f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$cHj3_heIm7v3yVK5WTcTNH3ovCc(MessagesStorage var1, long var2, Integer var4, int var5, int var6, int var7) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
   }

   public final void run() {
      this.f$0.lambda$updateMessageStateAndId$127$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
