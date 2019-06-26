package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$FileLoader$1$8qI8Hd84Wsy2NQjG71Z0jivjIew implements Runnable {
   // $FF: synthetic field
   private final FileLoader$1 f$0;
   // $FF: synthetic field
   private final boolean f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final boolean f$3;
   // $FF: synthetic field
   private final TLRPC.InputFile f$4;
   // $FF: synthetic field
   private final TLRPC.InputEncryptedFile f$5;
   // $FF: synthetic field
   private final byte[] f$6;
   // $FF: synthetic field
   private final byte[] f$7;
   // $FF: synthetic field
   private final FileUploadOperation f$8;

   // $FF: synthetic method
   public _$$Lambda$FileLoader$1$8qI8Hd84Wsy2NQjG71Z0jivjIew(FileLoader$1 var1, boolean var2, String var3, boolean var4, TLRPC.InputFile var5, TLRPC.InputEncryptedFile var6, byte[] var7, byte[] var8, FileUploadOperation var9) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
   }

   public final void run() {
      this.f$0.lambda$didFinishUploadingFile$0$FileLoader$1(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
   }
}
