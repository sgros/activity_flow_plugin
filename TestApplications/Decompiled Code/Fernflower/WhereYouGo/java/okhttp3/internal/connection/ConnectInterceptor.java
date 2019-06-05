package okhttp3.internal.connection;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.RealInterceptorChain;

public final class ConnectInterceptor implements Interceptor {
   public final OkHttpClient client;

   public ConnectInterceptor(OkHttpClient var1) {
      this.client = var1;
   }

   public Response intercept(Interceptor.Chain var1) throws IOException {
      RealInterceptorChain var2 = (RealInterceptorChain)var1;
      Request var5 = var2.request();
      StreamAllocation var3 = var2.streamAllocation();
      boolean var4;
      if (!var5.method().equals("GET")) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var2.proceed(var5, var3, var3.newStream(this.client, var4), var3.connection());
   }
}
