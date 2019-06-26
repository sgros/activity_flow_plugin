package com.google.android.exoplayer2.drm;

public class DecryptionException extends Exception {
   public final int errorCode;

   public DecryptionException(int var1, String var2) {
      super(var2);
      this.errorCode = var1;
   }
}
