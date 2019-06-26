package org.telegram.messenger;

import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$ImageLoader$3$_9uOaZVxDmXjnClpJdro4iTil8Q implements Runnable {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final int f$1;
   // $FF: synthetic field
   private final String f$2;
   // $FF: synthetic field
   private final TLRPC.InputFile f$3;
   // $FF: synthetic field
   private final TLRPC.InputEncryptedFile f$4;
   // $FF: synthetic field
   private final byte[] f$5;
   // $FF: synthetic field
   private final byte[] f$6;
   // $FF: synthetic field
   private final long f$7;

   // $FF: synthetic method
   public _$$Lambda$ImageLoader$3$_9uOaZVxDmXjnClpJdro4iTil8Q(Object var1, int var2, String var3, TLRPC.InputFile var4, TLRPC.InputEncryptedFile var5, byte[] var6, byte[] var7, long var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run() {
      this.f$0.lambda$fileDidUploaded$2$ImageLoader$3(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
