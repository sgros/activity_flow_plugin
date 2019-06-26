package com.google.android.exoplayer2.upstream;

import android.net.Uri;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public final class ParsingLoadable implements Loader.Loadable {
   private final StatsDataSource dataSource;
   public final DataSpec dataSpec;
   private final ParsingLoadable.Parser parser;
   private volatile Object result;
   public final int type;

   public ParsingLoadable(DataSource var1, Uri var2, int var3, ParsingLoadable.Parser var4) {
      this(var1, new DataSpec(var2, 1), var3, var4);
   }

   public ParsingLoadable(DataSource var1, DataSpec var2, int var3, ParsingLoadable.Parser var4) {
      this.dataSource = new StatsDataSource(var1);
      this.dataSpec = var2;
      this.type = var3;
      this.parser = var4;
   }

   public long bytesLoaded() {
      return this.dataSource.getBytesRead();
   }

   public final void cancelLoad() {
   }

   public Map getResponseHeaders() {
      return this.dataSource.getLastResponseHeaders();
   }

   public final Object getResult() {
      return this.result;
   }

   public Uri getUri() {
      return this.dataSource.getLastOpenedUri();
   }

   public final void load() throws IOException {
      this.dataSource.resetBytesRead();
      DataSourceInputStream var1 = new DataSourceInputStream(this.dataSource, this.dataSpec);

      try {
         var1.open();
         Uri var2 = this.dataSource.getUri();
         Assertions.checkNotNull(var2);
         var2 = (Uri)var2;
         this.result = this.parser.parse(var2, var1);
      } finally {
         Util.closeQuietly((Closeable)var1);
      }

   }

   public interface Parser {
      Object parse(Uri var1, InputStream var2) throws IOException;
   }
}
