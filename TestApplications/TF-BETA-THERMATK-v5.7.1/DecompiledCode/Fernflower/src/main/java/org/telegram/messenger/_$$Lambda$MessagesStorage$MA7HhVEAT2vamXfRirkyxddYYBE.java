package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final int f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$MA7HhVEAT2vamXfRirkyxddYYBE(MessagesStorage var1, ArrayList var2, ArrayList var3, int var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$updateDialogsWithDeletedMessages$133$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
