package okhttp3;

import java.io.IOException;

public interface Interceptor {
   Response intercept(Interceptor.Chain var1) throws IOException;

   public interface Chain {
      Connection connection();

      Response proceed(Request var1) throws IOException;

      Request request();
   }
}
