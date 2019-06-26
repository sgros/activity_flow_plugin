package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$QveLJ1Gqcvj_9l_dGSaDY1G3t6s implements Runnable {
   // $FF: synthetic field
   private final FileLoader f$0;
   // $FF: synthetic field
   private final TLRPC.Document f$1;
   // $FF: synthetic field
   private final int f$10;
   // $FF: synthetic field
   private final SecureDocument f$2;
   // $FF: synthetic field
   private final WebFile f$3;
   // $FF: synthetic field
   private final TLRPC.TL_fileLocationToBeDeprecated f$4;
   // $FF: synthetic field
   private final ImageLocation f$5;
   // $FF: synthetic field
   private final Object f$6;
   // $FF: synthetic field
   private final String f$7;
   // $FF: synthetic field
   private final int f$8;
   // $FF: synthetic field
   private final int f$9;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$QveLJ1Gqcvj_9l_dGSaDY1G3t6s(FileLoader var1, TLRPC.Document var2, SecureDocument var3, WebFile var4, TLRPC.TL_fileLocationToBeDeprecated var5, ImageLocation var6, Object var7, String var8, int var9, int var10, int var11) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
      this.f$10 = var11;
   }

   public final void run() {
      this.f$0.lambda$loadFile$7$FileLoader(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10);
   }
}
