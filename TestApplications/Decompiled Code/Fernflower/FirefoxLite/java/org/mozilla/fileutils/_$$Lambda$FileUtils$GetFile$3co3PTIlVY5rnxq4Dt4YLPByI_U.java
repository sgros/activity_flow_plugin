package org.mozilla.fileutils;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

// $FF: synthetic class
public final class _$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U implements Callable {
   // $FF: synthetic field
   private final FileUtils.GetFile f$0;
   // $FF: synthetic field
   private final WeakReference f$1;

   // $FF: synthetic method
   public _$$Lambda$FileUtils$GetFile$3co3PTIlVY5rnxq4Dt4YLPByI_U(FileUtils.GetFile var1, WeakReference var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final Object call() {
      return FileUtils.GetFile.lambda$new$0(this.f$0, this.f$1);
   }
}
