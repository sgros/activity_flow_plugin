package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileUploadOperation$XMcVvcrqfWd56m49RmvLW8_t4Os implements RequestDelegate {
   // $FF: synthetic field
   private final FileUploadOperation f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final byte[] f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final long f$7;
   // $FF: synthetic field
   private final TLObject f$8;

   // $FF: synthetic method
   public _$$Lambda$FileUploadOperation$XMcVvcrqfWd56m49RmvLW8_t4Os(FileUploadOperation var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, long var8, TLObject var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var10;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$startUploadRequest$4$FileUploadOperation(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, var1, var2);
   }
}
