package com.google.android.exoplayer2.upstream;

import java.io.IOException;

public interface LoaderErrorThrower {
   void maybeThrowError() throws IOException;

   public static final class Dummy implements LoaderErrorThrower {
      public void maybeThrowError() throws IOException {
      }
   }
}
