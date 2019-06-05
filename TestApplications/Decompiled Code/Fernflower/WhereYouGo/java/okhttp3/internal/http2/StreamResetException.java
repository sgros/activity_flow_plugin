package okhttp3.internal.http2;

import java.io.IOException;

public final class StreamResetException extends IOException {
   public final ErrorCode errorCode;

   public StreamResetException(ErrorCode var1) {
      super("stream was reset: " + var1);
      this.errorCode = var1;
   }
}
