package org.telegram.messenger;

import android.service.media.MediaBrowserService.Result;

// $FF: synthetic class
public final class _$$Lambda$MusicBrowserService$iS7bPWX5pXtbCNrWxnzS_j3JBYQ implements Runnable {
   // $FF: synthetic field
   private final MusicBrowserService f$0;
   // $FF: synthetic field
   private final MessagesStorage f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final Result f$3;

   // $FF: synthetic method
   public _$$Lambda$MusicBrowserService$iS7bPWX5pXtbCNrWxnzS_j3JBYQ(MusicBrowserService var1, MessagesStorage var2, String var3, Result var4) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
   }

   public final void run() {
      this.f$0.lambda$onLoadChildren$1$MusicBrowserService(this.f$1, this.f$2, this.f$3);
   }
}
