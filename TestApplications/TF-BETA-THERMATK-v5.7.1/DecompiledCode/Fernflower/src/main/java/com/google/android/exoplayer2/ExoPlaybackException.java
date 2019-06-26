package com.google.android.exoplayer2;

import java.io.IOException;

public final class ExoPlaybackException extends Exception {
   private final Throwable cause;
   public final int rendererIndex;
   public final int type;

   private ExoPlaybackException(int var1, Throwable var2, int var3) {
      super(var2);
      this.type = var1;
      this.cause = var2;
      this.rendererIndex = var3;
   }

   public static ExoPlaybackException createForRenderer(Exception var0, int var1) {
      return new ExoPlaybackException(1, var0, var1);
   }

   public static ExoPlaybackException createForSource(IOException var0) {
      return new ExoPlaybackException(0, var0, -1);
   }

   static ExoPlaybackException createForUnexpected(RuntimeException var0) {
      return new ExoPlaybackException(2, var0, -1);
   }
}
