package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.messenger.support.SparseLongArray;

// $FF: synthetic class
public final class _$$Lambda$MessagesStorage$IgPbgy6_ebqi0So_m2IdijRNQZs implements Runnable {
   // $FF: synthetic field
   private final MessagesStorage f$0;
   // $FF: synthetic field
   private final SparseLongArray f$1;
   // $FF: synthetic field
   private final SparseLongArray f$2;
   // $FF: synthetic field
   private final ArrayList f$3;

   // $FF: synthetic method
   public _$$Lambda$MessagesStorage$IgPbgy6_ebqi0So_m2IdijRNQZs(MessagesStorage var1, SparseLongArray var2, SparseLongArray var3, ArrayList var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$updateDialogsWithReadMessages$65$MessagesStorage(this.f$1, this.f$2, this.f$3);
   }
}
