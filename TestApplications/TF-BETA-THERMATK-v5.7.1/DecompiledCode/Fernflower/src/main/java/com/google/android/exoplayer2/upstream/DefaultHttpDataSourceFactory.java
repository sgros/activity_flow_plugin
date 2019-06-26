package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Predicate;

public final class DefaultHttpDataSourceFactory extends HttpDataSource.BaseFactory {
   private final boolean allowCrossProtocolRedirects;
   private final int connectTimeoutMillis;
   private final TransferListener listener;
   private final int readTimeoutMillis;
   private final String userAgent;

   public DefaultHttpDataSourceFactory(String var1, TransferListener var2) {
      this(var1, var2, 8000, 8000, false);
   }

   public DefaultHttpDataSourceFactory(String var1, TransferListener var2, int var3, int var4, boolean var5) {
      this.userAgent = var1;
      this.listener = var2;
      this.connectTimeoutMillis = var3;
      this.readTimeoutMillis = var4;
      this.allowCrossProtocolRedirects = var5;
   }

   protected DefaultHttpDataSource createDataSourceInternal(HttpDataSource.RequestProperties var1) {
      DefaultHttpDataSource var3 = new DefaultHttpDataSource(this.userAgent, (Predicate)null, this.connectTimeoutMillis, this.readTimeoutMillis, this.allowCrossProtocolRedirects, var1);
      TransferListener var2 = this.listener;
      if (var2 != null) {
         var3.addTransferListener(var2);
      }

      return var3;
   }
}
