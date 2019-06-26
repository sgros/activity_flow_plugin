package com.google.android.exoplayer2;

public final class IllegalSeekPositionException extends IllegalStateException {
   public final long positionMs;
   public final Timeline timeline;
   public final int windowIndex;

   public IllegalSeekPositionException(Timeline var1, int var2, long var3) {
      this.timeline = var1;
      this.windowIndex = var2;
      this.positionMs = var3;
   }
}
