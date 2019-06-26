package org.telegram.messenger;

import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$vU3H4O_uMIIPLYpg8QUqZEOJanU implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final long f$4;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$vU3H4O_uMIIPLYpg8QUqZEOJanU(MessagesStorage var1, ArrayList var2, ArrayList var3, int var4, long var5) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
   }

   public final void run() {
      this.f$0.lambda$setDialogsFolderId$142$MessagesStorage(this.f$1, this.f$2, this.f$3, this.f$4);
   }
}
