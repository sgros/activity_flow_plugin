package com.google.android.exoplayer2.util;

import java.util.concurrent.ThreadFactory;

// $FF: synthetic class
public final class _$$Lambda$Util$MRC4FgxCpRGDforKj_F0m_7VaCA implements ThreadFactory {
   // $FF: synthetic field
   private final String f$0;

   // $FF: synthetic method
   public _$$Lambda$Util$MRC4FgxCpRGDforKj_F0m_7VaCA(String var1) {
      this.f$0 = var1;
   }

   public final Thread newThread(Runnable var1) {
      return Util.lambda$newSingleThreadExecutor$0(this.f$0, var1);
   }
}
