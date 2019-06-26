package com.google.android.exoplayer2.upstream;

import java.io.IOException;

public final class DataSourceException extends IOException {
   public final int reason;

   public DataSourceException(int var1) {
      this.reason = var1;
   }
}
