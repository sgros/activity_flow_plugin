package okhttp3.internal.http2;

public enum ErrorCode {
   CANCEL(8),
   FLOW_CONTROL_ERROR(3),
   INTERNAL_ERROR(2),
   NO_ERROR(0),
   PROTOCOL_ERROR(1),
   REFUSED_STREAM(7);

   public final int httpCode;

   private ErrorCode(int var3) {
      this.httpCode = var3;
   }

   public static ErrorCode fromHttp2(int var0) {
      ErrorCode[] var1 = values();
      int var2 = var1.length;
      int var3 = 0;

      ErrorCode var4;
      while(true) {
         if (var3 >= var2) {
            var4 = null;
            break;
         }

         var4 = var1[var3];
         if (var4.httpCode == var0) {
            break;
         }

         ++var3;
      }

      return var4;
   }
}
