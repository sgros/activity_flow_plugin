package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
   private final Headers headers;
   private final BufferedSource source;

   public RealResponseBody(Headers var1, BufferedSource var2) {
      this.headers = var1;
      this.source = var2;
   }

   public long contentLength() {
      return HttpHeaders.contentLength(this.headers);
   }

   public MediaType contentType() {
      String var1 = this.headers.get("Content-Type");
      MediaType var2;
      if (var1 != null) {
         var2 = MediaType.parse(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public BufferedSource source() {
      return this.source;
   }
}
