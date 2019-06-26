package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Predicate;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface HttpDataSource extends DataSource {
   Predicate REJECT_PAYWALL_TYPES = _$$Lambda$HttpDataSource$fz_i4cgBB9tTB1JUdq8hmlAPFIw.INSTANCE;

   public abstract static class BaseFactory implements HttpDataSource.Factory {
      private final HttpDataSource.RequestProperties defaultRequestProperties = new HttpDataSource.RequestProperties();

      public final HttpDataSource createDataSource() {
         return this.createDataSourceInternal(this.defaultRequestProperties);
      }

      protected abstract HttpDataSource createDataSourceInternal(HttpDataSource.RequestProperties var1);
   }

   public interface Factory extends DataSource.Factory {
   }

   public static class HttpDataSourceException extends IOException {
      public final DataSpec dataSpec;
      public final int type;

      public HttpDataSourceException(IOException var1, DataSpec var2, int var3) {
         super(var1);
         this.dataSpec = var2;
         this.type = var3;
      }

      public HttpDataSourceException(String var1, DataSpec var2, int var3) {
         super(var1);
         this.dataSpec = var2;
         this.type = var3;
      }

      public HttpDataSourceException(String var1, IOException var2, DataSpec var3, int var4) {
         super(var1, var2);
         this.dataSpec = var3;
         this.type = var4;
      }
   }

   public static final class InvalidContentTypeException extends HttpDataSource.HttpDataSourceException {
      public final String contentType;

      public InvalidContentTypeException(String var1, DataSpec var2) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Invalid content type: ");
         var3.append(var1);
         super((String)var3.toString(), var2, 1);
         this.contentType = var1;
      }
   }

   public static final class InvalidResponseCodeException extends HttpDataSource.HttpDataSourceException {
      public final Map headerFields;
      public final int responseCode;
      public final String responseMessage;

      public InvalidResponseCodeException(int var1, String var2, Map var3, DataSpec var4) {
         StringBuilder var5 = new StringBuilder();
         var5.append("Response code: ");
         var5.append(var1);
         super((String)var5.toString(), var4, 1);
         this.responseCode = var1;
         this.responseMessage = var2;
         this.headerFields = var3;
      }
   }

   public static final class RequestProperties {
      private final Map requestProperties = new HashMap();
      private Map requestPropertiesSnapshot;

      public Map getSnapshot() {
         synchronized(this){}

         Map var4;
         try {
            if (this.requestPropertiesSnapshot == null) {
               HashMap var1 = new HashMap(this.requestProperties);
               this.requestPropertiesSnapshot = Collections.unmodifiableMap(var1);
            }

            var4 = this.requestPropertiesSnapshot;
         } finally {
            ;
         }

         return var4;
      }
   }
}
