package org.telegram.messenger;

import java.io.File;
import org.telegram.ui.ActionBar.AlertDialog;

// $FF: synthetic class
public final class _$$Lambda$MediaController$nx3Q4nKr4qmGQfXNSzGCKTPGTMo implements Runnable {
   // $FF: synthetic field
   private final int f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final File f$2;
   // $FF: synthetic field
   private final boolean[] f$3;
   // $FF: synthetic field
   private final AlertDialog f$4;
   // $FF: synthetic field
   private final String f$5;

   // $FF: synthetic method
   public _$$Lambda$MediaController$nx3Q4nKr4qmGQfXNSzGCKTPGTMo(int var1, String var2, File var3, boolean[] var4, AlertDialog var5, String var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      MediaController.lambda$saveFile$26(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
