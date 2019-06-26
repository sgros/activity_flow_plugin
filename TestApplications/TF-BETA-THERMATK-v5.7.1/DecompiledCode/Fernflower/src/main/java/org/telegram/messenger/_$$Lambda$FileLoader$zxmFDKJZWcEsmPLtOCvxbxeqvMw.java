package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$zxmFDKJZWcEsmPLtOCvxbxeqvMw implements Runnable {
   // $FF: synthetic field
   private final FileLoader f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final TLRPC.Document f$3;
   // $FF: synthetic field
   private final WebFile f$4;
   // $FF: synthetic field
   private final TLRPC.FileLocation f$5;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$zxmFDKJZWcEsmPLtOCvxbxeqvMw(FileLoader var1, int var2, String var3, TLRPC.Document var4, WebFile var5, TLRPC.FileLocation var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$checkDownloadQueue$9$FileLoader(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
