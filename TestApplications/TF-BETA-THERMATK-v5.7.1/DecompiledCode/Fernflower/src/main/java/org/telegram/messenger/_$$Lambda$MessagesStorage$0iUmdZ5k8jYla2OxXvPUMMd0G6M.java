package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$0iUmdZ5k8jYla2OxXvPUMMd0G6M(MessagesStorage var1, ArrayList var2, int var3, int var4, int var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$createTaskForSecretChat$64$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
