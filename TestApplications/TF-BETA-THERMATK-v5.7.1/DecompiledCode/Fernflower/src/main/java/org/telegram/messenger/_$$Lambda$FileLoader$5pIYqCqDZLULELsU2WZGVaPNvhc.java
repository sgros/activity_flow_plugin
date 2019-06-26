package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$5pIYqCqDZLULELsU2WZGVaPNvhc implements Runnable {
   // $FF: synthetic field
   private final FileLoader f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final TLRPC.Document f$2;
   // $FF: synthetic field
   private final WebFile f$3;
   // $FF: synthetic field
   private final SecureDocument f$4;
   // $FF: synthetic field
   private final TLRPC.FileLocation f$5;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$5pIYqCqDZLULELsU2WZGVaPNvhc(FileLoader var1, String var2, TLRPC.Document var3, WebFile var4, SecureDocument var5, TLRPC.FileLocation var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$cancelLoadFile$6$FileLoader(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
