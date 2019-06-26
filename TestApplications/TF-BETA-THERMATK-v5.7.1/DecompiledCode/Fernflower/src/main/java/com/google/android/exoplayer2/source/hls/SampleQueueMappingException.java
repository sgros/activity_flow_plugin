package com.google.android.exoplayer2.source.hls;

import java.io.IOException;

public final class SampleQueueMappingException extends IOException {
   public SampleQueueMappingException(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("Unable to bind a sample queue to TrackGroup with mime type ");
      var2.append(var1);
      var2.append(".");
      super(var2.toString());
   }
}
