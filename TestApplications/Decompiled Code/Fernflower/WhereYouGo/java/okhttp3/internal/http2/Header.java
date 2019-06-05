package okhttp3.internal.http2;

import okhttp3.internal.Util;
import okio.ByteString;

public final class Header {
   public static final ByteString PSEUDO_PREFIX = ByteString.encodeUtf8(":");
   public static final ByteString RESPONSE_STATUS = ByteString.encodeUtf8(":status");
   public static final ByteString TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
   public static final ByteString TARGET_METHOD = ByteString.encodeUtf8(":method");
   public static final ByteString TARGET_PATH = ByteString.encodeUtf8(":path");
   public static final ByteString TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
   final int hpackSize;
   public final ByteString name;
   public final ByteString value;

   public Header(String var1, String var2) {
      this(ByteString.encodeUtf8(var1), ByteString.encodeUtf8(var2));
   }

   public Header(ByteString var1, String var2) {
      this(var1, ByteString.encodeUtf8(var2));
   }

   public Header(ByteString var1, ByteString var2) {
      this.name = var1;
      this.value = var2;
      this.hpackSize = var1.size() + 32 + var2.size();
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      boolean var3 = var2;
      if (var1 instanceof Header) {
         Header var4 = (Header)var1;
         var3 = var2;
         if (this.name.equals(var4.name)) {
            var3 = var2;
            if (this.value.equals(var4.value)) {
               var3 = true;
            }
         }
      }

      return var3;
   }

   public int hashCode() {
      return (this.name.hashCode() + 527) * 31 + this.value.hashCode();
   }

   public String toString() {
      return Util.format("%s: %s", this.name.utf8(), this.value.utf8());
   }
}
