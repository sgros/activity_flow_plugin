package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public final class StatsDataSource implements DataSource {
   private long bytesRead;
   private final DataSource dataSource;
   private Uri lastOpenedUri;
   private Map lastResponseHeaders;

   public StatsDataSource(DataSource var1) {
      Assertions.checkNotNull(var1);
      this.dataSource = (DataSource)var1;
      this.lastOpenedUri = Uri.EMPTY;
      this.lastResponseHeaders = Collections.emptyMap();
   }

   public void addTransferListener(TransferListener var1) {
      this.dataSource.addTransferListener(var1);
   }

   public void close() throws IOException {
      this.dataSource.close();
   }

   public long getBytesRead() {
      return this.bytesRead;
   }

   public Uri getLastOpenedUri() {
      return this.lastOpenedUri;
   }

   public Map getLastResponseHeaders() {
      return this.lastResponseHeaders;
   }

   public Map getResponseHeaders() {
      return this.dataSource.getResponseHeaders();
   }

   public Uri getUri() {
      return this.dataSource.getUri();
   }

   public long open(DataSpec var1) throws IOException {
      this.lastOpenedUri = var1.uri;
      this.lastResponseHeaders = Collections.emptyMap();
      long var2 = this.dataSource.open(var1);
      Uri var4 = this.getUri();
      Assertions.checkNotNull(var4);
      this.lastOpenedUri = (Uri)var4;
      this.lastResponseHeaders = this.getResponseHeaders();
      return var2;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      var2 = this.dataSource.read(var1, var2, var3);
      if (var2 != -1) {
         this.bytesRead += (long)var2;
      }

      return var2;
   }

   public void resetBytesRead() {
      this.bytesRead = 0L;
   }
}
