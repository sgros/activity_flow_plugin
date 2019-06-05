package okhttp3;

public enum TlsVersion {
   SSL_3_0("SSLv3"),
   TLS_1_0("TLSv1"),
   TLS_1_1("TLSv1.1"),
   TLS_1_2("TLSv1.2"),
   TLS_1_3("TLSv1.3");

   final String javaName;

   private TlsVersion(String var3) {
      this.javaName = var3;
   }

   public static TlsVersion forJavaName(String var0) {
      byte var1 = -1;
      switch(var0.hashCode()) {
      case -503070503:
         if (var0.equals("TLSv1.1")) {
            var1 = 2;
         }
         break;
      case -503070502:
         if (var0.equals("TLSv1.2")) {
            var1 = 1;
         }
         break;
      case -503070501:
         if (var0.equals("TLSv1.3")) {
            var1 = 0;
         }
         break;
      case 79201641:
         if (var0.equals("SSLv3")) {
            var1 = 4;
         }
         break;
      case 79923350:
         if (var0.equals("TLSv1")) {
            var1 = 3;
         }
      }

      TlsVersion var2;
      switch(var1) {
      case 0:
         var2 = TLS_1_3;
         break;
      case 1:
         var2 = TLS_1_2;
         break;
      case 2:
         var2 = TLS_1_1;
         break;
      case 3:
         var2 = TLS_1_0;
         break;
      case 4:
         var2 = SSL_3_0;
         break;
      default:
         throw new IllegalArgumentException("Unexpected TLS version: " + var0);
      }

      return var2;
   }

   public String javaName() {
      return this.javaName;
   }
}
